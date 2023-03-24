package com.example.hiddenghosts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiddenghosts.data.*
import com.example.hiddenghosts.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()
    private val _gameFinishedEvent = MutableSharedFlow<Unit>()
    val gameFinishedEvent = _gameFinishedEvent.asSharedFlow()

    private val ghosts = listOf(
        R.drawable.ic_ghost_1,
        R.drawable.ic_ghost_2,
        R.drawable.ic_ghost_3,
        R.drawable.ic_ghost_4,
        R.drawable.ic_ghost_5
    )

    fun startGame() {
        viewModelScope.launch {
            setLevel()
            delay(_state.value.gridAnimationDuration)
            populateCells()
            showGhostsPreview()
            setGameProgressStatus(GameProgressStatus.InProgress)
        }
    }

    fun restartGame() {
        viewModelScope.launch {
            setGameProgressStatus(GameProgressStatus.Loading)
            populateCells(isRestart = true)
            showGhostsPreview()
            setGameProgressStatus(GameProgressStatus.InProgress)
        }
    }

    fun onCellClick(index: Int) {
        _state.update { state ->
            val cell = state.cells[index]
            val newState = when (cell.state) {
                CellState.Idle -> cell.imageRes?.let { CellState.Success } ?: CellState.Error
                else -> cell.state
            }
            if (cell.state == newState) return@update state
            val newScore =
                if (newState == CellState.Success) state.score + Constants.GUESSED_GHOST_SCORE
                else state.score
            state.copy(
                cells = state.cells.mapIndexed { i, c ->
                    if (i == index) c.copy(state = newState)
                    else c
                },
                tryCount = state.tryCount + 1,
                score = newScore
            )
        }
        if (_state.value.allTriesUsed) {
            finishGame()
        }
    }

    private fun setLevel() {
        with(_state.value) {
            val newLevel = if (status == GameProgressStatus.Success) level.nextLevel else level
            val cells = List(newLevel.cellCount) { Cell() }
            _state.update {
                it.copy(
                    level = newLevel,
                    cells = cells,
                    status = GameProgressStatus.Loading
                )
            }
        }
    }

    private fun populateCells(isRestart: Boolean = false) {
        _state.update { state ->
            val cells =
                if (isRestart) List(state.level.cellCount) { Cell() } else state.cells
            val randomCells = cells.shuffled().take(state.level.ghostCount)
            var availableGhosts = ghosts.toMutableList()
            val cellsWithGhosts = cells.map { cell ->
                if (randomCells.any { it === cell }) {
                    if (availableGhosts.isEmpty()) availableGhosts = ghosts.toMutableList()
                    cell.copy(imageRes = availableGhosts.removeFirst())
                } else {
                    cell
                }
            }
            state.copy(
                cells = cellsWithGhosts,
                tryCount = 0,
                score = 0
            )
        }
    }

    private fun setGhostCellsState(state: CellState) {
        _state.update {
            it.copy(
                cells = it.cells.map { cell ->
                    if (cell.imageRes == null) cell
                    else cell.copy(state = state)
                }
            )
        }
    }

    private fun setGameProgressStatus(status: GameProgressStatus) {
        _state.update {
            it.copy(status = status)
        }
    }

    private suspend fun showGhostsPreview() {
        setGhostCellsState(CellState.Preview)
        delay(Constants.GHOSTS_PREVIEW_DURATION)
        setGhostCellsState(CellState.Idle)
    }

    private fun finishGame() {
        val status =
            if (_state.value.cells.any { it.state == CellState.Error }) GameProgressStatus.Failure
            else GameProgressStatus.Success
        setGameProgressStatus(status)
        if (status == GameProgressStatus.Failure) showNotFoundGhosts()
        viewModelScope.launch {
            _gameFinishedEvent.emit(Unit)
        }
    }

    private fun showNotFoundGhosts() {
        val cells = _state.value.cells.map {
            val isGhostNotFound = it.imageRes != null && it.state == CellState.Idle
            if (isGhostNotFound) it.copy(state = CellState.Preview) else it
        }
        _state.update {
            it.copy(cells = cells)
        }
    }
}