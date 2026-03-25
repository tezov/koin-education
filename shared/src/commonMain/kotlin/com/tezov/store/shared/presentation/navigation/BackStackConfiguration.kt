package com.tezov.store.shared.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val savedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(
                AppNavKey.RolesOverviewScreen::class,
                AppNavKey.RolesOverviewScreen.serializer()
            )
            subclass(AppNavKey.RoleDetailsScreen::class, AppNavKey.RoleDetailsScreen.serializer())
            subclass(
                AppNavKey.ReleaseWorkersScreen::class,
                AppNavKey.ReleaseWorkersScreen.serializer()
            )
        }
    }
}

@Composable
fun rememberBackStackNavigation() = rememberNavBackStack(
    configuration = savedStateConfiguration,
    AppNavKey.RolesOverviewScreen
)