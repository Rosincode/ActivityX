package nl.thairosi.activityx.models.PlaceApiModel

data class PlaceResponse(
    val result: Result,
    val status: String,
    val error_message: String = ""
)