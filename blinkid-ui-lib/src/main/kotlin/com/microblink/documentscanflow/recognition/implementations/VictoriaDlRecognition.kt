package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.australia.AustraliaDlBackRecognizer
import com.microblink.entities.recognizers.blinkid.australia.AustraliaDlFrontRecognizer

class VictoriaDlRecognition : TwoSideRecognition<AustraliaDlFrontRecognizer.Result, AustraliaDlBackRecognizer.Result>() {

    override val frontRecognizer by lazy { AustraliaDlFrontRecognizer() }
    override val backRecognizer by lazy { AustraliaDlBackRecognizer() }

    override fun createValidator() = ResultValidator().match(frontResult.licenceExpiry, backResult.licenceExpiry)

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            frontResult.apply {
                add(ADDRESS, address)
                add(DATE_OF_BIRTH, dateOfBirth)
                add(FULL_NAME, fullName)
                addDateOfExpiry(licenceExpiry)
                add(LICENCE_NUMBER, licenceNumber)
                add(LICENCE_TYPE, licenceType)
            }
        }

        if (backResult.isNotEmpty()) {
            backResult.apply {
                add(LAST_NAME, lastName)
                add(ADDRESS, address)
                add(LICENCE_NUMBER, licenceNumber)
                addDateOfExpiry(licenceExpiry)
            }
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.fullName
        }
        if (backResult.isNotEmpty()) {
            return backResult.lastName
        }
        return null
    }

}
