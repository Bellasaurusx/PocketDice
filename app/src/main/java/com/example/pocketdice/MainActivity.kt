package com.example.pocketdice

import com.example.pocketdice.ui.theme.CosmicTheme
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmicTheme {
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
    var diceRoll by remember { mutableIntStateOf(1) }
    var selectedDie by remember { mutableIntStateOf(20) }
    val rollHistory = remember { mutableStateListOf<Int>() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                coroutineScope.launch {
                    scale.animateTo(0.8f, animationSpec = tween(100))
                    diceRoll = Random.nextInt(1, selectedDie + 1)
                    scale.animateTo(1f, animationSpec = spring())
                    mediaPlayer.start()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                100,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        vibrator.vibrate(100)
                    }
                    rollHistory.add(0, diceRoll)
                    if (rollHistory.size > 10) rollHistory.removeAt(rollHistory.lastIndex)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            listOf(4, 6, 8, 10, 12, 20, 100).forEach { sides ->
                Button(onClick = { selectedDie = sides }) {
                    Text("d$sides")
                }
            }
        }

        Text("Current die: d$selectedDie", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

// Tap to roll text
        Text("Tap to roll", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

// Rolled number with animation
        Box(modifier = Modifier.scale(scale.value)) {
            Text("🎲 $diceRoll", style = MaterialTheme.typography.displayLarge)
        }
        Spacer(modifier = Modifier.height(32.dp)) // 👈 extra space before history

// Roll history
        Text("Roll History:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.75f)
        ) {
            rollHistory.forEachIndexed { index, roll ->
                Text("#${index + 1}: 🎲 $roll", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}



