package id.usecase.core.domain.assessment.utils

interface FlowResultHandler {
    suspend fun onLoading()
    suspend fun onErrorOccurred(
        errorMessage: String?,
        retryCallback: (() -> Unit)? = null
    )
}