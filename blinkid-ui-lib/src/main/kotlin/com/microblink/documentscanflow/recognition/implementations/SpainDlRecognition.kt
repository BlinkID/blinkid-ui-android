package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer

class SpainDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { SpainDlFrontRecognizer() }
    private val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result

        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyLastName, result.surname)
        add(R.string.keyFirstName, result.firstName)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyPlaceOfBirth, result.placeOfBirth)
        add(R.string.keyIssueDate, result.validFrom)
        addDateOfExpiry(result.validUntil.date)
        add(R.string.keyIssuingAuthority, result.issuingAuthority)
        add(R.string.keyDocumentNumber, result.number)
        add(R.string.keyLicenceCategories, result.licenceCategories)

        return FormattingUtils.formatResultTitle(result.firstName, result.surname)
    }
}