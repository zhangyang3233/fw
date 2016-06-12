package com.fw.zycoder.utils;


import android.text.TextUtils;

/**
 *
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class FileNameUtil {

  private static final String ILLEGAL_REGX =
      "[`~!@#$%^&*()+=|{}':;',\\[\\]\\ <>/?~！@#￥%……&*（）/——+|{}【】‘；：”“’。，、？]";

  /**
   * The extension separator character.
   */
  private static final char EXTENSION_SEPARATOR = '.';

  /**
   * The linux separator character.
   */
  private static final char LINUX_SEPARATOR = '/';

  /**
   * format file name to avoid exception when writing data.
   *
   * @param fileName
   * @return formated string.
   */
  public static String removeIllegalChars(String fileName) {
    if (TextUtils.isEmpty(fileName)) {
      return null;
    }
    return fileName.replaceAll(ILLEGAL_REGX, "_").trim();
  }

  /**
   * Gets the base name, minus the full path and extension, from a full filename.
   * <p>
   * This method will handle a file in either Unix or Windows format. The text after the last
   * forward or backslash and before the last dot is returned.
   * 
   * <pre>
   * a/b/c.txt --> c
   * a.txt     --> a
   * a/b/c     --> c
   * a/b/c/    --> ""
   * </pre>
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to query, null returns null
   * @return the name of the file without the path, or an empty string if none exists
   */
  public static String getBaseName(String filename) {
    return removeExtension(getFileName(filename));
  }

  /**
   * Removes the extension from a filename.
   * <p>
   * This method returns the textual part of the filename before the last dot. There must be no
   * directory separator after the dot.
   * 
   * <pre>
   * foo.txt    --> foo
   * a\b\c.jpg  --> a\b\c
   * a\b\c      --> a\b\c
   * a.b\c      --> a.b\c
   * </pre>
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to query, null returns null
   * @return the filename minus the extension
   */
  public static String removeExtension(String filename) {
    if (filename == null) {
      return null;
    }
    int index = indexOfExtension(filename);
    if (index == -1) {
      return filename;
    } else {
      return filename.substring(0, index);
    }
  }

  /**
   * Returns the index of the last directory separator character.
   * <p>
   * This method will handle a file in *unix format. The position of the last forward or backslash
   * is returned.
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to find the last path separator in, null returns -1
   * @return the index of the last separator character, or -1 if there
   *         is no such character
   */
  private static int indexOfLastSeparator(String filename) {
    if (TextUtils.isEmpty(filename)) {
      return -1;
    }
    return filename.lastIndexOf(LINUX_SEPARATOR);
  }

  /**
   * Gets the name minus the path from a full filename.
   * <p>
   * This method will handle a file in either Unix or Windows format. The text after the last
   * forward or backslash is returned.
   * 
   * <pre>
   * a/b/c.txt --> c.txt
   * a.txt     --> a.txt
   * a/b/c     --> c
   * a/b/c/    --> ""
   * </pre>
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to query, null returns null
   * @return the name of the file without the path, or an empty string if none exists
   */
  public static String getFileName(String filename) {
    if (TextUtils.isEmpty(filename)) {
      return null;
    }
    int index = indexOfLastSeparator(filename);
    return filename.substring(index + 1);
  }

  /**
   * Get the first separator position in string.
   * 
   * @param str
   * @param separator
   * @return position
   */
  public static int getFirstSeparatorPos(String str, char separator) {
    if (TextUtils.isEmpty(str)) {
      return -1;
    }
    return str.indexOf(separator);
  }

  /**
   * Gets the extension of a filename.
   * <p>
   * This method returns the textual part of the filename after the last dot. There must be no
   * directory separator after the dot.
   * 
   * <pre>
   * foo.txt      --> "txt"
   * a/b/c.jpg    --> "jpg"
   * a/b.txt/c    --> ""
   * a/b/c        --> ""
   * </pre>
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to retrieve the extension of.
   * @return the extension of the file or an empty string if none exists.
   */
  public static String getExtension(String filename) {
    if (filename == null) {
      return null;
    }
    int index = indexOfExtension(filename);
    if (index == -1) {
      return "";
    } else {
      return filename.substring(index + 1);
    }
  }

  /**
   * Returns the index of the last extension separator character, which is a dot.
   * <p>
   * This method also checks that there is no directory separator after the last dot. To do this it
   * uses {@link #indexOfLastSeparator(String)} which will handle a file in Unix format.
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to find the last path separator in, null returns -1
   * @return the index of the last separator character, or -1 if there
   *         is no such character
   */
  public static int indexOfExtension(String filename) {
    if (filename == null) {
      return -1;
    }
    int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
    int lastSeparator = indexOfLastSeparator(filename);
    return (lastSeparator > extensionPos ? -1 : extensionPos);
  }

  /**
   * Gets the full path from a full filename, which is the prefix + path,
   * and also excluding the final directory separator.
   * <p>
   * This method will handle a file in either Unix or Windows format. The method is entirely text
   * based, and returns the text before the last forward or backslash.
   * <p>
   * The output will be the same irrespective of the machine that the code is running on.
   * 
   * @param filename the filename to query, null returns null
   * @return the path of the file, an empty string if none exists, null if invalid
   */
  public static String getFullPath(String filename) {
    if (TextUtils.isEmpty(filename)) {
      return null;
    }
    int index = indexOfLastSeparator(filename);
    return filename.substring(0, index + 1);
  }

}
