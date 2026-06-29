package com.jacks.pizzas.pizzas.data.network

import com.jacks.pizzas.pizzas.data.network.model.PizzaListResponse
import retrofit2.Response
import retrofit2.http.GET

interface PizzaApi {
    @GET("/api/pizzas")
    suspend fun getPizzas(): Response<PizzaListResponse>
}

