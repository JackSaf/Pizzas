package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PizzaDescription(modifier: Modifier = Modifier, pizzaDescription: String){
    AnimatedContent(modifier = modifier, targetState = pizzaDescription) {
        Text(
            modifier = Modifier,
            text = it,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}