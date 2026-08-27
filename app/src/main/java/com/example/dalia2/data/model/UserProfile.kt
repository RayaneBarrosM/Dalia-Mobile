package com.example.dalia2.data.model

import java.time.LocalDate

data class ProfileResponse(
    val user: UserRequest,
    val search: SearchData?,
    val pregnancy: PregnancyData?
){
    val currentMode: AppMode
        get()=if(pregnancy != null && (pregnancy.isPregnant == true || pregnancy.gestationWeeks > 0)) {
            AppMode.GRAVIDEZ
        } else {
            AppMode.MENSTRUACAO
        }
}
data class UserRequest(
    val name: String?,
    val surname: String?,
    val email: String?,
    val password: String? = null
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
    val search: SearchData?,
    val pregnancy: PregnancyData?
)

data class DenunciaResponse(
    val status: String,
    val message: String
)

enum class AppMode{
    MENSTRUACAO,
    GRAVIDEZ
}