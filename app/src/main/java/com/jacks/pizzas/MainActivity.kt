package com.jacks.pizzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jacks.pizzas.core.presentation.designsystem.PizzasTheme
import com.jacks.pizzas.pizzas.presentation.PizzaListScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzasTheme {
                PizzaListScreenRoot(onBackClick = {})
            }
        }
    }
}
