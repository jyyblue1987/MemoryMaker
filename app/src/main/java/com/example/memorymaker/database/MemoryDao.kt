package com.example.memorymaker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.memorymaker.Memory
import java.util.*

@Dao
interface MemoryDao {

    @Query("SELECT * fROM Memory")
    fun getMemories() : LiveData<List<Memory>>

    @Query("SELECT * fROM Memory WHERE id=(:id)")
    fun getMemory(id: UUID) : LiveData<Memory?>

    @Update
    fun updateMemory(memory: Memory)

    @Insert
    fun addMemory(memory: Memory)

}