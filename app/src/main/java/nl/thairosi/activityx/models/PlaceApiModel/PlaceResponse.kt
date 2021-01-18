package nl.thairosi.activityx.models.PlaceApiModel

/**
 * This is the data class used as a model for place detail API calls
 * The result value will hold the values of a Place
 */
data class PlaceResponse(
    val result: Result,
    val status: String,
    val error_message: String = ""
)