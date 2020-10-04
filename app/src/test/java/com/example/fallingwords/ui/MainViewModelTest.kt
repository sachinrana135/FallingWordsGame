package com.example.fallingwords.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.fallingwords.data.model.Session
import com.example.fallingwords.data.model.Word
import com.example.fallingwords.domain.WordUseCase
import com.example.fallingwords.game.GameManager
import com.example.fallingwords.util.UserResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class MainViewModelTest {


    lateinit var subject: MainViewModel

    @Mock
    lateinit var gameManager: GameManager

    @Mock
    lateinit var useCase: WordUseCase

    @Mock
    lateinit var activeWordObserver: Observer<Word?>

    @Mock
    lateinit var gameOverObserver: Observer<Boolean?>

    @Mock
    lateinit var userSelectedAnswerObserver: Observer<UserResponse?>

    @Mock
    lateinit var sessionObserver: Observer<Session?>

    @Mock
    lateinit var timerObserver: Observer<Long>

    val fakeCorrectWord =
        Word(text_eng = "English", text_spa = "Spanish", randomTranslatedWord = "Spanish")
    val fakeWrongWord =
        Word(text_eng = "English", text_spa = "Spanish", randomTranslatedWord = "German")
    val fakeWordsList = listOf<Word>(fakeCorrectWord, fakeWrongWord)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        subject = MainViewModel(gameManager, useCase)
    }

    @Test
    fun `should start game over if new session`() {

        `when`(gameManager.getGameSession()).thenReturn(null)
        `when`(useCase.getAllWords()).thenReturn(fakeWordsList)
        subject.startGame()
        verify(gameManager).startGame(fakeWordsList)

    }

    @Test
    fun `should return new word if game is not over`() {

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        subject.showNewWord()

        subject.activeWord.observeForever(activeWordObserver)

        verify(activeWordObserver).onChanged(fakeCorrectWord)

    }

    @Test
    fun `should return game over if game is over`() {

        `when`(gameManager.isGameOver()).thenReturn(true)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        subject.showNewWord()

        subject.isGameOver.observeForever(gameOverObserver)

        verify(gameManager).endGame()
        verify(gameOverObserver).onChanged(true)

    }

    @Test
    fun `should start game if restart function is invoked`() {

        `when`(useCase.getAllWords()).thenReturn(fakeWordsList)
        subject.restartGame()
        verify(gameManager).startGame(fakeWordsList)

    }

    @Test
    fun `should emit the CORRECT event if user is clicked correct button and translation is matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onCorrectButtonClicked()

        subject.userSelectedAnswer.observeForever(userSelectedAnswerObserver)

        verify(userSelectedAnswerObserver).onChanged(UserResponse.CORRECT)


    }

    @Test
    fun `should emit the WRONG event if user is clicked correct button BUT translation is NOT matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeWrongWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onCorrectButtonClicked()

        subject.userSelectedAnswer.observeForever(userSelectedAnswerObserver)

        verify(userSelectedAnswerObserver).onChanged(UserResponse.WRONG)


    }

    @Test
    fun `should emit the WRONG event if user is clicked wrong button BUT translation is  matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onWrongButtonClicked()

        subject.userSelectedAnswer.observeForever(userSelectedAnswerObserver)

        verify(userSelectedAnswerObserver).onChanged(UserResponse.WRONG)


    }

    @Test
    fun `should emit the CORRECT event if user is clicked wrong button and translation is  not matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeWrongWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onWrongButtonClicked()

        subject.userSelectedAnswer.observeForever(userSelectedAnswerObserver)

        verify(userSelectedAnswerObserver).onChanged(UserResponse.CORRECT)

    }

    @Test
    fun `should increment the score and attempt by 1 if user clicked correct button and translation is matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onCorrectButtonClicked()

        subject.session.observeForever(sessionObserver)
        assert(gameManager.getGameSession()?.attempted == 6)
        assert(gameManager.getGameSession()?.correct == 3)


    }

    @Test
    fun `should increment the attempt by 1 but not score if user clicked correct button and translation is not matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeWrongWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onCorrectButtonClicked()

        subject.session.observeForever(sessionObserver)
        assert(gameManager.getGameSession()?.attempted == 6)
        assert(gameManager.getGameSession()?.correct == 2)


    }

    @Test
    fun `should increment the score and attempt by 1 if user clicked worng button and translation is not matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeWrongWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onWrongButtonClicked()

        subject.session.observeForever(sessionObserver)

        assert(gameManager.getGameSession()?.attempted == 6)
        assert(gameManager.getGameSession()?.correct == 3)


    }

    @Test
    fun `should increment the attempt by 1 but not score if user clicked wrong button but translation not matching`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.isGameOver()).thenReturn(false)
        `when`(gameManager.getNewWord()).thenReturn(fakeCorrectWord)
        `when`(gameManager.getGameSession()).thenReturn(fakeSession)

        subject.showNewWord()

        Thread.sleep(1000)

        subject.onWrongButtonClicked()

        subject.session.observeForever(sessionObserver)
        assert(gameManager.getGameSession()?.attempted == 6)
        assert(gameManager.getGameSession()?.correct == 2)

    }

    @Test
    fun `should emit session if getSession is invoked`() {

        val fakeSession = Session(
            WordsPerSession = 10,
            attempted = 5,
            correct = 2,
            availableWords = fakeWordsList
        )

        `when`(gameManager.getGameSession()).thenReturn(fakeSession)
        subject.getSession()
        subject.session.observeForever(sessionObserver)

    }
}