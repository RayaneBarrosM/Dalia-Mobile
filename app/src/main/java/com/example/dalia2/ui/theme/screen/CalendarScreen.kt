package com.example.dalia2.ui.theme.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.data.model.AppMode
import com.example.dalia2.ui.components.CampoData
import com.example.dalia2.ui.components.CampoHora
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.PregnancyCalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.ProfileViewModel
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    viewModelPregnancy: PregnancyCalendarViewModel,
    viewModelProfile: ProfileViewModel,
    onToCreateEvent: (LocalDate) -> Unit ={},
) {
    //para o calendario
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // 1 ano para trás
    val endMonth = remember { currentMonth.plusMonths(12) }   // 1 ano para frente
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var dateSelected by remember { mutableStateOf<LocalDate?>(null) }

    //para o calendario da mentruação
    val menstruacao by viewModel.diasMenstruacao.collectAsState()
    val fertil by viewModel.diasFertil.collectAsState()
    val ovulacao by viewModel.diaOvulacao.collectAsState()
    val hoje = remember{LocalDate.now()}


   //para o calendario da gravidez
    val EventsDay = remember { setOf(LocalDate.now(), LocalDate.now().plusDays(3)) }
    val isModoGravidez = viewModelPregnancy.isModoGravidez
    val eventosDoCalendario = viewModelPregnancy.eventosAgrupadosPorData
    val eventosDoDiaSelecionado = dateSelected?.let { eventosDoCalendario[it] } ?: emptyList()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale()
    )

    var showCreateEventModal by remember { mutableStateOf(false) }
    var dataSelecionadaParaEvento by remember { mutableStateOf<LocalDate?>(null) }

// Quando clicar em adicionar evento (no clique do dia ou em um botão)
    val onAbrirCriacaoEvento: (LocalDate) -> Unit = { data ->
        dataSelecionadaParaEvento = data
        showCreateEventModal = true
    }

    LaunchedEffect(Unit) {
        viewModelPregnancy.atualizarModo()
        if(viewModelPregnancy.isModoGravidez){
            Log.d("Calendario", "o modo gravidez: " + viewModelPregnancy.isModoGravidez)
            viewModelPregnancy.carregarEvents()
        }
    }
// Dentro do seu Scaffold / Box principal:
    if (showCreateEventModal) {
        CreateEventBottomSheet(
            dataInicial = dataSelecionadaParaEvento,
            onDismiss = { showCreateEventModal = false },
            onEventCreated = { titulo, data, hora, local, descricao ->
                viewModelPregnancy.criarEvento(titulo, descricao, data, hora, local)
                showCreateEventModal = false
            }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                if(!isModoGravidez) {
                    Button(
                        onClick = { viewModel.registrarMenstruacao() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Registrar Menstruação Hoje", color = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            VerticalCalendar(
                modifier = Modifier.padding(paddingValues),
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        isModoGravidez = isModoGravidez,
                        haveEvent = eventosDoCalendario.containsKey(day.date),
                        isMenstruacao = menstruacao.contains(day.date),
                        isOvulacao = ovulacao == day.date,
                        isFertil = fertil.contains(day.date),
                        isHoje = hoje == day.date,
                        onClick = {
                            if(isModoGravidez){
                                dateSelected = day.date
                                showBottomSheet = true
                            }
                        }
                    )
                },
                monthHeader = { month ->
                    val title = "${month.yearMonth.month.name} ${month.yearMonth.year}"
                    Text(
                        text = title.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            )
        }
        if (showBottomSheet && dateSelected != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false},
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .padding(bottom = 32.dp)
                ){
                    Text(
                        text = "Eventos de ${dateSelected}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (eventosDoDiaSelecionado.isEmpty()) {
                        Text(
                            text = "Nenhum evento agendado para este dia.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    } else {
                        // Lista de eventos do dia
                        eventosDoDiaSelecionado.forEach { evento ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F7F9))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = evento.titulo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    if (evento.local.isNotBlank()) {
                                        Text(
                                            text = "Local: ${evento.local}",
                                            fontSize = 13.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                    if (evento.dataHora.length >= 16) {
                                        Text(
                                            text = "${evento.dataHora.substring(11, 16)}",
                                            fontSize = 13.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                    if (evento.descricao.isNotBlank()) {
                                        Text(
                                            text = evento.descricao,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showBottomSheet = false
                            onAbrirCriacaoEvento(dateSelected!!)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
                    ) {
                        Text(
                            text = "Criar evento",

                            )
                    }



                }
            }
        }
    }
}
@Composable
fun Day(
    day: CalendarDay,
    isModoGravidez: Boolean,
    haveEvent: Boolean,
    isMenstruacao: Boolean,
    isFertil: Boolean,
    isHoje: Boolean,
    isOvulacao: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isModoGravidez){
        Color.Transparent
    } else {
        when {
            isMenstruacao -> Color(0xFFFFB3B3)
            isOvulacao -> Color(0xFF63D2FF)// Rosa Dalia
            isFertil -> Color(0xFFC4F8FF) // Azul Bebê
            else -> Color.Transparent
        }
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Deixa o dia quadradinho
            .padding(2.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .border(
                width =  if(isHoje) 2.dp else 0.dp,
                color = if(isHoje) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .clickable{onClick()},
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) Color.Black else Color.Gray,
            fontWeight = if(isHoje) FontWeight.ExtraBold else FontWeight.Normal
        )
        if (isModoGravidez && haveEvent){
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = PinkButton, shape = CircleShape)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventBottomSheet(
    dataInicial: LocalDate?,
    onDismiss: () -> Unit,
    onEventCreated: (titulo: String, data: String, hora: String, local: String, descricao: String) -> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var titulo by remember { mutableStateOf("") }
    var data by remember { mutableStateOf(dataInicial?.toString() ?: "") }
    var hora by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Text(
            text = "Criar evento",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("titulo") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                CampoData(
                    value= data,
                    onDataConfirmada = { data = it },
                    modifier = Modifier.weight(1f)
                )
                CampoHora(
                    value = hora,
                    onHoraConfirmada = { hora = it },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = local,
                onValueChange = { local = it },
                placeholder = { Text("local") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                placeholder = { Text("descricao") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if(titulo.isNotBlank() && data.isNotBlank() && hora.isNotBlank() && local.isNotBlank() && descricao.isNotBlank()){
                        onEventCreated(titulo, data, hora, local, descricao)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
            ) {
                Text("Salvar evento", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    Dalia2Theme {
        //CalendarScreen
    }
}
