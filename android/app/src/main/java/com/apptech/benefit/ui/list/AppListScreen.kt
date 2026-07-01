package com.apptech.benefit.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.apptech.benefit.data.model.AppSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    onAppClick: (String) -> Unit,
    viewModel: AppListViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("앱테크 혜택모음") })
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("앱 이름 또는 카테고리 검색") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                when (val state = uiState) {
                    is AppListUiState.Loading -> LoadingContent()
                    is AppListUiState.Error -> ErrorContent(message = state.message)
                    is AppListUiState.Success -> AppList(apps = state.apps, onAppClick = onAppClick)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "불러오는 중 오류가 발생했습니다: $message",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
        )
    }
}

@Composable
private fun AppList(apps: List<AppSummary>, onAppClick: (String) -> Unit) {
    if (apps.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "등록된 앱이 없습니다.",
                modifier = Modifier.align(Alignment.Center),
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(apps, key = { it.id }) { app ->
            AppListItem(app = app, onClick = { onAppClick(app.id) })
        }
    }
}

@Composable
private fun AppListItem(app: AppSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(iconUrl = app.iconUrl, name = app.name)
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = app.name, style = MaterialTheme.typography.titleMedium)
                Text(text = app.category, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "받을 수 있는 혜택 ${app.benefitCount}개",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun AppIcon(iconUrl: String?, name: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (iconUrl != null) {
            AsyncImage(
                model = iconUrl,
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )
        } else {
            Text(
                text = name.take(1),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}
