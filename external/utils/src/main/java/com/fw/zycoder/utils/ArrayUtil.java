package com.fw.zycoder.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

  /**
   * 将 List<Long> 转为数组 long[]
   * 
   * @param list
   * @return
   */
  public static long[] toLongArray(List<Long> list) {
    if (list == null) {
      return null;
    }
    long[] longs = new long[list.size()];
    for (int i = 0; i < list.size(); i++) {
      longs[i] = list.get(i);
    }
    return longs;
  }

  /**
   * 将 数组 long[] 转为List<Long>
   * 
   * @param longs
   * @return
   */
  public static List<Long> toList(long[] longs) {
    if (longs == null) {
      return null;
    }
    List<Long> list = new ArrayList<Long>();
    for (long l : longs) {
      list.add(l);
    }
    return list;
  }


  /**
   * 数组合并
   * 
   * @param arr1
   * @param arr2
   * @param <T>
   * @return
   */
  public static <T> T[] combineArray(T[] arr1, T[] arr2) {
    T[] elements = copyOfRange(arr1, 0, arr1.length + arr2.length);
    System.arraycopy(arr2, 0, elements, arr1.length, arr2.length);
    return elements;
  }

  /**
   * 数组插入
   * 
   * @param array
   * @param index
   * @param element
   * @param <T>
   * @return
   */
  public static <T> T[] insert(T[] array, int index, T element) {
    T[] ret = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
    for (int i = 0; i < ret.length; ++i) {
      ret[i] = i < index ? array[i] : (i == index ? element : array[i - 1]);
    }
    return ret;
  }


  /**
   * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
   * end (exclusive). The original order of elements is preserved.
   * If {@code end} is greater than {@code original.length}, the result is padded
   * with the value {@code null}.
   * 
   * @param original the original array
   * @param start the start index, inclusive
   * @param end the end index, exclusive
   * @return the new array
   * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
   * @throws IllegalArgumentException if {@code start > end}
   * @throws NullPointerException if {@code original == null}
   * @since 1.6
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] copyOfRange(T[] original, int start, int end) {
    int originalLength = original.length; // For exception priority compatibility.
    if (start > end) {
      throw new IllegalArgumentException();
    }
    if (start < 0 || start > originalLength) {
      throw new ArrayIndexOutOfBoundsException();
    }
    int resultLength = end - start;
    int copyLength = Math.min(resultLength, originalLength - start);
    T[] result = (T[]) Array.newInstance(original.getClass().getComponentType(), resultLength);
    System.arraycopy(original, start, result, 0, copyLength);
    return result;
  }

  /**
   * 判断是否包含元素
   * 
   * @param array
   * @param value
   * @return
   */
  public static boolean contains(int[] array, int value) {
    if (array == null) return false;
    for (int element : array) {
      if (element == value) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断一个值在数组的哪一位,如果在数组中没有找到改值,返回-1
   * 
   * @param array
   * @param value
   * @return
   */
  public static int getIndex(String[] array, String value) {
    int index = -1;
    if (array == null)
      return 0;
    for (int i = 0; i < array.length; i++) {
      if (value.equals(array[i])) {
        index = i;
      }
    }
    return index;
  }

}
