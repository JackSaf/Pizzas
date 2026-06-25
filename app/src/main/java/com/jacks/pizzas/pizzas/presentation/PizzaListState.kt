package com.jacks.pizzas.pizzas.presentation

import androidx.compose.runtime.Stable
import com.jacks.pizzas.core.domain.util.Error
import com.jacks.pizzas.pizzas.domain.model.Pizza
import com.jacks.pizzas.pizzas.domain.model.PizzaSize

@Stable
data class PizzaListState(
    val isLoading: Boolean = true,
    val pizzas: List<Pizza> = listOf(),
    val selectedSize: PizzaSize = PizzaSize.L,
    val currentQuantity: Int = 1,
    val totalPrice: Float = 0f,
    val animationState: PizzaListAnimationState = PizzaListAnimationState.NotVisible,
    val error: Error? = null
)