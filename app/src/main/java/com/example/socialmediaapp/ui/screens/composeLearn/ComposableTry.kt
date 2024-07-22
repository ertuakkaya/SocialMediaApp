package com.example.socialmediaapp.ui.screens.composeLearn

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.example.socialmediaapp.R
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope

import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import org.intellij.lang.annotations.Language

@Preview
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ImageRenderEffect() {

    var image by remember {
        mutableIntStateOf(R.drawable.like_filled)
    }

    val blur = remember { Animatable(0f) }

    LaunchedEffect(image) {
        blur.animateTo(100f, tween(easing = LinearEasing))
        blur.animateTo(0f, tween(easing = LinearEasing))
    }

    Column(
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        ShaderContainer(
            modifier = Modifier
                .animateContentSize()
                .clipToBounds()
                .fillMaxWidth()
        ) {

            BlurContainer(
                modifier = Modifier.fillMaxWidth(),
                blur = blur.value,
                component = {
                    AnimatedContent(
                        targetState = image,
                        modifier = Modifier
                            .fillMaxWidth(),
                        transitionSpec = {
                            (fadeIn(tween(easing = LinearEasing)) + scaleIn(
                                tween(
                                    1_000,
                                    easing = LinearEasing
                                )
                            )).togetherWith(
                                fadeOut(
                                    tween(
                                        1_000,
                                        easing = LinearEasing
                                    )
                                ) + scaleOut(
                                    tween(
                                        1_000,
                                        easing = LinearEasing
                                    )
                                )
                            )
                        }, label = ""
                    ) { image ->
                        Image(
                            painter = painterResource(id = image),
                            modifier = Modifier
                                .size(200.dp),
                            contentDescription = ""
                        )
                    }
                }) {}
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                image =
                    if (image == R.drawable.like_filled) R.drawable.like_unfilled else R.drawable.like_filled
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Change Image")
        }
    }
}






@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BlurContainer(
    modifier: Modifier = Modifier,
    blur: Float = 60f,
    component: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .customBlur(blur),
            content = component,
        )
        Box(
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@SuppressLint("SuspiciousModifierThen")
@RequiresApi(Build.VERSION_CODES.S)
fun Modifier.customBlur(blur: Float) = this.then(
    graphicsLayer {
        if (blur > 0f)
            renderEffect = RenderEffect
                .createBlurEffect(
                    blur,
                    blur,
                    Shader.TileMode.DECAL,
                )
                .asComposeRenderEffect()
    }
)






@Language("AGSL")
const val ShaderSource = """
    uniform shader composable;
    
    uniform float visibility;
    
    half4 main(float2 cord) {
        half4 color = composable.eval(cord);
        color.a = step(visibility, color.a);
        return color;
    }
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShaderContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val runtimeShader = remember {
        RuntimeShader(ShaderSource)
    }
    Box(
        modifier
            .graphicsLayer {
                runtimeShader.setFloatUniform("visibility", 0.2f)
                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(
                        runtimeShader, "composable"
                    )
                    .asComposeRenderEffect()
            }
    ) {
        content()
    }
}