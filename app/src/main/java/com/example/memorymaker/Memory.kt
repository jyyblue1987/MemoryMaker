package com.example.memorymaker


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Memory(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isFavorite: Boolean = false,
    var pet: String = ""
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
