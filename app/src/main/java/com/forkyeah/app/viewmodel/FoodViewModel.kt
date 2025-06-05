package com.forkyeah.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forkyeah.app.model.FoodItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val _foodItems = MutableLiveData<List<FoodItem>>()
    val foodItems: LiveData<List<FoodItem>> = _foodItems

    init {
        Log.d("FoodViewModel", "Initializing ViewModel")
        loadInitialFood()
    }

    private fun loadInitialFood() {
        val sampleFood = listOf(
            FoodItem(
                id = "1",
                name = "Margherita Pizza",
                imageUrl = "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=800&auto=format&fit=crop&q=60",
                description = "Classic Italian pizza with fresh mozzarella, tomatoes, and basil",
                recipeUrl = "https://www.allrecipes.com/recipe/240376/homemade-margherita-pizza/"
            ),
            FoodItem(
                id = "2",
                name = "Pad Thai",
                imageUrl = "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=800&auto=format&fit=crop&q=60",
                description = "Stir-fried rice noodles with eggs, tofu, and peanuts in a sweet and sour sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/42968/pad-thai/"
            ),
            FoodItem(
                id = "3",
                name = "Beef Burger",
                imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&auto=format&fit=crop&q=60",
                description = "Juicy beef patty with fresh vegetables and special sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/25473/the-perfect-basic-burger/"
            ),
            FoodItem(
                id = "4",
                name = "Sushi Platter",
                imageUrl = "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&auto=format&fit=crop&q=60",
                description = "Assortment of fresh fish and seafood on seasoned rice",
                recipeUrl = "https://www.allrecipes.com/recipe/24228/sushi-roll/"
            ),
            FoodItem(
                id = "5",
                name = "Chicken Curry",
                imageUrl = "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=800&auto=format&fit=crop&q=60",
                description = "Tender chicken in a rich, spiced curry sauce with aromatic spices",
                recipeUrl = "https://www.allrecipes.com/recipe/212721/indian-chicken-curry-murgh-kari/"
            ),
            FoodItem(
                id = "6",
                name = "Caesar Salad",
                imageUrl = "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=800&auto=format&fit=crop&q=60",
                description = "Crisp romaine lettuce with parmesan, croutons, and Caesar dressing",
                recipeUrl = "https://www.allrecipes.com/recipe/229063/classic-restaurant-caesar-salad/"
            ),
            FoodItem(
                id = "7",
                name = "Pasta Carbonara",
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=800&auto=format&fit=crop&q=60",
                description = "Creamy pasta with pancetta, eggs, and parmesan cheese",
                recipeUrl = "https://www.allrecipes.com/recipe/245775/spaghetti-alla-carbonara-the-traditional-italian-recipe/"
            ),
            FoodItem(
                id = "8",
                name = "Fish and Chips",
                imageUrl = "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=800&auto=format&fit=crop&q=60",
                description = "Crispy battered fish with golden fries and tartar sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/254710/beer-battered-fish-and-chips/"
            ),
            FoodItem(
                id = "9",
                name = "Tacos",
                imageUrl = "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=800&auto=format&fit=crop&q=60",
                description = "Corn tortillas filled with seasoned meat, fresh vegetables, and salsa",
                recipeUrl = "https://www.allrecipes.com/recipe/46653/taco-seasoning-i/"
            ),
            FoodItem(
                id = "10",
                name = "Ramen",
                imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=800&auto=format&fit=crop&q=60",
                description = "Japanese noodle soup with rich broth, soft-boiled egg, and toppings",
                recipeUrl = "https://www.allrecipes.com/recipe/139878/homemade-ramen/"
            ),
            FoodItem(
                id = "11",
                name = "Greek Salad",
                imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&auto=format&fit=crop&q=60",
                description = "Fresh vegetables with feta cheese, olives, and olive oil dressing",
                recipeUrl = "https://www.allrecipes.com/recipe/214931/authentic-greek-salad/"
            ),
            FoodItem(
                id = "12",
                name = "Chicken Wings",
                imageUrl = "https://images.unsplash.com/photo-1608039829572-78524f79c4c7?w=800&auto=format&fit=crop&q=60",
                description = "Crispy wings tossed in your choice of sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/24087/easy-baked-chicken-wings/"
            ),
            FoodItem(
                id = "13",
                name = "Shrimp Scampi",
                imageUrl = "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800&auto=format&fit=crop&q=60",
                description = "Garlic butter shrimp with linguine and white wine sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/229960/shrimp-scampi-with-pasta/"
            ),
            FoodItem(
                id = "14",
                name = "Vegetable Stir Fry",
                imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800&auto=format&fit=crop&q=60",
                description = "Colorful mix of fresh vegetables in a savory sauce",
                recipeUrl = "https://www.allrecipes.com/recipe/24074/quick-and-easy-vegetable-stir-fry/"
            ),
            FoodItem(
                id = "15",
                name = "Chocolate Cake",
                imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&auto=format&fit=crop&q=60",
                description = "Rich, moist chocolate cake with chocolate frosting",
                recipeUrl = "https://www.allrecipes.com/recipe/17981/one-bowl-chocolate-cake-iii/"
            )
        )
        _foodItems.value = sampleFood.shuffled()
    }

    fun loadNewSearch() {
        viewModelScope.launch {
            // Simulate network delay
            delay(1000)
            // Get current list and shuffle it before posting
            val currentList = _foodItems.value ?: return@launch
            _foodItems.value = currentList.shuffled()
        }
    }
} 