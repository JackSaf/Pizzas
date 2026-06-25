package com.jacks.pizzas.pizzas.presentation

import com.jacks.pizzas.pizzas.domain.model.PizzaSize

sealed class PizzaListAction {
    data object OnAddToFavoritesClick: PizzaListAction()
    data class OnPizzaSizeClick(val pizzaSize: PizzaSize): PizzaListAction()
    data object OnPizzaZoomIn: PizzaListAction()
    data object OnPizzaZoomOut: PizzaListAction()
    data object OnLoadingDisappeared: PizzaListAction()
    data object OnIncrementPizzaCountClick: PizzaListAction()
    data object OnDecrementPizzaCountClick: PizzaListAction()
    data object OnAddToCartClick: PizzaListAction()

    data object OnRetryClick: PizzaListAction()

    data class OnChangeSelectedPizza(val pizzaId: String): PizzaListAction()
    data object OnBackClick: PizzaListAction()
}