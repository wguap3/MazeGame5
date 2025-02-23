package com.example.mazegame5

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MazeActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maze1)
    }
    fun Button21(view: View) {
        val button1Intent = Intent(this, MainActivity::class.java)
        startActivity(button1Intent)
    }
}
