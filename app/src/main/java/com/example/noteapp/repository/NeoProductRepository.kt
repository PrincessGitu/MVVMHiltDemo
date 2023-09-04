package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.api.NeoUserDataAPI
import com.example.noteapp.model.ProductList
import com.example.noteapp.utils.Resource
import javax.inject.Inject

class NeoProductRepository @Inject constructor(private val neoUserDataAPI: NeoUserDataAPI) {

    private val _productMutableList=MutableLiveData<Resource<ProductList>>()
    val productLiveData:LiveData<Resource<ProductList>>
        get() = _productMutableList


    suspend fun getProductList(productCategoryId:String){
        _productMutableList.postValue(Resource.Loading())
        val response= neoUserDataAPI.getProductList(productCategoryId)
        if (response.isSuccessful && response.body() != null) {
            _productMutableList.postValue(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            _productMutableList.postValue(Resource.Error(response.errorBody().toString()))
        } else {
            _productMutableList.postValue(Resource.Error("Something Went Wrong"))
        }
    }


}