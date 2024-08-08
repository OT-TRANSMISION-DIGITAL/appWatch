package com.example.app_watch.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import com.example.app_watch.R
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
import com.example.app_watch.presentation.Models.Agenda
import com.example.app_watch.presentation.Models.AgendaItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderViewModel : ViewModel() {
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
            val call = RetrofitClient.api.agenda("ordenes","")
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
fun OrdersScreen(navController: NavController,ordersViewModel: OrderViewModel = viewModel()) {

    val agendaList = remember { ordersViewModel.agendaList }

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
                "ORDENES",
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
                Column(modifier = Modifier
                    .clickable {
                        navController.navigate("details/${solicitud.id}")
                    }
                ) {
                    SolicitudView(solicitud = solicitud)
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
fun SolicitudView(solicitud: AgendaItem) {
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