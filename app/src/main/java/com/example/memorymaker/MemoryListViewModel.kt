package com.example.memorymaker


import android.os.AsyncTask
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memorymaker.database.MemoryRepository


class MemoryListViewModel: ViewModel() {

    private val memoryRepository = MemoryRepository.get()
//    var memories = memoryRepository.getMemories()

    var memories = MutableLiveData<List<Memory>>()

    fun addMemory(memory: Memory){
        memoryRepository.addMemory(memory)
    }

    fun getFavorites() {
        AsyncTask.execute { // Insert Data
            memories.postValue(memoryRepository.getFavorites())
        }

    }

    fun getAll() {
//        memories = memoryRepository.getMemories()
        AsyncTask.execute { // Insert Data
            memories.postValue(memoryRepository.getMemories())
        }
    }

}