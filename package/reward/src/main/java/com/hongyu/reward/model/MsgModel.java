package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class MsgModel extends BaseModel {
    private ArrayList<MessageModel> data;

    public ArrayList<MessageModel> getData() {
        return data;
    }

    public void setData(ArrayList<MessageModel> data) {
        this.data = data;
    }

    public static class MessageModel{
        public String title;
        public String date;
        public String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
