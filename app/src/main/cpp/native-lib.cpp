// API key storage

// apiKey:
// This directly refers to the method name that you'll be using in Kotlin later on.

// Keys:
// This refers to the Kotlin object in which you want to use your API key, where you'll interact with the C++ coded, and get a reference to your API key (which you can use throughout your app).

// com_package_name:
// This refers to the package name corresponding to the Keys Kotlin object here.
// This should always point to the package of the class where you intend to use it.
// So, if the package name is com.package.name, the . (periods) are replaced with _ (underscores),
// and it becomes com_package_name.

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_nl_thairosi_activityx_Keys_apiKey(JNIEnv *env, jobject object) {
    std::string api_key = "AIzaSyCofXZEndG5WokT6i6n5fdMabW3IWkGiRc";
    return env->NewStringUTF(api_key.c_str());
}