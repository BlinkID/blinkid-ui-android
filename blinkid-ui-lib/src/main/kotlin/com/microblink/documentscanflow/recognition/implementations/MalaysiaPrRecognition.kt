package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyPrFrontRecognizer

class MalaysiaPrRecognition: BaseRecognition(true) {

    val frontRecognizer by lazy { MalaysiaMyPrFrontRecognizer() }
    val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    val result by lazy { frontRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> = listOf(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        if (result.isNotEmpty()) {
            add(FULL_NAME, result.fullName)
            add(DATE_OF_BIRTH, result.birthDate)
            add(SEX, result.sex)
            add(RELIGION, result.religion)
            add(NRIC_NUMBER, result.nric)
            add(ADDRESS, result.fullAddress)
        }

        return result.fullName
    }

}