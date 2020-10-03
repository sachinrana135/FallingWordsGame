package com.example.fallingwords.game

import com.example.fallingwords.data.model.Word

interface GameManager {

    fun startGame(questions: List<Word>)

    fun endGame()

    fun getNewWord(): Word?

    fun markWordUsed(word: Word)

}