package com.example.app_watch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.app_watch.R
import com.example.app_watch.presentation.theme.App_watchTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.LocalTextStyle
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.AgendaItem
import com.example.app_watch.presentation.Models.ApiResponse
import com.example.app_watch.presentation.Models.Tecnico
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun scheduleScreen(solicitudes: List<AgendaItem>) {
    LazyColumn {
        items(solicitudes) { solicitud ->
            SolicitudView(solicitud = solicitud)
        }
    }
}
@Composable
fun TecnicoView(tecnico: Tecnico) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Nombre: ${tecnico.nombre}", style = MaterialTheme.typography.body1)
        Text(text = "ID: ${tecnico.id}", style = MaterialTheme.typography.body2)
    }
}

@Composable
fun SolicitudView(solicitud: AgendaItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Fecha y Hora: ${solicitud.fechaHoraSolicitud}",
            style = MaterialTheme.typography.body1
        )
        Text(text = "Estatus: ${solicitud.estatus}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        TecnicoView(tecnico = solicitud.tecnico)
    }
}
