package com.jacks.pizzas.core.presentation.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable RowScope.() -> Unit){
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.surface, containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(100.dp),
        onClick = onClick,
        content = content
    )
}