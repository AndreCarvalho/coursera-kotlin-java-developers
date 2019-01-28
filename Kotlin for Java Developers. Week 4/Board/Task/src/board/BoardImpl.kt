package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

fun Any?.ignore() = Unit

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells: Array<Array<Cell>>
    init {
        var tempArray = arrayOf<Array<Cell>>()

        for (i in 1..width) {
            var row = arrayOf<Cell>()
            for (j in 1..width) {
                row += Cell(i, j)
            }
            tempArray += row
        }

        cells = tempArray
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return cells.elementAtOrNull(i - 1)?.elementAtOrNull(j - 1)
    }

    override fun getCell(i: Int, j: Int): Cell {
        validateIndex(i)
        validateIndex(j)

        return getCellOrNull(i, j) !!
    }

    override fun getAllCells(): Collection<Cell> = cells.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        validateIndex(i)

        val row = cells[i - 1]
        return jRange.filter { it in 1..width }.map { row[it - 1] }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        validateIndex(j)

        val column = cells.flatMap { row -> listOf(row[j - 1]) }
        return iRange.filter { it in 1..width }.map { column[it - 1] }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            DOWN -> getCellOrNull(this.i + 1, this.j)
            UP -> getCellOrNull(this.i - 1, this.j)
            LEFT -> getCellOrNull(this.i, this.j - 1)
            RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }

    private fun validateIndex(index: Int) {
        if (index !in 1..width)
            throw IllegalArgumentException()
    }
}

class GameBoardImpl<T>(override val width: Int) : GameBoard<T>, SquareBoard by SquareBoardImpl(width) {
    private val valuesMap: MutableMap<Cell, T?>

    init {
        val allCells = getAllCells()
        valuesMap = allCells.zip((1..allCells.size).map { null as T }).toMap().toMutableMap()
    }

    override fun get(cell: Cell): T? = valuesMap[cell]

    override fun set(cell: Cell, value: T?) = valuesMap.put(cell, value).ignore()

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            valuesMap.filterValues { predicate(it) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean = filter(predicate).any()

    override fun all(predicate: (T?) -> Boolean): Boolean = valuesMap.all { predicate(it.value) }
}