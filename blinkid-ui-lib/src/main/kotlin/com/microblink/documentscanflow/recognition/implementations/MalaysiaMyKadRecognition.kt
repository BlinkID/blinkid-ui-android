package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MyKadBackRecognizer
import com.microblink.entities.recognizers.blinkid.malaysia.MyKadFrontRecognizer

class MalaysiaMyKadRecognition
    : BaseTwoSideRecognition() {

    val frontRecognizer by lazy { MyKadFrontRecognizer() }
    val backRecognizer by lazy { MyKadBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator()
                .match(frontResult.nricNumber, backResult.nric)
                .match(frontResult.ownerBirthDate, backResult.dateOfBirth.date)
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.ownerFullName
        } else if (backResult.isNotEmpty()) {
            return backResult.nric
        }
        return null
    }

    private fun extractFrontSide() {
        add(R.string.keyFullName, frontResult.ownerFullName)
        add(R.string.keyNricNumber, frontResult.nricNumber)
        add(R.string.keyDateOfBirth, frontResult.ownerBirthDate)
        add(R.string.keyAddress, frontResult.ownerAddress)
        add(R.string.keySex, frontResult.ownerSex)
        add(R.string.keyReligion, frontResult.ownerReligion)
    }

    private fun extractBackSide() {
        add(R.string.keyNricNumber, backResult.nric)
        add(R.string.keyExtendedNricNumber, backResult.extendedNric)
        add(R.string.keyDateOfBirth, backResult.dateOfBirth)
        add(R.string.keySex, backResult.sex)
    }

}
