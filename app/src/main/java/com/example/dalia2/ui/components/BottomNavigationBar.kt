package com.example.dalia2.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    isPregnantMode: Boolean,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        Destination.items.forEach { destination ->
            val isSelected = isDestinationSelected(destination, currentRoute)//Para deixar a aba em destaque e diferenciar as telas de home e calendar

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val targetRoute = getTargetRoute(destination, isPregnantMode)

                    if (currentRoute != targetRoute) { //Impede que a tela reinicie no modo errado
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = destination.icon(),
                        contentDescription = destination.label,
                        tint = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        fontSize = 12.sp,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

// Funções Auxiliares de Decisão de Rota para ajudar na persistencia
private fun getTargetRoute(destination: Destination, isPregnantMode: Boolean): String {
    return when (destination) {
        Destination.Home -> if (isPregnantMode) "homePregnant" else "home"
        Destination.Calendar -> if (isPregnantMode) "calendarPregnant" else "calendar"
        else -> destination.route
    }
}

private fun isDestinationSelected(destination: Destination, currentRoute: String?): Boolean {
    return when (destination) {
        Destination.Home -> currentRoute == "home" || currentRoute == "homePregnant"
        Destination.Calendar -> currentRoute == "calendar" || currentRoute == "calendarPregnant"
        else -> currentRoute == destination.route
    }
}