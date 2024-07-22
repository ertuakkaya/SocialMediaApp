package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.socialmediaapp.R

@Preview
@Composable
fun LoadingScreen(modifier: Modifier = Modifier)
{
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie_anim))
    val progress by animateLottieCompositionAsState(composition)
    Box(modifier = modifier.fillMaxSize()) {
        LottieAnimationExample(composition = composition!!, progress = progress)
    }

}


@Composable
fun LottieAnimationExample(composition : LottieComposition, progress : Float) {


    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}