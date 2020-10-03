package com.example.fallingwords.data.model

data class Word(
    var text_eng: String,
    var text_spa: String,
    var isShown: Boolean = false,
    var randomTranslatedWord: String? = null
)