package uz.ttpu.coroutines

import kotlinx.coroutines.runBlocking

suspend fun getCurrentThreadName(): String {
    return Thread.currentThread().name
}

fun runQuestion5() = runBlocking {
    val beforeCallThread = Thread.currentThread().name
    println("Thread before call: $beforeCallThread")

    val insideSuspendThread = getCurrentThreadName()
    println("Thread inside suspend function: $insideSuspendThread")

    val afterCallThread = Thread.currentThread().name
    println("Thread after call: $afterCallThread")

    if (beforeCallThread == insideSuspendThread) {
        println("Disproved: The thread name is identical ('$beforeCallThread').")
        println("Marking a function 'suspend' does NOT move execution to another thread.")
    }
}

fun main() = runQuestion5()
