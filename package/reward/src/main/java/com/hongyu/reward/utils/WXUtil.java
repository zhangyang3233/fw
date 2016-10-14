package com.hongyu.reward.utils;

import android.content.Context;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.config.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class WXUtil {
    private static WXUtil instance;
    private static IWXAPI api;

    public static WXUtil getInstance() {
        if(instance == null){
            instance = new WXUtil();
        }
        return instance;
    }


    public static IWXAPI getApi() {
        return api;
    }

    public void registWX(Context context){
        api = WXAPIFactory.createWXAPI(context.getApplicationContext(), Constants.WX.AppID, true);
        api.registerApp(Constants.WX.AppID);
    }

    // 文本分享
//    public void shareText() {
//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = "微信文本分享测试";
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;   // 发送文本类型的消息时，title字段不起作用
//        msg.title = "Will be ignored";
//        msg.description = "微信描述分享测试";   // 构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
//        req.message = msg;   // 分享或收藏的目标场景，通过修改scene场景值实现。
//        // 发送到聊天界面 —— WXSceneSession
//        // 发送到朋友圈 —— WXSceneTimeline
//        // 添加到微信收藏 —— WXSceneFavorite
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        // 调用api接口发送数据到微信
//        boolean req1 = api.sendReq(req);
//        T.show(String.valueOf(req1));
//    }

    private String buildTransaction(String text) {
        return text;
    }


    public void receiveShare(String shopName){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.WX.share_app+"shop_name="+shopName;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "快来使用"+ getAppName() +"吧~~~";
        msg.description = "我在 "+shopName+" 排队吃饭，通过维依悬赏APP把排队号给了更紧急，更需要的人，助人为乐，还小赚了一笔！下次需要的时候，我也用悬赏少排队~";
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
//        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public void publishShare(String order_id, String save_seat, String save_time){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.WX.share_shop+"order_id="+order_id+"&save_time="+save_time+"&save_seat="+save_seat;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "快来使用"+ getAppName()+"吧~~~";
        msg.description = "我使用"+getAppName()+", 节省了"+save_time+"分钟!从此吃饭不排队~~~~";
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
//        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }


    private String getAppName(){
        return GlobalConfig.getAppContext().getResources().getString(R.string.app_name_reward);
    }
}
