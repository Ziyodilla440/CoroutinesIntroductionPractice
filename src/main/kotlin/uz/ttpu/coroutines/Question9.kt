package uz.ttpu.coroutines

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion9() = runBlocking {
    val parentJob = launch {
        launch {
            try {
                println("Request 1 started")
                delay(1000L)
                println("Request 1 finished")
            } finally {
                println("Request 1 cleanup executed")
            }
        }

        launch {
            try {
                println("Request 2 started")
                delay(1000L)
                println("Request 2 finished")
            } finally {
                println("Request 2 cleanup executed")
            }
        }
    }

    delay(100L)
    println("Screen closed, cancelling parent coroutine...")
    parentJob.cancelAndJoin()
    println("end")
}

fun main() = runQuestion9()
