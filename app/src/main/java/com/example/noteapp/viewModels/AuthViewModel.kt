package com.example.noteapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.model.NeoUserRequest
import com.example.noteapp.model.NeoUserResponse
import com.example.noteapp.repository.UserRepository
import com.example.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) :ViewModel(){


    val userResponse:LiveData<Resource<NeoUserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest:NeoUserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(email:String,password:String){
        viewModelScope.launch {
            userRepository.loginUser(email,password)
        }
    }
}