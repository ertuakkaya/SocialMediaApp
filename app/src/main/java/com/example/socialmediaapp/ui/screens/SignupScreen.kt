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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.ui.viewmodels.AuthState
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel

//@Preview
@Composable
fun SignupScreen(navController: NavHostController, firebaseViewModel: FirebaseViewModel) {

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var termsAndConditions by remember { mutableStateOf(false) }


    val scrollState = rememberScrollState()


    val authState = firebaseViewModel.authState.observeAsState()
    val context = LocalContext.current


    // Based on the auth state value, navigate to the login screen or show an error message
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate(Screen.LoginScreen) // navigate to login
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

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
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),

                verticalArrangement = Arrangement.SpaceBetween,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Create An Account Text
                Text(
                    text = "Create an account",
                    fontSize = 36.sp,
                    color = Color(0xFF030303),
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,

                    )

                Spacer(modifier = Modifier.height(32.dp))

                // Full name TextField
                Column (

                ){
                    Text(
                        text = "Full name *",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Name Surname") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),

                        )

                }

                Spacer(modifier = Modifier.height(32.dp))

                // Username TextField
                Column (

                ){

                    Text(
                        text = "Username *",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )


                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        //visualTransformation = PasswordVisualTransformation(),
                        //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))

                // Email TextField
                Column (

                ){

                    Text(
                        text = "E-mail *",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("email@email.com") },
                        //visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))

                // Password TextField
                Column (

                ){

                    Text(
                        text = "Password *",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )


                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("**********") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))

                // Repeat Password TextField
                Column (

                ){

                    Text(
                        text = "Repeat Password *",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier

                    )


                    OutlinedTextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = { Text("**********") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                }


                Spacer(modifier = Modifier.height(32.dp))

                // Terms of conditions checkbox and text
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Checkbox(
                        checked = termsAndConditions,
                        onCheckedChange = {
                            termsAndConditions = it
                        },
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(100.dp, 100.dp, 100.dp, 100.dp )),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF3F51B5),
                            uncheckedColor = Color(0xFF3F51B5)
                        )

                    )

                    Text(
                        text = "I agree to the terms of conditions",
                        fontSize = 16.sp,
                        color = Color(0xFF030303),
                        modifier = Modifier
                            .padding(start = 16.dp)

                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    //Checkbox()
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (password != repeatPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }else if(!termsAndConditions){

                            Toast.makeText(context, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show()
                            return@Button

                        }else if(fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }else if(!email.contains("@") || !email.contains(".")){
                            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        else if(password.length < 6){
                            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        else{
                            firebaseViewModel.signUp(email = email, password = password, name = fullName, userName = username)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp, 50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3F51B5))

                ) {
                    Text("Sign up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        navController.navigate(Screen.LoginScreen)
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally),


                    ){
                    Text(
                        text = "Already have an account? Log in",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        )
                }
            }
        }

    }


}