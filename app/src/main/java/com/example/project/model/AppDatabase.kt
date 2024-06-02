package com.example.project.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project.model.dao.KoZnaDao
import com.example.project.model.dao.LeaderboardDao
import com.example.project.model.models.KoZna
import com.example.project.model.models.Leaderboard

@Database(entities = [KoZna::class, Leaderboard::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun koZnaDao(): KoZnaDao
    abstract fun leaderboardDao(): LeaderboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                Log.d("Database", "Database created at: ${context.getDatabasePath("app_database")}")
                INSTANCE = instance
                instance
            }
        }
    }
}
