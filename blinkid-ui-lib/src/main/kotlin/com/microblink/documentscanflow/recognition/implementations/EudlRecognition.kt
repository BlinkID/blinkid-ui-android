package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry
import com.microblink.entities.recognizers.blinkid.eudl.EudlRecognizer

class EudlRecognition(eudlCountry: EudlCountry) : BaseRecognition() {

    val recognizer by lazy { EudlRecognizer(eudlCountry) }
    private val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(recognizer, backRecognizer)


    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val firstName = result.firstName
        val lastName = result.lastName

        add(R.string.keyFirstName, firstName)
        add(R.string.keyLastName, lastName)
        add(R.string.keyAddress, result.address)
        add(R.string.keyBirthData, result.birthData)
        add(R.string.keyIssueDate, result.issueDate.date)
        addDateOfExpiry(result.expiryDate.date)
        add(R.string.keyDriverNumber, result.driverNumber)
        add(R.string.keyIssuingAuthority, result.issuingAuthority)
        
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}