package com.microblink.documentscanflow.ui

import android.content.Context
import android.support.annotation.UiThread
import android.view.View
import android.widget.TextSwitcher
import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.document.Document
import com.microblink.ui.blinkid.InstructionsHandler
import com.microblink.ui.blinkid.ScanFlowState

internal class DocumentInstructionsHandler(context: Context,
                                           private var document: Document,
                                           private val instructionsTextSwitcher: TextSwitcher,
                                           flipCardView: View) : InstructionsHandler(context, instructionsTextSwitcher, flipCardView) {


    @UiThread
    fun updateSideInstructions(newDocument: Document, scanFlowState: ScanFlowState): Long {
        document = newDocument
        return super.updateSideInstructions(scanFlowState)
    }

    @UiThread
    fun setAnySideInstructions(newDocument: Document) {
        document = newDocument

        delayedTextUpdateRunnable = Runnable { updateInstructionsIfChanged(R.string.mb_instructions_scan_any_side) }
        instructionsTextSwitcher.postDelayed(delayedTextUpdateRunnable, FIRST_INSTRUCTIONS_DURATION)
    }
}