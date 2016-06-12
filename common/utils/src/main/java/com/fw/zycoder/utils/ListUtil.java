package com.fw.zycoder.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class ListUtil<E> {

  static void subListRangeCheck(int fromIndex, int toIndex, int size) {
    if (fromIndex < 0)
      throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
    if (toIndex > size)
      throw new IndexOutOfBoundsException("toIndex = " + toIndex);
    if (fromIndex > toIndex)
      throw new IllegalArgumentException("fromIndex(" + fromIndex +
          ") > toIndex(" + toIndex + ")");
  }

  /**
   * use this function to copy sublist from parent
   */
  public List<E> subList(List<E> parent, int fromIndex, int toIndex) {
    if (parent == null) {
      throw new IllegalArgumentException("src list is null");
    }
    subListRangeCheck(fromIndex, toIndex, parent.size());

    List<E> subList = new ArrayList<E>();
    for (int i = fromIndex; i < toIndex; i++) {
      subList.add(parent.get(i));
    }
    return subList;
  }
}
