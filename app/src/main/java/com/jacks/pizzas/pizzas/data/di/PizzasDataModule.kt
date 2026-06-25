package com.jacks.pizzas.pizzas.data.di

import com.jacks.pizzas.pizzas.data.repository.RemotePizzaRepository
import com.jacks.pizzas.pizzas.domain.PizzaRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val pizzasDataModule = module {
    singleOf(::RemotePizzaRepository).bind<PizzaRepository>()
}