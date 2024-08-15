package com.example.app_watch.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.app_watch.R
import com.example.app_watch.presentation.Models.ordenDetail
import com.example.app_watch.presentation.Models.visitDetail
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class visitDetailViewHolder(val orderId: String) : ViewModel() {
    val detailList = mutableStateListOf<visitDetail>()

    init {
        getVisitDetail()
    }

    private fun getVisitDetail() {
        viewModelScope.launch {
            println(orderId)
            val call = RetrofitClient.api.visitaDetail(orderId)
            call.enqueue(object : Callback<visitDetail> {
                override fun onResponse(call: Call<visitDetail>, response: Response<visitDetail>) {
                    if (response.isSuccessful) {
                        val detail = response.body()
                        detailList.clear()
                        if (detail != null) {
                            detailList.addAll(listOf(detail))
                        }
                    } else {
                        println("Error en la respuesta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<visitDetail>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}

class VisitDetailViewModelFactory(private val orderId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(visitDetailViewHolder::class.java)) {
            return visitDetailViewHolder(orderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Composable
fun visitScreen(navController: NavController, id: String){
    val viewModel: visitDetailViewHolder = viewModel(
        factory = VisitDetailViewModelFactory(id)
    )
    val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val targetTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val details = viewModel.detailList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = { navController.navigate("visitas") }, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.volver),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                "VISITA : $id",
                color = Color(0xFF4B4EA3),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Divider(
            color = Color(0xFF4B4EA3),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        LazyColumn {
            items(details) { detail ->
                val originalDateTime = try {
                    detail.fechaHoraSolicitud?.let {
                        LocalDateTime.parse(it, originalFormat)
                    }
                } catch (e: Exception) {
                    null
                }

                val originalDateTimeAtencion = try {
                    detail.fechaHoraLlegada?.let {
                        LocalDateTime.parse(it, originalFormat)
                    }
                } catch (e: Exception) {
                    null
                }

                val formattedDate = originalDateTime?.format(targetDateFormat) ?: "Fecha no disponible"
                val formattedTime = originalDateTime?.format(targetTimeFormat) ?: "Hora no disponible"
                val formattedTimeAtencion = originalDateTimeAtencion?.format(targetTimeFormat) ?: "Hora atención no disponible"

                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Folio: ${detail.id}", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Fecha: $formattedDate", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Hora: $formattedTime", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Motivo: ${detail.motivo}", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Dirección: ${detail.direccion}", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Cliente: ${detail.cliente.nombre}", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Técnico: ${detail.tecnico.nombre}", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Estado: ${detail.estatus}", color = Color.Black, fontSize = 11.sp)
                    Text(text = "Hora atención: $formattedTimeAtencion", color = Color.Black, fontSize = 11.sp)
                }
            }
        }

    }
}