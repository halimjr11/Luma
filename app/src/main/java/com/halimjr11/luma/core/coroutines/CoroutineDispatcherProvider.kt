package com.halimjr11.luma.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}