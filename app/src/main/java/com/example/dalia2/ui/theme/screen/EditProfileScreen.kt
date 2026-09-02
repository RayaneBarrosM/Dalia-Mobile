package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.R
import com.example.dalia2.data.model.ProfileRequest
import com.example.dalia2.data.model.SearchData
import com.example.dalia2.data.model.UserRequest
import com.example.dalia2.ui.theme.Black
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.White
import com.example.dalia2.ui.theme.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit = {}
) {
    val state =viewModel._uiState

    // Estados editáveis
    var email by remember { mutableStateOf(state?.user?.email) }
    var senha by remember { mutableStateOf("") } // Nova senha (opcional)
    var idade by remember { mutableStateOf(state?.search?.age?.toString()?: "") }
    var usaMetodo by remember { mutableStateOf(state?.search?.useContraceptive ?: false) }
    var tipoMetodo by remember { mutableStateOf(state?.search?.contraceptiveType ?: "") }
    var phone by remember { mutableStateOf("(11) 99999-9999") } // Local

    val opcoesAnticoncepcional = listOf("Pílula", "DIU", "Injeção", "Implante", "Apenas Preservativo")
    var expanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    // Estados de erro
    var emailError by remember { mutableStateOf<String?>(null) }

    // Função de validação

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.White
                )

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFD286C9),
                            Color(0xFFF8ADCD),
                            Color(0xFFF18FB8)
                        )
                    )
                )
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Imagem do perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão para trocar foto (opcional)
            TextButton(
                onClick = { /* Implementar troca de foto */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Alterar foto",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card de informações
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Informações Pessoais",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // NOME
                    OutlinedTextField(
                        value = "${state?.user?.name} ${state?.user?.surname}",
                        onValueChange = {},
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,  // Desabilitado
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.DarkGray,
                            disabledLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // IDADE
                    OutlinedTextField(
                        value = idade,
                        onValueChange = {if (it.all { c -> c.isDigit() }) idade = it},
                        label = { Text("Idade") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.DarkGray,
                            disabledLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // EMAIL - mudavel
                    OutlinedTextField(
                        value = email?: "",
                        onValueChange = {
                            email = it
                        },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError != null,
                        supportingText = {
                            if (emailError != null) {
                                Text(
                                    text = emailError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = senha,
                        onValueChange = {senha = it},
                        label = { Text("Senha") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError != null,
                        supportingText = {
                            if (emailError != null) {
                                Text(
                                    text = emailError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Usa método contraceptivo?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Opção SIM
                        RadioButton(
                            selected = usaMetodo,
                            onClick = { usaMetodo = true },
                            colors = RadioButtonDefaults.colors(selectedColor = PinkButton)
                        )
                        Text("Sim", modifier = Modifier.clickable { usaMetodo = true })

                        Spacer(modifier = Modifier.width(24.dp))

                        // Opção NÃO
                        RadioButton(
                            selected = !usaMetodo,
                            onClick = {
                                usaMetodo = false
                                tipoMetodo = "" // Limpa o tipo se marcar Não
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = PinkButton)
                        )
                        Text("Não", modifier = Modifier.clickable { usaMetodo = false })
                    }

// APARECE APENAS SE FOR "SIM"
                    if (usaMetodo) {
                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = tipoMetodo,
                                onValueChange = {},
                                readOnly = true, // Usuária não digita, apenas seleciona
                                label = { Text("Tipo de anticoncepcional") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = PinkButton,
                                    focusedLabelColor = PinkButton
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                opcoesAnticoncepcional.forEach { opcao ->
                                    DropdownMenuItem(
                                        text = { Text(opcao) },
                                        onClick = {
                                            tipoMetodo = opcao
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // TELEFONE
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Telefone") },
                        placeholder = { Text("(00) 00000-0000") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Informação sobre campos bloqueados
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.alert),
                            contentDescription = "Info",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nome não pode ser alterado",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Button(
                        onClick = {
                            var search = SearchData( // Supondo que UserRegistre tenha um campo search
                                age = idade.toIntOrNull() ?: 0,
                                useContraceptive = usaMetodo,
                                contraceptiveType = if (usaMetodo) tipoMetodo else null
                            )

                            val dadosParaAtualizar = ProfileRequest(
                                name = state?.user?.name ?: "",
                                surname = state?.user?.surname ?: "",
                                email = email?:"",
                                password = if (senha.isBlank()) null else senha,
                                search = search,
                                pregnancyMonitoring = null
                            )

                            // Agora passamos o objeto e a função de sucesso
                            viewModel.updateUserProfile(
                                userRegistre = dadosParaAtualizar,
                                onSuccess = {
                                    onBackClick()
                                }
                            )
                        },
                        modifier = Modifier.size(width = 304.dp, height = 44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Salvar", fontSize = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    Dalia2Theme {
        EditProfileScreen(
            initialName = "Usuária Teste",
            initialEmail = "usuaria@email.com",
            initialAge = "19",
            initialPhone = "(00) 0000-0000"
        )
    }
}*/