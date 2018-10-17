package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeIdFrontRecognizer

class SingaporeIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { SingaporeIdFrontRecognizer() }
    val backRecognizer by lazy { SingaporeIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    val combinedRecognizer by lazy { SingaporeCombinedRecognizer() }
    val combinedResult by lazy { combinedRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun getCombinedRecognizer(): Recognizer<*, *>? {
        return combinedRecognizer
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator().match(combinedResult)
    }

    override fun extractFields() {
        if (combinedResult.isNotEmpty()) {
            extractCombinedResult()
        }
        extractFrontSide()
        extractBackSide()
    }

    override fun getResultTitle(): String? {
        return if (combinedResult.isNotEmpty()) {
            combinedResult.name
        } else {
            frontResult.name
        }
    }

    private fun extractCombinedResult() {
        add(R.string.keyDocumentNumber, combinedResult.identityCardNumber)
        add(R.string.keyFullName, combinedResult.name)
        add(R.string.keyRace, combinedResult.race)
        add(R.string.keyDateOfBirth, combinedResult.dateOfBirth)
        add(R.string.keySex, combinedResult.sex)
        add(R.string.keyCountryOfBirth, combinedResult.countryOfBirth)
        add(R.string.keyBloodGroup, combinedResult.bloodGroup)
        add(R.string.keyIssueDate, combinedResult.dateOfIssue)
        add(R.string.keyAddress, combinedResult.address)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }

        add(R.string.keyDocumentNumber, frontResult.identityCardNumber)
        add(R.string.keyFullName, frontResult.name)
        add(R.string.keyRace, frontResult.race)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        add(R.string.keySex, frontResult.sex)
        add(R.string.keyCountryOfBirth, frontResult.countryOfBirth)
    }

    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }

        add(R.string.keyDocumentNumber, backResult.cardNumber)
        add(R.string.keyBloodGroup, backResult.bloodGroup)
        add(R.string.keyIssueDate, backResult.dateOfIssue)
        add(R.string.keyAddress, backResult.address)
    }
    
}