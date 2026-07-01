package com.apptech.benefit.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.apptech.benefit.data.model.AppDetail
import com.apptech.benefit.data.model.Benefit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    appId: String,
    onBackClick: () -> Unit,
) {
    val viewModel: AppDetailViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AppDetailViewModel(appId = appId) }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("혜택 상세") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is AppDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is AppDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "불러오는 중 오류가 발생했습니다: ${state.message}",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                        )
                    }
                }
                is AppDetailUiState.Success -> {
                    AppDetailContent(
                        app = state.app,
                        onOpenLink = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(state.app.link)))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AppDetailContent(app: AppDetail, onOpenLink: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column {
                Text(text = app.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = app.category, style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            Button(onClick = onOpenLink, modifier = Modifier.fillMaxWidth()) {
                Text("앱/사이트 열기")
            }
        }
        item {
            HorizontalDivider()
        }
        items(app.benefits) { benefit ->
            BenefitCard(benefit = benefit)
        }
    }
}

@Composable
private fun BenefitCard(benefit: Benefit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = benefit.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = benefit.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            Text(text = "받는 방법", style = MaterialTheme.typography.labelLarge)
            benefit.howTo.forEachIndexed { index, step ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "${index + 1}. ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
