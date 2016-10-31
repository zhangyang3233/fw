package com.hongyu.reward.config;

import android.os.Environment;

/**
 * 存放全局常量
 * 
 * @auther centos
 * @since 2015-3-17 上午1:32:12
 */
public final class Constants {

  public static final class APP_EVENT{
    public static final String EVENT_PUBLISH_REWARD = "publish_reward";
    public static final String EVENT_PUBLISH_CANCEL = "publish_cancel";
    public static final String EVENT_PUBLISH_CANCEL_BY_RECEIVED = "publish_cancel_by_received";
    public static final String EVENT_PAY_SUCCESS_SUM = "publish_pay_success_sum";
    public static final String EVENT_PAY_FAILED = "publish_pay_failed";
    public static final String EVENT_REGISTER_SUCCESS = "register_success";
    public static final String EVENT_WITHDRAWALS_SUM = "withdrawals_sum";
  }
  public static final class PGY{
    public static final String APPID = "527aedc1c916cdd5e803eec807e6940a";
  }

  public static final class WX {
    public static final String AppID = "wx858b0a22ccdace16";
    public static final String AppSecret = "26c1342471b15405ba71bb9d29b6f592";
    /**
     * 发起任务的人分享
     * 分享商户 需要加
     * order_id
     * save_time
     * save_seat 参数
     */
    public static final String share_shop = "http://api.weiyixuanshang.com/share/index1?";
    /**
     * 接受任务的人分享
     * 分享app 需要加
     * shop_name 参数
     */
    public static final String share_app = "http://api.weiyixuanshang.com/share/index2?";
  }

  public static final class Path {
    public static final String DISK_BASE_PATH =
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/reward";

    // 图片保存地址
    public static final String PATH_IMAGES = DISK_BASE_PATH + "/images";
    public static final String PATH_IMAGES_TEMP = DISK_BASE_PATH + "/images/temp";
  }

  // App常量专用
  public final class App {

    // 可以用此final常量，作为JAVA预编译指令使用
    // 是否是测试环境，如果不是的话，将会使用线上服务器相关配置
    public static final boolean DEVELOP = false;
    // 是否打开测试开关？如果是测试状态，将会打印更多信息。
    public static final boolean DEBUG = false;

    // 数据库版本号
    // ***************IMPORTANT****************
    // 数据库有更新时，必须更新数据库版本，内部数据库测试，只允许增加最后两位数字，但在上线时，最后两位会变为00
    public static final int DATABASE_VERSION = 1;

    // 欢迎screen的版本号。在需要时，设置为要发布的VersionCode
    public static final int WELCOME_SCREEN_VERSION = 37;
    public static final int GUIDE_VERSION = 30;

    public static final String CACHE_DIR = "cache";
    public static final String CACHE_IMAGE = "image";

    public static final String VERIFY_KEY = "3.141592653589793";
    public static final String APP_CLIENTID = "app_clientId";
    public static final String APP_USERID = "user_id";
    // 微信开放平台AppID
    public static final String WX_APPID = "wx7339f7e0ce5f93cd";
    public static final String WX_APPSECRET = "123935aa010eec7443c6a6f3e059d001";
  }

  public final class Pref {
    // 用户信息
    public static final String USER_INFO = "com.hongyu.reward.USER_INFO";
    // 用户设置
    public static final String SETTINGS = "com.hongyu.reward.SETTINGS";

    public static final String STAT = "com.hongyu.reward.STAT";

    public static final String PREFERENCES_NAME = "ZDS_REFERENCE";
    public static final String GUIDE_KEY = "guide_key";
    public static final String TOKEN = "token";
    public static final String PUSHCODE = "PushCode";
    public static final String CURRENT_CITY = "current_city";

    public static final String AUDIO_CUES = "audioCues"; // 声音提示
    public static final String SHOCK_CUES = "shockCues"; // 震动提示

  }

  public final class Pay {
    // 商户PID
    public static final String PARTNER = "2088021993904511";
    // 商户收款账号
    public static final String SELLER = "weiyixuanshang@sina.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE =
        "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKXxCit9RmcT74fywDugqz9CQYYRqRZxqaEp5ZDjvkxg4k+28ojNfi4Fyp5hTOiq1H/+NfPOuWV2/kpeN7ebUs0N45V8cpAvx+IyfSmOmFkvySI8jQJao1J0J0W+7saUAunVOyMKa1r0bsKfE0aVabMXaCtGf5ztT+0ozEli+fx1AgMBAAECgYEAlFdn+fyhZSAjLurJAgTdUU2PeaRr/Y5aT6I+gEm3Ilwxs4UeR0E8HTQ8L11X/V5JXKoJ3QveHGBFboHPWSlW2FrG5q22kfmaMRAlJuVj33EZKVL40IkIZN1gb1nFe9xuYnx1qWhpedNoTuVIqrkWs+9ulWmnfLs4RzOVHZtTxoECQQDcsBhnzAcSWob+soRqAhi+SI3922cJ6mlOlWBF8sRGks+bPQ64Lqav+UBEn1GbliPvavYy/GHoZVydTvluXQilAkEAwH5ndzlo0pDRQEvrvI0SZE2lCFE7YoLtt+mQ1OjK+7CEB4f8ZseCXvb2hIsUwMUkxbIvghz2MbfbgJuSP/ULkQJBAInCaxmwJOHyHL61FS9Pa76sb+Z9pQ5tg5sIZ/aCGr+rOWlAbgXPKUJEJzgu87RZvsjfThx5q2x0iaGxAMdBz/kCQQCSdlsxOuxH8spUOrAGSexphy5/spcVsHtBiSSa6vmvIbKDrHNenITUzuHpHY3P0vDUv2woevLUB4mrRDO2GDQRAkEAzDEfRfx8/cb62v50UXGo/pXtluNywpR5B/S08qjfbfzcjH5XxOmueG0eim2hbaYdh/1KAVQSh+WrXcMEg3uzrg==";
    // 支付宝公钥
    public static final String RSA_PUBLIC =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCl8QorfUZnE++H8sA7oKs/QkGGEakWcamhKeWQ475MYOJPtvKIzX4uBcqeYUzoqtR//jXzzrlldv5KXje3m1LNDeOVfHKQL8fiMn0pjphZL8kiPI0CWqNSdCdFvu7GlALp1TsjCmta9G7CnxNGlWmzF2grRn+c7U/tKMxJYvn8dQIDAQAB";
    // 回调路径
    public static final String back_url = "http://api.longcxlt.com/pay/alipay_notify";


    // wechat
    // appid
    // 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme=""/>为新设置的appid
    public static final String WX_APP_ID = "";
    // 商户号
    public static final String WX_MCH_ID = "";
    // API密钥，在商户平台设置
    public static final String WX_API_KEY = "";
  }

  public final class XGNotification {
    // 信鸽通知样式 下发通知时指定build_id，样式id的取值范围为[1, 4096]
    public static final int DEFAULT_STYLT_ID = 1;
  }

  // 服务器API相关专用
  public final class Server {

    // 可以用此final常量，作为JAVA预编译指令使用
    // 是否是测试环境，如果不是的话，将会使用线上服务器相关配置
    public static final boolean DEVELOP = false;
    // 是否打开测试开关？如果是测试状态，将会打印更多信息。
    public static final boolean DEBUG = false;

    // 数据库版本号
    // ***************IMPORTANT****************
    // 数据库有更新时，必须更新数据库版本，内部数据库测试，只允许增加最后两位数字，但在上线时，最后两位会变为00
    public static final int DATABASE_VERSION = 3900;

    // 欢迎screen的版本号。在需要时，设置为要发布的VersionCode
    public static final int WELCOME_SCREEN_VERSION = 35;
    public static final int GUIDE_VERSION = 30;

    public static final String CACHE_DIR = "cache";
    public static final String CACHE_IMAGE = "image";

    public static final String VERIFY_KEY = "3.141592653589793";
    public static final String APP_CLIENTID = "app_clientId";
    public static final String APP_USERID = "app_userId";
    // 微信开放平台AppID
    public static final String WX_APPID = "wx25d948f96299d7ee";
    public static final String WX_APPSECRET = "186552e3de1e65549fd569a353d98336";

    public static final String XG_ACCESS_ID = "2100150299";
    public static final String XG_ACCESS_KEY = "AJY852Y42FVX";
    public static final String XG_SECRET_KEY = "3ce9be4ede7e527dd640aa68d6cf2910";

    // 主域名
    public static final String API_PREFIX = "http://api.weiyixuanshang.com";


    // 请求发送验证码
    public static final String API_LOGIN_CODE = "app/get_code.php";

    // 获取token
    public static final String API_GET_TOKEN = "/token/access";

    // =========================
    // 用户相关
    // =========================
    // 登录
    public static final String API_LOGIN = "/member/login";
    // 注册
    public static final String API_REGISTER = "/member/register";
    // 找回密码验证
    public static final String API_FORPWD_VERIFY = "/member/find_pwd_validate";
    // 找回密码设置
    public static final String API_FORPWD = "/member/find_pwd";
    // 修改密码
    public static final String API_RESET_PWD = "/member/reset_pwd";
    // 获取用户信息
    public static final String API_USER_INFO = "/member/info";
    // 获取短信验证码
    public static final String API_GET_CODE = "/member/sendsms";
    // 修改用户信息
    public static final String API_EDIT_INFO = "/member/edit";
    // 首页广告和资源位
    public static final String API_INDEX_AD = "/position/banner";

    // =========================
    // 用户相关
    // =========================
    // 获取商家列表
    public static final String API_STORE_LIST = "/shop/shop_list";
    // 获取已发布悬赏的商家列表
    public static final String API_SHOP_RECE_LIST = "/shop/order_shop_list";
    public static final String API_SHOP_TAG = "/shop/search_tag";

    // =========================
    // 订单相关
    // =========================
    // 发布悬赏
    public static final String API_ORDER_PUBLISH = "/order/publish";
    // 获取单个订单
    public static final String API_ORDER_ITEM = "/order/item";
    // 追加赏金
    public static final String API_ORDER_ADD_PRICE = "/order/add_to_price";
    // 查看悬赏列表
    public static final String API_ORDER_LIST = "/order/order_list";
    // 领取任务
    public static final String API_ORDER_RECEIVER = "/order/receive_order";
    // 获取领赏人列表
    public static final String API_ORDER_RECELIST = "/order/receive_list";
    // 获取订单领赏个数
    public static final String API_ORDER_RECE_NUM = "/order/receive_num";
    // 使用/不使用领赏人
    public static final String API_ORDER_USERECE = "/order/use_receive";
    // 完成任务
    public static final String API_ORDER_DONE = "/order/done";
    // 我的订单
    public static final String API_ORDER_MY = "/order/my_order";
    // 取消订单
    public static final String API_ORDER_CANCEL = "/order/cancel";
    public static final String API_ORDER_ACTIVE = "/order/active";
    public static final String API_ORDER_ACTIVE_RE = "/order/active_receive";

    // =========================
    // 评论相关
    // =========================
    // 获取评论标签
    public static final String API_COMMENT_TAG = "/comment/tag";
    // 订单评论
    public static final String API_COMMENT_ORDER = "/comment/order";
    // 获取我的单个订单评论
    public static final String API_COMMENT_ITEM = "/comment/item";
    // 获取我的评论
    public static final String API_COMMENT_MY = "/comment/my_comment";


    // =========================
    // 账户相关
    // =========================
    // 获取账户明细
    public static final String API_ACCOUNT_INFO = "/account/detail";
    // 提现
    public static final String API_ACCOUNT_GET = "/account/draw_cash";

    // 获取投诉信息
    public static final String API_REPORT_INFO = "/complaint/reason";
    // 投诉
    public static final String API_REPORT = "/complaint/submit";
    // 消息列表
    public static final String API_MESSAGE_LIST = "/message/message_list";

    // 分享后调用增加积分
    public static final String API_SHARE_POINT = "/share/sharepoint";



    public static final String API_PAY_SIGN = "/pay/sign";


    // 退出登录
    public static final String API_LOGOUT = "";
  }
}
