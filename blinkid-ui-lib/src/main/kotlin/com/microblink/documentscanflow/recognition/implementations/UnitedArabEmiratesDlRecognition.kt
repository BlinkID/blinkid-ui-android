package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesDlFrontRecognizer

class UnitedArabEmiratesDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { UnitedArabEmiratesDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result

        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyLicenceNumber, result.licenseNumber)
        add(R.string.keyLicencingAuthority, result.licensingAuthority)
        add(R.string.keyFullName, result.name)
        add(R.string.keyNationality, result.nationality)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyIssueDate, result.issueDate)
        addDateOfExpiry(result.expiryDate.date)
        add(R.string.keyPlaceOfIssue, result.placeOfIssue)

        return result.name
    }
}