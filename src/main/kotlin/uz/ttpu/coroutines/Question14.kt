package uz.ttpu.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun runQuestion14() = runBlocking {
    println("--- Attempt 1: Using launch ---")
    val job: Job = launch {
        delay(100L)
        val calculation = 6 * 7
        println("Inside launch: calculation completed ($calculation)")
    }
    job.join()
    println("launch returns a Job ($job). You cannot retrieve a result value from a Job!")

    println("\n--- Attempt 2: Using async ---")
    val deferred = async {
        delay(100L)
        6 * 7
    }
    println("async returns a Deferred<Int> ($deferred)")
    val result: Int = deferred.await()
    println("Result retrieved with deferred.await(): $result")
}

fun main() = runQuestion14()
