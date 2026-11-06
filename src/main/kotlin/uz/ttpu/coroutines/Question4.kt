package uz.ttpu.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun fetchUserData(): String {
    delay(100)
    return "User: Alex"
}

suspend fun fix1SuspendCaller(): String {
    val data = fetchUserData()
    return "Fix 1 result: $data"
}

fun fix2RunBlocking(): String {
    return runBlocking {
        val data = fetchUserData()
        "Fix 2 (runBlocking) result: $data"
    }
}

fun fix2ScopeLaunch() {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        val data = fetchUserData()
        println("Fix 2 (scope.launch) result: $data")
    }
}

fun runQuestion4() = runBlocking {
    println("--- Demonstrating Fixes ---")
    println(fix1SuspendCaller())
    println(fix2RunBlocking())
    fix2ScopeLaunch()
    delay(200)
}

fun main() = runQuestion4()
