package com.example.dalia2.ui.theme.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.LightPink
import com.example.dalia2.ui.theme.Purple
import com.example.dalia2.ui.components.Destination
import com.example.dalia2.ui.theme.Black
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dalia2.data.model.Articles
import com.example.dalia2.ui.theme.BlueButton
import com.example.dalia2.ui.theme.White
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.ForumViewModel
import com.example.dalia2.ui.theme.Black
import java.time.LocalDate
import kotlin.math.absoluteValue

data class MeuItem(
    val id: Int,
    val titulo: String,
    val dia: String,
    val isClickable: Boolean = true,
    val destination: String? = null

)

data class MedicoData(
    val id: Int,
    val nome: String,
    val especialidade: String,
    val crm: String
)
/*
val medicosDisponiveis = remember {
    listOf(
        MedicoData(1, "Dra. Ana Silva", "Ginecologista", "CRM 12345/SP"),
        MedicoData(2, "Dra. Beatriz Santos", "Obstetra", "CRM 67890/SP"),
        MedicoData(3, "Dr. Carlos Mendes", "Endocrinologista", "CRM 54321/RJ"),
        MedicoData(4, "Dra. Fernanda Lima", "Psicóloga Perinatal", "CRP 98765/SP")
    )
}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CalendarViewModel,
    viewModelForum: ForumViewModel,
    onNavigateToRegister: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {}
) {
    val hoje = LocalDate.now()
    val ontem = hoje.minusDays(1)
    val amanha = hoje.plusDays(1)

   val itensCarrossel = remember {
       listOf(
           MeuItem(1, "Ontem", ontem.toString(), destination = "calendar"),
           MeuItem(2, "Hoje", hoje.toString(), destination = "register"),
           MeuItem(3, "Amanhã", amanha.toString())
       )
   }

    var selectedDay by remember { mutableStateOf(itensCarrossel[1]) }
    val articlesAPI by viewModelForum.articles.collectAsState()

    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.carregarStatusHoje()
        viewModelForum.carregarArticles()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Seção dos dias
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            // Gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFF7CFC3),
                                Color(0xFFF7C4CF)
                            ),
                            radius = 600f,
                            center = Offset(0.5f, 0.5f)
                        )
                    )
            )

            // Carrossel de dias
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center //
            ) {
                DayCarousel(
                    itens = itensCarrossel,
                    selectedDay = selectedDay,
                    viewModel = viewModel,
                    onDaySelected = { day ->
                        //Rotas
                        if (day.isClickable) {
                            selectedDay = day
                            when (day.destination) {
                                "register" -> onNavigateToRegister()
                                "calendar" -> onNavigateToCalendar()
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Notícias sobre saúde",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Carrossel de notícias de saúde AQUI!!!!                 !
            NewsCarousel(
                articlesList = articlesAPI.filter { it.category?.equals("saude", ignoreCase = true) ==true },
                cardColor = Purple,
                categoriaLabel = "saude"
            )

            Spacer(modifier = Modifier.height(24.dp))

            //Banner
            Banner()

            Spacer(modifier = Modifier.height(24.dp))

            // Título: Notícias sobre legislação
            Text(
                text = "Notícias sobre legislação",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Carrossel de notícias de legislação AQUI!!!!                         !!
            NewsCarousel(
                articlesList = articlesAPI.filter { it.category?.equals("lesgilacao", ignoreCase = true) ==true },
                cardColor = LightPink,
                categoriaLabel = "legislacao"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Médicos disponíveis",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Text(
                text = "Agende uma consulta com nossos especialistas",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = Color(0xFF888888)
            )
            Spacer(modifier = Modifier.height(8.dp))
/*
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(medicosDisponiveis) { medico ->
                    DoctorCardHorizontal(medico = medico)
                }
            }*/

            Spacer(modifier = Modifier.height(32.dp))
    }
}

//Dias de marcar o registro
@Composable
fun DayCarousel(
    itens: List<MeuItem>,
    selectedDay: MeuItem,
    onDaySelected: (MeuItem) -> Unit,
    viewModel: CalendarViewModel
) {
    val initialPageIndex = itens.indexOf(selectedDay).coerceAtLeast(0)

    val pagerState = rememberPagerState(
        initialPage = initialPageIndex,
        pageCount = { itens.size }
    )

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 90.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        val item = itens[page]
        val dataDoCard = LocalDate.parse(item.dia) // Transforma a string de volta em data
        val status = viewModel.getStatusDoDia(dataDoCard) // BUSCA O STATUS REAL

        val isSelected = pagerState.currentPage == page

        // LOGICA DE CORES LIGADA AO CALENDÁRIO
        val corDeFundo = if (status == "MENSTRUAÇÃO") {
            Color(0xFFFF7979)
            } else if (status == "OVULAÇÃO") {
            Color(0xFF30ACFF)
            }  else if (status == "PERIODO FERTIL") {
            Color(0xFF93FFEE)
             }else {
                if (isSelected) Color(0xFFFFF5E6) else Color.White
        }

        Surface(
            modifier = Modifier
                .width(150.dp)
                .height(210.dp)
                .clickable { onDaySelected(item) },
            shape = CircleShape,
            color = corDeFundo, // APLICANDO A COR DINÂMICA
            shadowElevation = if (isSelected) 8.dp else 0.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = item.titulo.uppercase(),
                    color = if (status == "MENSTRUAÇÃO") Color.Red else Color.Black
                )

                // Texto do Status embaixo (ex: "Menstruação")
                if (status.isNotEmpty()) {
                    Text(text = status, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else if (isSelected && item.titulo == "Hoje") {
                    Text(text = "Como você está?", fontSize = 11.sp, color = Black)
                }
            }
        }
    }
}

@Composable
fun Banner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(BlueButton, LightPink)
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = "No Dália, nos importamos com a sua segurança, por isso gostaríamos que ficasse por dentro das leis que te defendem.",
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hm_moca),
                    contentDescription = "Moça demonstrando a força da mulher",
                    modifier = Modifier
                        .size(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

// Card de Notícia
@Composable
fun NewsCard(
    articles: Articles,
    cardColor: Color,
    categoriaLabel: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Imagem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.lotus),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Badge
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.White
            ) {
                Text(
                    text = categoriaLabel,
                    fontSize = 10.sp,
                    color = cardColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = articles.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "futura legenda aqui não se preocuope",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                maxLines = 2
            )
        }
    }
}

// Carrossel de Notícias
@Composable
fun NewsCarousel(
    articlesList: List<Articles>,
    cardColor: Color,
    categoriaLabel: String
) {

    val uriHandler = LocalUriHandler.current

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(articlesList) { article ->
            NewsCard(
                articles = article,
                cardColor = cardColor,
                categoriaLabel = categoriaLabel,
                onClick = {
                    article.link?.let { link ->
                        try {
                            uriHandler.openUri(article.link)
                        } catch (e: Exception) {
                            Log.e(
                                "NAV_ERROR",
                                "Não foi possível abrir a URL: ${article.link}",
                                e
                            )
                        }
                    }
                }
            )
        }
    }
}

// Componente do médico
@Composable
fun DoctorCardHorizontal(medico: MedicoData) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .clickable { /* Agendar consulta */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BlueButton.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = BlueButton,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = medico.nome,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = medico.especialidade,
                fontSize = 13.sp,
                color = BlueButton,
                textAlign = TextAlign.Center
            )

            Text(
                text = medico.crm,
                fontSize = 11.sp,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Agendar consulta */ },
                colors = ButtonDefaults.buttonColors(containerColor = BlueButton),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar", fontSize = 13.sp, color = White)
            }
        }
    }
}

// Navegação
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBarExample(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.Home.route
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.items.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon(),
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        MainNavGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(contentPadding)
        )
    }
}


@Composable
fun MainNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Destination.Home.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(navController.graph.startDestinationId)
            }
            val sharedViewModel: CalendarViewModel = hiltViewModel(parentEntry)
            val viewModelForum: ForumViewModel = hiltViewModel(parentEntry)

            HomeScreen(
                viewModel = sharedViewModel,
                viewModelForum = viewModelForum,
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToCalendar = { navController.navigate(Destination.Calendar.route) },
            )
        }
        composable(Destination.Calendar.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(navController.graph.startDestinationId)
            }
            val sharedViewModel: CalendarViewModel = hiltViewModel(parentEntry)

            CalendarScreen(viewModel = sharedViewModel)
        }

        composable(Destination.Calendar.route) {
            CalendarScreen()
        }

        composable(Destination.Bot.route) {
            DaliaBotScreen()
        }

        composable(Destination.Forum.route) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(navController.graph.startDestinationId)
            }
            val sharedViewModel: ForumViewModel = hiltViewModel(parentEntry)

            ForumScreen(
                viewModel = sharedViewModel
            )
        }

        composable(Destination.Settings.route) {
            ProfileScreen(
                viewModel = hiltViewModel()
            )
        }

        composable("register") {
            RegisterScreen(

            )
        }
    }
}



@Preview(widthDp = 400,
    heightDp = 1800)
@Composable
fun HomeScreenPreview() {
    Dalia2Theme {
        //HomeScreen()
    }
}