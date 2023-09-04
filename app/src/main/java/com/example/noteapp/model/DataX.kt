package com.example.noteapp.model

data class DataX(
    val product_categories: List<ProductCategory>,
    val total_carts: Int,
    val total_orders: Int,
    val user_data: UserData
)