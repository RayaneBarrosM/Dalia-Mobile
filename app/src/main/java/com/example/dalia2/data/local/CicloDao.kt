package com.example.dalia2.data.local


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CicloDao {
    @Insert
    suspend fun insert( ciclo: CicloEntity)
    @Update
    suspend fun update( ciclo: CicloEntity)
    @Delete
    suspend fun delete( ciclo: CicloEntity)
    // Obtém o histórico completo de ciclos
    @Query("SELECT * FROM ciclo ORDER BY id DESC")
    suspend fun getAllCiclos(): List<CicloEntity>

    // Obtém apenas o ciclo mais recente gravado
    @Query("SELECT * FROM ciclo ORDER BY id DESC LIMIT 1")
    suspend fun getUltimoCiclo(): CicloEntity?
}