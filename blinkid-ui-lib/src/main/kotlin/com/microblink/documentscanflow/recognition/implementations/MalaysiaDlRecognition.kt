package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaDlFrontRecognizer

class MalaysiaDlRecognition : BaseRecognition() {

    val frontRecognizer by lazy { MalaysiaDlFrontRecognizer() }
    val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if(result.isEmpty()) {
            return null
        }

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