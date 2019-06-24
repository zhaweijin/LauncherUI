LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_STATIC_JAVA_LIBRARIES := guava android-support-v4 locsdk universal-image-loader



LOCAL_PACKAGE_NAME := LauncherUI
LOCAL_CERTIFICATE := platform

#LOCAL_JNI_SHARED_LIBRARIES := libscreen_adjust_jni

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := locsdk:libs/locSDK_3.3.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += universal-image-loader:libs/universal-image-loader-1.8.4.jar


include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
