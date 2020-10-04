package com.example.fallingwords.game

import com.example.fallingwords.data.model.Word
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class GameManagerImplTest {


    lateinit var subject: GameManager

    val fakeshownWord =
        Word(
            text_eng = "English1",
            text_spa = "Spanish1",
            randomTranslatedWord = "Spanish1",
            isShown = true
        )
    val fakeNotShownWord =
        Word(text_eng = "English2", text_spa = "Spanish2", randomTranslatedWord = "German")
    val fakeWordsList = listOf<Word>(fakeshownWord, fakeNotShownWord)


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = GameManagerImpl
    }


    @Test
    fun `should return word that is not shown to user`() {
        subject.startGame(fakeWordsList)
        val result = subject.getNewWord()
        assert(result != fakeshownWord)

    }

    @Test
    fun `should make the session null on end game`() {
        subject.startGame(fakeWordsList)
        subject.endGame()
        assert(subject.getGameSession() == null)

    }

    @Test
    fun `should return game over if attempt is greater than or equal to total words in session`() {
        subject.startGame(fakeWordsList)
        subject.getGameSession()?.attempted = 2
        assert(subject.isGameOver())

    }

    @Test
    fun `should not return game over if attempt is less of total words in session`() {
        subject.startGame(fakeWordsList)
        subject.getGameSession()?.attempted = 1
        assert(!subject.isGameOver())

    }
}