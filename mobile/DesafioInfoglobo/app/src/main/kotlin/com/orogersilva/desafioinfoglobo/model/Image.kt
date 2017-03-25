package com.orogersilva.desafioinfoglobo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by orogersilva on 3/24/2017.
 */
data class Image(@SerializedName("autor") val author: String,
                 @SerializedName("fonte") val source: String,
                 @SerializedName("legenda") val caption: String,
                 @SerializedName("url") val url: String) {

    // region OVERRIDED METHODS

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