package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.hongkong.HongKongIdFrontRecognizer

class HongKongIdRecognition : BaseRecognition() {

    private val recognizer by lazy { HongKongIdFrontRecognizer() }
    private val recognizerBack by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer, recognizerBack)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val fullName = result.fullName

        add(R.string.keyFullName, fullName)
        add(R.string.keyDocumentNumber, result.documentNumber)
        add(R.string.keySex, result.sex)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyIssueDate, result.dateOfIssue)

        return fullName
    }

}