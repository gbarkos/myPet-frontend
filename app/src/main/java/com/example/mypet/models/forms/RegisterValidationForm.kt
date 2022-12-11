package com.example.mypet.models.forms

import com.example.mypet.models.enums.RegisterErrorCodes
import com.example.mypet.models.enums.RegisterFormFields

data class RegisterValidationForm (
    var username : RegisterFormFields = RegisterFormFields.Username,
    var usernameError : RegisterErrorCodes = RegisterErrorCodes.NoError,
    var hasUsernamePassedValidation : Boolean = true,
    var email : RegisterFormFields = RegisterFormFields.Email,
    var emailError : RegisterErrorCodes = RegisterErrorCodes.NoError,
    var hasEmailPassedValidation : Boolean = true,
    var password : RegisterFormFields = RegisterFormFields.Password,
    var passwordValue : String = "",
    var passwordError : RegisterErrorCodes = RegisterErrorCodes.NoError,
    var hasPasswordPassedValidation : Boolean = true,
    var confirmPassword : RegisterFormFields = RegisterFormFields.ConfirmPassword,
    var confirmPasswordValue : String = "",
    var confirmPasswordError : RegisterErrorCodes = RegisterErrorCodes.NoError,
    var hasConfirmPasswordPassedValidation : Boolean = true,
){
    fun isFormValidated() : Boolean {
        return this.hasUsernamePassedValidation &&
                this.hasEmailPassedValidation &&
                this.hasPasswordPassedValidation &&
                this.hasConfirmPasswordPassedValidation
    }
}