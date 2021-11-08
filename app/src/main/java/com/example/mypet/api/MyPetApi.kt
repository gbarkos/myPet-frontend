package com.example.mypet.api

import com.example.mypet.models.requests.*
import com.example.mypet.models.responses.*
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

    //Vet
    @POST("vet/signup")
    fun registerVet(@Body body: VetRegisterPostRequest): Call<VetLoginRegisterPostResponse>

    @POST("vet/login")
    fun loginVet(@Body body: VetLoginPostRequest): Call<VetLoginRegisterPostResponse>

    @GET("vets/myProfile")
    fun getVetInfo(): Call<VetGetResponse>

    //Pet
    @GET("pets")
    fun getPets(): Call<PetsGetResponse>

    @GET("pets/{id}")
    fun getPet(@Path("id") id: String): Call<PetGetResponse>

    @POST("pets")
    fun newPet(@Body body: PetPostRequest): Call<PetPostRequest>

    @PATCH("pets/{id}")
    fun updatePet(@Query("height") height: Double,
                  @Query("weight") weight: Double): Call<PetPatchResponse>

    //Medical Record
    @PATCH("records/{id}")
    fun addVaccination(@Body body: VaccinationPostRequest,
                       @Path("id") id: String,
                       @Query("option") option: String = "vaccination"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addVermifugation(@Body body: VermifugationPatchRequest,
                         @Path("id") id: String,
                         @Query("option") option: String = "vermifugation"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addTreatment(@Body body: TreatmentPatchRequest,
                     @Path("id") id: String,
                     @Query("option") option: String = "treatment"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addSurgery(@Body body: SurgeryPatchRequest,
                   @Path("id") id: String,
                   @Query("option") option: String = "surgery"): Call<MedicalRecordPatchResponse>

    @PATCH("records/{id}")
    fun addDiagnosticTest(@Body body: DiagnosticTestPatchRequest,
                          @Path("id") id: String,
                          @Query("option") option: String = "test"): Call<MedicalRecordPatchResponse>

}