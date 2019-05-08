package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
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

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun getCombinedRecognizer(): Recognizer<*>? {
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
        add(DOCUMENT_NUMBER, combinedResult.identityCardNumber)
        add(FULL_NAME, combinedResult.name)
        add(RACE, combinedResult.race)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(SEX, combinedResult.sex)
        add(COUNTRY_OF_BIRTH, combinedResult.countryOfBirth)
        add(BLOOD_GROUP, combinedResult.bloodGroup)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        add(ADDRESS, combinedResult.address)
    }

    private fun extractFrontSide() {
        if (frontResult.isEmpty()) {
            return
        }

        add(DOCUMENT_NUMBER, frontResult.identityCardNumber)
        add(FULL_NAME, frontResult.name)
        add(RACE, frontResult.race)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(SEX, frontResult.sex)
        add(COUNTRY_OF_BIRTH, frontResult.countryOfBirth)
    }

    private fun extractBackSide() {
        if (backResult.isEmpty()) {
            return
        }

        add(DOCUMENT_NUMBER, backResult.cardNumber)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        add(ADDRESS, backResult.address)
    }
    
}