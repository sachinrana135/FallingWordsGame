package com.example.fallingwords.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fallingwords.data.model.Word
import com.example.fallingwords.domain.WordUseCase
import com.example.fallingwords.game.GameManager
import com.example.fallingwords.game.GameManagerImpl

class MainViewModel : ViewModel() {

    private val gameManager = GameManagerImpl as GameManager
    private val useCase by lazy { WordUseCase() }
    private var _activeWord = MutableLiveData<Word?>()
    val activeWord: LiveData<Word?> = _activeWord


    fun startGame() {
        val words = useCase.getAllWords()
        gameManager.startGame(words)
        _activeWord.value = gameManager.getNewWord()
    }


    fun showNewWord() {

        gameManager.getNewWord()

    }


}