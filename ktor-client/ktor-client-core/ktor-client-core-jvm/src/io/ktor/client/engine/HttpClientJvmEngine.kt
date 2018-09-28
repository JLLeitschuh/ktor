package io.ktor.client.engine

import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.scheduling.*
import kotlin.coroutines.*

@UseExperimental(InternalAPI::class)
abstract class HttpClientJvmEngine(engineName: String) : HttpClientEngine {

    override val dispatcher: ExperimentalCoroutineDispatcher by lazy {
        ExperimentalCoroutineDispatcher(config.threadsCount)
    }

    override val coroutineContext: CoroutineContext by lazy {
        dispatcher + Supervisor() + CoroutineName("$engineName-context")
    }

    protected fun createCallContext() = coroutineContext + CompletableDeferred<Unit>(coroutineContext[Job])

    override fun close() {
        dispatcher.close()
    }
}