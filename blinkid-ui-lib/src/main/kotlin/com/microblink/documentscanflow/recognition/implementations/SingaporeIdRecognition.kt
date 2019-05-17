package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.recognition.CombinedRecognition
import com.microblink.documentscanflow.recognition.resultentry.ResultKey.*
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeIdBackRecognizer
import com.microblink.entities.recognizers.blinkid.singapore.SingaporeIdFrontRecognizer

class SingaporeIdRecognition: CombinedRecognition<SingaporeIdFrontRecognizer.Result, SingaporeIdBackRecognizer.Result, SingaporeCombinedRecognizer.Result>() {

    override val frontRecognizer by lazy { SingaporeIdFrontRecognizer() }
    override val backRecognizer by lazy { SingaporeIdBackRecognizer() }
    override val combinedRecognizer by lazy { SingaporeCombinedRecognizer() }

    override fun extractCombinedResult(combinedResult: SingaporeCombinedRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, combinedResult.identityCardNumber)
        add(FULL_NAME, combinedResult.name)
        add(RACE, combinedResult.race)
        add(DATE_OF_BIRTH, combinedResult.dateOfBirth)
        add(SEX, combinedResult.sex)
        add(COUNTRY_OF_BIRTH, combinedResult.countryOfBirth)
        add(BLOOD_GROUP, combinedResult.bloodGroup)
        add(DATE_OF_ISSUE, combinedResult.dateOfIssue)
        add(ADDRESS, combinedResult.address)
        return combinedResult.name
    }

    override fun extractFrontResult(frontResult: SingaporeIdFrontRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, frontResult.identityCardNumber)
        add(FULL_NAME, frontResult.name)
        add(RACE, frontResult.race)
        add(DATE_OF_BIRTH, frontResult.dateOfBirth)
        add(SEX, frontResult.sex)
        add(COUNTRY_OF_BIRTH, frontResult.countryOfBirth)
        return frontResult.name
    }

    override fun extractBackResult(backResult: SingaporeIdBackRecognizer.Result): String? {
        add(DOCUMENT_NUMBER, backResult.cardNumber)
        add(DATE_OF_ISSUE, backResult.dateOfIssue)
        add(ADDRESS, backResult.address)
        return null
    }
    
}