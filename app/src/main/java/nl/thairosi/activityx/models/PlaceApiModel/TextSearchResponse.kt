package nl.thairosi.activityx.models.PlaceApiModel

/**
 * This is the data class used as a model for place text search API calls
 * The result value will hold the received places in a List
 */
data class TextSearchResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)