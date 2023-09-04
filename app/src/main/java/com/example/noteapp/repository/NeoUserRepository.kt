package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NeoUserDataAPI
import com.example.noteapp.model.NeoUserData
import com.example.noteapp.utils.Resource
import javax.inject.Inject

class NeoUserRepository @Inject constructor(private val neoUserDataAPI: NeoUserDataAPI) {


    private val _neoUserDataMutableList = MutableLiveData<Resource<NeoUserData>>()
    val neoUserLiveData: LiveData<Resource<NeoUserData>> get() = _neoUserDataMutableList

    suspend fun getUserDetails() {
        _neoUserDataMutableList.postValue(Resource.Loading())
        val response=neoUserDataAPI.getAccountDetails()
        if (response.isSuccessful && response.body() != null) {
            _neoUserDataMutableList.postValue(Resource.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            _neoUserDataMutableList.postValue(Resource.Error(response.errorBody().toString()))
        } else {
            _neoUserDataMutableList.postValue(Resource.Error("Something Went Wrong"))
        }
    }
}