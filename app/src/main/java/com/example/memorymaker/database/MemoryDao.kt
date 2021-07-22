package com.example.memorymaker.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.memorymaker.Memory
import java.util.*

@Dao
interface MemoryDao {

    @Query("SELECT * fROM Memory")
    fun getMemories() : List<Memory>

    @Query("SELECT * fROM Memory WHERE isFavorite = 1")
    fun getFavorites() : List<Memory>

    @Query("SELECT * fROM Memory WHERE id=(:id)")
    fun getMemory(id: UUID) : LiveData<Memory?>

    @Update
    fun updateMemory(memory: Memory)

    @Delete
    fun deleteMemory(memory: Memory)

    @Insert
    fun addMemory(memory: Memory)

}