package com.microblink.documentscanflow.ui

import android.animation.AnimatorSet
import androidx.annotation.UiThread
import android.view.View
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.ScanFlowState
import com.microblink.documentscanflow.document.Document
import android.animation.AnimatorInflater
import android.content.Context
import android.widget.TextSwitcher
import android.view.animation.AnimationUtils
import android.view.Gravity
import android.widget.TextView
import android.widget.ViewSwitcher

internal class InstructionsHandler(private val context: Context,
                                   private var document: Document,
                                   private val instructionsTextSwitcher: TextSwitcher,
                                   private val flipCardView: View) {

    private var lastSideState = ScanFlowState.NOT_STARTED
    private val flipAnimation = AnimatorInflater.loadAnimator(context, R.animator.mb_card_flip) as AnimatorSet
    private var delayedTextUpdateRunnable: Runnable? = null

    init {
        val viewFactory = ViewSwitcher.ViewFactory {
            val textView = TextView(context)
            textView.gravity = Gravity.CENTER
            textView.setTextAppearance(context, R.style.MbTextScanInstructions)
            textView
        }
        val animationIn = AnimationUtils.loadAnimation(context, R.anim.mb_show_text)
        val animationOut = AnimationUtils.loadAnimation(context, R.anim.mb_hide_text)
        animationIn.startOffset = 550 // bit more than hide animation duration
        instructionsTextSwitcher.apply {
            setFactory(viewFactory)
            inAnimation = animationIn
            outAnimation = animationOut
        }
    }

    @UiThread
    fun updateSideInstructions(newDocument: Document, scanFlowState: ScanFlowState): Long {
        document = newDocument
        var delay = 0L
        if (scanFlowState == ScanFlowState.BACK_SIDE_SCAN) {
            flipToBackSide()
            delay = FLIP_DELAY
        } else {
            if (lastSideState == ScanFlowState.NOT_STARTED) {
                switchToFrontSide()
            } else {
                flipBackToFront()
                delay = FLIP_DELAY
            }
        }

        lastSideState = scanFlowState
        return delay
    }

    @UiThread
    fun setAnySideInstructions(newDocument: Document) {
        document = newDocument
        updateInstructionsIfChanged(R.string.mb_instructions_scan_any_side)
    }

    @UiThread
    fun onGlareDetected() {
        val currentText = getCurrentText()
        val didTextChange = updateInstructionsIfChanged(context.getString(R.string.mb_instructions_glare))
        if (didTextChange) {
            delayedTextUpdateRunnable = Runnable { updateInstructionsIfChanged(currentText) }
        } else {
            instructionsTextSwitcher.handler.removeCallbacks(delayedTextUpdateRunnable)
        }
        instructionsTextSwitcher.postDelayed(delayedTextUpdateRunnable, GLARE_INSTRUCTIONS_DURATION)
    }

    @UiThread
    fun clear(shouldClearScanFlowState: Boolean) {
        if (shouldClearScanFlowState) {
            lastSideState = ScanFlowState.NOT_STARTED
        }

        flipAnimation.end()
    }

    private fun switchToFrontSide() {
        updateInstructionsIfChanged(R.string.mb_instructions_scan_front)
    }

    private fun flipToBackSide() {
        updateInstructionsIfChanged("")
        delayedTextUpdateRunnable = Runnable { updateInstructionsIfChanged(R.string.mb_instructions_scan_back) }
        instructionsTextSwitcher.postDelayed(delayedTextUpdateRunnable, BACK_SIDE_INSTRUCTIONS_DELAY)
        animateFlipView()
    }

    private fun flipBackToFront() {
        updateInstructionsIfChanged(R.string.mb_instructions_scan_front)
    }

    private fun animateFlipView() {
        val scale = context.resources.displayMetrics.density
        flipCardView.cameraDistance = 60 * flipCardView.width * scale
        flipAnimation.setTarget(flipCardView)
        flipAnimation.start()
    }

    private fun updateInstructionsIfChanged(newText: Int) = updateInstructionsIfChanged(context.getString(newText))

    private fun updateInstructionsIfChanged(newText: CharSequence): Boolean {
        if (getCurrentText() != newText) {
            instructionsTextSwitcher.setText(newText)
            instructionsTextSwitcher.removeCallbacks(delayedTextUpdateRunnable)
            return true
        }
        return false
    }

    private fun getCurrentText(): CharSequence {
        val currentTextView = instructionsTextSwitcher.currentView as TextView
        return currentTextView.text
    }

    internal fun testAnimation() {
        animateFlipView()
    }

    companion object {
        const val FLIP_DELAY = 1500L
        const val BACK_SIDE_INSTRUCTIONS_DELAY = 1000L
        const val GLARE_INSTRUCTIONS_DURATION = 2000L
    }

}