package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NeoUserDataAPI
import com.example.noteapp.model.ProductList
import com.example.noteapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NeoProductRepository @Inject constructor(private val neoUserDataAPI: NeoUserDataAPI) {

    private val _productMutableList= MutableStateFlow<Resource<ProductList>>(Resource.Loading())
    val productLiveData:StateFlow<Resource<ProductList>>
        get() = _productMutableList


    suspend fun getProductList(productCategoryId:String){
        val response= neoUserDataAPI.getProductList(productCategoryId)
        if (response.isSuccessful && response.body() != null) {
            _productMutableList.emit(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _productMutableList.emit(Resource.Error(response.errorBody().toString()))
        } else {
            _productMutableList.emit(Resource.Error("Something Went Wrong"))
        }
    }


}