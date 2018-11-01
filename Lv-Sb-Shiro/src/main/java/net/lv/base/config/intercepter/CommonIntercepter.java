package net.lv.base.config.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 配置拦截器 HandlerInterceptor
 * 作用：
 * SpringMVC 的拦截器类似于 Servlet 开发中的过滤器 Filter，用于对处理器进行预处理和后处理。
 * @author lv
 *
 */
@Component
public class CommonIntercepter implements HandlerInterceptor {

	//<!--资源访问之前-->
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		 return true;
	}

	//<!--资源访问之后，视图渲染之前 ...-->
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
			// 获取项目的根路径
			request.setAttribute("ctx", request.getContextPath());
	}

	//<!--视图渲染完之后 。。。-->
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}


}
