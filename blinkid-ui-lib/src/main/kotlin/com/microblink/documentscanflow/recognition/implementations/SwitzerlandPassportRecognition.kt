package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.switzerland.SwitzerlandPassportRecognizer

class SwitzerlandPassportRecognition: BaseRecognition() {

    private val mStringCombiner by lazy { StringCombiner(StringCombiner.Country.SWITZERLAND) }
    private val recognizer by lazy { SwitzerlandPassportRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val firstName = mStringCombiner.combineMRZString(result.secondaryId, result.givenName)
        val lastName = mStringCombiner.combineMRZString(result.primaryId, result.surname)

        add(R.string.keyFirstName, firstName)
        add(R.string.keyLastName, lastName)
        add(R.string.keyPassportNumber, result.passportNumber)
        add(R.string.keyNationality, result.nationality)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keyPlaceOfOrigin, result.placeOfBirth)
        add(R.string.keyIssueDate, result.dateOfIssue)
        add(R.string.keyDateOfExpiry, result.dateOfExpiry)
        add(R.string.keySex, result.sex)
        add(R.string.keyHeight, result.height)
        add(R.string.keyAuthority, result.authority)

        return FormattingUtils.formatResultTitle(firstName, lastName)
    }

}