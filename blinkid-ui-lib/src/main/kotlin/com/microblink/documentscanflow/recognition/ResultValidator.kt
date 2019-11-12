package com.microblink.documentscanflow.recognition

import com.microblink.documentscanflow.compareTo
import com.microblink.documentscanflow.recognition.util.StringCombiner
import com.microblink.entities.recognizers.blinkid.CombinedResult
import com.microblink.entities.recognizers.blinkid.DataMatchResult
import com.microblink.results.date.Date
import com.microblink.results.date.DateResult

internal class ResultValidator {

    private val stringCombiner = StringCombiner(null)
    var isResultValid = true
        private set

    fun match(combinedResult: CombinedResult?): ResultValidator {
        isResultValid = combinedResult?.documentDataMatch != DataMatchResult.Failed
        return this
    }

    fun match(str1: String?, str2: String?, maxAllowedDistance: Int = 1): ResultValidator {
        val isValid = validateString(str1, str2, maxAllowedDistance)
        if (!isValid) {
            isResultValid = false
        }
        return this
    }

    fun match(date1: DateResult?, date2: DateResult?): ResultValidator {
        return match(date1?.date, date2?.date)
    }

    fun match(date1: Date?, date2: Date?): ResultValidator {
        val isValid = validateDate(date1, date2)
        if (!isValid) {
            isResultValid = false
        }

        return this
    }

    private fun validateString(str1: String?, str2: String?, maxAllowedDistance: Int): Boolean {
        if (str1 == null || str2 == null) {
            if (str1 != str2) {
                return false
            }
        } else if (stringCombiner.calcDistance(str1, str2, false) > maxAllowedDistance) {
            return false
        }
        return true
    }

    private fun validateDate(d1: Date?, d2: Date?): Boolean {
        if (d1 == null || d2 == null) {
            if (d1 != d2) {
                return false
            }
        } else if (d1.compareTo(d1) != 0){
            return false
        }
        return true
    }

}