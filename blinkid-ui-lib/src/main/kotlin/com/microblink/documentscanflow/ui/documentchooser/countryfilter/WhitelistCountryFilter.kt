package com.microblink.documentscanflow.ui.documentchooser.countryfilter

import android.os.Parcel
import android.os.Parcelable
import com.microblink.documentscanflow.country.Country

class WhitelistCountryFilter(private val whitelistedCountryCodes : Set<String>) : CountryFilter {

    override fun filter(countries: List<Country>) = countries.filter { whitelistedCountryCodes.contains(it.code) }

    constructor(parcel: Parcel) : this(mutableSetOf<String>(*parcel.createStringArray()))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringArray(whitelistedCountryCodes.toTypedArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WhitelistCountryFilter> {
        override fun createFromParcel(parcel: Parcel): WhitelistCountryFilter {
            return WhitelistCountryFilter(parcel)
        }

        override fun newArray(size: Int): Array<WhitelistCountryFilter?> {
            return arrayOfNulls(size)
        }
    }

}