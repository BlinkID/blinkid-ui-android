package com.microblink.documentscanflow

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.microblink.entities.detectors.quad.document.DocumentDetector
import com.microblink.entities.detectors.quad.document.DocumentSpecification
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.processors.imageReturn.ImageReturnProcessor
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult
import com.microblink.entities.recognizers.detector.DetectorRecognizer
import com.microblink.entities.recognizers.templating.ProcessorGroup
import com.microblink.entities.recognizers.templating.TemplatingClass
import com.microblink.entities.recognizers.templating.dewarpPolicies.DPIBasedDewarpPolicy
import com.microblink.geometry.Rectangle
import com.microblink.results.date.Date
import java.util.*

internal fun View.fadeOut(duration : Long) {
    val alpha = AlphaAnimation(1.0f, 0.0f)
    alpha.duration = duration
    alpha.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
        }

        override fun onAnimationEnd(animation: Animation) {
            visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animation) {
        }
    })
    startAnimation(alpha)
}

internal fun View.setVisible(isVisible : Boolean) {
    visibility = if (isVisible) View.VISIBLE
    else View.GONE
}

/**
 * Compares current date with the given date and returns positive integer if current date is greater than
 * the given argument, negative integer if the given argument is greater than current date. If dates are the
 * same returns 0.
 */
internal fun Date.compareTo(other: Date): Int {
    if (this.year < other.year) {
        return -1
    } else if (this.year > other.year) {
        return 1
    }
    if (this.month < other.month) {
        return -1
    } else if (this.month > other.month) {
        return 1
    }
    if (this.day < other.day) {
        return -1
    } else if (this.day > other.day) {
        return 1
    }
    return 0
}

internal fun String.sanitizeMRZString(): String {
    return this.replace('<', ' ').trim()
}

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal fun Recognizer.Result.isEmpty() = this.resultState == Recognizer.Result.State.Empty

internal fun Recognizer.Result.isNotEmpty() = this.resultState != Recognizer.Result.State.Empty

internal fun MrzResult.buildTitle() = "$primaryId $secondaryId"

internal fun Calendar.currentDate() = Date(get(Calendar.DAY_OF_MONTH), get(Calendar.MONTH) + 1, get(Calendar.YEAR))


@ColorInt
internal fun Context.getThemeColor(@AttrRes id: Int): Int {
    val typedValue = TypedValue()
    val a = obtainStyledAttributes(typedValue.data, intArrayOf(id))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}