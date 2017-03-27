package com.orogersilva.desafioinfoglobo.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by orogersilva on 3/25/2017.
 */
data class Publication(@SerializedName("conteudos") val content: List<News>?,
                       @SerializedName("produto") val product: String) {

    // region OVERRIDED METHODS

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Publication

        return Arrays.equals(content?.toTypedArray(), other.content?.toTypedArray()) &&
                product == other.product
    }

    // endregion
}