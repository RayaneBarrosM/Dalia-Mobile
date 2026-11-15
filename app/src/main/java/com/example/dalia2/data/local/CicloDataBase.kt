package com.example.dalia2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CicloEntity::class], version = 1, exportSchema = false)

abstract class CicloDataBase : RoomDatabase() {
    abstract fun cicloDao() : CicloDao
    companion object {
        @Volatile
        private var INSTANCE: CicloDataBase? = null
        fun getDatabase(context: Context): CicloDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CicloDataBase:: class.java,
                    "ciclo_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}