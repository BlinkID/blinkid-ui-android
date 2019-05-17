package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.eudl.EudlCountry
import com.microblink.entities.recognizers.blinkid.eudl.EudlRecognizer

class EudlRecognition(eudlCountry: EudlCountry) : SingleSideWithId1CardDetectorRecognition<EudlRecognizer.Result>() {

    override val recognizer by lazy { EudlRecognizer(eudlCountry) }

    override fun extractData(result: EudlRecognizer.Result): String? {
        val firstName = result.firstName
        val lastName = result.lastName

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(ADDRESS, result.address)
        add(BIRTH_DATA, result.birthData)
        add(DATE_OF_ISSUE, result.issueDate.date)
        addDateOfExpiry(result.expiryDate.date)
        add(DRIVER_NUMBER, result.driverNumber)
        add(ISSUING_AUTHORITY, result.issuingAuthority)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}