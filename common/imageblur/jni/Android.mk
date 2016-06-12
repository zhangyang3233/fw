LOCAL_PATH		:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ImageBlur
LOCAL_SRC_FILES := \
                   ImageBlurImpl.cpp \
                   ImageBlurJniOnLoad.cpp
LOCAL_LDLIBS    := -lm -llog -ljnigraphics -landroid

LOCAL_C_INCLUDES += /
 $(JNI_H_INCLUDE)

include $(BUILD_SHARED_LIBRARY)