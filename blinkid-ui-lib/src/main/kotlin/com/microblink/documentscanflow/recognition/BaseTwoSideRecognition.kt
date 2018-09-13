package com.microblink.documentscanflow.recognition

abstract class BaseTwoSideRecognition
    : BaseRecognition() {

    override fun extractData(): String? {
        if (shouldValidate) {
            val validator = createValidator()
            if (!validator.isResultValid) {
                throw ResultMergeException()
            }
        }

        extractFields()
        return getResultTitle()
    }

    internal abstract fun createValidator(): ResultValidator

    abstract fun extractFields()

    abstract fun getResultTitle(): String?

}
