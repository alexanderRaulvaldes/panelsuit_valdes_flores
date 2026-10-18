package com.panalbit.panalsuite.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.data.repository.TicketRepository
import com.panalbit.panalsuite.model.Role
import com.panalbit.panalsuite.model.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val loading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val error: String? = null
)

class DashboardViewModel : ViewModel() {

    private val repository = TicketRepository()

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state

    // Remembers which role the tickets were already loaded for
    private var loadedRole: Role? = null

    fun loadTickets(role: Role) {
        if (loadedRole == role) return
        loadedRole = role
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val list = repository.getInbox(role)
                _state.update { it.copy(loading = false, tickets = list) }
            } catch (e: Exception) {
                loadedRole = null
                _state.update { it.copy(loading = false, error = "No se pudieron cargar los tickets") }
            }
        }
    }

    fun changeState(id: String, newState: String) {
        _state.update { s ->
            s.copy(tickets = s.tickets.map {
                if (it.idRequest == id) it.copy(state = newState) else it
            })
        }
    }

    fun addComment(id: String, text: String) {
        if (text.isBlank()) return
        _state.update { s ->
            s.copy(tickets = s.tickets.map {
                if (it.idRequest == id) it.copy(comments = it.comments + text.trim()) else it
            })
        }
    }
}