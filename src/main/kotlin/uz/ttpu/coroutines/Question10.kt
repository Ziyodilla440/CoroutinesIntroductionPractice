package uz.ttpu.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion10() = runBlocking {
    coroutineScope {
        launch {
            delay(200L)
            println("Avatar loaded")
        }
        launch {
            delay(100L)
            println("Settings loaded")
        }
    }
    println("Profile ready")

    launch {
        delay(150L)
        println("Background analytics sync completed")
    }
    println("end of main")
}

fun main() = runQuestion10()
