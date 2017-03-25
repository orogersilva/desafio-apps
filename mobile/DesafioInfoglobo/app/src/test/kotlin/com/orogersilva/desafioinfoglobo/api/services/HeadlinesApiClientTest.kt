package com.orogersilva.desafioinfoglobo.api.services

import com.orogersilva.desafioinfoglobo.api.BaseApiClientTest
import com.orogersilva.desafioinfoglobo.api.HttpLocalResponseDispatcher
import com.orogersilva.desafioinfoglobo.api.RestApiClient
import com.orogersilva.desafioinfoglobo.model.Publication
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

/**
 * Created by orogersilva on 3/25/2017.
 */
class HeadlinesApiClientTest : BaseApiClientTest() {

    // region TEST METHODS

    @Test fun getAllNews_requestToCorrectEndpoint() {

        // ARRANGE

        server?.setDispatcher(HttpLocalResponseDispatcher(loadJsonFromAsset("headlines.json")))
        server?.start()

        // ACT

        val response: Response<List<Publication>> = getHeadlinesApiClient().getAllNews().execute()

        // ASSERT

        assertNotNull(response)
        assertTrue(response.isSuccessful)

        val recordedRequest = server?.takeRequest()

        assertEquals("/master/capa.json", recordedRequest?.path)
    }

    // endregion

    // region UTILITY METHODS

    private fun getHeadlinesApiClient() = RestApiClient.getApiClient(HeadlinesApiClient::class.java, getBaseEndpoint())

    // endregion
}