package com.panalbit.panalsuite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.data.repository.AuthRepository
import com.panalbit.panalsuite.data.repository.SessionRepository
import com.panalbit.panalsuite.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionUiState(
    val loading: Boolean = true,
    val user: User? = null
)

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepository = SessionRepository(application)
    private val authRepository = AuthRepository()

    private val _state = MutableStateFlow(SessionUiState())
    val state: StateFlow<SessionUiState> = _state

    init {
        // si se inicia, restaura el usuario guardado en la sesión
        viewModelScope.launch {
            val savedId = sessionRepository.getSavedUserId()
            val user = savedId?.let { authRepository.findById(it) }
            _state.update { SessionUiState(loading = false, user = user) }
        }
    }

    fun onLogin(user: User) {
        _state.update { it.copy(user = user) }
        viewModelScope.launch { sessionRepository.saveUserId(user.id) }
    }

    fun onLogout() {
        _state.update { it.copy(user = null) }
        viewModelScope.launch { sessionRepository.clear() }
    }
}
