package com.example.a_d_c.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a_d_c.data.api.RetrofitClient
import com.example.a_d_c.data.model.VastuRequest
import com.example.a_d_c.data.model.VastuResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class VastuUiState {
    object Idle : VastuUiState()
    object Loading : VastuUiState()
    data class Success(val response: VastuResponse) : VastuUiState()
    data class Error(val message: String) : VastuUiState()
}

class VastuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<VastuUiState>(VastuUiState.Idle)
    val uiState: StateFlow<VastuUiState> = _uiState.asStateFlow()

    fun generatePlan(request: VastuRequest) {
        viewModelScope.launch {
            _uiState.value = VastuUiState.Loading
            try {
                // Check if we are on emulator or physical device for the log or debug
                val response = RetrofitClient.instance.generateFullPlan(request)
                if (response.success) {
                    _uiState.value = VastuUiState.Success(response)
                } else {
                    _uiState.value = VastuUiState.Error("Failed to generate plan: Success flag is false")
                }
            } catch (e: Exception) {
                _uiState.value = VastuUiState.Error(
                    "Connection Error: ${e.localizedMessage}. \n" +
                    "Check if backend is running at ${RetrofitClient.BASE_URL}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = VastuUiState.Idle
    }
}
