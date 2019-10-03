package com.microblink.documentscanflow.ui.splashoverlay

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

interface SplashOverlaySettings {

    fun isEnabled() : Boolean
    fun getDurationMillis() : Long
    @ColorRes fun getBackgroundColorResource() : Int
    @DrawableRes fun getLogoDrawableResource() : Int

}