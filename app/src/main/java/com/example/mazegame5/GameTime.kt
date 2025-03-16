package com.example.mazegame5

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "game_times")
data class GameTime(
    @PrimaryKey var id: Int, // Уникальный ID (1 — Легкий, 2 — Средний, 3 — Сложный)
    var bestTimeLevel1: Long = Long.MAX_VALUE, // Лучшее время для первого уровня
    var bestTimeLevel2: Long = Long.MAX_VALUE, // Лучшее время для второго уровня
    var bestTimeLevel3: Long = Long.MAX_VALUE // Лучшее время для третьего уровня
)
