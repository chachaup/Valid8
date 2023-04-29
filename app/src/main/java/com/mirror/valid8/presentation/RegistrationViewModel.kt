package com.mirror.valid8.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mirror.valid8.domain.use_case.ValidateEmail
import com.mirror.valid8.domain.use_case.ValidateFirstName
import com.mirror.valid8.domain.use_case.ValidateLastName
import com.mirror.valid8.domain.use_case.ValidatePassword
import com.mirror.valid8.domain.use_case.ValidatePhoneNumber
import com.mirror.valid8.domain.use_case.ValidateRepeatedPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateFirstName: ValidateFirstName,
    private val validateLastName: ValidateLastName,
    private val validateEmail: ValidateEmail,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword
) : ViewModel() {

    private val _state = MutableStateFlow(FormValidationState())

    private val _validationEventChannel = Channel<ValidationEvent>()
    val validationEventChannel = _validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.FirstNameChanged -> {
                _state.value = _state.value.copy(firstName = event.firstName)
            }
            is RegistrationFormEvent.LastNameChanged -> {
                _state.value = _state.value.copy(lastName = event.lastName)
            }
            is RegistrationFormEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is RegistrationFormEvent.PhoneNumberChanged -> {
                _state.value = _state.value.copy(phoneNumber = event.phoneNumber)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }
            is RegistrationFormEvent.ConfirmPasswordChanged -> {
                _state.value = _state.value.copy(confirmPassword = event.confirmPassword)
            }
            is RegistrationFormEvent.Submit -> {
                saveData()
            }
        }
    }

    private fun saveData() {
        val emailResult = validateEmail.execute(_state.value.email)
        val phoneNumberResult = validatePhoneNumber.execute(_state.value.phoneNumber)
        val firstNameResult = validateFirstName.execute(_state.value.firstName)
        val lastNameResult = validateLastName.execute(_state.value.lastName)
        val passwordResult = validatePassword.execute(_state.value.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            _state.value.password,
            _state.value.confirmPassword
        )

        val hasError = listOf(
            emailResult,
            phoneNumberResult,
            firstNameResult,
            lastNameResult,
            passwordResult,
            repeatedPasswordResult
        ).any { !it.first }

        if (hasError) {
            _state.value = _state.value.copy(
                emailError = emailResult.second,
                phoneNumberError = phoneNumberResult.second,
                firstNameError = firstNameResult.second,
                lastNameError = lastNameResult.second,
                passwordError = passwordResult.second,
                confirmPasswordError = repeatedPasswordResult.second
            )
            return
        }
        viewModelScope.launch {
            _validationEventChannel.send(ValidationEvent.Success)

            // TODO: Save data to database
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()

    }

    companion object{
        val factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                RegistrationViewModel(
                    ValidateFirstName(),
                    ValidateLastName(),
                    ValidateEmail(),
                    ValidatePhoneNumber(),
                    ValidatePassword(),
                    ValidateRepeatedPassword()
                )
            }
        }
    }

}