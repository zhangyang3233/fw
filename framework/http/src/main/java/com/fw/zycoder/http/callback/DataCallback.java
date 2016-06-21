package com.fw.zycoder.http.callback;

/**
 *
 */
public interface DataCallback<T> {


  /**
   * Gets call when result arrives or error happens
   * 
   * @param data which parse request or null if error happens
   */
  void onDataCallback(T data);

}
