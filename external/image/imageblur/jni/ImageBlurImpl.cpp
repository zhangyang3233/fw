#include <jni.h>
#include <ImageBlur.c>
#include <android/log.h>
#include <android/bitmap.h>
#include <stdio.h>

//----------------------------------------------------------------------
// Native JNI methods
//----------------------------------------------------------------------
static void BlurIntArray(JNIEnv *env, jclass obj, jintArray arrIn, jint w, jint h, jint r)
{
	jint *pix;
	pix = env->GetIntArrayElements(arrIn, 0);
	if (pix == NULL)
		return;
	//Start
	pix = StackBlur(pix, w, h, r);
	//End
	//int size = w * h;
	//jintArray result = env->NewIntArray(size);
	//env->SetIntArrayRegion(result, 0, size, pix);
	env->ReleaseIntArrayElements(arrIn, pix, 0);
	//return result;
}

static void BlurBitMap(JNIEnv *env, jclass obj, jobject bitmapIn, jint r)
{
	AndroidBitmapInfo infoIn;
	void* pixelsIn;
	int ret;

	// Get image info
	if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoIn)) < 0)
		return;
	// Check image
	if (infoIn.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
		return;
	// Lock all images
	if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsIn)) < 0) {
		//AndroidBitmap_lockPixels failed!
	}
	//height width
	int h = infoIn.height;
	int w = infoIn.width;

	//Start
	pixelsIn = StackBlur((int*)pixelsIn, w, h, r);
	//End

	// Unlocks everything
	AndroidBitmap_unlockPixels(env, bitmapIn);
}
//----------------------------------------------------------------------


/*
 * JNI registration.
 */
static JNINativeMethod gJavaImageBlur[] = {
    { "nativeBlurBitMap", "(Landroid/graphics/Bitmap;I)V", (void*) BlurBitMap },
    { "nativeBlurIntArray", "([IIII)V", (void*) BlurIntArray },
};

static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        fprintf(stderr,
            "Native registration unable to find class '%s'\n", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        fprintf(stderr, "RegisterNatives failed for '%s'\n", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int registerImageBlur(JNIEnv* env)
{
    if (!registerNativeMethods(env, "com/zycoder/imageblur/jni/ImageBlur", gJavaImageBlur,
        sizeof(gJavaImageBlur) / sizeof(gJavaImageBlur[0])))
        return JNI_FALSE;

    return JNI_TRUE;
}