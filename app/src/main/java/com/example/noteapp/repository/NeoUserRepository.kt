package com.example.noteapp.repository

import com.example.noteapp.api.NeoUserDataAPI
import com.example.noteapp.model.NeoUserData
import com.example.noteapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NeoUserRepository @Inject constructor(private val neoUserDataAPI: NeoUserDataAPI) {


    private val _neoUserDataMutableList = MutableStateFlow<Resource<NeoUserData>>(Resource.Loading())
    val neoUserLiveData: StateFlow<Resource<NeoUserData>> get() = _neoUserDataMutableList

    suspend fun getUserDetails() {
        //_neoUserDataMutableList.value=Resource.Loading()
            val response=neoUserDataAPI.getAccountDetails()
            if (response.isSuccessful && response.body() != null) {
                _neoUserDataMutableList.emit(Resource.Success(response.body()!!))

            } else if (response.errorBody() != null) {
                _neoUserDataMutableList.emit(Resource.Error(response.errorBody().toString()))
            } else {
                _neoUserDataMutableList.emit(Resource.Error("Something Went Wrong"))
            }

    }
}