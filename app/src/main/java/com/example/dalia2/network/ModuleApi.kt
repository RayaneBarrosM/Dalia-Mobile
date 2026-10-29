package com.example.dalia2.network

import android.content.Context
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.example.dalia2.data.SessionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleApi {

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager,
    sessionEvenBus: SessionEvenBus): AuthInterceptor {
        return AuthInterceptor(sessionManager, sessionEvenBus)
    }

    @Provides
    @Singleton
    fun  provideTokenAuthenticator (
        sessionManager: SessionManager , apiService: Provider<ApiService>
    ) : TokenAuthenticator {
        return TokenAuthenticator(sessionManager, apiService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenAuthenticator: TokenAuthenticator, authInterceptor: AuthInterceptor) : OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor (logging)
            .addInterceptor (authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            // Isso aqui ensina o GSON a ler o formato yyyy-MM-dd do seu Java
            .registerTypeAdapter(LocalDate::class.java, JsonDeserializer { json, _, _ ->
                LocalDate.parse(json.asJsonPrimitive.asString)
            })
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://daliaapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService{
        return retrofit.create(ApiService::class.java)
    }
}