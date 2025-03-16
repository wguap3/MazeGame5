package com.example.mazegame5

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer


class StatisticsActivity : AppCompatActivity() {
    private lateinit var textView5: TextView
    private lateinit var ItemDao: ItemDao
    private lateinit var db: MainDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView5 = findViewById<TextView>(R.id.textView5)

        // Получаем экземпляр базы данных
        db = MainDb.getDb(this)

        // Получаем DAO
        ItemDao = db.getDao()

        ItemDao.getGameTime(1).observe(this, Observer { gameTime ->
            // Обновляем TextView, когда данные изменяются
            if (gameTime != null) {
                val displayText = "Best Level 1 Time: ${gameTime.bestTimeLevel1}\n" +
                        "Best Level 2 Time: ${gameTime.bestTimeLevel2}\n" +
                        "Best Level 3 Time: ${gameTime.bestTimeLevel3}"
                textView5.text = displayText
            } else {
                textView5.text = "No game data found."
            }
        })
    }
}