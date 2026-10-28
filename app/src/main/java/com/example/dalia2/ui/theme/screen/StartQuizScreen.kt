package com.example.dalia2.ui.theme.screen

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.permissions.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton


@OptIn(ExperimentalPermissionsApi::class) //Permite utilizar a biblioteca de gerenciamento de permições
@Composable
fun  StartQuizScreen (
    onStartClick: () -> Unit = {}
){

    val foregroundPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val backgroundPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    DisposableEffect(Unit) {
        println(">>> quiz CRIADA")
        Log.d("Nav","quiz screen criada")
        onDispose {
            println(">>> quiz DESTRUÍDA")
            Log.d("Nav","quiz screen destruida")
        }
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(80.dp)) // Aproximação do vertical_bias

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo do app",
                modifier = Modifier.size(154.dp, 147.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Pesquisa",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold, // fontFamily="@font/inter_semibold"
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Para continuar responda uma breve pesquisa para sabermos seu perfil",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium, // fontFamily="@font/inter_semibold"
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            //Botão do pop-up
            Button(
                onClick = {
                    if (!foregroundPermissionState.allPermissionsGranted) { //Permissão durante uso do app
                        foregroundPermissionState.launchMultiplePermissionRequest() //Abre o pop-up
                    } else if (!backgroundPermissionState.status.isGranted) { //Pede a permissão de uso o tempo todo
                        backgroundPermissionState.launchPermissionRequest()
                    } else {
                        onStartClick() //Inicia o quiz
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkButton
                )
            ) {
                Text(
                    text = "Começar",
                    fontSize = 16.sp
                )
            }

            //Pop-up
            if (foregroundPermissionState.shouldShowRationale || backgroundPermissionState.status.shouldShowRationale) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Acesso à Localização") },
                    text = { Text("Este app precisa da sua localização o tempo todo para te rastrear caso voce esteja em perigo. Por favor, selecione 'Permitir o tempo todo' nas configurações.") },
                    confirmButton = {
                        TextButton(onClick = {
                            if (!foregroundPermissionState.allPermissionsGranted) {
                                foregroundPermissionState.launchMultiplePermissionRequest()
                            } else {
                                backgroundPermissionState.launchPermissionRequest()
                            }
                        }) {
                            Text("Entendi")
                        }
                    }
                )
            }
    }
}

@Preview(showBackground = true)
@Composable
fun StartQuizScreenPreview() {
    Dalia2Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StartQuizScreen()
        }
    }
}