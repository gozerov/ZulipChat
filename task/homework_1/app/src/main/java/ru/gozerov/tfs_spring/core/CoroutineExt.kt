package ru.gozerov.tfs_spring.core

import kotlinx.coroutines.CancellationException
import java.lang.Exception

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}