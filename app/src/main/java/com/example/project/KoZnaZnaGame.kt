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

@Composable
fun KoZnaZnaGame(navController: NavHostController) {
    var question by remember { mutableStateOf("What is the capital of France?") }
    var options by remember { mutableStateOf(listOf("London", "Paris", "Berlin", "Madrid")) }
    var selectedOption by remember { mutableStateOf("") }
    var correctOption by remember { mutableStateOf("Paris") }
    var answered by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun checkAnswer(answer: String) {
        answered = true
        selectedOption = answer
        coroutineScope.launch {
            delay(3000) // Wait for 3 seconds
            // Proceed to the next question or navigate back to the main screen if finished
            // For simplicity, let's assume we finish after this question
            navController.navigate("main")
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(26.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = question, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            options.forEach { option ->
                val buttonColor = when {
                    !answered -> Color.Gray
                    option == correctOption -> Color.Green
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizApp() {
    // For preview purposes, we'll use a dummy navController. In actual app, it will be provided by the NavHost.
    KoZnaZnaGame(navController = rememberNavController())
}

