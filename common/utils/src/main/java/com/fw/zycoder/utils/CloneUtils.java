package com.fw.zycoder.utils;

import com.google.gson.Gson;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class CloneUtils {

  /**
   * use gson to serialization model. use deserialize to implements clone
   * 
   */
  public static <T> T clone(T model, Class<T> clazz) {
    if (model == null) {
      return null;
    }
    Gson gson = new Gson();
    String json = gson.toJson(model);
    return gson.fromJson(json, clazz);
  }


}
