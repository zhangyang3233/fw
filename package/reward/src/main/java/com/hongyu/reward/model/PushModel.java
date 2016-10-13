package com.hongyu.reward.model;

import com.umeng.message.entity.UMessage;

/**
 * Created by zhangyang131 on 16/10/11.
 */
public class PushModel implements BaseDataModel{
    String custom;
    String message_id;
    String msg_id;
    String title;
    String url;
    PushInfo push;

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PushInfo getPush() {
        return push;
    }

    public void setPush(PushInfo push) {
        this.push = push;
    }

    public static class PushInfo  implements BaseDataModel{
        String order_id;
        String title;
        String status;
        String content;
        String type;
        String user_id;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static PushModel parse(UMessage uMessage){
        if(uMessage == null){
            return null;
        }
        PushModel pm = new PushModel();
        pm.setCustom(uMessage.custom);
        pm.setMessage_id(uMessage.message_id);
        pm.setMsg_id(uMessage.msg_id);
        pm.setTitle(uMessage.title);
        pm.setUrl(uMessage.url);
        PushInfo pi = new PushInfo();
        pi.setTitle(uMessage.extra.get("title"));
        pi.setContent(uMessage.extra.get("content"));
        pi.setOrder_id(uMessage.extra.get("order_id"));
        pi.setStatus(uMessage.extra.get("status"));
        pi.setType(uMessage.extra.get("type"));
        pi.setUser_id(uMessage.extra.get("user_id"));
        pm.setPush(pi);
        return pm;
    }

}
