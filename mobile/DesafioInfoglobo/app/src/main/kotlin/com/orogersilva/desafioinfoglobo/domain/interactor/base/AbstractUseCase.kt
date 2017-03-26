package com.orogersilva.desafioinfoglobo.domain.interactor.base

import com.orogersilva.desafioinfoglobo.domain.executor.Executor
import com.orogersilva.desafioinfoglobo.domain.executor.MainThread

/**
 * Created by orogersilva on 3/25/2017.
 */
abstract class AbstractUseCase(protected val threadExecutor: Executor,
                               protected val mainThread: MainThread) : UseCase {

    // region PROPERTIES

    protected var isCancelled = false
    protected var isRunning = false

    // endregion

    // region PUBLIC METHODS

    abstract fun run()

    override fun execute() {

        isRunning = true

        threadExecutor.execute(this)
    }

    fun threadIsRunning() = isRunning

    fun cancel() {

        isCancelled = true
        isRunning = false
    }

    fun onFinished() {

        isCancelled = false
        isRunning = false
    }

    // endregion
}