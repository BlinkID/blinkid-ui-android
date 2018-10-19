package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.indonesia.IndonesiaIdFrontRecognizer

class IndonesiaIdRecognition : BaseRecognition() {

    val recognizer by lazy { IndonesiaIdFrontRecognizer() }

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        add(R.string.keyProvince, result.province)
        add(R.string.keyCity, result.city)
        add(R.string.keyDocumentNumber, result.documentNumber)
        add(R.string.keyFullName, result.name)
        add(R.string.keyPlaceOfBirth, result.placeOfBirth)
        add(R.string.keyDateOfBirth, result.dateOfBirth)
        add(R.string.keySex, result.sex)
        add(R.string.keyBloodGroup, result.bloodType)
        add(R.string.keyAddress, result.address)
        add(R.string.keyRt, result.rt )
        add(R.string.keyRw, result.rw )
        add(R.string.keyKelDesa, result.kelDesa)
        add(R.string.keyDistrict, result.district)
        add(R.string.keyReligion, result.religion)
        add(R.string.keyMaritalStatus, result.maritalStatus)
        add(R.string.keyOccupation, result.occupation)
        add(R.string.keyCitizenship, result.citizenship)
        add(R.string.keyDateOfExpiry, result.dateOfExpiry)
        add(R.string.keyDateOfExpiryPermanent, result.isDateOfExpiryPermanent)
        
        return result.name
    }
    
}