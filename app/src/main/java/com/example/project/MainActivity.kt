package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.project.model.AppDatabase
import com.example.project.model.models.KoZna
import com.example.project.model.models.Leaderboard
import com.example.project.ui.theme.ProjectTheme
import com.example.project.viewmodel.FieldsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    private val viewModel by viewModels<FieldsViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FieldsViewModel(database.koZnaDao(), database.leaderboardDao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Insert dummy data using Dispatchers.IO
        lifecycleScope.launch(Dispatchers.IO) {
            // Insert dummy data
            database.koZnaDao().insertQuestion(
                KoZna(question = "Question 1", ans_a = "A", ans_b = "B", ans_c = "C", ans_d = "D", correct = "A")
            )
            database.koZnaDao().insertQuestion(
                KoZna(question = "Question 2", ans_a = "A", ans_b = "B", ans_c = "C", ans_d = "D", correct = "B")
            )
            database.leaderboardDao().insertScore(
                Leaderboard(player_id = 1, player_name = "Player 1", score = 100)
            )
            database.leaderboardDao().insertScore(
                Leaderboard(player_id = 2, player_name = "Player 2", score = 200)
            )
        }

        setContent {
            ProjectTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("mojbroj") { MojBrojGame(navController) }
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("mojbroj") }) {
            Text("Moj Broj")
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
