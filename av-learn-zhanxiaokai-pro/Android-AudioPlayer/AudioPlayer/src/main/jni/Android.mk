LOCAL_PATH := $(call my-dir)

$(info "print log.")
$(info "LOCAL_PATH=$(LOCAL_PATH)")
include $(call all-subdir-makefiles)
include $(CLEAR_VARS)