package id.usecase.core.domain.assessment

sealed class DataResult <out T> {
    data class Success<out T>(val data: T): DataResult<T>()
    data class Error(val exception: Exception): DataResult<Nothing>()
    object Loading: DataResult<Nothing>()
}