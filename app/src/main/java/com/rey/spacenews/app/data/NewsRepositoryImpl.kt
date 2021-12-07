package com.rey.spacenews.app.data

import com.rey.spacenews.app.data.model.ArticleModel
import com.rey.spacenews.app.repository.NewsRepository
import com.rey.spacenews.app.repository.entity.Article
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class NewsRepositoryImpl(
    private val api: SpaceFlightNewsApi
): NewsRepository {

    private val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

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
        publishedDateTime = LocalDateTime.parse(publishedAt, dateTimeFormat),
        updatedDateTime = LocalDateTime.parse(updatedAt, dateTimeFormat),
    )

}