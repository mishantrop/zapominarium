package com.puregames.zapominarium

import android.content.Context
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout

class Field {
    private var isEnabled = true
    private var grid: GridLayout? = null
    private var successToast: Toast? = null
    private var width: Int? = null
    private var height: Int? = null
    private var maxWidth = 5
    private var maxHeight = 8
    private val matchTargetCount = 2
    private var context: Context? = null
    private val cells = mutableMapOf<Int, Cell>()
    private val emojies = arrayOf(
        "\uD83D\uDC7D", // 👽 Alien
        "\uD83D\uDC3B", // 🐻 Bear
        "\uD83D\uDCA3", // 💣 Bomb
        "\uD83D\uDC31", // 🐱 Cat
        "\uD83D\uDC36", // 🐶 Dog
        "\uD83D\uDC2C", // 🐬 Dolphin
        "\uD83D\uDC32", // 🐲 Dragon
        "\uD83D\uDD25", // 🔥 Fire
        "\uD83D\uDC8E", // 💎 Gem Stone
        "\uD83C\uDF1F", // 🌟 Glowing Star
        "\uD83D\uDC1D", // 🐝 Honeybee
        "\uD83C\uDF83", // 🎃 Jack-O-Lantern
        "\uD83D\uDC27", // 🐧 Octopus
        "\uD83D\uDC79", // 👹 Ogre
        "\uD83D\uDC3C", // 🐼 Panda
        "\uD83D\uDC37", // 🐷 Pig
        "\uD83C\uDF08", // 🌈 Rainbow
        "\uD83C\uDF4E", // 🍎 Red Apple
        "\uD83C\uDF85", // 🎅 Santa Claus
        "\uD83C\uDF81", // 🎁 Wrapped Gift
        "\uD83D\uDC3A"  // 🐺 Wolf
    )

    public fun init(context: Context, grid: GridLayout, width: Int, height: Int) {
        this.context = context
        val pairsCount = (width * height) / this.matchTargetCount
        if (pairsCount > this.emojies.size) {
            throw Exception("I do not have emojies enough (${this.emojies.size}) to create $pairsCount pairs")
        }
        if (width % this.matchTargetCount > 0 && height % this.matchTargetCount > 0) {
            throw Exception("Count of cells must be even")
        }
        if (width > this.maxWidth || height > this.maxHeight) {
            throw Exception("Maximum size: ${this.maxWidth}x${this.maxHeight}")
        }


        this.width = width
        this.height = height

        this.successToast = Toast.makeText(this.context!!, "Ты молодец \uD83C\uDF1A", Toast.LENGTH_LONG)
        this.grid = grid

        this.reset()

        this.grid?.columnCount = this.width!!
        this.grid?.rowCount = this.height!!

        this.render()
    }

    public fun decreaseComplexity() {
        var width = this.width!!
        var height = this.height!!

        if (width % 2 != 0) {
            width--
        } else if (height % 2 != 0) {
            height--
        } else if (height > width) {
            height--
        } else {
            width--
        }

        if (width <= 0 || height <= 0) {
            return
        }

        this.init(this.context!!, this.grid!!, width, height)
    }

    public fun increaseComplexity() {
        var width = this.width!!
        var height = this.height!!

        if (width % 2 != 0 && width < this.maxWidth) {
            width++
        } else if (height % 2 != 0 && height < this.maxHeight) {
            height++
        } else if (height > width) {
            width++
        } else {
            height++
        }

        if (width > this.maxWidth || height > this.maxHeight) {
            return
        }

        this.init(this.context!!, this.grid!!, width, height)
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

    private fun initCells() {
        this.grid?.removeAllViews()
        this.cells.clear()

        var cellId = 1;

        for (w in 1..this.width!!) {
            for (h in 1..this.height!!) {
                var button = Button(this.context)
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
    }

    private fun onCellClick(cellId: Int) {
        if (!this.isEnabled) {
            return
        }
        showCell(cellId)
        val openedCells = this.cells.filter { (_, cell) -> cell.isVisible() && !cell.isMatched() }
        val openedCellsChars = mutableSetOf<String>()
        if (openedCells.size == this.matchTargetCount) {
            for ((_, cell) in openedCells) {
                openedCellsChars.add(cell.getChar())
            }

            val isMatched = openedCellsChars.size == 1
            if (isMatched) {
                for ((_, cell) in openedCells) {
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

        val matchedCells = this.cells.filter { (_, cell) -> cell.isMatched() }
        if (matchedCells.size == this.cells.size) {
            this.successToast?.show()
            this.reset()
            this.render()
        }
    }

    private fun disable() {
        this.isEnabled = false
    }

    private fun enable() {
        this.isEnabled = true
    }

    private fun showCell(cellId: Int) {
        for ((id, cell) in this.cells) {
            if (id == cellId) {
                cell.show()
            }
        }
    }

    private fun reset() {
        this.initCells()
    }

    private fun initChars() {
        val chars = this.getChars((this.width!! * this.height!!) / 2)
        var charIndex = 0;
        for ((_, cell) in this.cells) {
            cell.setChar(chars[charIndex])
            charIndex++
        }
    }

    private fun render() {
        for ((_, cell) in this.cells) {
            cell.render()
        }
    }

    private fun hideUnmatchedCells() {
        val unmatchedCells = this.cells.filter { (_, cell) -> !cell.isMatched() }
        for ((_, cell) in unmatchedCells) {
            cell.hide()
        }
    }
}