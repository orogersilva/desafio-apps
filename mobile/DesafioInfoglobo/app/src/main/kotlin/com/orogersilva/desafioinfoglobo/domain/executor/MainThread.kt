package com.orogersilva.desafioinfoglobo.domain.executor

/**
 * Created by orogersilva on 3/25/2017.
 */
interface MainThread {

    // region METHODS

    fun post(runnable: Runnable)

    // endregion
}