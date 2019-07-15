package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.buildTitle
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.extract
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer

class KuwaitIdRecognition : TwoSideRecognition<KuwaitIdFrontRecognizer.Result, KuwaitIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { KuwaitIdFrontRecognizer() }
    override val backRecognizer by lazy { KuwaitIdBackRecognizer() }

    override fun setupRecognizers() {
        backRecognizer.setExtractSerialNo(true)
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
            return backResult.mrzResult.buildTitle()
        }

        return null
    }

    private fun extractBackSide() {
        extract(backResult.mrzResult)
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