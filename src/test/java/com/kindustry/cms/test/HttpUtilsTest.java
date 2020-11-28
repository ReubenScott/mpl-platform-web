package com.kindustry.cms.test;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.kindustry.network.utils.HttpUtils;

public class HttpUtilsTest {

  @Test
  public void testGetHtml() {
    HttpClient httpClient = new DefaultHttpClient();
    String url = "http://basic.10jqka.com.cn/122695/dividend.html";
    // String str = getHtml(httpClient,"http://www.pudongnews.com.cn/");
    String html = HttpUtils.getHtml(httpClient, url);
    httpClient.getConnectionManager().shutdown();

    // 设置文章的内容
    String content = StringUtils.substringBetween(html, "<!-- SUMMARY_BEGIN -->", "<!--CMA ID: 507487-->");

    getTableDate(url);
  }

  // 正确提取网页内容工具方法
  public static String getTableDate(String url) {
    try {
      // Document doc = Jsoup.connect(url).timeout(5000).get();
      Document doc = Jsoup.connect(url).get();

      Elements table = doc.select("table").eq(0); // 取第一张表格 债券现金流

      Elements trs = table.select("tr"); // 取行数据

      Elements ths = trs.get(0).select("th"); // 获取列名
      for (int j = 0; j < ths.size(); j++) {
        // 对每一行中的某些你需要的列进行处理
        // 获取第i行第j列的值
        String oldClose = ths.get(j).text();
        System.out.println(oldClose);
      }

      for (int i = 0; i < trs.size(); i++) {
        Elements tds = trs.get(i).select("td"); // 获取每一行的列数据
        for (int j = 0; j < tds.size(); j++) {
          // 对每一行中的某些你需要的列进行处理
          // 获取第i行第j列的值
          String oldClose = tds.get(j).text();
          System.out.println(oldClose);

        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

}
