package com.tezov.store.shared.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Serializable
sealed class AppNavKey(
    @OptIn(ExperimentalUuidApi::class)
    val id: Uuid = Uuid.generateV4()
) : NavKey {


    @Serializable
    data object RolesOverviewScreen : AppNavKey()

    @Serializable
    data class RoleDetailsScreen(
        val roleName: String
    ) : AppNavKey()

    @Serializable
    data object ReleaseWorkersScreen : AppNavKey()
}

