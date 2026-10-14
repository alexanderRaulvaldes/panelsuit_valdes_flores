package com.panalbit.panalsuite.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panalbit.panalsuite.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel: ViewModel() {
    fun loadTickets(rol: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiServie.getInbox(rol)

                if (response.isSuccessful){
                    val tickets= response.body()

                    withContext(Dispatchers.Main){
                        // actualizar variables de estado AQUI
                    }
                }
            } catch (e: Exception){
                // manejo de errores ACA
            }
        }
    }

}