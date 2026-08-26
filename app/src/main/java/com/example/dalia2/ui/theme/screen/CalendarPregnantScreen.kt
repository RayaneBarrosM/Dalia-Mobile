package com.example.dalia2.ui.theme.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPregnantScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {}