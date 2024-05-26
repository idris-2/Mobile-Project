package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.ui.theme.ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("slagalica") { SlagalicaGame() }
                    composable("mojbroj") { MojBrojGame() }
                    composable("spojnice") { SpojniceGame() }
                    composable("skocko") { SkockoGame() }
                    composable("koznazna") { KoZnaZnaGame() }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("slagalica") }) {
            Text("Slagalica")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("mojbroj") }) {
            Text("Moj Broj")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("spojnice") }) {
            Text("Spojnice")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("skocko") }) {
            Text("Skočko")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("koznazna") }) {
            Text("Ko Zna Zna")
        }
    }
}