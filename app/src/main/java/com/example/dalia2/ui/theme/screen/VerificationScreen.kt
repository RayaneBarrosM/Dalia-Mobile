package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.VerificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    email: String,
    viewModel: VerificationViewModel = hiltViewModel(),
    isPasswordReset: Boolean = false,
    onVerificationSucess: () -> Unit,
    onBackClick: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    var tokenError by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.verificationSucess) {
        if(viewModel.verificationSucess){
            viewModel.resetState()
            onVerificationSucess()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isPasswordReset) "Recuperação de Palavra-passe" else "Verifique o seu E-mail",                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Start
            )
            Text("Enviamos um codigo de verificação para você.")

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = token,
                onValueChange = { input ->
                    if(input.length <= 6){
                        token = input
                        viewModel.onTokenChanged(input)
                        tokenError = input.length != 6

                    }
                },
                label = { Text("Código de verificação") },
                isError = tokenError,
                supportingText = {
                    if (tokenError) {
                        Text("Código inválido")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            viewModel.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            TextButton(
                onClick = {  },
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = "Reenviar Token",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = {
                    if (isPasswordReset) {
                        viewModel.onVerifyToken(email, token)
                    } else {
                        viewModel.onVerifyClick(email, token)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verificar Código")
            }
        }

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
@Preview(showBackground = true)
@Composable
fun VerificationScreenPreview() {
    Dalia2Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

        }
    }
}