package dev.olog.msc.domain.interactor.all.last.played

import dev.olog.core.MediaId
import dev.olog.core.gateway.AlbumGateway
import dev.olog.core.gateway.PodcastAlbumGateway
import dev.olog.shared.utils.assertBackgroundThread
import javax.inject.Inject

class InsertLastPlayedAlbumUseCase @Inject constructor(
    private val albumGateway: AlbumGateway,
    private val podcastGateway: PodcastAlbumGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        assertBackgroundThread()
        if (mediaId.isPodcastAlbum) {
            podcastGateway.addLastPlayed(mediaId.categoryValue.toLong())
        } else {
            albumGateway.addLastPlayed(mediaId.categoryValue.toLong())
        }
    }

}