package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.isEmpty
import com.microblink.entities.recognizers.Recognizer

abstract class SingleSideRecognition<FrontResult : Recognizer.Result>
    (isFullySupported: Boolean = true) : BaseRecognition(isFullySupported) {

    abstract val recognizer: Recognizer<FrontResult>

    val result by lazy { recognizer.result }

    override fun getSingleSideRecognizers() = listOf(recognizer)

    override fun extractData() =
        if (result.isEmpty()) null
        else extractData(result)

    abstract fun extractData(result: FrontResult): String?

}