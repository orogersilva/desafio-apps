package com.orogersilva.desafioinfoglobo.data.remote

import com.orogersilva.desafioinfoglobo.api.RestClient
import com.orogersilva.desafioinfoglobo.api.services.PublicationApiClient
import com.orogersilva.desafioinfoglobo.data.NewsDataSource
import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.model.Publication
import retrofit2.Response

/**
 * Created by orogersilva on 3/25/2017.
 */
object NewsRemoteDataSource : NewsDataSource {

    // region INITIALIZER

    fun getInstance() : NewsDataSource {

        return this
    }

    // endregion

    // region OVERRIDED METHODS

    override fun getAllNews(callback: NewsDataSource.LoadNewsCallback) {

        val publicationApiClient = RestClient.getApiClient(PublicationApiClient::class.java)

        var response: Response<List<Publication>>? = null

        try {

            response = publicationApiClient.getAllNews().execute()

        } catch (e: Exception) {

            e.printStackTrace()
        }

        if (response != null && response.isSuccessful) {

            var news = response.body()[0].content

            if (news == null) news = listOf()

            callback.onNewsLoaded(news)

        } else {

            callback.onDataNotAvailable()
        }

    }

    override fun saveNews(news: List<News>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllNews(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // endregion
}