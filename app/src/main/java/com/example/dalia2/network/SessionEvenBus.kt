package com.example.dalia2.network

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionEvenBus @Inject constructor() {
    private val _sessionExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpiredEvent = _sessionExpiredEvent.asSharedFlow()

    fun notificaSessaoExpirada(){
        _sessionExpiredEvent.tryEmit(Unit)
    }
}