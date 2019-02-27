package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesDlFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class UnitedArabEmiratesDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { UnitedArabEmiratesDlFrontRecognizer() }
    private val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        val result = frontRecognizer.result

        if (result.isEmpty()) {
            return null
        }

        add(LICENCE_NUMBER, result.licenseNumber)
        add(AUTHORITY, result.licensingAuthority)
        add(FULL_NAME, result.name)
        add(NATIONALITY, result.nationality)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DATE_OF_ISSUE, result.issueDate)
        addDateOfExpiry(result.expiryDate.date)
        add(PLACE_OF_ISSUE, result.placeOfIssue)

        return result.name
    }
}