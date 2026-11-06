package uz.ttpu.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion1() = runBlocking {
    launch {
        delay(1000L)
        println("Coffee ready")
    }
    println("Order accepted")
}

fun main() = runQuestion1()
