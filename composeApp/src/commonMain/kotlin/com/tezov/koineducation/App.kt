package com.tezov.koineducation

import androidx.compose.runtime.Composable
import com.tezov.koineducation.di.koinConfiguration
import com.tezov.koineducation.implementation.FuelStorage
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.Software
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinApplication()
}

@Composable
fun KoinApplication() = KoinApplication(
    configuration = koinConfiguration,
    content = {
        val software = koinInject<Software>()

        with(Program { 5L }) {
            software.build(this)
            software.launch(this)
        }
    }
)