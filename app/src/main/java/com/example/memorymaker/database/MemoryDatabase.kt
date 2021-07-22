package com.example.memorymaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.memorymaker.Memory


@Database(entities = [ Memory::class ], version=2)
@TypeConverters(MemoryTypeConverters::class)
abstract class MemoryDatabase: RoomDatabase() {

    abstract fun memoryDao() : MemoryDao

}

val migration_1_2 = object: Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Memory ADD COLUMN pet TEXT NOT NULL DEFAULT ''"
        )
    }
}