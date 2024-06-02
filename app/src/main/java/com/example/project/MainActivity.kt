package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.KoZnaZnaGame
import com.example.project.MojBrojGame
import com.example.project.SkockoGame
import com.example.project.SlagalicaGame
import com.example.project.SpojniceGame
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
                    composable("mojbroj") { MojBrojGame(navController) }
                    composable("spojnice") { SpojniceGame() }
                    composable("skocko") { SkockoGame(navController) }
                    composable("koznazna") { KoZnaZnaGame(navController) }
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.feningashlogo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(100.dp))
        )
        Spacer(modifier = Modifier.height(64.dp))
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
