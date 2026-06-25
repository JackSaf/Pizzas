package com.jacks.pizzas.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacks.pizzas.R


@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor =  Color(0x26000000),
                spotColor =  Color(0x26000000)
            )
            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape),
        contentAlignment = Alignment.Center
    ){
        Icon(painter = painterResource(id = iconId), contentDescription = contentDescription, tint = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun BackIconButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppIconButton(modifier = modifier, iconId = R.drawable.ic_back, onClick = onClick)
}