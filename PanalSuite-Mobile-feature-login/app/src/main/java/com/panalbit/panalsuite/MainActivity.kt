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
import com.panalbit.panalsuite.model.Usuario
import com.panalbit.panalsuite.ui.LoginScreen
import com.panalbit.panalsuite.ui.dashboard.DashboardScreen
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
                    var usuario by remember { mutableStateOf<Usuario?>(null) }
                    val u = usuario

                    if (u == null) {
                        LoginScreen(onLoginExitoso = { usuario = it })
                    } else {
                        DashboardScreen(
                            usuario = u,
                            onCerrarSesion = { usuario = null }
                        )
                    }
                }
            }
        }
    }
}