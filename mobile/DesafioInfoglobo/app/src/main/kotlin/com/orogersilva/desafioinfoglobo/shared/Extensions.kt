package com.orogersilva.desafioinfoglobo.shared

import java.util.*

/**
 * Created by orogersilva on 3/25/2017.
 */

// region MUTABLE MAP EXTENSION METHODS

fun <K, V> MutableMap<K, V>?.toImmutableMap() = HashMap(this)

// endregion