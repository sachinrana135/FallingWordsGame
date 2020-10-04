package com.example.fallingwords.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fallingwords.data.model.Session
import com.example.fallingwords.data.model.Word
import com.example.fallingwords.domain.WordUseCase
import com.example.fallingwords.game.GameManager
import com.example.fallingwords.game.GameManagerImpl
import com.example.fallingwords.util.UserResponse
import com.example.fallingwords.util.WORD_FALLING_DURATION


class MainViewModel(
    val gameManager: GameManager = GameManagerImpl,
    val useCase: WordUseCase = WordUseCase()
) : ViewModel() {

    private var timer: CountDownTimer? = null
    private var _activeWord = MutableLiveData<Word?>()
    val activeWord: LiveData<Word?> = _activeWord
    private var _liveTimer = MutableLiveData<Long>()
    val liveTimer: LiveData<Long?> = _liveTimer
    private var _userSelectedAnswer = MutableLiveData<UserResponse>()
    val userSelectedAnswer: LiveData<UserResponse?> = _userSelectedAnswer
    private var _session = MutableLiveData<Session>()
    val session: LiveData<Session?> = _session
    private var _isGameOver = MutableLiveData<Boolean>()
    val isGameOver: LiveData<Boolean?> = _isGameOver

    fun startGame() {
        if (gameManager.getGameSession() == null) {
            val words = useCase.getAllWords()
            gameManager.startGame(words)
        }
    }

    fun restartGame() {
        val words = useCase.getAllWords()
        gameManager.startGame(words)
    }

    fun showNewWord() {
        if (!gameManager.isGameOver()) {
            _activeWord.value = gameManager.getNewWord()
        } else {
            gameManager.endGame()
            _isGameOver.value = true
        }
    }

    fun startTimer() {
        timer = object : CountDownTimer((WORD_FALLING_DURATION).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _liveTimer.value = (millisUntilFinished / 1000)
            }

            override fun onFinish() {
                gameManager.getGameSession()?.attempted =
                    gameManager.getGameSession()!!.attempted.plus(1)
                _userSelectedAnswer.value = UserResponse.UNATTEMPTED
            }
        }.start()

    }

    fun stopTimer() {
        timer?.cancel()
    }

    fun onCorrectButtonClicked() {
        gameManager.getGameSession()?.attempted = gameManager.getGameSession()!!.attempted.plus(1)
        if (activeWord.value?.text_spa == activeWord.value?.randomTranslatedWord) {
            gameManager.getGameSession()?.correct = gameManager.getGameSession()!!.correct.plus(1)
            _userSelectedAnswer.value = UserResponse.CORRECT
        } else {
            _userSelectedAnswer.value = UserResponse.WRONG
        }
    }

    fun onWrongButtonClicked() {
        gameManager.getGameSession()?.attempted = gameManager.getGameSession()!!.attempted.plus(1)
        if (activeWord.value?.text_spa != activeWord.value?.randomTranslatedWord) {
            gameManager.getGameSession()?.correct = gameManager.getGameSession()!!.correct.plus(1)
            _userSelectedAnswer.value = UserResponse.CORRECT
        } else {
            _userSelectedAnswer.value = UserResponse.WRONG
        }
    }

    fun getSession() {
        _session.value = gameManager.getGameSession()
    }

}