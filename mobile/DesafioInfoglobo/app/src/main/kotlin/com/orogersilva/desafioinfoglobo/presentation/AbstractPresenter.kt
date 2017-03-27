package com.orogersilva.desafioinfoglobo.presentation

import com.orogersilva.desafioinfoglobo.domain.executor.Executor
import com.orogersilva.desafioinfoglobo.domain.executor.MainThread

/**
 * Created by orogersilva on 3/25/2017.
 */
abstract class AbstractPresenter (protected val executor: Executor, protected val mainThread: MainThread) {
}