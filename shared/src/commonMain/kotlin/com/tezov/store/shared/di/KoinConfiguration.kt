package com.tezov.store.shared.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.plugin.module.dsl.koinConfiguration

@Module
@ComponentScan("com.tezov.store.shared")
class SharedModule

@KoinApplication(modules = [SharedModule::class])
object SharedApplication

val koinConfiguration = koinConfiguration<SharedApplication> {

}
