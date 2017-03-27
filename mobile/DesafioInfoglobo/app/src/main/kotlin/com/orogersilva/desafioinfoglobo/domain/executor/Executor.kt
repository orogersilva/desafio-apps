package com.orogersilva.desafioinfoglobo.domain.executor

import com.orogersilva.desafioinfoglobo.domain.interactor.base.AbstractUseCase

/**
 * Created by orogersilva on 3/25/2017.
 */
interface Executor {

    // region METHODS

    fun execute(useCase: AbstractUseCase)

    // endregion
}