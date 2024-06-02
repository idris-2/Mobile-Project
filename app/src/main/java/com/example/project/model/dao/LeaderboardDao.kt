package com.example.project.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project.model.models.Leaderboard

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboards ORDER BY score DESC")
    fun getAllScores(): List<Leaderboard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScore(leaderboard: Leaderboard)
}
