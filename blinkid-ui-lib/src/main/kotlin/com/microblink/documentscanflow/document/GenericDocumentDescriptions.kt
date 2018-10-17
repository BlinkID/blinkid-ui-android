package com.microblink.documentscanflow.document

import com.microblink.documentscanflow.R
import com.microblink.documentscanflow.recognition.implementations.GenericRecognition

internal class GenericDocumentDescriptions {

    companion object {
        val residencePermit = DocumentDescription(
                true,
                R.string.mb_residence_permit,
                GenericRecognition.faceMrtd()
        )

        val id = DocumentDescription(
                false,
                R.string.mb_id_card,
                GenericRecognition.faceMrtd()
        )

        val drivingLicenceId1FormatUnsupported = DocumentDescription(
                false,
                R.string.mb_driver_license,
                GenericRecognition.faceId1()
        )

        val drivingLicenceId1FormatSupported = DocumentDescription(
                true,
                R.string.mb_driver_license,
                GenericRecognition.faceId1()
        )

        val passport = DocumentDescription(
                true,
                R.string.mb_passport,
                GenericRecognition.mrtd()
        )

        val visa = DocumentDescription(
                true,
                R.string.mb_visa,
                GenericRecognition.mrtd()
        )
    }

}

