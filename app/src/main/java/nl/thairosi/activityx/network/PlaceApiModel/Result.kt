package nl.thairosi.activityx.network.PlaceApiModel

data class Result(
    val formatted_address: String,
    val geometry: Geometry,
    val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val place_id: String,
    val types: List<String>,
    val url: String,
)