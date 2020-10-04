package com.example.fallingwords.data.model

data class Session(
    var WordsPerSession: Int = 0,
    var attempted: Int = 0,
    var correct: Int = 0,
    val availableWords: List<Word>? = null,
    var isGameOver: Boolean = false
)