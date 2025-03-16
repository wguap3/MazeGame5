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

    @Insert
    suspend fun insert(gameTime: GameTime)

    @Query("SELECT * FROM game_times WHERE id = :id")
    fun getGameTime(id: Int): LiveData<GameTime>

    @Update
    suspend fun updateGameTime(gameTime: GameTime)

    @Query("SELECT * FROM game_times")
    suspend fun getAllGameTimes(): List<GameTime>

    @Query("UPDATE game_times SET bestTimeLevel1 = :time WHERE id = :id")
    suspend fun updateLevel1Time(id: Int, time: Long)


    @Query("UPDATE game_times SET bestTimeLevel2 = :time WHERE id = :id")
    suspend fun updateLevel2Time(id: Int, time: Long)

    @Query("UPDATE game_times SET bestTimeLevel3 = :time WHERE id = :id")
    suspend fun updateLevel3Time(id: Int, time: Long)

    @Query("SELECT bestTimeLevel1 FROM game_times WHERE id = :id")
    suspend fun getBestLevel1Time(id: Int): Long?

    @Query("SELECT bestTimeLevel2 FROM game_times WHERE id = :id")
    suspend fun getBestLevel2Time(id: Int): Long?

    @Query("SELECT bestTimeLevel3 FROM game_times WHERE id = :id")
    suspend fun getBestLevel3Time(id: Int): Long?



}
