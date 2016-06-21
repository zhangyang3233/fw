package com.fw.zycoder.http.volley;


import com.android.volley.DefaultRetryPolicy;

/**
 *
 */
public class ApiRetryPolicy extends DefaultRetryPolicy {

  /**
   * wanda timeout 60s
   * 30s connect timeout , 30s transfer timeout
   */
  public static final int DEFAULT_TIMEOUT_MS = 30000;

  /** The default number of retries */
  public static final int DEFAULT_MAX_RETRIES = 1;

  /** The default backoff multiplier */
  public static final float DEFAULT_BACKOFF_MULT = 1f;

  public ApiRetryPolicy(int connectTimoutMs, int maxNumRetries,
      float backoffMultiplier) {
    super(connectTimoutMs, maxNumRetries, backoffMultiplier);
  }

  public ApiRetryPolicy() {
    super(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
  }
}
