package com.orogersilva.desafioinfoglobo.presentation.screen.news

import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.presentation.BasePresenter
import com.orogersilva.desafioinfoglobo.presentation.BaseView

/**
 * Created by orogersilva on 3/26/2017.
 */
interface NewsContract {

    // region INTERFACES

    interface View : BaseView<Presenter> {

        // region METHODS

        fun showLoadingIndicator(isActive: Boolean)

        fun showNews(news: List<News>)

        // endregion
    }

    interface Presenter : BasePresenter {

        // region METHODS

        fun loadAllNews()

        // endregion
    }

    // endregion
}