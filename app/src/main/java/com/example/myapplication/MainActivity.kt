package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.gridlayout.widget.GridLayout

class MainActivity : AppCompatActivity() {
    private var grid: GridLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityId = R.layout.activity_main
        setContentView(activityId)

        this.grid = findViewById<GridLayout>(R.id.activity_main_grid)

        val field = Field(3, 4)
        field.init(this, this.grid!!);
    }
}
