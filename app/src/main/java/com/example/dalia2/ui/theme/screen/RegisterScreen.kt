package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.CalendarViewModel

@Composable
fun RegisterScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    var selectedMoods by remember { mutableStateOf(setOf<String>()) }
    var selectedHabits by remember { mutableStateOf(setOf<String>()) }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var selectedExercises by remember { mutableStateOf(setOf<String>()) }
    var selectedSex by remember { mutableStateOf(setOf<String>()) }
    var selectedDischarge by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(viewModel.recordSucess) {
        if (viewModel.recordSucess) {
            onBack()
        }
    }

    fun toggleSelection(currentSet: Set<String>, item: String): Set<String> {
        return if (currentSet.contains(item)) currentSet - item else currentSet + item
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Registro Diário",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Por favor, selecione as opções com as quais você mais se identifica hoje",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            BoxDegrade(title = "Como está o seu humor?") {
                val moods = listOf(
                    listOf(
                        "Feliz" to R.drawable.rg_happy,
                        "Neutro" to R.drawable.rg_neutral,
                        "Muito Feliz" to R.drawable.rg_great
                    ),
                    listOf(
                        "Triste" to R.drawable.rg_sad,
                        "Chorosa" to R.drawable.rg_sad,
                        "Apaixonada" to R.drawable.rg_fallinglove
                    ),
                    listOf(
                        "Irritada" to R.drawable.rg_upset,
                        "Surpresa" to R.drawable.rg_surprised,
                        "Ansiosa" to R.drawable.rg_grimace
                    )
                )

                moods.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { (name, icon) ->
                            EmojiItem(
                                iconRes = icon,
                                isSelected = selectedMoods.contains(name),
                                onClick = {
                                    selectedMoods = toggleSelection(selectedMoods, name)
                                    viewModel.updateHumor(selectedMoods.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BoxDegrade(title = "Quais hábitos praticou?") {
                val habits = listOf(
                    listOf("Leitura", "Meditação", "Terapia"),
                    listOf("Alongamento", "Pintura", "Banho de Sol"),
                    listOf("DIY", "Esportes", "Outro")
                )

                habits.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { habit ->
                            ButtonItem(
                                text = habit,
                                isSelected = selectedHabits.contains(habit),
                                onClick = {
                                    selectedHabits = toggleSelection(selectedHabits, habit)
                                    viewModel.updateHabitos(selectedHabits.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Sintomas
            BoxDegrade(title = "Quais sintomas sentiu?") {
                val symptoms = listOf(
                    listOf("Constipação", "Mudança de humor", "Nauseas"),
                    listOf("Dor de cabeça", "Fadiga", "Cólicas"),
                    listOf("Acne", "Inchaço", "Outro")
                )

                symptoms.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { symptom ->
                            ButtonItem(
                                text = symptom,
                                isSelected = selectedSymptoms.contains(symptom),
                                onClick = {
                                    selectedSymptoms = toggleSelection(selectedSymptoms, symptom)
                                    viewModel.updateSintomas(selectedSymptoms.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Atividades Físicas
            BoxDegrade(title = "Quais exercicios voce praticou?") {
                val exercises = listOf(
                    listOf("Musculação", "Caminhada", "Aerobico"),
                    listOf("Corrida", "Dança", "Luta"),
                    listOf("Pilates", "?", "Outro")
                )

                exercises.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { exercise ->
                            ButtonItem(
                                text = exercise,
                                isSelected = selectedExercises.contains(exercise),
                                onClick = {
                                    selectedExercises = toggleSelection(selectedExercises, exercise)
                                    viewModel.updateAtividadeFisica(selectedExercises.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BoxDegrade(title = "Vida íntima") {
                val sexOptions = listOf(
                    listOf("Não tive relações", "Com proteção"),
                    listOf("Sem proteção", "Toque sensual"),
                )

                sexOptions.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { opt ->
                            ButtonItem(
                                text = opt,
                                isSelected = selectedSex.contains(opt),
                                onClick = {
                                    selectedSex = toggleSelection(selectedSex, opt)
                                    viewModel.updateSexo(selectedSex.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // 6. Secreção Vaginal
            BoxDegrade(title = "Secreção vaginal") {
                val discharges = listOf(
                    listOf("Sem secreção", "Pastosa", "Aquoso"),
                    listOf("Clara de ovo", "Corrimento")
                )
                discharges.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { discharge ->
                            ButtonItem(
                                text = discharge,
                                isSelected = selectedDischarge.contains(discharge),
                                onClick = {
                                    selectedDischarge =
                                        toggleSelection(selectedDischarge, discharge)
                                    viewModel.updateSecrecao(selectedDischarge.toList())
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

                // Exibição de erro se houver
            viewModel.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Botão de Salvar
            Button(
                onClick = {
                    viewModel.createDailyRecord(
                    onSuccess = {
                        onBack()
                    }
                ) },
                enabled = viewModel.isLoading != true,
                modifier = Modifier.size(width = 304.dp, height = 44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                shape = RoundedCornerShape(8.dp),
            ) {
                if (viewModel.isLoading == true) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Salvar", fontSize = 16.sp)
                }
            }
        }
    }
}

//Função compose das sections
@Composable
fun BoxDegrade(
    title: String,
    content: @Composable ColumnScope.() -> Unit  //Passa o conteudo da interface dentro de Column
){
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFC7D0F2), Color(0xFFEFB2D9))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background( brush = gradient, shape = RoundedCornerShape(20.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            content()
        }
    }
}

@Composable
fun EmojiItem(iconRes: Int, isSelected: Boolean, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(if (isSelected) PinkButton.copy(alpha = 0.3f) else Color.White, CircleShape)
            .then(
                if (isSelected) Modifier.border(2.dp, PinkButton, CircleShape) else Modifier
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
    }
}

@Composable
fun ButtonItem(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PinkButton else Color.White,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(36.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}


@Preview(showBackground = true, heightDp = 2000)
@Composable
fun RegisterScreenPreview() {
    Dalia2Theme {
    }
}