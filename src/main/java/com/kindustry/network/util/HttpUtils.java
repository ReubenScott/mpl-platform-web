package com.kindustry.network.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Parser;
import org.htmlparser.tags.MetaTag;


public class HttpUtils {

  // 正确提取网页内容工具方法
  public static String getHtml(HttpClient httpclient, String url) {
    try {

      // 利用HTTP GET向服务器发起请求
      HttpGet get = new HttpGet(url);

      // 获得服务器响应的的所有信息
      HttpResponse response = httpclient.execute(get);
      // 获得服务器响应回来的消息体（不包括HTTP HEAD）
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        // 获得响应的字符集编码信息
        // 即获取HTTP HEAD的：Content-Type:text/html;charset=UTF-8中的字符集信息
        // 注意: 终于知道了, 这里是从response返回的HTTP HEAD中提取charset的值的,
        // HEAD中可能不包括charset值,但是返回的内容content中一般肯定会有charset的值
        String charset = EntityUtils.getContentCharSet(entity);
        InputStream is = entity.getContent();
        byte[] content = IOUtils.toByteArray(is);

        String html = null;
        // 假如HTTP HEAD中不包含charset的信息，则从网页内容的<meta>标签中提取charset信息
        if (charset == null) {
          // 尝试用ISO-8859-1这个编码来解释HTML
          html = new String(content, "ISO-8859-1");
          Parser parser = new Parser();
          parser.setInputHTML(html);
          // 尝试解释本网页(不给传递filter)，HTMLParser在解释网页的过程中，会自动提取
          // <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>中
          // 所包含的编码信息
          parser.parse(null);

          // 如果网页中不包含编码信息，则这个值返回空
          // 这种情况适合<meta http-equiv="content-type" content="text/html; charset=GBK" /> 这种写法
          charset = parser.getEncoding();

          // 但是有的国内网站是这样的写法: <meta http-equiv="Content-Type" content="text/html"; charset="gb2312" />
          // 这种写法的时候就得另外写一下了
          List<MetaTag> metaTags = ParseUtils.parseTags(html, MetaTag.class);
          String charSet2 = null;
          for (MetaTag metaTag : metaTags) {
            charSet2 = metaTag.getAttribute("charset");
          }
          if (null != charSet2 && charset != charSet2) {
            charset = charSet2;
          }
        }

        if (charset == null) { // 可以采用猜测算法（现在忽略）
          charset = "ISO-8859-1";
        }

        return new String(content, charset);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] getImage(HttpClient httpclient, String url) {
    try {

      // 利用HTTP GET向服务器发起请求
      HttpGet get = new HttpGet(url);

      // 获得服务器响应的的所有信息
      HttpResponse response = httpclient.execute(get);
      // 获得服务器响应回来的消息体（不包括HTTP HEAD）
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        InputStream is = entity.getContent();
        return IOUtils.toByteArray(is);
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


//  @Test
//  public void testGetHtml() {
//    HttpClient httpClient = new DefaultHttpClient();
//    // String str = getHtml(httpClient,"http://www.pudongnews.com.cn/");
//    String str = getHtml(httpClient, "http://www.ibm.com/developerworks/cn/java/j-lo-profiling/");
//    System.out.println(str);
//  }
}
