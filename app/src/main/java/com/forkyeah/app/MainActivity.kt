package com.forkyeah.app

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.forkyeah.app.databinding.ActivityMainBinding
import com.forkyeah.app.model.FoodItem
import com.forkyeah.app.viewmodel.FoodViewModel
import android.view.MotionEvent
import android.view.GestureDetector
import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FoodViewModel
    private lateinit var gestureDetector: GestureDetector
    private var currentIndex = 0
    private var foodList = listOf<FoodItem>()
    private var isItemLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        setupGestureDetector()
        setupNewSearchButton()
        setupQuestionMarkButton()
        observeFoodItems()
        
        // Show initial tooltip
        showTooltip("Swipe right to like, left to skip")
    }

    private fun showTooltip(message: String) {
        val tooltip = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        tooltip.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
        tooltip.show()
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                        if (diffX > 0) {
                            // Swipe right - like
                            nextCard(true)
                            showTooltip("Liked! Click 'New Search' to find more")
                        } else {
                            // Swipe left - next
                            nextCard(false)
                        }
                        return true
                    }
                }
                return false
            }
        })

        binding.foodCard.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun showFoodCard(index: Int) {
        if (index in foodList.indices) {
            val food = foodList[index]
            binding.foodName.text = food.name
            binding.foodDescription.text = food.description
            
            // Show recipe URL if the item is liked
            if (isItemLiked) {
                food.recipeUrl?.let { url ->
                    binding.recipeUrl.apply {
                        text = "Recipe: $url"
                        visibility = View.VISIBLE
                        setOnClickListener {
                            // Open the recipe URL in a browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                }
            } else {
                binding.recipeUrl.visibility = View.GONE
            }

            Glide.with(this)
                .load(food.imageUrl)
                .centerCrop()
                .into(binding.foodImage)
        }
    }

    private fun setupNewSearchButton() {
        binding.newSearchButton.setOnClickListener {
            viewModel.loadNewSearch()
            isItemLiked = false
            binding.newSearchButton.visibility = View.GONE
            binding.persistentHeart.alpha = 0f
            binding.persistentHeart.visibility = View.GONE
            binding.heartOverlay.visibility = View.GONE
            binding.recipeUrl.visibility = View.GONE
            // Move to next item when starting new search
            currentIndex++
            if (currentIndex >= foodList.size) {
                currentIndex = 0
            }
            showFoodCard(currentIndex)
            showTooltip("Swipe right to like, left to skip")
        }
    }

    private fun setupQuestionMarkButton() {
        binding.questionMarkButton.setOnClickListener {
            showTooltip("Swipe right to like, left to skip")
        }
    }

    private fun animateHeartOverlay() {
        // Only show heart animation if it's not already visible
        if (binding.persistentHeart.alpha == 0f) {
            // Reset heart overlay state
            binding.heartOverlay.alpha = 0f
            binding.heartOverlay.scaleX = 0.5f
            binding.heartOverlay.scaleY = 0.5f
            binding.heartOverlay.visibility = View.VISIBLE

            // Create scale animation
            val scaleX = android.animation.ObjectAnimator.ofFloat(binding.heartOverlay, View.SCALE_X, 0.5f, 1.2f, 1f)
            val scaleY = android.animation.ObjectAnimator.ofFloat(binding.heartOverlay, View.SCALE_Y, 0.5f, 1.2f, 1f)
            val fadeIn = android.animation.ObjectAnimator.ofFloat(binding.heartOverlay, View.ALPHA, 0f, 1f)
            
            // Set animation durations
            scaleX.duration = 300
            scaleY.duration = 300
            fadeIn.duration = 200

            // Create fade out animation
            val fadeOut = android.animation.ObjectAnimator.ofFloat(binding.heartOverlay, View.ALPHA, 1f, 0f)
            fadeOut.duration = 200
            fadeOut.startDelay = 300

            // Start animations
            android.animation.AnimatorSet().apply {
                playTogether(scaleX, scaleY, fadeIn)
                start()
            }

            // Show persistent heart after animation
            fadeIn.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    fadeOut.start()
                    binding.persistentHeart.alpha = 1f
                    binding.persistentHeart.scaleX = 1f
                    binding.persistentHeart.scaleY = 1f
                    binding.persistentHeart.visibility = View.VISIBLE
                    // Show new search button after liking
                    binding.newSearchButton.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun nextCard(liked: Boolean = false) {
        if (foodList.isEmpty()) {
            Log.d("MainActivity", "Cannot show next card: food list is empty")
            return
        }
        if (liked) {
            // Only animate if not already liked
            if (!isItemLiked) {
                Log.d("MainActivity", "Liking current card")
                animateHeartOverlay()
                isItemLiked = true
            }
            // Don't increment index or show next card when liking
        } else {
            if (isItemLiked) {
                Log.d("MainActivity", "Cannot swipe left: item is liked")
                showTooltip("Click 'New Search' to find more")
                return
            }
            // Only increment and show next card when not liking
            currentIndex++
            if (currentIndex >= foodList.size) {
                currentIndex = 0
            }
            Log.d("MainActivity", "Moving to next card at index $currentIndex")
            showFoodCard(currentIndex)
            binding.persistentHeart.alpha = 0f
        }
    }

    private fun observeFoodItems() {
        viewModel.foodItems.observe(this) { foodItems ->
            Log.d("MainActivity", "Received ${foodItems.size} food items")
            foodList = foodItems
            if (foodItems.isNotEmpty()) {
                currentIndex = 0
                showFoodCard(currentIndex)
            }
        }
    }
} 