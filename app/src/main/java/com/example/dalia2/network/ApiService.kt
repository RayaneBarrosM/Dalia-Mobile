package com.example.dalia2.network

import com.example.dalia2.data.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//Aqui fica os edpoitns criados na api
interface ApiService {
    @GET("/api/user/")
    suspend fun onRender(
    ): Response<Unit>
    //USER
    @POST("/api/user/criarUsuario")
    suspend fun createUser(
        @Body request: UserRegistre
    ): Response<UserResponse>

    //Verificação de email
    @POST("/api/user/verify")
    suspend fun verifyCode(
        @Body request: VerificationRequest
    ): Response<TokensResponse>

    @POST("/api/user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokensResponse>

    @POST("/api/user/refresh")
    fun refreshToken(
        @Header("Authorization") token: String,
    ): Call<TokensResponse>

    @Headers("Content-Type: application/json",
        "Accept: */*",
        "User-Agent: PostmanRuntime/7.41.1")
    @POST("/api/user/search")
    suspend fun search(
        @Body request: SearchRequest
    ): Response<SearchResponse>

    //CALENDARIO
    @Headers("Content-Type: application/json",
        "Accept: */*",
        "User-Agent: PostmanRuntime/7.41.1")
    @PUT("/api/ciclo/registrar-menstruacao")
    suspend fun registrarMenstruacao(
    ): Response<CycleData>

    @Headers("Content-Type: application/json",
        "Accept: */*",
        "User-Agent: PostmanRuntime/7.41.1")
    @GET("/api/ciclo/status")
    suspend fun getCycle(
    ): Response<CycleData>


    //FORUM
    @GET("/api/posts/getTodos")
    suspend fun getPosts (
    ): List<Posts>

    @POST("/api/posts/create")
    suspend fun createPost (
        @Body request: Posts
    ): Response<Unit>

    @PUT("/api/posts/{idPost}/like")
    suspend fun likePost (
        @Path("idPost") idPost: String,
    ): Response<Unit>

    @PUT("/api/posts/{idPost}/unlike")
    suspend fun unLikePost (
        @Path("idPost") idPost: String
    ): Response<Unit>

    @POST("/api/comments/{postId}/addComment")
    suspend fun createComment (
        @Path ("postId") idPost: String,
        @Body request: Comments
    ): Response<Unit>

    //PERFIL
    @GET("api/perfil/perfilView")
    suspend fun getPerfil(
    ): Response<ProfileResponse>

    @PUT("api/perfil/updatePerfil")
    suspend fun updatePerfil(
        @Body userRegistre: ProfileRequest
    ): Response<ProfileResponse>

    //PEDIDO DE AJUDA
    @POST("api/reports/enviar-denuncia")
    suspend fun needHelp(
        @Body message: RequestBody
    ): Response<DenunciaResponse>
}