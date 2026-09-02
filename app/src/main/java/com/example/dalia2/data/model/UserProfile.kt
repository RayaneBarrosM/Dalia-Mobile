package com.example.dalia2.data.model

import java.time.LocalDate

data class ProfileResponse(
    val user: UserRequest,
    val search: SearchData?,
    val pregnancyMonitoring: PregnancyData?
){
    val currentMode: AppMode
        get()= when{
            user.modo.equals("GRAVIDEZ", ignoreCase = true) -> AppMode.GRAVIDEZ
            user.modo.equals("MENSTRUACAO", ignoreCase = true) -> AppMode.MENSTRUACAO

            pregnancyMonitoring != null && pregnancyMonitoring.isPregnant -> AppMode.GRAVIDEZ
            else -> AppMode.MENSTRUACAO
        }
}

data class RetornoMenstruacaoRequest(
    val modo: String = "MENSTRUACAO",
    val search: SearchRequest,
    val pregnancyMonitoring: PregnancyData = PregnancyData(
        isPregnant = false,
        startDate = "",
        gestationWeeks = 0,
        expectedBirthDate = ""
    )
)

data class UserRequest(
    val name: String?,
    val surname: String?,
    val email: String?,
    val password: String? = null,
    val modo: String
)

data class SearchData(
    var age: Int,
    var useContraceptive: Boolean,
    var contraceptiveType: String?
)

data class PregnancyData(
    val isPregnant: Boolean,
    val startDate: String,
    val gestationWeeks: Int,
    val expectedBirthDate: String,
)

data class ProfileRequest(
    val name: String?,
    val surname: String?,
    val email: String?,
    val password: String? = null,
    val modo: String? = null,
    val search: SearchData?,
    val pregnancyMonitoring: PregnancyData?
)

data class DenunciaResponse(
    val status: String,
    val message: String
)

enum class AppMode{
    MENSTRUACAO,
    GRAVIDEZ
}