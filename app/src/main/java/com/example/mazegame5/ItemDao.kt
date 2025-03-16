package com.example.mazegame5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameTime(gameTime: GameTime)

    @Query("SELECT * FROM game_times WHERE id = :id")
    suspend fun getGameTime(id: Int): GameTime?

    @Update
    suspend fun updateGameTime(gameTime: GameTime)

    @Query("SELECT * FROM game_times")
    suspend fun getAllGameTimes(): List<GameTime>
}
