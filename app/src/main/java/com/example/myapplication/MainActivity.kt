package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.gridlayout.widget.GridLayout

class Field {
    val width: Int = 3
    val height: Int = 4

    fun init(context: Context, grid: GridLayout) {
        for (w in 1..this.width) {
            for (h in 1..this.height) {
                var button = Button(context)
                button.text = "xyu" + w.toString() + h.toString();
                grid?.addView(button)
            }
        }
    }

    fun render() {

    }
}

class MainActivity : AppCompatActivity() {
    private var grid: GridLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        println("xxx onCreate")

        super.onCreate(savedInstanceState)
        val activityId = R.layout.activity_main
        setContentView(activityId)

        this.grid = findViewById<GridLayout>(R.id.activity_main_grid)

        val field = Field()
        field.init(this, this.grid!!);
    }

    override fun onPause() {
        super.onPause()
        println("xxx onPause")
    }

    override fun onResume() {
        super.onResume()
        println("xxx onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("xxx onDestroy")
    }
}
