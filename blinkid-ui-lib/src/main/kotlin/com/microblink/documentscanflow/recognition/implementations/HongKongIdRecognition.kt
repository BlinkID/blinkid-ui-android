package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideWithId1CardDetectorRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.hongkong.HongKongIdFrontRecognizer

class HongKongIdRecognition : SingleSideWithId1CardDetectorRecognition<HongKongIdFrontRecognizer.Result>() {

    override val recognizer by lazy { HongKongIdFrontRecognizer() }

    override fun extractData(result: HongKongIdFrontRecognizer.Result): String? {
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