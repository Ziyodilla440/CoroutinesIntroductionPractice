package uz.ttpu.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runExtra2() = runBlocking {
    coroutineScope {
        launch {
            launch {
                delay(200L)
                println("grandchild done")
            }
            println("child 1 done")
        }

        launch {
            delay(100L)
            println("child 2 done")
        }
    }
    println("scope done")
}

fun main() = runExtra2()
