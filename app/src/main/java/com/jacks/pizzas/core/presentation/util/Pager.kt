package com.jacks.pizzas.core.presentation.util

import androidx.compose.foundation.pager.PagerState

fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}