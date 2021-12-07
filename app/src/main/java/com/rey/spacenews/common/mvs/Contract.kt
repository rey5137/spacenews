package com.rey.spacenews.common.mvs

interface Command

interface Event

interface State

interface Reducer<in E : Event, S : State> {

    fun reduce(oldState: S, event: E): S

}
