package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.dao.KoZnaDao
import com.example.project.model.dao.LeaderboardDao
import com.example.project.model.models.KoZna
import com.example.project.model.models.Leaderboard
import kotlinx.coroutines.launch

class FieldsViewModel(
    private val koZnaDao: KoZnaDao,
    private val leaderboardDao: LeaderboardDao
) : ViewModel() {

    suspend fun getAllQuestions() = koZnaDao.getAllQuestions()

    fun insertQuestion(question: KoZna) {
        viewModelScope.launch {
            koZnaDao.insertQuestion(question)
        }
    }

    suspend fun getAllScores() = leaderboardDao.getAllScores()

    fun insertScore(score: Leaderboard) {
        viewModelScope.launch {
            leaderboardDao.insertScore(score)
        }
    }
}
