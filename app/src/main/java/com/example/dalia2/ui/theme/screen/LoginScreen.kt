package com.example.dalia2.ui.theme.screen
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.LoginViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgetPassClick: () -> Unit = {}
) {
    // Variáveis para armazenar o que o usuário digita
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.loginSucess) {
        if(viewModel.loginSucess){
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Logo (ImageView)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(width = 154.dp, height = 147.dp),
                contentScale = ContentScale.Fit

            )

            Spacer(modifier = Modifier.height(25.dp))

            // 2. Título (TextView)
            Text(
                text = "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            // 3. Subtítulo
            TextButton(
                onClick = onSignUpClick,
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Não tem conta? Cadastrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = PinkButton
                )
            }

            // 4. Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.onEmailChanged(it)
                    if (it.isNotEmpty()) emailError = false
                },
                label = { Text("Email") },
                modifier = Modifier.width(304.dp),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Campo de Senha
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.onPasswordChanged(it)
                    if (it.length >= 8) passwordError = false
                },
                label = { Text("Senha") },
                modifier = Modifier.width(304.dp),
                shape = RoundedCornerShape(10.dp),
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

            // 6. Esqueceu a senha
            TextButton(
                onClick = { onForgetPassClick() },
                modifier = Modifier.align(Alignment.End).padding(end = 16.dp)
            ) {
                Text(
                    text = "Esqueceu a senha?",
                    color = PinkButton,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            viewModel.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 7. Botão Entrar
            Button(
                onClick = {
                    viewModel.onLoginClick(email, password)
                },
                modifier = Modifier.size(width = 304.dp, height = 44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Entrar", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 8. Botões Sociais (Row substitui o LinearLayout horizontal)
            /*Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Modificar
                Spacer(modifier = Modifier.width(width = 304.dp, height = 44.dp))
                SocialButton(R.drawable.google)
            }*/
        }
        if(viewModel.isLoading){
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

@Composable
fun SocialButton(iconRes: Int) {
    IconButton(
        onClick = { /* Ação */ },
        modifier = Modifier.size(60.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified // Mantém as cores originais do ícone
        )
    }
}

// 2. A função de PREVIEW (é isso que faz a mágica aparecer no Android Studio)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    Dalia2Theme {
        LoginScreen()
    }
}
