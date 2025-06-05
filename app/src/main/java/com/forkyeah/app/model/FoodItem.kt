package com.forkyeah.app.model

data class FoodItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val recipeUrl: String? = null
) 