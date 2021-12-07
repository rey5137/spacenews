package com.rey.spacenews.app.data

import com.rey.spacenews.app.data.model.ArticleModel
import retrofit2.http.GET
import retrofit2.http.Query


interface SpaceFlightNewsApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("_limit") limit: Int,
        @Query("_sort") sort: String,
        @Query("_start") start: Int,
    ): List<ArticleModel>

}