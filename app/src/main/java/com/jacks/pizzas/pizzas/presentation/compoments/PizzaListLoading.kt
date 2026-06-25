package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R
import kotlinx.coroutines.delay

@Composable
fun PizzaLoadingScreen(
    isLoading: Boolean,
    onLoadingDisappeared: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        content()
        PizzaLoadingOverlay(
            modifier = Modifier.fillMaxSize(),
            isLoading = isLoading,
            onLoadingDisappeared = onLoadingDisappeared
        )
    }
}

@Composable
fun PizzaLoadingOverlay(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    frameDelayMillis: Long = 100L,
    onLoadingDisappeared: () -> Unit
) {
    val frames = remember {
        intArrayOf(
            R.drawable.pizza_loading_1,
            R.drawable.pizza_loading_2,
            R.drawable.pizza_loading_3,
            R.drawable.pizza_loading_4,
            R.drawable.pizza_loading_5,
            R.drawable.pizza_loading_6,
            R.drawable.pizza_loading_7,
            R.drawable.pizza_loading_8
        )
    }
    var didNotify by rememberSaveable { mutableStateOf(false) }
    var currentFrameIndex by rememberSaveable { mutableIntStateOf(0) }
    var loadingAnimationFinished by rememberSaveable { mutableStateOf(false) }
    val loadingDisappearProgress by animateFloatAsState(targetValue = if(loadingAnimationFinished) 0f else 1f, animationSpec = tween(500))
    LaunchedEffect(currentFrameIndex) {
        if (currentFrameIndex == frames.size - 1) {
            delay(frameDelayMillis * 2)
            if (!isLoading) {
                loadingAnimationFinished = true
            } else {
                currentFrameIndex = 0
            }
        } else {
            delay(frameDelayMillis)
            currentFrameIndex++
        }
    }

    LaunchedEffect(key1 = loadingDisappearProgress) {
        if(!didNotify){
            if(loadingDisappearProgress < 0.5f) {
                onLoadingDisappeared.invoke()
                didNotify = true
            }
        }
    }
    Box(
        modifier = modifier
            .alpha(loadingDisappearProgress)
            .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(270.dp).scale(loadingDisappearProgress),
            painter = painterResource(id = frames[currentFrameIndex]),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}