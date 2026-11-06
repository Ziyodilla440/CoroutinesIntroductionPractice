package uz.ttpu.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun runQuestion13() {
    val runBlockingResult: String = runBlocking {
        val launchResult = launch {
            delay(50L)
            println("Inside launch: executing background task")
        }
        println("1. launch returned: $launchResult (type: Job)")

        val asyncResult = async {
            delay(50L)
            42
        }
        println("2. async returned: $asyncResult (type: Deferred<Int>)")

        val scopeResult: String = coroutineScope {
            delay(50L)
            "Result from coroutineScope block"
        }
        println("3. coroutineScope returned: $scopeResult")

        val contextResult: String = withContext(Dispatchers.Default) {
            "Result from withContext on ${Thread.currentThread().name}"
        }
        println("4. withContext returned: $contextResult")

        val awaitedValue = asyncResult.await()
        println("async await() value: $awaitedValue")

        "All builders executed successfully"
    }

    println("5. runBlocking returned: $runBlockingResult")
}

fun main() = runQuestion13()
