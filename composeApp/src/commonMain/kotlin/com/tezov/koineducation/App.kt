package com.tezov.koineducation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tezov.koineducation.di.koinConfiguration
import com.tezov.koineducation.implementation.ConfigProtocol
import com.tezov.koineducation.implementation.FuelStorageProtocol
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.Software
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    KoinApplication(
        configuration = koinConfiguration, content = {
            // initialize single configuration
            koinInject<ConfigProtocol>(parameters = {
                parametersOf(
                    /* storage initial quantity*/ 50000L
                )
            })

            val dispose = remember { mutableStateOf(true) }
            if (!dispose.value) {
                KoinGlobalScoped()
            }
            LaunchedEffect(Unit) {
                repeat(1000) {
                    dispose.value = !dispose.value
                    delay(2000)
                }
            }
        })
}

@Composable
fun KoinGlobalScoped() {
    val koin = getKoin()

    val androidScope = remember { koin.createScope<Program.Android>() }
    val androidSoftware = remember { androidScope.get<Software>() }
    with(Program.Android { 5L }) {
        androidSoftware.build(this)
        androidSoftware.launch(this)
    }

    val iosScope = remember { koin.createScope<Program.iOS>() }
    val iosSoftware = remember { iosScope.get<Software>() }
    with(Program.iOS { 5L }) {
        iosSoftware.build(this)
        iosSoftware.launch(this)
    }

    DisposableEffect(Unit) {
        onDispose {
            iosScope.close()
            iosScope.close()
        }
    }
}