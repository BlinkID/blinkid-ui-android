package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.newzealand.NewZealandDlFrontRecognizer

class NewZealandDlRecognition : BaseRecognition() {

    val frontRecognizer by lazy { NewZealandDlFrontRecognizer() }
    val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        val firstName = result.firstNames
        val lastName = result.surname

        add(R.string.keyFirstName, firstName)
        add(R.string.keyLastName, lastName)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyDocumentNumber, result.licenseNumber)
        add(R.string.keyIssueDate, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(R.string.keyAddress, result.address)
        
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }
    
}