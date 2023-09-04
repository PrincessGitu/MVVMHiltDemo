package com.example.noteapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.model.NeoUserData
import com.example.noteapp.repository.NeoUserRepository
import com.example.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeoUserViewModel @Inject constructor(private val neoUserRepository: NeoUserRepository) :
    ViewModel() {

    val neoUserData: LiveData<Resource<NeoUserData>>
        get() = neoUserRepository.neoUserLiveData

    fun getUserDetails() {
        viewModelScope.launch {
            neoUserRepository.getUserDetails()
        }
    }
}