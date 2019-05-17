package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaDlFrontRecognizer

class MalaysiaDlRecognition : SingleSideWithId1CardDetectorRecognition<MalaysiaDlFrontRecognizer.Result>() {

    override val recognizer by lazy { MalaysiaDlFrontRecognizer() }

    override fun extractData(result: MalaysiaDlFrontRecognizer.Result): String? {
        add(FULL_NAME, result.name)
        add(IDENTITY_NUMBER, result.identityNumber)
        add(NATIONALITY, result.nationality)
        add(CLASS, result.dlClass)
        add(DATE_OF_ISSUE, result.validFrom)
        addDateOfExpiry(result.validUntil.date)
        add(ADDRESS, result.fullAddress)

        return result.name
    }
    
}