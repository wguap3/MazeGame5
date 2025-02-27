package com.example.mazegame5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: Item)

    @Query("SELECT * FROM maze_levels")
    fun getAllItems(): Flow<List<Item>>

    @Query("DELETE FROM maze_levels")
    suspend fun deleteAllItems()
}

