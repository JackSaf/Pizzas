package com.jacks.pizzas.pizzas.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PizzaVariantDto(
    val size: String,
    val price: Float
)