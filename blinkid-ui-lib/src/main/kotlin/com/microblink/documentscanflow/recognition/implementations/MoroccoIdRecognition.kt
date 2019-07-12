package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.morocco.MoroccoIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.morocco.MoroccoIdFrontRecognizer

class MoroccoIdRecognition : TwoSideRecognition<MoroccoIdFrontRecognizer.Result, MoroccoIdBackRecognizer.Result>() {

    override val frontRecognizer by lazy { MoroccoIdFrontRecognizer() }
    override val backRecognizer by lazy { MoroccoIdBackRecognizer() }

    override fun createValidator() = ResultValidator()
            .match(frontResult.documentNumber, backResult.documentNumber)
            .match(frontResult.dateOfExpiry, backResult.dateOfExpiry)
            .match(frontResult.sex, backResult.sex)

    override fun extractFields() {
        if (frontResult.isNotEmpty()) {
            extractFrontSide()
        }
        if (backResult.isNotEmpty()) {
            extractBackSide()
        }
    }

    override fun getResultTitle() =
            when {
                frontResult.isNotEmpty() -> FormattingUtils.formatResultTitle(frontResult.name, frontResult.surname)
                else -> null
            }

    private fun extractFrontSide() {
        frontResult.apply {
            add(FIRST_NAME, name)
            add(LAST_NAME, surname)
            add(DOCUMENT_NUMBER, documentNumber)
            add(SEX, sex)
            add(DATE_OF_BIRTH, dateOfBirth)
            add(PLACE_OF_BIRTH, placeOfBirth)
            addDateOfExpiry(dateOfExpiry.date)
        }
    }

    private fun extractBackSide() {
        backResult.apply {
            add(DOCUMENT_NUMBER, documentNumber)
            addDateOfExpiry(dateOfExpiry.date)
            add(ADDRESS, address)
            add(CIVIL_STATUS_NUMBER, civilStatusNumber)
            add(SEX, sex)
        }
    }
}