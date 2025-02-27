package com.example.mazegame5

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maze_levels")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "level_matrix")
    var levelMatrix: String // Матрица уровня, представленная строкой
)
