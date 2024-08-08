package com.example.app_watch.presentation

import android.os.Bundle
import android.widget.Space
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.Agenda
import com.example.app_watch.presentation.Models.AgendaItem
import com.example.app_watch.presentation.Models.ApiResponse
import com.example.app_watch.presentation.Models.Tecnico
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class visitsViewModel : ViewModel() {
    private val _agendaList = mutableStateListOf<AgendaItem>()
    val agendaList: List<AgendaItem> by derivedStateOf { _agendaList }

    init {
        fetchOrdersData()
    }

    private fun fetchOrdersData() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = today.format(formatter)
        viewModelScope.launch {
            val call = RetrofitClient.api.agenda("visitas","")
            call.enqueue(object : Callback<Agenda> {
                override fun onResponse(call: Call<Agenda>, response: Response<Agenda>) {
                    if (response.isSuccessful) {
                        val agenda = response.body()
                        _agendaList.clear()
                        if (agenda != null) {
                            _agendaList.addAll(agenda)
                        }
                    } else {
                        println("Error en la respuesta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Agenda>, t: Throwable) {
                    println("Error en la solicitud: ${t.message}")
                }
            })
        }
    }
}

@Composable
fun VisitsScreen(navController: NavController,VisitsViewModel: visitsViewModel = viewModel()) {

    val agendaList = remember { VisitsViewModel.agendaList }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = { navController.navigate("schedule") }, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.volver),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                "VISITAS",
                color = Color(0xFF4B4EA3),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }
        Divider(
            color = Color(0xFF4B4EA3),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        LazyColumn {
            items(agendaList) { solicitud ->
                println(solicitud)
                Column (modifier = Modifier
                    .clickable {
                        navController.navigate("visitDetails/${solicitud.id}")
                    }
                ){
                    visitView(solicitud = solicitud)
                    Divider(
                        color = Color(0xFF4B4EA3),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun visitView(solicitud: AgendaItem) {
    val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val targetTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val originalDateTime = LocalDateTime.parse(solicitud.fechaHoraSolicitud, originalFormat)

    val formattedDate = originalDateTime.format(targetDateFormat)
    val formattedTime = originalDateTime.format(targetTimeFormat)
    Column(modifier = Modifier.padding(16.dp)) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Folio: ${solicitud.id}",
                color = Color.Black,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))

            Text(
                text = "Fecha: ${formattedDate}",
                color = Color.Black,
                fontSize = 11.sp
            )
        }
        Text(text = "Hora: ${formattedTime}", color = Color.Black)
        Text(text = "Estatus: ${solicitud.estatus}", color = Color.Black)
    }
}