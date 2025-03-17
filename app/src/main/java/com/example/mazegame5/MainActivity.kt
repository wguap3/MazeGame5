package com.example.mazegame5

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

    fun Button12(view: View) {
        val button1Intent = Intent(this, StatisticsActivity::class.java)
        startActivity(button1Intent)
    }
    fun Button13(view: View) {
        val button1Intent = Intent(this, MazeActivity2::class.java)
        startActivity(button1Intent)
    }
    fun Button14(view: View) {
        val button1Intent = Intent(this, MazeActivity3::class.java)
        startActivity(button1Intent)
    }

}

