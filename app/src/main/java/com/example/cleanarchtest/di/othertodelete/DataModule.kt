package com.example.cleanarchtest.di.othertodelete

import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import com.example.cleanarchtest.BuildConfig
import com.example.cleanarchtest.data.network.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRickAndMortyApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(RICK_AND_MORTY_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideLogger() = HttpLoggingInterceptor()
        .setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    companion object {
        private const val RICK_AND_MORTY_URL = "https://rickandmortyapi.com/api/"
    }
}
