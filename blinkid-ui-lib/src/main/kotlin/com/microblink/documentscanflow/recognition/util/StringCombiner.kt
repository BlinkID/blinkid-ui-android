package com.microblink.documentscanflow.recognition.util

/**
 * This class can be used to combine string results for the same information
 * to produce better (more accurate) result.
 *
 * @param country Country(language) for which the combiner's allowed character substitutions
 * will be prepared.
 */
internal class StringCombiner(val country: Country?) {

    private companion object {
        /** Cost when character is inserted or removed */
        @JvmStatic private val CHAR_ADD_DEL_COST = 1
        /** Cost when character is replaced with char that is not in the substitution map */
        @JvmStatic private val CHAR_REPLACE_COST = 1
        /** Cost when character is replaced with char from the substitution map */
        @JvmStatic private val CHAR_SUBSTITUTION_COST = 0
    }

    enum class Country {
        AUSTRIA, CROATIA, CZECHIA, GERMANY, POLAND, SLOVAKIA, SWITZERLAND
    }

    private val mCharSubstitutionMap: MutableMap<Char, MutableSet<Char>> =  mutableMapOf()
    private val mMRZSubstitutionMap: MutableMap<String, Char> = mutableMapOf()

    init {
        when (country) {
            Country.CROATIA -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createCroatianSpecificSubstitutions())
                // mrz substitutions should be empty
            }
            Country.POLAND -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createPolishSpecificSubstitutions())
            }
            Country.AUSTRIA,
            Country.SWITZERLAND -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createGermanSpecificSubstitutions())
                mergeToSubstitutionMap(mCharSubstitutionMap, createCroatianSpecificSubstitutions())
                mMRZSubstitutionMap.putAll(crateGermanMRZSubstitutions())
            }
            Country.CZECHIA -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createCzechSpecificSubstitutions())
            }
            Country.GERMANY -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createGermanSpecificSubstitutions())
                mMRZSubstitutionMap.putAll(crateGermanMRZSubstitutions())
            }
            Country.SLOVAKIA -> {
                mergeToSubstitutionMap(mCharSubstitutionMap, createSlovakSpecificSubstitutions())
            }
        }
    }

    /**
     * Combines mrzString with decoratedString which possibly contains more information
     * (eg. string from the front side of the ID card) based on combiner's country that defines which
     * character substitutions are allowed.
     *
     * @param mrzString whose alphabet is restricted to A–Z. This is base string with higher confidence
     * (but possibly with some characters that are not completely accurate, eg. Croatian specific letters like č,ć,ž,š,đ
     * may be substituted with closest A–Z letters)
     * whose characters may be substituted with characters from
     * decoratedString (depends on chosen country) to produce the final result. Also in mrzString some
     * country specific characters can be transliterated (eg. original ä → AE)
     *
     * @param decoratedString string which probably holds more precise country specific characters,
     * but whose overall confidence is lower.
     *
     * @return Combination result.
     *
     */
    fun combineMRZString(mrzString: String?, decoratedString: String?): String  {
        if (mrzString == null) {
            return decoratedString ?: ""
        }
        if (decoratedString == null) {
            return mrzString
        }

        var substitutedMrz: String = mrzString
        var curPos = 0
        var curDistance = calcDistance(substitutedMrz, decoratedString, true)
        if (!mMRZSubstitutionMap.isEmpty()) {
            while (curPos < substitutedMrz.length) {
                for ((key, value) in mMRZSubstitutionMap) {
                    if (substitutedMrz.startsWith(key, curPos, true)) {
                        val newSubstitutedMrz = substitutedMrz.replaceFirst(key, value.toString(), true)
                        val newDistance = calcDistance(newSubstitutedMrz, decoratedString, true)
                        if (newDistance <= curDistance) {
                            substitutedMrz = newSubstitutedMrz
                            curDistance = newDistance
                        }
                        break
                    }
                }
                if (curDistance == 0) {
                    break
                }
                curPos++
            }
        }

        return combineStrings(substitutedMrz, decoratedString).combinationResult
    }

    /**
     * Combines two strings based on combiner's country that defines which character
     * substitutions are allowed. First, decoratedString is aligned with originalString. Then
     * every character from the originalString, for which exists allowed substitution to character
     * from the decoratedString which is at the same position (after alignment), is substituted with
     * corresponding character from decoratedString to produce final result.
     *
     * @param originalString base string with higher confidence (but possibly with some characters
     * that are not completely accurate, eg. Croatian specific letters like č,ć,ž,š,đ may be substituted
     * with closest letters from the English alphabet) whose characters may be substituted with characters from
     * decoratedString (depends on chosen country) to produce the final result.
     *
     * @param decoratedString string which probably holds more precise country specific characters,
     * but whose overall confidence is lower.
     *
     * @return Result contains string whose length is the same as the length of the originalString and in most cases
     * with content copied from it, with possibly some country specific characters substituted
     * with corresponding characters from the decoratedString - if character was uppercase(lowercase)
     * in originalString it will remain uppercase(lowercase). AND other part of the result is initial
     * distance between originalString and decoratedString (similar to Levenshtein distance with different costs
     * for: addition/removal - [CHAR_ADD_DEL_COST], substitution - [CHAR_SUBSTITUTION_COST] and replacement - [CHAR_REPLACE_COST]).
     *
     */
    private fun combineStrings(originalString: String, decoratedString: String): CombineResult {
        // this method uses dynamic programming approach, similar to calculating
        // the Levenshtein distance between two strings with different costs for addition/removal,
        // substitution and replacement
        val d = calcDistanceMatrix(originalString, decoratedString, true)

        val m = originalString.length
        val n = decoratedString.length

        // indexes in matrix start at 1
        val original = ("*$originalString").toCharArray()
        val decorated = ("*$decoratedString").toCharArray()

        val sb = StringBuilder()

        var i = m
        var j = n
        while (i > 0 && j > 0) {
            val curCost = d[i][j]
            val substitutionCost = calcSubstitutionCost(original[i], decorated[j], true)
            when {
                curCost - d[i-1][j-1] == substitutionCost -> {
                    // diagonal move in table d
                    if (substitutionCost == 0) {
                        var upperCase = false
                        if (original[i].isUpperCase()) {
                            upperCase = true
                        }
                        sb.append(if (upperCase) decorated[j].toUpperCase() else decorated[j].toLowerCase())
                    } else {
                        sb.append(original[i])
                    }
                    i--
                    j--
                }
                curCost - CHAR_ADD_DEL_COST == d[i-1][j] -> {
                    // up move in table d which means that original character should be added
                    sb.append(original[i])
                    i--
                }
                curCost - CHAR_ADD_DEL_COST == d[i][j - 1] -> // left move in table d which means that decorated character should be removed
                    // do nothing
                    j--
                else -> throw IllegalStateException("Error while combining strings!")
            }
        }
        while (i > 0) {
            sb.append(original[i])
            i--
        }

        // reverse built string because letters have been inserted from last to first
        return CombineResult(sb.reverse().toString(), d[m][n])
    }

    fun calcDistance(originalString: String, decoratedString: String, allowCountrySpecificSubstitutions: Boolean): Int {
        val m = originalString.length
        val n = decoratedString.length

        val distanceMatrix = calcDistanceMatrix(originalString, decoratedString, allowCountrySpecificSubstitutions)
        return distanceMatrix[m][n]
    }

    private fun calcDistanceMatrix(originalString: String, decoratedString: String, allowCountrySpecificSubstitutions: Boolean): Array<IntArray> {
        // this method uses dynamic programming approach, similar to calculating
        // the Levenshtein distance between two strings with different costs for addition/removal,
        // substitution and replacement

        val m = originalString.length
        val n = decoratedString.length

        val original = ("*$originalString").toCharArray()
        val decorated = ("*$decoratedString").toCharArray()

        val d = Array(m + 1) { IntArray(n + 1) }

        //init first column
        for (i in 1..m) {
            d[i][0] = i
        }

        //init first row
        for (j in 1..n) {
            d[0][j] = j
        }

        for (j in 1..n) {
            for (i in 1..m) {
                d[i][j] = intArrayOf (
                        d[i-1][j] + CHAR_ADD_DEL_COST,
                        d[i][j-1] + CHAR_ADD_DEL_COST,
                        d[i-1][j-1] + calcSubstitutionCost(original[i], decorated[j], allowCountrySpecificSubstitutions)
                ).min()!!
            }
        }

        return d
    }

    /**
     * Calculates the cost for substituting the originalChar with decoratedChar which is
     * [CHAR_REPLACE_COST] if they are different and substitution is not allowed by current country
     * settings, [CHAR_SUBSTITUTION_COST] if substitution is allowed, or 0 if characters are the same.
     * Case is ignored.
     */
    private fun calcSubstitutionCost(originalChar: Char, decoratedChar: Char, allowCountrySpecificSubstitutions: Boolean): Int {
        val originalLowercase = originalChar.toLowerCase()
        val decoratedLowercase = decoratedChar.toLowerCase()
        if (originalLowercase == decoratedLowercase || (allowCountrySpecificSubstitutions && (mCharSubstitutionMap[originalLowercase]?.contains(decoratedLowercase) == true))) {
            return 0
        }
        return 1
    }

    /**
     * @param combinationResult Result of combining strings
     * @param initialDistance Initial distance between strings before combining them.
     */
    private data class CombineResult(val combinationResult: String, val initialDistance: Int)


    private fun createCroatianSpecificSubstitutions(): Map<Char, Set<Char>> {
        return mapOf(
                'c' to setOf('č', 'ć'),
                'd' to setOf('đ'),
                'z' to setOf('ž'),
                's' to setOf('š')
        )
    }

    private fun createGermanSpecificSubstitutions(): Map<Char, Set<Char>> {
        return mapOf(
                'a' to setOf('ä'),
                'o' to setOf('ö'),
                'u' to setOf('ü'),
                's' to setOf('ß')
        )
    }

    private fun createCzechSpecificSubstitutions(): Map<Char, Set<Char>> {
        return mapOf(
                'a' to setOf('á'),
                'c' to setOf('č'),
                'd' to setOf('ď'),
                'e' to setOf('é', 'ě'),
                'i' to setOf('í'),
                'n' to setOf('ň'),
                'o' to setOf('ó'),
                'r' to setOf('ř'),
                's' to setOf('š'),
                't' to setOf('ť'),
                'u' to setOf('ú', 'ů'),
                'y' to setOf('ý'),
                'z' to setOf('ž')
        )
    }

    private fun createSlovakSpecificSubstitutions(): Map<Char, Set<Char>> {
        return mapOf(
                'a' to setOf('á', 'ä'),
                'c' to setOf('č'),
                'd' to setOf('ď'),
                'e' to setOf('é'),
                'i' to setOf('í'),
                'l' to setOf('ĺ', 'ľ'),
                'n' to setOf('ň'),
                'o' to setOf('ó', 'ô'),
                'r' to setOf('ŕ'),
                's' to setOf('š'),
                't' to setOf('ť'),
                'u' to setOf('ú'),
                'y' to setOf('ý'),
                'z' to setOf('ž')
        )
    }

    private fun createPolishSpecificSubstitutions(): Map<Char, Set<Char>> {
        return mapOf(
                'a' to setOf('ą'),
                'c' to setOf('ć'),
                'e' to setOf('ę'),
                'l' to setOf('ł'),
                'n' to setOf('ń'),
                'o' to setOf('ó'),
                's' to setOf('ś'),
                'z' to setOf('ź', 'ż')
        )
    }

    private fun crateGermanMRZSubstitutions(): Map<String, Char> {
        return mapOf(
                "AE" to 'Ä',
                "OE" to 'Ö',
                "UE" to 'Ü',
                "SS" to 'ß'
        )
    }

    private fun mergeToSubstitutionMap(substitutionMap: MutableMap<Char, MutableSet<Char>>, substitutionsToAdd: Map<Char, Set<Char>>) {
        for ((key, value) in substitutionsToAdd) {
            if (substitutionMap.containsKey(key)) {
                substitutionMap[key]!!.addAll(value)
            } else {
                substitutionMap[key] = value.toMutableSet()
            }
        }
    }

}