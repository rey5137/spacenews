object Version {

    const val MAJOR = 0
    const val MINOR = 0
    const val PATCH = 1
    const val CLASSIFIER = "1"
    const val MINIMUM_SDK = 21
    const val TARGET_SDK = 31

    const val CODE = MINIMUM_SDK * 10000000 + MAJOR * 10000 + MINOR * 100 + PATCH

    const val NAME_WITHOUT_CLASSIFIER = "$MAJOR.$MINOR.$PATCH"

    val NAME = if (CLASSIFIER.isNullOrBlank()) NAME_WITHOUT_CLASSIFIER else "$NAME_WITHOUT_CLASSIFIER ($CLASSIFIER)"

}