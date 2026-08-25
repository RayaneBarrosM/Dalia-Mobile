package com.example.dalia2

import CreatePostScreen
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dalia2.ui.components.BottomNavigationBar
import com.example.dalia2.ui.theme.screen.*
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.ForumViewModel
import com.example.dalia2.ui.theme.viewmodel.PregnancyQuizViewModel
import com.example.dalia2.ui.theme.viewmodel.ProfileViewModel
import com.example.dalia2.ui.theme.viewmodel.QuizViewModel

// implementar viewModel para o salvamento no banco de dados
fun saveData(month: Int, weeks: Int) {
    println(" Dados salvos - Mês: $month, Semanas: $weeks")

}
fun saveFactor(factor: String) {
  println("Fator escolhido: $factor")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val viewmodelQuiz: QuizViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val viewmodelCalendar: CalendarViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val viewModelForum: ForumViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val viewModelPregnancyQuiz: PregnancyQuizViewModel = hiltViewModel(LocalContext.current as ComponentActivity)


    // Lista de rotas onde a barra deve aparecer
    val bottomBarRoutes = listOf("home", "calendar", "bot", "forum", "settings")

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
    NavHost(
        navController = navController,
        startDestination = "welcomeScreen" // pagina inícial

    ) {

        composable("welcomeScreen") {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate("login")
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home")
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignupScreen(navController = navController,
                onSignUpSuccess = {
                    navController.navigate("verification") // vai para o quiz
                },
                onLoginClick = {
                    navController.navigate("login") // Navega para login
                }
            )
        }

        //passa o email pela "url"
        composable("verification/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerificationScreen(
                email = email,
                onVerificationSucess = {
                    navController.navigate("startQuiz")
                },
                onBackClick ={
                    navController.navigate("signup")
                }
            )
        }

        composable("startQuiz") {
            StartQuizScreen(
                onStartClick = {
                    navController.navigate("quizPeriod")
                }
            )
        }

        composable("quizPeriod") {
            QuizPeriodScreen(viewModel = viewmodelQuiz,

                onQuizComplete = {
                    navController.navigate("home") {
                        popUpTo("quizPeriod"){inclusive = true}
                    }
                }
            )
        }


        composable("quizPregnant") {
            QuizPregnantScreen(viewModel = viewModelPregnancyQuiz,
                onQuizComplete = {
                    navController.navigate("homePregnant") {
                        popUpTo("quizPregnant"){inclusive = true}
                    }
                }
            )
        }

        composable("home"){
            HomeScreen(
                viewModel = viewmodelCalendar,
                viewModelForum = viewModelForum,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToCalendar = {
                    navController.navigate("calendar")
                }
            )
        }

        composable("homePregnant") {
            HomePregnantScreen()
        }


        composable("register") {
            RegisterScreen()
        }

        composable ("calendar"){
            /*onNavigateToRegister = {
                navController.navigate("register")
            }*/
        }

        composable("forum") {
            ForumScreen(
                viewModel = viewModelForum,
                onToCreatePost = {
                    navController.navigate("createPost")
                },
                onNavigateToPostDetail = { idPost ->
                    navController.navigate("postDetail/$idPost")
                }
            )
        }

        composable("createPost") {
            CreatePostScreen(
                viewModel = viewModelForum,
                onBack = { navController.popBackStack() }
            )
        }

        composable("postDetail/{idPost}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("idPost") ?: ""
            Log.d("PostDetailScreen", "ID do post: $postId")
            PostDetailScreen(
                postId = postId,
                viewModel = viewModelForum,
                onBack = { navController.popBackStack() }
            )
        }

        composable("calendar") {
            CalendarScreen()
        }

        composable("bot") {
            DaliaBotScreen()
        }

        composable("informationScreen") {
            InformationScreen()
        }

        composable("helpScreen") {
            HelpScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("editProfileScreen") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("settings")
            }
            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)
            Log.d("EditProfileScreen", "passando aqui")
            EditProfileScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("settings") {backStackEntry ->
            val viewModel: ProfileViewModel = hiltViewModel(backStackEntry)
            ProfileScreen(
                viewModel = viewModel,
                onEditarClick = {
                    navController.navigate("editProfileScreen")
                },
                onInformationClick ={
                    navController.navigate("informationScreen")
                },
                onHelpClick ={
                    navController.navigate("helpScreen")
                },
                onChangeModeClick ={
                    navController.navigate("quizPregnant") //Vai ter que criar um view model para saber em qual modo está
                }
            )
        }

    }}
}