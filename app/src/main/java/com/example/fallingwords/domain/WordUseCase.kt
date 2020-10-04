package com.example.fallingwords.domain

import com.example.fallingwords.data.WordsRepo
import com.example.fallingwords.data.model.Word

class WordUseCase {

    private val repo by lazy { WordsRepo() }

    fun getAllWords(): List<Word> {
        return repo.getAllWords()
    }

}