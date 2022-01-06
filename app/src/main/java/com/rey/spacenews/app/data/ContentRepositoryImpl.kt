package com.rey.spacenews.app.data

import com.rey.spacenews.app.data.model.ContentModel
import com.rey.spacenews.app.repository.ContentRepository
import com.rey.spacenews.app.repository.entity.Content
import com.rey.spacenews.app.repository.entity.ContentType
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ContentRepositoryImpl(
    private val api: SpaceFlightNewsApi
): ContentRepository {

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    override suspend fun getContents(type: ContentType, page: Int, size: Int): List<Content> {
        return when(type) {
            ContentType.ARTICLE -> api.getArticles(size, "id:DESC", page * size)
            ContentType.BLOG -> api.getBlogs(size, "id:DESC", page * size)
            ContentType.REPORT -> api.getReports(size, "id:DESC", page * size)
        }.map { it.toContent(type) }
    }

    private fun ContentModel.toContent(type: ContentType) = Content(
        id = id,
        type = type,
        title = title,
        url = url,
        image = imageUrl,
        site = newsSite,
        summary = summary,
        publishedDateTime = LocalDateTime.parse(publishedAt, dateTimeFormat).atZone(ZoneId.of("UTC")),
        updatedDateTime = LocalDateTime.parse(updatedAt, dateTimeFormat).atZone(ZoneId.of("UTC")),
    )

}