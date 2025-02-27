package com.example.mazegame5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.mazegame5.MainDb
import com.example.mazegame5.LevelSeeder

class MainActivity : AppCompatActivity() {

    private val db by lazy { MainDb.getDb(applicationContext) }
    private val levelSeeder by lazy { LevelSeeder(db.getDao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            levelSeeder.insertLevels()
            Log.d("MainActivity", "Levels inserted successfully.")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun Button11(view: View) {
        val button1Intent = Intent(this, MazeActivity1::class.java)
        startActivity(button1Intent)
    }
}

