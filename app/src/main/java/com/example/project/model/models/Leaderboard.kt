package com.example.project.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboards")
data class Leaderboard(
    @PrimaryKey(autoGenerate = true) val player_id: Int = 0,
    val player_name: String,
    val score: Int
)
