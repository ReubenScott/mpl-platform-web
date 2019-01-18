package com.kindustry.system.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class LoginServlet {
	
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	private AdminDao adminDao;

	private int width;
	private int height;
	private int number; // 显示多少个字符
	private String codes; // 有哪些字符可供选择

	//把配置信息放在Servlet的配置项中,这里拿过来使用
	public void init(ServletConfig config) throws ServletException {
		width = Integer.parseInt(config.getInitParameter("width"));
		height = Integer.parseInt(config.getInitParameter("height"));
		number = Integer.parseInt(config.getInitParameter("number"));
		codes = config.getInitParameter("codes");
	}

	//旧的生成验证码方法
//	public void checkcode(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		response.setContentType("image/jpeg");
//
//		// 创建一张图片
//		BufferedImage image = new BufferedImage(width, height,
//				BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = image.createGraphics();
//
//		// 创建白色背景
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, width, height);
//
//		// 画黑边框,这个边框其实画不画的无所谓
//		g.setColor(Color.BLACK);
//		g.drawRect(0, 0, width - 1, height - 1);
//
//		Random random = new Random();
//
//		// 每个字符占据的宽度
//		int x = (width - 1) / number;
//		int y = height - 4;
//
//		StringBuffer sb = new StringBuffer();
//
//		// 随机生成字符
//		for (int i = 0; i < number; i++) {
//			String code = String.valueOf(codes.charAt(random.nextInt(codes
//					.length())));
//			int red = random.nextInt(255);
//			int green = random.nextInt(255);
//			int blue = random.nextInt(255);
//			g.setColor(new Color(red, green, blue));
//
//			Font font = new Font("Arial", Font.PLAIN,
//					random(height / 2, height));
//			g.setFont(font);
//
//			g.drawString(code, i * x + 1, y);
//
//			sb.append(code);
//		}
//
//		// 将验证码串放到HTTP SESSION中
//		request.getSession().setAttribute("codes", sb.toString());
//
//		// 随机生成一些点
//		for (int i = 0; i < 50; i++) {
//			int red = random.nextInt(255);
//			int green = random.nextInt(255);
//			int blue = random.nextInt(255);
//			g.setColor(new Color(red, green, blue));
//			g.drawOval(random.nextInt(width), random.nextInt(height), 1, 1);
//		}
//
//		OutputStream out = response.getOutputStream();
//		// 将图片转换为JPEG类型
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		encoder.encode(image);
//
//		out.flush();
//		out.close();
//
//	}

	/**
	 * 生成验证码
	 * 思路: 画一张图片, 然后将这张图片做为流输入到response中去
	 */
	public void checkcode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("image/jpeg");
		
		// 在内存中创建图象
		int width = 55, height = 20;
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体并控制字体大小
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位字符)
		StringBuilder sRand = new StringBuilder();
		for (int i = 0; i < 4; i++) {
//			String rand = String.valueOf(random.nextInt(10));
//			sRand += rand;
			
			//下面是产生字母的写法
			//产生的随机数在0~codes的length之间,而且不包括length
			String rand = String.valueOf(codes.charAt(random.nextInt(codes.length())));
			sRand.append(rand);
			
			// 将认证码显示到图象中, 认证码以随机的颜色显示
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			// 原来是每出来一个字母就生成一点,最后拼成四个字母的图片
			// 这里后两个参数是左下角的坐标值, x的值是会变化的,比如第一个字符在第0个位置画
			// 第二个字符在第1个位置画, 一次类推, 而Y高度的值则是固定不变的
			g.drawString(rand, 13 * i + 2, 16);
		}
		// 将认证码存入SESSION
		request.getSession().setAttribute("codes", sRand.toString());
		// 图象生效
		g.dispose();
		
		try {
			OutputStream out = response.getOutputStream();
			// 将图片转换为JPEG类型
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(image);

      ImageIO.write(image, "jpeg", out);


			out.flush();
			out.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		
	}
	
	/*
	 * 给定范围获得随机颜色
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	// 用于执行登录认证
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String checkcode = request.getParameter("checkcode"); // 用户输入的验证码

		// 系统判断验证码是否正确
		// 刚刚生成的验证码串
		String sessionCodes = (String) request.getSession().getAttribute(
				"codes");

		// 系统判断用户名是否存在，判断密码是否正确
//		Admin admin = adminDao.findAdminByUsername(username);

//		if (admin == null) {
//			// forward到long.jsp，并且提示“用户名不存在”
//			request.setAttribute("error", "用户【" + username + "】不存在");
//			request.getRequestDispatcher("/backend/login.jsp").forward(request,
//					response);
//			return;
//		}

//		if (!admin.getPassword().equals(password)) {
//			// forward到long.jsp，并且提示“用户密码不正确”
//			request.setAttribute("error", "用户【" + username + "】的密码不正确，请重试");
//			request.getRequestDispatcher("/backend/login.jsp").forward(request,
//					response);
//			return;
//		}

		 if(!sessionCodes.equalsIgnoreCase(checkcode)){
			 //重定向[forward]到登录页面
			 request.setAttribute("error", "验证码错误");
			 request.getRequestDispatcher("/backend/login.jsp").forward(request,
					 response);
			 return;
		 }
		
		// 需要把登录用户的信息存入HTTP SESSION
		request.getSession().setAttribute("LOGIN_ADMIN", username);

		// 判断都通过了，转向后台管理主页面
		response.sendRedirect(request.getContextPath() + "/backend/main.jsp");

	}
	
	//退出后台管理系统
	public void quit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 清空Http Session中的所有数据，销毁HTTP SESSION对象
		// 结束会话
		request.getSession().invalidate();

		// 转向登录页面
		response.sendRedirect(request.getContextPath() + "/backend/login.jsp");
	}	

	/**
	 * 产生一个从min到max之间的随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private int random(int min, int max) {
		int m = new Random().nextInt(999999) % (max - min);
		return m + min;
	}


}
