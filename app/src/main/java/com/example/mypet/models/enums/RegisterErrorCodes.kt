package com.example.mypet.models.enums

enum class RegisterErrorCodes {
    MissingUsername,
    MissingEmail,
    MissingPassword,
    MissingConfirmPassword,
    InvalidEmail,
    InvalidPassword,
    InvalidUsername,
    PasswordsDoNotMatch,
    NoError
}