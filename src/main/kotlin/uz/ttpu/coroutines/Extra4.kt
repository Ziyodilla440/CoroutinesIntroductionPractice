package uz.ttpu.coroutines

import kotlinx.coroutines.delay

data class User(val id: Int, val name: String)

suspend fun loadUserById(id: Int): User {
    delay(100L)
    return User(id, "User_$id")
}

fun runExtra4() {
    println("Original Kotlin signature: suspend fun loadUserById(id: Int): User")
    println("Decompiled Java signature: public static final Object loadUserById(int id, Continuation<? super User> \$completion)")
}

fun main() = runExtra4()
