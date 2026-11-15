package com.example.dalia2.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Isso diz que o banco dura enquanto o app estiver aberto
object ModuleDatabase {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CicloDataBase {
        return Room.databaseBuilder(
            context,
            CicloDataBase::class.java,
            "dalia_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCicloDao(db: CicloDataBase): CicloDao {
        return db.cicloDao() // É aqui que o Hilt aprende a criar o Dao!
    }
}