package com.example.project.model.repo

import com.example.project.model.dao.KoZnaDao
import com.example.project.model.models.KoZna

class KoZnaRepo(private val koZnaDao: KoZnaDao) {
    suspend fun getAllQuestions(): List<KoZna> {
        return koZnaDao.getAllQuestions()
    }

    suspend fun insertQuestion(question: KoZna) {
        koZnaDao.insertQuestion(question)
    }
}
