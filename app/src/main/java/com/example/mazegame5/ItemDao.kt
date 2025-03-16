package com.example.mazegame5

import androidx.lifecycle.LiveData
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
    fun getGameTime(id: Int): LiveData<GameTime>

    @Update
    suspend fun updateGameTime(gameTime: GameTime)

    @Query("SELECT * FROM game_times")
    suspend fun getAllGameTimes(): List<GameTime>

}
