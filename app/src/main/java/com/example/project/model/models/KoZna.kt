package com.example.project.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "koznas")
data class KoZna(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val ans_a: String,
    val ans_b: String,
    val ans_c: String,
    val ans_d: String,
    val correct: String
)
