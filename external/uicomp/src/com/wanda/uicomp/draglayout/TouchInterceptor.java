package com.wanda.uicomp.draglayout;

/**
 * @author zhangyuwen 2015-12-03
 */
public interface TouchInterceptor {
  boolean willHandlePullDown();

  boolean willHandlePullUp();
}
