package com.example.fallingwords.game

import com.example.fallingwords.data.model.Session
import com.example.fallingwords.data.model.Word


object GameManagerImpl : GameManager {

    private const val WORDS_PER_SESSION = 10
    private const val TOTAL_ANSWER_OPTIONS = 3
    var wordsPerSession = 0
    var session: Session? = null

    override fun getGameSession(): Session? {
        return session
    }


    override fun startGame(words: List<Word>) {
        wordsPerSession = if (words.size >= WORDS_PER_SESSION) WORDS_PER_SESSION else words.size
        session = Session(WordsPerSession = wordsPerSession, availableWords = words)
    }

    override fun endGame() {
        session = null
    }

    override fun getNewWord(): Word? {
        //find the words which are not yet shown and select randomly
        val word = session?.availableWords?.filter { !it.isShown }?.random()

        //creating answer options with one correct answer
        var answers = arrayListOf<String?>()
        answers.add(word?.text_spa)
        var filterAnswers = session?.availableWords?.filter { it.text_spa != word?.text_spa }
        if (filterAnswers != null && filterAnswers.isNotEmpty()) {
            for (i in 1 until TOTAL_ANSWER_OPTIONS) {
                answers.add(filterAnswers?.random()?.text_spa)
            }
        }

        // selecting random answer
        word?.randomTranslatedWord = answers.random()
        return word
    }

    override fun markWordUsed(word: Word) {
        session?.availableWords?.first { it == word }?.apply { isShown = true }
    }


    override fun isGameOver(): Boolean {
        return session?.attempted!! >= wordsPerSession
    }


}