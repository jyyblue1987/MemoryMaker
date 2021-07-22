package com.example.memorymaker

import java.util.*

interface Callbacks {
    fun onMemorySelected(Memory: UUID)
    fun onBackPressed()
}