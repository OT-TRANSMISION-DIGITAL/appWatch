package com.example.app_watch.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.geometry.times
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_watch.presentation.Models.ordenDetail
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.times


class orderDetailViewHolder(val orderId: String) : ViewModel() {
    private val _detailList = mutableStateListOf<ordenDetail>()
    val detailList: List<ordenDetail> by derivedStateOf { _detailList }

    init {
        getOrderDetail()
    }

    private fun getOrderDetail() {
        viewModelScope.launch {
            println(orderId)
            val call = RetrofitClient.api.ordenDetail(orderId)
            call.enqueue(object : Callback<ordenDetail> {
                override fun onResponse(call: Call<ordenDetail>, response: Response<ordenDetail>) {
                    if (response.isSuccessful) {
                        val detail = response.body()
                        _detailList.clear()
                        if (detail != null) {
                            _detailList.addAll(listOf(detail))
                        }
                    } else {
                        println("Error en la respuesta: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ordenDetail>, t: Throwable) {
                    println("Error en la llamada: ${t.message}")
                }
            })
        }
    }
}

class OrderDetailViewModelFactory(private val orderId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(orderDetailViewHolder::class.java)) {
            return orderDetailViewHolder(orderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun orderDetail(navController: NavController, id: String) {

    val viewModel: orderDetailViewHolder = viewModel(
        factory = OrderDetailViewModelFactory(id)
    )
    val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val targetTimeFormat = DateTimeFormatter.ofPattern("HH:mm")
    val details = viewModel.detailList
    var subtotal : Double
    var total: Double = 0.0

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
            Button(onClick = { navController.navigate("ordenes") }, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.volver),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                "ORDEN : $id",
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
            items(details) { details ->
                val originalDateTime = LocalDateTime.parse(details.fechaHoraSolicitud, originalFormat)
                val formattedDate = originalDateTime.format(targetDateFormat)
                val formattedTime = originalDateTime.format(targetTimeFormat)
                Column (modifier = Modifier.padding(16.dp),horizontalAlignment = Alignment.Start) {
                    Column (        
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                            Text(text = "Folio: ${details.id}", color = Color.Black,fontWeight = FontWeight.Bold )
                    }
                    Text(text = "Fecha: ${formattedDate}", color = Color.Black, fontSize = 11.sp )
                    Text(text = "Hora: ${formattedTime}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Dirección: ${details.direccion}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Cliente: ${details.cliente.nombre}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Tecnico: ${details.tecnico.nombre}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Persona solicitante: ${details.persona_solicitante}", color = Color.Black, fontSize = 10.sp  )
                    Text(text = "Puesto: ${details.puesto}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Estado: ${details.estatus}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Hora llegada: ${details.fechaHoraLlegada}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Hora salida: ${details.fechaHoraSalida}", color = Color.Black, fontSize = 11.sp  )
                    Text(text = "Servicios:", color = Color.Black, fontSize = 12.sp,fontWeight = FontWeight.Bold)
                    Divider(
                        color = Color(0xFF4B4EA3),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    details.detalles.forEach{detail ->
                        Text(text = "Descripcion: ${detail.descripcion}",color = Color.Black, fontSize = 11.sp )
                        Text(text = "Cantidad: ${detail.cantidad}", color = Color.Black, fontSize = 11.sp )
                        Text(text = "Orden: ${detail.orden_id}", color = Color.Black, fontSize = 11.sp )
                        Text(text = "Producto: ${detail.producto.nombre}", color = Color.Black, fontSize = 11.sp )
                        Text(text = "Precio: ${detail.producto.precio}", color = Color.Black, fontSize = 11.sp )
                        Divider(
                            color = Color(0xFF4B4EA3),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        subtotal = detail.cantidad * detail.producto.precio.toDouble()
                        total = total + subtotal
                    }
                    Text(text = "Total:$${total}", color = Color.Black, fontSize = 15.sp,fontWeight = FontWeight.Bold)
                }
            }

        }

    }
}