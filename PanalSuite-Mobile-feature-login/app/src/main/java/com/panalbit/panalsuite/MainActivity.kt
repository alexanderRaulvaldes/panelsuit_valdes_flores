package com.panalbit.panalsuite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.panalbit.panalsuite.model.User
import com.panalbit.panalsuite.ui.LoginScreen
import com.panalbit.panalsuite.ui.dashboard.DashboardScreen
import com.panalbit.panalsuite.ui.dashboard.TicketDetailScreen
import com.panalbit.panalsuite.ui.theme.PanalsuiteTheme

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
                    var user by remember { mutableStateOf<User?>(null) }
                    var ticketId by remember { mutableStateOf<String?>(null) }
                    val currentUser = user
                    val currentTicketId = ticketId

                    if (currentUser == null) {
                        LoginScreen(onLoginSuccess = { user = it })
                    } else if (currentTicketId == null) {
                        DashboardScreen(
                            user = currentUser,
                            onLogout = { user = null },
                            onTicketClick = { ticketId = it }
                        )
                    } else {
                        TicketDetailScreen(
                            ticketId = currentTicketId,
                            user = currentUser,
                            onBack = { ticketId = null }
                        )
                    }
                }
            }
        }
    }
}