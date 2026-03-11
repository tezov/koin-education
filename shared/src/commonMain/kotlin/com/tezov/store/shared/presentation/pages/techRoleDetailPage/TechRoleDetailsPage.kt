package com.tezov.store.shared.presentation.pages.techRoleDetailPage

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tezov.store.shared.presentation.models.WorkerDetailUiModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechRoleDetailsPage(
    modifier: Modifier,
    role: String,
    viewModel: TechRoleDetailsViewModel = koinViewModel(
        parameters = { parametersOf(role) }
    )
) {
    val workers = viewModel.workers.collectAsState().value
    Column(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            items(workers) { worker ->
                WorkerDetailCard(worker)
            }
        }
    }
}

@Composable
private fun WorkerDetailCard(worker: WorkerDetailUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(worker.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("Experience: ${worker.experience}")
            Text("Productivity: ${worker.productivity}")
            Text("Sarcasm: ${worker.sarcasm}")
            Text("Burnout Risk: ${worker.burnoutRisk}")
        }
    }
}