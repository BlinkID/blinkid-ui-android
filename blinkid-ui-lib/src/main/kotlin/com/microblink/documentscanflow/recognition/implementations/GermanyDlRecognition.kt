package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyDlBackRecognizer
import com.microblink.entities.recognizers.blinkid.germany.GermanyDlFrontRecognizer

class GermanyDlRecognition : BaseRecognition() {

    private val frontRecognizer by lazy { GermanyDlFrontRecognizer() }
    private val backRecognizer by lazy { GermanyDlBackRecognizer() }

    private var title: String? = null

    override fun getSingleSideRecognizers() = listOf<Recognizer<*, *>>(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        extractFrontSide()
        extractBackSide()
        return title
    }

    private fun extractFrontSide() {
        val frontResult = frontRecognizer.result
        if (frontResult.resultState != Recognizer.Result.State.Valid) {
            return
        }

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

    private fun extractBackSide() {
        val backResult = backRecognizer.result
        if (!backResult.isEmpty()) {
            add(DATE_OF_ISSUE_FOR_B_CATEGORY, backResult.dateOfIssueB10)
        }
    }

}