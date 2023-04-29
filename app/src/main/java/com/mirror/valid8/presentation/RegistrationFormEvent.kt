package com.mirror.valid8.presentation

sealed class RegistrationFormEvent {
    data class PhoneNumberChanged(val phoneNumber: String) : RegistrationFormEvent()
    data class FirstNameChanged(val firstName: String) : RegistrationFormEvent()
    data class LastNameChanged(val lastName: String) : RegistrationFormEvent()
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationFormEvent()
    object Submit : RegistrationFormEvent()
}