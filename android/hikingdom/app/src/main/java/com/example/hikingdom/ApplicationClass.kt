package com.example.hikingdom

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.hikingdom.config.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    companion object{
        lateinit var baseApplication: ApplicationClass

        fun getInstance(): ApplicationClass {
            return baseApplication
        }

        const val X_ACCESS_TOKEN: String = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInR5cGUiOiJhY2Nlc3MiLCJpYXQiOjE2ODM3MDI4MDYsImV4cCI6MTY4MzcwNjQwNn0._Fpo6Bqh8wQQxoSG7WtQ7FDeedXlWAknwKWc5FQVrMo"
        const val TAG: String = "HIKINGDOM-ANDROID"                      // Log, SharedPreference
        const val APP_DATABASE = "$TAG-DB"

        lateinit var mSharedPreferences: SharedPreferences
        lateinit var retrofit: Retrofit

        const val BASE_URL: String = BuildConfig.PROD_URL       // 웹뷰나 api 통신 시 사용할 기본 주소. 이를 이용해 한번에 PROD_URL <-> DEV_URL 전환 가능. local.properties를 참조.
        const val HOME_WEB_URL: String = BASE_URL + "main"         // 메인페이지 WebView URL
        const val RANKING_WEB_URL: String = BASE_URL + "rank"      // 랭킹페이지 WebView URL
        const val GROUP_WEB_URL: String = BASE_URL + "club/main"        // 소모임페이지 WebView URL
        const val MYPAGE_WEB_URL: String = BASE_URL + "profile"       // 마이페이지 WebView URL

//        const val RETROFIT_BASE_URL: String = BASE_URL + "api/v1/"       // API 통신 시 사용할 기본 주소
        const val RETROFIT_BASE_URL: String = "http://70.12.246.168:8080/" + "api/v1/"       // API 통신 시 사용할 기본 주소

        const val HIKING_URL: String = BASE_URL + "hiking"       // 트래킹 API URL
    }

    override fun onCreate() {
        super.onCreate()

        baseApplication = this

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}