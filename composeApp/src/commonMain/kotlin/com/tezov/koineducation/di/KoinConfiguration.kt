package com.tezov.koineducation.di

import com.tezov.koineducation.implementation.AndroidSimulator
import com.tezov.koineducation.implementation.AndroidStudioEngine
import com.tezov.koineducation.implementation.ConfigProtocol
import com.tezov.koineducation.implementation.EngineProtocol
import com.tezov.koineducation.implementation.FuelStorage
import com.tezov.koineducation.implementation.FuelStorageProtocol
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.SimulatorProtocol
import com.tezov.koineducation.implementation.Software
import com.tezov.koineducation.implementation.XcodeEngine
import com.tezov.koineducation.implementation.iOSSimulator
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

val koinConfiguration = KoinConfiguration {
    modules(module {

        single<ConfigProtocol> { params ->
            object : ConfigProtocol {
                override val storageInitialQuantity = params.get<Long>()
            }
        }

        factory<Software> {
            Software(
                engine = get(),
                simulator = get()
            )
        }

        scope<FuelStorageProtocol> {
            scoped<FuelStorageProtocol> {
                FuelStorage(initialQuantity = get<ConfigProtocol>().storageInitialQuantity)
            }
        }

        scope<Program.iOS> {
            scoped<EngineProtocol> {
                XcodeEngine(fuel = get())
            }
            factory<SimulatorProtocol> {
                iOSSimulator()
            }
        }

        scope<Program.Android> {
            scoped<EngineProtocol> {
                AndroidStudioEngine(fuel = get())
            }
            factory<SimulatorProtocol> {
                AndroidSimulator()
            }
        }
    })
}