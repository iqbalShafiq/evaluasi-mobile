package id.usecase.evaluasi.authentication.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}