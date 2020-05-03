package com.quasigames.zapominarium

import android.widget.Button

class Cell(id: Int, button: Button, onClick: (id: Int) -> Unit) {
    var char: String? = null
    var isMatched = false
    var isVisible = false
    private var id: Int? = id
    private var button: Button? = button
    private var clickCount = 0

    init {
        this.button?.setOnClickListener {
            if (!isVisible) {
                clickCount++
                onClick(this.id!!)
            }
        }
    }

    fun hide() {
        isVisible = false
        render()
    }

    fun render() {
        if (isVisible) {
            button?.text = char
        } else {
            button?.text = "❓"
        }
    }

    fun setMatched() {
        isMatched = true
        render()
    }

    fun setSize(width: Int, height: Int) {
        button?.layoutParams?.width = width
        button?.layoutParams?.height = height
        button?.setPadding(0,0,0,0)
    }

    fun show() {
        isVisible = true
        render()
    }
}