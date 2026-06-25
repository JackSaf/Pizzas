package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jacks.pizzas.R
import com.jacks.pizzas.pizzas.domain.model.PizzaSize

@Composable
fun PizzaSizeSelector(
    modifier: Modifier = Modifier,
    selectedPizzaSize: PizzaSize,
    onSelectPizzaSize: (PizzaSize) -> Unit,
) {
    ConstraintLayout(modifier = modifier) {
        val (sSize, mSize, lSize, banana) = createRefs()
        Image(
            modifier = Modifier.constrainAs(banana){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            painter = painterResource(id = R.drawable.ic_banana_for_scale),
            contentDescription = null
        )
        PizzaSizeSelectorItem(
            modifier = Modifier.constrainAs(sSize){
                top.linkTo(parent.top, 35.dp)
                end.linkTo(mSize.start, 50.dp)
            },
            pizzaSize = PizzaSize.S,
            isSelected = PizzaSize.S == selectedPizzaSize,
            onClick = { onSelectPizzaSize.invoke(PizzaSize.S) }
        )
        PizzaSizeSelectorItem(
            modifier = Modifier.constrainAs(mSize){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, 51.dp)
            },
            pizzaSize = PizzaSize.M,
            isSelected = PizzaSize.M == selectedPizzaSize,
            onClick = { onSelectPizzaSize.invoke(PizzaSize.M) }
        )
        PizzaSizeSelectorItem(
            modifier = Modifier.constrainAs(lSize){
                top.linkTo(parent.top, 35.dp)
                start.linkTo(mSize.end, 50.dp)
            },
            pizzaSize = PizzaSize.L,
            isSelected = PizzaSize.L == selectedPizzaSize,
            onClick = { onSelectPizzaSize.invoke(PizzaSize.L) }
        )
    }
}

@Composable
fun PizzaSizeSelectorItem(
    modifier: Modifier = Modifier,
    pizzaSize: PizzaSize,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        animateColorAsState(if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface)
    val textColor =
        animateColorAsState(if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface)
    val shadowColor = animateColorAsState(if (isSelected) Color.Transparent else Color(0x26000000))
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = shadowColor.value,
                spotColor = shadowColor.value
            )
            .background(color = backgroundColor.value, shape = CircleShape)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.surface, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pizzaSize.name,
            fontSize = 18.sp,
            color = textColor.value
        )
    }
}