package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaIkadFrontRecognizer

class MalaysiaIkadRecognition : BaseRecognition() {

    val recognizer by lazy { MalaysiaIkadFrontRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(FULL_NAME, result.name)
        add(ADDRESS, result.address)
        add(FACULTY_ADDRESS, result.facultyAddress)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(EMPLOYER, result.employer)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(NATIONALITY, result.nationality)
        add(SECTOR, result.sector)
        add(SEX, result.gender)
        
        return result.name
    }

}