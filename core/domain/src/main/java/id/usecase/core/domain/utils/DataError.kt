package id.usecase.core.domain.utils

sealed interface DataError: Error {
    val message: String?

    sealed class Network(override val message: String? = null) : DataError {
        data class REQUEST_TIMEOUT(override val message: String? = null) : Network(message)
        data class BAD_REQUEST(override val message: String? = null) : Network(message)
        data class UNAUTHORIZED(override val message: String? = null) : Network(message)
        data class CONFLICT(override val message: String? = null) : Network(message)
        data class TOO_MANY_REQUESTS(override val message: String? = null) : Network(message)
        data class NO_INTERNET(override val message: String? = null) : Network(message)
        data class PAYLOAD_TOO_LARGE(override val message: String? = null) : Network(message)
        data class SERVER_ERROR(override val message: String? = null) : Network(message)
        data class SERIALIZATION(override val message: String? = null) : Network(message)
        data class UNKNOWN(override val message: String? = null) : Network(message)
    }

    sealed class Local(override val message: String? = null) : DataError {
        data class DISK_FULL(override val message: String? = null) : Local(message)
    }
}