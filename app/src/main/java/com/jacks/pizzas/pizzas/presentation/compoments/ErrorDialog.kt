package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jacks.pizzas.R
import com.jacks.pizzas.core.presentation.designsystem.components.PrimaryButton

@Composable
fun ErrorDialog(onDismissRequest: () -> Unit, onRetry: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.error_text_something_went_wrong),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(onClick = onRetry) {
                Text(
                    text = stringResource(id = R.string.button_text_retry),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}