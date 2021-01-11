package nl.thairosi.activityx.network.PlaceApiModel

data class PlaceResponse(
    val result: Result,
    val status: String,
    val error_message: String = ""
)