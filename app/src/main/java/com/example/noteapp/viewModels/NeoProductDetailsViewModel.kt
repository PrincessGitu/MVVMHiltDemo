package com.example.noteapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.model.ProductDetails
import com.example.noteapp.repository.NeoProductDetailsRepository
import com.example.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeoProductDetailsViewModel @Inject constructor(private val repository: NeoProductDetailsRepository):ViewModel() {
    
    val productDetailsList:LiveData<Resource<ProductDetails>>
        get() = repository.productDetailsLiveData

    fun getProductDetails(productId:String){
        viewModelScope.launch {
            repository.getProductDetails(productId)
        }
    }

}