package com.kindustry.framework.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SecurityFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("SecurityFilter");
//		HttpServletRequest request = (HttpServletRequest) req ;
//		HttpServletResponse response = (HttpServletResponse) res ;
//		HttpSession session = request.getSession();

//		Object emp = session.getAttribute(SystemContant.CURRENT_USER);
		
//		String uri = request.getRequestURI();
//		// �û�û�е�¼
//		if (emp == null) {
//			if (uri.indexOf(".js") > 0 
//					|| uri.indexOf(".css") > 0 
//					|| uri.indexOf(".gif") > 0 
//					|| uri.indexOf(".jpg") > 0
//					|| uri.indexOf("login") > 0 
//					|| uri.indexOf("imageRnd") > 0
//					|| uri.indexOf("exteriorDownloadInit") > 0
//					|| uri.indexOf("exteriorDownloadFile") > 0
//					|| uri.indexOf("monitor") > 0) {
//				
//				chain.doFilter(req, res);
//			} else {
//				response.sendRedirect(request.getContextPath() + "/index.jsp");
//			}
//			
//			// �û��Ѿ���¼
//		} else {
//			chain.doFilter(req, res);
//		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
