package com.example.hiddenghosts.data

import androidx.annotation.DrawableRes

data class Cell(
    val id: Int,
    val state: CellState = CellState.Idle,
    @DrawableRes val imageRes: Int? = null
)
