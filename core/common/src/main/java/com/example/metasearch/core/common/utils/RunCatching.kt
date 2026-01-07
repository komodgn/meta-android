package com.example.metasearch.core.common.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 코루틴 환경에서 안전하게 예외를 포착하는 유틸리티
 * * [CancellationException]은 그대로 던져 코루틴의 취소 메커니즘을 유지하고,
 * 그 외의 예외(타임아웃 포함)는 [Result]로 캡슐화합니다.
 */
@OptIn(ExperimentalContracts::class)
suspend inline fun <R> runSuspendCatching(crossinline block: suspend () -> R): Result<R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return try {
        Result.success(block())
    } catch (e: TimeoutCancellationException) {
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
