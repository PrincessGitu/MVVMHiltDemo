package com.example.noteapp.api

import com.example.noteapp.model.NeoUserData
import com.example.noteapp.model.ProductDetails
import com.example.noteapp.model.ProductList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoUserDataAPI {

    @GET("users/getUserData")
    suspend fun getAccountDetails() :Response<NeoUserData>

    @GET("products/getList")
    suspend fun getProductList(@Query("product_category_id") productCategoryId:String) :Response<ProductList>

    @GET("products/getDetail")
    suspend fun getProductDetails(@Query("product_id") productId:String) :Response<ProductDetails>


}