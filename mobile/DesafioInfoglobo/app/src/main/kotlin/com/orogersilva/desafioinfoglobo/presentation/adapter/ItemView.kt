package com.orogersilva.desafioinfoglobo.presentation.adapter

/**
 * Created by orogersilva on 3/26/2017.
 */
interface ItemView<in T> {

    // region METHODS

    fun setItem(item: T)

    // endregion
}