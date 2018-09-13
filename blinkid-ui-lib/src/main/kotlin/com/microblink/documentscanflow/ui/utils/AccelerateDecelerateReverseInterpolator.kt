package com.microblink.documentscanflow.ui.utils

import android.view.animation.Interpolator

class AccelerateDecelerateReverseInterpolator: Interpolator {

    override fun getInterpolation(input: Float): Float {
        return (Math.cos((input * 2 + 1) * Math.PI) / 2.0f).toFloat() + 0.5f
    }

}