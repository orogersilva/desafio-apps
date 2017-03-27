package com.orogersilva.desafioinfoglobo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel

/**
 * Created by orogersilva on 3/24/2017.
 */
@PaperParcel
data class Section(@SerializedName("nome") val name: String,
                   @SerializedName("url") val url: String) : Parcelable {

    // region COMPANION OBJECT

    companion object {
        @JvmField val CREATOR = PaperParcelSection.CREATOR
    }

    // endregion

    // region OVERRIDED METHODS

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        PaperParcelSection.writeToParcel(this, dest!!, flags)
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Section

        return name == other.name &&
                url == other.url
    }

    // endregion
}