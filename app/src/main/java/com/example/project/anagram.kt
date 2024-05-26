package com.example.project

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle

@Composable
fun AnagramScreen() {
    var answer by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }
    var messageColor by remember { mutableStateOf(Color.Black) }

    val correctAnswer = "example"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rearrange the letters to form a word",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = answer,
            onValueChange = { answer = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (answer.text.equals(correctAnswer, ignoreCase = true)) {
                message = "Correct!"
                messageColor = Color.Green
            } else {
                message = "Try Again"
                messageColor = Color.Red
            }
        }) {
            Text("Submit")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (message.isNotEmpty()) {
            Text(text = message, color = messageColor, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnagramScreenPreview() {
    AnagramScreen()
}