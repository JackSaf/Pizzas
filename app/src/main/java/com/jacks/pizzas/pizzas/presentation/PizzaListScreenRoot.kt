package com.jacks.pizzas.pizzas.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R
import com.jacks.pizzas.core.presentation.util.ObserveAsEvents
import com.jacks.pizzas.pizzas.domain.model.Pizza
import com.jacks.pizzas.pizzas.domain.model.PizzaSize
import com.jacks.pizzas.pizzas.presentation.compoments.ErrorDialog
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaDescription
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaHorizontalPager
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaListBottomPanel
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaListHeader
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaLoadingScreen
import com.jacks.pizzas.pizzas.presentation.compoments.PizzaSizeSelector
import com.jacks.pizzas.pizzas.presentation.compoments.curveBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun PizzaListScreenRoot(viewModel: PizzaListViewModel = koinViewModel(), onBackClick: () -> Unit) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PizzaListEvent.AddedToCart -> {
                Toast.makeText(context, R.string.pizza_added_to_cart, Toast.LENGTH_LONG).show()
            }

            is PizzaListEvent.AddedToFavorites -> {
                Toast.makeText(context, R.string.pizza_added_to_favorites, Toast.LENGTH_LONG).show()
            }
        }
    }


    PizzaListScreen(state = state, onAction = { action ->
        when (action) {
            is PizzaListAction.OnBackClick -> onBackClick.invoke()
            else -> viewModel.onAction(action)
        }
    })

    if (state.error != null) {
        ErrorDialog(onDismissRequest = {
            (context as? Activity)?.finish()
        }, onRetry = {
            viewModel.onAction(PizzaListAction.OnRetryClick)
        })
    }

}

@Composable
fun PizzaListScreen(state: PizzaListState, onAction: (PizzaListAction) -> Unit) {
    PizzaLoadingScreen(
        isLoading = state.isLoading,
        onLoadingDisappeared = {
            onAction.invoke(PizzaListAction.OnLoadingDisappeared)
        },
        content = {
            PizzaListContent(
                modifier = Modifier.fillMaxSize(),
                pizzaList = state.pizzas,
                selectedSize = state.selectedSize,
                pizzaQuantity = state.currentQuantity,
                totalPrice = state.totalPrice,
                animationState = state.animationState,
                onAction = onAction
            )
        }
    )
}

@Composable
fun PizzaListContent(
    modifier: Modifier = Modifier,
    pizzaList: List<Pizza>,
    selectedSize: PizzaSize,
    pizzaQuantity: Int,
    totalPrice: Float,
    animationState: PizzaListAnimationState,
    onAction: (PizzaListAction) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {
        pizzaList.size
    })
    val currentPizza = remember(
        pagerState.currentPage,
        pagerState.pageCount
    ) { pizzaList.getOrNull(pagerState.currentPage) }

    var bottomAreaHeightPx by remember { mutableFloatStateOf(0f) }

    val bottomContentTranslationY by animateFloatAsState(
        targetValue = if (animationState is PizzaListAnimationState.NotVisible || animationState is PizzaListAnimationState.ZoomedIn) bottomAreaHeightPx else 0f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = Spring.StiffnessLow
        ),
        label = "bottomContentTranslationY"
    )
    val bottomRowTranslationY by animateFloatAsState(
        targetValue = if (animationState is PizzaListAnimationState.ZoomedIn) bottomAreaHeightPx else 0f
    )

    LaunchedEffect(key1 = currentPizza?.id) {
        currentPizza?.let {
            onAction.invoke(PizzaListAction.OnChangeSelectedPizza(it.id))
        }
    }
    Box(modifier = modifier) {
        Column(modifier = Modifier) {
            PizzaListHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                currentPizzaName = currentPizza?.name ?: "",
                animationState = animationState,
                onBackClick = {
                    onAction.invoke(PizzaListAction.OnBackClick)
                },
                onLikeClick = {
                    currentPizza?.let {
                        onAction.invoke(PizzaListAction.OnAddToFavoritesClick)
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .height(21.dp)
                    .fillMaxWidth()
            )

            PizzaHorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                pagerState = pagerState,
                pizzas = pizzaList,
                selectedSize = selectedSize,
                isZoomedIn = animationState is PizzaListAnimationState.ZoomedIn,
                onZoomIn = {
                    onAction.invoke(PizzaListAction.OnPizzaZoomIn)
                },
                onZoomOut = {
                    onAction.invoke(PizzaListAction.OnPizzaZoomOut)
                },
            )

            Spacer(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .height(28.dp)
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.onSizeChanged {
                bottomAreaHeightPx = it.height.toFloat()
            }) {
                PizzaSizeSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .curveBackground(
                            color = MaterialTheme.colorScheme.background,
                            radius = 300.dp,
                            bottomPadding = 24.dp
                        )
                        .graphicsLayer {
                            translationY = bottomContentTranslationY
                        },
                    selectedPizzaSize = selectedSize,
                    onSelectPizzaSize = { size ->
                        onAction.invoke(PizzaListAction.OnPizzaSizeClick(size))
                    }
                )

                Spacer(modifier = Modifier.height(21.dp))

                PizzaDescription(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 21.dp)
                        .graphicsLayer {
                            translationY = bottomContentTranslationY
                        },
                    pizzaDescription = currentPizza?.description ?: ""
                )

                Spacer(modifier = Modifier.weight(1f))

                PizzaListBottomPanel(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(start = 25.dp, end = 24.dp, bottom = 24.dp)
                        .graphicsLayer {
                            translationY = bottomRowTranslationY
                        },
                    pizzaQuantity = pizzaQuantity,
                    totalPrice = totalPrice,
                    onDecrementPizzaCountClick = { onAction.invoke(PizzaListAction.OnDecrementPizzaCountClick) },
                    onIncrementPizzaCountClick = { onAction.invoke(PizzaListAction.OnIncrementPizzaCountClick) },
                    onAddToCartClick = {
                        onAction.invoke(PizzaListAction.OnAddToCartClick)
                    }
                )
            }
        }
    }
}

