package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.isNotEmpty
import com.microblink.entities.recognizers.Recognizer

abstract class CombinedRecognition<FrontResult : Recognizer.Result,
        BackResult : Recognizer.Result,
        CombinedResult: Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val frontRecognizer: Recognizer<FrontResult>
    abstract val backRecognizer: Recognizer<BackResult>
    abstract val combRecognizer: Recognizer<CombinedResult>

    val frontResult by lazy { frontRecognizer.result }
    val backResult by lazy { backRecognizer.result }
    val combinedResult by lazy { combRecognizer.result }

    override fun getSingleSideRecognizers() = listOf(frontRecognizer, backRecognizer)

    override fun getCombinedRecognizer() = combRecognizer

    override fun extractData(): String? {
        if (shouldValidate) {
            val isValid = (combinedResult as com.microblink.entities.recognizers.blinkid.CombinedResult).isDocumentDataMatch
            if (!isValid) {
                throw ResultMergeException()
            }
        }
        return when {
            combinedResult.isNotEmpty() -> extractCombinedResult(combinedResult)
            frontResult.isNotEmpty() -> extractFrontResult(frontResult)
            backResult.isNotEmpty() -> extractBackResult(backResult)
            else -> null
        }
    }

    abstract fun extractCombinedResult(combinedResult: CombinedResult): String?

    abstract fun extractFrontResult(frontResult: FrontResult): String?

    abstract fun extractBackResult(backResult: BackResult): String?

}