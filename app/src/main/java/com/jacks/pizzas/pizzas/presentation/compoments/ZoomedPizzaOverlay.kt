package com.jacks.pizzas.pizzas.presentation.compoments

import android.view.WindowManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun ZoomedPizzaOverlay(
    pagerPositionY: Float,
    currentItemSize: Dp,
    defaultItemSize: Dp,
    isZoomedIn: Boolean,
    imageUrl: String,
    onZoomOut: () -> Unit,
    onShowItemBack: () -> Unit
) {
    val dialogShown = remember { mutableStateOf(isZoomedIn) }.apply {
        if (isZoomedIn) value = true
    }
    val screenCenterOffset = LocalWindowInfo.current.containerSize.center.y
    val density = LocalDensity.current
    val itemSizePx = remember(currentItemSize) { with(density) { currentItemSize.toPx() } }
    val defaultItemSizePx = remember(defaultItemSize) { with(density) { defaultItemSize.toPx() } }
    val positionY = remember(pagerPositionY, itemSizePx) {
        Animatable(pagerPositionY + defaultItemSizePx - itemSizePx)
    }
    val alpha = remember {
        Animatable(0f)
    }
    val scale = remember {
        Animatable(1f)
    }
    val springSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )

    LaunchedEffect(key1 = isZoomedIn) {
        if (isZoomedIn) {
            launch {
                alpha.animateTo(1f, animationSpec = tween(100))
            }
            launch {
                positionY.animateTo(
                    screenCenterOffset.toFloat() - itemSizePx / 2,
                    springSpec
                )
            }
            launch {
                scale.animateTo(5f, animationSpec = springSpec)
            }
        } else {
            val positionJob = launch {
                positionY.animateTo(
                    pagerPositionY + (defaultItemSizePx - itemSizePx) / 2f,
                    animationSpec = springSpec
                )
            }
            val scaleJob = launch {
                scale.animateTo(1f, animationSpec = springSpec)
            }
            joinAll(positionJob, scaleJob)
            onShowItemBack.invoke()
            alpha.animateTo(0f, animationSpec = tween(300))
            dialogShown.value = false
        }
    }

    OverlayDialog(
        show = dialogShown.value,
        onDismissRequest = onZoomOut,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if(dialogShown.value) Modifier.pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        if (zoom < 1f) onZoomOut.invoke()
                    }
                } else Modifier),
            contentAlignment = Alignment.TopCenter
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(currentItemSize)
                    .graphicsLayer {
                        this.alpha = alpha.value
                        translationY = positionY.value
                        scaleX = scale.value
                        scaleY = scale.value
                    },
                model = imageUrl,
                contentDescription = null
            )
        }
    }

}


@Composable
private fun OverlayDialog(show: Boolean, onDismissRequest: () -> Unit, content: @Composable () -> Unit){
    if(show){
        Dialog(
            onDismissRequest = {
                onDismissRequest.invoke()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            val view = LocalView.current
            val window = (view.parent as? DialogWindowProvider)?.window

            DisposableEffect(window) {
                val originalFlags = window?.attributes?.flags ?: 0
                val originalStyle = window?.attributes?.windowAnimations ?: 0
                window?.let {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    window.setWindowAnimations(0)
                }

                onDispose {
                    window?.setFlags(
                        originalFlags,
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND
                    )
                    window?.setWindowAnimations(originalStyle)
                }
            }

            content.invoke()
        }
    }

}