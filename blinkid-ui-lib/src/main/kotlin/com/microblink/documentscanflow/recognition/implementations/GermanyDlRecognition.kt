package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
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

        add(R.string.keyFirstName, firstName)
        add(R.string.keyLastName, lastName)
        add(R.string.keyLicenceNumber, frontResult.licenceNumber)
        add(R.string.keyLicenceCategories, frontResult.licenceCategories)
        add(R.string.keyPlaceOfBirth, frontResult.placeOfBirth)
        add(R.string.keyDateOfBirth, frontResult.dateOfBirth)
        add(R.string.keyIssueDate, frontResult.dateOfIssue)
        add(R.string.keyIssuingAuthority, frontResult.issuingAuthority)
        addDateOfExpiry(frontResult.dateOfExpiry.date)

        title = FormattingUtils.formatResultTitle(firstName, lastName)
    }

    private fun extractBackSide() {
        val backResult = backRecognizer.result
        if (!backResult.isEmpty()) {
            add(R.string.keyDateOfIssueBCategory, backResult.dateOfIssueB10)
        }
    }

}