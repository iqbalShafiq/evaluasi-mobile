package id.usecase.core.domain.assessment

sealed class DataResult <out T>() {
    class Success<T>(val data: T) : DataResult<T>()
    data object Loading: DataResult<Nothing>()
}