package com.jacks.pizzas.pizzas.presentation.di

import com.jacks.pizzas.pizzas.presentation.PizzaListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val pizzaListViewModelModule = module {
    viewModelOf(::PizzaListViewModel)
}