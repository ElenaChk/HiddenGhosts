package com.example.hiddenghosts.data

enum class Level(
    val columnCount: Int,
    val rowCount: Int,
    val ghostCount: Int
) {
    One(4, 4, 4),
    Two(4, 5, 5),
    Three(4, 6, 6),
    Four(5, 6, 7),
    Five(5, 7, 8);

    val nextLevel
        get() = when (this) {
            One -> Two
            Two -> Three
            Three -> Four
            Four -> Five
            Five -> One
        }

    val cellCount get() = columnCount * rowCount
}