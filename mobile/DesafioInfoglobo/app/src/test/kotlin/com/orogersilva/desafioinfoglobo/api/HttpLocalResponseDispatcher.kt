package com.orogersilva.desafioinfoglobo.api

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException

/**
 * Created by orogersilva on 3/25/2017.
 */
class HttpLocalResponseDispatcher(val scenarioContent: String?) : Dispatcher() {

    // region OVERRIDED METHODS

    override fun dispatch(request: RecordedRequest?): MockResponse {

        val mockResponse = MockResponse()

        if (scenarioContent != null) {

            try {

                mockResponse.setBody(scenarioContent)
                mockResponse.setResponseCode(BaseApiClientTest.OK_STATUS_CODE)

            } catch (e: IOException) {

                e.printStackTrace()
            }
        }

        return mockResponse
    }

    // endregion
}