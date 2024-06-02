package com.example.project.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project.model.models.KoZna

@Dao
interface KoZnaDao {
    @Query("SELECT * FROM koznas")
    fun getAllQuestions(): List<KoZna>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(koZna: KoZna)
}
