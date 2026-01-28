package com.example.fakestore

import com.example.fakestore.core.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

class TestDispatcherProvider(
    override val io: CoroutineDispatcher,
    override val main: CoroutineDispatcher,
    override val default: CoroutineDispatcher
) : DispatcherProvider