package com.example.project.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.project.model.models.KoZna

@Dao
interface KoZnaDao {
    @Query("SELECT * FROM koznas")
    fun getAllQuestions(): List<KoZna>

    @Query("SELECT * FROM koznas ORDER BY RANDOM() LIMIT 5")
    suspend fun getRandomQuestions(): List<KoZna>

    @Query("SELECT * FROM koznas WHERE question = :question")
    fun getQuestionByText(question: String): KoZna?

    @Transaction
    fun upsertQuestion(koZna: KoZna) {
        val existingQuestion = getQuestionByText(koZna.question)
        if (existingQuestion == null) {
            insertQuestion(koZna)
        } else {
            insertQuestion(koZna.copy(id = existingQuestion.id))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(koZna: KoZna)
}
