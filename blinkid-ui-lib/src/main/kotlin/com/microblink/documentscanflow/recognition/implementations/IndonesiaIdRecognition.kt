package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.SingleSideRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.indonesia.IndonesiaIdFrontRecognizer

class IndonesiaIdRecognition : SingleSideRecognition<IndonesiaIdFrontRecognizer.Result>() {

    override val recognizer by lazy { IndonesiaIdFrontRecognizer() }

    override fun extractData(result: IndonesiaIdFrontRecognizer.Result): String? {
        add(PROVINCE, result.province)
        add(CITY, result.city)
        add(DOCUMENT_NUMBER, result.documentNumber)
        add(FULL_NAME, result.name)
        add(PLACE_OF_BIRTH, result.placeOfBirth)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(SEX, result.sex)
        add(ADDRESS, result.address)
        add(RELIGION, result.religion)
        add(MARITAL_STATUS, result.maritalStatus)
        add(OCCUPATION, result.occupation)
        add(CITIZENSHIP, result.citizenship)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(DATE_OF_EXPIRY_PERMANENT, result.isDateOfExpiryPermanent)

        return result.name
    }
    
}