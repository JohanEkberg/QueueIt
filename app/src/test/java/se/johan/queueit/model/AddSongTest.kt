package se.johan.queueit.model

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import se.johan.queueit.FakeDatabase
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.usecases.AddSong
import se.johan.queueit.model.usecases.GetSongs
import java.util.Date


class AddSongTest {
    val database = FakeDatabase()
    lateinit var addSong: AddSong
    lateinit var getSongs: GetSongs

    @Before
    fun setUp() {
        addSong = AddSong(database)
        getSongs = GetSongs(database)
    }

    @Test
    fun add_artists_to_database() {
        runBlocking {
            addSong(SongEntity(
                songId = 1,
                songName = "AAA",
                songArtistId = 111,
                songAlbumId = 111111,
                songUri = "song/uri1",
                albumUri = "album/uri1",
                format = "mp3",
                duration = "1000",
                resolution = "2000",
                size = 3000,
                timestampDate = Date()
            ))
            addSong(SongEntity(
                songId = 2,
                songName = "BBB",
                songArtistId = 222,
                songAlbumId = 222222,
                songUri = "song/uri2",
                albumUri = "album/uri2",
                format = "mp3",
                duration = "1000",
                resolution = "2000",
                size = 3000,
                timestampDate = Date()
            ))
            addSong(SongEntity(
                songId = 3,
                songName = "CCC",
                songArtistId = 333,
                songAlbumId = 333333,
                songUri = "song/uri3",
                albumUri = "album/uri3",
                format = "mp3",
                duration = "1000",
                resolution = "2000",
                size = 3000,
                timestampDate = Date()
            ))
            addSong(SongEntity(
                songId = 4,
                songName = "DDD",
                songArtistId = 444,
                songAlbumId = 444444,
                songUri = "song/uri4",
                albumUri = "album/uri4",
                format = "mp3",
                duration = "1000",
                resolution = "2000",
                size = 3000,
                timestampDate = Date()
            ))
            val result = getSongs()
            assertEquals(4, result.size)
            assertEquals(1, result[0].songId)
            assertEquals("AAA", result[0].songName)
            assertEquals(2, result[1].songId)
            assertEquals("BBB", result[1].songName)
            assertEquals(3, result[2].songId)
            assertEquals("CCC", result[2].songName)
            assertEquals(4, result[3].songId)
            assertEquals("DDD", result[3].songName)
        }
    }

    @Test
    fun add_invalid_artist_to_database() {
        runBlocking {
            val result = try {
                addSong(SongEntity(
                    songId = 1,
                    songName = "",
                    songArtistId = 111,
                    songAlbumId = 111111,
                    songUri = "song/uri1",
                    albumUri = "album/uri1",
                    format = "mp3",
                    duration = "1000",
                    resolution = "2000",
                    size = 3000,
                    timestampDate = Date()
                ))
                false
            } catch (e: Exception) {
                val success = e.message?.contains("Song data is invalid") ?: false
                assertTrue(success)
                true
            }
            assertTrue(result)
        }
    }
}