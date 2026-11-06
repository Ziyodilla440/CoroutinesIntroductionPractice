package uz.ttpu.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion7() = runBlocking {
    println("1")
    launch {
        println("2")
        delay(100)
        println("3")
    }
    println("4")
    delay(50)
    println("5")
}

fun main() = runQuestion7()
