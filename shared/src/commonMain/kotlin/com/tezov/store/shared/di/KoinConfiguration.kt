package com.tezov.store.shared.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module
import org.koin.plugin.module.dsl.koinConfiguration

@Module
@ComponentScan(
    "com.tezov.store.shared.di",
    "com.tezov.store.shared.data",
    "com.tezov.store.shared.domain",
    "com.tezov.store.shared.presentation"
)
class SharedModule

@KoinApplication(modules = [SharedModule::class])
class SharedApplication

fun koinConfiguration(
    moduleDeclaration: ModuleDeclaration
) = koinConfiguration<SharedApplication> {
    modules(module { moduleDeclaration() })
}
