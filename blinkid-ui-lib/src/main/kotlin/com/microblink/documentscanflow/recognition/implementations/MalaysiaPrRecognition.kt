package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
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
            add(R.string.keyFullName, result.fullName)
            add(R.string.keyDateOfBirth, result.birthDate)
            add(R.string.keySex, result.sex)
            add(R.string.keyReligion, result.religion)
            add(R.string.keyNricNumber, result.nric)
            add(R.string.keyAddress, result.fullAddress)
        }

        return result.fullName
    }

}