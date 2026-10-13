package com.panalbit.panalsuite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.panalbit.panalsuite.model.Usuario
import com.panalbit.panalsuite.ui.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var usuario by remember { mutableStateOf<Usuario?>(null) }
                    val u = usuario

                    if (u == null) {
                        LoginScreen(onLoginExitoso = { usuario = it })
                    } else {
                        Column(Modifier.padding(24.dp)) {
                            Text("Bienvenido, ${u.nombre}")
                            Text("Rol: ${u.rol}")
                        }
                    }
                }
            }
        }
    }
}