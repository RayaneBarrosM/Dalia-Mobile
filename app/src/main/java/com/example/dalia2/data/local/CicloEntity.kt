package com.example.dalia2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "ciclo" )
data class CicloEntity (
    @PrimaryKey(autoGenerate = true)  val id: Int = 0 , // ID auto-incrementado
    val minCycleDuration: Int,
    val maxCycleDuration: Int,
    val lastMenstruationDay: String,
    val diasDeAtraso: Long,
    val fimMenstruacao: String,
    val inicioPeriodoFertil: String,
    val fimPeriodoFertil: String,
    val diaOvulacao: String                         // Status de conclusão da tarefa
)
