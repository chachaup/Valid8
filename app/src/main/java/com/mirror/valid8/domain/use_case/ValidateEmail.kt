package com.mirror.valid8.domain.use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): Pair<Boolean, String?> {
        if (email.isBlank()){
            return Pair(false, "The email can't be blank")
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return Pair(false, "The email is not valid")
        }
        return Pair(true, null)
    }
}