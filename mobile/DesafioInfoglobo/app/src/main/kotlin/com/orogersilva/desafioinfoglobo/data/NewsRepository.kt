package com.orogersilva.desafioinfoglobo.data

import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.shared.toImmutableMap

/**
 * Created by orogersilva on 3/25/2017.
 */
object NewsRepository : NewsDataSource {

    // region PROPERTIES

    private var newsLocalDataSource: NewsDataSource? = null
    private var newsRemoteDataSource: NewsDataSource? = null

    private var cachedNews: MutableMap<Long, News>? = null

    // endregion

    // region INITIALIZER / DESTRUCTOR

    fun getInstance(newsLocalDataSource: NewsDataSource, newsRemoteDataSource: NewsDataSource): NewsRepository {


        if (this.newsLocalDataSource == null && this.newsRemoteDataSource == null) {

            this.newsLocalDataSource = newsLocalDataSource
            this.newsRemoteDataSource = newsRemoteDataSource
        }

        return this
    }

    fun destroyInstance() {

        newsLocalDataSource = null
        newsRemoteDataSource = null

        cachedNews?.clear()
        cachedNews = null
    }

    // endregion

    // region OVERRIDED METHODS

    override fun getAllNews(callback: NewsDataSource.LoadNewsCallback) {

        if (cachedNews != null) {

            callback?.onNewsLoaded(cachedNews.toImmutableMap().values.toList())
            return
        }

        newsLocalDataSource?.getAllNews(object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                cacheInMemory(news)

                callback.onNewsLoaded(cachedNews.toImmutableMap().values.toList())
            }

            override fun onDataNotAvailable() {

                getNewsFromRemoteDataSource(callback)
            }
        })
    }

    override fun saveNews(news: List<News>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllNews(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // endregion

    // region UTILITY METHODS

    private fun cacheInMemory(news: List<News>) {

        if (cachedNews == null) {
            cachedNews = mutableMapOf()
        }

        cachedNews?.clear()

        news.forEach { cachedNews?.put(it.id, it) }
    }

    private fun saveToDisk(news: List<News>) {

        newsLocalDataSource?.deleteAllNews()
        newsLocalDataSource?.saveNews(news)
    }

    private fun getNewsFromRemoteDataSource(callback: NewsDataSource.LoadNewsCallback) {

        newsRemoteDataSource?.getAllNews(object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                cacheInMemory(news)
                saveToDisk(news)

                callback?.onNewsLoaded(cachedNews.toImmutableMap().values.toList())
            }

            override fun onDataNotAvailable() {

                callback?.onDataNotAvailable()
            }
        })
    }

    // endregion
}