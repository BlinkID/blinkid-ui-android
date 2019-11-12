package com.microblink.documentscanflow.recognition.implementations

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.documentscanflow.recognition.BaseRecognition
import com.microblink.documentscanflow.recognition.ResultMergeException
import com.microblink.documentscanflow.recognition.util.BlinkIdRecognitionUtils
import com.microblink.entities.recognizers.Recognizer
import com.microblink.entities.recognizers.blinkid.DataMatchResult
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdRecognizer

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
                (combinedResult as com.microblink.entities.recognizers.blinkid.CombinedResult).documentDataMatch != DataMatchResult.Failed
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return when {
            frontResult.isNotEmpty() -> BlinkIdRecognitionUtils.extractFrontResult(frontResult, this)
            combinedResult.isNotEmpty() -> BlinkIdRecognitionUtils.extractCombinedResult(combinedResult, this)
            else -> null
        }
    }


}