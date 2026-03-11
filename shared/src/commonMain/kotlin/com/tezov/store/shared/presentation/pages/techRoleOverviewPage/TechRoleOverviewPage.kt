package com.tezov.store.shared.presentation.pages.techRoleOverviewPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.presentation.models.TechRoleCardUiModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TechRoleOverviewPage(
    modifier: Modifier,
    bottomExtraPadding: Dp,
    navigateToDetailRole: (TechRoleDomainModel) -> Unit,
    viewModel: TechRoleOverviewViewModel = koinViewModel(),
) {
    val roles = viewModel.state.collectAsState().value
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(roles) { roleCard ->
            TechRoleCard(
                roleCard = roleCard,
                onClick = { navigateToDetailRole(roleCard.role) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(bottomExtraPadding))
        }
    }
}

@Composable
fun TechRoleCard(
    roleCard: TechRoleCardUiModel,
    onClick: (TechRoleCardUiModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onClick(roleCard) },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = roleCard.role.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Workers: ${roleCard.workerCount}")

                roleCard.avgExperience?.let { Text("Avg Experience: $it") }
                roleCard.avgProductivity?.let { Text("Avg Productivity: $it") }
                roleCard.avgSarcasm?.let { Text("Avg Sarcasm: $it") }
                roleCard.avgBurnoutRisk?.let { Text("Avg Burnout Risk: $it") }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Back",
                tint = Color.Gray
            )
        }
    }
}