package com.microblink.documentscanflow.recognition.resultentry

import android.os.Parcel
import android.os.Parcelable
import com.microblink.documentscanflow.compareTo
import com.microblink.documentscanflow.currentDate
import com.microblink.results.date.Date
import java.util.*

abstract class ResultEntry<T>(val key: ResultKey, val value: T) : Parcelable {

    var isValid = true
        protected set

    override fun equals(other: Any?): Boolean {
        return other is ResultEntry<*> && other.key == this.key
    }

    override fun hashCode() = key.hashCode()

    protected fun writeValidityToParcel(dest: Parcel?) {
        dest?.writeInt((if (isValid) 1 else 0))
    }

    protected fun readValidityFromParcel(source: Parcel) {
        isValid = source.readInt() == 1
    }

    internal class Builder {

        fun build(key: ResultKey, value: String?) =
                StringResultEntry(key, value ?: "")

        fun build(key: ResultKey, value: Date) =
                build(key, value, DateCheckType.NO_CHECK)

        fun build(key: ResultKey, amount: Int, unitName: String) =
                StringResultEntry(key, "$amount $unitName")

        fun build(key: ResultKey, value: Int) =
                StringResultEntry(key, value.toString())

        fun build(key: ResultKey, value: Boolean) =
                StringResultEntry(key, value.toString())

        fun build(key: ResultKey, value: Date, checkType: Builder.DateCheckType): ResultEntry<*> {
            val entry = DateResultEntry(key, value)
            val currentDate = GregorianCalendar.getInstance().currentDate()

            entry.isValid = checkType.check(currentDate, value)
            return entry
        }

        enum class DateCheckType {
            NO_CHECK {
                override fun check(currentDate: Date, dateToCheck: Date) = true
            },
            IN_PAST {
                override fun check(currentDate: Date, dateToCheck: Date) = currentDate.compareTo(dateToCheck) > 0
            },
            IN_FUTURE {
                override fun check(currentDate: Date, dateToCheck: Date) = currentDate.compareTo(dateToCheck) < 0
            };

            abstract fun check(currentDate: Date, dateToCheck: Date): Boolean
        }
    }

}