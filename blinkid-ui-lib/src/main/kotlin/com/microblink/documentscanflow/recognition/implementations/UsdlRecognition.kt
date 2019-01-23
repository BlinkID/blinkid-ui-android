package com.microblink.documentscanflow.recognition.implementations

import android.text.TextUtils
import com.microblink.documentscanflow.isEmpty
import com.microblink.documentscanflow.recognition.resultentry.StringResultEntry
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.util.FormattingUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlKeys
import com.microblink.entities.recognizers.blinkbarcode.usdl.UsdlRecognizer
import com.microblink.entities.recognizers.blinkid.documentface.DocumentFaceRecognizer

open class UsdlRecognition : BaseRecognition() {

    private val recognizer by lazy { UsdlRecognizer() }
    private val faceRecognizer by lazy { DocumentFaceRecognizer() }

    protected open val keysToExcludeFromResults
        get() = setOf<UsdlKeys>()

    override fun getSingleSideRecognizers(): List<Recognizer<*, *>> {
        return listOf(faceRecognizer, recognizer)
    }

    override fun extractData(): String? {
        val result = recognizer.result
        if (result.isEmpty()) {
            return null
        }

        val sb = StringBuilder()
        sb.append(result.getField(UsdlKeys.CustomerFirstName))
        val middleName = result.getField(UsdlKeys.CustomerMiddleName)
        if (!middleName.isEmpty()) {
            sb.append(" ").append(middleName)
        }
        val fullName = FormattingUtils.formatResultTitle(
                sb.toString(),
                result.getField(UsdlKeys.CustomerFamilyName))

        for (key in UsdlKeys.values()) {
            val value = result.getField(key)
            if (!TextUtils.isEmpty(value) && !keysToExcludeFromResults.contains(key)) {
                resultEntries.add(StringResultEntry(key.name, value))
            }
        }

        return fullName
    }

}