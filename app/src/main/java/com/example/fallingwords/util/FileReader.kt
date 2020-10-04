package com.example.fallingwords.util

import android.content.Context

object FileReader {

    var context: Context? = null

    fun readFile(id: Int): String {
        var text = ""
        context?.let { ctx ->
            text = ctx.resources.openRawResource(id)
                .bufferedReader().use { it.readText() }
        }

        return text
    }

}