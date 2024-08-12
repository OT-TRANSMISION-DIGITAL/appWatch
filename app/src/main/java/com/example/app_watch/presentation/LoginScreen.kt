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
import com.example.app_watch.presentation.Models.ApiResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController) {
    var codigo by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logola),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .size(50.dp)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = codigo,
            onValueChange = {
                codigo = it
                if (it.isNotEmpty()) {
                    errorMessage = null
                }
            },
            label = { Text("Codigo", color = Color(0xFF4B4EA3)) },
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4B4EA3),
                unfocusedBorderColor = Color(0xFF4B4EA3),
                focusedLabelColor = Color(0xFF4B4EA3),
                unfocusedLabelColor = Color(0xFF8A8A8A),
                cursorColor = Color(0xFF4B4EA3),
            ),
            textStyle = LocalTextStyle.current.copy(color = Color(0xFF4B4EA3))
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (codigo.isEmpty()) {
                    errorMessage = "El campo no puede estar vacío"
                } else {
                    coroutineScope.launch {
                        validateCodigo(codigo) { success ->
                            if (success) {
                                navController.navigate("home")
                            } else {
                                errorMessage = "Código inválido"
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4B4EA3)
            )
        ) {
            Text(
                stringResource(id = R.string.login),
                color = Color.White
            )
        }
    }
}

fun validateCodigo(codigo: String, callback: (Boolean) -> Unit) {
    val requestBody = RequestBody.create(
        "application/json".toMediaTypeOrNull(),
        """{"codigo":"$codigo"}"""
    )
    val call = RetrofitClient.api.validateCode(requestBody)
    call.enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                val apiResponse = response.body()
                Singleton.token = apiResponse?.token ?: ""
                Singleton.rol = apiResponse?.usuario?.rol_id ?: 0
                Singleton.user_id = apiResponse?.usuario?.id ?: 0
                Singleton.name = apiResponse?.usuario?.nombre ?: ""
                callback(true)
            } else {
                println("Error: ${response.errorBody()}")
                callback(false)
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            println("Failure: ${t.message}")
            callback(false)
        }
    })
}
