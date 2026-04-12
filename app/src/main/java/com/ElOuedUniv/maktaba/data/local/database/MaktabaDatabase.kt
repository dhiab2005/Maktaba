// data/local/database/MaktabaDatabase.kt
package com.ElOuedUniv.maktaba.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.ElOuedUniv.maktaba.data.local.dao.BookDao
import com.ElOuedUniv.maktaba.data.local.entity.BookEntity
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus

// ── Converters لتحويل Enum إلى String ──────────
class Converters {
    @TypeConverter
    fun fromReadingStatus(status: ReadingStatus): String = status.name

    @TypeConverter
    fun toReadingStatus(value: String): ReadingStatus =
        ReadingStatus.valueOf(value)
}

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MaktabaDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: MaktabaDatabase? = null

        fun getDatabase(context: Context): MaktabaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaktabaDatabase::class.java,
                    "maktaba_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}