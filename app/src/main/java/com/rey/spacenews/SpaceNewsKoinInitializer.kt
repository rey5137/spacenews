package com.rey.spacenews

import com.rey.spacenews.app.data.NewsRepositoryImpl
import com.rey.spacenews.app.data.SpaceFlightNewsApi
import com.rey.spacenews.app.repository.NewsRepository
import com.rey.spacenews.common.initializer.KoinInitializer
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SpaceNewsKoinInitializer : KoinInitializer(app)

private val app = module {

    single {
        Moshi.Builder().build()
    }

    single {
        OkHttpClient.Builder()
            .build()
    }

    single {
        val moshi = get<Moshi>()

        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    single {
        get<Retrofit>().create(SpaceFlightNewsApi::class.java)
    }

    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }

}