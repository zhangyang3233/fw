package com.fw.zycoder.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * sp存储工具类 *
 */
public class SPUtil {
        private static String name = "PREFERENCES_NAME";
        private SPUtil() {
        }
        /**
         * 获取SharedPreferences实例对象
         *
         * @param 
         * @return
         */
        private static SharedPreferences getSharedPreference() {
                return GlobalConfig.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        /**
         * 保存一个Boolean类型的值！
         * @param 
         * @param key
         * @param value
         * @return
         */
        public static boolean putBoolean( String key, Boolean value) {
                SharedPreferences sharedPreference = getSharedPreference();
                Editor editor = sharedPreference.edit();
                editor.putBoolean(key, value);
                return editor.commit();
        }
        /**
         * 保存一个int类型的值！
         * @param 
         * @param key
         * @param value
         * @return
         */
        public static boolean putInt( String key, int value) {
                SharedPreferences sharedPreference = getSharedPreference();
                Editor editor = sharedPreference.edit();
                editor.putInt(key, value);
                return editor.commit();
        }
        /**
         * 保存一个float类型的值！
         * @param 
         * @param key
         * @param value
         * @return
         */
        public static boolean putFloat( String key, float value) {
                SharedPreferences sharedPreference = getSharedPreference();
                Editor editor = sharedPreference.edit();
                editor.putFloat(key, value);
                return editor.commit();
        }
        /**
         * 保存一个long类型的值！
         * @param 
         * @param key
         * @param value
         * @return
         */
        public static boolean putLong( String key, long value) {
                SharedPreferences sharedPreference = getSharedPreference();
                Editor editor = sharedPreference.edit();
                editor.putLong(key, value);
                return editor.commit();
        }
        /**
         * 保存一个String类型的值！
         * @param 
         * @param key
         * @param value
         * @return
         */
        public static boolean putString(String key, String value) {
                SharedPreferences sharedPreference = getSharedPreference();
                Editor editor = sharedPreference.edit();
                editor.putString(key, value);
                return editor.commit();
        }

        /**
         * 获取String的value
         *
         * @param 
         * @param key
         * 名字
         * @param defValue
         * 默认值
         * @return
         */
        public static String getString(String key, String defValue) {
                SharedPreferences sharedPreference = getSharedPreference();
                return sharedPreference.getString(key, defValue);
        }

        /**
         * 获取int的value
         *
         * @param 
         * @param key
         * 名字
         * @param defValue
         * 默认值
         * @return
         */
        public static int getInt(String key, int defValue) {
                SharedPreferences sharedPreference = getSharedPreference();
                return sharedPreference.getInt(key, defValue);
        }

        /**
         * 获取float的value
         *
         * @param 
         * @param key
         * 名字
         * @param defValue
         * 默认值
         * @return
         */
        public static float getFloat(String key, Float defValue) {
                SharedPreferences sharedPreference = getSharedPreference();
                return sharedPreference.getFloat(key, defValue);
        }

        /**
         * 获取boolean的value
         *
         * @param 
         * @param key
         * 名字
         * @param defValue
         * 默认值
         * @return
         */
        public static boolean getBoolean(String key,
                        Boolean defValue) {
                SharedPreferences sharedPreference = getSharedPreference();
                return sharedPreference.getBoolean(key, defValue);
        }

        /**
         * 获取long的value
         *
         * @param 
         * @param key
         * 名字
         * @param defValue
         * 默认值
         * @return
         */
        public static long getLong(String key, long defValue) {
                SharedPreferences sharedPreference = getSharedPreference();
                return sharedPreference.getLong(key, defValue);
        }
        
        public static void removeKey(String key) {
        	SharedPreferences sharedPreference = getSharedPreference();
            Editor editor = sharedPreference.edit();
            editor.remove(key);
            editor.commit();
        }

}