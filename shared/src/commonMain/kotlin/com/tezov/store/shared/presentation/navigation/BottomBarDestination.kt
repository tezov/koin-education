package com.tezov.store.shared.presentation.navigation

import androidx.navigation3.runtime.NavKey

sealed class BottomBarDestination(
    val navKey: NavKey,
    val label: String
) {
    object RolesOverview : BottomBarDestination(
        navKey = AppNavKey.RolesOverviewScreen,
        label = "Roles"
    )

    object ReleaseWorkers : BottomBarDestination(
        navKey = AppNavKey.ReleaseWorkersScreen,
        label = "Release"
    )
}

val bottomBarNavigationItems = listOf(
    BottomBarDestination.RolesOverview,
    BottomBarDestination.ReleaseWorkers
)