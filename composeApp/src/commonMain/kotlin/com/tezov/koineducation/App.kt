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

    val softwareScope = remember { koin.createScope<Software>() }
    val storageScope = remember { koin.createScope<FuelStorageProtocol>() }

    // Android
    val androidScope = remember {
        koin.createScope<Program.Android>().also {
            it.linkTo(storageScope) // Now android will be able to resolve FuelStorageProtocol
        }
    }
    softwareScope.linkTo(androidScope) // Engine and Simulator will be resolved by AndroidScope
    val androidSoftware = remember { softwareScope.get<Software>() }
    with(Program.Android { 5L }) {
        androidSoftware.build(this)
        androidSoftware.launch(this)
    }

    // iOS
    val iosScope = remember {
        koin.createScope<Program.iOS>().also {
            it.linkTo(storageScope) // Now iOS will be able to resolve FuelStorageProtocol
            // Same as Android because same scope instance and FuelStorageProtocol is singleton 'scoped'
        }
    }
    softwareScope.unlink(softwareScope) // need to unlink to avoid conflict since iosScope resolve same class than androidScope
    softwareScope.linkTo(iosScope) // Engine and Simulator will be resolved by iosScope

    val iosSoftware = remember { softwareScope.get<Software>() } // this time software scope will resolve with iosScope
    with(Program.iOS { 5L }) {
        iosSoftware.build(this)
        iosSoftware.launch(this)
    }

    DisposableEffect(Unit) {
        onDispose {
            androidScope.close()
            iosScope.close()
            storageScope.close()
            softwareScope.close()
        }
    }
}