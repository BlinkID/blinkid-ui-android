package com.microblink.documentscanflow.recognition.implementations

import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys

class NigeriaDlRecognition: UsdlRecognition() {

    override val keysToExcludeFromResults: Set<UsdlKeys>
        get() = setOf(UsdlKeys.Height, UsdlKeys.HeightCm, UsdlKeys.HeightIn)

}