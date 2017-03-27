package com.orogersilva.desafioinfoglobo.presentation.adapter

import com.orogersilva.desafioinfoglobo.model.News

/**
 * Created by orogersilva on 3/26/2017.
 */
class NewsItemPresenter {

    // region PUBLIC METHODS

    fun presentListItem(newsItemViewHolder: NewsAdapter.NewsItemViewHolder, news: News) {

        newsItemViewHolder.setItem(news)
    }

    // endregion
}