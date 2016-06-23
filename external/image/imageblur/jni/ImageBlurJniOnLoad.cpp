#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <assert.h>

extern int registerImageBlur(JNIEnv*);

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }
    assert(env != NULL);

    if(registerImageBlur(env) != JNI_TRUE) {
        return -1;
    }

    return JNI_VERSION_1_4;
}