package uz.ttpu.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
// Reproduction of compiler error:
fun plainMain() {
    launch {
        delay(100L)
        println("Inside launch")
    }
}
*/

fun runExtra3() = runBlocking {
    launch {
        delay(100L)
        println("Inside launch: executed successfully within runBlocking scope")
    }
    println("Main body finished, waiting for children")
}

fun main() = runExtra3()
