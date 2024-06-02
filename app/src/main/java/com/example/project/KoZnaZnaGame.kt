package com.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Question(val text: String, val options: List<String>, val correctOption: String)

@Composable
fun KoZnaZnaGame(navController: NavHostController) {
    val questions = listOf(
        Question("What is the capital of France?", listOf("London", "Paris", "Berlin", "Madrid"), "Paris"),
        Question("What is the capital of Germany?", listOf("Berlin", "Vienna", "Zurich", "Frankfurt"), "Berlin"),
        Question("What is the capital of Spain?", listOf("Madrid", "Barcelona", "Seville", "Valencia"), "Madrid")
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var answered by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    fun checkAnswer(answer: String) {
        answered = true
        selectedOption = answer
        if (answer == currentQuestion?.correctOption) {
            correctAnswers++
        } else {
            incorrectAnswers++
        }
        coroutineScope.launch {
            delay(3000)
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                selectedOption = ""
                answered = false
            } else {
                showResult = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Game") },
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        if (showResult) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (correctAnswers > incorrectAnswers) "You Win!" else "You Lose!",
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Correct Answers: $correctAnswers", style = MaterialTheme.typography.body1)
                Text("Incorrect Answers: $incorrectAnswers", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("main") }) {
                    Text("Back to Main")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(26.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentQuestion?.text ?: "", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                currentQuestion?.options?.forEach { option ->
                    val buttonColor = when {
                        !answered -> Color.Gray
                        option == currentQuestion.correctOption -> Color.Green
                        option == selectedOption -> Color.Red
                        else -> Color.Gray
                    }
                    Button(
                        onClick = { if (!answered) selectedOption = option },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
                    ) {
                        Text(option)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { if (!answered) checkAnswer(selectedOption) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                ) {
                    Text("Submit", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Correct Answers: $correctAnswers", style = MaterialTheme.typography.body1)
                Text("Incorrect Answers: $incorrectAnswers", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizApp() {
    KoZnaZnaGame(navController = rememberNavController())
}
