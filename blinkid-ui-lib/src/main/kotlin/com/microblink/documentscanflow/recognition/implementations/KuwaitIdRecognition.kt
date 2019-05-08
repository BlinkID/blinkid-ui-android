package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer

class KuwaitIdRecognition : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { KuwaitIdFrontRecognizer() }
    private val backRecognizer by lazy { KuwaitIdBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun setupRecognizers() {
        backRecognizer.setExtractSerialNo(true)
    }

    override fun createValidator(): ResultValidator {
        return ResultValidator()
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
            return frontResult.name
        } else if (backResult.isNotEmpty()) {
            return buildMrtdTitle(backResult.mrzResult)
        }

        return null
    }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    private fun extractBackSide() {
        extractMrzResult(backResult.mrzResult)
        add(SERIAL_NUMBER, backResult.serialNo)
    }

    private fun extractFrontSide() {
        add(CIVIL_ID_NUMBER, frontResult.civilIdNumber)
        add(FULL_NAME, frontResult.name)
        add(NATIONALITY, frontResult.nationality)
        add(SEX, frontResult.sex)
        add(DATE_OF_BIRTH, frontResult.birthDate)
        addDateOfExpiry(frontResult.expiryDate.date)
    }

}