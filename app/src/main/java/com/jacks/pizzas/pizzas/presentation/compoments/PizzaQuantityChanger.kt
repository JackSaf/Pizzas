package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R
import com.jacks.pizzas.core.presentation.designsystem.components.AppIconButton

@Composable
fun PizzaQuantityChanger(
    modifier: Modifier = Modifier,
    currentQuantity: Int,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit
) {
    Row(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(100.dp)
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        DecrementQuantityButton(onClick = onDecrementClick)
        AnimatedContent(targetState = currentQuantity) {
            Text(
                modifier = Modifier.defaultMinSize(minWidth = 48.dp),
                text = it.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        IncrementQuantityButton(onClick = onIncrementClick)
    }
}

@Composable
private fun IncrementQuantityButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppIconButton(modifier = modifier, iconId = R.drawable.ic_increment, onClick = onClick)
}

@Composable
private fun DecrementQuantityButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppIconButton(modifier = modifier, iconId = R.drawable.ic_decrement, onClick = onClick)
}