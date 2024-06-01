package com.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.abs
import kotlin.random.Random


@Composable
fun MojBrojGame(navController: NavController) {
    var resultText by remember { mutableStateOf("") }
    val numbers = remember { generateNumbers() }
    val targetNumber = remember { generateTargetNumber() }
    var usedNumbers by remember { mutableStateOf(List(numbers.size) { false }) }
    var points by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }
    var gameFinished by remember { mutableStateOf(false) }
    var gameWon by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display target number
        Text(text = "Target: $targetNumber", fontSize = 24.sp)

        // Display number buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            numbers.forEachIndexed { index, number ->
                Button(
                    onClick = {
                        if (!usedNumbers[index]) {
                            resultText += number.toString()
                            usedNumbers = usedNumbers.toMutableList().also { it[index] = true }
                        }
                    },
                    enabled = !usedNumbers[index],
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(text = number.toString(), fontSize = 24.sp)
                }
            }
        }

        // Display operator buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("+", "-", "*", "/", "(", ")").forEach { operator ->
                Button(
                    onClick = {
                        resultText += " $operator "
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    Text(text = operator, fontSize = 24.sp)
                }
            }
        }

        // Display result field
        BasicTextField(
            value = resultText,
            onValueChange = { resultText = it },
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // Submit button
        Button(
            onClick = {
                try {
                    val result = evaluateExpression(resultText)
                    val difference = abs(result - targetNumber)
                    points = when {
                        difference == 0 -> 30
                        difference <= 5 -> 20
                        difference <= 10 -> 5
                        else -> 0
                    }
                    message = when {
                        difference == 0 -> "30"
                        difference <= 5 -> "20"
                        difference <= 10 -> "5"
                        else -> "0"
                    }
                    gameFinished = true
                    gameWon = difference <= 10
                } catch (e: Exception) {
                    message = "Invalid expression."
                    gameFinished = true
                    gameWon = false
                }
            },
            modifier = Modifier.padding(4.dp).align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }

        if (gameFinished) {
            EndGameDialogMojBroj(navController, gameWon, message)
        }


        // Clear button
        Button(
            onClick = {
                resultText = ""
                usedNumbers = List(numbers.size) { false }
                points = 0
                message = ""
            },
            modifier = Modifier.padding(4.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Clear", fontSize = 24.sp)
        }

        // Display points and message
        if (message.isNotEmpty()) {
            Text(text = message, fontSize = 24.sp, color = Color.Blue)
        }
    }
    }

@Composable
fun EndGameDialogMojBroj(navController: NavController, gameWon: Boolean, text: String) {
    AlertDialog(
        onDismissRequest = { navController.navigate("main") },
        title = { Text(text = if (gameWon) "Congratulations!" else "You Failed") },
        text = { Text(text = if (gameWon) "Good job, you've got $text points!" else "You've got $text points!") },
        confirmButton = {
            Button(onClick = { navController.navigate("main") }) {
                Text("OK")
            }
        }
    )
}


fun generateNumbers(): List<Int> {
    val firstFourNumbers = List(4) { Random.nextInt(1, 10) }
    val fifthNumber = listOf(10, 15, 20, 25).random()
    val sixthNumber = listOf(25, 50, 75, 100).random()
    return firstFourNumbers + fifthNumber + sixthNumber
}

fun generateTargetNumber(): Int {
    return Random.nextInt(1, 1000)
}

fun evaluateExpression(expression: String): Int {
    return try {
        expression.toInt()
    } catch (e: Exception) {
        val tokens = expression.split(" ")
        val values = mutableListOf<Int>()
        val ops = mutableListOf<Char>()

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token.isNumber()) {
                values.add(token.toInt())
            } else if (token.isOperator()) {
                while (ops.isNotEmpty() && precedence(token[0]) <= precedence(ops.last())) {
                    val val2 = values.removeLast()
                    val val1 = values.removeLast()
                    val op = ops.removeLast()
                    values.add(applyOp(op, val1, val2))
                }
                ops.add(token[0])
            } else if (token == "(") {
                ops.add('(')
            } else if (token == ")") {
                while (ops.last() != '(') {
                    val val2 = values.removeLast()
                    val val1 = values.removeLast()
                    val op = ops.removeLast()
                    values.add(applyOp(op, val1, val2))
                }
                ops.removeLast()
            }
            i++
        }

        while (ops.isNotEmpty()) {
            val val2 = values.removeLast()
            val val1 = values.removeLast()
            val op = ops.removeLast()
            values.add(applyOp(op, val1, val2))
        }

        values.last()
    }
}

fun String.isNumber() = this.toIntOrNull() != null

fun String.isOperator() = this in listOf("+", "-", "*", "/")

fun precedence(op: Char): Int {
    return when (op) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> 0
    }
}

fun applyOp(op: Char, a: Int, b: Int): Int {
    return when (op) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> a / b
        else -> throw UnsupportedOperationException("Unknown operator: $op")
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MojBrojGame(navController = rememberNavController())
}

