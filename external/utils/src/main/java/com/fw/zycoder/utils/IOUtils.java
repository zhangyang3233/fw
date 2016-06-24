package com.fw.zycoder.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Utility class to handle I/O operations.
 * 
 */
public class IOUtils {

  public static final String DEFAULT_ENCODING = "utf-8";
  private static final int BUFFER_SIZE = 8 * 1024;

  private IOUtils() {}

  /**
   * Read the Stream content as a string (use utf-8).
   * 
   * @param is The stream to read
   * @return The String content
   * @throws IOException IOException
   */
  public static String readString(InputStream is) throws IOException {
    return readString(is, DEFAULT_ENCODING);
  }

  /**
   * Read the Stream content as a string.
   *
   * @param is The stream to read
   * @return The String content
   * @throws IOException IOException
   */
  public static String readString(InputStream is, String encoding) throws IOException {
    StringWriter sw = new StringWriter();
    try {
      copy(is, sw, encoding);
      return sw.toString();
    } finally {
      close(is);
      close(sw);
    }
  }

  private static void copy(InputStream input, Writer output, String encoding)
      throws IOException {
    InputStreamReader in =
        new InputStreamReader(input, encoding == null ? DEFAULT_ENCODING : encoding);
    char[] buffer = new char[BUFFER_SIZE];
    int n = 0;
    while (-1 != (n = in.read(buffer))) {
      output.write(buffer, 0, n);
    }
  }

  /**
   * Read file content to a String (always use utf-8).
   *
   * @param file The file to read
   * @return The String content
   * @throws IOException IOException
   */
  public static String readString(File file) throws IOException {
    return readString(file, DEFAULT_ENCODING);
  }

  /**
   * Read file content to a String.
   *
   * @param file The file to read
   * @return The String content
   * @throws IOException IOException
   */
  public static String readString(File file, String encoding) throws IOException {
    return readString(new FileInputStream(file), encoding);
  }

  /**
   * Read binary content of a file.
   * <p>
   * <b>Warning: Do not use on large file !</b>
   * </p>
   *
   * @param file The file te read
   * @return The binary data
   * @throws IOException IOException
   */
  public static byte[] readBytes(File file) throws IOException {
    InputStream is = null;
    try {
      is = new FileInputStream(file);
      byte[] result = new byte[(int) file.length()];
      is.read(result);
      return result;
    } finally {
      close(is);
    }
  }

  /**
   * Read binary content of a stream.
   * <p>
   * <b>Warning: Do not use on large stream !</b>
   * </p>
   *
   * @param is The stream to read
   * @return The binary data
   * @throws IOException IOException
   */
  public static byte[] readBytes(InputStream is) throws IOException {
    ByteArrayOutputStream baos = null;
    try {
      baos = new ByteArrayOutputStream();
      int read = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
      while ((read = is.read(buffer)) > 0) {
        baos.write(buffer, 0, read);
      }
      return baos.toByteArray();
    } finally {
      close(is);
      close(baos);
    }
  }

  /**
   * Write String content to a stream (always use utf-8).
   *
   * @param content The content to read
   * @param os The stream to write
   * @throws IOException IOException
   */
  public static void writeString(String content, OutputStream os) throws IOException {
    writeString(content, os, DEFAULT_ENCODING);
  }

  /**
   * Write String content to a stream (always use utf-8).
   *
   * @param content The content to read
   * @param os The stream to write
   * @throws IOException IOException
   */
  public static void writeString(String content, OutputStream os, String encoding)
      throws IOException {
    try {
      PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(os, encoding));
      printWriter.write(content);
      printWriter.flush();
      os.flush();
    } finally {
      close(os);
    }
  }

  /**
   * Write stream to another stream.
   *
   * @param is The stream to read
   * @param os The stream to write
   * @throws IOException IOException
   */
  public static void write(InputStream is, OutputStream os) throws IOException {
    write(is, true, os, true);
  }

  /**
   * Write stream to another stream.
   *
   * @param is The stream to read
   * @param closeInputStream
   * @param os The stream to write
   * @param closeOutputStream
   * @throws IOException IOException
   */
  public static void write(InputStream is, boolean closeInputStream, OutputStream os,
      boolean closeOutputStream) throws IOException {
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int count;
      while ((count = is.read(buffer)) != -1) {
        os.write(buffer, 0, count);
      }
      os.flush();
    } finally {
      if (closeInputStream) {
        close(is);
      }
      if (closeOutputStream) {
        close(os);
      }
    }
  }

  /**
   * Write stream to a file.
   *
   * @param is The stream to read
   * @param file The file to write
   * @throws IOException
   */
  public static void write(InputStream is, File file) throws IOException {
    OutputStream os = null;
    try {
      os = new FileOutputStream(file);
      byte[] buffer = new byte[BUFFER_SIZE];
      int count;
      while ((count = is.read(buffer)) != -1) {
        os.write(buffer, 0, count);
      }
      os.flush();
    } finally {
      close(is);
      close(os);
    }
  }

  /**
   * Write binay data to a file.
   *
   * @param data The binary data to write
   * @param file The file to write
   * @throws IOException
   */
  public static void write(byte[] data, File file) throws IOException {
    write(data, file, false);
  }

  /**
   * Write binay data to a file.
   *
   * @param data The binary data to write
   * @param file The file to write
   * @throws IOException
   */
  public static void write(byte[] data, File file, boolean append) throws IOException {
    OutputStream os = null;
    try {
      os = new FileOutputStream(file, append);
      os.write(data);
      os.flush();
    } finally {
      close(os);
    }
  }

  /**
   * Close stream.
   * 
   * @param is The stream to close
   */
  public static void close(Closeable is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
