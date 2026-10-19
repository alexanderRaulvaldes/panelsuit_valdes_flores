package com.panalbit.panalsuite.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.panalbit.panalsuite.model.TicketPriority
import com.panalbit.panalsuite.model.TicketState
import com.panalbit.panalsuite.ui.theme.SemanticError
import com.panalbit.panalsuite.ui.theme.SemanticInfo
import com.panalbit.panalsuite.ui.theme.SemanticSuccess
import com.panalbit.panalsuite.ui.theme.SemanticWarning

@Composable
fun StateChip(ticketState: String) {
    val color = when (ticketState) {
        TicketState.OPEN -> SemanticInfo
        TicketState.IN_PROGRESS, TicketState.PENDING -> SemanticWarning
        TicketState.CLOSED -> SemanticSuccess
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    ColoredLabel(ticketState, color)
}

@Composable
fun PriorityChip(priority: String) {
    val color = when (priority) {
        TicketPriority.HIGH -> SemanticError
        TicketPriority.MEDIUM -> SemanticWarning
        TicketPriority.LOW -> SemanticSuccess
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    ColoredLabel("Prioridad $priority", color)
}

@Composable
private fun ColoredLabel(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

