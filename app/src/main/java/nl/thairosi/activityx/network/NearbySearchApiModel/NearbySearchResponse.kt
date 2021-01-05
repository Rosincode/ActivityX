package nl.thairosi.activityx.network.NearbySearchApiModel

data class NearbySearchResponse(
    val html_attributions: List<Any>,
    val results: List<Result>,
    val status: String
)