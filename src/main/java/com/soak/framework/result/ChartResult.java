package com.soak.framework.result;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import com.opensymphony.xwork2.ActionInvocation;

public class ChartResult extends StrutsResultSupport {

  private static final long serialVersionUID = 1L;

  private int wordLength;

  private int imageWidth;

  private int imageHeight;

  private boolean useMap;

  private String toolTipFormat;

  private String url;

  protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
    HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(HTTP_RESPONSE);
    
    JFreeChart chart = (JFreeChart) invocation.getStack().findValue("chart");
    
//    chart.getTitle().setFont(new Font("华文行楷",Font.CENTER_BASELINE,15));   //设置标题字体 
//    chart.getLegend().setItemFont(new Font("黑体",Font.BOLD,15));  // 设置引用标签字体 
    chart.setBackgroundPaint(Color.LIGHT_GRAY);
    
    Plot plot = chart.getPlot();
    plot.setBackgroundPaint(Color.WHITE); 

    
    if (plot instanceof PiePlot) {
      PiePlot piePlot = (PiePlot) plot;
      piePlot.setCircular(true);     //选择形状 true圆形 false椭圆
//      piePlot.setLabelFont(new Font("黑体",Font.BOLD,10)); 
//      piePlot.setToolTipGenerator(new StandardPieToolTipGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
      piePlot.setToolTipGenerator(new StandardPieToolTipGenerator("ok"));
      piePlot.setURLGenerator(new StandardPieURLGenerator("www.baidu.com"));
    } else if (plot instanceof CategoryPlot) {
      CategoryPlot cPlot = (CategoryPlot) plot;
      CategoryItemRenderer render = cPlot.getRenderer();
      render.setBaseItemURLGenerator(new StandardCategoryURLGenerator(url));
      render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(toolTipFormat, NumberFormat.getInstance()));
    }

    OutputStream os = response.getOutputStream();
    ChartUtilities.writeChartAsJPEG(os, chart, imageWidth, imageHeight);

    os.flush();
    os.close();
  }

  protected void doExecute1(String arg0, ActionInvocation arg1) throws Exception {
    JFreeChart chart = (JFreeChart) arg1.getStack().findValue("chart");

    if (useMap) {
      Plot plot = chart.getPlot();
      if (plot instanceof PiePlot) {
        PiePlot piePlot = (PiePlot) plot;
        piePlot.setToolTipGenerator(new StandardPieToolTipGenerator(toolTipFormat));
        piePlot.setURLGenerator(new StandardPieURLGenerator(url));
      } else if (plot instanceof CategoryPlot) {
        CategoryPlot cPlot = (CategoryPlot) plot;
        CategoryItemRenderer render = cPlot.getRenderer();
        render.setBaseItemURLGenerator(new StandardCategoryURLGenerator(url));
        render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(toolTipFormat, NumberFormat.getInstance()));
      }
    }

    ChartRenderingInfo info = new ChartRenderingInfo();
    String filename = ServletUtilities.saveChartAsPNG(chart, imageWidth, imageHeight, info, null);
    String map = ChartUtilities.getImageMap("map", info);
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=gbk");
    PrintWriter out = response.getWriter();

    if (useMap) {
      out.println(map);
    }
    out.println("<p align='center'><img src='" + System.getProperty("java.io.tmpdir") + "/" + filename + "' usemap='#map'>");
    System.out.println(chart + "    " + this.imageHeight + " " + this.toolTipFormat + "  " + this.url + "  " + this.imageWidth + "  " + this.useMap);
    System.out.println("<p align='center'><img src='" + System.getProperty("java.io.tmpdir") + "/" + filename + "' usemap='#map'>");

  }

  /******************      get set      ******************/

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getToolTipFormat() {
    return toolTipFormat;
  }

  public void setToolTipFormat(String toolTipFormat) {
    this.toolTipFormat = toolTipFormat;
  }

  public boolean isUseMap() {
    return useMap;
  }

  public void setUseMap(boolean useMap) {
    this.useMap = useMap;
  }

  public int getWordLength() {
    return wordLength;
  }

  public void setWordLength(int wordLength) {
    this.wordLength = wordLength;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

}
