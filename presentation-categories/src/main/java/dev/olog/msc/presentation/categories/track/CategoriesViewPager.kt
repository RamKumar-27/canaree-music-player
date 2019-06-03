package dev.olog.msc.presentation.categories.track

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import dev.olog.msc.core.MediaIdCategory
import dev.olog.msc.core.entity.LibraryCategoryBehavior
import dev.olog.msc.core.gateway.prefs.AppPreferencesGateway
import dev.olog.msc.presentation.base.extensions.asString
import dev.olog.msc.presentation.navigator.Fragments
import dev.olog.msc.shared.core.lazyFast

class CategoriesViewPager(
    private val context: Context,
    fragmentManager: FragmentManager,
    private val categories: List<LibraryCategoryBehavior>,
    private val gateway: AppPreferencesGateway

) : FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val showFolderAsHierarchy by lazyFast { gateway.getShowFolderAsTreeView() }

    fun getCategoryAtPosition(position: Int): MediaIdCategory? {
        try {
            return categories[position].category
        } catch (ex: Exception) {
            return null
        }
    }

    override fun getItem(position: Int): Fragment {
        val category = categories[position].category
        return if (category == MediaIdCategory.FOLDERS && showFolderAsHierarchy) {
            Fragments.folderTree(context)
        } else Fragments.tab(context, category)
    }

    override fun getCount(): Int = categories.size

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].asString(context)
    }

    fun isEmpty() = categories.isEmpty()

}