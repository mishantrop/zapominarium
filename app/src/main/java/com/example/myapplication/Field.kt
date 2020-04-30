package com.example.myapplication

import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.gridlayout.widget.GridLayout

class Field(width: Int, height: Int) {
    private var grid: GridLayout? = null
    private var successToast: Toast? = null
    private val width = width
    private val height = height
    private val cells = mutableMapOf<Int, Cell>()
    private val chars = listOf(
        "\uD83D\uDC3A",
        "\uD83D\uDC3A",

        "\uD83D\uDC31",
        "\uD83D\uDC31",

        "\uD83D\uDC37",
        "\uD83D\uDC37",

        "\uD83D\uDC3B",
        "\uD83D\uDC3B",

        "\uD83D\uDC14",
        "\uD83D\uDC14",

        "\uD83D\uDC27",
        "\uD83D\uDC27"
    )

    fun init(context: Context, grid: GridLayout) {
        var cellId = 1;
        this.successToast = Toast.makeText(context, "Ты молодец \uD83C\uDF1A", Toast.LENGTH_LONG)
        this.grid = grid

        for (w in 1..this.width) {
            for (h in 1..this.height) {
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
                this.hideUnmatchedCells()
            }
        }

        val matchedCells = this.cells.filter { (key, cell) -> cell.isMatched() }
        if (matchedCells.size == this.cells.size) {
            this.successToast?.show()
            this.reset()
        }
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
        val shuffledChars = this.chars.shuffled()
        var charIndex = 0;
        for ((id, cell) in this.cells) {
            cell.setChar(shuffledChars[charIndex])
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