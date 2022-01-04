package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.common.mvs.State

data class HomeScreenState(
    val page: Int = -1,
    val loading: Boolean = false,
    val items: List<Item> = emptyList(),
    val articleIds: Set<Int> = emptySet(),
    val hasMore: Boolean = true,
) : State
