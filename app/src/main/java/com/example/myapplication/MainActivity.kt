package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout

class MainActivity : AppCompatActivity() {
    private var grid: GridLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityId = R.layout.activity_main
        setContentView(activityId)

        this.grid = findViewById<GridLayout>(R.id.activity_main_grid)

        try {
            val width = 4
            val height = 6
            this.grid?.rowCount = height
            this.grid?.columnCount = width
            val field = Field(width, height)
            field.init(this, this.grid!!)
        } catch (error: Exception) {
            val errorToast = Toast.makeText(this, error.message, Toast.LENGTH_LONG)
            errorToast.show()
        }
    }
}
