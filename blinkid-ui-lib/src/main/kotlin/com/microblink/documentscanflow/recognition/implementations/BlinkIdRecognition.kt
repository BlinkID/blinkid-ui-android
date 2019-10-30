package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.ResultMergeException
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.DataMatchResult
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult

class BlinkIdRecognition(isFullySupported: Boolean = true) :
    BaseRecognition(isFullySupported) {

    val frontRecognizer by lazy { BlinkIdRecognizer() }
    override val combinedRecognizer by lazy { BlinkIdCombinedRecognizer() }

    override fun canScanBothSides() = true

    override fun getSingleSideRecognizers() = listOf(frontRecognizer)

    override fun getAllRecognizers(): List<Recognizer<*>> {
        val allRecognizers = mutableListOf<Recognizer<*>>()
        allRecognizers.addAll(getSingleSideRecognizers())
        allRecognizers.add(combinedRecognizer)
        return allRecognizers
    }

    override fun extractData(): String? {
        val frontResult = frontRecognizer.result
        val combinedResult = combinedRecognizer.result

        if (shouldValidate) {
            val isValid =
                (combinedResult as com.microblink.entities.recognizers.blinkid.CombinedResult).documentDataMatch == DataMatchResult.Success
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return when {
            frontResult.isNotEmpty() -> extractFrontResult(frontResult)
            combinedResult.isNotEmpty() -> extractCombinedResult(combinedResult)
            else -> null
        }
    }

    private fun extractCombinedResult(combinedResult: BlinkIdCombinedRecognizer.Result): String? {
        combinedResult.apply {
            add(ResultKey.SEX, sex)
            add(ResultKey.ADDRESS, address)
            add(ResultKey.PLACE_OF_BIRTH, placeOfBirth)
            add(ResultKey.NATIONALITY, nationality)
            add(ResultKey.RACE, race)
            add(ResultKey.RELIGION, religion)
            add(ResultKey.OCCUPATION, profession)
            add(ResultKey.MARITAL_STATUS, maritalStatus)
            add(ResultKey.RESIDENTIAL_STATUS, residentialStatus)
            add(ResultKey.EMPLOYER, employer)
            add(ResultKey.DOCUMENT_NUMBER, documentNumber)
            add(ResultKey.PERSONAL_NUMBER, personalIdNumber)
            add(ResultKey.ISSUING_AUTHORITY, issuingAuthority)
            add(ResultKey.DATE_OF_BIRTH, dateOfBirth)
            add(ResultKey.DATE_OF_ISSUE, dateOfIssue)
            addDateOfExpiry(dateOfExpiry.date)

            add(ResultKey.VEHICLE_CLASS, driverLicenseDetailedInfo.vehicleClass)
            add(ResultKey.ENDORSEMENTS, driverLicenseDetailedInfo.endorsements)
            add(ResultKey.DRIVER_RESTRICTIONS, driverLicenseDetailedInfo.restrictions)

            addMrzResults(mrzResult)

            var title = if (mrzResult.isMrzParsed) {
                add(ResultKey.SECONDARY_ID, mrzResult.secondaryId)
                add(ResultKey.PRIMARY_ID, mrzResult.primaryId)

                FormattingUtils.formatResultTitle(mrzResult.secondaryId, mrzResult.primaryId)
            } else {
                add(ResultKey.FIRST_NAME, firstName)
                add(ResultKey.LAST_NAME, lastName)

                FormattingUtils.formatResultTitle(firstName, lastName)
            }

            if (fullName.isNotEmpty()) {
                add(ResultKey.FULL_NAME, fullName)
                title = fullName
            }

            return title
        }
    }

    private fun extractFrontResult(frontResult: BlinkIdRecognizer.Result): String? {
        frontResult.apply {
            add(ResultKey.SEX, sex)
            add(ResultKey.ADDRESS, address)
            add(ResultKey.PLACE_OF_BIRTH, placeOfBirth)
            add(ResultKey.NATIONALITY, nationality)
            add(ResultKey.RACE, race)
            add(ResultKey.RELIGION, religion)
            add(ResultKey.OCCUPATION, profession)
            add(ResultKey.MARITAL_STATUS, maritalStatus)
            add(ResultKey.RESIDENTIAL_STATUS, residentialStatus)
            add(ResultKey.EMPLOYER, employer)
            add(ResultKey.DOCUMENT_NUMBER, documentNumber)
            add(ResultKey.PERSONAL_NUMBER, personalIdNumber)
            add(ResultKey.ISSUING_AUTHORITY, issuingAuthority)
            add(ResultKey.DATE_OF_BIRTH, dateOfBirth)
            add(ResultKey.DATE_OF_ISSUE, dateOfIssue)
            addDateOfExpiry(dateOfExpiry.date)

            add(ResultKey.VEHICLE_CLASS, driverLicenseDetailedInfo.vehicleClass)
            add(ResultKey.ENDORSEMENTS, driverLicenseDetailedInfo.endorsements)
            add(ResultKey.DRIVER_RESTRICTIONS, driverLicenseDetailedInfo.restrictions)

            addMrzResults(mrzResult)

            var title = if (mrzResult.isMrzParsed) {
                add(ResultKey.SECONDARY_ID, mrzResult.secondaryId)
                add(ResultKey.PRIMARY_ID, mrzResult.primaryId)

                FormattingUtils.formatResultTitle(mrzResult.secondaryId, mrzResult.primaryId)
            } else {
                add(ResultKey.FIRST_NAME, firstName)
                add(ResultKey.LAST_NAME, lastName)

                FormattingUtils.formatResultTitle(firstName, lastName)
            }

            if (fullName.isNotEmpty()) {
                add(ResultKey.FULL_NAME, fullName)
                title = fullName
            }

            return title
        }
    }

    private fun addMrzResults(mrzResult: MrzResult) {
        mrzResult.apply {
            if (isMrzParsed) {
                add(ResultKey.ALIEN_NUMBER, alienNumber)
                add(ResultKey.APPLICATION_RECEIPT_NUMBER, applicationReceiptNumber)
                add(ResultKey.DOCUMENT_CODE, documentCode)
                add(ResultKey.DOCUMENT_NUMBER, documentNumber)
                add(ResultKey.SEX, gender)
                add(ResultKey.IMMIGRANT_CASE_NUMBER, immigrantCaseNumber)
                add(ResultKey.ISSUER, issuer)
                add(ResultKey.MRZ_TEXT, mrzText)
                add(ResultKey.NATIONALITY, nationality)
                add(ResultKey.OPTIONAL_FIELD_1, opt1)
                add(ResultKey.OPTIONAL_FIELD_2, opt2)
                add(ResultKey.DATE_OF_BIRTH, dateOfBirth)
                addDateOfExpiry(dateOfExpiry.date)
            }
        }
    }
}