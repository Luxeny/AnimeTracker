package com.example.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShikimoriApiService {
    @GET("api/animes")
    suspend fun getAnimes(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("order") order: String = "popularity",
        @Query("search") search: String? = null
    ): List<ShikimoriAnime>

    @GET("api/animes/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Int
    ): ShikimoriAnime
}
