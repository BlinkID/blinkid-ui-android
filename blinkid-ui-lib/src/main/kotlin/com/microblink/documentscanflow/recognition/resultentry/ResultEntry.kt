package com.microblink.documentscanflow.recognition.resultentry

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.microblink.documentscanflow.compareTo
import com.microblink.documentscanflow.currentDate
import com.microblink.results.date.Date
import java.util.*

abstract class ResultEntry<T>(val key: String, val value: T) : Parcelable {

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

    internal class Builder(private val context: Context) {

        fun build(@StringRes keyResId: Int, value: String?) =
                StringResultEntry(context.getString(keyResId), value ?: "")

        fun build(@StringRes key: Int, value: Date?) =
                build(key, value, DateCheckType.NO_CHECK)

        fun build(@StringRes key: Int, amount: Int, unitName: String) =
                StringResultEntry(context.getString(key), amount.toString() + " " + unitName)

        fun build(@StringRes key: Int, value: Int) =
                StringResultEntry(context.getString(key), value.toString())

        fun build(@StringRes key: Int, value: Boolean) =
                StringResultEntry(context.getString(key), value.toString())

        fun build(@StringRes key: Int, value: Date?, checkType: Builder.DateCheckType): ResultEntry<*> {
            if (value == null) {
                return StringResultEntry(context.getString(key), "")
            }

            val entry = DateResultEntry(context.getString(key), value)
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