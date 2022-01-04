package com.rey.spacenews.app.data

import com.rey.spacenews.app.data.model.ArticleModel
import com.rey.spacenews.app.repository.NewsRepository
import com.rey.spacenews.app.repository.entity.Article
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsRepositoryImpl(
    private val api: SpaceFlightNewsApi
): NewsRepository {

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    override suspend fun getArticles(page: Int, size: Int): List<Article> {
        return api.getArticles(size, "id:DESC", page * size)
            .map { it.toArticle() }
    }

    private fun ArticleModel.toArticle() = Article(
        id = id,
        title = title,
        url = url,
        image = imageUrl,
        site = newsSite,
        summary = summary,
        publishedDateTime = LocalDateTime.parse(publishedAt, dateTimeFormat).atZone(ZoneId.of("UTC")),
        updatedDateTime = LocalDateTime.parse(updatedAt, dateTimeFormat).atZone(ZoneId.of("UTC")),
    )

}