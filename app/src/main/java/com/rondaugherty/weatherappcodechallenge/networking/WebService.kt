package com.rondaugherty.weatherappcodechallenge.networking

import com.rondaugherty.weatherappcodechallenge.Utils.Utils
import com.rondaugherty.weatherappcodechallenge.model.LogLevel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object WebService {

    private var retrofit: Retrofit? = null

    private fun getClient(logLevel: LogLevel): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        when (logLevel) {
            LogLevel.LOG_NOT_NEEDED ->
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            LogLevel.LOG_REQ_RES ->
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
            LogLevel.LOG_REQ_RES_BODY_HEADERS ->
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            LogLevel.LOG_REQ_RES_HEADERS_ONLY ->
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder().run {
            connectTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES).addInterceptor(interceptor).build()
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder().apply {
                baseUrl(Utils.BASE_URL_OPEN)
                addConverterFactory(GsonConverterFactory.create())
                addConverterFactory(ScalarsConverterFactory.create())
                client(client)
            }.build()
        }

        return retrofit!!

    }

    fun getAPIService(logLevel: LogLevel = LogLevel.LOG_REQ_RES_BODY_HEADERS) =
        getClient(logLevel).create(APIService::class.java)!!

}