package com.example.pocketdice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import android.media.MediaPlayer
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.scale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DiceRollerScreen()
                }
            }

        }
    }
}

@Composable
fun DiceRollerScreen() {
    val scale = remember { Animatable(1f) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.dice_roll) }
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    var diceRoll by remember { mutableStateOf(1) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                coroutineScope.launch {
                    scale.animateTo(0.8f, animationSpec = tween(100))
                    diceRoll = Random.nextInt(1, 21)
                    scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))

                    mediaPlayer.start()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(100)
                    }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tap to roll", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.scale(scale.value)) {
            Text("🎲 $diceRoll", style = MaterialTheme.typography.displayLarge)
        }
    }
}




