package com.orogersilva.desafioinfoglobo.api.services

import com.orogersilva.desafioinfoglobo.model.Publication
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by orogersilva on 3/25/2017.
 */
interface HeadlinesApiClient {

    // region ENDPOINTS

    @GET("master/capa.json")
    fun getAllNews(): Call<List<Publication>>

    // endregion
}