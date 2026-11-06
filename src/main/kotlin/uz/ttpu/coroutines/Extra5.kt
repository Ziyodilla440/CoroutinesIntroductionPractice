package uz.ttpu.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun greetEverybody(names: List<String>) = coroutineScope {
    names.forEachIndexed { index, name ->
        val i = index + 1
        launch {
            delay(i * 100L)
            println("Hello, $name")
        }
    }
}

fun runExtra5() = runBlocking {
    println("before")
    greetEverybody(listOf("Ada", "Linus", "Grace"))
    println("after")
}

fun main() = runExtra5()
