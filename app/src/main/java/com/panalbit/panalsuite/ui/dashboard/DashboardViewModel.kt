package com.panalbit.panalsuite.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.data.repository.TicketRepository
import com.panalbit.panalsuite.model.*
import com.panalbit.panalsuite.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val loading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val error: String? = null,
    val query: String = "",
    val stateFilter: String? = null
) {

    val visibleTickets: List<Ticket>
        get() = tickets.filter { ticket ->
            val q = query.trim()
            val matchesState = stateFilter == null || ticket.state == stateFilter
            val matchesQuery = q.isEmpty() ||
                    ticket.idRequest.contains(q, ignoreCase = true) ||
                    ticket.flowType.contains(q, ignoreCase = true) ||
                    ticket.referenceLocation.contains(q, ignoreCase = true)
            matchesState && matchesQuery
        }
}

class DashboardViewModel : ViewModel() {

    private val repository = TicketRepository()

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state

    private var loadedUserId: Int? = null

    val resolvers: List<String> get() = repository.resolvers

    fun loadTickets(user: User) {
        if (loadedUserId == user.id) return
        loadedUserId = user.id
        viewModelScope.launch {
            _state.update { DashboardUiState(loading = true) }
            try {
                val list = repository.getInbox(user)
                _state.update { it.copy(loading = false, tickets = list) }
            } catch (e: Exception) {
                loadedUserId = null
                _state.update { it.copy(loading = false, error = "No se pudieron cargar los tickets") }
            }
        }
    }

    fun onQueryChange(value: String) {
        _state.update { it.copy(query = value) }
    }

    fun onFilterChange(filter: String?) {
        _state.update { it.copy(stateFilter = filter) }
    }

    fun createTicket(user: User, flowType: String, priority: String, location: String, description: String) {
        viewModelScope.launch {
            val ticket = repository.create(user, flowType, priority, location, description)
            _state.update { it.copy(tickets = listOf(ticket) + it.tickets) }
        }
    }

    fun changeState(id: String, newState: String, author: String) = modify(id) { t ->
        if (t.state == newState) {
            t
        } else {
            val now = DateUtils.now()
            t.copy(
                state = newState,
                updatedAt = now,
                history = t.history + TicketEvent(author, "Estado: ${t.state} → $newState", now)
            )
        }
    }

    fun assign(id: String, assignee: String, author: String) = modify(id) { t ->
        if (t.assignee == assignee) {
            t
        } else {
            val now = DateUtils.now()
            t.copy(
                assignee = assignee,
                updatedAt = now,
                history = t.history + TicketEvent(author, "Asignado a $assignee", now)
            )
        }
    }

    fun addComment(id: String, text: String, author: String) {
        if (text.isBlank()) return
        modify(id) { t ->
            val now = DateUtils.now()
            t.copy(
                comments = t.comments + TicketComment(author, text.trim(), now),
                updatedAt = now
            )
        }
    }

    private fun modify(id: String, transform: (Ticket) -> Ticket) {
        viewModelScope.launch {
            val updatedList = _state.value.tickets.map { if (it.idRequest == id) transform(it) else it }
            updatedList.firstOrNull { it.idRequest == id }?.let { repository.update(it) }
            _state.update { it.copy(tickets = updatedList) }
        }
    }
}