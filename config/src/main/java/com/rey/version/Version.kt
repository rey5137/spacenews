package com.rey.version

import org.gradle.api.Plugin
import org.gradle.api.Project

const val VERSION_MAJOR = 0
const val VERSION_MINOR = 0
const val VERSION_PATCH = 1
const val VERSION_CLASSIFIER = "1"

class Version : Plugin<Project> {

    override fun apply(project: Project) {}

    companion object {

        const val MINIMUM_SDK = 21
        const val TARGET_SDK = 31

        const val CODE = MINIMUM_SDK * 10000000 + VERSION_MAJOR * 10000 + VERSION_MINOR * 100 + VERSION_PATCH

        const val NAME_WITHOUT_CLASSIFIER = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

        val NAME = if (VERSION_CLASSIFIER.isNullOrBlank()) NAME_WITHOUT_CLASSIFIER else "$NAME_WITHOUT_CLASSIFIER ($VERSION_CLASSIFIER)"

    }

}