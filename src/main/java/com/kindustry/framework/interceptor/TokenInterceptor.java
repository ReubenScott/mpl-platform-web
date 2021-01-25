package com.kindustry.framework.interceptor;


import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kindustry.framework.annotation.Token;

public class TokenInterceptor extends HandlerInterceptorAdapter {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      Method method = handlerMethod.getMethod();
      Token annotation = method.getAnnotation(Token.class);
      if (annotation != null) {
        boolean needSaveSession = annotation.save();
        if (needSaveSession) {
//          request.getSession(true).setAttribute("token", UUID.randomUUID().toString());
          HttpSession session = request.getSession(true);
          // Token [signature=e93e39f6b44ad12b8dec8349da73f237, timestamp=1447303373541]
          String signature = UUID.randomUUID().toString();
          String sessonId = session.getId() ;
          long createTime = session.getCreationTime();
          request.setAttribute("token", sessonId + createTime );
//        request.setAttribute("token", UUID.randomUUID().toString());
          logger.info(signature, sessonId, createTime );
        }
        boolean needRemoveSession = annotation.remove();
        if (needRemoveSession) {
          if (isRepeatSubmit(request)) {
            logger.warn("please don't repeat submit,url:" + request.getServletPath());
            return false;
          }
          request.getSession(true).removeAttribute("token");
        }
      }
      return true;
    } else {
      return super.preHandle(request, response, handler);
    }
  }

  private boolean isRepeatSubmit(HttpServletRequest request) {
    String serverToken = (String) request.getSession(true).getAttribute("token");
    if (serverToken == null) {
      return true;
    }
    String clinetToken = request.getParameter("token");
    if (clinetToken == null) {
      return true;
    }
    if (!serverToken.equals(clinetToken)) {
      return true;
    }
    return false;
  }
}
