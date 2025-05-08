package se.johan.queueit.model.usecases

data class AudioDataUseCases (
    val databaseExist: DatabaseExist,
    val addArtist: AddArtist,
    val addAlbum: AddAlbum,
    val addSong: AddSong,
    val getPagedAlbum: GetPagedAlbum,
    val getPagedArtistWithSongs: GetPagedArtistWithSongs,
    val getPagedAlbumWithSong: GetPagedAlbumWithSong,
    val getArtists: GetArtists,
    val getArtistById: GetArtistById,
    val getArtistByName: GetArtistByName,
    val getArtistWithSongs: GetArtistWithSongs,
    val getArtistWithAlbums: GetArtistWithAlbums,
    val getAlbumSongs: GetAlbumSongs,
    val getAlbum: GetAlbumById,
    val getAlbumByName: GetAlbumByName,
    val getSongByName: GetSongByName,
    val deleteAllData: DeleteAllData,
    val clearAllDatabaseTables: ClearAllDatabaseTables,
    val getSongs: GetSongs
)

class AudioDataException(
    message: String = "",
    val errorCode: Int = 0
) : Exception(message)