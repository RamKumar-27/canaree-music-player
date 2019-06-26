package dev.olog.msc.presentation.edit.track

import dev.olog.core.MediaId
import dev.olog.core.entity.LastFmTrack
import dev.olog.core.entity.track.Song
import dev.olog.core.gateway.SongGateway
import dev.olog.core.gateway.UsedImageGateway
import dev.olog.msc.domain.interactor.item.GetPodcastUseCase
import dev.olog.msc.utils.k.extension.get
import dev.olog.presentation.AppConstants
import io.reactivex.Single
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx2.asObservable
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import javax.inject.Inject

class EditTrackFragmentPresenter @Inject constructor(
    private val mediaId: MediaId,
    private val songGateway2: SongGateway,
    private val getPodcastUseCase: GetPodcastUseCase,
    private val usedImageGateway: UsedImageGateway

) {

    private lateinit var originalSong : DisplayableSong

    fun observeSong(): Single<DisplayableSong> {
        if (mediaId.isPodcast){
            return observePodcastInternal()
        }
        return observeSongInternal()
    }

    private fun observeSongInternal(): Single<DisplayableSong> {
        return songGateway2.observeByParam(mediaId.leaf!!).map { it!! }.asObservable()
                .firstOrError()
                .map { it.copy(
                        artist = if (it.artist == AppConstants.UNKNOWN) "" else it.artist,
                        album = if (it.album == AppConstants.UNKNOWN) "" else it.album
                ) }
                .map { it.toDisplayableSong() }
                .doOnSuccess {
//                    val usedImage = usedImageGateway.getForTrack(it.id)
//                            ?: usedImageGateway.getForAlbum(it.albumId)
//                            ?: it.image
//                    originalSong = it.copy(image = usedImage) TODO
                }
    }

    private fun observePodcastInternal(): Single<DisplayableSong> {
        return getPodcastUseCase.execute(mediaId)
                .firstOrError()
                .map { it.copy(
                        artist = if (it.artist == AppConstants.UNKNOWN) "" else it.artist,
                        album = if (it.album == AppConstants.UNKNOWN) "" else it.album
                ) }
                .map { it.toDisplayableSong() }
                .doOnSuccess {
//                    val usedImage = usedImageGateway.getForTrack(it.id)
//                            ?: usedImageGateway.getForAlbum(it.albumId)
//                            ?: it.image
//                    originalSong = it.copy(image = usedImage) TODO
                }
    }

    fun fetchData(): LastFmTrack? {
        TODO()
//        return getLastFmTrackUseCase.execute(
//                LastFmTrackRequest(originalSong.id, originalSong.title, originalSong.artist, originalSong.album)
//        )
    }

    fun getSong(): DisplayableSong = originalSong

    private fun Song.toDisplayableSong(): DisplayableSong {
        val file = File(path)
        val audioFile = AudioFileIO.read(file)
        val audioHeader = audioFile.audioHeader
        val tag = audioFile.tagOrCreateAndSetDefault

        return DisplayableSong(
                this.id,
                this.artistId,
                this.albumId,
                this.title,
                tag.get(FieldKey.ARTIST),
                tag.get(FieldKey.ALBUM_ARTIST),
                album,
                tag.get(FieldKey.GENRE),
                tag.get(FieldKey.YEAR),
                tag.get(FieldKey.DISC_NO),
                tag.get(FieldKey.TRACK),
                this.path,
                audioHeader.bitRate + " kb/s",
                audioHeader.format,
                audioHeader.sampleRate +  " Hz",
                false
        )
    }

}