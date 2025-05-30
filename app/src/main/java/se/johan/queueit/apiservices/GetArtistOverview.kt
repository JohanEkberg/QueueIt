package se.johan.queueit.apiservices

class GetArtistOverview(private val repository: ArtistOverviewRepository) {
    suspend operator fun invoke(artist: String) : ArtistOverviewApiResult {
        return repository.getArtistOverview(artist)
    }
}