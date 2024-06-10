package com.example.project

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.project.model.AppDatabase
import com.example.project.viewmodel.KoZnaZnaViewModel
import com.example.project.viewmodel.KoZnaZnaViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KoZnaZnaGame(navController: NavHostController, database: AppDatabase) {
    val viewModel: KoZnaZnaViewModel = viewModel(
        factory = KoZnaZnaViewModelFactory(database.koZnaDao())
    )
    val questions by viewModel.questions.observeAsState(initial = emptyList())

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
        if (answer == currentQuestion?.correct) {
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
                Text(text = currentQuestion?.question ?: "", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                currentQuestion?.let { question ->
                    listOf(question.ans_a, question.ans_b, question.ans_c, question.ans_d).forEach { option ->
                        val isSelected = selectedOption == option
                        val buttonColor = when {
                            !answered -> Color.Gray
                            option == question.correct -> Color.Green
                            option == selectedOption -> Color.Red
                            else -> Color.Gray
                        }
                        Button(
                            onClick = { if (!answered) selectedOption = option },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .then(
                                    if (isSelected) Modifier.border(
                                        width = 1.dp,
                                        color = Color.Blue,
                                        shape = RoundedCornerShape(4.dp)
                                    ) else Modifier
                                ),
                            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
                        ) {
                            Text(option)
                        }
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
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    KoZnaZnaGame(navController = navController, database = database)
}