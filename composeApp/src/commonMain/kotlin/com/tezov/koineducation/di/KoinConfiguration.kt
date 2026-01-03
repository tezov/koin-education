package com.tezov.koineducation.di

import com.tezov.koineducation.implementation.Engine
import com.tezov.koineducation.implementation.FuelStorage
import com.tezov.koineducation.implementation.Simulator
import com.tezov.koineducation.implementation.Software
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

val koinConfiguration = KoinConfiguration {
    modules(module {

        factory { params ->
            FuelStorage(initialQuantity = params.get())
        }

        factory {
            Engine(fuel = get(parameters = { parametersOf(3000L) }))
        }
        factory {
            Simulator()
        }
        single<Software> {
            Software(
                engine = get(),
                simulator = get()
            )
        }
    })
}