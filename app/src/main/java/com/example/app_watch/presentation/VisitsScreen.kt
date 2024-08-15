package com.example.app_watch.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import com.example.app_watch.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.material.Button
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.AgendaAdmin
import com.example.app_watch.presentation.Models.AgendaItemAdmin
import com.example.app_watch.presentation.Models.AgendaTecnico
import com.example.app_watch.presentation.Models.AgendaTecnicoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class visitsViewModel : ViewModel() {
    val agendaList = mutableStateListOf<AgendaItemAdmin>()
    val agendaTecnicoList = mutableStateListOf<AgendaTecnicoItem>()
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = today.format(formatter)

    init {
        fetchOrdersData()
    }

    private fun fetchOrdersData() {
        viewModelScope.launch {
            println("rol del usuario - ${Singleton.rol}")
            when(Singleton.rol){
                1 -> {
                    val callAgenda = RetrofitClient.api.agenda("visitas","")
                    callAgenda.enqueue(object : Callback<AgendaAdmin> {
                        override fun onResponse(call: Call<AgendaAdmin>, response: Response<AgendaAdmin>) {
                            if (response.isSuccessful) {
                                val agenda = response.body()
                                println(response.body())
                                agendaList.clear()
                                if (agenda != null) {
                                    agendaList.addAll(agenda)
                                }
                            } else {
                                println("Error en la respuesta: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<AgendaAdmin>, t: Throwable) {
                            println("Error en la solicitud: ${t.message}")
                        }
                    })
                }
                else -> {
                    val callAgendaTecnico = RetrofitClient.api.agendaTecnico("visitas","${Singleton.user_id}")
                    callAgendaTecnico.enqueue(object : Callback<AgendaTecnico> {
                        override fun onResponse(call: Call<AgendaTecnico>, response: Response<AgendaTecnico>) {
                            if (response.isSuccessful) {
                                val agenda = response.body()
                                agendaTecnicoList.clear()
                                println(response.body())
                                if (agenda != null) {
                                    agendaTecnicoList.addAll(agenda)
                                }
                            } else {
                                println("Error en la respuesta: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<AgendaTecnico>, t: Throwable) {
                            println("Error en la solicitud: ${t.message}")
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun VisitsScreen(navController: NavController,VisitsViewModel: visitsViewModel = viewModel()) {

    val agendaList = remember { VisitsViewModel.agendaList }
    val agendaTecnicoList = remember {VisitsViewModel.agendaTecnicoList}

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

        when(Singleton.rol) {

            1 -> {
                LazyColumn {
                    items(agendaList) { agenda ->
                        Column (modifier = Modifier
                            .clickable {
                                agenda.id?.let {
                                    navController.navigate("visitDetails/$it")
                                } ?: run {
                                    println("ID es nulo")
                                }
                            }
                        ){
                            visitsView(agenda = agenda)
                            Divider(
                                color = Color(0xFF4B4EA3),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            else -> {
                LazyColumn {
                    items(agendaTecnicoList) { agenda ->
                        Column (modifier = Modifier
                            .clickable {
                                navController.navigate("visitDetails/${agenda.id}")
                            }
                        ){
                            visitTecnicoView(agenda = agenda)
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
    }
}

@Composable
fun visitsView(agenda: AgendaItemAdmin) {
    val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val targetTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val originalDateTime = LocalDateTime.parse(agenda.fechaHoraSolicitud, originalFormat)

    val formattedDate = originalDateTime.format(targetDateFormat)
    val formattedTime = originalDateTime.format(targetTimeFormat)
    Column(modifier = Modifier.padding(16.dp)) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Folio: ${agenda.id}",
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
        Text(text = "Estatus: ${agenda.estatus}", color = Color.Black)
    }
}

@Composable
fun visitTecnicoView(agenda: AgendaTecnicoItem) {
    val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val targetTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val originalDateTime = LocalDateTime.parse(agenda.fechaHoraSolicitud, originalFormat)

    val formattedDate = originalDateTime.format(targetDateFormat)
    val formattedTime = originalDateTime.format(targetTimeFormat)
    Column(modifier = Modifier.padding(16.dp)) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Folio: ${agenda.id}",
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
        Text(text = "Estatus: ${agenda.estatus}", color = Color.Black)
    }
}