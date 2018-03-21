package dev.olog.msc.glide

import android.content.Context
import android.net.wifi.WifiManager
import android.preference.PreferenceManager
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl
import dev.olog.msc.R
import dev.olog.msc.utils.k.extension.defer
import dev.olog.msc.utils.k.extension.unsubscribe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Singles
import java.io.InputStream
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

abstract class BaseRxDataFetcher(
        private val context: Context

) : DataFetcher<InputStream> {

    companion object {
        private const val TIMEOUT = 2500

        private var counter = AtomicLong(1)
        // NB: max 5 request per second
        private const val THRESHOLD = 600L
    }

    private var hasIncremented = false
    private var hasAlreadyDecremented = false
    protected var disposable: Disposable? = null

    override fun getDataClass(): Class<InputStream> = InputStream::class.java

    override fun getDataSource(): DataSource = DataSource.REMOTE

    override fun cleanup() {
        unsubscribe()
    }

    override fun cancel() {
        unsubscribe()
    }

    private fun unsubscribe(){
        disposable.unsubscribe()
        if (hasIncremented && !hasAlreadyDecremented) {
            counter.decrementAndGet()
        }
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        disposable = shouldFetch()
                .flatMap { should ->
                    val single = if (should){
                        delay()
                    } else Single.just(false)
                    // DO NOT DELETE DEFER
                    single.flatMap { execute(priority, callback).defer() }
                }.subscribe({ image ->
                    if (image.isNotBlank()){
                        networkSafeAction {
                            val urlFetcher = HttpUrlFetcher(GlideUrl(image), TIMEOUT)
                            urlFetcher.loadData(priority, callback)
                        }
                    } else {
                        callback.onLoadFailed(NoSuchElementException())
                    }
                }, {
                    it.printStackTrace()
                    callback.onLoadFailed(it as Exception)
                })
    }

    private fun delay(): Single<*>{
        val current = counter.incrementAndGet()
        hasIncremented = true

        return Singles.zip(
                Observable.timer(current * THRESHOLD, TimeUnit.MILLISECONDS)
                        .firstOrError()
                        .doOnEvent { _, _ ->
                            hasAlreadyDecremented = true
                            counter.decrementAndGet()
                        },
                Single.just(false), { _, _ -> false })
    }

    private fun isOnWifi(): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?

        return wifiManager?.isWifiEnabled ?: false &&
                wifiManager?.connectionInfo?.networkId != -1
    }

    private fun networkSafeAction(action: () -> Unit){
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val canDownloadOnMobile = prefs.getBoolean(context.getString(R.string.prefs_auto_download_images_key), false)

        val isWifiActive = isOnWifi()
        when {
            isWifiActive -> action()
            canDownloadOnMobile && !isWifiActive -> action()
        }
    }

    protected abstract fun execute(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>)
            : Single<String>

    protected abstract fun shouldFetch(): Single<Boolean>

}