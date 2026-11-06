# Coroutines Introduction Practice

Practice Work: Kotlin Coroutines Introduction & Structured Concurrency  
**Organization:** TTPU  
**Dependencies:** `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2`  
**Target:** Kotlin/JVM 2.1.10 / JRE 21  

---

## 💡 Suspending vs Blocking Explanation

```kotlin
/*
 * ============================================================================
 * EXPLANATION: How Suspending Differs from Blocking in Kotlin Coroutines
 * ============================================================================
 *
 * 1. BLOCKING (e.g., Thread.sleep(ms), blocking I/O):
 *    - A thread is an expensive operating system resource (~1MB memory stack,
 *      OS context-switching overhead).
 *    - When a thread blocks, it is held captive and put into a WAITING/BLOCKED
 *      state by the OS scheduler.
 *    - The thread cannot perform ANY other work, execute other tasks, or handle
 *      UI events until the blocking operation completes.
 *
 * 2. SUSPENDING (e.g., delay(ms), suspend functions, await()):
 *    - Suspending is non-blocking and cooperative.
 *    - When a coroutine suspends, it saves its current state and local variables
 *      into a state machine object (the Continuation).
 *    - It immediately RELEASES the underlying thread back to the dispatcher's
 *      thread pool / event loop.
 *    - The thread is completely free to execute other coroutines or tasks in the meantime.
 *    - When the asynchronous operation finishes (e.g. the delay timer fires),
 *      the coroutine is resumed on an available thread (or the same thread)
 *      from the exact point where it was suspended via Continuation.resumeWith().
 *
 * Analogy:
 * - Blocking is like standing in line at a restaurant counter and refusing to
 *   step aside while waiting for your food, preventing anyone else from ordering.
 * - Suspending is like taking a buzzer number and stepping aside: the cashier
 *   serves other customers, and your buzzer rings when your food is ready.
 * ============================================================================
 */
```

---

## Question 1. First coroutine

### Context & Task
A cafe app must confirm the order at once, while the coffee itself needs one second to prepare. The confirmation must not wait for the coffee. Build console app with `runBlocking` where a launched coroutine waits one second with `delay` and prints `"Coffee ready"`, while the main body prints `"Order accepted"` right after the launch.

### Program Output
```text
Order accepted
Coffee ready
```

### Explanation
- **Why "Order accepted" comes first:** `launch` is a non-blocking coroutine builder. It creates and schedules the coroutine on the dispatcher's event loop queue and immediately returns without waiting for the coroutine body to run. The main body continues executing synchronously, reaching `println("Order accepted")` first. The launched coroutine suspends for 1000 ms using `delay(1000L)`, releasing the thread. Once the delay expires, it resumes and prints `"Coffee ready"`.

---

## Question 2. Three baristas

### Context & Task
Three baristas make drinks at the same time on one thread: espresso needs 300 ms, tea 100 ms, and latte 200 ms. A teammate says it is the same if you use a sleep instead of a suspension. Launch three coroutines, each prints "start", waits with delay and prints "end". Then replace delay with `Thread.sleep` and compare outputs.

### Program Output
#### With `delay` (suspension):
```text
espresso start
tea start
latte start
tea end
latte end
espresso end
```

#### With `Thread.sleep` (blocking):
```text
espresso start
espresso end
tea start
tea end
latte start
latte end
```

### One-Sentence Explanation of What the Thread Does in Each Case
- **With `delay`:** The single thread is released at each suspension point, allowing other queued coroutines to interleave and start concurrently before earlier ones finish.
- **With `Thread.sleep`:** The single thread is completely seized and blocked by each coroutine in turn, forcing all other coroutines to wait sequentially one after the other.

---

## Question 3. Chain of suspend functions

### Context & Task
A login flow has three steps: load the user, check the password, and open the profile. Each step takes some time and must run strictly after the previous one. Write three suspend functions where each calls the next with a delay and a printed step name. Call the first from a suspend main.

### Program Output
```text
Starting login flow...
Step 1: Load user
Step 2: Check password
Step 3: Open profile
Login flow completed successfully.
```

### Explanation
Suspending does not alter sequential line ordering within a single coroutine; code executes step-by-step from top to bottom, exactly like normal synchronous code, preserving sequential ordering while freeing threads during suspensions.

---

## Question 4. Calling suspend from normal code

### Context & Task
A teammate wrote a normal function that calls a suspend function to load a user, and the project does not compile. Reproduce the compile error, show its message, fix it in two different ways, and identify which fix you never use on the Android UI thread and why.

### Compile Error Reproduction & Message
```kotlin
fun normalFunction() {
    val data = fetchUserData() 
}
```
**Compiler Error Message:**
```text
Suspend function 'fetchUserData' should be called only from a coroutine or another suspend function
```

### Two Fixes
1. **Fix 1 (Make caller suspend):**
   ```kotlin
   suspend fun callerFunction(): String {
       return fetchUserData()
   }
   ```
2. **Fix 2 (Bridge using a coroutine builder or scope):**
   ```kotlin
   fun callerFunction(): String = runBlocking {
       fetchUserData()
   }
   ```
   *(Or launch asynchronously within an application `CoroutineScope(Dispatchers.Main).launch { ... }`)*

### Which Fix Never to Use on Android UI Thread & Why
**Never use `runBlocking` on the Android UI thread.**  
`runBlocking` forcefully blocks the current thread until the coroutine and all its children complete. On Android, blocking the main UI thread stops event dispatching, animations, and user input processing, leading directly to dropped frames and an **Application Not Responding (ANR)** error.

---

## Question 5. Suspend is not a thread

### Context & Task
A junior developer believes that marking a function `suspend` moves its work to a background thread. Write a suspend function that returns the current thread name, call it from `runBlocking`, print thread names before and inside, and prove or disprove the belief. Name the hidden parameter compiler adds.

### Program Output
```text
Thread before call: main
Thread inside suspend function: main
Thread after call: main
Disproved: The thread name is identical ('main').
Marking a function 'suspend' does NOT move execution to another thread.
```

### Conclusion
- **Proof:** Both the caller and the suspend function executed on the exact same thread (`main`). The junior developer's belief is **disproved**.
- **Hidden Parameter:** The Kotlin compiler appends a hidden parameter of type **`kotlin.coroutines.Continuation<T>`** to every suspend function.

---

## Question 6. Ten downloads, one thread

### Context & Task
A download manager has ten files, but the app is allowed to use only the main thread. Launch ten coroutines under `runBlocking`. Each prints "download i started", waits, and prints "download i finished". Show all 10 start before any finishes. Decide if this is concurrency or parallelism.

### Program Output
```text
Starting 10 downloads on single thread: main
download 0 started on main
download 1 started on main
download 2 started on main
download 3 started on main
download 4 started on main
download 5 started on main
download 6 started on main
download 7 started on main
download 8 started on main
download 9 started on main
download 0 finished on main
download 1 finished on main
download 2 finished on main
download 3 finished on main
download 4 finished on main
download 5 finished on main
download 6 finished on main
download 7 finished on main
download 8 finished on main
download 9 finished on main
```

### Concurrency vs. Parallelism Decision
This is **concurrency, NOT parallelism**:
- **Concurrency** is the composition of independently executing processes (interleaved execution and dealing with multiple things by sharing time). Here, all 10 coroutines make progress on a single CPU thread (`main`) by yielding control during `delay()`.
- **Parallelism** requires the simultaneous execution of multiple computations at the exact same physical instant on separate CPU cores or separate OS threads. Because only one thread is used, only one instruction runs at any nanosecond.

---

## Question 7. Order of launch

### Context & Task
Under `runBlocking`, print `"1"`, launch a coroutine that prints `"2"`, waits 100 ms and prints `"3"`, then print `"4"`, wait 50 ms and print `"5"`. Predict the output before running it and explain why `"2"` does not come right after `"1"`.

### Predicted Output
```text
1
4
2
5
3
```

### Actual Output
```text
1
4
2
5
3
```

### Explanation of Why "2" Does Not Come Right After "1"
`launch` places the new coroutine onto the dispatcher's event queue but does NOT immediately dispatch or interrupt the current executing block. The main thread continues running the current coroutine until it suspends or finishes. Therefore, `"4"` is printed immediately. Only when the parent hits `delay(50)` does it suspend and release the thread, enabling the event loop to run the queued child coroutine, which prints `"2"`. At 50 ms the parent resumes to print `"5"`, and at 100 ms the child resumes to print `"3"`.

---

## Question 8. Parent waits for child

### Context & Task
A report task starts a sub task compressing attachments for 200 ms. Launch a parent coroutine that launches a child coroutine and prints `"parent body done"`. The child waits and prints `"child done"`. In main wait for the parent with `join` and print `"report completed"`. Explain which rule of structured concurrency gives this order.

### Program Output
```text
parent body done
child done
report completed
```

### Rule of Structured Concurrency
**"A parent coroutine does not complete until all of its child coroutines have completed."**  
Even though the body of the parent coroutine reaches its end and prints `"parent body done"`, the parent job remains in the `Completing` state until its child finishes. Therefore, calling `parentJob.join()` in main blocks until the child prints `"child done"` and the parent transitions to `Completed`, after which `"report completed"` prints.

---

## Question 9. Screen closed, requests cancelled

### Context & Task
A user closes a screen while two network requests are still running. Launch a parent coroutine with two child coroutines waiting 1 second inside `try` and printing cleanup in `finally`. Cancel the parent after 100 ms with `cancelAndJoin` and print `"end"`. Explain why children stop although only the parent was cancelled.

### Program Output
```text
Request 1 started
Request 2 started
Screen closed, cancelling parent coroutine...
Request 1 cleanup executed
Request 2 cleanup executed
end
```

### Explanation
In Kotlin's structured concurrency hierarchy, **cancellation propagates downwards from parent to all children**. When `parentJob.cancelAndJoin()` is called, the parent cancels itself and automatically sends a `CancellationException` down to its child jobs, causing their active suspensions (`delay(1000)`) to abort immediately and jump to their `finally` cleanup blocks.

---

## Question 10. Profile ready

### Context & Task
A profile screen loads avatar (200 ms) and settings (100 ms) concurrently. The message `"Profile ready"` must appear only after both are loaded. Use `coroutineScope` with two launched coroutines. Then launch one more coroutine after the scope and show that `"end of main"` prints before it. Explain the difference between the two cases.

### Program Output
```text
Settings loaded
Avatar loaded
Profile ready
end of main
Background analytics sync completed
```

### Explanation of Difference
1. **Inside `coroutineScope`:** `coroutineScope` is a suspending function designed to enforce structured concurrency. It suspends the calling coroutine until all coroutines launched within its block complete. Therefore, `"Profile ready"` is guaranteed to execute strictly after both child tasks finish.
2. **Outside the scope (`launch` on outer scope):** `launch` is a concurrent fire-and-forget builder that does not suspend the caller. The main body immediately proceeds to print `"end of main"` while the launched coroutine runs in the background.

---

## Question 11. Reusable counter

### Context & Task
Write an extension function on `CoroutineScope` that starts a counter coroutine, prints name and number every 100 ms up to a given number, and returns its `Job`. Start two counters with different limits inside `coroutineScope` and print `"launched"` before the first number. Explain why the receiver is needed.

### Program Output
```text
launched
[Counter-A] tick: 1
[Counter-B] tick: 1
[Counter-A] tick: 2
[Counter-B] tick: 2
[Counter-A] tick: 3
Both counters finished (scope completed)
```

### Explanation of Why Receiver is Needed
The `CoroutineScope` receiver is required because `launch` is an extension function on `CoroutineScope`. By defining `fun CoroutineScope.startCounter(...)`, the new coroutine is bound to the caller's scope and inherits its `CoroutineContext` (dispatcher, Job hierarchy, exception handler). This ensures structured concurrency: if the caller's scope is cancelled, the counter coroutine is automatically cancelled as well.

---

## Question 12. Returns at once or waits

### Context & Task
Implement two greeting helpers:
1. An extension on `CoroutineScope` that starts a greeter in the background and returns immediately.
2. A suspend function with `coroutineScope` that greets several people and returns only when all greetings finish.  
In main, call both and demonstrate their return behavior.

### Program Output
```text
--- Testing Helper 1 (Extension on CoroutineScope) ---
Helper 1 called - returned immediately without waiting!
Background greeting: Hello, Alice!

--- Testing Helper 2 (Suspend function with coroutineScope) ---
Batch greeting: Hello, Bob!
Batch greeting: Hello, Charlie!
Batch greeting: Hello, Diana!
Helper 2 completed - waited for all greetings to finish before printing this line!
```

### Summary
- `CoroutineScope.greetInBackground` returns a `Job` immediately without suspending the caller.
- `suspend fun greetAllAndWait` suspends the caller until all greetings inside its `coroutineScope` finish.

---

## Question 13. Five builders

### Context & Task
Write a program that uses `launch`, `async`, `runBlocking`, and `coroutineScope` once each and prints their return values. Provide a complete cheat sheet for all five builders (including `withContext`).

### Program Output
```text
1. launch returned: StandaloneCoroutine{Active}@3498ed (type: Job)
2. async returned: DeferredCoroutine{Active}@79b4d0f (type: Deferred<Int>)
3. coroutineScope returned: Result from coroutineScope block
Inside launch: executing background task
4. withContext returned: Result from withContext on DefaultDispatcher-worker-1
async await() value: 42
5. runBlocking returned: All builders executed successfully
```

### Coroutine Builders Cheat Sheet

| Builder | Type | Return Value | Blocks Calling Thread? | Suspends Caller? | Waits for Children? | Primary Use Case |
| :--- | :--- | :--- | :---: | :---: | :---: | :--- |
| **`launch`** | Extension on `CoroutineScope` | `Job` | ❌ No | ❌ No | ❌ (caller doesn't wait) | "Fire and forget" background tasks without return value |
| **`async`** | Extension on `CoroutineScope` | `Deferred<T>` | ❌ No | ❌ No | ❌ (caller doesn't wait) | Concurrent tasks that compute a return value (via `.await()`) |
| **`runBlocking`** | Regular function | `T` (block result) |  **Yes** | ❌ No (blocks, doesn't suspend) |  Yes | Bridging non-coroutine code (main/tests) to coroutines |
| **`coroutineScope`** | Suspend function | `T` (block result) | ❌ No |  **Yes** |  Yes | Decomposing concurrent subtasks with structured concurrency |
| **`withContext`** | Suspend function | `T` (block result) | ❌ No |  **Yes** |  Yes | Switching thread dispatcher (e.g., to `Dispatchers.IO`) and returning result |

---

## Question 14. Job or Deferred

### Context & Task
A developer needs the result of a calculation ($6 \times 7 = 42$), started it with `launch`, and cannot retrieve the value. Write the `launch` version and show why it fails, then write the `async` version that returns `Deferred` and print 42 with `await`. Explain the difference between `Job` and `Deferred`.

### Program Output
```text
--- Attempt 1: Using launch ---
Inside launch: calculation completed (42)
launch returns a Job (StandaloneCoroutine{Completed}@5315b42e). You cannot retrieve a result value from a Job!

--- Attempt 2: Using async ---
async returns a Deferred<Int> (DeferredCoroutine{Active}@60addb54)
Result retrieved with deferred.await(): 42
```

### Difference Between Job and Deferred
- **`Job`:** Represents a cancellable background task with a lifecycle (Active, Completing, Completed, Cancelled). It does NOT produce a return value (`Job` has no `.await()` function and its completion yields `Unit`).
- **`Deferred<T>`:** A sub-interface of `Job` (`interface Deferred<out T> : Job`) that represents a deferred future result. In addition to full lifecycle management, it provides the suspending function `await(): T` to retrieve the computed result once ready.

---

## Question 15. Three exports in a scope

### Context & Task
A report exports three files concurrently: X takes 300 ms, Y takes 100 ms, and Z takes 200 ms. The message `"All done"` must appear after all three exports without manually joining or delaying. Under `runBlocking`, print `"Start"`, use one `coroutineScope` with three launched coroutines printing their name, and print `"All done"`.

### Program Output
```text
Start
Y
Z
X
All done
```

### Explanation
Because all three tasks are launched concurrently inside a single `coroutineScope`:
1. `Y` finishes first after 100 ms -> prints `Y`.
2. `Z` finishes next after 200 ms -> prints `Z`.
3. `X` finishes last after 300 ms -> prints `X`.
4. `coroutineScope` finishes only after all its launched children complete, ensuring `"All done"` prints strictly at the end without any manual `join()` or arbitrary `delay()` calls.

---

## Extra Questions

### Extra 1. Delay or sleep: measure it
- **Context:** Project manager claims three waits of 300 ms always cost 900 ms.
- **Output:**
  ```text
  --- Measuring delay inside coroutineScope ---
  Time with delay: 308ms (rounded to hundreds: ~300ms)

  --- Measuring Thread.sleep inside coroutineScope ---
  Time with Thread.sleep: 934ms (rounded to hundreds: ~900ms)
  ```
- **Explanation:** With `delay`, all three coroutines suspend non-blockingly and run concurrently on the event loop; total elapsed time is $\max(300, 300, 300) \approx 300\text{ ms}$. With `Thread.sleep`, the underlying thread is blocked completely by each coroutine in sequence, resulting in $300 + 300 + 300 \approx 900\text{ ms}$.

---

### Extra 2. Three levels of coroutines
- **Context:** Inside `coroutineScope`, launch child 1 (which launches a grandchild waiting 200 ms, then child 1 prints "child 1 done") and child 2 (waits 100 ms and prints "child 2 done").
- **Coroutine Tree Diagram:**
  ```text
            coroutineScope
             /          \
        child 1        child 2 (delay 100ms)
          /
     grandchild (delay 200ms)
  ```
- **Output:**
  ```text
  child 1 done
  child 2 done
  grandchild done
  scope done
  ```
- **Explanation:** Child 1 launches grandchild and finishes its body immediately (`child 1 done`). Child 2 completes its 100 ms delay (`child 2 done`). Grandchild finishes its 200 ms delay (`grandchild done`). `coroutineScope` waits for the entire subtree (including grandchildren) to complete before exiting (`scope done`).

---

### Extra 3. No scope, no launch
- **Context:** Teammate calls `launch` directly from a plain `fun main()`.
- **Reproduced Error Message:**
  ```text
  Unresolved reference: launch
  ```
- **Fix:** Wrap the body in `runBlocking { ... }`.
- **Explanation:** `runBlocking` creates a new coroutine scope and blocks the current thread until its execution completes, serving as a bridge between blocking synchronous code and asynchronous coroutines.

---

### Extra 4. What the compiler sees
- **Original Kotlin Signature:**
  ```kotlin
  suspend fun loadUserById(id: Int): User
  ```
- **Decompiled Java Signature:**
  ```java
  public static final Object loadUserById(int id, kotlin.coroutines.Continuation<? super User> $completion)
  ```
- **Extra Parameter:** `Continuation<? super User> $completion`
- **New Return Type:** `java.lang.Object` (can return either the resulting `User` instance or the special marker `kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED`).
- **Purpose of Extra Parameter:** Implements Continuation Passing Style (CPS). The `$completion` object maintains the execution state machine, label pointer, and resumption callback (`resumeWith`) to return the value or exception when asynchronous execution completes.

---

### Extra 5. Greet everybody
- **Context:** Greet Ada (100 ms), Linus (200 ms), and Grace (300 ms) with growing pauses, reporting "after" only when the last greeting completes.
- **Output:**
  ```text
  before
  Hello, Ada
  Hello, Linus
  Hello, Grace
  after
  ```
- **Explanation:** `"after"` is printed last because `greetEverybody` is wrapped in `coroutineScope`, which suspends until all launched child greeting coroutines have completely finished.

---

## 🛠️ How to Build and Run

### Run All Questions
```powershell
gradle run
```
Or directly via Kotlin compiler:
```powershell
kotlinc -cp libs/kotlinx-coroutines-core-jvm-1.10.2.jar src/main/kotlin/uz/ttpu/coroutines/*.kt -d out.jar
java -cp "out.jar;libs/kotlinx-coroutines-core-jvm-1.10.2.jar" uz.ttpu.coroutines.MainKt
```

### Run an Individual Question
```powershell
java -cp "out.jar;libs/kotlinx-coroutines-core-jvm-1.10.2.jar" uz.ttpu.coroutines.MainKt q1
```
*(Replace `q1` with `q1` through `q15`, or `e1` through `e5`)*
