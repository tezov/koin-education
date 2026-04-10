package com.tezov.store.shared.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.tezov.store.shared.di.koinConfiguration
import com.tezov.store.shared.domain.protocol.FromIosProtocol
import com.tezov.store.shared.presentation.navigation.AppNavKey
import com.tezov.store.shared.presentation.navigation.bottomBarNavigationItems
import com.tezov.store.shared.presentation.navigation.rememberBackStackNavigation
import com.tezov.store.shared.presentation.pages.releaseWorkersPage.ReleaseWorkersPage
import com.tezov.store.shared.presentation.pages.techRoleDetailPage.TechRoleDetailsPage
import com.tezov.store.shared.presentation.pages.techRoleOverviewPage.TechRoleOverviewPage
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.currentKoinScope
import org.koin.dsl.ModuleDeclaration

@Composable
fun MainScreen(
    moduleDeclaration: ModuleDeclaration
) {
    KoinApplication(
        configuration = koinConfiguration(moduleDeclaration),
        content = {
            /* to demonstrate ios injection */
            val scope = currentKoinScope()
            val fromIos = remember {
                scope.getOrNull<FromIosProtocol>()
            }
            println("fromIos is ${fromIos?.getInt()}")
            /* end demo */

//            MainScreenContent()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent() {
    val backStackNavigation = rememberBackStackNavigation()
    val currentScreen = backStackNavigation.lastOrNull()

    val topAppBarTitle = remember { mutableStateOf("") }

    val topBarHeight = 92.dp
    val showTopAppBar = remember { mutableStateOf(true) }

    val bottomBarHeight = 84.dp
    val showBottomBar = remember { mutableStateOf(false) }
    val bottomBarOffset by animateDpAsState(
        targetValue = if (showBottomBar.value) 0.dp else bottomBarHeight,
        animationSpec = tween(300)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimatedVisibility(
                visible = showTopAppBar.value,
                enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(300))
            ) {
                TopAppBar(
                    modifier = Modifier.height(topBarHeight),
                    title = { Text(topAppBarTitle.value) },
                    navigationIcon = {
                        IconButton(onClick = { backStackNavigation.removeLastOrNull() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.offset { IntOffset(x = 0, y = bottomBarOffset.roundToPx()) }
            ) {
                bottomBarNavigationItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentScreen == item.navKey,
                        onClick = {
                            if (currentScreen != item.navKey) {
                                val index =
                                    backStackNavigation.indexOfFirst { it == item.navKey }
                                if (index != -1) {
                                    // single top + popup to not inclusive
                                    while (backStackNavigation.lastIndex > index) {
                                        backStackNavigation.removeAt(backStackNavigation.lastIndex)
                                    }
                                } else {
                                    // push
                                    backStackNavigation.add(item.navKey)
                                }
                            }
                        },
                        label = { Text(item.label) },
                        icon = { }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStackNavigation,
            onBack = { backStackNavigation.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<AppNavKey.RoleDetailsScreen> { key ->
                    showBottomBar.value = false
                    showTopAppBar.value = true
                    topAppBarTitle.value = key.roleName
                    TechRoleDetailsPage(
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                        role = key.roleName
                    )
                }
                entry<AppNavKey.ReleaseWorkersScreen> {
                    showBottomBar.value = true
                    showTopAppBar.value = false
                    ReleaseWorkersPage(
                        modifier = Modifier
                            .padding(WindowInsets.statusBars.asPaddingValues())
                            .padding(bottom = innerPadding.calculateBottomPadding()),
                        onSend = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Not implemented :)")
                            }
                        }
                    )
                }
                entry<AppNavKey.RolesOverviewScreen> {
                    showBottomBar.value = true
                    showTopAppBar.value = false
                    TechRoleOverviewPage(
                        modifier = Modifier
                            .padding(WindowInsets.statusBars.asPaddingValues()),
                        bottomExtraPadding = innerPadding.calculateBottomPadding(),
                        navigateToDetailRole = { role ->
                            backStackNavigation.add(AppNavKey.RoleDetailsScreen(roleName = role.name))
                        }
                    )
                }
            }
        )
    }
}