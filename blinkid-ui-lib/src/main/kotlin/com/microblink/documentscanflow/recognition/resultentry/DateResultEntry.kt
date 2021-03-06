package com.microblink.documentscanflow.recognition.resultentry

import android.os.Parcel
import android.os.Parcelable
import com.microblink.results.date.Date

class DateResultEntry(key: ResultKey, value: Date) : ResultEntry<Date>(key, value) {

    constructor(source: Parcel) : this(source.readSerializable() as ResultKey, Date(source.readInt(), source.readInt(), source.readInt())) {
        readValidityFromParcel(source)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeSerializable(key)
        dest?.writeInt(value.day)
        dest?.writeInt(value.month)
        dest?.writeInt(value.year)
        writeValidityToParcel(dest)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<DateResultEntry> = object : Parcelable.Creator<DateResultEntry> {
            override fun createFromParcel(source: Parcel): DateResultEntry = DateResultEntry(source)
            override fun newArray(size: Int): Array<DateResultEntry?> = arrayOfNulls(size)
        }
    }
}