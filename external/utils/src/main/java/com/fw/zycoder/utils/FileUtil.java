package com.fw.zycoder.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtil {

  public static final int MAX_ERROR_FILES = 5;
  private final static String PATH_PLACEHOLDER_EXTERNAL = "%EXTERNAL%";
  private final static String PATH_PLACEHOLDER_HOME = "%HOME%";
  private final static String PATH_PREFIX_EXTERNAL = "/mnt/sdcard";
  private final static String PATH_PREFIX_EXTERNAL_SDCARD = "/sdcard";
  private static final int BUFFER_SIZE = 2048;

  public static void deleteFile(String path) {
    if (TextUtils.isEmpty(path)) {
      return;
    }
    File file = new File(path);
    if (!file.exists() || !file.isFile()) {
      return;
    }
    file.delete();
  }

  public static boolean exists(String path) {
    if (TextUtils.isEmpty(path)) {
      return false;
    }
    File file = new File(path);
    return file.exists();
  }

  public static boolean mkdir(String path) {
    File dir = new File(path);
    return dir.mkdirs();
  }

  public static boolean copyFile(File srcFile, File dstFile) {
    if (srcFile.exists() && srcFile.isFile()) {
      if (dstFile.isDirectory()) {
        return false;
      }
      if (dstFile.exists()) {
        dstFile.delete();
      }
      FileInputStream fi = null;
      FileOutputStream fo = null;
      FileChannel in = null;
      FileChannel out = null;

      try {
        fi = new FileInputStream(srcFile);
        fo = new FileOutputStream(dstFile);
        in = fi.getChannel();
        out = fo.getChannel();
        in.transferTo(0, in.size(), out);
        return true;
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (fi != null) {
            fi.close();
          }
          if (in != null) {
            in.close();
          }
          if (fo != null) {
            fo.close();
          }
          if (out != null) {
            out.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  /**
   * @return -1 if error or over max file num , return error copy num
   */
  public static int copyDirectory(File sourceDir, File targetDir) {
    int errorFileNum = 0;
    if (!targetDir.exists()) {
      if (!targetDir.mkdirs()) {
        return -1;
      }
    }
    File[] file = sourceDir.listFiles();
    if (file == null) {
      return errorFileNum;
    }
    for (int i = 0; i < file.length; i++) {
      if (file[i].isFile()) {
        File sourceFile = file[i];
        File targetFile = new File(targetDir, file[i].getName());
        boolean result = copyFile(sourceFile, targetFile);
        if (!result) {
          errorFileNum++;
          if (errorFileNum > MAX_ERROR_FILES) {
            return -1;
          }
        }
      }
      if (file[i].isDirectory()) {
        int error = copyDirectory(file[i],
            new File(targetDir, file[i].getName()));
        if (error < 0) {
          return error;
        } else {
          errorFileNum += error;
          if (errorFileNum > MAX_ERROR_FILES) {
            return -1;
          }
        }
      }
    }
    return errorFileNum;
  }

  /**
   * @return -1 if error or over max file num , return error copy num
   */
  public static int copyAllFiles(String sourceDir, String targetDir) {
    File sourceDF = new File(sourceDir);
    int errorFileNum = 0;
    if (!sourceDF.exists()) {
      return -1;
    }
    File targetDF = new File(targetDir);
    if (!targetDF.exists()) {
      return -1;
    }
    File[] file = sourceDF.listFiles();
    for (int i = 0; i < file.length; i++) {
      if (file[i].isFile()) {
        boolean result = copyFile(file[i], new File(targetDir, file[i].getName()));
        if (!result) {
          errorFileNum++;
          if (errorFileNum > MAX_ERROR_FILES) {
            return -1;
          }
        }
      }

      if (file[i].isDirectory()) {
        int error = copyDirectory(file[i],
            new File(targetDir, file[i].getName()));
        if (error < 0) {
          return error;
        } else {
          errorFileNum += error;
          if (errorFileNum > MAX_ERROR_FILES) {
            return -1;
          }
        }
      }
    }
    return errorFileNum;
  }

  public static boolean copyFile(String srcPath, String dstPath) {
    return copyFile(new File(srcPath), new File(dstPath));
  }

  public static String readFileFirstLine(String filePath) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      reader.close();
      return line;
    } catch (Exception e) {
      return "";
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void deletePath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      file.delete();
      return;
    }
    String[] tmpList = file.list();
    if (tmpList == null) {
      return;
    }
    for (String fileName : tmpList) {
      if (fileName == null) {
        continue;
      }
      String tmpPath = null;
      if (path.endsWith(File.separator)) {
        tmpPath = path + fileName;
      } else {
        tmpPath = path + File.separator + fileName;
      }
      File tmpFile = new File(tmpPath);
      if (tmpFile.isFile()) {
        tmpFile.delete();
      }
      if (tmpFile.isDirectory()) {
        deletePath(tmpPath);
      }
    }
    file.delete();
  }

  public static void clearPath(String path) {
    File file = new File(path);
    if (!file.exists() || !file.isDirectory()) {
      return;
    }
    String[] tmpList = file.list();
    for (String fileName : tmpList) {
      String tmpPath = null;
      if (path.endsWith(File.separator)) {
        tmpPath = path + fileName;
      } else {
        tmpPath = path + File.separator + fileName;
      }
      File tmpFile = new File(tmpPath);
      if (tmpFile.isFile()) {
        tmpFile.delete();
      }
      if (tmpFile.isDirectory()) {
        deletePath(tmpPath);
      }
    }
  }

  public static long getFileSize(String path) {
    if (TextUtils.isEmpty(path)) {
      return 0;
    }
    File file = new File(path);
    if (file == null || !file.exists()) {
      return 0;
    }
    long size = file.length();
    if (file.isDirectory()) {
      File[] childList = file.listFiles();
      if (childList != null) {
        for (File childFile : childList) {
          try {
            size += getFileSize(childFile.getAbsolutePath());
          } catch (StackOverflowError e) {
            // too many recursion may cause stack over flow
            e.printStackTrace();
            return 0;
          } catch (OutOfMemoryError e2) {
            // too many call filenamesToFiles method
            e2.printStackTrace();
            return 0;
          }
        }
      }
    }
    return size;
  }

  /**
   * This method is used to transform %EXTERNAL% to external SD path
   * especially large files. This method should be used for video, music,
   * image, app related methods.
   *
   * @param path
   * @return
   */
  public static String formatExternalPath(String path) {
    String formatedPath = formatExternalPath(path,
        Environment.getExternalStorageDirectory().getAbsolutePath());
    if (!FileUtil.exists(formatedPath)) {
      return path;
    } else {
      return formatedPath;
    }
  }

  private static String formatExternalPath(String path, String replaced) {
    if (!TextUtils.isEmpty(path)) {
      if (path.startsWith(PATH_PLACEHOLDER_EXTERNAL)) {
        return path.replace(PATH_PLACEHOLDER_EXTERNAL, replaced);
      } else if (path.startsWith(PATH_PREFIX_EXTERNAL)) {
        return path.replace(PATH_PREFIX_EXTERNAL, replaced);
      } else if (path.startsWith(PATH_PREFIX_EXTERNAL_SDCARD)) {
        return path.replace(PATH_PREFIX_EXTERNAL_SDCARD, replaced);
      } else {
        return PathAdjustUtil.adjustSdcardPathForApp(path);
      }
    }
    return path;
  }

  /**
   * This method is used for formatting path using '%HOME%', '%EXTERNAL'.
   *
   * @param context
   * @param path
   * @return
   */
  public static String formatPath(Context context, String path) {
    if (path.startsWith(PATH_PLACEHOLDER_HOME)) {
      return formatHomePath(context, path);
    } else {
      return formatExternalPath(path);
    }
  }

  public static boolean isExternalPath(String path) {
    if (path.startsWith(PATH_PLACEHOLDER_HOME)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * This method is used for transform '%HOME%' to available paths whether sd
   * card is mounted or not. This method should be only used for small files
   * from import/export, backup/restore.
   *
   * @param context
   * @param path
   * @return
   */
  public static String formatHomePath(Context context, String path) {
    if (path.startsWith(PATH_PLACEHOLDER_HOME)) {
      if (Environment.getExternalStorageState().equals(
          Environment.MEDIA_MOUNTED)) {
        return path.replace(PATH_PLACEHOLDER_HOME, Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/"
            + GlobalConfig.getAppRootDir() + "/");

      } else {
        return path.replace(PATH_PLACEHOLDER_HOME, context
            .getFilesDir().getAbsolutePath());
      }
    }
    return path;
  }

  public static boolean isExternalFile(Context context, String path) {
    if (!TextUtils.isEmpty(path)) {
      return path.startsWith(Environment.getExternalStorageDirectory()
          .getAbsolutePath());
    } else {
      return false;
    }
  }

  public static String getFileName(String path) {
    if (path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    int lastIndex = path.lastIndexOf("/");
    if (lastIndex > 0 && lastIndex < path.length() - 1) {
      return path.substring(lastIndex + 1);
    } else {
      return path;
    }
  }

  public static String getParentFilePath(String filePath) {
    if (filePath.endsWith("/")) {
      filePath = filePath.substring(0, filePath.length() - 1);
    }
    int lastSplitIndex = filePath.lastIndexOf("/");
    if (lastSplitIndex >= 0) {
      return filePath.substring(0, lastSplitIndex);
    }
    return null;
  }

  public static String getFileNameWithoutExtension(String path) {
    String filename = getFileName(path);
    if (filename != null && filename.length() > 0) {
      int dotPos = filename.lastIndexOf('.');
      if (0 < dotPos) {
        return filename.substring(0, dotPos);
      }
    }
    return filename;
  }

  public static String getFileExtension(String path) {
    int index = path.lastIndexOf(".");
    if (index >= 0 && index < path.length() - 1) {
      return path.substring(index + 1).toUpperCase();
    }
    return "";
  }

  public static boolean renameFile(String originPath, String destPath) {
    File origin = new File(originPath);
    File dest = new File(destPath);
    if (!origin.exists()) {
      return false;
    }
    return origin.renameTo(dest);
  }

  public static long getUsedBytes(String root) {
    try {
      if (!TextUtils.isEmpty(root) && new File(root).exists()) {
        StatFs stat = new StatFs(root);
        long availableBlocks = (long) stat.getAvailableBlocks();
        long blocks = (long) stat.getBlockCount();
        return stat.getBlockSize() * (blocks - availableBlocks);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * <b>return the available size of filesystem<b/>
   *
   * @return the number of bytes available on the filesystem rooted at the
   *         given File
   */
  public static long getAvailableBytes(String root) {
    try {
      if (!TextUtils.isEmpty(root) && new File(root).exists()) {
        StatFs stat = new StatFs(root);
        long availableBlocks = (long) stat.getAvailableBlocks();
        return stat.getBlockSize() * availableBlocks;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * the size of all bytes of current filesystem where root path inside.<br>
   * <b>return the size of filesystem<b/>
   *
   * @param root
   * @return all bytes.
   */
  public static long getAllBytes(String root) {
    if (TextUtils.isEmpty(root)) {
      return 0L;
    }
    try {
      if (!TextUtils.isEmpty(root) && new File(root).exists()) {
        StatFs stat = new StatFs(root);
        long blocks = (long) stat.getBlockCount();
        return stat.getBlockSize() * blocks;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0L;
  }

  /**
   * Check If the storage is able to download.
   *
   * @param file
   * @return able to write
   */
  public static boolean canWrite(File file) {
    if (file == null) {
      return false;
    }

    if (!file.exists()) {
      file.mkdirs();
    }

    String testName = "." + System.currentTimeMillis();
    File testFile = new File(file, testName);

    boolean result = testFile.mkdir();
    if (result) {
      result = testFile.delete();
    }
    return result;
  }

  public static boolean canWrite(String path) {
    return canWrite(new File(path));
  }


  public static boolean copyFile(InputStream ins, File outputFile) {
    if (outputFile == null) {
      return false;
    }

    String path = outputFile.getAbsolutePath();
    if (outputFile.exists()) {
      outputFile.delete();
    }

    OutputStream ous = null;
    try {
      ous = new FileOutputStream(path);

      return copyFile(ins, ous);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } finally {
      close(ous);
      close(ins);
    }
  }

  public static boolean copyFile(InputStream ins, OutputStream outs) {
    if (ins == null || outs == null) {
      return false;
    }

    int count = 0;
    byte[] buffer = new byte[BUFFER_SIZE];

    try {
      while ((count = ins.read(buffer)) > 0) {
        outs.write(buffer, 0, count);
      }
      outs.flush();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * @param context
   * @param assetPath
   * @return
   */
  public static String[] getAssetsFile(Context context, String assetPath) {
    if (context == null || TextUtils.isEmpty(assetPath)) {
      return null;
    }

    try {
      return context.getResources().getAssets().list(assetPath);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] readFully(InputStream is) {
    if (is == null) {
      return null;
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024 * 4];
    try {
      int count = 0;
      while ((count = is.read(buffer)) != -1) {
        bos.write(buffer, 0, count);
      }
    } catch (IOException e) {}
    return bos.toByteArray();
  }

  private static void close(Closeable closeable) {
    if (closeable == null)
      return;
    try {
      closeable.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void deleteFilesByDirectory(File directory) {
    if (directory == null || !directory.exists()) {
      return;
    }

    if (directory.isFile()) {
      directory.delete();
      return;
    }

    if (directory.isDirectory()) {
      for (File item : directory.listFiles()) {
        if (item.isFile()) {
          item.delete();
        }
        if (item.isDirectory()) {
          deleteFilesByDirectory(item);
        }
      }
    }

    directory.delete();
  }

  // 将bytes数组写到一个路径下。
  public static boolean writeFileFromBytes(byte[] fileBytes, String path) {
    if (fileBytes == null) {
      return false;
    }
    boolean result = true;
    try {
      File file = new File(path);
      file.setWritable(true, false);
      FileOutputStream fs = new FileOutputStream(file);
      fs.write(fileBytes);
      fs.flush();
      fs.close();
    } catch (IOException e) {
      result = false;
    }
    return result;
  }

  public static byte[] readFileToBytes(InputStream is) throws IOException {
    byte[] buffer;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int n;
    while ((n = is.read(b)) != -1) {
      bos.write(b, 0, n);
    }
    is.close();
    bos.flush();
    bos.close();
    buffer = bos.toByteArray();
    return buffer;
  }

  // 将一个uri转换成path
  public static String getRealFilePath(final Context context, final Uri uri) {
    if (null == uri) return null;
    final String scheme = uri.getScheme();
    String data = null;
    if (scheme == null)
      data = uri.getPath();
    else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      data = uri.getPath();
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
      Cursor cursor =
          context.getContentResolver().query(uri,
              new String[] {MediaStore.Images.ImageColumns.DATA}, null, null, null);
      if (null != cursor) {
        if (cursor.moveToFirst()) {
          int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
          if (index > -1) {
            data = cursor.getString(index);
          }
        }
        cursor.close();
      }
    }
    return data;
  }
}
