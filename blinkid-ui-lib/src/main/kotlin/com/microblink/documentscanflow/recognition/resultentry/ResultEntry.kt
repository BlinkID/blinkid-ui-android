package com.microblink.documentscanflow.recognition.resultentry

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.microblink.documentscanflow.compareTo
import com.microblink.results.date.Date
import java.util.*

abstract class ResultEntry<T>(val key: String, val value: T) : Parcelable {

    protected var mValid = true

    protected var mInvalidReasonRes: Int = 0

    fun isValid(): Boolean {
        return mValid
    }

    /**
     * Returns invalid reason message resource ID, or <code>0</code> if reason is not set.
     */
    @StringRes
    fun getInvalidReasonRes(): Int {
        return mInvalidReasonRes
    }

    internal class Builder(private val context: Context) {

        enum class DateCheckType {
            NO_CHECK, IN_PAST, IN_FUTURE
        }

        fun build(@StringRes keyResId: Int, value: String?): StringResultEntry {
            return StringResultEntry(context.getString(keyResId), value ?: "")
        }

        fun build(@StringRes key: Int, value: Date?, checkType: DateCheckType, @StringRes errorMessageResource: Int = 0): ResultEntry<*> {
            if (value != null) {
                val currentCalendar = GregorianCalendar.getInstance()
                val currentDate = Date(
                        currentCalendar.get(Calendar.DAY_OF_MONTH),
                        currentCalendar.get(Calendar.MONTH) + 1,
                        currentCalendar.get(Calendar.YEAR)
                )

                val entry = DateResultEntry(context.getString(key), value)
                when (checkType) {
                    DateCheckType.IN_FUTURE -> {
                        if (currentDate.compareTo(value) > 0) {
                            entry.mValid = false
                        }
                    }
                    DateCheckType.IN_PAST -> {
                        if (currentDate.compareTo(value) < 0) {
                            entry.mValid = false
                        }
                    }
                    else -> entry.mValid = true
                }
                if (!entry.mValid) {
                    entry.mInvalidReasonRes = errorMessageResource
                }
                return entry
            } else {
                return StringResultEntry(context.getString(key), "")
            }
        }

        fun build(@StringRes key: Int, value: Date?): ResultEntry<*> {
            return build(key, value, DateCheckType.NO_CHECK, 0)
        }

        fun build(@StringRes key: Int, amount: Int, unitName: String): StringResultEntry {
            return StringResultEntry(context.getString(key), amount.toString() + " " + unitName)
        }

        fun build(@StringRes key: Int, value: Int): StringResultEntry {
            return StringResultEntry(context.getString(key), value.toString())
        }

        fun build(@StringRes key: Int, value: Boolean): StringResultEntry {
            return StringResultEntry(context.getString(key), value.toString())
        }
    }

    protected fun writeValidityToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt((if (mValid) 1 else 0))
        dest?.writeInt(mInvalidReasonRes)
    }

    protected fun readValidityFromParcel(source: Parcel) {
        mValid = source.readInt() == 1
        mInvalidReasonRes = source.readInt()
    }

    override fun equals(other: Any?): Boolean {
        return other is ResultEntry<*> && other.key == this.key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

}