package com.jacks.pizzas.pizzas.domain.model

data class Pizza(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val variants: List<PizzaVariant>
)