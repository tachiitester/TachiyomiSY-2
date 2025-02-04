package eu.kanade.presentation.reader

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.components.AdaptiveSheet
import eu.kanade.presentation.manga.components.MangaChapterListItem
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.ui.reader.chapter.ReaderChapterItem
import eu.kanade.tachiyomi.ui.reader.setting.ReaderSettingsScreenModel
import eu.kanade.tachiyomi.util.lang.toRelativeString
import exh.metadata.MetadataUtil
import exh.source.isEhBasedManga
import tachiyomi.domain.chapter.model.Chapter
import tachiyomi.domain.library.service.LibraryPreferences
import java.util.Date

@Composable
fun ChapterListDialog(
    onDismissRequest: () -> Unit,
    screenModel: ReaderSettingsScreenModel,
    chapters: List<ReaderChapterItem>,
    onClickChapter: (Chapter) -> Unit,
    onBookmark: (Chapter) -> Unit,
) {
    val manga by screenModel.mangaFlow.collectAsState()
    val context = LocalContext.current
    val state = rememberLazyListState(chapters.indexOfFirst { it.isCurrent }.coerceAtLeast(0))

    AdaptiveSheet(
        onDismissRequest = onDismissRequest,
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier.defaultMinSize(minHeight = 200.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(
                items = chapters,
                key = { "chapter-${it.chapter.id}" },
            ) { chapterItem ->
                MangaChapterListItem(
                    title = chapterItem.chapter.name,
                    date = chapterItem.chapter.dateUpload
                        .takeIf { it > 0L }
                        ?.let {
                            // SY -->
                            if (manga?.isEhBasedManga() == true) {
                                MetadataUtil.EX_DATE_FORMAT.format(Date(it))
                            } else {
                                Date(it).toRelativeString(context, chapterItem.dateFormat)
                            }
                            // SY <--
                        },
                    readProgress = null,
                    scanlator = chapterItem.chapter.scanlator,
                    sourceName = null,
                    read = chapterItem.chapter.read,
                    bookmark = chapterItem.chapter.bookmark,
                    selected = false,
                    downloadIndicatorEnabled = false,
                    downloadStateProvider = { Download.State.NOT_DOWNLOADED },
                    downloadProgressProvider = { 0 },
                    chapterSwipeStartAction = LibraryPreferences.ChapterSwipeAction.ToggleBookmark,
                    chapterSwipeEndAction = LibraryPreferences.ChapterSwipeAction.ToggleBookmark,
                    onLongClick = { /*TODO*/ },
                    onClick = { onClickChapter(chapterItem.chapter) },
                    onDownloadClick = null,
                    onChapterSwipe = {
                        onBookmark(chapterItem.chapter)
                    },
                )
            }
        }
    }
}
