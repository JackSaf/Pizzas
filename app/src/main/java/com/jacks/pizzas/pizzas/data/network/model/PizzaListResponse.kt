package com.jacks.pizzas.pizzas.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PizzaListResponse(
    @SerialName("pizzas")
    val pizzas: List<PizzaDto>
)