package com.jacks.pizzas.pizzas.data.mapper

import com.jacks.pizzas.pizzas.data.remote.model.PizzaDto
import com.jacks.pizzas.pizzas.data.remote.model.PizzaVariantDto
import com.jacks.pizzas.pizzas.domain.model.Pizza
import com.jacks.pizzas.pizzas.domain.model.PizzaSize
import com.jacks.pizzas.pizzas.domain.model.PizzaVariant

fun PizzaDto.toDomain() = Pizza(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    variants = variants.map { it.toDomain() }
)

fun PizzaVariantDto.toDomain() = PizzaVariant(
    size = try {
        PizzaSize.valueOf(size)
    } catch (e: Exception) {
        PizzaSize.M },
    price = price
)