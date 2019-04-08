package com.microblink.documentscanflow.recognition.config

class DefaultRecognitionConfig: RecognitionConfig {

    override fun getRecognitionTimeoutSeconds() = 10

    override fun isPartialResultAllowed() = false
    
}