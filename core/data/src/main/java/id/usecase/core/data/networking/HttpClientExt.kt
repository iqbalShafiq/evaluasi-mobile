package id.usecase.core.data.networking

import id.usecase.core.domain.utils.DataError
import id.usecase.core.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.jsonPrimitive

suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request, reified Response : Any> HttpClient.post(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET(e.message))
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION(e.message))
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN(e.message))
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    val errorMessage = response.extractErrorMessage()
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body<T>())
        400 -> Result.Error(DataError.Network.BAD_REQUEST(errorMessage))
        401 -> Result.Error(DataError.Network.UNAUTHORIZED(errorMessage))
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT(errorMessage))
        409 -> Result.Error(DataError.Network.CONFLICT(errorMessage))
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE(errorMessage))
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS(errorMessage))
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR(errorMessage))
        else -> Result.Error(DataError.Network.UNKNOWN(errorMessage))
    }
}

fun constructRoute(route: String): String {
    val baseUrl = "https://evaluasi-api.iqbalsyafiq.space"
    return when {
        route.contains(baseUrl) -> route
        route.startsWith("/") -> baseUrl + route
        else -> "$baseUrl/$route"
    }
}

/**
 * Extension function to extract error message from JSON response
 */
suspend fun HttpResponse.extractErrorMessage(): String? {
    return try {
        val jsonObject = kotlinx.serialization.json.Json.decodeFromString(
            kotlinx.serialization.json.JsonObject.serializer(),
            this.bodyAsText()
        )
        jsonObject["message"]?.jsonPrimitive?.content
    } catch (e: Exception) {
        null
    }
}