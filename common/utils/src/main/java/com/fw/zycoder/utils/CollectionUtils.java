package com.fw.zycoder.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Usage: Util for collections.
 */
public class CollectionUtils {

  /**
   * replace all elements from start with elements in source.
   * Some adapter use this method to append newPart to originData from position.
   */
  public static <T> List<T> replaceFromPosition(List<T> origin, List<T> newPart, int pos) {
    if (origin == null || origin.isEmpty()) {
      return newPart;
    }
    if (newPart == null || newPart.isEmpty()) {
      return origin;
    }
    if (pos > origin.size()) {// append to end of list
      pos = origin.size();
    }
    List result = new ArrayList(origin);// avoid operation about origin list
    int newSize = pos + newPart.size();
    result.addAll(pos, newPart);
    while (result.size() > newSize) {
      result.remove(result.size() - 1); // remove last until size is right.
    }
    return result;
  }

  /**
   * append the newPart to the origin from pos.
   */
  public static <T> List<T> appendFromPosition(List<T> origin, List<T> newPart, int pos) {
    if (origin == null || origin.isEmpty()) {
      List<T> result = new ArrayList<T>();
      if (newPart != null) {
        result.addAll(newPart); // clone
      }
      return result;
    }
    if (newPart == null || newPart.isEmpty()) {
      return origin;
    }
    if (pos > origin.size()) {// append to end of list
      pos = origin.size();
    }
    List<T> result = new ArrayList<T>(origin);// avoid operation about origin list
    result.addAll(pos, newPart);
    return result;
  }

  public static <T> boolean isEmpty(Collection<T> list) {
    return list == null || list.isEmpty();
  }

  public static <K, V> boolean isEmpty(Map<K, V> map) {
    return map == null || isEmpty(map.keySet());
  }
}
