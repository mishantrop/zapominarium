package com.quasigames.zapominarium

import android.widget.Button

class Cell {
    var char: String? = null
    var isMatched = false
    var isVisible = false
    private var id: Int? = null
    private var button: Button? = null
    private var clickCount = 0

    constructor(
      id: Int,
      button: Button,
      onClick: (id: Int) -> Unit
    ) {
        this.id = id
        this.button = button
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