package com.example.myapplication

import android.content.Context
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout

class Field {
    private var enabled = true
    private var grid: GridLayout? = null
    private var successToast: Toast? = null
    private var width: Int? = null
    private var height: Int? = null
    private val cells = mutableMapOf<Int, Cell>()
    private val emojies = arrayOf(
        "\uD83D\uDC7D", // 👽 Alien
        "\uD83D\uDC3B", // 🐻 Bear
        "\uD83D\uDC31", // 🐱 Cat
        "\uD83D\uDC14", // 🐔 Chicken
        "\uD83D\uDC36", // 🐶 Dog
        "\uD83D\uDC2C", // 🐬 Dolphin
        "\uD83D\uDC32", // 🐲 Dragon
        "\uD83C\uDF83", // 🎃 Jack-O-Lantern
        "\uD83C\uDF1F", // 🌟 Glowing Star
        "\uD83C\uDF3A", // 🌺 Hibiscus
        "\uD83D\uDC1D", // 🐝 Honeybee
        "\uD83D\uDC27",  // 🐧 Octopus
        "\uD83D\uDC3C", // 🐼 Panda
        "\uD83D\uDC37", // 🐷 Pig
        "\uD83C\uDF08", // 🌈 Rainbow
        "\uD83D\uDC3A" // 🐺 Wolf
    )

    constructor(width: Int, height: Int) {
        val pairsCount = (width * height) / 2
        if (pairsCount > this.emojies.size) {
            throw Exception("I do not have emojies enough (${this.emojies.size}) to create $pairsCount pairs")
        }
        if (width % 2 > 0 && height % 2 > 0) {
            throw Exception("Count of cells must be even")
        }

        this.width = width
        this.height = height
    }

    private fun getChars(pairsCount: Int): MutableList<String> {
        val chars = mutableListOf<String>()

        val pairsLastIndex = pairsCount - 1

        for (i in 0..pairsLastIndex) {
            chars.add(this.emojies[i])
            chars.add(this.emojies[i])
        }

        chars.shuffle()

        return chars
    }

    fun init(context: Context, grid: GridLayout) {
        var cellId = 1;
        this.successToast = Toast.makeText(context, "Ты молодец \uD83C\uDF1A", Toast.LENGTH_LONG)
        this.grid = grid

        for (w in 1..this.width!!) {
            for (h in 1..this.height!!) {
                var button = Button(context)
                val cell = Cell(
                  cellId,
                  button,
                  onClick = { id -> this.onCellClick(id) }
                )
                this.cells[cellId] = cell
                this.grid?.addView(button)
                cellId++
            }
        }
        this.initChars()
        this.render()
    }

    private fun onCellClick(id: Int) {
        if (!this.enabled) {
            return
        }
        showCell(id)
        val openedCells = this.cells.filter { (key, cell) -> cell.isVisible() && !cell.isMatched() }
        val openedCellsChars = mutableSetOf<String>()
        val matchTarget = 2
        if (openedCells.size == matchTarget) {
            for ((id, cell) in openedCells) {
                openedCellsChars.add(cell.getChar())
            }

            val isMatched = openedCellsChars.size == 1
            if (isMatched) {
                for ((id, cell) in openedCells) {
                    cell.match()
                }
            } else {
                this.disable()
                Handler().postDelayed(
                    {
                        this.hideUnmatchedCells()
                        this.enable()
                    },
                    1000
                )
            }
        }

        val matchedCells = this.cells.filter { (key, cell) -> cell.isMatched() }
        if (matchedCells.size == this.cells.size) {
            this.successToast?.show()
            this.reset()
        }
    }

    private fun disable() {
        this.enabled = false
    }

    private fun enable() {
        this.enabled = true
    }

    private fun showCell(cellId: Int) {
        for ((id, cell) in this.cells) {
            if (id === cellId) {
                cell.show()
            }
        }
    }

    private fun reset() {
        for ((id, cell) in this.cells) {
            cell.reset()
        }

        this.initChars()
        this.render()
    }

    private fun initChars() {
        val chars = this.getChars((this.width!! * this.height!!) / 2)
        var charIndex = 0;
        for ((id, cell) in this.cells) {
            cell.setChar(chars[charIndex])
            charIndex++
        }
    }

    private fun render() {
        for ((id, cell) in this.cells) {
            cell.render()
        }
    }

    private fun hideUnmatchedCells() {
        val unmatchedCells = this.cells.filter { (key, cell) -> !cell.isMatched() }
        for ((id, cell) in unmatchedCells) {
            cell.hide()
        }
    }
}