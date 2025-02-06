package id.usecase.core.domain.utils

sealed class DataResult <out T>() {
    class Success<T>(val data: T) : DataResult<T>()
    data object Loading: DataResult<Nothing>()
}