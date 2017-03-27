package com.orogersilva.desafioinfoglobo.presentation.screen.news.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.orogersilva.desafioinfoglobo.R
import com.orogersilva.desafioinfoglobo.data.NewsRepository
import com.orogersilva.desafioinfoglobo.data.local.DbHelper
import com.orogersilva.desafioinfoglobo.data.local.NewsLocalDataSource
import com.orogersilva.desafioinfoglobo.data.remote.NewsRemoteDataSource
import com.orogersilva.desafioinfoglobo.domain.executor.impl.MainThreadImpl
import com.orogersilva.desafioinfoglobo.domain.executor.impl.ThreadExecutor
import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.presentation.adapter.NewsAdapter
import com.orogersilva.desafioinfoglobo.presentation.screen.news.NewsContract
import com.orogersilva.desafioinfoglobo.presentation.screen.news.NewsPresenter
import com.orogersilva.desafioinfoglobo.presentation.screen.newsdetails.view.NewsDetailsActivity
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.toolbar_news.*

/**
 * Created by orogersilva on 3/26/2017.
 */
class NewsActivity : AppCompatActivity(), NewsContract.View {

    // region PROPERTIES

    private lateinit var newsPresenter: NewsContract.Presenter

    private lateinit var newsLayoutManager: RecyclerView.LayoutManager
    private lateinit var newsAdapter: NewsAdapter

    private val news = mutableListOf<News>()

    private val newsItemListener = object : NewsAdapter.NewsItemListener {

        override fun onNewsPressed(news: News) {

            val intent = Intent(baseContext, NewsDetailsActivity::class.java)

            intent.putExtra("news_extra", news)

            startActivity(intent)
        }
    }

    // endregion

    // region ACTIVITY LIFECYCLE METHODS

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        newsToolbar.title = ""

        setSupportActionBar(newsToolbar)

        newsAdapter = NewsAdapter(news, newsItemListener)
        newsLayoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager

        newsRecyclerView.layoutManager = newsLayoutManager
        newsRecyclerView.adapter = newsAdapter
        newsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        newsPresenter = NewsPresenter(this, ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                NewsRepository.getInstance(NewsLocalDataSource.getInstance(DbHelper(this)),
                        NewsRemoteDataSource.getInstance()))
    }

    override fun onResume() {

        super.onResume()

        newsPresenter.resume()
    }

    // endregion

    // region OVERRIDED METHODS

    override fun setPresenter(presenter: NewsContract.Presenter) {

        newsPresenter = presenter
    }

    override fun showLoadingIndicator(isActive: Boolean) {

        if (isActive) {
            loadingView.show()
        } else {
            loadingView.hide()
        }
    }

    override fun showNews(news: List<News>) {

        newsAdapter.replaceData(news)
    }

    // endregion
}