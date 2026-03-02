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
import org.koin.dsl.module

val koinConfiguration = KoinConfiguration {
    modules(module {

        single<ConfigProtocol> { params ->
            object : ConfigProtocol {
                override val storageInitialQuantity = params.get<Long>()
            }
        }

        scope<Software> {
            factory<Software> {
                Software(
                    engine = get(),
                    simulator = get()
                )
            }
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