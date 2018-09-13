package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.austria.AustriaPassportRecognizer

class AustriaPassportRecognition : BaseRecognition() {

    val recognizer by lazy { AustriaPassportRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val passResult = recognizer.result
        if (passResult.isEmpty()) {
            return null
        }

        val stringCombiner = StringCombiner(StringCombiner.Country.AUSTRIA)
        val firstName = stringCombiner.combineMRZString(passResult.mrzResult.secondaryId, passResult.givenName)
        val lastName = stringCombiner.combineMRZString(passResult.mrzResult.primaryId, passResult.surname)

        add(R.string.keyFirstName, firstName)
        add(R.string.keyLastName, lastName)
        add(R.string.keyNationality, passResult.nationality)
        add(R.string.keyPlaceOfBirth, passResult.placeOfBirth)
        add(R.string.keySex, passResult.sex)
        add(R.string.keyAuthority, passResult.issuingAuthority)
        add(R.string.keyDocumentNumber, passResult.passportNumber.removeSuffix("<"))
        add(R.string.keyDateOfBirth, passResult.dateOfBirth)
        add(R.string.keyIssueDate, passResult.dateOfIssue)
        add(R.string.keyIssuer, passResult.issuingAuthority)
        add(R.string.keyDateOfExpiry, passResult.dateOfExpiry)
        add(R.string.keyHeight, passResult.height)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}