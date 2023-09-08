package com.example.noteapp.di

import android.content.Context
import com.example.noteapp.MyApplication
import com.example.noteapp.api.AuthInterceptor
import com.example.noteapp.api.NeoUserAPI
import com.example.noteapp.api.NeoUserDataAPI

import com.example.noteapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {


    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkClientBuilder(interceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(interceptor)
    }


    @Singleton
    @Provides
    fun providesNeoUserAPI(
        retrofitBuilder: Retrofit.Builder, okHttpClientBuilder: OkHttpClient.Builder): NeoUserAPI {
        return retrofitBuilder.client(okHttpClientBuilder.build()).build().create(NeoUserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNeoUserDataAPI(
        retrofitBuilder: Retrofit.Builder, okHttpClientBuilder: OkHttpClient.Builder,
        authInterceptor: AuthInterceptor): NeoUserDataAPI {
        return retrofitBuilder.client(okHttpClientBuilder.addInterceptor(authInterceptor).build())
            .build().create(NeoUserDataAPI::class.java)
    }


//    @Singleton
//    @Provides
//    fun provideApplicationContext(application: MyApplication):Context {
//        return application.applicationContext
//
//    }

}