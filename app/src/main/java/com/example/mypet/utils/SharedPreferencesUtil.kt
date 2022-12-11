package com.example.mypet.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    private lateinit var sharedPreferences: SharedPreferences
    @JvmStatic
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    @JvmStatic
    fun writeToSharedPreferences(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    @JvmStatic
    fun readFromSharedPreferences(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
    @JvmStatic
    fun deleteFromSharedPreferences(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
    @JvmStatic
    fun saveAccessToken(accessToken: String) {
        writeToSharedPreferences(Constants().getAccessToken(), accessToken)
    }
    @JvmStatic
    fun getAccessToken(): String? {
        return readFromSharedPreferences(Constants().getAccessToken())
    }
    @JvmStatic
    fun saveVetData(vet : String) {
        return writeToSharedPreferences("VET_PROFILE", vet)
    }
    @JvmStatic
    fun getVetData(): String?{
        return readFromSharedPreferences("VET_PROFILE")
    }
    @JvmStatic
    fun deleteVetData() {
        return deleteFromSharedPreferences("VET_PROFILE")
    }
    @JvmStatic
    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
    @JvmStatic
    fun saveUsername(username : String){
        writeToSharedPreferences("USERNAME", username)
    }
    @JvmStatic
    fun savePassword(password : String){
        writeToSharedPreferences("PASSWORD", password)
    }
    @JvmStatic
    fun getUsername(): String?{
        return readFromSharedPreferences("USERNAME")
    }
    @JvmStatic
    fun getPassword(): String?{
        return readFromSharedPreferences("PASSWORD")
    }
    @JvmStatic
    fun clearUserData(){
        deleteFromSharedPreferences("USERNAME")
        deleteFromSharedPreferences("PASSWORD")
        return
    }
}