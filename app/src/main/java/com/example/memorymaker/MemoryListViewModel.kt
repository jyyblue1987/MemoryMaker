package com.example.memorymaker


import androidx.lifecycle.ViewModel
import com.example.memorymaker.database.MemoryRepository

class MemoryListViewModel: ViewModel() {

    private val memoryRepository = MemoryRepository.get()
    val memories = memoryRepository.getMemories()

    fun addMemory(memory: Memory){
        memoryRepository.addMemory(memory)
    }

}