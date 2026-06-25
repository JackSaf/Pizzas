package com.jacks.pizzas.pizzas.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PizzaDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("variants")
    val variants: List<PizzaVariantDto>
)
