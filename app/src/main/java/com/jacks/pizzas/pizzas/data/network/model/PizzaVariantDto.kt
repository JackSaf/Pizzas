package com.jacks.pizzas.pizzas.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PizzaVariantDto(
    val size: String,
    val price: Float
)