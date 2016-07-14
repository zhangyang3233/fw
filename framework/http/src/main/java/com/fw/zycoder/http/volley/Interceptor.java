package com.fw.zycoder.http.volley;

/**
 * 用于拦截解析后的数据，可用于数据校验以及数据补充。
 * 
 * @see com.zycoder.rpc.http.volley.ApiRequest#addInterceptor(Interceptor)
 * 
 *
 */
public interface Interceptor<T> {

  /**
   * 处理拦截数据。
   * 1. 校验数据：
   * 直接判断数据的合法性，如果不合法请抛出异常。
   * 2. 数据填充：
   * 直接操作拦截下来的数据，补充一些额外的信息。
   * 
   * @param input 被操作的数据。
   */
  void intercept(T input);
}
