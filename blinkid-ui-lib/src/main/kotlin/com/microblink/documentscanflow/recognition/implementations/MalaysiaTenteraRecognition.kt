package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MyTenteraRecognizer

class MalaysiaTenteraRecognition : BaseRecognition() {

    val recognizer by lazy { MyTenteraRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyFullName, result.ownerFullName)
        add(R.string.keyArmyNumber, result.armyNumber)
        add(R.string.keyAddress, result.ownerAddress)
        add(R.string.keyDateOfBirth, result.ownerBirthDate)
        add(R.string.keySex, result.ownerSex)
        add(R.string.keyReligion, result.ownerReligion)
        add(R.string.keyNricNumber, result.nricNumber)
        
        return result.ownerFullName
    }
    
}