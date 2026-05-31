package com.example.core.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.example.core.model.Anime

@JsonClass(generateAdapter = true)
data class ShikimoriImage(
    @Json(name = "original") val original: String?,
    @Json(name = "preview") val preview: String?,
    @Json(name = "x96") val x96: String?,
    @Json(name = "x48") val x48: String?
)

@JsonClass(generateAdapter = true)
data class ShikimoriGenre(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "russian") val russian: String?,
    @Json(name = "kind") val kind: String?
)

@JsonClass(generateAdapter = true)
data class ShikimoriAnime(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "russian") val russian: String?,
    @Json(name = "image") val image: ShikimoriImage?,
    @Json(name = "score") val score: String?,
    @Json(name = "episodes") val episodes: Int?,
    @Json(name = "episodes_aired") val episodesAired: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "kind") val kind: String?,
    @Json(name = "released_on") val releasedOn: String?,
    @Json(name = "aired_on") val airedOn: String?,
    @Json(name = "rating") val rating: String?,
    @Json(name = "description") val description: String? = null,
    @Json(name = "genres") val genres: List<ShikimoriGenre>? = null
) {
    fun toDomain(): Anime {
        val imageUrl = image?.original ?: image?.preview ?: ""
        val absoluteImageUrl = when {
            imageUrl.startsWith("/system") -> "https://shikimori.one$imageUrl"
            imageUrl.startsWith("/assets") -> "https://shikimori.one$imageUrl"
            imageUrl.isEmpty() -> "https://shikimori.one/assets/globals/missing_original.jpg"
            else -> imageUrl
        }
        val scoreDouble = score?.toDoubleOrNull() ?: 0.0
        val genreList = genres?.map { it.russian ?: it.name } ?: emptyList()
        val descStr = description ?: "Нет описания."

        return Anime(
            id = id,
            name = name,
            russian = russian ?: name,
            imageUrl = absoluteImageUrl,
            score = scoreDouble,
            episodes = episodes ?: 0,
            episodesAired = episodesAired ?: 0,
            status = status ?: "unknown",
            kind = kind ?: "tv",
            genres = genreList,
            description = descStr
        )
    }
}
