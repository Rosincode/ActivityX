package nl.thairosi.activityx

object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}