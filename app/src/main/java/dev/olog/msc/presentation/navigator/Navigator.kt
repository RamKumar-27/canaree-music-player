package dev.olog.msc.presentation.navigator

import android.view.View
import dev.olog.msc.core.MediaId
import dev.olog.msc.core.MediaIdCategory
import dev.olog.msc.core.entity.PlaylistType
import dev.olog.msc.presentation.model.DisplayableItem

interface Navigator {

    fun toFirstAccess()

    fun toDetailFragment(mediaId: MediaId)

    fun toRelatedArtists(mediaId: MediaId)

    fun toRecentlyAdded(mediaId: MediaId)

    fun toChooseTracksForPlaylistFragment(type: PlaylistType)

    fun toEditInfoFragment(mediaId: MediaId)

    fun toOfflineLyrics()

    fun toDialog(item: DisplayableItem, anchor: View)
    fun toDialog(mediaId: MediaId, anchor: View)

    fun toMainPopup(anchor: View, category: MediaIdCategory?)

    fun toSetRingtoneDialog(mediaId: MediaId, title: String, artist: String)

    fun toCreatePlaylistDialog(mediaId: MediaId, listSize: Int, itemTitle: String)

    fun toAddToFavoriteDialog(mediaId: MediaId, listSize: Int, itemTitle: String)

    fun toPlayLater(mediaId: MediaId, listSize: Int, itemTitle: String)

    fun toPlayNext(mediaId: MediaId, listSize: Int, itemTitle: String)

    fun toRenameDialog(mediaId: MediaId, itemTitle: String)

    fun toClearPlaylistDialog(mediaId: MediaId, itemTitle: String)

    fun toDeleteDialog(mediaId: MediaId, listSize: Int, itemTitle: String)

    fun toRemoveDuplicatesDialog(mediaId: MediaId, itemTitle: String)

}