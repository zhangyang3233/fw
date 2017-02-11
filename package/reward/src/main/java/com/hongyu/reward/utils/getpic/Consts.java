package com.hongyu.reward.utils.getpic;

import android.os.Environment;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class Consts {
    public static final int REQUEST_CODE_PICK_FROM_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_A_PICTURE = 2;
    public static final int REQUEST_CODE_CROP_A_PICTURE = 3;
    public static final String DIRECTORY_SD_ROOT = Environment
            .getExternalStorageDirectory().getPath();

    public static final String DIRECTORY_HEAD = DIRECTORY_SD_ROOT + "/weiyixuanshang/Head";
    public static final String JPG_FILE_SUFFIX = ".jpg";
    public static final String HEAD_TMP_NAME = "head";
    public static final String mShootPath = DIRECTORY_SD_ROOT + "/" + HEAD_TMP_NAME
            + JPG_FILE_SUFFIX;
    public static final String mTempHeadPath = DIRECTORY_HEAD + "/" + HEAD_TMP_NAME
            + JPG_FILE_SUFFIX;

    public static final int WIDTH_HEAD_OUTPUT = 150;
    public static final int HEIGHT_HEAD_OUTPUT = 150;


    public static final String A_TICKET_TMP_NAME = "head";
    public static final String A_DIRECTORY_TICKET = DIRECTORY_SD_ROOT + "/weiyixuanshang/Head";
    public static final String A_mTempTicketPath = A_DIRECTORY_TICKET + "/" + A_TICKET_TMP_NAME
            + JPG_FILE_SUFFIX;
    public static final String A_mShootPath = DIRECTORY_SD_ROOT + "/" + A_TICKET_TMP_NAME
            + JPG_FILE_SUFFIX;

}
