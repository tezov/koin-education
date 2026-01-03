package com.tezov.koineducation.di

import com.tezov.koineducation.implementation.AndroidSimulator
import com.tezov.koineducation.implementation.AndroidStudioEngine
import com.tezov.koineducation.implementation.EngineProtocol
import com.tezov.koineducation.implementation.FuelStorage
import com.tezov.koineducation.implementation.FuelStorageProtocol
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.SimulatorProtocol
import com.tezov.koineducation.implementation.Software
import com.tezov.koineducation.implementation.XcodeEngine
import com.tezov.koineducation.implementation.iOSSimulator
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

val koinConfiguration = KoinConfiguration {
    modules(module {

        factory<FuelStorageProtocol> { params ->
            FuelStorage(initialQuantity = params.get())
        }

        // iOS
        factory<EngineProtocol>(named<Program.iOS>()) {
            XcodeEngine(fuel = get(parameters = { parametersOf(3000L) }))
        }
        factory<SimulatorProtocol>(named<Program.iOS>()) {
            iOSSimulator()
        }
        single<Software>(named<Program.iOS>()) {
            Software(
                engine = get(named<Program.iOS>()),
                simulator = get(named<Program.iOS>())
            )
        }

        // Android
        factory<EngineProtocol>(named<Program.Android>()) {
            AndroidStudioEngine(fuel = get(parameters = { parametersOf(20000L) }))
        }
        factory<SimulatorProtocol>(named<Program.Android>()) {
            AndroidSimulator()
        }
        single<Software>(named<Program.Android>()) {
            Software(
                engine = get(named<Program.Android>()),
                simulator = get(named<Program.Android>())
            )
        }
    })
}