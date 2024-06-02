package com.example.project.model.repo

import com.example.project.model.dao.LeaderboardDao
import com.example.project.model.models.Leaderboard

class LeaderboardRepo(private val leaderboardDao: LeaderboardDao) {
    suspend fun getAllScores(): List<Leaderboard> {
        return leaderboardDao.getAllScores()
    }

    suspend fun insertScore(score: Leaderboard) {
        leaderboardDao.insertScore(score)
    }
}
