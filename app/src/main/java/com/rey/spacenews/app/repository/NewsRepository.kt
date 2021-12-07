package com.rey.spacenews.app.repository

import com.rey.spacenews.app.repository.entity.Article

interface NewsRepository {

    suspend fun getArticles(page: Int, size: Int): List<Article>

}