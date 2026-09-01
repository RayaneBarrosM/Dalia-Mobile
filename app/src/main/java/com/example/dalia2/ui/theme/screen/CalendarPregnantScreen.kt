package com.example.dalia2.ui.theme.screen

import com.example.dalia2.ui.theme.viewmodel.PregnancyCalendarViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel
import com.example.dalia2.ui.theme.viewmodel.PregnancyQuizViewModel
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPregnantScreen(
    viewModel: PregnancyCalendarViewModel = hiltViewModel()
) {
    val selectDay = remember { mutableStateOf<LocalDate?>(null) }
    val hoje = remember{LocalDate.now()}

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // 1 ano para trás
    val endMonth = remember { currentMonth.plusMonths(12) }   // 1 ano para frente

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale()
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Registrar Menstruação Hoje", color = Color.White)
                }
            }
        ) { paddingValues ->
            VerticalCalendar(
                modifier = Modifier.padding(paddingValues),
                state = state,
                dayContent = { day ->
                    //Day(
                        //isHoje = hoje == day.date
                    //)
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
    }
}
/*@Composable
fun Day(
    isOvulacao: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Deixa o dia quadradinho
            .padding(2.dp)
            .background(
                color = when {
                    isMenstruacao -> Color(0xFFFFB3B3)
                    isOvulacao -> Color(0xFF63D2FF)// Rosa Dalia
                    isFertil -> Color(0xFFC4F8FF) // Azul Bebê
                    //escuro
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .border(
                width =  if(isHoje) 2.dp else 0.dp,
                color = if(isHoje) Color.Black else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) Color.Black else Color.Gray,
            fontWeight = if(isHoje) FontWeight.ExtraBold else FontWeight.Normal
        )
}    }*/
