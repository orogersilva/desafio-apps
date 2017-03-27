package com.orogersilva.desafioinfoglobo.presentation

/**
 * Created by orogersilva on 3/25/2017.
 */
interface BaseView<T> {

    // region METHODS

    fun setPresenter(presenter: T)

    // endregion
}