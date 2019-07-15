package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.sanitizeMRZString
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.unitedArabEmirates.UnitedArabEmiratesIdFrontRecognizer

class UnitedArabEmiratesIdRecognition: TwoSideRecognition<UnitedArabEmiratesIdFrontRecognizer.Result, UnitedArabEmiratesIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { UnitedArabEmiratesIdFrontRecognizer() }
    override val backRecognizer by lazy { UnitedArabEmiratesIdBackRecognizer() }

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
            extract(backResult.mrzResult)
        }
    }

    override fun getResultTitle(): String? {
        if (frontResult.isNotEmpty()) {
            return frontResult.name
        }
        if (backResult.isNotEmpty()) {
            return backResult.mrzResult.buildTitle()
        }
        return null
    }

}