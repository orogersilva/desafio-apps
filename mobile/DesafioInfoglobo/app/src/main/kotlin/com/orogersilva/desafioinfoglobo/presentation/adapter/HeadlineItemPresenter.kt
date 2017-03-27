package com.orogersilva.desafioinfoglobo.presentation.adapter

import com.orogersilva.desafioinfoglobo.model.News

/**
 * Created by orogersilva on 3/26/2017.
 */
class HeadlineItemPresenter {

    // region PUBLIC METHODS

    fun presentListItem(headLineItemViewHolder: NewsAdapter.HeadLineItemViewHolder, news: News) {

        headLineItemViewHolder.setItem(news)
    }

    // endregion
}