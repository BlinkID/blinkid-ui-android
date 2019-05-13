package com.microblink.documentscanflow.recognition

import com.microblink.entities.recognizers.Recognizer

abstract class TwoSideRecognition<FrontResult : Recognizer.Result, BackResult : Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val frontRecognizer: Recognizer<FrontResult>
    abstract val backRecognizer: Recognizer<BackResult>

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }

    override fun getSingleSideRecognizers(): List<Recognizer<*>> = listOf(frontRecognizer, backRecognizer)

    override fun extractData(): String? {
        if (shouldValidate) {
            val isValid = createValidator().isResultValid
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return extractData(frontResult, backResult)
    }

    abstract fun extractData(frontResult: FrontResult, backResult: BackResult): String?

    internal open fun createValidator() = ResultValidator()

}