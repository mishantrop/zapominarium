package com.quasigames.zapominarium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var fieldGrid: GridLayout? = null
    private var isDarkMode = false
    private val field = Field()
    private val gameplay = Gameplay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        fieldGrid = findViewById(R.id.activity_main_grid)

        var initialWidth = 3
        var initialHeight = 4
        val buttonDecreaseComplexity = findViewById<Button>(R.id.buttonDecreaseComplexity)
        val buttonIncreaseComplexity = findViewById<Button>(R.id.buttonIncreaseComplexity)
        val mainLayout = findViewById<LinearLayout>(R.id.activity_main_layout)
        val mainLayoutToolbar = findViewById<LinearLayout>(R.id.activity_main_toolbar)

        buttonDecreaseComplexity.setOnClickListener {
            gameplay.decreaseComplexity()
        }
        buttonIncreaseComplexity.setOnClickListener {
            gameplay.increaseComplexity()
        }

        try {
            fieldGrid?.columnCount = initialWidth
            fieldGrid?.rowCount = initialHeight

            field.maxWidth = 8
            field.maxHeight = 8
            field.init(this, fieldGrid!!, initialWidth, initialHeight)
            gameplay.init(field)

            val vto: ViewTreeObserver = mainLayoutToolbar.viewTreeObserver
            vto.addOnGlobalLayoutListener {
                val mainLayoutToolbarHeight = mainLayoutToolbar.height
                val actionbarHeight = getActionbarHeight()
                field.setViewportSize(0, mainLayout.height - mainLayoutToolbarHeight - actionbarHeight)
            }
        } catch (error: Exception) {
            val errorToast = Toast.makeText(this, error.message, Toast.LENGTH_LONG)
            errorToast.show()
        }
    }

    private fun toggleTheme() {
        val mainLayout = findViewById<LinearLayout>(R.id.activity_main_layout)

        isDarkMode = !isDarkMode

        if (isDarkMode) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDark))
        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundLight))
        }
    }

    private fun getActionbarHeight(): Int {
        val tv = TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        return actionBarHeight
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                toggleTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
