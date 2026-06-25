package com.jacks.pizzas.pizzas.domain

import com.jacks.pizzas.core.domain.util.DataError
import com.jacks.pizzas.core.domain.util.Result
import com.jacks.pizzas.pizzas.domain.model.Pizza

interface PizzaRepository {

    suspend fun getPizzas(): Result<List<Pizza>, DataError.Network>
}