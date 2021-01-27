package com.kindustry.common.util;

import java.util.ArrayList;
import java.util.List;

public class DataSplitUtil {

  /** 把字节分割 */
  public static List<byte[]> splitBytes(byte[] data, byte b) {
    List<byte[]> list = new ArrayList<byte[]>();
    int index = 0;
    List<Byte> temp = new ArrayList<Byte>();
    byte[] tb;
    while (index < data.length) {
      if (data[index] == b) {
        tb = new byte[temp.size()];
        int i = 0;
        for (byte t : temp) {
          tb[i++] = t;
        }
        list.add(tb);
      } else {
        temp.add(data[index]);
      }
      index++;
    }

    return list;
  }

  /** 把字节分割 */
  public static byte[] replace(byte[] data, byte target, byte replace) {
    for (int i = 0, j = data.length; i < j; i++) {
      if (data[i] == target) {
        data[i] = replace;
      }
    }
    return data;
  }
}
