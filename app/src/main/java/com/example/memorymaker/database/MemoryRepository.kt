package com.example.memorymaker.database

import android.content.Context
import android.content.LocusId
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.memorymaker.Memory
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val DATABASE_NAME = "Memory-database"

class MemoryRepository private constructor(context: Context) {

    private val database: MemoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        MemoryDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2).build()

    private val memoryDao = database.memoryDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getMemories(): LiveData<List<Memory>> = memoryDao.getMemories()
    fun getMemory(id: UUID): LiveData<Memory?> = memoryDao.getMemory(id)

    fun updateMemory(memory: Memory) {
        executor.execute {
            memoryDao.updateMemory(memory)
        }
    }

    fun addMemory(memory: Memory) {
        executor.execute {
            memoryDao.addMemory(memory)
        }
    }

    fun getPhotoFile(memory: Memory) : File = File(filesDir, memory.photoFileName)

    companion object {
        private var INSTANCE: MemoryRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MemoryRepository(context)
            }
        }

        fun get(): MemoryRepository {
            return INSTANCE ?: throw IllegalStateException("Memory Repository must be initialized")
        }
    }

}