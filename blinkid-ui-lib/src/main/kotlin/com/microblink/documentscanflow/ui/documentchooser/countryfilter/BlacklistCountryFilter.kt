package com.microblink.documentscanflow.ui.documentchooser.countryfilter

import android.os.Parcel
import android.os.Parcelable
import com.microblink.documentscanflow.country.Country

class BlacklistCountryFilter(private val blacklistedCountryCodes : Set<String>) : CountryFilter {

    override fun filter(countries: List<Country>) = countries.filter { !blacklistedCountryCodes.contains(it.code) }

    constructor(parcel: Parcel) : this(mutableSetOf<String>(*parcel.createStringArray()))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            dest.writeStringArray(blacklistedCountryCodes.toTypedArray())
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BlacklistCountryFilter> {
        override fun createFromParcel(parcel: Parcel): BlacklistCountryFilter {
            return BlacklistCountryFilter(parcel)
        }

        override fun newArray(size: Int): Array<BlacklistCountryFilter?> {
            return arrayOfNulls(size)
        }
    }

}