package com.example.fallingwords

import android.app.Application
import com.example.fallingwords.util.FileReader

class FwApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FileReader.context = applicationContext
    }
}