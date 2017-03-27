package com.orogersilva.desafioinfoglobo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel

/**
 * Created by orogersilva on 3/24/2017.
 */
@PaperParcel
data class Image(@SerializedName("autor") val author: String,
                 @SerializedName("fonte") val source: String,
                 @SerializedName("legenda") val caption: String,
                 @SerializedName("url") val url: String) : Parcelable {

    // region COMPANION OBJECT

    companion object {
        @JvmField val CREATOR = PaperParcelImage.CREATOR
    }

    // endregion

    // region OVERRIDED METHODS

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        PaperParcelImage.writeToParcel(this, dest!!, flags)
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        return author == other.author &&
                source == other.source &&
                caption == other.caption &&
                url == other.url
    }

    // endregion
}