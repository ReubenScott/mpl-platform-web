package com.kindustry.framework.sql.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.IOUtils;

/**
 * 读取SQL文件
 * */
public class SQLReader {

  private static Log log = LogFactory.getLog(SQLReader.class);

  private File file;

  private byte[] content;

  private String encode = "gbk";

  /**
   * 构造Reader
   * */
  public SQLReader(String path) {
    file = new File(path);
    if (!file.exists()) {
      throw new RuntimeException("File <" + file.getAbsolutePath() + "> not exist");
    }
    if (file.isDirectory()) {
      throw new RuntimeException("File <" + file.getAbsolutePath() + "> is a directory");
    }

    log.debug("file init finished");
  }

  /** 加载 */
  public void load() {
    BufferedInputStream inputStream = null ;
    try {
      inputStream = new BufferedInputStream(new FileInputStream(file));
      content = IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    log.debug("file load success");
  }

  /** 得到SQL内容 */
  public String getSQLContent() {
    return getSQLContent(encode);
  }

  /** 得到SQL内容 */
  public String getSQLContent(String encode) {

    try {
      return new String(content, encode);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public byte[] getData() {
    return content;
  }
}
