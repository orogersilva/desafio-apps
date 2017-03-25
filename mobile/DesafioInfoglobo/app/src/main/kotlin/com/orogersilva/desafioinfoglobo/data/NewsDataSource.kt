package com.orogersilva.desafioinfoglobo.data

import com.orogersilva.desafioinfoglobo.model.News

/**
 * Created by orogersilva on 3/24/2017.
 */
interface NewsDataSource {

    // region INTERFACES

    interface LoadNewsCallback {

        fun onNewsLoaded(news: List<News>)
        fun onDataNotAvailable()
    }

    // endregion

    // region CRUD

    fun getAllNews(callback: LoadNewsCallback)
    fun saveNews(news: List<News>?)
    fun deleteAllNews(): Int

    // endregion
}