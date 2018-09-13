package com.microblink.documentscanflow.recognition.util

internal object FormattingUtils {

    fun formatResultTitle(firstName: String?, lastName: String?): String {
        val sb = StringBuilder()
        sb.append(lastName ?: "").append(" ").append(firstName ?: "")
        return sb.toString()
    }
}