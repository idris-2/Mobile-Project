package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Najveća Euro novčanica je?", ans_a = "100", ans_b = "200", ans_c = "500", ans_d = "1000", correct = "C")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Žabe spadaju u?", ans_a = "Vodozemce", ans_b = "Ribe", ans_c = "Gmizavce", ans_d = "Gmazove", correct = "A")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Prvi svjetski rat je počeo?", ans_a = "1921", ans_b = "1918", ans_c = "1914", ans_d = "1911", correct = "C")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Koji proizvođač automobila se prije zvao Datsun?", ans_a = "Porsche", ans_b = "Suzuki", ans_c = "Hyundai", ans_d = "Nissan", correct = "D")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Koji protokol se koristi za razmjenu elektronske pošte?", ans_a = "DNS", ans_b = "SMTP", ans_c = "SMP", ans_d = "HTTPS", correct = "B")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Koji predmet je najbolji objektivno na Burch univeryitetu?", ans_a = "Mobilno programiranje", ans_b = "Web programiranje", ans_c = "Calculus II", ans_d = "Likovno", correct = "A")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Najbolji asistent na burchu je:", ans_a = "Lejla", ans_b = "Nedim", ans_c = "Senija", ans_d = "Naida", correct = "D")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Tranzistor je izmišljen", ans_a = "1950", ans_b = "1947", ans_c = "1948", ans_d = "1941", correct = "B")
            )
            database.koZnaDao().upsertQuestion(
                KoZna(question = "Koja država zauzima najveću površinu zemlje?", ans_a = "Japan", ans_b = "Evropa (kontinent)", ans_c = "USA", ans_d = "Rusija", correct = "D")
            )

            // Leaderboard DB
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
                    composable("koznazna") { KoZnaZnaGame(navController = navController, database=database) }
                    composable("leaderboard") { LeaderboardScreen(database = database) }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Main Screen") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            GameButton(navController, "Moj Broj", "mojbroj", 10)
            Spacer(modifier = Modifier.height(16.dp))
            GameButton(navController, "Skočko", "skocko", 20)
            Spacer(modifier = Modifier.height(16.dp))
            GameButton(navController, "Ko Zna Zna", "koznazna", 30)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Leaderboard")
            }
        }
    }
}

@Composable
fun GameButton(navController: NavHostController, text: String, route: String, score: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { navController.navigate(route) },
            modifier = Modifier.weight(1f)
        ) {
            Text(text)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$score",
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray)
                .padding(4.dp),
            color = Color.White
        )
    }
}

@Composable
fun LeaderboardScreen(database: AppDatabase) {
    var scores by remember { mutableStateOf(listOf<Leaderboard>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            val leaderboard = database.leaderboardDao().getAllScores()
            scores = leaderboard
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Leaderboard") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            scores.forEach { score ->
                Text(
                    text = "${score.player_name}: ${score.score}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
