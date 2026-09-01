package com.example.dalia2.data.repository

import android.util.Log
import com.example.dalia2.data.SessionManager
import com.example.dalia2.data.local.CicloDao
import com.example.dalia2.data.local.CicloEntity
import com.example.dalia2.data.model.Articles
import com.example.dalia2.data.model.Comments
import com.example.dalia2.data.model.CycleData
import com.example.dalia2.data.model.DenunciaResponse
import com.example.dalia2.data.model.EventCalendar
import com.example.dalia2.data.model.LoginRequest
import com.example.dalia2.data.model.Posts
import com.example.dalia2.data.model.PregnancyRequest
import com.example.dalia2.data.model.ProfileRequest
import com.example.dalia2.data.model.ProfileResponse
import com.example.dalia2.data.model.SearchRequest
import com.example.dalia2.data.model.SearchResponse
import com.example.dalia2.data.model.TokensResponse
import com.example.dalia2.data.model.UserRegistre
import com.example.dalia2.data.model.UserResponse
import com.example.dalia2.data.model.VerificationRequest
import com.example.dalia2.data.model.Weeks
import com.example.dalia2.data.session.UserSession
import com.example.dalia2.network.ApiService
import okhttp3.RequestBody
import java.time.LocalDate
import javax.inject.Inject

class DaliaRepository @Inject constructor(
    private val api: ApiService,
    private val sessionM: SessionManager,
    private val cicloDao: CicloDao
) {
    suspend fun createUser(userRegistre: UserRegistre): Result<UserResponse> {
        return try {
            val response = api.createUser(userRegistre)
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    Result.success(userResponse) // Agora os tipos batem!
                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""),"").replace("}","")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")

                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun verifiyCode( verificationRequest: VerificationRequest): Result<TokensResponse>{
        return try {
            val response = api.verifyCode(verificationRequest)
            if (response.isSuccessful) {
                // Se o backend retorna um JSON, o .string() lê o texto puro dele
                val tokens = response.body()
                if(tokens != null){
                    sessionM.saveAccessToken(tokens.token)
                    sessionM.saveRefreshToken(tokens.refreshToken)
                    Result.success(tokens)
                }else{
                    Result.failure(Exception("Resposta vazia"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""),"").replace("}","")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")

                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun login(LoginRequest: LoginRequest): Result<TokensResponse>{
        return try {
            val response = api.login(LoginRequest)
            if(response.isSuccessful){
                val tokens = response.body()
                if(tokens != null){
                    sessionM.saveAccessToken(tokens.token)
                    sessionM.saveRefreshToken(tokens.refreshToken)
                    Result.success(tokens)
                } else {
                    Result.failure(Exception("Resposta vazia"))
                }
            }else{
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""),"").replace("}","")
                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")

                Result.failure(Exception(cleanMessage))
            }
        }catch(e: Exception){
            Log.d("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }
    suspend fun search(SearchRequest: SearchRequest): Result<SearchResponse> {
        return try {
            val response = api.search(SearchRequest)
            if (response.isSuccessful) {
                val searchResponse = response.body()

                if (searchResponse != null) {
                    Result.success(searchResponse) // Agora os tipos batem!
                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""), "").replace("}", "")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")
                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun registrarMenstruacao(): Result<CycleData> {
        return try {
            val response = api.registrarMenstruacao()
            if (response.isSuccessful) {
                val menstruacaoResponse = response.body()

                if (menstruacaoResponse != null) {
                    //Salva Local
                    val entity = CicloEntity(
                        minCycleDuration = menstruacaoResponse.minCycleDuration,
                        maxCycleDuration = menstruacaoResponse.maxCycleDuration,
                        lastMenstruationDay = menstruacaoResponse.lastMenstruationDay.toString(),
                        diasDeAtraso = menstruacaoResponse.diasDeAtraso,
                        fimMenstruacao = menstruacaoResponse.fimMenstruacao.toString(),
                        inicioPeriodoFertil = menstruacaoResponse.inicioPeriodoFertil.toString(),
                        fimPeriodoFertil = menstruacaoResponse.fimPeriodoFertil.toString(),
                        diaOvulacao = menstruacaoResponse.diaOvulacao.toString()
                    )
                    cicloDao.update(entity)
                    Result.success(menstruacaoResponse) // Agora os tipos batem!
                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""), "").replace("}", "")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")
                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun getCiclo(): Result<CycleData> {
        return try {
            val response = api.getCycle()
            if (response.isSuccessful) {
                val cicloResponse = response.body()

                if (cicloResponse != null) {

                    Result.success(cicloResponse)                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""), "").replace("}", "")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")
                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun createEventCalendar(EventCalendar: EventCalendar): Result<EventCalendar> {
        return try {
            val response = api.createEvent(EventCalendar)
            if (response.isSuccessful) {
                val eventResponse = response.body()

                if (eventResponse != null) {
                    Result.success(eventResponse)
                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""), "").replace("}", "")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")
                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }
    suspend fun pregnancyQuiz(PregnancyRequest: PregnancyRequest): Result<PregnancyRequest> {
        return try {
            val response = api.pregnancyQuiz(PregnancyRequest)
            if (response.isSuccessful) {
                val pregnancyResponse = response.body()

                if (pregnancyResponse != null) {
                    Result.success(pregnancyResponse)
                } else {
                    Result.failure(Exception("Corpo da resposta vazio"))
                }
            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                val cleanMessage = errorBody.replace(Regex("""\d{3}:\s*"""), "").replace("}", "")

                Log.e("REPO_ERROR", "Código: $errorCode | Mensagem: $errorBody")
                Result.failure(Exception(cleanMessage))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun getWeek(week: Int): Result<Weeks> {
        return try{
            val response = api.getWeeks(week)
            if(response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error pegar dados da semana"))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha ao pegar dados da semana", e)
            Result.failure(e)
        }
    }

    suspend fun getDataInicio(): String? {
        return try {
            val userProfile = api.getPerfil() // Chama endpoint do perfil
            val dataStr = userProfile.body()?.pregnancy?.startDate // Pega o campo da data
                if (dataStr != null) {
                    dataStr.toString()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }

    suspend fun getArticles(): Result<List<Articles>> {
        return try {
            val response = api.getArticles()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha ao tentar resgatar articles", e)
            Result.failure(e)
        }
    }


    suspend fun getPosts(): Result<List<Posts>> {
        return try {
            val response = api.getPosts()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun createPost(post: Posts): Result<Unit> {
        return try {
            val response = api.createPost(post)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro em criar post"))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }
    suspend fun createComment(idPost: String, comment: Comments): Result<Unit> {
        return try {
            val response = api.createComment(idPost, comment)
            Log.d("API_RESPONSE", "Olha o id: $idPost")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro em criar comentario"))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun likePost(idPost: String): Result<Unit>{
        return try{
            val response =api.likePost(idPost)
            Log.d("API_RESPONSE", "Like no: $idPost")
            if(response.isSuccessful){
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error em dar like"))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun unlikePost(idPost: String): Result<Unit>{
        return try{
            val response =api.unLikePost(idPost)
            Log.d("API_RESPONSE", "unlike no: $idPost")
            if(response.isSuccessful){
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error em dar like"))
            }
        } catch (e: Exception) {
            Log.e("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }

    suspend fun getUserFullProfile(): Result<ProfileResponse> {
        UserSession.profileCache?.let {
            return Result.success(it)
        }
        return try {
            val response = api.getPerfil()
            if (response.isSuccessful) {
                val body:  ProfileResponse? = response.body()
                if (body != null) {
                    val data = response.body()!!
                    UserSession.profileCache = data
                    Result.success(data)
                } else {
                    Result.failure(Exception("Corpo vazio"))
                }
            } else {
                Result.failure(Exception("Erro na API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updatePerfil(userDto: ProfileRequest): Result<ProfileResponse> {
        return try {
            val response = api.updatePerfil(userDto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao atualizar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun needHelp(message: RequestBody): Result<DenunciaResponse>{
        return try {
            val response = api.needHelp(message)
            if(response.isSuccessful){
                Log.d("REPO_SUCESS", "deu certo")
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success(responseBody)
                } else {
                    // Caso o corpo venha vazio por algum motivo, não deixamos quebrar
                    Result.success(DenunciaResponse(status = "success", message = "Ajuda solicitada com sucesso!"))
                }            } else {
               Result.failure(Exception("falha ao enviar mensagem"))
            }
        }catch(e: Exception){
            Log.d("REPO_EXCEPTION", "Falha catastrófica", e)
            Result.failure(e)
        }
    }
}
