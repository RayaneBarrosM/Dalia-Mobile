package com.example.dalia2.data.model

import java.time.LocalDate

data class UserRegistre(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val passConfirmation: String
)

data class UserResponse(
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val enable: Boolean,
    val verificationToken: String,
    val TokenExperation: Long
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class VerificationRequest(
    val email: String,
    val token: String
)

data class TokensResponse(
    val token: String,
    val refreshToken:String
)

data class RefreshTokenRequest(
    val refreshToken:String
)

data class SearchRequest(
    val age: Int,
    val regularMenstruation: Boolean,
    val useContraceptive: Boolean,
    val contraceptiveType: String,
    val lastMenstruationDay: String,
    val cycleDuration: Int,
    val menstruationDuration: Int,
)

data class SearchResponse(
    val age: Int,
    val regularMenstruation: Boolean,
    val useContraceptive: Boolean,
    val contraceptiveType: String,
    val lastMenstruationDay: String,
    val cycleDuration: Int,
    val menstruationDuration: Int,
    val cycleHistory: List<Int>,
    val minCycleDuration: Int,
    val maxCycleDuration: Int
)

data class CycleData(
    val minCycleDuration: Int,
    val maxCycleDuration: Int,
    val lastMenstruationDay: LocalDate,
    val isMenstruando: Boolean,
    val isPeriodoFertil: Boolean,
    val isOvulacao: Boolean,
    val diasDeAtraso: Long,
    val fimMenstruacao: LocalDate,
    val inicioPeriodoFertil: LocalDate,
    val fimPeriodoFertil: LocalDate,
    val diaOvulacao: LocalDate
)

data class PregnancyRequest(
    val isPregnant: Boolean,
    val startDate: String,
    val gestationWeeks: Int,
    val expectedBirthDate: String,
    val plannedPregnancy: Boolean,
    val takeMedicine: Boolean,
    val habits: List<String>,
    val symptoms: List<String>
)

data class Weeks(
    val semana: Int,
    val tamanho: String,
    val peso: String,
    val comidaAssociada: String,
    val frase: String
)

