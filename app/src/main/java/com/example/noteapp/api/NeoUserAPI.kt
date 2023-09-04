package com.example.noteapp.api


import com.example.noteapp.model.NeoUserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NeoUserAPI {




    @FormUrlEncoded
    @POST("users/login")
    suspend fun signIn(@Field("email") email: String,
                        @Field("password") password: String) : Response<NeoUserResponse>

    @FormUrlEncoded
    @POST("users/register")
    suspend  fun signup(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("gender") gender: String,
        @Field("phone_no") phoneNo: String,
    ): Response<NeoUserResponse>
}