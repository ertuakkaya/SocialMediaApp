package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Components(){

    //




}

@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(50.dp))
                Text(
                    text = "Connected",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications, // Icon için uygun bir image vector seçin
                        contentDescription = "Logout",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },// Top bar
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // Icon için uygun bir image vector seçin
                        contentDescription = "Logout",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Your favorite social network",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Box(modifier = Modifier.size(50.dp)
                )
            }
        },// Bottom bar

    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Surface (
        color = Color(0xFFFFFFFF),
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(10) {
                Post()
            }

        }
    }
}

@Composable
@Preview
fun Post(){

    Card(
        onClick = {
            /*TODO*/
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            //.background(Color(0xFFFEFEFE))
            .background(Color.Black)
            ,
        shape = RoundedCornerShape(100.dp, 100.dp, 100.dp, 100.dp),
        elevation = CardDefaults.cardElevation(8.dp),


        //colors = CardDefaults.cardColors(Color(0xFFFFFFFF))





    ) {
        
    }

}



