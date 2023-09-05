package com.example.noteapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.model.ProductList
import com.example.noteapp.repository.NeoProductRepository
import com.example.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeoProductViewModel @Inject constructor(private val repository: NeoProductRepository) :ViewModel(){

    val productLit:StateFlow<Resource<ProductList>>
        get() = repository.productLiveData

    fun getProductList(productCategoryId:String){
        viewModelScope.launch {
            repository.getProductList(productCategoryId)
        }
    }

}

