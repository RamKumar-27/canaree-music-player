package dev.olog.presentation.base.theme.immersive

import android.app.Activity

interface IImmersive {
    fun setCurrentActivity(activity: Activity?)
    fun isEnabled(): Boolean
}