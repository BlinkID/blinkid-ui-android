package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildDetectorRecognizerFromPreset
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.detectors.quad.document.DocumentSpecificationPreset
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.ireland.IrelandDlFrontRecognizer

class IrelandDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { IrelandDlFrontRecognizer() }
    private val backRecognizer by lazy {
        buildDetectorRecognizerFromPreset(DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD)
    }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        result.apply {
            add(R.string.keyLastName, surname)
            add(R.string.keyFirstName, firstName)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyPlaceOfBirth, placeOfBirth)
            add(R.string.keyIssueDate, dateOfIssue)
            add(R.string.keyIssuedBy, issuedBy)
            addDateOfExpiry(dateOfExpiry.date)
            add(R.string.keyDriverNumber, driverNumber)
            add(R.string.keyLicenceNumber, licenceNumber)
            add(R.string.keyAddress, address)
            add(R.string.keyLicenceCategories, licenceCategories)
        }

        return FormattingUtils.formatResultTitle(result.firstName, result.surname)
    }
}