package com.microblink.documentscanflow.recognition.resultentry

import android.os.Parcel
import android.os.Parcelable

class StringResultEntry(key: ResultKey, value: String) : ResultEntry<String>(key, value) {

    constructor(source: Parcel) : this(source.readSerializable() as ResultKey, source.readString()) {
        readValidityFromParcel(source)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeSerializable(key)
        dest?.writeString(value)
        writeValidityToParcel(dest)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<StringResultEntry> = object : Parcelable.Creator<StringResultEntry> {
            override fun createFromParcel(source: Parcel): StringResultEntry = StringResultEntry(source)
            override fun newArray(size: Int): Array<StringResultEntry?> = arrayOfNulls(size)
        }
    }
}