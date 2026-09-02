package com.example.dalia2.ui.theme.screen


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.verticalScroll
import com.example.dalia2.R
import com.example.dalia2.data.model.AppMode
import com.example.dalia2.data.model.SearchData
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.GrayButton
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.LightPink
import com.example.dalia2.ui.theme.viewmodel.ProfileViewModel
data class LanguageOption(
    val code: String?,
    val name: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    //Variaveis para mudar de tela
    onEditarClick: () -> Unit = {},
    onInformationClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onChangeModeClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val state = viewModel._uiState

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }


    val nomeCompleto = "${state?.user?.name ?: ""} ${state?.user?.surname ?: ""}".trim()
    val telefoneUsuario =
        state?.user?.email ?: "Não informado" // Adapte para state?.user?.telefone se houver

    val currentMode = state?.currentMode ?: AppMode.MENSTRUACAO //[cite: 3, 4]
    val isModoGravidez = currentMode == AppMode.GRAVIDEZ
    var showModeDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            fun sendMsg() {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            val enderecoCompleto = if (!addresses.isNullOrEmpty()) {
                                addresses[0].getAddressLine(0)
                            } else {
                                "Latiude: ${location.latitude}, Longitude: ${location.longitude}"
                            }

                            val mensagemFinal =
                                "Usuaria $nomeCompleto, numero: $telefoneUsuario, está em: $enderecoCompleto"

                            viewModel.enviarDenuncia(mensagemFinal) {
                                Toast.makeText(context, "Ajuda solicitada", Toast.LENGTH_LONG)
                                    .show()
                                onBackClick()
                            }
                        } else {
                            Toast.makeText(context, "Permissão negada", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Permissão negada", Toast.LENGTH_LONG).show()
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Perfil",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            );

            Spacer(modifier = Modifier.height(20.dp))

            //Imagem do perfil
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

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)

            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 8.dp)

                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Informações",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        );

                        Button(
                            onClick = {
                                Log.d("TESTE", "botão editar clicado")
                                onEditarClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
                        ) {
                            Text("Editar", color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    InfoSection(
                        label = "Nome",
                        value = "${state?.user?.name} ${state?.user?.surname}".ifBlank { "Carregando..." }
                    );

                    InfoSection(
                        label = "Email",
                        value = state?.user?.email?.ifBlank { "Carregando..." }
                            ?: ""  //dado do banco de dados
                    );

                    InfoSection(
                        label = "Idade",
                        value = state?.search?.age.toString()
                    );

                    InfoSection(
                        label = "Modo Atual",
                        value = if (isModoGravidez) "Gravidez" else "Menstruação"
                    )

                    if (isModoGravidez) {
                        // Exibe apenas se estiver no modo gravidez
                        InfoSection(
                            label = "Semanas de Gestação",
                            value = "${state?.pregnancyMonitoring?.gestationWeeks ?: 0} semanas"
                        )
                        InfoSection(
                            label = "Previsão do Parto",
                            value = state?.pregnancyMonitoring?.expectedBirthDate ?: "Não informada"
                        )
                    } else {
                        InfoSection(
                            label = "Anticoncepcional",
                            value = if (state?.search?.useContraceptive == true) "Sim" else "Não"
                        )
                        if (state?.search?.useContraceptive == true) {
                            InfoSection(
                                label = "Tipo de Anticoncepcional",
                                value = state?.search?.contraceptiveType ?: ""
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Seção de configurações
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {

                    SettingsButton(
                        text = "Informações pessoais",
                        icon = R.drawable.person,
                        onClick = onInformationClick,
                        backgroundColor = LightPink

                    )

                    SettingsButton(
                        text = "Ajuda",
                        icon = R.drawable.search_icon,
                        onClick = onHelpClick,
                        backgroundColor = LightPink
                    )

                    SettingsButton(
                        text = if(isModoGravidez) "Mudar para modo menstruação" else "Mudar para modo gravidez",
                        icon = R.drawable.pop_pregnance,
                        onClick = { showModeDialog = true },
                        backgroundColor = LightPink
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Zona de alerta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "Zona de perigo!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold

                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    SettingsButton(
                        text = "Denunciar",
                        icon = R.drawable.alert,
                        backgroundColor = PinkButton,
                        onClick = { showReportDialog = true },
                    )

                    Button(
                        onClick = { /* Excluir conta */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
                    ) {
                        Text("Sair da conta")
                    }
                }
            }

            //Pop-up - Mudar de modo
            if (showModeDialog) {
                AlertDialog(
                    onDismissRequest = { showModeDialog = false },
                    title = { Text("Trocar de Modo") },
                    text = {
                        Text(
                            if (isModoGravidez)
                                "Deseja encerrar o acompanhamento de gestação e voltar para o ciclo menstrual?"
                            else
                                "Você está prestes a mudar para o modo gravidez!"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showModeDialog = false
                            if(isModoGravidez) {
                                val searchAtual = state?.search ?: SearchData(age=18,useContraceptive = false,contraceptiveType = null)
                                viewModel.retornaMenstrucao(searchAtual) {
                                    onBackClick()
                                }
                            }else{
                            onChangeModeClick()
                            }
                        }){
                            Text("Confirmar", color = PinkButton, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showModeDialog = false }) {
                            Text("Cancelar", color = Color.Gray)
                        }
                    }
                )
            }

            //Pop-up - Denunciar
            if (showReportDialog) {
                AlertDialog(
                    onDismissRequest = { showReportDialog = false },
                    title = { Text("Atenção") },
                    text = {
                        Text("Você tem certeza que gostaria de realizar a denúncia?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showReportDialog = false
                            Log.d("TESTE", "botão confirmar clicado")
                            sendMsg()
                        }) {
                            Text("Sim", color = PinkButton, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showReportDialog = false }) {
                            Text("Não", color = Color.Gray)
                        }
                    }
                )
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoSection(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = GrayButton,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun SettingsButton(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    textColor: Color = Color.Black,
    backgroundColor: Color = Color.Transparent
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileScreenPreview() {
    Dalia2Theme {
        //ProfileScreen()
    }
}
