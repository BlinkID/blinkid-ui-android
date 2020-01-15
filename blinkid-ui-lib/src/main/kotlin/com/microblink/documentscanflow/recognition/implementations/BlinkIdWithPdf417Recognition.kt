package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.util.BlinkIdRecognitionUtils
import com.microblink.entities.recognizers.blinkbarcode.pdf417.Pdf417Recognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer

class BlinkIdWithPdf417Recognition : TwoSideRecognition<BlinkIdRecognizer.Result, Pdf417Recognizer.Result>() {

    override val frontRecognizer by lazy { BlinkIdRecognizer() }
    override val backRecognizer by lazy { Pdf417Recognizer() }
    private var title: String? = null

    override fun extractFields() {
        title = BlinkIdRecognitionUtils.extractFrontResult(frontResult, this)
        add(ResultKey.BARCODE_DATA, backRecognizer.result.stringData)
    }

    override fun getResultTitle() = title

}