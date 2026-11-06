package uz.ttpu.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion8() = runBlocking {
    val parentJob = launch {
        launch {
            delay(200L)
            println("child done")
        }
        println("parent body done")
    }

    parentJob.join()
    println("report completed")
}

fun main() = runQuestion8()
