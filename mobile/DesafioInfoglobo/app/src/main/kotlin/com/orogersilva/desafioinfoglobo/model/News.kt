package com.orogersilva.desafioinfoglobo.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by orogersilva on 3/24/2017.
 */
data class News(@SerializedName("autores") var authors: List<String>? = null,
                @SerializedName("informePublicitario") val advertisingReport: Boolean,
                @SerializedName("subTitulo") val subtitle: String?,
                @SerializedName("texto") val text: String?,
                @SerializedName("videos") var videos: List<String>? = null,
                @SerializedName("atualizadoEm") val updatedIn: String,
                @SerializedName("id") val id: Long,
                @SerializedName("publicadoEm") val publishedIn: String,
                @SerializedName("secao") val section: Section,
                @SerializedName("tipo") val type: String,
                @SerializedName("titulo") val title: String,
                @SerializedName("url") val url: String,
                @SerializedName("urlOriginal") val originalUrl: String,
                @SerializedName("imagens") var images: List<Image>? = null) {

    // region OVERRIDED METHODS

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as News

        return isEqual(authors, other?.authors) &&
                advertisingReport == other.advertisingReport &&
                subtitle == other.subtitle &&
                text == other.text &&
                isEqual(videos, other?.videos) &&
                updatedIn == other.updatedIn &&
                id == other.id &&
                publishedIn == other.publishedIn &&
                section == other.section &&
                type == other.type &&
                title == other.title &&
                url == other.url &&
                originalUrl == other.originalUrl &&
                isEqual(images, other?.images)
    }

    // endregion

    // region UTILITY METHODS

    /**
     * TODO: This method is a workaround for treat equality among empty array and null values. Later, make a TypeAdapter for treat json objects and get rid this method.
     */
    inline private fun <reified T> isEqual(list1: List<T>?, list2: List<T>?): Boolean {

        return ((list1 == null || (list1 != null && list1?.isEmpty())) &&
                (list2 == null || (list2 != null && list2?.isEmpty()))) ||
                Arrays.equals(list1?.toTypedArray(), list2?.toTypedArray())
    }

    // endregion
}