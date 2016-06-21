package com.fw.zycoder.http.request;


import com.fw.zycoder.http.volley.ApiRequest;

/**
 *
 */
public interface VolleyRequestBuilder {


  /**
   * Build a volley request
   * 
   * @return ApiRequest. Returns ApiRequest never be null
   */
  ApiRequest build();

}
