package com.microblink.documentscanflow.recognition.util

import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer
import com.microblink.entities.recognizers.blinkid.mrtd.MrzResult

object BlinkIdRecognitionUtils {

    fun extractCombinedResult(combinedResult: BlinkIdCombinedRecognizer.Result,
                                      recognition: BaseRecognition): String? {
        combinedResult.apply {
            recognition.apply {
                recognition.add(ResultKey.SEX, sex)
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

                addMrzResults(mrzResult, recognition)

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
    }

    fun extractFrontResult(frontResult: BlinkIdRecognizer.Result,
                                   recognition: BaseRecognition): String? {
        frontResult.apply {
            recognition.apply {
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

                addMrzResults(mrzResult, recognition)

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
    }

    private fun addMrzResults(mrzResult: MrzResult, recognition: BaseRecognition) {
        mrzResult.apply {
            if (isMrzParsed) {
                recognition.apply {
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

}