package se.johan.queueit.model

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import se.johan.queueit.FakeDatabase
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.usecases.AddAlbum
import se.johan.queueit.model.usecases.GetAlbumById

class AddAlbumTest {
    val database = FakeDatabase()
    lateinit var addAlbum: AddAlbum
    lateinit var getAlbum: GetAlbumById

    @Before
    fun setUp() {
        addAlbum = AddAlbum(database)
        getAlbum = GetAlbumById(database)
    }

    @Test
    fun add_artists_to_database() {
        runBlocking {
            addAlbum(AlbumEntity(albumId = 1, albumName = "AAA", albumUri = "uri1", year = "2020", genre = "rock"))
            addAlbum(AlbumEntity(albumId = 2, albumName = "BBB", albumUri = "uri2", year = "2021", genre = "pop"))
            addAlbum(AlbumEntity(albumId = 3, albumName = "CCC", albumUri = "uri3", year = "2022", genre = "dance"))
            addAlbum(AlbumEntity(albumId = 4, albumName = "DDD", albumUri = "uri4", year = "2023", genre = "metal"))
            val result = getAlbum(2)

            assertEquals(2, result.albumId)
            assertEquals("BBB", result.albumName)
            assertEquals("uri2", result.albumUri)
            assertEquals("2021", result.year)
            assertEquals("pop", result.genre)
        }
    }

    @Test
    fun add_invalid_artist_to_database() {
        runBlocking {
            val result = try {
                addAlbum(AlbumEntity(albumId = 4, albumName = "", albumUri = "uri4", year = "2023", genre = "metal"))
                false
            } catch (e: Exception) {
                val success = e.message?.contains("Album data is invalid") ?: false
                assertTrue(success)
                true
            }
            assertTrue(result)
        }
    }
}