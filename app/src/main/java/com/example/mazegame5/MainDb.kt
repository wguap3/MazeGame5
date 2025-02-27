package com.example.mazegame5

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): ItemDao

    companion object {
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "maze_levels.db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}


