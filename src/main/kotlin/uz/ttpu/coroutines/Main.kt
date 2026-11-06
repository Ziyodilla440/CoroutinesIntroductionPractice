package uz.ttpu.coroutines

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    println("==========================================================")
    println("    Coroutines Introduction Practice - All Questions     ")
    println("==========================================================")

    if (args.isNotEmpty()) {
        when (args[0].lowercase()) {
            "q1" -> runQuestion1()
            "q2" -> runQuestion2()
            "q3" -> runBlocking { runQuestion3() }
            "q4" -> runQuestion4()
            "q5" -> runQuestion5()
            "q6" -> runQuestion6()
            "q7" -> runQuestion7()
            "q8" -> runQuestion8()
            "q9" -> runQuestion9()
            "q10" -> runQuestion10()
            "q11" -> runQuestion11()
            "q12" -> runQuestion12()
            "q13" -> runQuestion13()
            "q14" -> runQuestion14()
            "q15" -> runQuestion15()
            "e1" -> runExtra1()
            "e2" -> runExtra2()
            "e3" -> runExtra3()
            "e4" -> runExtra4()
            "e5" -> runExtra5()
            else -> println("Unknown question argument: ${args[0]}")
        }
        return
    }

    println("\n--- [Question 1: First coroutine] ---")
    runQuestion1()

    println("\n--- [Question 2: Three baristas] ---")
    runQuestion2()

    println("\n--- [Question 3: Chain of suspend functions] ---")
    runBlocking { runQuestion3() }

    println("\n--- [Question 4: Calling suspend from normal code] ---")
    runQuestion4()

    println("\n--- [Question 5: Suspend is not a thread] ---")
    runQuestion5()

    println("\n--- [Question 6: Ten downloads, one thread] ---")
    runQuestion6()

    println("\n--- [Question 7: Order of launch] ---")
    runQuestion7()

    println("\n--- [Question 8: Parent waits for child] ---")
    runQuestion8()

    println("\n--- [Question 9: Screen closed, requests cancelled] ---")
    runQuestion9()

    println("\n--- [Question 10: Profile ready] ---")
    runQuestion10()

    println("\n--- [Question 11: Reusable counter] ---")
    runQuestion11()

    println("\n--- [Question 12: Returns at once or waits] ---")
    runQuestion12()

    println("\n--- [Question 13: Five builders] ---")
    runQuestion13()

    println("\n--- [Question 14: Job or Deferred] ---")
    runQuestion14()

    println("\n--- [Question 15: Three exports in a scope] ---")
    runQuestion15()

    println("\n--- [Extra 1: Delay or sleep: measure it] ---")
    runExtra1()

    println("\n--- [Extra 2: Three levels of coroutines] ---")
    runExtra2()

    println("\n--- [Extra 3: No scope, no launch] ---")
    runExtra3()

    println("\n--- [Extra 4: What the compiler sees] ---")
    runExtra4()

    println("\n--- [Extra 5: Greet everybody] ---")
    runExtra5()

    println("\n==========================================================")
    println("              All 20 Questions Completed!                 ")
    println("==========================================================")
}
