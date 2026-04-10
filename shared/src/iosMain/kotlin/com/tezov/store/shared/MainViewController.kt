package com.tezov.store.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.tezov.store.shared.domain.protocol.FromIosProtocol
import com.tezov.store.shared.presentation.MainScreen

@Suppress("unused")
fun MainViewController(
    fromIosProtocol: ()  -> FromIosProtocol
) = ComposeUIViewController {
    MainScreen(
        moduleDeclaration = { single<FromIosProtocol> { fromIosProtocol() } }
    )
}
