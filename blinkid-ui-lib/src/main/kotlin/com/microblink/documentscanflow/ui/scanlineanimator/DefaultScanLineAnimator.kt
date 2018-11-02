package com.microblink.documentscanflow.ui.scanlineanimator

import android.graphics.PorterDuff
import android.support.annotation.UiThread
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.ui.utils.AccelerateDecelerateReverseInterpolator
import com.microblink.documentscanflow.ui.view.CameraOverlayView
import org.jetbrains.anko.px2dip


class DefaultScanLineAnimator(private val scanLineView: ImageView,
                              private val scanContainer: CameraOverlayView) : ScanLineAnimator {

    private var scanRectHeight = 0f
    private var scanLineHeight = 0f
    private var scanAnimation = createAnimation()
    private var scanLineInitialY = 0f

    init {
        scanContainer.sizeCalculatedListeners.add(object : CameraOverlayView.OnSizeCalculatedListener {
            override fun onScanRectSizeCalculated(width: Float, height: Float) {
                scanRectHeight = height
                scanContainer.sizeCalculatedListeners.remove(this)
                if (scanLineHeight > 0) {
                    moveScanLineToScanRectTop()
                }
                startAnimation()
            }
        })

        scanLineView.visibility = View.VISIBLE
        scanLineView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                scanLineHeight = scanLineView.height.toFloat()
                if (scanLineHeight > 0) {
                    scanLineView.removeOnLayoutChangeListener(this)
                }

                if (scanRectHeight > 0) {
                    scanLineView.y = scanLineView.y - scanRectHeight / 2
                    scanLineInitialY = scanLineView.y
                }

                startAnimation()
            }
        })

        scanLineView.drawable.mutate()
                .setColorFilter(ContextCompat.getColor(scanLineView.context, R.color.mbIconScanLine), PorterDuff.Mode.MULTIPLY)
    }

    private fun moveScanLineToScanRectTop() {
        scanLineView.y = scanLineView.y - scanRectHeight / 2
        scanLineInitialY = scanLineView.y
    }

    @UiThread
    override fun onScanStart() {
        startAnimation()
    }

    @UiThread
    override fun onScanPause() {
        scanAnimation.cancel()
        scanLineView.visibility = View.GONE
    }

    @UiThread
    override fun onScanResume() {
        startAnimation()
    }

    private fun canStartAnimation() = scanRectHeight > 0 && scanLineHeight > 0

    private fun startAnimation() {
        if (!canStartAnimation()) {
            return
        }
        if (scanAnimation.hasStarted()) {
            scanAnimation.cancel()
            scanAnimation = createAnimation()
        }

        val previewHeightDp = scanContainer.px2dip(scanRectHeight.toInt())
        val scanLineAnimDuration = 4.167f * previewHeightDp + 1250

        scanAnimation.apply {
            duration = scanLineAnimDuration.toLong()
            interpolator = AccelerateDecelerateReverseInterpolator()
            repeatCount = Animation.INFINITE
        }

        scanLineView.startAnimation(scanAnimation)
    }

    private fun createAnimation(): Animation {
        return object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val translateHeight = scanRectHeight * interpolatedTime
                scanLineView.y = scanLineInitialY + translateHeight
            }
        }
    }

}