package uz.ttpu.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun runExtra1() = runBlocking {
    println("--- Measuring delay inside coroutineScope ---")
    val delayTime = measureTimeMillis {
        coroutineScope {
            launch { delay(300L) }
            launch { delay(300L) }
            launch { delay(300L) }
        }
    }
    val delayRounded = Math.round(delayTime / 100.0) * 100
    println("Time with delay: ${delayTime}ms (rounded to hundreds: ~${delayRounded.toInt()}ms)")

    println("\n--- Measuring Thread.sleep inside coroutineScope ---")
    val sleepTime = measureTimeMillis {
        coroutineScope {
            launch { Thread.sleep(300L) }
            launch { Thread.sleep(300L) }
            launch { Thread.sleep(300L) }
        }
    }
    val sleepRounded = Math.round(sleepTime / 100.0) * 100
    println("Time with Thread.sleep: ${sleepTime}ms (rounded to hundreds: ~${sleepRounded.toInt()}ms)")
}

fun main() = runExtra1()
