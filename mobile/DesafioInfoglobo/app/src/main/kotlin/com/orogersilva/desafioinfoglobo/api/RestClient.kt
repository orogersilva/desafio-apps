package com.orogersilva.desafioinfoglobo.api

import com.orogersilva.desafioinfoglobo.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by orogersilva on 3/25/2017.
 */
object RestClient {

    // region PROPERTIES

    private lateinit var retrofit: Retrofit

    // endregion

    // region PUBLIC METHODS

    fun <T> getApiClient(serviceClass: Class<T>, baseEndpoint: String = BuildConfig.INFOGLOBO_API_URL): T {

        retrofit = Retrofit.Builder()
                .baseUrl(baseEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(serviceClass)
    }

    // endregion
}