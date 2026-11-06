package uz.ttpu.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion6() = runBlocking {
    println("Starting 10 downloads on single thread: ${Thread.currentThread().name}")
    repeat(10) { i ->
        launch {
            println("download $i started on ${Thread.currentThread().name}")
            delay(100L)
            println("download $i finished on ${Thread.currentThread().name}")
        }
    }
}

fun main() = runQuestion6()
