package com.example.mazegame5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

import com.google.gson.Gson

class LevelSeeder(private val dao: ItemDao) {

    suspend fun insertLevels() {
        val levels = listOf(
            arrayOf(
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 0, 3, 1, 0, 0, 0, 1), // Стартовая позиция (3) на (2, 1)
                intArrayOf(1, 0, 1, 1, 0, 1, 0, 1),
                intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
                intArrayOf(1, 1, 1, 1, 0, 1, 1, 1),
                intArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
                intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
                intArrayOf(1, 1, 1, 1, 1, 1, 2, 1)  // Финиш (2)
            ),
            // Уровень 2
            arrayOf(
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 0, 0, 0, 1, 0, 3, 1), // Стартовая позиция (3) на (1, 1)
                intArrayOf(1, 0, 1, 0, 1, 0, 1, 1),
                intArrayOf(1, 0, 1, 0, 0, 0, 1, 1),
                intArrayOf(1, 0, 1, 1, 1, 0, 0, 1),
                intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
                intArrayOf(1, 0, 1, 1, 0, 0, 1, 1),
                intArrayOf(1, 1, 1, 1, 1, 2, 1, 1)  // Финиш (2)
            ),
            // Уровень 3
            arrayOf(
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 0, 0, 0, 0, 0, 1, 1), // Стартовая позиция (3) на (2, 1)
                intArrayOf(1, 0, 1, 1, 1, 0, 1, 1),
                intArrayOf(1, 0, 0, 0, 0, 0, 3, 1),
                intArrayOf(1, 0, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
                intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
                intArrayOf(1, 1, 1, 1, 1, 1, 2, 1)  // Финиш (2)
            )
        )

        withContext(Dispatchers.IO) {
            dao.deleteAllItems() // Очистка базы данных
            val items = levels.map { level ->
                val levelString = convertMatrixToString(level)
                Log.d("LevelSeeder", "Inserting level: $levelString")
                Item(levelMatrix = levelString)
            }
            items.forEach { item ->
                dao.insertItem(item)
            }
            Log.d("LevelSeeder", "Levels inserted successfully.")
        }
    }

    private fun convertMatrixToString(matrix: Array<IntArray>): String {
        return Gson().toJson(matrix)
    }
}
