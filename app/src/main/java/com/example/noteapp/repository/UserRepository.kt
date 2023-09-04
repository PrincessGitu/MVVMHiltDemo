package com.example.noteapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NeoUserAPI
import com.example.noteapp.model.NeoUserRequest
import com.example.noteapp.model.NeoUserResponse
import com.example.noteapp.utils.Resource
import javax.inject.Inject


class UserRepository @Inject constructor(private val userAPI: NeoUserAPI) {

    private val _userResponse = MutableLiveData<Resource<NeoUserResponse>>()
    val userResponseLiveData: LiveData<Resource<NeoUserResponse>>
        get() = _userResponse


    suspend fun registerUser(userRequest:NeoUserRequest) {
        _userResponse.postValue(Resource.Loading())
        val response = userAPI.signup(userRequest.first_name,
            userRequest.last_name, userRequest.email,
            userRequest.password,userRequest.confirm_password,userRequest.gender,userRequest.phone_no)

        if (response.isSuccessful && response.body() != null) {
            _userResponse.postValue(Resource.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val error = response.errorBody().toString()
            //  val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            Log.e("error", "" + error)
            _userResponse.postValue(Resource.Error(response.errorBody().toString()))
        } else {
            _userResponse.postValue(Resource.Error("Something Went Wrong"))
        }
    }

    suspend fun loginUser(email:String,password:String) {
        _userResponse.postValue(Resource.Loading())
          val response=  userAPI.signIn(email,password)
        if (response.isSuccessful && response.body() != null) {
            _userResponse.postValue(Resource.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val error = response.errorBody().toString()
            Log.e("error", "" + error)
            _userResponse.postValue(Resource.Error(response.errorBody().toString()))
        } else {
            _userResponse.postValue(Resource.Error("Something Went Wrong"))
        }
    }
}