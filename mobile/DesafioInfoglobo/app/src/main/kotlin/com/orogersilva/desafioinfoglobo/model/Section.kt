package com.orogersilva.desafioinfoglobo.model

import com.google.gson.annotations.SerializedName

/**
 * Created by orogersilva on 3/24/2017.
 */
data class Section(@SerializedName("nome") val name: String,
                   @SerializedName("url") val url: String) {

    // region OVERRIDED METHODS

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Section

        return name == other.name &&
                url == other.url
    }

    // endregion
}