package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseTwoSideRecognition
import com.microblink.documentscanflow.recognition.ResultValidator
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.morocco.MoroccoIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.morocco.MoroccoIdFrontRecognizer

class MoroccoIdRecognition : BaseTwoSideRecognition() {

    private val frontRecognizer by lazy { MoroccoIdFrontRecognizer() }
    private val backRecognizer by lazy { MoroccoIdBackRecognizer() }

    private val frontResult by lazy { frontRecognizer.result }
    private val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(frontRecognizer, backRecognizer)
    }

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
            add(R.string.keyFirstName, name)
            add(R.string.keyLastName, surname)
            add(R.string.keyDocumentNumber, documentNumber)
            add(R.string.keySex, sex)
            add(R.string.keyDateOfBirth, dateOfBirth)
            add(R.string.keyPlaceOfBirth, placeOfBirth)
            addDateOfExpiry(dateOfExpiry.date)
        }
    }

    private fun extractBackSide() {
        backResult.apply {
            add(R.string.keyDocumentNumber, documentNumber)
            addDateOfExpiry(dateOfExpiry.date)
            add(R.string.keyFatherName, fathersName)
            add(R.string.keyMotherName, mothersName)
            add(R.string.keyAddress, address)
            add(R.string.keyCivilStatusNumber, civilStatusNumber)
            add(R.string.keySex, sex)
        }
    }
}