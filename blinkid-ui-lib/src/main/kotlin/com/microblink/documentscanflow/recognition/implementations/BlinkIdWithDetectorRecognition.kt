package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.BlinkIdRecognitionUtils
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer

class BlinkIdWithDetectorRecognition :
    SingleSideWithId1CardDetectorRecognition<BlinkIdRecognizer.Result>() {

    override val recognizer by lazy { BlinkIdRecognizer() }

    override fun extractData(result: BlinkIdRecognizer.Result): String? {
        return BlinkIdRecognitionUtils.extractFrontResult(result, this)
    }

}
