package com.jacks.pizzas.pizzas.presentation.compoments

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.curveBackground(
	color: Color,
	radius: Dp = 300.dp,
	bottomPadding: Dp = 50.dp
): Modifier {
	return this.drawBehind {
		val radiusPx = radius.toPx()
		val bottomPaddingPx = bottomPadding.toPx()

		val cy = size.height - radiusPx - bottomPaddingPx
		val cx = size.width / 2f

		val ovalRect = Rect(
			left = cx - radiusPx,
			top = cy - radiusPx,
			right = cx + radiusPx,
			bottom = cy + radiusPx
		)

		val path = Path().apply {
			moveTo(0f, 0f)
			lineTo(size.width, 0f)
			lineTo(size.width, size.height)
			arcTo(
				rect = ovalRect,
				startAngleDegrees = 0f,
				sweepAngleDegrees = 180f,
				forceMoveTo = false
			)
			lineTo(0f, 0f)
			close()
		}

		drawPath(path = path, color = color, style = Fill)
	}
}
