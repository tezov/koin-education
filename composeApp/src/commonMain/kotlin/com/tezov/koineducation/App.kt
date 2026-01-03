package com.tezov.koineducation

import androidx.compose.runtime.Composable
import com.tezov.koineducation.di.koinConfiguration
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.Software
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
@Preview
fun App() {
    KoinApplication()
}

@Composable
fun KoinApplication() = KoinApplication(
    configuration = koinConfiguration,
    content = {
        val iosSoftware = koinInject<Software>(named<Program.iOS>())
        with(Program.iOS { 5L }) {
            iosSoftware.build(this)
            iosSoftware.launch(this)
        }

        val androidSoftware = koinInject<Software>(named<Program.Android>())
        with(Program.Android { 5L }) {
            androidSoftware.build(this)
            androidSoftware.launch(this)
        }
    }
)