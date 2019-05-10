package dev.olog.msc.presentation.categories

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import dev.olog.msc.shared.ui.extensions.colorAccent
import dev.olog.msc.shared.ui.extensions.windowBackground
import dev.olog.msc.taptargetview.TapTarget
import dev.olog.msc.taptargetview.TapTargetView

object Tutorial {
    fun floatingWindow(view: View){
        val context = view.context

        val target = TapTarget.forView(view, context.getString(R.string.tutorial_floating_window))
                .icon(ContextCompat.getDrawable(context, R.drawable.vd_search_text))
                .tint(context)
        TapTargetView.showFor(view.context as Activity, target)
    }

    private fun TapTarget.tint(context: Context): TapTarget {
        val accentColor = context.colorAccent()
        val backgroundColor = context.windowBackground()

        return this.tintTarget(true)
                .outerCircleColorInt(accentColor)
                .targetCircleColorInt(backgroundColor)
    }
}