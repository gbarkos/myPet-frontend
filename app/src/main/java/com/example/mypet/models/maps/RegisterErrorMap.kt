package com.example.mypet.models.maps

import com.example.mypet.models.enums.RegisterErrorCodes

class RegisterErrorMap {

    companion object{
        val ErrorMap = mapOf<RegisterErrorCodes, String>(
            RegisterErrorCodes.MissingUsername to "Το πεδίο είναι υποχρεωτικό",
            RegisterErrorCodes.MissingEmail to "Το πεδίο είναι υποχρεωτικό",
            RegisterErrorCodes.MissingPassword to "Το πεδίο είναι υποχρεωτικό",
            RegisterErrorCodes.MissingConfirmPassword to "Το πεδίο είναι υποχρεωτικό",
            RegisterErrorCodes.InvalidEmail to "Το email δεν είναι έγκυρο",
            RegisterErrorCodes.PasswordsDoNotMatch to "Οι κωδικοί δεν ταιριάζουν",
            RegisterErrorCodes.InvalidPassword to "Ο κωδικός πρέπει να έχει μήκος τουλάχισοτν 8 χαρακτήρες",
            RegisterErrorCodes.InvalidUsername to "Το όνομα χρήστη πρέπει να έχει τουλάχιστον 6 χαρακτήρες"
        )
    }
}