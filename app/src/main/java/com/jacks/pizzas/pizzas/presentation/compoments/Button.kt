package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jacks.pizzas.R
import com.jacks.pizzas.core.presentation.designsystem.components.AppIconButton
import com.jacks.pizzas.core.presentation.designsystem.components.PrimaryButton

@Composable
fun FavoriteButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppIconButton(modifier = modifier, iconId = R.drawable.ic_favorite, onClick = onClick)
}

@Composable
fun ZoomButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Image(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        painter = painterResource(id = R.drawable.ic_zoom),
        contentDescription = null
    )
}

@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    PrimaryButton(modifier = modifier, onClick = onClick) {
        Text(
            text = stringResource(id = R.string.button_text_add),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.surface
        )
    }
}