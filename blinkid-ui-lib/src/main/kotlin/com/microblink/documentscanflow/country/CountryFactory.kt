package com.microblink.documentscanflow.country

import com.microblink.documentscanflow.ui.documentchooser.countryfilter.CountryFilter
import java.text.Collator
import java.util.*

object CountryFactory {

    var currentLocale: Locale = Locale.ENGLISH
        private set

    private val countriesByCode = HashMap<String, Country>()

    private lateinit var sortedCountries: List<Country>

    private var shouldSortCountries = true

    init {
        for (supportedCountry in SupportedCountry.values()) {
            countriesByCode[supportedCountry.code] = supportedCountry
        }

        val countryCodes: Array<String> = Locale.getISOCountries()
        for (countryCode in countryCodes) {
            val countryCodeLowercase = countryCode.toLowerCase(Locale.US)
            if (!countriesByCode.containsKey(countryCodeLowercase)) {
                val country = GenericCountry(countryCodeLowercase)
                if (!country.getLocalisedName().isEmpty()) {
                    countriesByCode[countryCodeLowercase] = country
                }
            }
        }
    }

    fun getCountryForCode(countryCode: String): Country? {
        val lowercaseCode = countryCode.toLowerCase(Locale.US)
        return countriesByCode[lowercaseCode]
    }

    fun getSortedCountries(countryFilter: CountryFilter): List<Country> {
        if (shouldSortCountries) {
            sortedCountries = createSortedCountriesList()
            shouldSortCountries = false
        }
        return countryFilter.filter(sortedCountries)
    }

    internal fun setLocale(newLocale: Locale) {
        if (currentLocale.language != newLocale.language) {
            shouldSortCountries = true
        }
        currentLocale = newLocale
    }

    private fun createSortedCountriesList(): List<Country> {
        val coll = Collator.getInstance()
        coll.strength = Collator.SECONDARY
        return countriesByCode.values
                .sortedWith(
                        Comparator {
                            country1, country2 ->
                            val name1 = country1.getLocalisedName()
                            val name2 = country2.getLocalisedName()
                            if (name1.isNotEmpty() && name2.isNotEmpty() && name1[0] != name2[0]) {
                                coll.compare(name1.substring(0, 1), name2.substring(0, 1))
                            } else {
                                coll.compare(name1, name2)
                            }
                        })
    }

}