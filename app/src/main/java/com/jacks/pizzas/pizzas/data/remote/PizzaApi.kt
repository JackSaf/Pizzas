package com.jacks.pizzas.pizzas.data.remote

import com.jacks.pizzas.pizzas.data.remote.model.PizzaListResponse
import retrofit2.Response
import retrofit2.http.GET

interface PizzaApi {
    @GET("/api/pizzas")
    suspend fun getPizzas(): Response<PizzaListResponse>
}

