package com.example.app_watch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import com.example.app_watch.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun scheduleScreen(navController: NavController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = { navController.navigate("home") }, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.volver),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                "AGENDA",
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

        Spacer(modifier = Modifier.height(1.dp))

        Text(text = "ORDENES", color = Color.Black, fontSize = 8.sp)

        Spacer(modifier = Modifier.height(1.dp))

        Button(
            onClick = { navController.navigate("ordenes") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6E6E95)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ordenes),
                contentDescription = "Icon",
                modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.height(1.dp))

        Text(text = "VISITAS", color = Color.Black, fontSize = 8.sp)

        Spacer(modifier = Modifier.height(1.dp))

        Button(
            onClick = { navController.navigate("visitas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.visitas),
                contentDescription = "Icon",
                modifier = Modifier.size(20.dp))
        }
    }

}

