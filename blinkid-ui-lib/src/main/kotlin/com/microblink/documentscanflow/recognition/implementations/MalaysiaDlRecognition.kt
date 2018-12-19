package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
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

        add(R.string.keyFullName, result.name)
        add(R.string.keyIdentityNumber, result.identityNumber)
        add(R.string.keyNationality, result.nationality)
        add(R.string.keyClass, result.dlClass)
        add(R.string.keyIssueDate, result.validFrom)
        addDateOfExpiry(result.validUntil.date)
        add(R.string.keyAddress, result.fullAddress)
        
        return result.name
    }
    
}