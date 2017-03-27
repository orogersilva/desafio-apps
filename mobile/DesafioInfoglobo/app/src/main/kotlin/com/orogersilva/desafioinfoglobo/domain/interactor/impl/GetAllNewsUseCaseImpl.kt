package com.orogersilva.desafioinfoglobo.domain.interactor.impl

import com.orogersilva.desafioinfoglobo.data.NewsDataSource
import com.orogersilva.desafioinfoglobo.data.NewsRepository
import com.orogersilva.desafioinfoglobo.domain.executor.Executor
import com.orogersilva.desafioinfoglobo.domain.executor.MainThread
import com.orogersilva.desafioinfoglobo.domain.interactor.GetAllNewsUseCase
import com.orogersilva.desafioinfoglobo.domain.interactor.base.AbstractUseCase
import com.orogersilva.desafioinfoglobo.domain.interactor.base.UseCase
import com.orogersilva.desafioinfoglobo.model.News
import java.util.*

/**
 * Created by orogersilva on 3/25/2017.
 */
class GetAllNewsUseCaseImpl(threadExecutor: Executor,
                            mainThread: MainThread,
                            private val callback: GetAllNewsUseCase.Callback,
                            private val newsRepository: NewsRepository)
    : AbstractUseCase(threadExecutor, mainThread), UseCase {


    // region OVERRIDED METHODS

    override fun run() {

        newsRepository.getAllNews(object : NewsDataSource.LoadNewsCallback {

            // region OVERRIDED METHODS

            override fun onNewsLoaded(news: List<News>) {

                mainThread.post(Runnable {

                    Collections.sort(news)

                    callback.onNewsLoaded(news)
                })
            }

            override fun onDataNotAvailable() {

                mainThread.post(Runnable { callback.onDataNotAvailable() })
            }

            // endregion
        })
    }

    // endregion
}