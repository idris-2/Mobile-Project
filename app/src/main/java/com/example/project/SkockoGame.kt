package com.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

val symbols = listOf("♠", "♥", "♦", "♣", "★", "☻")

@Composable
fun SkockoGame(navController: NavController) {
    val targetCombination = remember { List(4) { symbols.random() } }
    var guesses by remember { mutableStateOf(List(6) { MutableList(4) { "" } }) }
    var currentGuessIndex by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf(List(6) { MutableList(4) { "" } }) }
    var gameFinished by remember { mutableStateOf(false) }
    var gameWon by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Skocko Game") }) },
        bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    symbols.forEach { symbol ->
                        val symbolColor = if (symbol == "★" || symbol == "☻") Color.Yellow else Color.Yellow
                        Box(
                            modifier = Modifier
                                .size(60.dp)  // Increased size
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .clickable {
                                    if (currentGuessIndex < 6 && guesses[currentGuessIndex].contains("")) {
                                        guesses = guesses.toMutableList().apply {
                                            this[currentGuessIndex] = guesses[currentGuessIndex].toMutableList().apply {
                                                val index = this.indexOf("")
                                                if (index != -1) this[index] = symbol
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp) // Adjust circle size here
                                    .background(Color.White, CircleShape) // Use the bigger circle as background
                            ) {
                                Text(
                                    text = symbol,
                                    color = symbolColor,
                                    fontSize = 24.sp,
                                    modifier = Modifier.align(Alignment.Center) // Center the symbol in the circle
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            guesses.forEachIndexed { rowIndex, guess ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(4.dp)
                ) {
                    guess.forEachIndexed { colIndex, symbol ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)  // Increased size
                                .background(Color.LightGray)
                                .padding(8.dp)
                                .clickable {
                                    if (currentGuessIndex == rowIndex) {
                                        guesses = guesses.toMutableList().apply {
                                            this[rowIndex] = guess.toMutableList().apply {
                                                if (this[colIndex].isNotEmpty()) {
                                                    this[colIndex] = ""
                                                } else {
                                                    this[colIndex] = symbols.random()
                                                }
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp) // Adjust circle size here
                                    .background(Color.White, CircleShape) // Use the bigger circle as background
                            ) {
                                Text(
                                    text = symbol,
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    modifier = Modifier.align(Alignment.Center) // Center the symbol in the circle
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        feedback[rowIndex].forEach { indicator ->
                            val indicatorSize = 32.dp  // Increased size for empty circles
                            Box(
                                modifier = Modifier
                                    .size(indicatorSize)
                                    .background(
                                        color = when (indicator) {
                                            "red" -> Color.Red
                                            "yellow" -> Color.Yellow
                                            else -> Color.Gray
                                        },
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(8.dp)
                    .size(width = 130.dp, height = 50.dp)
                    .clickable {
                        if (!guesses[currentGuessIndex].contains("")) {
                            val currentGuess = guesses[currentGuessIndex]
                            val indicators = mutableListOf<String>()
                            val targetCopy = targetCombination.toMutableList()
                            val guessCopy = currentGuess.toMutableList()

                            for (i in 0..3) {
                                if (currentGuess[i] == targetCombination[i]) {
                                    indicators.add("red")
                                    targetCopy[i] = ""
                                    guessCopy[i] = ""
                                }
                            }

                            for (i in 0..3) {
                                if (guessCopy[i].isNotEmpty() && targetCopy.contains(guessCopy[i])) {
                                    indicators.add("yellow")
                                    targetCopy[targetCopy.indexOf(guessCopy[i])] = ""
                                }
                            }

                            feedback = feedback.toMutableList().apply {
                                this[currentGuessIndex] = (indicators + List(4 - indicators.size) { "" }).toMutableList()
                            }

                            if (indicators.count { it == "red" } == 4) {
                                gameFinished = true
                                gameWon = true
                            } else if (currentGuessIndex == 5) {
                                gameFinished = true
                            } else {
                                currentGuessIndex++
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Check Guess", color = Color.White)
            }
        }
    }

    if (gameFinished) {
        EndGameDialog(navController, gameWon)
    }
}

@Composable
fun EndGameDialog(navController: NavController, gameWon: Boolean) {
    AlertDialog(
        onDismissRequest = { navController.navigate("main") },
        title = { Text(text = if (gameWon) "Congratulations!" else "You Failed") },
        text = { Text(text = if (gameWon) "You guessed the correct combination!" else "Better luck next time!") },
        confirmButton = {
            Button(onClick = { navController.navigate("main") }) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SkockoGame(navController = rememberNavController())
}
