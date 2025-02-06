package id.usecase.evaluasi.authentication.data

import android.util.Patterns
import id.usecase.evaluasi.authentication.domain.PatternValidator

object EmailPatternValidator : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}