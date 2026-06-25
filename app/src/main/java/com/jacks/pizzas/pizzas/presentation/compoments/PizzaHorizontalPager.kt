package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jacks.pizzas.core.presentation.util.calculateCurrentOffsetForPage
import com.jacks.pizzas.pizzas.domain.model.Pizza
import com.jacks.pizzas.pizzas.domain.model.PizzaSize
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun PizzaHorizontalPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    selectedSize: PizzaSize,
    pizzas: List<Pizza>,
    isZoomedIn: Boolean,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit
) {
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val density = LocalDensity.current
    val screenWidthDp = with(density) { screenWidth.toDp() }
    val coroutineScope = rememberCoroutineScope()
    val itemSize = 274.dp
    var pagerTopRect by remember { mutableStateOf(0f) }
    var showPagerItem by remember { mutableStateOf(true) }.apply { if(isZoomedIn) value = false }
    val pageAlpha = animateFloatAsState(if(showPagerItem) 1f else 0f, animationSpec = tween(50))
    val pizzaScales = remember { mutableStateMapOf(
        PizzaSize.S to 0.715f,
        PizzaSize.M to 0.89f,
        PizzaSize.L to 1f
    ) }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemSize)
                .onGloballyPositioned {
                    pagerTopRect = it.positionInRoot().y
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        if (zoom > 1f) {
                            onZoomIn.invoke()
                        }
                    }
                }
            ,
            beyondViewportPageCount = 1,
            key = { pizzas.getOrNull(it)?.id ?: "" },
            pageSize = PageSize.Fixed(itemSize),
            contentPadding = PaddingValues(horizontal = (screenWidthDp - itemSize) / 2),
            pageSpacing = 12.dp,
            snapPosition = SnapPosition.Center,
            state = pagerState
        ) { page ->
            val item = pizzas.getOrNull(page)
            item?.let {
                PizzaPagerItem(
                    modifier = Modifier.graphicsLayer {
                        alpha = if(pagerState.currentPage == page) pageAlpha.value else 1f
                    },
                    pageOffset = pagerState.calculateCurrentOffsetForPage(page),
                    imageUrl = item.imageUrl,
                    selectedSize = selectedSize,
                    pizzaSizeScales = pizzaScales,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                )
            }
        }
        ZoomButton(modifier = Modifier, onClick = {
            onZoomIn.invoke()
        })

        ZoomedPizzaOverlay(
            pagerPositionY = pagerTopRect,
            defaultItemSize = itemSize,
            currentItemSize = itemSize * pizzaScales.getValue(selectedSize),
            isZoomedIn = isZoomedIn,
            imageUrl = pizzas.getOrNull(pagerState.currentPage)?.imageUrl ?: "",
            onZoomOut = {
                onZoomOut.invoke()
            },
            onShowItemBack = {
                showPagerItem = true
            }
        )
    }

}

@Composable
fun PizzaPagerItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    selectedSize: PizzaSize,
    pageOffset: Float,
    pizzaSizeScales: Map<PizzaSize, Float>,
    onClick: () -> Unit
) {
    val openedScale = animateFloatAsState(
        pizzaSizeScales.getValue(selectedSize),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val closedScale = 0.29f
    AsyncImage(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            )
            .graphicsLayer {
                val scale = (1 - pageOffset.absoluteValue).coerceIn(closedScale, openedScale.value)
                val actualSize = this.size * scale
                translationX = (size.width - actualSize.width) / 2f * pageOffset
                scaleX = scale
                scaleY = scale
            },
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}