package com.example.memorymaker

import android.app.Application
import com.example.memorymaker.database.MemoryRepository

class MemoryMakerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MemoryRepository.initialize(this)
    }

}