package com.microblink.documentscanflow.ui.splashoverlay

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes

interface SplashOverlaySettings {

    fun isEnabled() : Boolean
    fun getDurationMillis() : Long
    @ColorRes fun getBackgroundColorResource() : Int
    @DrawableRes fun getLogoDrawableResource() : Int

}