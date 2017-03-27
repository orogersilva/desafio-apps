package com.orogersilva.desafioinfoglobo.api.services

import com.orogersilva.desafioinfoglobo.api.BaseApiClientTest
import com.orogersilva.desafioinfoglobo.api.HttpLocalResponseDispatcher
import com.orogersilva.desafioinfoglobo.api.RestClient
import com.orogersilva.desafioinfoglobo.model.Publication
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

/**
 * Created by orogersilva on 3/25/2017.
 */
class PublicationApiClientTest : BaseApiClientTest() {

    // region TEST METHODS

    @Test fun getAllNews_requestToCorrectEndpoint() {

        // ARRANGE

        server?.setDispatcher(HttpLocalResponseDispatcher(loadJsonFromAsset("publication.json")))
        server?.start()

        // ACT

        val response: Response<List<Publication>> = getPublicationApiClient().getAllNews().execute()

        // ASSERT

        assertNotNull(response)
        assertTrue(response.isSuccessful)

        val recordedRequest = server?.takeRequest()

        assertEquals("/Infoglobo/desafio-apps/master/capa.json", recordedRequest?.path)
    }

    // endregion

    // region UTILITY METHODS

    private fun getPublicationApiClient() = RestClient.getApiClient(PublicationApiClient::class.java, getBaseEndpoint())

    // endregion
}