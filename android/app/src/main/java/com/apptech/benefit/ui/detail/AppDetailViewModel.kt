package com.apptech.benefit.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.benefit.data.model.AppDetail
import com.apptech.benefit.data.repository.BenefitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AppDetailUiState {
    data object Loading : AppDetailUiState
    data class Success(val app: AppDetail) : AppDetailUiState
    data class Error(val message: String) : AppDetailUiState
}

class AppDetailViewModel(
    private val appId: String,
    private val repository: BenefitRepository = BenefitRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppDetailUiState>(AppDetailUiState.Loading)
    val uiState: StateFlow<AppDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = AppDetailUiState.Loading
            try {
                val detail = repository.getAppDetail(appId)
                _uiState.value = AppDetailUiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = AppDetailUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}
