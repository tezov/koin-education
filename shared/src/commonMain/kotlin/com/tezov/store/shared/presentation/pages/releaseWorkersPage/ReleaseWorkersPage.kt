package com.tezov.store.shared.presentation.pages.releaseWorkersPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tezov.store.shared.presentation.models.WorkerCardUiModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReleaseWorkersPage(
    modifier: Modifier,
    onSend: () -> Unit,
    viewModel: ReleaseWorkersViewModel = koinViewModel()
) {
    val allWorkers = viewModel.allWorkers.collectAsState().value
    val selectedWorkers = viewModel.selectedWorkers.collectAsState().value

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(allWorkers) { worker ->
                WorkerCard(worker, selectedWorkers.contains(worker.id)) {
                    viewModel.toggleWorkerSelection(worker.id)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.sendReleaseRequest()
                    onSend()
                },
                enabled = selectedWorkers.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) { Text("Send") }

            Button(
                onClick = { viewModel.cancelRelease() },
                modifier = Modifier.weight(1f)
            ) { Text("Cancel") }
        }
    }
}

@Composable
fun WorkerCard(
    worker: WorkerCardUiModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSelected) Color.LightGray else Color.White)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = worker.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = worker.role.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}