package com.example.hiddenghosts.data

import androidx.annotation.DrawableRes

data class Cell(
    val state: CellState = CellState.Idle,
    @DrawableRes val imageRes: Int? = null
)
