package com.copart.rtlaisdk.di

import com.copart.rtlaisdk.MainActivity
import com.copart.rtlaisdk.data.Endpoints
import com.copart.rtlaisdk.data.RTLApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

val appModule = module {

    single {
        OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            MainActivity.rtlClientParams?.let {
                requestBuilder.header("Cookie", it.sessionCookie)
            }
            requestBuilder.header("sitecode", "CPRTUS")
            requestBuilder.header("appcode", "RTLAISDK")
            requestBuilder.header("correlationId", UUID.randomUUID().toString())
            requestBuilder.method(original.method, original.body)
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
