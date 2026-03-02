package com.tezov.store.shared.di

import com.tezov.store.shared.implementation.AndroidSimulator
import com.tezov.store.shared.implementation.AndroidStudioEngine
import com.tezov.store.shared.implementation.ConfigProtocol
import com.tezov.store.shared.implementation.EngineProtocol
import com.tezov.store.shared.implementation.FuelStorage
import com.tezov.store.shared.implementation.FuelStorageProtocol
import com.tezov.store.shared.implementation.Program
import com.tezov.store.shared.implementation.SimulatorProtocol
import com.tezov.store.shared.implementation.Software
import com.tezov.store.shared.implementation.XcodeEngine
import com.tezov.store.shared.implementation.iOSSimulator
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.scoped

val koinConfiguration = KoinConfiguration {
    modules(module {

        single<ConfigProtocol> { params ->
            object : ConfigProtocol {
                override val storageInitialQuantity = params.get<Long>()
            }
        }

        scope<Software> {
            factory<Software>()
        }

        scope<FuelStorageProtocol> {
            scoped<FuelStorageProtocol> {
                FuelStorage(initialQuantity = get<ConfigProtocol>().storageInitialQuantity)
            }
        }

        scope<Program.iOS> {
            scoped<XcodeEngine>() bind EngineProtocol::class
            factory<iOSSimulator>() bind SimulatorProtocol::class
        }

        scope<Program.Android> {
            scoped<AndroidStudioEngine>() bind EngineProtocol::class
            factory<AndroidSimulator>() bind SimulatorProtocol::class
        }
    })
}