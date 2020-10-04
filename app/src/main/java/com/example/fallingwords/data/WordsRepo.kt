package com.example.fallingwords.data

import com.example.fallingwords.R
import com.example.fallingwords.data.model.Word
import com.example.fallingwords.util.FileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type


class WordsRepo {

    fun getAllWords(): List<Word> {
        var result: List<Word> = listOf()
        runBlocking {
            launch(Dispatchers.IO) {
                val type: Type = object : TypeToken<List<Word?>?>() {}.type
                result = Gson().fromJson(FileReader.readFile(R.raw.words_v2), type)
            }
        }
        return result
    }
}