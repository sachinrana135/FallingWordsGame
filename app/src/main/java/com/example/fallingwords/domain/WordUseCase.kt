package com.example.fallingwords.domain

import com.example.fallingwords.data.WordsRepo
import com.example.fallingwords.data.model.Word

class WordUseCase {

    val repo = lazy { WordsRepo() }

    fun getAllWords(): List<Word> {
        return listOf(
            Word(text_eng = "English", text_spa = "Spanish"),
            Word(text_eng = "English1", text_spa = "Spanish1"),
            Word(text_eng = "English2", text_spa = "Spanish2"),
            Word(text_eng = "English3", text_spa = "Spanish3"),
            Word(text_eng = "English4", text_spa = "Spanish4"),
            Word(text_eng = "English5", text_spa = "Spanish5"),
            Word(text_eng = "English6", text_spa = "Spanish6"),
            Word(text_eng = "English7", text_spa = "Spanish7"),
            Word(text_eng = "English8", text_spa = "Spanish8"),
            Word(text_eng = "English9", text_spa = "Spanish9")
        )
    }

}