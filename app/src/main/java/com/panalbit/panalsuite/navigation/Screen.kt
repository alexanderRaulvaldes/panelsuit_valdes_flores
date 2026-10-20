package com.panalbit.panalsuite.navigation

sealed interface Screen {
    data object Inbox : Screen
    data object Create : Screen
    data class Detail(val ticketId: String) : Screen
}