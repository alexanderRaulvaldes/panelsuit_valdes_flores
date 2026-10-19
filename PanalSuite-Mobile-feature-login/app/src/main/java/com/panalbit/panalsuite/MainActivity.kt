package com.panalbit.panalsuite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panalbit.panalsuite.navigation.Screen
import com.panalbit.panalsuite.ui.login.LoginScreen
import com.panalbit.panalsuite.ui.dashboard.CreateTicketScreen
import com.panalbit.panalsuite.ui.dashboard.DashboardScreen
import com.panalbit.panalsuite.ui.dashboard.TicketDetailScreen
import com.panalbit.panalsuite.ui.theme.PanalsuiteTheme
import com.panalbit.panalsuite.viewmodel.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PanalsuiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sessionVm: SessionViewModel = viewModel()
                    val session by sessionVm.state.collectAsState()
                    var screen by remember { mutableStateOf<Screen>(Screen.Inbox) }
                    val currentUser = session.user

                    when {
                        session.loading -> Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }

                        currentUser == null -> LoginScreen(
                            onLoginSuccess = {
                                screen = Screen.Inbox
                                sessionVm.onLogin(it)
                            }
                        )

                        else -> when (val current = screen) {
                            Screen.Inbox -> DashboardScreen(
                                user = currentUser,
                                onLogout = {
                                    screen = Screen.Inbox
                                    sessionVm.onLogout()
                                },
                                onTicketClick = { screen = Screen.Detail(it) },
                                onCreateTicket = { screen = Screen.Create }
                            )

                            Screen.Create -> CreateTicketScreen(
                                user = currentUser,
                                onBack = { screen = Screen.Inbox }
                            )

                            is Screen.Detail -> TicketDetailScreen(
                                ticketId = current.ticketId,
                                user = currentUser,
                                onBack = { screen = Screen.Inbox }
                            )
                        }
                    }
                }
            }
        }
    }
}