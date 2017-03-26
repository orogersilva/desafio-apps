package com.orogersilva.desafioinfoglobo.domain.interactor

import com.orogersilva.desafioinfoglobo.domain.interactor.base.UseCase
import com.orogersilva.desafioinfoglobo.model.News

/**
 * Created by orogersilva on 3/25/2017.
 */
interface GetAllNewsUseCase : UseCase {

    // region INTERFACES

    interface Callback {

        fun onNewsLoaded(news: List<News>)
        fun onDataNotAvailable()
    }

    // endregion
}