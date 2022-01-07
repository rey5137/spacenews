package com.rey.spacenews.app.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.bluelinelabs.conductor.Controller
import com.google.accompanist.pager.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rey.spacenews.MainActivity
import com.rey.spacenews.R
import com.rey.spacenews.app.feature.home.contract.*
import com.rey.spacenews.app.repository.entity.ContentType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max
import kotlin.math.roundToInt


@ExperimentalPagerApi
@ExperimentalMaterialApi
class HomeController : Controller(), KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val stores = mapOf(
        ContentType.ARTICLE to HomeStore(
            repository = get(),
            contentType = ContentType.ARTICLE,
            scope = scope,
            context = Dispatchers.Default
        ),
        ContentType.BLOG to HomeStore(
            repository = get(),
            contentType = ContentType.BLOG,
            scope = scope,
            context = Dispatchers.Default
        ),
        ContentType.REPORT to HomeStore(
            repository = get(),
            contentType = ContentType.REPORT,
            scope = scope,
            context = Dispatchers.Default
        )
    )

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return ComposeView(container.context).apply {
            setContent {
                HomeScreen()
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }


    @Composable
    fun HomeScreen() {
        val pagerState = rememberPagerState()
        val contentTypes = ContentType.values()

        Column {
            TopBar(pagerState)
            HorizontalPager(
                count = contentTypes.size,
                state = pagerState,
            ) { page -> ContentList(store = stores[contentTypes[page]]!!) }
        }

    }

    @Composable
    fun TopBar(pagerState: PagerState) {
        val scope = rememberCoroutineScope()
        Surface(elevation = 4.dp) {
            Column {
                TopAppBar(
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { (activity as MainActivity).openDrawer() } }) {
                            Icon(Icons.Filled.Menu, null)
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name)
                        )
                    },
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(
                                pagerState,
                                tabPositions
                            )
                        )
                    },
                ) {
                    ContentType.values().forEachIndexed { index, contentType ->
                        Tab(
                            text = { Text(contentType.name) },
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.scrollToPage(index) } },
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ContentList(store: HomeStore) {
        val listState = rememberLazyListState()

        val itemState = store.states.map { it.items }.collectAsState(initial = emptyList())
        val refreshingState = store.states.map { it.isRefreshing }.distinctUntilChanged()
            .collectAsState(initial = false)


        SwipeRefresh(
            state = rememberSwipeRefreshState(refreshingState.value),
            onRefresh = { store.dispatch(RefreshCommand) },
        ) {
            LazyColumn(modifier = Modifier.fillMaxHeight(), state = listState) {
                items(items = itemState.value, key = { it.id }) { item ->
                    when (item) {
                        is LoadingItem -> LoadingItem(
                            itemModifier = if (item.firstTime) Modifier.fillParentMaxHeight()
                            else Modifier.height(96.dp),
                            indicatorModifier = if (item.firstTime) Modifier.size(64.dp)
                            else Modifier.size(48.dp)
                        )
                        is ContentItem -> ContentItem(
                            id = item.content.id,
                            title = item.content.title,
                            image = item.content.image,
                            site = item.content.site,
                            publishedDateTime = item.content.publishedDateTime,
                        )
                    }
                }
            }
        }
        val loadMoreReached by remember {
            derivedStateOf {
                val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastIndex >= listState.layoutInfo.totalItemsCount - 3
            }
        }

        LaunchedEffect(loadMoreReached) {
            if (loadMoreReached)
                store.dispatch(LoadMoreCommand)
        }
    }

    @Composable
    fun LoadingItem(itemModifier: Modifier, indicatorModifier: Modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = itemModifier.fillMaxWidth()
        ) {
            CircularProgressIndicator(modifier = indicatorModifier)
        }
    }

    @Composable
    fun ContentItem(
        id: Int,
        title: String,
        image: String,
        site: String,
        publishedDateTime: ZonedDateTime
    ) {
        Card(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            onClick = { Timber.d("asd click $id") }
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Image(
                    painter = rememberImagePainter(image) {
                        crossfade(true)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .minHeightAspectRatio(1f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                bottomStart = 8.dp,
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .padding(top = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = site,
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .weight(1f)
                            .padding(top = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        text = title,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .padding(top = 8.dp, bottom = 8.dp)
                            .align(Alignment.End),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = publishedDateTime.withZoneSameInstant(ZoneId.systemDefault())
                            .format(dateTimeFormatter),
                        style = MaterialTheme.typography.caption
                    )
                }

            }
        }
    }
}

fun Modifier.minHeightAspectRatio(aspectRatio: Float = 1f) = this.then(object : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int = if (width != Constraints.Infinity) {
        (width / aspectRatio).roundToInt()
    } else {
        max((width / aspectRatio).roundToInt(), measurable.minIntrinsicHeight(width))
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int = if (width != Constraints.Infinity) {
        (width / aspectRatio).roundToInt()
    } else {
        max((width / aspectRatio).roundToInt(), measurable.maxIntrinsicHeight(width))
    }

})