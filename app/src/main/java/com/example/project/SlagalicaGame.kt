package com.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SlagalicaGame() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Slagalica Game")
        // Add your game components here (buttons, input fields, etc.)
        // For now, let's display a placeholder button:
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Play Slagalica")
        }
    }
}

@Preview
@Composable
fun PreviewSlagalicaGame() {
    SlagalicaGame()
}
