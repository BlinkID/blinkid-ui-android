package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult

class BlinkIdRecognition :
    CombinedRecognition<BlinkIdRecognizer.Result, BlinkIdRecognizer.Result, BlinkIdCombinedRecognizer.Result>() {


    override val frontRecognizer by lazy { BlinkIdRecognizer() }
    override val backRecognizer by lazy { BlinkIdRecognizer() }
    override val combinedRecognizer by lazy { BlinkIdCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: BlinkIdCombinedRecognizer.Result): String? {
        // empty field
        add(ResultKey.SEX, combinedResult.sex)
        add(ResultKey.ADDRESS, combinedResult.address)
        add(ResultKey.PLACE_OF_BIRTH, combinedResult.placeOfBirth)
        add(ResultKey.NATIONALITY, combinedResult.nationality)
        add(ResultKey.RACE, combinedResult.race)
        add(ResultKey.RELIGION, combinedResult.religion)
        add(ResultKey.OCCUPATION, combinedResult.profession)
        add(ResultKey.MARITAL_STATUS, combinedResult.maritalStatus)
        add(ResultKey.RESIDENTIAL_STATUS, combinedResult.residentialStatus)
        add(ResultKey.EMPLOYER, combinedResult.employer)
        add(ResultKey.DOCUMENT_NUMBER, combinedResult.documentNumber)
        add(ResultKey.PERSONAL_NUMBER, combinedResult.personalIdNumber)
        add(ResultKey.ISSUING_AUTHORITY, combinedResult.issuingAuthority)
        add(ResultKey.DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(ResultKey.DATE_OF_ISSUE, combinedResult.dateOfIssue)
        addDateOfExpiry(combinedResult.dateOfExpiry.date)

        add(ResultKey.VEHICLE_CLASS, combinedResult.driverLicenseDetailedInfo.vehicleClass)
        add(ResultKey.ENDORSEMENTS, combinedResult.driverLicenseDetailedInfo.endorsements)
        add(ResultKey.DRIVER_RESTRICTIONS, combinedResult.driverLicenseDetailedInfo.restrictions)

        combinedResult.mrzResult.add()

        var title = if (combinedResult.mrzResult.isMrzParsed) {
            add(ResultKey.SECONDARY_ID, combinedResult.mrzResult.secondaryId)
            add(ResultKey.PRIMARY_ID, combinedResult.mrzResult.primaryId)

            FormattingUtils.formatResultTitle(combinedResult.mrzResult.secondaryId, combinedResult.mrzResult.primaryId)
        } else {
            add(ResultKey.FIRST_NAME, combinedResult.firstName)
            add(ResultKey.LAST_NAME, combinedResult.lastName)

            FormattingUtils.formatResultTitle(combinedResult.firstName, combinedResult.lastName)
        }

        if (combinedResult.fullName.isNotEmpty()) {
            add(ResultKey.FULL_NAME, combinedResult.fullName)
            title = combinedResult.fullName
        }

        return title
    }

    override fun extractFrontResult(frontResult: BlinkIdRecognizer.Result): String? {
        return frontResult.add()
    }

    override fun extractBackResult(backResult: BlinkIdRecognizer.Result): String? {
        return backResult.add()
    }

    private fun BlinkIdRecognizer.Result.add(): String? {
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

        mrzResult.add()

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

    private fun MrzResult.add() {
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