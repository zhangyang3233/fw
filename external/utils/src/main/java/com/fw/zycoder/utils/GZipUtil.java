package com.fw.zycoder.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;


public class GZipUtil {
  private static final int BUFFER_SIZE = 1024;

  public static byte[] zipBytes(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    GZIPOutputStream gzout = null;
    try {
      bais = new ByteArrayInputStream(bytes);
      baos = new ByteArrayOutputStream();
      gzout = new GZIPOutputStream(baos);

      byte[] buffer = new byte[BUFFER_SIZE];
      int count;
      while (true) {
        count = bais.read(buffer, 0, BUFFER_SIZE);
        if (count == -1) {
          break;
        }
        gzout.write(buffer, 0, count);
      }
      gzout.close();
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      IOUtils.close(bais);
      IOUtils.close(baos);
      IOUtils.close(gzout);
    }
  }

  public static void zip(InputStream input, OutputStream output) {
    try {
      GZIPOutputStream gzout = new GZIPOutputStream(output);
      byte[] buffer = new byte[BUFFER_SIZE];

      while (true) {
        int readed = input.read(buffer);
        if (readed == -1) {
          break;
        }
        gzout.write(buffer, 0, readed);
      }
      gzout.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
