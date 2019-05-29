package dev.olog.msc.app.injection

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import dev.olog.msc.apilastfm.LastFmModule
import dev.olog.msc.app.injection.viewmodel.ViewModelModule
import dev.olog.msc.appshortcuts.AppShortcutsModule
import dev.olog.msc.appwidgets.di.WidgetBindingModule
import dev.olog.msc.core.AppShortcuts
import dev.olog.msc.core.gateway.podcast.PodcastAlbumGateway
import dev.olog.msc.core.gateway.podcast.PodcastArtistGateway
import dev.olog.msc.core.gateway.podcast.PodcastGateway
import dev.olog.msc.core.gateway.podcast.PodcastPlaylistGateway
import dev.olog.msc.core.gateway.prefs.AppPreferencesGateway
import dev.olog.msc.core.gateway.prefs.MusicPreferencesGateway
import dev.olog.msc.core.gateway.prefs.TutorialPreferenceGateway
import dev.olog.msc.core.gateway.track.*
import dev.olog.msc.data.di.PreferenceModule
import dev.olog.msc.data.di.RepositoryHelperModule
import dev.olog.msc.data.di.RepositoryModule
import dev.olog.msc.floatingwindowservice.di.FloatingWindowServiceInjector
import dev.olog.msc.musicservice.di.EqualizerModule
import dev.olog.msc.musicservice.di.MusicServiceInjector
import dev.olog.msc.presentation.sleeptimer.di.SleepTimerModule
import javax.inject.Singleton

@Component(
    modules = arrayOf(
        AppModule::class,
        SchedulersModule::class,
        AppShortcutsModule::class,
        LastFmModule::class,

//        // data
        RepositoryModule::class,
        RepositoryHelperModule::class,
        PreferenceModule::class,
//
//        // presentation
        SleepTimerModule::class,
        DialogModules::class,
        PresentationModules::class,
        NavigatorModule::class,
        WidgetBindingModule::class,
        ViewModelModule::class,

//        // music service
        MusicServiceInjector::class,
        EqualizerModule::class,

//        // floating info service
        FloatingWindowServiceInjector::class
    )
)
@Singleton
interface AppComponent : InjectionHelper<Application> {

    fun viewModelFactory(): ViewModelProvider.Factory

    fun prefs(): AppPreferencesGateway
    fun musicPrefs(): MusicPreferencesGateway
    fun tutorialPrefs(): TutorialPreferenceGateway

    fun folderGateway(): FolderGateway
    fun playlistGateway(): PlaylistGateway
    fun songGateway(): SongGateway
    fun albumGateway(): AlbumGateway
    fun artistGateway(): ArtistGateway
    fun genreGateway(): GenreGateway
    fun podcastPlaylistGateway(): PodcastPlaylistGateway
    fun podcastGateway(): PodcastGateway
    fun podcastAlbumGateway(): PodcastAlbumGateway
    fun podcastArtistGateway(): PodcastArtistGateway

    fun appShortcuts(): AppShortcuts


    @Component.Factory
    interface Factory {

        fun create(@BindsInstance instance: Application): AppComponent
    }

    companion object {

        private var appComponent: AppComponent? = null

        fun appComponent(app: Application): AppComponent {
            if (appComponent == null){
                // not double checking because it will be created in App.kt on main thread at app startup
                appComponent = DaggerAppComponent.factory().create(app)
            }
            return appComponent!!
        }

    }

}