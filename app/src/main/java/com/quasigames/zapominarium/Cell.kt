package com.quasigames.zapominarium

import android.widget.Button

class Cell {
    private var id: Int? = null
    private var button: Button? = null
    private var char: String? = null
    private var isMatched = false
    private var isVisible = false
    private var clickCount = 0

    constructor(
      id: Int,
      button: Button,
      onClick: (id: Int) -> Unit
    ) {
        this.id = id
        this.button = button
        this.button?.setOnClickListener {
            if (!this.isVisible) {
                this.clickCount++
                onClick(this.id!!)
            }
        }
    }

    public fun isMatched(): Boolean {
        return this.isMatched
    }

    public fun isVisible(): Boolean {
        return this.isVisible
    }

    public fun getChar(): String {
        return this.char!!
    }

    public fun setChar(char: String) {
        this.char = char
    }

    public fun render() {
        if (this.isVisible) {
            this.button?.text = this.char
        } else {
            this.button?.text = "❓"
        }
    }

    public fun hide() {
        this.isVisible = false
        this.render()
    }

    public fun setMatched() {
        this.isMatched = true
        this.render()
    }

    public fun show() {
        this.isVisible = true
        this.render()
    }
}