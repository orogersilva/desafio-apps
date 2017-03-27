package com.orogersilva.desafioinfoglobo.domain.executor.impl

import android.os.Handler
import com.orogersilva.desafioinfoglobo.domain.executor.MainThread

/**
 * Created by orogersilva on 3/25/2017.
 */
object MainThreadImpl : MainThread {

    // region PROPERTIES

    private lateinit var handler: Handler

    // endregion

    // region INITIALIZER / DESTRUCTOR

    fun getInstance(): MainThreadImpl {

        handler = Handler()

        return this
    }

    // endregion

    // region OVERRIDED METHODS

    override fun post(runnable: Runnable) {

        handler.post(runnable)
    }

    // endregion
}