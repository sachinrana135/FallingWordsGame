package com.example.fallingwords.game

import com.example.fallingwords.data.model.Session
import com.example.fallingwords.data.model.Word

interface GameManager {

    fun getGameSession(): Session?

    fun startGame(questions: List<Word>)

    fun endGame()

    fun getNewWord(): Word?

    fun markWordUsed(word: Word)

    fun setGameOver()

    fun isGameOver(): Boolean

}