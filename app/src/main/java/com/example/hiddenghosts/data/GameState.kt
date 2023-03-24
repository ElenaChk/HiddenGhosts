package com.example.hiddenghosts.data

import com.example.hiddenghosts.utils.Constants

data class GameState(
    val level: Level = Level.One,
    val score: Int = 0,
    val tryCount: Int = 0,
    val cells: List<Cell> = emptyList(),
    val status: GameProgressStatus = GameProgressStatus.Loading
) {
    val allTriesUsed get() = tryCount == level.ghostCount
    val gridAnimationDuration get() = cells.size * Constants.CELL_ANIMATION_DELAY + 500L
}