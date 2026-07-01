package com.apptech.benefit.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptech.benefit.data.model.AppSummary
import com.apptech.benefit.data.repository.BenefitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Success(val apps: List<AppSummary>) : AppListUiState
    data class Error(val message: String) : AppListUiState
}

class AppListViewModel(
    private val repository: BenefitRepository = BenefitRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppListUiState>(AppListUiState.Loading)
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allApps: List<AppSummary> = emptyList()

    init {
        loadApps()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    fun refresh() {
        loadApps(showRefreshSpinner = true)
    }

    private fun loadApps(showRefreshSpinner: Boolean = false) {
        viewModelScope.launch {
            if (showRefreshSpinner) {
                _isRefreshing.value = true
            } else {
                _uiState.value = AppListUiState.Loading
            }
            try {
                allApps = repository.getApps()
                applyFilter()
            } catch (e: Exception) {
                _uiState.value = AppListUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun applyFilter() {
        val query = _searchQuery.value.trim()
        val filtered = if (query.isEmpty()) {
            allApps
        } else {
            allApps.filter { app ->
                app.name.contains(query, ignoreCase = true) ||
                    app.category.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = AppListUiState.Success(filtered)
    }
}
