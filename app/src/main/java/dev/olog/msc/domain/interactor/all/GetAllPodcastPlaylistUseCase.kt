package dev.olog.msc.domain.interactor.all

import dev.olog.core.entity.podcast.PodcastPlaylist
import dev.olog.core.executor.ComputationScheduler
import dev.olog.msc.domain.gateway.PodcastPlaylistGateway
import dev.olog.msc.domain.interactor.base.GetGroupUseCase
import javax.inject.Inject

class GetAllPodcastPlaylistUseCase @Inject constructor(
        gateway: PodcastPlaylistGateway,
        schedulers: ComputationScheduler
) : GetGroupUseCase<PodcastPlaylist>(gateway, schedulers)