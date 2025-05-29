package se.johan.queueit.apiservices

object LyricsApiQueryParameters {
    const val ARTIST_KEY = "artist"
    const val TITLE_KEY = "title"
}


object OverviewApiQueryParameters {
    const val METHOD_KEY = "method"
    const val METHOD_VALUE = "artist.getinfo"
    const val ARTIST_KEY = "artist"
    const val API_KEY = "api_key"
    //const val API_VALUE = "39021aad5adbbe39899dbe70a855c129" // TODO: This API key is for my development account, investigate how this should be managed.
    const val FORMAT_KEY = "format"
    const val FORMAT_VALUE = "json"
}

/**
 * Here is an example how to use.
 *
 *  curl -G "https://ws.audioscrobbler.com/2.0/"
 *  --data-urlencode "method=artist.getinfo"
 *  --data-urlencode "artist=Radiohead"
 *  --data-urlencode "api_key=39021aad5adbbe39899dbe70a855c129"
 *  --data-urlencode "format=json"
 */
