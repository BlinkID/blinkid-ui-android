package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.TwoSideRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.germany.GermanyDlBackRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyDlFrontRecognizer

class GermanyDlRecognition : TwoSideRecognition<GermanyDlFrontRecognizer.Result, GermanyDlBackRecognizer.Result>() {

    override val frontRecognizer by lazy { GermanyDlFrontRecognizer() }
    override val backRecognizer by lazy { GermanyDlBackRecognizer() }

    override fun extractData(frontResult: GermanyDlFrontRecognizer.Result, backResult: GermanyDlBackRecognizer.Result): String? {
        var title: String? = null
        if (frontResult.isNotEmpty()) {
            val firstName = frontResult.firstName
            val lastName = frontResult.lastName

            add(FIRST_NAME, firstName)
            add(LAST_NAME, lastName)
            add(LICENCE_NUMBER, frontResult.licenceNumber)
            add(LICENCE_CATEGORIES, frontResult.licenceCategories)
            add(PLACE_OF_BIRTH, frontResult.placeOfBirth)
            add(DATE_OF_BIRTH, frontResult.dateOfBirth)
            add(DATE_OF_ISSUE, frontResult.dateOfIssue)
            add(ISSUING_AUTHORITY, frontResult.issuingAuthority)
            addDateOfExpiry(frontResult.dateOfExpiry.date)

            title = FormattingUtils.formatResultTitle(firstName, lastName)
        }
        if (backResult.isNotEmpty()) {
            add(DATE_OF_ISSUE_FOR_B_CATEGORY, backResult.dateOfIssueB10)
        }
        return title
    }

}