package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.util.AuthState
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import com.example.socialmediaapp.util.uploadProfileImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.CreditCard
import compose.icons.feathericons.Key
import compose.icons.feathericons.Settings
import compose.icons.feathericons.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    user: User,
    firebaseViewModel: FirebaseViewModel,
    navHostController: NavHostController,
    firestoreViewModel: FirestoreViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
) {


    val authState = firebaseViewModel.authState.observeAsState()
    val context = LocalContext.current


    // Launch effect, bu sayfa açıldığında çalışacak
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navHostController.navigate(Screen.LoginScreen)
            is AuthState.Authenticated -> Unit
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    //val user by firestoreViewModel.getUserFromFirestore(currentUserID).observeAsState(null)



    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier,

        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Profile",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        fontWeight = FontWeight.Medium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },

                scrollBehavior = scrollBehavior
            )
        },


        bottomBar = {
            BottomBarComponent(navController = navHostController)

        },// Bottom bar

    ) { innerPadding ->




        val currentUserID = firebaseViewModel.getCurrentUser()?.uid ?: ""

        val userData by firestoreViewModel.userData.collectAsState()
        val isLoading by firestoreViewModel.isLoading.collectAsState()
        val error by firestoreViewModel.error.collectAsState()

        LaunchedEffect(currentUserID) {
            firestoreViewModel.getUserFromFirestore(currentUserID)
        }

        when {
            isLoading -> LoadingIndicator()
            error != null -> ErrorMessage(error!!)
            userData != null ->
                AccountScreenBodyContent(
                    userData!!,
                    Modifier.padding(innerPadding),
                    onSignOut = { firebaseViewModel.signOut() },
                    firestoreViewModel = firestoreViewModel,
                    firebaseViewModel = firebaseViewModel,
                    userViewModel = userViewModel,
                    navController = navHostController
                )
            else -> Text("No user data available")
        }


    }



}




@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun ErrorMessage(error: String) {
    Text(text = error, color = Color.Red)
}

@Composable
fun UserProfile(user: User) {
    Column {
        Text("Username: ${user.userName ?: "N/A"}")
        Text("Email: ${user.email}")
        Text(text = "Name: ${user.name ?: "N/A"}")
        Text(text = "Profile Image URL: ${user.profileImageUrl ?: "N/A"}")
        Text(text = "Bio: ${user.userID ?: "N/A"}")
        // You can add AsyncImage for profile picture if needed
    }
}


@Composable
fun AccountScreenBodyContent(
    user: User?,
    modifier: Modifier,
    onSignOut: () -> Unit,
    firestoreViewModel : FirestoreViewModel,
    firebaseViewModel: FirebaseViewModel,
    userViewModel: UserViewModel,
    navController: NavController





){


    var currentUser by remember {
        mutableStateOf(user)
    }

    var isUploading by remember {
        mutableStateOf(false)
    }

    var userProfileImageUrl by remember {
        mutableStateOf<String>(user?.profileImageUrl ?: "")
    }

    /// Image Picker
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val coroutineScope = rememberCoroutineScope()




    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { selectedUri ->
                isUploading = true
                coroutineScope.launch {
                    val updatedUser = uploadProfileImage(selectedUri, currentUser, userViewModel, firestoreViewModel)
                    currentUser = updatedUser
                    isUploading = false
                }
            }
        }
    )










    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentUser != null) {
            // Profile Image
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ){
                //  Profile image
                // name and show profile text Column
                // IconButton to show profile
                Box (
                    modifier = Modifier
                        .weight(1f)
                ){
                    Row (modifier = Modifier.fillMaxWidth() ){
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)

                        ) {
                            if (isUploading) {
                                CircularProgressIndicator( ///////// Oploading Indicator
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                //navController.navigate(Screen.LoadingScreen) ///////////

                            } else if (currentUser?.profileImageUrl != null) {

                                ProfileImageComponent(user = currentUser!!)
                            } else {
                                Button(
                                    onClick = {
                                        singlePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Profile Image")
                                }
                            }
                        } // Profile Image Box
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                        ){
                            Column (modifier = Modifier){
                                Text(text = currentUser?.name ?: "No Name", style = MaterialTheme.typography.headlineMedium)
                                Text(text = "Show profile" , style = MaterialTheme.typography.bodyMedium)
                            }

                        }
                    }

                }
                IconButton(
                    onClick = {
                        //navController.navigate(Screen.ProfileScreen)
                    },
                    modifier = Modifier
                        //.size(50.dp)
                        .fillMaxWidth()
                        .weight(0.1f)


                ) {
                    Icon(
                        imageVector = FeatherIcons.ChevronRight, // Icon için uygun bir image vector seçin
                        contentDescription = "Logout",
                        modifier = Modifier.fillMaxSize()
                    )
                }


            } // Profile Image / username  Profile

            DividerComponent()


//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Name (if available)
//            currentUser?.name?.let { name ->
//                Text(
//                    text = name,
//                    style = MaterialTheme.typography.headlineLarge,
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // User Name
//            Text(
//                text = "@${currentUser?.userName}" ?: "No Username",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            SettingSectionComponent()

            DividerComponent()

            // Photos Card
            ElevatedCard(

                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(180.dp)
                    .fillMaxWidth()

            ) {
                // Card Content
                Column {
                    // Photos Text
                    Text(
                        text = "Photos: ",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp),
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                    )

                }
            }



            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = user?.email ?: "Email N/A",
                style = MaterialTheme.typography.bodyLarge
            )


        } else {
            // User data not available
            CircularProgressIndicator()////////////////
            //navController.navigate(Screen.LoadingScreen)

        }

        DividerComponent()

        // Sign Out Button
        SignOutButton(
            onItemClick = { firebaseViewModel.signOut() }
        )




    }// Account Screen Column

}

/*
@Composable
fun PersonalInfoItemRowComponent(textSize : Int = 16,
                                 iconSize : Int = 32,
                                 paddingValue : Int = 16,
                                 iconPaddingValue : Int = 16) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(16.dp)
    ){

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(32.dp))





        // Personal Info
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(modifier = Modifier.weight(1f)) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        ,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = FeatherIcons.User,
                        contentDescription = "",
                        modifier = Modifier
                            .size(iconSize.dp)

                    )
                    Text(
                        text = "Personal Information",
                        modifier = Modifier
                            .padding(start = textSize.dp)
                    )
                }
            }
            IconButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .size(50.dp)
                    .weight(0.1f)
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight, // Icon için uygun bir image vector seçin
                    contentDescription = "Personal Information",
                    modifier = Modifier.size(iconSize.dp)
                )
            }

        } // Personal Info Row End

        DividerComponent()

        // Login & Security
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(modifier = Modifier.weight(1f)) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = FeatherIcons.Key,
                        contentDescription = "",
                        modifier = Modifier
                            .size(32.dp)

                    )
                    Text(
                        text = "Login & Security",
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
            }
            IconButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .size(50.dp)
                    .weight(0.1f)
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight, // Icon için uygun bir image vector seçin
                    contentDescription = "Personal Information",
                    modifier = Modifier.size(24.dp)
                )
            }

        } // Login & Security Row End

        DividerComponent()


        // Payment and Subscriptions
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(modifier = Modifier.weight(1f)) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = FeatherIcons.CreditCard,
                        contentDescription = "",
                        modifier = Modifier
                            .size(32.dp)

                    )
                    Text(
                        text = "Payment & Subscriptions",
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
            }
            IconButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .size(50.dp)
                    .weight(0.1f)
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight, // Icon için uygun bir image vector seçin
                    contentDescription = "Personal Information",
                    modifier = Modifier.size(24.dp)
                )
            }

        } // Payment and Subscriptions Row End

        DividerComponent()


        // Accessibility
        // Payment and Subscriptions
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box(modifier = Modifier.weight(1f)) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = FeatherIcons.Settings,
                        contentDescription = "",
                        modifier = Modifier
                            .size(32.dp)

                    )
                    Text(
                        text = "Accessibility",
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
            }
            IconButton(
                onClick = { /* do something */ },
                modifier = Modifier
                    .size(50.dp)
                    .weight(0.1f)
            ) {
                Icon(
                    imageVector = FeatherIcons.ChevronRight, // Icon için uygun bir image vector seçin
                    contentDescription = "Personal Information",
                    modifier = Modifier.size(24.dp)
                )
            }

        } // Accebility Row End

    }




}



 */


@Composable
fun SettingSectionComponent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        val items = listOf(
            Pair(FeatherIcons.User, "Personal Information"),
            Pair(FeatherIcons.Key, "Login & Security"),
            Pair(FeatherIcons.CreditCard, "Payment & Subscriptions"),
            Pair(FeatherIcons.Settings, "Accessibility")
        )

        items.forEach { (icon, text) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(imageVector = icon, contentDescription = "", modifier = Modifier.size(32.dp))
                    Text(text = text, modifier = Modifier.padding(start = 16.dp))
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(imageVector = FeatherIcons.ChevronRight, contentDescription = "Navigate", modifier = Modifier.size(24.dp))
                }
            }
            DividerComponent()
        }
    }
}


/**
 *  Bottom bar component for the Account screen
 *  @param verticalPadding : Int is the vertical padding for the bottom bar
 *  @param horizontalPadding : Int is the horizontal padding for the bottom bar
 */
@Composable
fun DividerComponent(verticalPadding : Int = 16 , horizontalPadding : Int = 0) {
    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = verticalPadding.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )

}

@Composable
fun SignOutButton(onItemClick: () -> Unit) {
    ElevatedButton(
        onClick = { onItemClick() },
        shape = MaterialTheme.shapes.medium,
    ) {
        Text("Sign Out")
    }
}


@Composable
fun ProfileImageComponent(user: User, modifier: Modifier = Modifier) {
    AsyncImage(
        model = user.profileImageUrl,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .fillMaxSize()
            .border(2.dp, Color.LightGray, CircleShape)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}