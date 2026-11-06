package uz.ttpu.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun CoroutineScope.startCounter(name: String, maxCount: Int): Job {
    return launch {
        for (i in 1..maxCount) {
            delay(100L)
            println("[$name] tick: $i")
        }
    }
}

fun runQuestion11() = runBlocking {
    coroutineScope {
        startCounter("Counter-A", 3)
        startCounter("Counter-B", 2)
        println("launched")
    }
    println("Both counters finished (scope completed)")
}

fun main() = runQuestion11()
