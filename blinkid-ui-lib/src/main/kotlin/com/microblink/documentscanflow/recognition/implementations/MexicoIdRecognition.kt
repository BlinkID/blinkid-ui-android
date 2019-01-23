package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.mexico.MexicoVoterIdFrontRecognizer

class MexicoIdRecognition : BaseRecognition() {

    val frontRecognizer by lazy { MexicoVoterIdFrontRecognizer() }
    val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyFullName, result.fullName)
        add(R.string.keyAddress, result.address)
        add(R.string.keyCurp, result.curp)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keySex, result.sex)

        return result.fullName
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

}