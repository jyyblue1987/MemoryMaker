package com.example.memorymaker

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.memorymaker.database.MemoryRepository

import java.io.File
import java.util.*

class MemoryDetailViewModel(): ViewModel() {

    private val memoryRepository = MemoryRepository.get()
    private val memoryId = MutableLiveData<UUID>()

    var memoryData: LiveData<Memory?> = Transformations.switchMap(memoryId){
            memoryId -> memoryRepository.getMemory(memoryId)
    }

    fun loadMemory(newMemoryId: UUID){
        memoryId.value = newMemoryId
    }

    fun saveMemory(memory: Memory){
        memoryRepository.updateMemory(memory)
    }

    fun deleteMemory(memory: Memory){
        memoryRepository.deleteMemory(memory)
    }

    fun getPhotoFile(memory: Memory): File {
        return memoryRepository.getPhotoFile(memory)
    }

}