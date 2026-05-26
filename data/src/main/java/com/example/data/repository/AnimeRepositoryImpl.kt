package com.example.data.repository

import com.example.data.local.UserAnimeDao
import com.example.data.local.UserAnimeEntity
import com.example.data.remote.ShikimoriApiService
import com.example.domain.model.Anime
import com.example.domain.model.UserAnime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AnimeRepositoryImpl(
    private val apiService: ShikimoriApiService,
    private val userAnimeDao: UserAnimeDao
) : AnimeRepository {

    override fun searchAnimes(query: String): Flow<List<Anime>> = flow {
        if (query.trim().isEmpty()) {
            emit(getMockPopularAnimes())
            return@flow
        }
        try {
            val remoteList = apiService.getAnimes(search = query.trim())
            if (remoteList.isEmpty()) {
                emit(filterMocks(query))
            } else {
                emit(remoteList.map { it.toDomain() })
            }
        } catch (e: Exception) {
            emit(filterMocks(query))
        }
    }

    override fun getAnimeDetails(id: Int): Flow<Anime?> = flow {
        try {
            val remoteDetails = apiService.getAnimeDetails(id)
            emit(remoteDetails.toDomain())
        } catch (e: Exception) {
            // Check local mocks or database
            val mockItem = getMockPopularAnimes().find { it.id == id }
            emit(mockItem)
        }
    }

    override fun getRecommendations(): Flow<List<Anime>> = flow {
        try {
            val remoteRecommended = apiService.getAnimes(order = "popularity", limit = 10)
            if (remoteRecommended.isEmpty()) {
                emit(getMockPopularAnimes())
            } else {
                emit(remoteRecommended.map { it.toDomain() })
            }
        } catch (e: Exception) {
            emit(getMockPopularAnimes())
        }
    }

    override fun getRandomAnimes(page: Int): Flow<List<Anime>> = flow {
        try {
            val remoteRecommended = apiService.getAnimes(page = page, limit = 10, order = "popularity")
            if (remoteRecommended.isEmpty()) {
                emit(getMockPopularAnimes().shuffled())
            } else {
                emit(remoteRecommended.map { it.toDomain() }.shuffled())
            }
        } catch (e: Exception) {
            emit(getMockPopularAnimes().shuffled())
        }
    }

    override fun getWatchlist(): Flow<List<UserAnime>> {
        return userAnimeDao.getWatchlistFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getWatchlistAnime(id: Int): Flow<UserAnime?> {
        return userAnimeDao.getWatchlistAnimeFlow(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveToWatchlist(userAnime: UserAnime) {
        userAnimeDao.insert(UserAnimeEntity.fromDomain(userAnime))
    }

    override suspend fun deleteFromWatchlist(id: Int) {
        userAnimeDao.deleteById(id)
    }

    private fun filterMocks(query: String): List<Anime> {
        val lowercaseQuery = query.trim().lowercase()
        return getMockPopularAnimes().filter {
            it.name.lowercase().contains(lowercaseQuery) ||
                    it.russian.lowercase().contains(lowercaseQuery) ||
                    it.genres.any { genre -> genre.lowercase().contains(lowercaseQuery) }
        }
    }

    private fun getMockPopularAnimes(): List<Anime> {
        return listOf(
            Anime(
                id = 16498,
                name = "Shingeki no Kyojin",
                russian = "Атака титанов",
                imageUrl = "https://shikimori.one/system/animes/original/16498.jpg",
                score = 9.1,
                episodes = 25,
                episodesAired = 25,
                status = "released",
                kind = "tv",
                genres = listOf("Экшен", "Военное", "Детектив", "Фэнтези"),
                description = "Прошло более ста лет с тех пор, как человечество столкнулось со смертельной угрозой — гигантскими человекоподобными существами, которых назвали титанами. Скрывшись за тремя высокими концентрическими стенами, люди смогли вернуть себе долгие десятилетия мирной жизни. Но всё меняется в тот роковой день, когда супергигантский титан разрушает внешние врата стены Мария..."
            ),
            Anime(
                id = 1535,
                name = "Death Note",
                russian = "Тетрадь смерти",
                imageUrl = "https://shikimori.one/system/animes/original/1535.jpg",
                score = 8.6,
                episodes = 37,
                episodesAired = 37,
                status = "released",
                kind = "tv",
                genres = listOf("Детектив", "Психологическое", "Сверхъестественное", "Триллер"),
                description = "Лайт Ягами — блестящий и примерный ученик выпускного класса, который находит загадочную чёрную тетрадь... С её помощью каждый, чьё имя будет в неё записано при знании его лица, погибнет в течение нескольких десятков секунд. Лайт решает очистить мир от преступности и построить новое совершенное общество во главе со своим правосудием."
            ),
            Anime(
                id = 20,
                name = "Naruto",
                russian = "Наруто",
                imageUrl = "https://shikimori.one/system/animes/original/20.jpg",
                score = 8.0,
                episodes = 220,
                episodesAired = 220,
                status = "released",
                kind = "tv",
                genres = listOf("Экшен", "Комедия", "Сёнен", "Боевые искусства"),
                description = "В центре сюжета — юный ниндзя-сирота Наруто Узумаки из Скрытой деревни Листа. Внутри него запечатан Смертоносный Девятихвостый Демон-Лис, из-за чего односельчане долгое время избегали и презирали мальчика. Однако Наруто не сдаётся и ставит перед собой величайшую цель в жизни — вопреки всему стать следующим Хокагэ."
            ),
            Anime(
                id = 38000,
                name = "Kimetsu no Yaiba",
                russian = "Клинок, рассекающий демонов",
                imageUrl = "https://shikimori.one/system/animes/original/38000.jpg",
                score = 8.5,
                episodes = 26,
                episodesAired = 26,
                status = "released",
                kind = "tv",
                genres = listOf("Экшен", "Фэнтези", "Сёнен", "Исторический"),
                description = "Эпоха Тайсё. Тандзиро Камадо — добрый юноша, чей семейный очаг жестоко разрушает нападение прожорливых демонов, перебивших всех его родных, кроме младшей сестры Нэдзуко. Однако Нэдзуко сама обращена в демоническое существо... Тандзиро вступает на путь Охотника на Демонов, стремясь спасти сестру и найти лекарство."
            ),
            Anime(
                id = 40748,
                name = "Jujutsu Kaisen",
                russian = "Магическая битва",
                imageUrl = "https://shikimori.one/system/animes/original/40748.jpg",
                score = 8.7,
                episodes = 24,
                episodesAired = 24,
                status = "released",
                kind = "tv",
                genres = listOf("Экшен", "Сёнен", "Сверхъестественное", "Школа"),
                description = "Юдзи Итадори — обычный старшеклассник с выдающимися физическими способностями. Стараясь уберечь друзей из оккультного клуба от злых духов, Юдзи непреднамеренно проглатывает высохший палец могущественного Двуликого проклятия — Рёмена Сукуны. Теперь он вынужден поступить в Магический техникум, чтобы найти и поглотить оставшиеся пальцы демона."
            ),
            Anime(
                id = 9253,
                name = "Steins;Gate",
                russian = "Врата Штейна",
                imageUrl = "https://shikimori.one/system/animes/original/9253.jpg",
                score = 9.1,
                episodes = 24,
                episodesAired = 24,
                status = "released",
                kind = "tv",
                genres = listOf("Фантастика", "Драма", "Триллер", "Психологическое"),
                description = "Ринтаро Окабэ — эксцентричный студент, увлечённый созданием безумных гаджетов в своей съёмной квартирке-лаборатории в Акихабаре. Его команда разрабатывает странное устройство — мобильную микроволновку, способную отправлять текстовые сообщения в прошлое. Вскоре они обнаруживают, что даже крошечные SMS способны необратимо изменять ткань реальности."
            ),
            Anime(
                id = 52991,
                name = "Sousou no Frieren",
                russian = "Провожающая в последний путь Фрирен",
                imageUrl = "https://shikimori.one/system/animes/original/52991.jpg",
                score = 9.3,
                episodes = 28,
                episodesAired = 28,
                status = "released",
                kind = "tv",
                genres = listOf("Приключения", "Драма", "Фэнтези"),
                description = "Отряд героя Гиммеля триумфально возвращается после долгого десятилетнего похода и победы над ужасным Королём Демонов. Для эльфийки-магички Фрирен это было лишь крошечным мгновением в её почти бесконечном вековом существовании. Фрирен отправляется в кругосветное странствие. Спустя десятилетия она возвращается в город и застаёт друзей престарелыми, что пробуждает веру понять природу человеческих чувств."
            )
        )
    }
}
