package com.quasigames.zapominarium

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.gridlayout.widget.GridLayout

class Field {
    private var context: Context? = null
    private var grid: GridLayout? = null
    private var successToast: Toast? = null

    private var maxWidth = 8
    private var maxHeight = 8
    private var viewportHeight = 0
    private var viewportWidth = 0
    private var width: Int? = null
    private var height: Int? = null

    private var isEnabled = true
    private val matchTargetCount = 2
    private val cells = mutableMapOf<Int, Cell>()
    private val emojis = mutableListOf<String>(
        "\uD83D\uDC7D", // 👽 Alien
        "\uD83D\uDC3B", // 🐻 Bear
        "\uD83D\uDCA3", // 💣 Bomb
        "\uD83D\uDC31", // 🐱 Cat
        "\uD83D\uDC2E", // 🐮 Cow Face
        "\uD83D\uDC36", // 🐶 Dog
        "\uD83D\uDC2C", // 🐬 Dolphin
        "\uD83D\uDC32", // 🐲 Dragon
        "\uD83D\uDC18", // 🐘 Elephant
        "\uD83D\uDD25", // 🔥 Fire
        "\uD83C\uDF40", // 🍀 Four Leaf Clover
        "\uD83D\uDC38", // 🐸 Frog
        "\uD83D\uDC8E", // 💎 Gem Stone
        "\uD83C\uDF1F", // 🌟 Glowing Star
        "\uD83D\uDC1D", // 🐝 Honeybee
        "\uD83D\uDC0E", // 🐎 Horse
        "\uD83C\uDFE0", // 🏠 House
        "\uD83C\uDF83", // 🎃 Jack-O-Lantern
        "\uD83D\uDC1E", // 🐞 Lady Beetle
        "\uD83D\uDDFF", // 🗿 Moai
        "\uD83D\uDC35", // 🐵 Monkey Face
        "\uD83D\uDC27", // 🐧 Octopus
        "\uD83D\uDC79", // 👹 Ogre
        "\uD83D\uDC3C", // 🐼 Panda
        "\uD83D\uDC37", // 🐷 Pig
        "\uD83C\uDF08", // 🌈 Rainbow
        "\uD83C\uDF4E", // 🍎 Red Apple
        "\uD83C\uDF85", // 🎅 Santa Claus
        "\uD83D\uDC22", // 🐢 Turtle
        "\uD83C\uDF0B", // 🌋 Volcano
        "\uD83D\uDC3A", // 🐺 Wolf
        "\uD83C\uDF81"  // 🎁 Wrapped Gift
    )

    fun init(context: Context, grid: GridLayout, width: Int, height: Int) {
        this.context = context
        val pairsCount = (width * height) / this.matchTargetCount
        if (pairsCount > this.emojis.size) {
            throw Exception("I do not have emojis enough (total: ${this.emojis.size}) to create $pairsCount pairs")
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

    fun setViewportSize(width: Int, height: Int) {
        this.viewportWidth = width
        this.viewportHeight = height

        this.calcButtonsSize()
    }

    private fun calcButtonsSize() {
        val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels
        val buttonWidth = screenWidthPx / this.width!!
        val buttonHeight = this.viewportHeight / this.height!!
        for ((_, cell) in this.cells) {
            cell.setSize(buttonWidth, buttonHeight)
        }
    }

    private fun disable() {
        this.isEnabled = false
    }

    private fun enable() {
        this.isEnabled = true
    }

    private fun getChars(pairsCount: Int): MutableList<String> {
        val chars = mutableListOf<String>()
        this.emojis.shuffle()
        val pairsLastIndex = pairsCount - 1
        for (i in 0..pairsLastIndex) {
            chars.add(this.emojis[i])
            chars.add(this.emojis[i])
        }
        chars.shuffle()
        return chars
    }

    private fun hideUnmatchedCells() {
        val unmatchedCells = this.cells.filter { (_, cell) -> !cell.isMatched() }
        for ((_, cell) in unmatchedCells) {
            cell.hide()
        }
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
        this.calcButtonsSize()
    }

    private fun initChars() {
        val pairsCount = (this.width!! * this.height!!) / 2
        val chars = this.getChars(pairsCount)
        var charIndex = 0;
        for ((_, cell) in this.cells) {
            cell.setChar(chars[charIndex])
            charIndex++
        }
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
                    cell.setMatched()
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
            this.disable()
            this.successToast?.show()
            Handler().postDelayed(
                {
                    this.reset()
                    this.enable()
                    this.render()
                },
                1000
            )

        }
    }

    private fun render() {
        for ((_, cell) in this.cells) {
            cell.render()
        }
    }

    private fun reset() {
        this.initCells()
    }

    private fun showCell(cellId: Int) {
        for ((id, cell) in this.cells) {
            if (id == cellId) {
                cell.show()
            }
        }
    }

    fun setSize(width: Int, height: Int) {
        this.init(this.context!!, this.grid!!, width, height)
    }

    fun getWidth(): Int {
        return this.width!!
    }

    fun getHeight(): Int {
        return this.height!!
    }

    fun getMaxWidth(): Int {
        return this.maxWidth!!
    }

    fun getMaxHeight(): Int {
        return this.maxHeight!!
    }
}
