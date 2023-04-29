package com.mirror.valid8.domain.use_case

class ValidatePhoneNumber {

    fun execute(phone: String): Pair<Boolean, String?> {
        if (phone.isBlank()){
            return Pair(false, "The name can't be blank")
        }
        if (phone.length < 10){
            return Pair(false, "The phone number is not valid")
        }
        if (phone.length > 10){
            return Pair(false, "The phone number is not valid")
        }
        return Pair(true, null)
    }
}