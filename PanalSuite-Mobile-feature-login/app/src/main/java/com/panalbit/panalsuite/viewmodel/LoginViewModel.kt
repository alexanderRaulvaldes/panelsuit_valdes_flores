package com.panalbit.panalsuite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.model.Usuario
import com.panalbit.panalsuite.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val usuario: Usuario? = null
)

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEmailChange(valor: String) {
        _state.update { it.copy(email = valor, error = null) }
    }

    fun onPasswordChange(valor: String) {
        _state.update { it.copy(password = valor, error = null) }
    }

    fun login() {
        val actual = _state.value
        if (actual.email.isBlank() || actual.password.isBlank()) {
            _state.update { it.copy(error = "Completa todos los campos") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            val usuario = repository.login(actual.email, actual.password)
            if (usuario != null) {
                _state.update { it.copy(cargando = false, usuario = usuario) }
            } else {
                _state.update { it.copy(cargando = false, error = "Correo o contraseña incorrectos") }
            }
        }
    }


    fun limpiarUsuario() {
        _state.update { it.copy(usuario = null, password = "") }
    }
}