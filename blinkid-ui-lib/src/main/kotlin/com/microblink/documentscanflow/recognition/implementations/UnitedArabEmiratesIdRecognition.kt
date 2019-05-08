package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.sanitizeMRZString
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesIdFrontRecognizer

class UnitedArabEmiratesIdRecognition: BaseTwoSideRecognition() {

    val frontRecognizer by lazy { UnitedArabEmiratesIdFrontRecognizer() }
    val backRecognizer by lazy { UnitedArabEmiratesIdBackRecognizer() }

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun createValidator(): ResultValidator {
        val frontIdNumber = frontResult.idNumber.replace("-", "")
        val backIdNumber = backResult.mrzResult.opt1.sanitizeMRZString()
        return ResultValidator().match(frontIdNumber, backIdNumber)
    }

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            add(FULL_NAME, frontResult.name)
            add(IDENTITY_NUMBER, frontResult.idNumber)
            add(NATIONALITY, frontResult.nationality)
        }
        if (backResult.isNotEmpty()) {
            extractMrzResult(backResult.mrzResult)
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.name
        }
        if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }
        return null
    }

}