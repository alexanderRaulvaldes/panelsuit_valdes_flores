package com.panalbit.panalsuite.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.data.repository.TicketRepository
import com.panalbit.panalsuite.model.Rol
import com.panalbit.panalsuite.model.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val cargando: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val error: String? = null
)

class DashboardViewModel : ViewModel() {

    private val repository = TicketRepository()

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state

    fun loadTickets(rol: Rol) {
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            try {
                val lista = repository.getInbox(rol)
                _state.update { it.copy(cargando = false, tickets = lista) }
            } catch (e: Exception) {
                _state.update { it.copy(cargando = false, error = "No se pudieron cargar los tickets") }
            }
        }
    }
}