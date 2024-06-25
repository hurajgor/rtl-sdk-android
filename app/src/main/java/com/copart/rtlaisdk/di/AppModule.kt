package com.copart.rtlaisdk.di

import com.copart.rtlaisdk.data.Endpoints
import com.copart.rtlaisdk.data.RTLApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.copart.rtlaisdk.BuildConfig

val appModule = module {

    single {
        val sessionId = BuildConfig.SESSION_ID

        OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Cookie", sessionId)
                .header("sitecode", "CPRTUS")
                .header("appcode", "RTLAISDK")
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(RTLApi::class.java)
    }

}

val appModules = listOf(
    appModule,
    repositoryModule,
    viewModelModule
)
