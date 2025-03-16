package com.example.mazegame5

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GameTime::class], version = 3)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): ItemDao

    companion object {
        private var instance: MainDb? = null

        fun getDb(context: Context): MainDb {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "game_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("Database", "Tables created: ${db.version}")
                        }
                    })
                    .build()
                    .also { instance = it }
            }
        }
    }
}


