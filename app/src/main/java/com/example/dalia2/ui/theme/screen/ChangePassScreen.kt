package com.example.dalia2.ui.theme.screen

import com.example.dalia2.ui.theme.viewmodel.LoginViewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.R
import com.example.dalia2.ui.theme.PinkButton
import androidx.compose.material3.Surface
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onChangeClick: () -> Unit,
) {
    var password by remember { mutableStateOf("") }
    var passConfirmation by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.changeSucess) {
        if (viewModel.changeSucess) {
            onChangeClick()
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
                text = "Nova Senha",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Campo Nome com validação
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
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
                    viewModel.onChangePass(password, passConfirmation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                enabled = password.isNotEmpty()
            ) {
                Text(
                    text = "Mudar senha",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

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

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {

}