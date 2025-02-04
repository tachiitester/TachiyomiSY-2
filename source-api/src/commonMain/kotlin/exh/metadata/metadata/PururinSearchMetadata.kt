package exh.metadata.metadata

import android.content.Context
import eu.kanade.tachiyomi.source.R
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.model.copy
import kotlinx.serialization.Serializable

@Serializable
class PururinSearchMetadata : RaisedSearchMetadata() {
    var prId: Int? = null

    var prShortLink: String? = null

    var title by titleDelegate(TITLE_TYPE_TITLE)
    var altTitle by titleDelegate(TITLE_TYPE_ALT_TITLE)

    var thumbnailUrl: String? = null

    var uploaderDisp: String? = null

    var pages: Int? = null

    var fileSize: String? = null

    var ratingCount: Int? = null
    var averageRating: Double? = null

    override fun createMangaInfo(manga: SManga): SManga {
        val key = prId?.let { prId ->
            prShortLink?.let { prShortLink ->
                "/gallery/$prId/$prShortLink"
            }
        }

        val title = title ?: altTitle

        val cover = thumbnailUrl

        val artist = tags.ofNamespace(TAG_NAMESPACE_ARTIST).joinToString { it.name }

        val genres = tagsToGenreString()

        val description = "meta"

        return manga.copy(
            url = key ?: manga.url,
            title = title ?: manga.title,
            thumbnail_url = cover ?: manga.thumbnail_url,
            artist = artist,
            genre = genres,
            description = description,
        )
    }

    override fun getExtraInfoPairs(context: Context): List<Pair<String, String>> {
        return with(context) {
            listOfNotNull(
                getItem(prId) { getString(R.string.id) },
                getItem(title) { getString(R.string.title) },
                getItem(altTitle) { getString(R.string.alt_title) },
                getItem(thumbnailUrl) { getString(R.string.thumbnail_url) },
                getItem(uploaderDisp) { getString(R.string.uploader_capital) },
                getItem(uploader) { getString(R.string.uploader) },
                getItem(pages) { getString(R.string.page_count) },
                getItem(fileSize) { getString(R.string.gallery_size) },
                getItem(ratingCount) { getString(R.string.total_ratings) },
                getItem(averageRating) { getString(R.string.average_rating) },
            )
        }
    }

    companion object {
        private const val TITLE_TYPE_TITLE = 0
        private const val TITLE_TYPE_ALT_TITLE = 1

        const val TAG_TYPE_DEFAULT = 0

        private const val TAG_NAMESPACE_ARTIST = "artist"
        const val TAG_NAMESPACE_CATEGORY = "category"

        const val BASE_URL = "https://pururin.io"
    }
}
