package com.example.watchly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.watchly.ui.detail.DetailScreen
import com.example.watchly.ui.detail.DetailScreenViewModel
import com.example.watchly.ui.home.HomeScreen
import com.example.watchly.ui.home.HomeViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {


    val navController = rememberNavController()

    val homeScreenViewModel = hiltViewModel<HomeViewModel>()
    val detailScreenViewModel = hiltViewModel<DetailScreenViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {


        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(
                viewModel = homeScreenViewModel,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            val itemId = checkNotNull(it.arguments?.getInt("itemId"))
            DetailScreen(
                viewModel = detailScreenViewModel,
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}



