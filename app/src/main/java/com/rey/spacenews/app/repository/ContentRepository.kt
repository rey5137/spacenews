package com.rey.spacenews.app.repository

import com.rey.spacenews.app.repository.entity.Content
import com.rey.spacenews.app.repository.entity.ContentType

interface ContentRepository {

    suspend fun getContents(type: ContentType, page: Int, size: Int): List<Content>

}