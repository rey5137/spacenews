package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.common.mvs.Command

sealed interface HomeScreenCommand : Command

object LoadMoreCommand : HomeScreenCommand {
    override fun toString() = "LoadMoreCommand"
}