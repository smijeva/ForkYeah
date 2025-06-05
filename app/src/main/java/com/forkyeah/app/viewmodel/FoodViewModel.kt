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
    
    private var lastLikedItem: FoodItem? = null
    private var currentIndex: Int = 0

    init {
        Log.d("FoodViewModel", "Initializing ViewModel")
        loadInitialFood()
    }

    private fun loadInitialFood() {
        val sampleFood = listOf(
            FoodItem(
                id = "1",
                name = "Margherita Pizza",
                imageUrl = "https://images.unsplash.com/photo-1664309641932-0e03e0771b97?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/240376/homemade-margherita-pizza/"
            ),
            FoodItem(
                id = "2",
                name = "Pad Thai",
                imageUrl = "https://images.unsplash.com/photo-1559314809-0d155014e29e?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/42968/pad-thai/"
            ),
            FoodItem(
                id = "3",
                name = "Beef Burger",
                imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/25473/the-perfect-basic-burger/"
            ),
            FoodItem(
                id = "4",
                name = "Sushi Platter",
                imageUrl = "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/24228/sushi-roll/"
            ),
            FoodItem(
                id = "5",
                name = "Chicken Curry",
                imageUrl = "https://images.unsplash.com/photo-1708184528306-f75a0a5118ee?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/212721/indian-chicken-curry-murgh-kari/"
            ),
            FoodItem(
                id = "6",
                name = "Caesar Salad",
                imageUrl = "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/229063/classic-restaurant-caesar-salad/"
            ),
            FoodItem(
                id = "7",
                name = "Pasta Carbonara",
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/245775/spaghetti-alla-carbonara-the-traditional-italian-recipe/"
            ),
            FoodItem(
                id = "8",
                name = "Fish and Chips",
                imageUrl = "https://images.unsplash.com/photo-1697748836791-9ddf7e616ece?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/254710/beer-battered-fish-and-chips/"
            ),
            FoodItem(
                id = "9",
                name = "Tacos",
                imageUrl = "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/46653/taco-seasoning-i/"
            ),
            FoodItem(
                id = "10",
                name = "Ramen",
                imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/139878/homemade-ramen/"
            ),
            FoodItem(
                id = "11",
                name = "Greek Salad",
                imageUrl = "https://images.unsplash.com/photo-1599021419847-d8a7a6aba5b4?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/214931/authentic-greek-salad/"
            ),
            FoodItem(
                id = "12",
                name = "Chicken Wings",
                imageUrl = "https://images.unsplash.com/photo-1567620832903-9fc6debc209f?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/24087/easy-baked-chicken-wings/"
            ),
            FoodItem(
                id = "13",
                name = "Shrimp Scampi",
                imageUrl = "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/229960/shrimp-scampi-with-pasta/"
            ),
            FoodItem(
                id = "14",
                name = "Vegetable Stir Fry",
                imageUrl = "https://images.unsplash.com/photo-1516901121982-4ba280115a36?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/24074/quick-and-easy-vegetable-stir-fry/"
            ),
            FoodItem(
                id = "15",
                name = "Chocolate Cake",
                imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&auto=format&fit=crop&q=60",
                recipeUrl = "https://www.allrecipes.com/recipe/17981/one-bowl-chocolate-cake-iii/"
            )
        )
        _foodItems.value = sampleFood.shuffled()
    }

    fun loadNewSearch() {
        viewModelScope.launch {
            // Simulate network delay
            delay(1000)
            // Get current list
            val currentList = _foodItems.value ?: return@launch
            
            // Create a new list excluding the last liked item
            val filteredList = currentList.filter { it != lastLikedItem }
            
            // If the filtered list is empty (all items were liked), reset to full list
            val finalList = if (filteredList.isEmpty()) {
                lastLikedItem = null
                currentList
            } else {
                filteredList
            }
            
            _foodItems.value = finalList
        }
    }

    fun shuffleCurrentList(currentIndex: Int) {
        val currentList = foodItems.value?.toMutableList() ?: return
        if (currentIndex !in currentList.indices) return

        // Store the current item
        val currentItem = currentList[currentIndex]
        lastLikedItem = currentItem

        // Create a new list excluding the current item
        val remainingItems = currentList.filter { it != currentItem }.toMutableList()
        remainingItems.shuffle()

        // Create the final list with the current item in its original position
        val newList = remainingItems.toMutableList()
        newList.add(currentIndex, currentItem)

        // Update the current index to maintain the same position
        this.currentIndex = currentIndex
        _foodItems.value = newList
    }

    fun getCurrentIndex(): Int {
        return currentIndex
    }

    fun getCurrentItem(): FoodItem? {
        return foodItems.value?.getOrNull(currentIndex)
    }
} 