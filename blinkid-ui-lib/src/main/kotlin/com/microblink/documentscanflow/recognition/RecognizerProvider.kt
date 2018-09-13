package com.microblink.documentscanflow.recognition

import com.microblink.entities.recognizers.Recognizer

abstract class RecognizerProvider {

    val recognizers by lazy { createRecognizers()}

    protected abstract fun createRecognizers(): List<Recognizer<*, *>>

}