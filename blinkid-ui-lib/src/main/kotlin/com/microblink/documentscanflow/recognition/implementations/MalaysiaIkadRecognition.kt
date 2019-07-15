package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaIkadFrontRecognizer

class MalaysiaIkadRecognition : SingleSideRecognition<MalaysiaIkadFrontRecognizer.Result>() {

    override val recognizer by lazy { MalaysiaIkadFrontRecognizer() }

    override fun extractData(result: MalaysiaIkadFrontRecognizer.Result): String? {
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