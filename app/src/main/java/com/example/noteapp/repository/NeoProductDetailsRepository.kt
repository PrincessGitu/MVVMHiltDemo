package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NeoUserDataAPI
import com.example.noteapp.model.ProductDetails
import com.example.noteapp.utils.Resource
import javax.inject.Inject

class NeoProductDetailsRepository @Inject constructor(private val neoUserDataAPI: NeoUserDataAPI) {

    private val _productDetailsMutableList=MutableLiveData<Resource<ProductDetails>>()
    val productDetailsLiveData:LiveData<Resource<ProductDetails>>
        get() = _productDetailsMutableList

    suspend fun getProductDetails(productId:String){
        _productDetailsMutableList.postValue(Resource.Loading())
        val response= neoUserDataAPI.getProductDetails(productId)
        if (response.isSuccessful && response.body() != null) {
            _productDetailsMutableList.postValue(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _productDetailsMutableList.postValue(Resource.Error(response.errorBody().toString()))
        } else {
            _productDetailsMutableList.postValue(Resource.Error("Something Went Wrong"))
        }
    }
}