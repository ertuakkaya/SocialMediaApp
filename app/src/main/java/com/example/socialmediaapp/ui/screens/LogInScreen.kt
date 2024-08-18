package com.example.socialmediaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.util.AuthState
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel

//@Preview
@Composable
fun LoginScreen(navController: NavHostController,firebaseViewModel: FirebaseViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    val authState = firebaseViewModel.authState.observeAsState()
    val context = LocalContext.current


    // Launch effect, bu sayfa açıldığında çalışacak
    LaunchedEffect(authState.value) {
        when(authState.value){

            is AuthState.Authenticated -> navController.navigate(Screen.HomeScreen)
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Surface (
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFF4B67D0),


        ){
        Column (
            modifier = Modifier
                .padding(18.dp),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Connected",
                fontSize = 32.sp,
                color = Color(0xFFE0E0E0),
                modifier = Modifier
                    .padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your favorite social network",
                fontSize = 16.sp,
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        Surface (
            modifier = Modifier
                .padding(top = 128.dp)
                .fillMaxSize(),
            color = Color(0xFFE0E0E0),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ){



            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .background(Color(0xFFE0E0E0)),

                verticalArrangement = Arrangement.SpaceEvenly,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Log in
                Text(
                    text = "Log in",
                    fontSize = 36.sp,
                    color = Color(0xFF030303),
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,

                    )

                Column (

                ){
                    Text(
                        text = "Username",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),

                        )

                }

                Column (

                ){
                    Row(){
                        Text(
                            text = "Password",
                            fontSize = 16.sp,
                            color = Color(0xFF030303),
                            modifier = Modifier

                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Forgot your password?",
                            color = Color(0xFF3F51B5),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()

                        )
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                }





                Button(
                    onClick = {
                        // firebase login
                        firebaseViewModel.login(email, password)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp, 50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3F51B5))

                ) {
                    Text("Log in")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        navController.navigate(Screen.SignupScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),

                    ){
                    Text("Don't have an account? Sign up")
                }
            }
        }

    }
}