package com.jacks.pizzas.pizzas.presentation

sealed class PizzaListAnimationState {
    data object NotVisible: PizzaListAnimationState()
    data object Default: PizzaListAnimationState()
    data object ZoomedIn: PizzaListAnimationState()
}