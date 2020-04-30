package com.example.myapplication

import android.content.Context
import android.widget.Button

class Cell {
    private var id: Int? = null
    private var button: Button? = null
    private var char: String? = null
    private var isMatched = false
    private var isVisible = false

    constructor(
      id: Int,
      button: Button,
      onClick: (id: Int) -> Unit
    ) {
        this.id = id
        this.button = button
        this.button?.setOnClickListener {
            if (!this.isVisible) {
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
        this.button?.isEnabled = !this.isVisible

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

    public fun show() {
        this.isVisible = true
        this.render()
    }

    public fun match() {
        this.isMatched = true
        this.render()
    }

    private fun unmatch() {
        this.isMatched = false
        this.render()
    }

    public fun reset() {
        this.unmatch()
        this.hide()
    }
}