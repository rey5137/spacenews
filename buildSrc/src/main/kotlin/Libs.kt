object Libs {

    object Common {
        const val OKHTTP = "com.squareup.okhttp3:okhttp:4.9.1"
        const val OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        const val RXJAVA = "io.reactivex.rxjava3:rxjava:3.0.12"
        const val RXANDROID = "io.reactivex.rxjava3:rxandroid:3.0.0"
        const val TIMBER = "com.jakewharton.timber:timber:4.7.1"
        const val JWT_DECODE = "com.auth0.android:jwtdecode:2.0.0"
        const val JODA_TIME = "net.danlew:android.joda:2.10.9.1"
        const val TSNACKBAR = "com.github.Redman1037:TSnackBar:V2.0.0"
        const val PAPER_DB = "io.github.pilgr:paperdb:2.7.1"
        const val CIRCLE_INDICATOR = "me.relex:circleindicator:2.1.6"
        const val VIEW_ANIMATOR = "com.github.florent37:ViewAnimator:v1.1.2"
        const val PDF_VIEWER = "com.github.barteksc:AndroidPdfViewer:2.8.0"
        const val QR_SCANNER = "io.github.g00fy2.quickie:quickie-bundled:1.2.2"
        const val LANDSCAPIST = "com.github.skydoves:landscapist-glide:1.4.4"
    }

    object Coil {
        private const val domain = "io.coil-kt"
        private const val version = "1.4.0"
        const val CORE = "$domain:coil:$version"
        const val COMPOSE = "$domain:coil-compose:$version"
    }

    object Koin {
        private const val domain = "io.insert-koin"
        private const val version = "3.0.1"
        const val CORE = "$domain:koin-core:$version"
        const val ANDROID = "$domain:koin-android:$version"
    }

    object AndroidX {
        private const val domain = "androidx"
        const val APP_COMPAT = "$domain.appcompat:appcompat:1.3.0-rc01"
        const val CARD_VIEW = "$domain.cardview:cardview:1.0.0"
        const val COORDINATOR_LAYOUT = "$domain.coordinatorlayout:coordinatorlayout:1.1.0"
        const val DRAWER_LAYOUT = "$domain.drawerlayout:drawerlayout:1.1.1"
        const val RECYCLER_VIEW = "$domain.recyclerview:recyclerview:1.2.0"
        const val SWIPE_REFRESH_LAYOUT = "$domain.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val VIEW_PAGER2 = "$domain.viewpager2:viewpager2:1.0.0"
        const val ANNOTATIONS = "$domain.annotation:annotation:1.2.0"
        const val MATERIAL = "com.google.android.material:material:1.4.0"
        const val CORE = "$domain.core:core-ktx:1.7.0"
        const val NAVIGATION = "$domain.navigation:navigation-compose:2.4.0-beta02"
        const val START_UP = "$domain.startup:startup-runtime:1.1.0"

        object ConstraintLayout {
            private const val subDomain = "constraintlayout"
            private const val version = "2.0.4"
            const val CORE = "$domain.$subDomain:constraintlayout:$version"
            const val SOLVER = "$domain.$subDomain:constraintlayout-solver:$version"
        }

    }

    object Coroutines {
        private const val domain = "org.jetbrains.kotlinx"
        private const val version = "1.5.0"

        const val CORE = "$domain:kotlinx-coroutines-core:$version"
        const val ANDROID = "$domain:kotlinx-coroutines-android:$version"
        const val TEST = "$domain:kotlinx-coroutines-test:$version"
    }

    object RxBinding {
        private const val domain = "com.jakewharton.rxbinding4"
        private const val version = "4.0.0"
        const val PLATFORM = "$domain:rxbinding:$version"
        const val CORE = "$domain:rxbinding-core:$version"
        const val APP_COMPAT = "$domain:rxbinding-appcompat:$version"
        const val DRAWER_LAYOUT = "$domain:rxbinding-drawerlayout:$version"
        const val RECYCLER_VIEW = "$domain:rxbinding-recyclerview:$version"
        const val SLIDING_PANE_LAYOUT = "$domain:rxbinding-slidingpanelayout:$version"
        const val SWIPE_REFRESH_LAYOUT = "$domain:rxbinding-swiperefreshlayout:$version"
        const val VIEW_PAGER = "$domain:rxbinding-viewpager:$version"
        const val VIEW_PAGER_2 = "$domain:rxbinding-viewpager2:$version"
    }

    object Retrofit {
        private const val domain = "com.squareup.retrofit2"
        private const val version = "2.9.0"
        const val CORE = "$domain:retrofit:$version"
        const val MOSHI = "$domain:converter-moshi:$version"
        const val RXJAVA = "com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0"
    }

    object Glide {
        private const val domain = "com.github.bumptech.glide"
        private const val version = "4.12.0"
        const val CORE = "$domain:glide:$version"
        const val COMPILER = "$domain:compiler:$version"
        const val OKHTTP3 = "$domain:okhttp3-integration:$version"
    }

    object ExoPlayer {
        private const val domain = "com.google.android.exoplayer"
        private const val version = "2.14.1"
        const val CORE = "$domain:exoplayer-core:$version"
        const val DASH = "$domain:exoplayer-dash:$version"
        const val HLS = "$domain:exoplayer-hls:$version"
        const val RTSP = "$domain:exoplayer-rtsp:$version"
        const val SMOOTH_STREAMING = "$domain:exoplayer-smoothstreaming:$version"
        const val TRANSFORMER = "$domain:exoplayer-transformer:$version"
        const val UI = "$domain:exoplayer-ui:$version"
    }

    object ComposeUI {
        private const val domain = "androidx.compose"
        const val VERSION = "1.0.5"
        const val UI = "$domain.ui:ui:$VERSION"
        const val TOOLING = "$domain.ui:ui-tooling:$VERSION"
        const val FOUNDATION = "$domain.foundation:foundation:$VERSION"
        const val MATERIAL = "$domain.material:material:$VERSION"
        const val MATERIAL_ICONS = "$domain.material:material-icons-core:$VERSION"
        const val MATERIAL_ICONS_EXTENDED = "$domain.material:material-icons-extended:$VERSION"
        const val ACTIVITY = "androidx.activity:activity-compose:1.3.1"
        const val TEST = "$domain.ui:ui-test-junit4:$VERSION"
    }

    object Accompanist {
        private const val domain = "com.google.accompanist"
        const val VERSION = "0.20.3"
        const val SWIPE_REFRESH = "$domain:accompanist-swiperefresh:$VERSION"
    }

    object Conductor {
        private const val domain = "com.bluelinelabs"
        private const val VERSION = "3.1.2"
        const val CORE = "$domain:conductor:$VERSION"
        const val ANDROIDX_TRANSITION = "$domain:conductor-androidx-transition:$VERSION"
        const val VIEW_PAGER2 = "$domain:conductor-viewpager2:$VERSION"
        const val LIFE_CYCLE = "$domain:conductor-archlifecycle:$VERSION"
    }
}