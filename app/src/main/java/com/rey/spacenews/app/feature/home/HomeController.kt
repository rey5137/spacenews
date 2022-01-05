package com.rey.spacenews.app.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.bluelinelabs.conductor.Controller
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rey.spacenews.app.feature.home.contract.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
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


@ExperimentalMaterialApi
class HomeController : Controller(), KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val store = HomeStore(repository = get(), scope = scope, context = Dispatchers.Default)

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val items = store.states.map { it.items }
        val isRefreshing = store.states.map { it.isRefreshing }.distinctUntilChanged()
        val view = ComposeView(container.context).apply {
            setContent {
                homeScreen(items, isRefreshing)
            }
        }
        store.dispatch(LoadMoreCommand)
        return view
    }

    override fun onDestroy() {
        scope.cancel()
    }

    @Composable
    fun homeScreen(items: Flow<List<Item>>, isRefreshing: Flow<Boolean>) {
        MaterialTheme {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    articleList(items, isRefreshing)
                }
            }
        }
    }

    @Composable
    fun articleList(items: Flow<List<Item>>, isRefreshing: Flow<Boolean>) {
        val listState = rememberLazyListState()
        val itemState = items.collectAsState(initial = emptyList())
        val refreshingState by isRefreshing.collectAsState(initial = false)

        SwipeRefresh(
            state = rememberSwipeRefreshState(refreshingState),
            onRefresh = { store.dispatch(RefreshCommand) },
        ) {
            LazyColumn(modifier = Modifier.fillMaxHeight(), state = listState) {
                items(itemState.value, key = { it.id }) { item ->
                    Modifier.fillParentMaxHeight()
                    when (item) {
                        is LoadingItem -> loadingItem(
                            itemModifier = if (item.firstTime) Modifier.fillParentMaxHeight()
                            else Modifier.height(96.dp),
                            indicatorModifier = if (item.firstTime) Modifier.size(64.dp)
                            else Modifier.size(48.dp)
                        )
                        is ArticleItem -> articleItem(
                            id = item.article.id,
                            title = item.article.title,
                            image = item.article.image,
                            site = item.article.site,
                            publishedDateTime = item.article.publishedDateTime,
                        )
                    }
                }
            }
        }
        val loadMoreReached by remember {
            derivedStateOf {
                val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastIndex >= listState.layoutInfo.totalItemsCount - 3 && lastIndex > 1
            }
        }

        LaunchedEffect(loadMoreReached) {
            if (loadMoreReached)
                store.dispatch(LoadMoreCommand)
        }
    }

    @Composable
    fun loadingItem(itemModifier: Modifier, indicatorModifier: Modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = itemModifier.fillMaxWidth()
        ) {
            CircularProgressIndicator(modifier = indicatorModifier)
        }
    }

    @Composable
    fun articleItem(
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
                        style = MaterialTheme.typography.caption
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