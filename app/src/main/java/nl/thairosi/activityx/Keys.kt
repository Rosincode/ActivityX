package nl.thairosi.activityx

/**
 * This object supplies a library with secret keys
 */
object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}