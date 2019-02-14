package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.recognition.framelistener.FrameGrabberMode
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.framegrabber.FrameCallback
import com.microblink.entities.recognizers.framegrabber.FrameGrabberRecognizer
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer
import com.microblink.ui.blinkid.ScanFlowState
import com.microblink.image.Image

internal class RecognizerManager(private val frameGrabberMode: FrameGrabberMode,
                                 frameCallback: FrameCallback) {

    private val singleSideSuccessFrameRecognizers: MutableList<SuccessFrameGrabberRecognizer> = mutableListOf()
    private val singleSideRecognizers: MutableList<Recognizer<*, *>> = mutableListOf()

    private var combinedRecognizer: Recognizer<*, *>? = null
    private var combinedSuccessFrameRecognizer: SuccessFrameGrabberRecognizer? = null

    private val frameGrabberRecognizer: FrameGrabberRecognizer = FrameGrabberRecognizer(frameCallback)

    fun addRecognizers(recognition: BaseRecognition) {
        for (recognizer in recognition.getSingleSideRecognizers()) {
            addSingleSideRecognizer(recognizer)
        }
        if (recognition.getCombinedRecognizer() != null) {
            addCombinedRecognizer(recognition.getCombinedRecognizer()!!)
        }
    }

    private fun addSingleSideRecognizer(recognizer: Recognizer<*, *>) {
        singleSideRecognizers.add(recognizer)
        if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            singleSideSuccessFrameRecognizers.add(SuccessFrameGrabberRecognizer(recognizer))
        }
    }

    private fun addCombinedRecognizer(recognizer: Recognizer<*, *>) {
        combinedRecognizer = recognizer
        if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            combinedSuccessFrameRecognizer = SuccessFrameGrabberRecognizer(recognizer)
        }
    }

    fun buildRecognizerBundle(scanFlowState: ScanFlowState): RecognizerBundle {
        return if (canUseCombinedRecognizer(scanFlowState)) {
            buildCombinedRecognizerBundle()
        } else {
            buildStandardRecognizerBundle(scanFlowState)
        }
    }

    private fun canUseCombinedRecognizer(scanFlowState: ScanFlowState) =
            scanFlowState != ScanFlowState.ANY_SIDE_SCAN && combinedRecognizer != null

    private fun buildCombinedRecognizerBundle(): RecognizerBundle {
        val recognizersForBundle = mutableListOf<Recognizer<*, *>>()
        if (frameGrabberMode == FrameGrabberMode.ALL_FRAMES) {
            recognizersForBundle.add(frameGrabberRecognizer)
        }
        if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            recognizersForBundle.add(combinedSuccessFrameRecognizer!!)
        } else {
            recognizersForBundle.add(combinedRecognizer!!)
        }

        return RecognizerBundle(*recognizersForBundle.toTypedArray())
    }

    private fun buildStandardRecognizerBundle(scanFlowState: ScanFlowState): RecognizerBundle {
        val allRecognizers = if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            singleSideSuccessFrameRecognizers
        } else {
            singleSideRecognizers
        }

        val recognizersForBundle = mutableListOf<Recognizer<*, *>>()

        if (frameGrabberMode == FrameGrabberMode.ALL_FRAMES) {
            recognizersForBundle.add(frameGrabberRecognizer)
        }

        if (scanFlowState == ScanFlowState.ANY_SIDE_SCAN) {
            recognizersForBundle.addAll(allRecognizers)
        } else {
            recognizersForBundle.add(allRecognizers[getIndexForSide(scanFlowState)])
        }

        return RecognizerBundle(*recognizersForBundle.toTypedArray())
    }

    private fun getIndexForSide(scanFlowState: ScanFlowState) =
            if (scanFlowState == ScanFlowState.BACK_SIDE_SCAN) {
                1
            } else {
                0
            }

    fun getSuccessFrame(scanFlowState: ScanFlowState): Image? {
        return when {
            frameGrabberMode == FrameGrabberMode.NOTHING -> null
            canUseCombinedRecognizer(scanFlowState) -> combinedSuccessFrameRecognizer!!.result.successFrame
            singleSideSuccessFrameRecognizers.size == 1 -> getSuccessFrameAt(0)
            scanFlowState == ScanFlowState.ANY_SIDE_SCAN -> getSuccessFrameAt(0) ?: getSuccessFrameAt(1)
            else -> getSuccessFrameAt(getIndexForSide(scanFlowState))
        }
    }

    private fun getSuccessFrameAt(index: Int): Image? {
        if (index >= singleSideSuccessFrameRecognizers.size) {
            return null
        }
        return singleSideSuccessFrameRecognizers[index].result.successFrame
    }

    fun clear() {
        singleSideSuccessFrameRecognizers.clear()
        singleSideRecognizers.clear()
        combinedRecognizer = null
        combinedSuccessFrameRecognizer = null
    }

    fun isCombinedRecognition(scanFlowState: ScanFlowState) = canUseCombinedRecognizer(scanFlowState)

}