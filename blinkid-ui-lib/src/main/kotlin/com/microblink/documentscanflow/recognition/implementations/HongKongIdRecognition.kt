package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.hongkong.HongKongIdFrontRecognizer

class HongKongIdRecognition : BaseRecognition() {

    private val recognizer by lazy { HongKongIdFrontRecognizer() }
    private val recognizerBack by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(recognizer, recognizerBack)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val fullName = result.fullName

        add(FULL_NAME, fullName)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(SEX, result.sex)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        add(RESIDENTIAL_STATUS, result.residentialStatus)
        
        return fullName
    }

}