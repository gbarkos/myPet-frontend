package com.example.mypet.api

import com.example.mypet.models.requests.*
import com.example.mypet.models.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MyPetApi {

    //User
    @POST("users/signup")
    fun registerUser(@Body body: UserRegisterPostRequest): Call<UserLoginRegisterPostResponse>

    @POST("users/login")
    fun loginUser(@Body body: UserLoginPostRequest): Call<UserLoginRegisterPostResponse>

    @GET("users/myProfile")
    fun getUserInfo(): Call<UserGetResponse>

    @PATCH("users/changeEmailStatus")
    fun changeEmailStatus(@Body body: UserChangeEmailPreferencesRequest): Call<UserGetResponse>

    @POST("users/updateFcmToken")
    fun saveFcmToken(@Body body: UserUpdateFcmToken): Call<UserGetResponse>

    //Vet
    @POST("vets/signup")
    fun registerVet(@Body body: VetRegisterPostRequest): Call<VetLoginRegisterPostResponse>

    @POST("vets/login")
    fun loginVet(@Body body: VetLoginPostRequest): Call<VetLoginRegisterPostResponse>

    @GET("vets/myProfile")
    fun getVetInfo(): Call<VetGetResponse>

    //Pet
    @GET("pets")
    fun getPets(): Call<PetsLimitedGetResponse>

    @GET("pets/{id}")
    fun getPet(@Path("id") id: String): Call<PetGetResponse>

    @POST("pets")
    fun newPet(@Body body: PetPostRequest): Call<PetGetResponse>

    @PATCH("pets/set-as-missing/{id}")
    fun setPetAsMissing(@Body body: SetPetAsMissingRequest,
                        @Path("id") id: String?): Call<PetGetResponse>

    @PATCH("pets/set-as-found/{id}")
    fun setPetAsFound(@Path("id") id: String?): Call<PetGetResponse>

    @Multipart
    @PATCH("pets/{id}")
    fun updatePet(@Part photo: MultipartBody.Part?,
                  @Part("id") id: RequestBody?,
                  @Part("distinguishingMarks") distinguishingMarks: RequestBody?,
                  @Part("weight") weight: RequestBody?,
                  @Part("height") height: RequestBody?,
                  @Path("id") _id: String?): Call<PetGetResponse>



    //Medical Record
    @PATCH("records/{id}")
    fun addVaccination(@Body body: VaccinationPostRequest,
                       @Path("id") id: String?,
                       @Query("option") option: String = "vaccination"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addVermifugation(@Body body: VermifugationPatchRequest,
                         @Path("id") id: String?,
                         @Query("option") option: String = "vermifugation"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addTreatment(@Body body: TreatmentPatchRequest,
                     @Path("id") id: String?,
                     @Query("option") option: String = "treatment"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addSurgery(@Body body: SurgeryPatchRequest,
                   @Path("id") id: String?,
                   @Query("option") option: String = "surgery"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addDiagnosticTest(@Body body: DiagnosticTestPatchRequest,
                          @Path("id") id: String?,
                          @Query("option") option: String = "test"): Call<MedicalRecordPatchResponse>

    // Reminders
    @GET("reminders")
    fun getReminders() : Call<RemindersGetResponse>

    @POST("reminders")
    fun newReminder(@Body body: NewReminder) : Call<SingleReminderGetResponse>

    @POST("reminders/{id}")
    fun deleteReminder(@Path("id") id: String) : Call<RemindersGetResponse>

}
