package com.jacks.pizzas.pizzas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacks.pizzas.core.domain.util.DataError
import com.jacks.pizzas.core.domain.util.Result
import com.jacks.pizzas.pizzas.domain.PizzaRepository
import com.jacks.pizzas.pizzas.domain.model.Pizza
import com.jacks.pizzas.pizzas.domain.model.PizzaSize
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PizzaListViewModel(private val pizzaRepository: PizzaRepository) : ViewModel() {

    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(PizzaListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getPizzas()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PizzaListState()
        )

    private val eventChannel = Channel<PizzaListEvent>()
    val events = eventChannel.receiveAsFlow()

    private val selectedPizzaId: MutableStateFlow<String?> = MutableStateFlow(null)

    private fun getPizzas() {
        viewModelScope.launch {
            _state.update {
                it.copy(error = null)
            }
            val result = pizzaRepository.getPizzas()
            handlePizzasResult(result)
        }
    }



    init {
        selectedPizzaId.onEach { resetQuantity() }.launchIn(viewModelScope)
        combine(
            state.map { it.currentQuantity },
            state.map { it.selectedSize },
            state.map { it.pizzas },
            selectedPizzaId
        ) { currentQuantity, selectedSize, pizzas, pizzaId ->
            val currentPizza = pizzas.find { it.id == pizzaId }
            currentPizza?.let {
                calculateTotalPrice(
                    currentQuantity = currentQuantity,
                    selectedSize = selectedSize,
                    currentPizza = currentPizza
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun handlePizzasResult(result: Result<List<Pizza>, DataError>) {
        when (result) {
            is Result.Success -> {
                _state.update {
                    it.copy(isLoading = false, pizzas = result.data)
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(error = result.error)
                }
            }
        }
    }

    fun onAction(action: PizzaListAction) {
        when (action) {
            is PizzaListAction.OnDecrementPizzaCountClick -> {
                decrementPizzaCount()
            }

            is PizzaListAction.OnIncrementPizzaCountClick -> {
                incrementPizzaCount()
            }

            is PizzaListAction.OnAddToFavoritesClick -> {
                addToFavorites()
            }

            is PizzaListAction.OnPizzaSizeClick -> {
                selectPizzaSize(pizzaSize = action.pizzaSize)
            }

            is PizzaListAction.OnAddToCartClick -> {
                addToCart()
            }

            is PizzaListAction.OnRetryClick -> {
                getPizzas()
            }

            is PizzaListAction.OnChangeSelectedPizza -> {
                changeSelectedPizza(action.pizzaId)
            }

            is PizzaListAction.OnPizzaZoomIn -> {
                changeAnimationState(PizzaListAnimationState.ZoomedIn)
            }

            is PizzaListAction.OnPizzaZoomOut -> {
                changeAnimationState(PizzaListAnimationState.Default)
            }

            is PizzaListAction.OnLoadingDisappeared -> {
                changeAnimationState(PizzaListAnimationState.Default)
            }

            else -> Unit
        }
    }

    private fun changeAnimationState(animationState: PizzaListAnimationState) {
        _state.update {
            it.copy(animationState = animationState)
        }
    }

    private fun selectPizzaSize(pizzaSize: PizzaSize) {
        _state.update {
            it.copy(selectedSize = pizzaSize)
        }
    }

    private fun changeSelectedPizza(pizzaId: String) {
        selectedPizzaId.value = pizzaId
    }

    private fun incrementPizzaCount() {
        _state.update {
            it.copy(currentQuantity = (it.currentQuantity + 1).coerceAtMost(20))
        }
    }

    private fun decrementPizzaCount() {
        _state.update {
            it.copy(currentQuantity = (it.currentQuantity - 1).coerceAtLeast(1))
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            val currentPizza = _state.value.pizzas.find { it.id == selectedPizzaId.value}
            currentPizza?.let {
                eventChannel.send(PizzaListEvent.AddedToFavorites)
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            resetQuantity()
            eventChannel.send(PizzaListEvent.AddedToCart)
        }
    }

    private fun calculateTotalPrice(
        currentQuantity: Int,
        selectedSize: PizzaSize,
        currentPizza: Pizza
    ) {
        val price = currentPizza.variants.find { variant -> variant.size == selectedSize }?.price
        price?.let {
            _state.update {
                it.copy(totalPrice = currentQuantity * price)
            }
        }
    }

    private fun resetQuantity() {
        _state.update { it.copy(currentQuantity = 1) }
    }
}