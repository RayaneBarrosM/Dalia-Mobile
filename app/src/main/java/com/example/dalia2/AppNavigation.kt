package com.example.dalia2

import CreatePostScreen
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dalia2.data.SessionManager
import com.example.dalia2.data.model.AppMode
import com.example.dalia2.ui.components.BottomNavigationBar
import com.example.dalia2.ui.theme.screen.*
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.ForumViewModel
import com.example.dalia2.ui.theme.viewmodel.PregnancyCalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.PregnancyQuizViewModel
import com.example.dalia2.ui.theme.viewmodel.ProfileViewModel
import com.example.dalia2.ui.theme.viewmodel.QuizViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val viewmodelPregnancyCalendar: PregnancyCalendarViewModel = hiltViewModel(context as ComponentActivity)
    val viewmodelProfile: ProfileViewModel = hiltViewModel(context as ComponentActivity)
    val viewmodelQuiz: QuizViewModel = hiltViewModel(context as ComponentActivity)
    val viewmodelCalendar: CalendarViewModel = hiltViewModel(context as ComponentActivity)
    val viewModelForum: ForumViewModel = hiltViewModel(context as ComponentActivity)
    val viewModelPregnancyQuiz: PregnancyQuizViewModel = hiltViewModel(context as ComponentActivity)

    val currentMode by viewmodelProfile.currentMode.collectAsState()

    // Função auxiliar para determinar a rota de Home correta dinamicamente
    val homeRoute = if (currentMode == AppMode.GRAVIDEZ) "homePregnant" else "home"

    // Lista de rotas onde a barra deve aparecer
    val bottomBarRoutes = listOf("home", "homePregnant","calendar", "bot", "forum", "settings")

    val startDestination = if (!sessionManager.getAccessToken().isNullOrBlank()) {
        val mode = sessionManager.getAppMode()
        if (mode == AppMode.GRAVIDEZ) "homePregnant" else "home"
    } else {
        "welcomeScreen"
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(
                    navController = navController,
                    currentMode = currentMode
                )
            }
        }
    ) { padding ->
    NavHost(
        navController = navController,
        //startDestination = "welcomeScreen",
        modifier = Modifier.padding(padding),
        startDestination = startDestination // pagina inícial

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
                    val mode = sessionManager.getAppMode()
                    val target = if (mode == AppMode.GRAVIDEZ) "homePregnant" else "home"
                    navController.navigate(target) {
                        popUpTo("login") { inclusive = true }
                    }
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
                    viewmodelProfile.loadUserProfile(forceRefresh = true)
                    navController.navigate("home") {
                        popUpTo("quizPeriod"){inclusive = true}
                    }
                }
            )
        }


        composable("quizPregnant") {
            QuizPregnantScreen(viewModel = viewModelPregnancyQuiz,
                onQuizComplete = {
                    viewmodelProfile.loadUserProfile(forceRefresh = true)
                    navController.navigate("homePregnant") {
                        popUpTo("quizPregnant"){inclusive = true}
                    }                }
            )
        }

        composable("home"){
            HomeScreen(
                viewModel = viewmodelCalendar,
                viewModelForum = viewModelForum,
                viewModelProfile = viewmodelProfile,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToCalendar = {
                    navController.navigate("calendar")
                }
            )
        }

        composable("homePregnant") {
            HomePregnantScreen(
                viewModel = viewmodelPregnancyCalendar,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToCalendar = {
                    navController.navigate("calendar")
                },
                onNavigateToArticle = {
                    navController.navigate("articleScreen")
                },
                onNavigateToGeneralNews = {
                    navController.navigate("generalNews")
                }
            )
        }


        composable("register") {
            RegisterScreen()
        }

        composable ("calendar"){
            CalendarScreen(
                viewModel = viewmodelCalendar,
                viewModelPregnancy = viewmodelPregnancyCalendar,
                viewModelProfile = viewmodelProfile
            )
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
         composable("settings") {
            ProfileScreen(
                viewModel = viewmodelProfile,
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
                    navController.navigate("quizPregnant")
                },
                onBackClick = {
                    navController.navigate(homeRoute) {
                        popUpTo(homeRoute) { inclusive = true }
                    }
                },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("articleScreen/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
            ArticleScreen(
                articleItem = TODO(),
                onBackClick = { navController.popBackStack() },
                onBookmarkClick = { },
                onShareClick = { }
            )
        } //Muda a tela pela id do card

        composable("generalNews") {
            GeneralNewsScreen()
        }

    }}
}
