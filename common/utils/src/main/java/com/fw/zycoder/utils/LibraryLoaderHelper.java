package com.fw.zycoder.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The class provides helper functions to extract native libraries from APK,
 * and load libraries from there.
 * <p/>
 * The class should be package-visible only, but made public for testing purpose.
 * 
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class LibraryLoaderHelper {
  private static final String TAG = "LibraryLoaderHelper";

  private static final String LIB_DIR = "lib_so";

  /**
   * avoid load one lib more than once
   */
  private static List<String> loadedLibNames = new ArrayList<String>();

  /**
   * Try to load a native library
   * 
   * @return true :library loaded
   *         false : library not load
   */
  public static synchronized boolean loadLibrarySafety(Context context, String library) {
    if (loadedLibNames.contains(library)) {
      return true;
    }

    final File libFile = getWorkaroundLibFile(context, library);
    try {
      // normal load lib
      System.loadLibrary(library);
      loadedLibNames.add(library);
      return true;
    } catch (UnsatisfiedLinkError e) {
      Log.e(TAG, "loadLibrarySafety normal way error ", e);
      // load from apk file
      if (!libFile.exists() && !unpackLibrariesOnce(context, library)) {
        Log.d(TAG, "loadLibrarySafety unpackLibrariesOnce fail ");
        throw e;
      }
      try {
        System.load(libFile.getAbsolutePath());
        loadedLibNames.add(library);
        return true;
      } catch (UnsatisfiedLinkError e2) {
        Log.e(TAG, "loadLibrarySafety in apk fail ", e2);
        throw e2;
      }
    }
  }

  /**
   * Returns the directory for holding extracted native libraries.
   * It may create the directory if it doesn't exist.
   * 
   * @param context
   * @return the directory file object
   */
  private static File getWorkaroundLibDir(Context context) {
    File file = new File(context.getFilesDir(), LIB_DIR);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  private static File getWorkaroundLibFile(Context context, String library) {
    String libName = System.mapLibraryName(library);
    return new File(getWorkaroundLibDir(context), libName);
  }

  /**
   * Unpack native libraries from the APK file. The method is supposed to
   * be called only once. It deletes existing files in unpacked directory
   * before unpacking.
   * 
   * @param context
   * @return true when unpacking was successful, false when failed or called
   *         more than once.
   */
  private static boolean unpackLibrariesOnce(Context context, String libName) {
    File libDir = getWorkaroundLibDir(context);
    deleteDirectorySync(libDir);
    ZipFile file = null;
    InputStream is = null;
    FileOutputStream os = null;

    try {
      ApplicationInfo appInfo = context.getApplicationInfo();
      file = new ZipFile(new File(appInfo.sourceDir), ZipFile.OPEN_READ);
      String jniNameInApk = "lib/" + Build.CPU_ABI + "/" +
          System.mapLibraryName(libName);

      ZipEntry entry = file.getEntry(jniNameInApk);
      if (entry == null) {
        Log.e(TAG, appInfo.sourceDir + " doesn't have file " + jniNameInApk);
        final int lineCharIndex = Build.CPU_ABI.indexOf('-');
        jniNameInApk =
            "lib/"
                + Build.CPU_ABI.substring(0,
                    lineCharIndex > 0 ? lineCharIndex : Build.CPU_ABI.length()) + "/"
                + System.mapLibraryName(libName);

        entry = file.getEntry(jniNameInApk);
        if (entry == null) {
          Log.e(TAG, appInfo.sourceDir + " doesn't have file " + jniNameInApk);
          file.close();
          deleteDirectorySync(libDir);
          return false;
        }
      }

      File outputFile = getWorkaroundLibFile(context, libName);
      Log.i(TAG, "Extracting native libraries into " + outputFile.getAbsolutePath());

      if (!outputFile.createNewFile()) {
        return false;
      }

      is = file.getInputStream(entry);
      os = new FileOutputStream(outputFile);
      int count = 0;
      byte[] buffer = new byte[16 * 1024];
      while ((count = is.read(buffer)) > 0) {
        os.write(buffer, 0, count);
      }
      // Change permission to rwxr-xr-x
//      FileUtil.setPermissions(outputFile.getAbsolutePath(), 0755);
      return true;
    } catch (IOException e) {
      Log.e(TAG, "Failed to unpack native libraries", e);
      deleteDirectorySync(libDir);
      return false;
    } catch (NoSuchMethodError e2) {
      e2.printStackTrace();
      return false;
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (is != null) {
          is.close();
        }
        if (file != null) {
          file.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Delete old library files in the backup directory.
   * The actual deletion is done in a background thread.
   * 
   * @param context
   */
  // private static void deleteWorkaroundLibrariesAsynchronously(final Context context) {
  // // Child process should not reach here.
  // new Thread() {
  // @Override
  // public void run() {
  // deleteWorkaroundLibrariesSynchronously(context);
  // }
  // }.start();
  // }

  /**
   * Delete the workaround libraries and directory synchronously.
   * For testing purpose only.
   * 
   * @param dir
   */
  // private static void deleteWorkaroundLibrariesSynchronously(Context context) {
  // File libDir = getWorkaroundLibDir(context);
  // deleteDirectorySync(libDir);
  // }
  private static void deleteDirectorySync(File dir) {
    try {
      File[] files = dir.listFiles();
      if (files != null) {
        for (File file : files) {
          String fileName = file.getName();
          if (!file.delete()) {
            Log.e(TAG, "Failed to remove " + file.getAbsolutePath());
          }
        }
      }
      if (!dir.delete()) {
        Log.w(TAG, "Failed to remove " + dir.getAbsolutePath());
      }
      return;
    } catch (Exception e) {
      Log.e(TAG, "Failed to remove old libs, ", e);
    }
  }
}
