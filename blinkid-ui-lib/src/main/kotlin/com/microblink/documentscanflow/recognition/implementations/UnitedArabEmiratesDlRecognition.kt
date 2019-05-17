package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesDlFrontRecognizer
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*

class UnitedArabEmiratesDlRecognition : SingleSideWithId1CardDetectorRecognition<UnitedArabEmiratesDlFrontRecognizer.Result>() {

    override val recognizer by lazy { UnitedArabEmiratesDlFrontRecognizer() }

    override fun extractData(result: UnitedArabEmiratesDlFrontRecognizer.Result): String? {
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