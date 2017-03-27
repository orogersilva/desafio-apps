package com.orogersilva.desafioinfoglobo.domain.executor.impl

import com.orogersilva.desafioinfoglobo.domain.executor.Executor
import com.orogersilva.desafioinfoglobo.domain.interactor.base.AbstractUseCase
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by orogersilva on 3/25/2017.
 */
object ThreadExecutor : Executor {

    // region PROPERTIES

    private val CORE_POOL_SIZE = 3
    private val MAX_POOL_SIZE = 5
    private val KEEP_ALIVE_TIME = 120L
    private val TIME_UNIT = TimeUnit.SECONDS
    private val WORK_QUEUE = LinkedBlockingQueue<Runnable>()

    private lateinit var threadPoolExecutor: ThreadPoolExecutor

    // endregion

    // region INITIALIZER / DESTRUCTOR

    fun getInstance(): Executor {

        threadPoolExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, WORK_QUEUE)

        return this
    }

    // endregion

    // region OVERRIDED METHODS

    override fun execute(useCase: AbstractUseCase) {

        threadPoolExecutor.submit {

            useCase.run()
            useCase.onFinished()
        }
    }

    // endregion
}