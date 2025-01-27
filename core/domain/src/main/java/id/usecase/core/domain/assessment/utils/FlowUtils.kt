package id.usecase.core.domain.assessment.utils

import id.usecase.core.domain.assessment.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<DataResult<T>>.handleSuccessResult(
    listener: FlowResultHandler,
    retryCallback: (() -> Unit)? = null,
    successCallback: suspend (DataResult<T>) -> Unit,
) = catch { e ->
    listener.onErrorOccurred(
        errorMessage = e.message,
        retryCallback = retryCallback
    )
}.collectLatest { result ->
    when (result) {
        is DataResult.Loading -> listener.onLoading()
        is DataResult.Success<T> -> successCallback(result)
    }
}