package com.puregames.zapominarium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var fieldGrid: GridLayout? = null
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        this.fieldGrid = findViewById<GridLayout>(R.id.activity_main_grid)

        var initialWidth = 5
        var initialHeight = 6
        val field = Field()
        val buttonToggleTheme = findViewById<Button>(R.id.buttonToggleTheme)
        val buttonDecreaseComplexity = findViewById<Button>(R.id.buttonDecreaseComplexity)
        val buttonIncreaseComplexity = findViewById<Button>(R.id.buttonIncreaseComplexity)
        buttonDecreaseComplexity.setOnClickListener {
            field.decreaseComplexity()
        }
        buttonIncreaseComplexity.setOnClickListener {
            field.increaseComplexity()
        }

        buttonToggleTheme.setOnClickListener {
            this.isDarkMode = !this.isDarkMode
            val mainLayout = findViewById<LinearLayout>(R.id.activity_main_layout)

            if (this.isDarkMode) {
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDark))
            } else {
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundLight))
            }
        }

        try {
            this.fieldGrid?.columnCount = initialWidth
            this.fieldGrid?.rowCount = initialHeight

            field.init(this, this.fieldGrid!!, initialWidth, initialHeight)
        } catch (error: Exception) {
            val errorToast = Toast.makeText(this, error.message, Toast.LENGTH_LONG)
            errorToast.show()
        }
    }
}
