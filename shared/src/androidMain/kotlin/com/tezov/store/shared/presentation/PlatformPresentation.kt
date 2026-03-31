package com.tezov.store.shared.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tezov.store.shared.domain.PlatformDomainProtocol
import org.koin.core.annotation.Factory

@Factory
class PlatformPresentation(
    private val platformDomain: PlatformDomainProtocol
) : PlatformPresentationProtocol {

    @Composable
    override fun ComposableFromPlatform() {
        Text(
            text = platformDomain.description(),
            fontSize = 32.sp
        )
    }

}