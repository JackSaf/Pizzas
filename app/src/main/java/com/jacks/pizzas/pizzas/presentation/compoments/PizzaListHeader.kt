package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R
import com.jacks.pizzas.core.presentation.designsystem.components.BackIconButton
import com.jacks.pizzas.pizzas.presentation.PizzaListAnimationState

@Composable
fun PizzaListHeader(
    modifier: Modifier = Modifier,
    currentPizzaName: String,
    animationState: PizzaListAnimationState,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    var iconWidth by remember { mutableFloatStateOf(0f) }
    var rowHeight by remember { mutableFloatStateOf(0f) }
    val animationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
    val globalTranslationY by animateFloatAsState(
        if (animationState is PizzaListAnimationState.ZoomedIn) -rowHeight else 0f,
        animationSpec = animationSpec
    )
    val leftIconTranslationX by animateFloatAsState(
        if (animationState is PizzaListAnimationState.NotVisible) -iconWidth else 0f,
        animationSpec = animationSpec
    )
    val rightIconTranslationX by animateFloatAsState(
        if (animationState is PizzaListAnimationState.NotVisible) iconWidth else 0f,
        animationSpec = animationSpec
    )
    val textTranslationY by animateFloatAsState(
        if (animationState is PizzaListAnimationState.NotVisible) -rowHeight else 0f,
        animationSpec = animationSpec
    )
    Row(modifier = modifier
        .onSizeChanged {
            rowHeight = it.height.toFloat()
        }
        .statusBarsPadding()
        .graphicsLayer {
            translationY = globalTranslationY
        }) {
        BackIconButton(
            modifier = Modifier
                .onSizeChanged {
                    iconWidth = it.width.toFloat()
                }
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .graphicsLayer {
                    translationX = leftIconTranslationX
                },
            onClick = onBackClick
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .padding(top = 14.dp)
                .graphicsLayer {
                    translationY = textTranslationY
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.title_pizzas),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AnimatedContent(currentPizzaName) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        FavoriteButton(
            modifier = Modifier
                .padding(end = 24.dp, top = 16.dp, bottom = 16.dp)
                .graphicsLayer {
                    translationX = rightIconTranslationX
                },
            onClick = onLikeClick
        )
    }
}