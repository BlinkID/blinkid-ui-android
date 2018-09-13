package com.microblink.documentscanflow.ui.splashoverlay

class InvisibleSplashOverlaySettings : SplashOverlaySettings {

    override fun isEnabled(): Boolean = false

    override fun getDurationMillis(): Long = 0

    override fun getBackgroundColorResource(): Int = 0

    override fun getLogoDrawableResource(): Int = 0

}