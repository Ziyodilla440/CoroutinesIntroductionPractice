package uz.ttpu.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun CoroutineScope.greetInBackground(name: String): Job {
    return launch {
        delay(150L)
        println("Background greeting: Hello, $name!")
    }
}

suspend fun greetAllAndWait(names: List<String>) = coroutineScope {
    for (name in names) {
        launch {
            delay(100L)
            println("Batch greeting: Hello, $name!")
        }
    }
}

fun runQuestion12() = runBlocking {
    println("--- Testing Helper 1 (Extension on CoroutineScope) ---")
    greetInBackground("Alice")
    println("Helper 1 called - returned immediately without waiting!")

    delay(200L)

    println("\n--- Testing Helper 2 (Suspend function with coroutineScope) ---")
    greetAllAndWait(listOf("Bob", "Charlie", "Diana"))
    println("Helper 2 completed - waited for all greetings to finish before printing this line!")
}

fun main() = runQuestion12()
