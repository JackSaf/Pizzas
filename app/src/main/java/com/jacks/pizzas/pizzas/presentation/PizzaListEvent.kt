package com.jacks.pizzas.pizzas.presentation

sealed class PizzaListEvent {
    data object AddedToCart: PizzaListEvent()
    data object AddedToFavorites: PizzaListEvent()
}