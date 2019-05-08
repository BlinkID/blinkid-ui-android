package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.documentscanflow.buildId1CardDetectorRecognizer
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.newzealand.NewZealandDlFrontRecognizer

class NewZealandDlRecognition : BaseRecognition() {

    val frontRecognizer by lazy { NewZealandDlFrontRecognizer() }
    val backRecognizer by lazy { buildId1CardDetectorRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> {
        return listOf(frontRecognizer, backRecognizer)
    }

    override fun extractData(): String? {
        val result = frontRecognizer.result
        if (result.isEmpty()) {
            return null
        }

        val firstName = result.firstNames
        val lastName = result.surname

        add(FIRST_NAME, firstName)
        add(LAST_NAME, lastName)
        add(DATE_OF_BIRTH, result.dateOfBirth)
        add(DOCUMENT_NUMBER, result.licenseNumber)
        add(DATE_OF_ISSUE, result.dateOfIssue)
        addDateOfExpiry(result.dateOfExpiry.date)
        add(ADDRESS, result.address)
        
        return FormattingUtils.formatResultTitle(firstName, lastName)
    }
    
}