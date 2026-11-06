package uz.ttpu.coroutines

import kotlinx.coroutines.delay

suspend fun openProfile() {
    delay(100)
    println("Step 3: Open profile")
}

suspend fun checkPassword() {
    delay(100)
    println("Step 2: Check password")
    openProfile()
}

suspend fun loadUser() {
    delay(100)
    println("Step 1: Load user")
    checkPassword()
}

suspend fun runQuestion3() {
    println("Starting login flow...")
    loadUser()
    println("Login flow completed successfully.")
}

suspend fun main() = runQuestion3()
