package debug.xly.com.debugkit.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by zhangyang131 on 2017/6/7.
 */

public class DeivceInfoUtil {
  public static final String CPU_ARCHITECTURE_TYPE_32 = "32";
  public static final String CPU_ARCHITECTURE_TYPE_64 = "64";

  /** ELF文件头 e_indent[]数组文件类标识索引 */
  private static final int EI_CLASS = 4;
  /** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目标 */
  private static final int ELFCLASS32 = 1;
  /** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目标 */
  private static final int ELFCLASS64 = 2;

  /** The system property key of CPU arch type */
  private static final String CPU_ARCHITECTURE_KEY_64 = "ro.product.cpu.abilist64";

  /** The system libc.so file path */
  private static final String SYSTEM_LIB_C_PATH = "/system/lib/libc.so";
  private static final String SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so";
  private static final String PROC_CPU_INFO_PATH = "/proc/cpuinfo";

  /**
   * 获取 Linux 信息
   * 
   * @return
   */
  public static String getLinuxKernalInfoEx() {
    String result = "";
    String line;
    String[] cmd = new String[] {"/system/bin/cat", "/proc/version"};
    String workdirectory = "/system/bin/";
    try {
      ProcessBuilder bulider = new ProcessBuilder(cmd);
      bulider.directory(new File(workdirectory));
      bulider.redirectErrorStream(true);
      Process process = bulider.start();
      InputStream in = process.getInputStream();
      InputStreamReader isrout = new InputStreamReader(in);
      BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

      while ((line = brout.readLine()) != null) {
        result += line;
        // result += "\n";
      }
      in.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取屏幕是多少英寸的
   * 
   * @param display
   * @param dm
   * @return
   */
  public static double getScreenSizeOfDevice2(Display display, DisplayMetrics dm) {
    Point point = new Point();
    display.getRealSize(point);
    double x = Math.pow(point.x / dm.xdpi, 2);
    double y = Math.pow(point.y / dm.ydpi, 2);
    double screenInches = Math.sqrt(x + y);
    return (double) Math.round(screenInches * 10) / 10;
  }

  /**
   * 获取CPU最大频率（单位KHZ）
   * 
   * @return
   */
  public static String getMaxCpuFreq() {
    String result = "";
    ProcessBuilder cmd;
    InputStream in = null;
    try {
      String[] args = {"/system/bin/cat",
          "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
      cmd = new ProcessBuilder(args);
      Process process = cmd.start();
       in = process.getInputStream();
      byte[] re = new byte[24];
      while (in.read(re) != -1) {
        result = result + new String(re);
      }
      in.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      result = "N/A";
    }finally {
      if(in != null){
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return result.trim();
  }

  public static String cpuFromat(String rateStr){
    String cpuRate = null;
    try{
      long rate = Long.parseLong(rateStr);
      int rate1 = (int) (rate/100000);
      cpuRate = String.valueOf(rate1/10.0)+"GHz";
    }catch (NumberFormatException e){

    }
    return cpuRate;
  }

  // 获取CPU最小频率（单位KHZ）
  public static String getMinCpuFreq() {
    String result = "";
    ProcessBuilder cmd;
    InputStream in = null;
    try {
      String[] args = {"/system/bin/cat",
          "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
      cmd = new ProcessBuilder(args);
      Process process = cmd.start();
       in = process.getInputStream();
      byte[] re = new byte[24];
      while (in.read(re) != -1) {
        result = result + new String(re);
      }
      in.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      result = "N/A";
    } finally {
      if(in != null){
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return result.trim();
  }

  // 实时获取CPU当前频率（单位KHZ）
  public static String getCurCpuFreq() {
    String result = "N/A";
    BufferedReader br = null;
    try {
      FileReader fr = new FileReader(
          "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
       br = new BufferedReader(fr);
      String text = br.readLine();
      result = text.trim();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if(br != null){
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  // 获取CPU名字
  public static String getCpuName() {
    try {
      FileReader fr = new FileReader("/proc/cpuinfo");
      BufferedReader br = new BufferedReader(fr);
      String text = br.readLine();
      String[] array = text.split(":\\s+", 2);
      for (int i = 0; i < array.length; i++) {}
      return array[1];
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  // CPU个数
  public static int getNumCores() {
    // Private Class to display only CPU devices in the directory listing
    class CpuFilter implements FileFilter {
      @Override
      public boolean accept(File pathname) {
        // Check if filename is "cpu", followed by a single digit number
        if (Pattern.matches("cpu[0-9]", pathname.getName())) {
          return true;
        }
        return false;
      }
    }

    try {
      // Get directory containing CPU info
      File dir = new File("/sys/devices/system/cpu/");
      // Filter to only list the devices we care about
      File[] files = dir.listFiles(new CpuFilter());
      // Return the number of cores (virtual CPU devices)
      return files.length;
    } catch (Exception e) {
      // Print exception
      e.printStackTrace();
      // Default to return 1 core
      return 1;
    }
  }


  /**
   * Check if the CPU architecture is x86
   */
  public static boolean checkIfCPUx86() {
    // 1. Check CPU architecture: arm or x86
    if (getSystemProperty("ro.product.cpu.abi", "arm").contains("x86")) {
      // The CPU is x86
      return true;
    } else {
      return false;
    }
  }

  private static String getSystemProperty(String key, String defaultValue) {
    String value = defaultValue;
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      Method get = clazz.getMethod("get", String.class, String.class);
      value = (String) (get.invoke(clazz, key, ""));
    } catch (Exception e) {
      L.d("getSystemProperty", "key = " + key + ", error = " + e.getMessage());
    }
    L.d("getSystemProperty", key + " = " + value);
    return value;
  }

  /**
   * Get the CPU arch type: x32 or x64
   */
  public static String getArchType(Context context) {
    if (getSystemProperty(CPU_ARCHITECTURE_KEY_64, "").length() > 0) {

      L.d("###############getSystemProperty", "CPU arch is 64bit");

      return CPU_ARCHITECTURE_TYPE_64;
    } else if (isCPUInfo64()) {
      return CPU_ARCHITECTURE_TYPE_64;
    } else if (isLibc64()) {
      return CPU_ARCHITECTURE_TYPE_64;
    } else {
      L.d("###############getArchType()", "return cpu DEFAULT 32bit!");
      return CPU_ARCHITECTURE_TYPE_32;
    }
  }

  /**
   * Read the first line of "/proc/cpuinfo" file, and check if it is 64 bit.
   */
  private static boolean isCPUInfo64() {
    File cpuInfo = new File(PROC_CPU_INFO_PATH);
    if (cpuInfo != null && cpuInfo.exists()) {
      InputStream inputStream = null;
      BufferedReader bufferedReader = null;
      try {
        inputStream = new FileInputStream(cpuInfo);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 512);
        String line = bufferedReader.readLine();
        if (line != null && line.length() > 0 && line.toLowerCase(Locale.US).contains("arch64")) {

          L.d("###############isCPUInfo64()", PROC_CPU_INFO_PATH + " contains is arch64");

          return true;
        } else {

          L.d("###############isCPUInfo64()", PROC_CPU_INFO_PATH + " is not arch64");

        }
      } catch (Throwable t) {

        L.d("###############isCPUInfo64()",
            "read " + PROC_CPU_INFO_PATH + " error = " + t.toString());

      } finally {
        try {
          if (bufferedReader != null) {
            bufferedReader.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          if (inputStream != null) {
            inputStream.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  /**
   * Check if system libc.so is 32 bit or 64 bit
   */
  private static boolean isLibc64() {
    File libcFile = new File(SYSTEM_LIB_C_PATH);
    if (libcFile != null && libcFile.exists()) {
      byte[] header = readELFHeadrIndentArray(libcFile);
      if (header != null && header[EI_CLASS] == ELFCLASS64) {
        L.d("###############isLibc64()", SYSTEM_LIB_C_PATH + " is 64bit");
        return true;
      }
    }

    File libcFile64 = new File(SYSTEM_LIB_C_PATH_64);
    if (libcFile64 != null && libcFile64.exists()) {
      byte[] header = readELFHeadrIndentArray(libcFile64);
      if (header != null && header[EI_CLASS] == ELFCLASS64) {
        L.d("###############isLibc64()", SYSTEM_LIB_C_PATH_64 + " is 64bit");
        return true;
      }
    }

    return false;
  }

  /**
   * ELF文件头格式是固定的:文件开始是一个16字节的byte数组e_indent[16]
   * e_indent[4]的值可以判断ELF是32位还是64位
   */
  private static byte[] readELFHeadrIndentArray(File libFile) {
    if (libFile != null && libFile.exists()) {
      FileInputStream inputStream = null;
      try {
        inputStream = new FileInputStream(libFile);
        if (inputStream != null) {
          byte[] tempBuffer = new byte[16];
          int count = inputStream.read(tempBuffer, 0, 16);
          if (count == 16) {
            return tempBuffer;
          } else {

            L.e("readELFHeadrIndentArray",
                "Error: e_indent lenght should be 16, but actual is " + count);

          }
        }
      } catch (Throwable t) {

        L.e("readELFHeadrIndentArray", "Error:" + t.toString());

      } finally {
        if (inputStream != null) {
          try {
            inputStream.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

    return null;
  }
}
