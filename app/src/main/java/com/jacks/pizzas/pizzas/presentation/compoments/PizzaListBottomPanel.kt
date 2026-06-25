package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R

@Composable
fun PizzaListBottomPanel(
    modifier: Modifier = Modifier,
    pizzaQuantity: Int,
    totalPrice: Float,
    onIncrementPizzaCountClick: () -> Unit,
    onDecrementPizzaCountClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PizzaQuantityChanger(
            modifier = Modifier,
            currentQuantity = pizzaQuantity,
            onIncrementClick = onIncrementPizzaCountClick,
            onDecrementClick = onDecrementPizzaCountClick
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.widthIn(min = 44.dp),
            text = stringResource(id = R.string.price_format, totalPrice),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        AddButton(
            modifier = Modifier,
            onClick = onAddToCartClick
        )
    }
}