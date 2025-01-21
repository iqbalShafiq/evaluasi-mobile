package id.usecase.core.domain.assessment

sealed class DataResult <out T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : DataResult<T>(data = data)
    data class Error(val exception: Exception): DataResult<Nothing>()
    data object Loading: DataResult<Nothing>()
}