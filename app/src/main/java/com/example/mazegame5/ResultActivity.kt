package com.example.mazegame5

import android.content.Intent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resultTextView = findViewById(R.id.resultTextView)
        val bestLevel1 = intent.getLongExtra("CURRENT_LEVEL1", Long.MAX_VALUE)
        val bestLevel2 = intent.getLongExtra("CURRENT_LEVEL2", Long.MAX_VALUE)
        val bestLevel3 = intent.getLongExtra("CURRENT_LEVEL3", Long.MAX_VALUE)
        fun formatTime(millis: Long): String {
            val seconds = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60)) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

        val resultText = """
            Текущее время уровня 1: ${if (bestLevel1 == Long.MAX_VALUE) "Нет данных" else formatTime(bestLevel1)}
            Текущее время уровня 2: ${if (bestLevel2 == Long.MAX_VALUE) "Нет данных" else formatTime(bestLevel2)}
            Текущее время уровня 3: ${if (bestLevel3 == Long.MAX_VALUE) "Нет данных" else formatTime(bestLevel3)}
        """.trimIndent()

        resultTextView.text = resultText


    }


    fun Button31(view: View) {
        val button1Intent = Intent(this, MainActivity::class.java)
        startActivity(button1Intent)
    }
}