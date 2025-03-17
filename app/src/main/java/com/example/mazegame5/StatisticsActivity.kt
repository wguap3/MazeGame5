package com.example.mazegame5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer


class StatisticsActivity : AppCompatActivity() {
    private lateinit var textView5: TextView
    private lateinit var textView4: TextView
    private lateinit var textView3: TextView
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
        textView4 = findViewById<TextView>(R.id.middle3)
        textView3 = findViewById<TextView>(R.id.textView2)


        // Получаем экземпляр базы данных
        db = MainDb.getDb(this)

        // Получаем DAO
        ItemDao = db.getDao()

        ItemDao.getGameTime(1).observe(this, Observer { gameTime ->
            if (gameTime != null) {
                val displayText = "Best Level 1 Time: ${formatTime(gameTime.bestTimeLevel1)}\n" +
                        "Best Level 2 Time: ${formatTime(gameTime.bestTimeLevel2)}\n" +
                        "Best Level 3 Time: ${formatTime(gameTime.bestTimeLevel3)}"
                textView5.text = displayText
            } else {
                textView5.text = "No game data found."
            }
        })

        ItemDao.getGameTime(2).observe(this, Observer { gameTime ->
            if (gameTime != null) {
                val displayText = "Best Level 1 Time: ${formatTime(gameTime.bestTimeLevel1)}\n" +
                        "Best Level 2 Time: ${formatTime(gameTime.bestTimeLevel2)}\n" +
                        "Best Level 3 Time: ${formatTime(gameTime.bestTimeLevel3)}"
                textView4.text = displayText
            } else {
                textView4.text = "No game data found."
            }
        })
        ItemDao.getGameTime(3).observe(this, Observer { gameTime ->

            if (gameTime != null) {
                val displayText = "Best Level 1 Time: ${formatTime(gameTime.bestTimeLevel1)}\n" +
                        "Best Level 2 Time: ${formatTime(gameTime.bestTimeLevel2)}\n" +
                        "Best Level 3 Time: ${formatTime(gameTime.bestTimeLevel3)}"
                textView3.text = displayText
            } else {
                textView3.text = "No game data found."
            }
        })
    }
    private fun formatTime(timeNs: Long): String {
        return if (timeNs == Long.MAX_VALUE) {
            "--:--"
        } else {
            val totalSeconds = timeNs / 1000

            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            String.format("%02d:%02d", minutes, seconds)
        }
    }
    fun Button21(view: View) {
        val button1Intent = Intent(this, MainActivity::class.java)
        startActivity(button1Intent)
    }
}