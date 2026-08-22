package com.example.dalia2.ui.theme.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.SignupViewModel
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    onLoginClick: () -> Unit, // Parâmetro para navegar para login
    navController: NavController,
    onSignUpSuccess: () -> Unit // Parâmetro para após cadastro bem-sucedido
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passConfirmation by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Estados de erro
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.sigupSucess) {
        if (viewModel.sigupSucess) {
            val encodedEmail = URLEncoder.encode(email, "UTF-8")
            navController.navigate("verification/$encodedEmail")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(154.dp, 147.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Cadastro",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(25.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = "Já tem conta? Entrar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Campo Nome com validação
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    viewModel.onNameChanged(it)
                    if (it.isNotEmpty()) nameError = false
                },
                label = { Text("Nome") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Nome")
                },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text("Nome é obrigatório")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp)
            )

            // Campo Sobrenome
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    viewModel.onSurnameChanged(it)
                    if (it.isNotEmpty()) nameError = false
                },
                label = { Text("Sobrenome") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Sobrenome")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Campo Email com validação
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.onEmailChanged(it)
                    if (it.isNotEmpty()) emailError = false
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text("Email inválido")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Campo Senha com validação
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.onPasswordChanged(it)
                    if (it.length >= 8) passwordError = false
                },
                label = { Text("Senha") },
                isError = passwordError,
                supportingText = {
                    if (passwordError) {
                        Text("Mínimo 6 caracteres")
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = passConfirmation,
                onValueChange = {
                    passConfirmation = it
                    viewModel.onPassConfirmationChanged(it)
                    if (it.length >= 8) passwordError = false
                },
                label = { Text("Confirme a senha") },
                isError = passwordError,
                supportingText = {
                    if (passwordError) {
                        Text("Mínimo  caracteres")
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            //Mensagem se as senhas não baterem
            viewModel.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onRegistreClick(password, passConfirmation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                enabled = name.isNotEmpty() && lastName.isNotEmpty() &&
                        email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text(
                    text = "Criar conta",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = "Ao se cadastrar no Dália você estará aceitando nossos Termos & Condições",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            //Imagem de carregamento
            if (viewModel.isLoading) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PinkButton)
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    Dalia2Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignupScreen(
                viewModel = viewModel(),
                onLoginClick = {},
                navController = NavController(),
                onSignUpSuccess = {}
            )
        }
    }
}*/