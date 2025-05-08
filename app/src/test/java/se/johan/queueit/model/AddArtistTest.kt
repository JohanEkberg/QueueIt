package se.johan.queueit.model

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import se.johan.queueit.FakeDatabase
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.usecases.AddArtist
import se.johan.queueit.model.usecases.GetArtists

class AddArtistTest {
    val database = FakeDatabase()
    lateinit var addArtist: AddArtist
    lateinit var getArtists: GetArtists

    @Before
    fun setUp() {
        addArtist = AddArtist(database)
        getArtists = GetArtists(database)
    }

    @Test
    fun add_artists_to_database() {
        runBlocking {
            addArtist(ArtistEntity(1, "AAA"))
            addArtist(ArtistEntity(2, "BBB"))
            addArtist(ArtistEntity(3, "CCC"))
            addArtist(ArtistEntity(4, "DDD"))
            val result = getArtists()
            assertEquals(4, result.size)
            assertEquals(1, result[0].artistId)
            assertEquals("AAA", result[0].artistName)
            assertEquals(2, result[1].artistId)
            assertEquals("BBB", result[1].artistName)
            assertEquals(3, result[2].artistId)
            assertEquals("CCC", result[2].artistName)
            assertEquals(4, result[3].artistId)
            assertEquals("DDD", result[3].artistName)
        }
    }

    @Test
    fun add_invalid_artist_to_database() {
        runBlocking {
            val result = try {
                addArtist(ArtistEntity(1, ""))
                false
            } catch (e: Exception) {
                val success = e.message?.contains("Artist data is invalid") ?: false
                assertTrue(success)
                true
            }
            assertTrue(result)
        }
    }
}