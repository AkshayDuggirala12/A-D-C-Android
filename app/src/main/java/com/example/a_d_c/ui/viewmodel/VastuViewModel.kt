package com.example.a_d_c.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a_d_c.data.api.RetrofitClient
import com.example.a_d_c.data.model.PlanRequest
import com.example.a_d_c.data.model.PlanResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class VastuUiState {
    object Idle : VastuUiState()
    object Loading : VastuUiState()
    data class Success(val response: PlanResponse) : VastuUiState()
    data class Error(val message: String) : VastuUiState()
}

class VastuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<VastuUiState>(VastuUiState.Idle)
    val uiState: StateFlow<VastuUiState> = _uiState.asStateFlow()

    fun generatePlan(request: PlanRequest) {
        viewModelScope.launch {
            _uiState.value = VastuUiState.Loading
            try {
                val response = RetrofitClient.instance.generatePlan(request)
                if (response.success) {
                    _uiState.value = VastuUiState.Success(response)
                } else {
                    _uiState.value = VastuUiState.Error(response.message ?: response.error ?: "Failed to generate plan")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _uiState.value = VastuUiState.Error("Validation Error (422): $errorBody")
            } catch (e: Exception) {
                _uiState.value = VastuUiState.Error(
                    "Error: ${e.localizedMessage}. \n" +
                    "Check if backend is running at ${RetrofitClient.BASE_URL}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = VastuUiState.Idle
    }
}
