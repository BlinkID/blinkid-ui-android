package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MalaysiaMyTenteraFrontRecognizer

class MalaysiaTenteraRecognition : BaseRecognition() {

    private val recognizerFront by lazy { MalaysiaMyTenteraFrontRecognizer() }
    private val recognizerBack by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizerFront, recognizerBack)
    }

    override fun extractData(): String? {
        val result = recognizerFront.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyFullName, result.fullName)
        add(R.string.keyArmyNumber, result.armyNumber)
        add(R.string.keyAddress, result.fullAddress)
        add(R.string.keyDateOfBirth, result.birthDate)
        add(R.string.keySex, result.sex)
        add(R.string.keyReligion, result.religion)
        add(R.string.keyNricNumber, result.nric)
        
        return result.fullName
    }
    
}
