package dev.olog.msc.domain.interactor.all.last.played

import dev.olog.core.entity.podcast.PodcastAlbum
import dev.olog.core.executor.ComputationScheduler
import dev.olog.msc.domain.gateway.PodcastAlbumGateway
import dev.olog.msc.domain.gateway.prefs.PresentationPreferences
import dev.olog.msc.domain.interactor.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class GetLastPlayedPodcastAlbumsUseCase @Inject constructor(
    schedulers: ComputationScheduler,
    private val albumGateway: PodcastAlbumGateway,
    private val appPreferencesUseCase: PresentationPreferences

): ObservableUseCase<List<PodcastAlbum>>(schedulers) {

    override fun buildUseCaseObservable(): Observable<List<PodcastAlbum>> {
        return Observables.combineLatest(
                albumGateway.getLastPlayed(),
                appPreferencesUseCase.observeLibraryRecentPlayedVisibility()) { albums, show ->
            if (show){
                albums
            } else {
                listOf()
            }
        }
    }
}