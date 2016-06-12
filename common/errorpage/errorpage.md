# 简介
本库改写于:https://github.com/Ereza/CustomActivityOnCrash
# 使用
// application 中初始化:
CustomActivityOnCrash.install(this);
// 程序在后台崩溃是否显示错误页面
CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
// 是否显示错误详细信息
CustomActivityOnCrash.setShowErrorDetails(false);
// 设置错误页面图片
CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.customactivityoncrash_error_image);
// 设置是否重启程序
CustomActivityOnCrash.setEnableAppRestart(true);
// 设置错误页面事件的监听
CustomActivityOnCrash.setEventListener(null);
// 设置重启的activity
CustomActivityOnCrash.setRestartActivityClass(null);
