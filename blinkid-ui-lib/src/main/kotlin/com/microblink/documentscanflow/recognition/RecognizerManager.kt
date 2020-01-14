package com.microblink.documentscanflow.recognition

import android.os.Parcel
import android.os.Parcelable
import com.microblink.documentscanflow.ScanFlowState
import com.microblink.documentscanflow.recognition.config.RecognitionConfig
import com.microblink.documentscanflow.recognition.framelistener.FrameGrabberMode
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer
import com.microblink.entities.recognizers.classifier.ClassifierCallback
import com.microblink.entities.recognizers.framegrabber.FrameCallback
import com.microblink.entities.recognizers.framegrabber.FrameGrabberRecognizer
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer
import com.microblink.image.Image
import com.microblink.recognition.RecognitionSuccessType

internal class RecognizerManager(private val recognitionConfig: RecognitionConfig,
                                 private val frameGrabberMode: FrameGrabberMode,
                                 frameCallback: FrameCallback,
                                 private val onDocumentNotSupported: () -> Unit) {

    private val singleSideSuccessFrameRecognizers: MutableList<SuccessFrameGrabberRecognizer> = mutableListOf()
    private val singleSideRecognizers: MutableList<Recognizer<*>> = mutableListOf()

    private var combinedRecognizer: Recognizer<*>? = null
    private var combinedSuccessFrameRecognizer: SuccessFrameGrabberRecognizer? = null

    private val frameGrabberRecognizer: FrameGrabberRecognizer = FrameGrabberRecognizer(frameCallback)

    fun addRecognizers(recognition: BaseRecognition) {
        for (recognizer in recognition.getSingleSideRecognizers()) {
            addSingleSideRecognizer(recognizer)
            if (recognizer is BlinkIdRecognizer) {
                recognizer.setClassifierCallback(MyClassifierCallback(onDocumentNotSupported))
            }
        }

        if (recognition.combinedRecognizer != null) {
            val recognizer = recognition.combinedRecognizer
            addCombinedRecognizer(recognition.combinedRecognizer!!)
            if (recognizer is BlinkIdCombinedRecognizer) {
                recognizer.setClassifierCallback(MyClassifierCallback(onDocumentNotSupported))
            }
        }
    }

    private fun addSingleSideRecognizer(recognizer: Recognizer<*>) {
        singleSideRecognizers.add(recognizer)
        if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            singleSideSuccessFrameRecognizers.add(SuccessFrameGrabberRecognizer(recognizer))
        }
    }

    private fun addCombinedRecognizer(recognizer: Recognizer<*>) {
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
        val recognizersForBundle = mutableListOf<Recognizer<*>>()
        if (frameGrabberMode == FrameGrabberMode.ALL_FRAMES) {
            recognizersForBundle.add(frameGrabberRecognizer)
        }
        if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            recognizersForBundle.add(combinedSuccessFrameRecognizer!!)
        } else {
            recognizersForBundle.add(combinedRecognizer!!)
        }

        return RecognizerBundle(*recognizersForBundle.toTypedArray()).apply {
            numMsBeforeTimeout = recognitionConfig.getRecognitionTimeoutSeconds() * 1000
        }
    }

    private fun buildStandardRecognizerBundle(scanFlowState: ScanFlowState): RecognizerBundle {
        val allRecognizers = if (frameGrabberMode != FrameGrabberMode.NOTHING) {
            singleSideSuccessFrameRecognizers
        } else {
            singleSideRecognizers
        }

        val recognizersForBundle = mutableListOf<Recognizer<*>>()

        if (frameGrabberMode == FrameGrabberMode.ALL_FRAMES) {
            recognizersForBundle.add(frameGrabberRecognizer)
        }

        if (scanFlowState == ScanFlowState.ANY_SIDE_SCAN) {
            recognizersForBundle.addAll(allRecognizers)
        } else {
            recognizersForBundle.add(allRecognizers[getIndexForSide(scanFlowState)])
        }

        return RecognizerBundle(*recognizersForBundle.toTypedArray()).apply {
            numMsBeforeTimeout = recognitionConfig.getRecognitionTimeoutSeconds() * 1000
        }
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
            scanFlowState == ScanFlowState.ANY_SIDE_SCAN -> getSuccessFrameAt(0)
                    ?: getSuccessFrameAt(1)
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

    fun handleRecognitionResult(successType: RecognitionSuccessType, onSuccess: () -> Unit, onRestartRequired: () -> Unit) {
        when {
            successType == RecognitionSuccessType.SUCCESSFUL ->
                onSuccess()
            successType == RecognitionSuccessType.PARTIAL && recognitionConfig.isPartialResultAllowed() ->
                onSuccess()
            else ->
                onRestartRequired()
        }
    }

    class MyClassifierCallback(private val onDocumentNotSupported: () -> Unit) : ClassifierCallback {

        private var unsupportedDocumentFrameCount = 0

        override fun writeToParcel(parcel: Parcel, flags: Int) {}

        override fun describeContents() = 0

        override fun onDocumentSupportStatus(isDocumentSupported: Boolean) {
            if (!isDocumentSupported) unsupportedDocumentFrameCount++
            if (unsupportedDocumentFrameCount == 3) {
                onDocumentNotSupported()
            }
        }

        companion object CREATOR : Parcelable.Creator<MyClassifierCallback> {
            override fun createFromParcel(parcel: Parcel): MyClassifierCallback {
                return MyClassifierCallback {}
            }

            override fun newArray(size: Int): Array<MyClassifierCallback?> {
                return arrayOfNulls(size)
            }
        }
    }

}