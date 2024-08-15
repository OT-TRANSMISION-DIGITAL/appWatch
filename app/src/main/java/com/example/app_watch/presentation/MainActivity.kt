/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.app_watch.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_watch.presentation.theme.App_watchTheme
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_watch.Singleton

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        loadData(this)

        Intent(this, PusherService::class.java).also { intent ->
            ContextCompat.startForegroundService(this, intent)
        }

        setContent {
            val lastScreen = loadLastScreen(this)
            WearApp(startDestination = lastScreen ?: "login")
        }
    }


    override fun onPause() {
        super.onPause()
        saveData(this)
        saveLastScreen(navController.currentDestination?.route, this)
    }


}

fun saveData(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Guardar el token
    editor.putString("token", Singleton.token)
    editor.putInt("rol", Singleton.rol)
    editor.putInt("user_id", Singleton.user_id)
    editor.putString("name", Singleton.name)

    editor.apply()
}

fun loadData(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    // Recuperar los datos del Singleton
    Singleton.token = sharedPreferences.getString("token", "") ?: ""
    Singleton.rol = sharedPreferences.getInt("rol", -1)
    Singleton.user_id = sharedPreferences.getInt("user_id", -1)
    Singleton.name = sharedPreferences.getString("name", "") ?: ""
}

fun saveLastScreen(screen: String?, context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("last_screen", screen)
    editor.apply()
}

fun loadLastScreen(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("last_screen", null)
}






@Composable
fun WearApp(startDestination: String) {
    val navController = rememberNavController()
    App_watchTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") { LoginScreen(navController) }
                composable("home") { HomeScreen(navController) }
                composable("schedule") { scheduleScreen(navController)}
                composable("ordenes") { OrdersScreen(navController) }
                composable("visitas") { VisitsScreen(navController) }
                composable("details/{id}") { backStackEntry ->
                    orderDetail(navController,backStackEntry.arguments?.getString("id") ?: "")
                }
                composable("visitDetails/{id}") { backStackEntry ->
                    visitScreen(navController,backStackEntry.arguments?.getString("id") ?: "")
                }
            }
        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(startDestination = "login")
}