package com.orogersilva.desafioinfoglobo.presentation.screen.news

import com.orogersilva.desafioinfoglobo.data.NewsRepository
import com.orogersilva.desafioinfoglobo.domain.executor.Executor
import com.orogersilva.desafioinfoglobo.domain.executor.MainThread
import com.orogersilva.desafioinfoglobo.domain.interactor.GetAllNewsUseCase
import com.orogersilva.desafioinfoglobo.domain.interactor.impl.GetAllNewsUseCaseImpl
import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.presentation.AbstractPresenter

/**
 * Created by orogersilva on 3/26/2017.
 */
class NewsPresenter(private val view: NewsContract.View,
                    executor: Executor,
                    mainThread: MainThread,
                    private val newsRepository: NewsRepository)
    : AbstractPresenter(executor, mainThread), NewsContract.Presenter {

    // region INITIALIZER BLOCK

    init {

        view.setPresenter(this)
    }

    // endregion

    // region OVERRIDED METHODS

    override fun resume() {

        loadAllNews()
    }

    override fun loadAllNews() {

        view.showLoadingIndicator(true)

        processNews()
    }

    // endregion

    // region UTILITY METHODS

    private fun processNews() {

        val getAllNewsUseCase = GetAllNewsUseCaseImpl(executor, mainThread, object : GetAllNewsUseCase.Callback {

            override fun onNewsLoaded(news: List<News>) {

                view.showLoadingIndicator(false)

                view.showNews(news)
            }

            override fun onDataNotAvailable() {

                view.showLoadingIndicator(false)
            }

        }, newsRepository)

        getAllNewsUseCase.execute()
    }

    // endregion
}