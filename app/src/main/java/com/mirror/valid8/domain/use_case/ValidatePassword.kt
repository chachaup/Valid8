package com.mirror.valid8.domain.use_case

class ValidatePassword {

    fun execute(password: String): Pair<Boolean, String?> {
        if (password.length < 8){
            return Pair(false, "The password needs to have at least 8 characters")
        }

        val containsLettersAndDigits = password.any { it.isLetter() } && password.any { it.isDigit() }

        if (!containsLettersAndDigits){
            return Pair(false, "The password needs to have letters and digits")
        }
        return Pair(true, null)
    }
}