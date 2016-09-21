package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class OrderInfoModel extends BaseModel {

    /**
     * order : {"order_id":"100470","user_id":"45","shop_id":"129","type":"0","price":"20.00","usernum":"2","begin_time":"1474253743","end_time":"1474254342","status":"0","nickname":"15900400020","mobile":"15900400020","shop_name":"德克士(临汾迎旭街店)","img":"http://api.weiyixuanshang.com/static/shop/000/000/e3613f7f513937fdfcf8fa7621f16e15.jpg","date":"09月19日11:05","good":"","gcr":"100%","order_num":"0","isComment":"0"}
     * receive : []
     */

    private OrderData data;

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public static class OrderData implements BaseDataModel{
        /**
         * order_id : 100470
         * user_id : 45
         * shop_id : 129
         * type : 0
         * price : 20.00
         * usernum : 2
         * begin_time : 1474253743
         * end_time : 1474254342
         * status : 0
         * nickname : 15900400020
         * mobile : 15900400020
         * shop_name : 德克士(临汾迎旭街店)
         * img : http://api.weiyixuanshang.com/static/shop/000/000/e3613f7f513937fdfcf8fa7621f16e15.jpg
         * date : 09月19日11:05
         * good :
         * gcr : 100%
         * order_num : 0
         * isComment : 0
         */

        private OrderModel order;
        private ReceiveModel receive;

        public OrderModel getOrder() {
            return order;
        }

        public void setOrder(OrderModel order) {
            this.order = order;
        }

        public ReceiveModel getReceive() {
            return receive;
        }

        public void setReceive(ReceiveModel receive) {
            this.receive = receive;
        }


    }
}
