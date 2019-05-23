/*
 * Created on 2005-7-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.web.filters;

/**
 * Created on 2005-7-12
 * <p>
 * Title: 至于Web的公用方法
 * </p>
 * <p>
 * Description: 实现url参数传递时字符集的转化(主要针对form表单中文乱码问题)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * <p>
 * Company:友联科技
 * </p>
 * 
 * @author wyj
 * @version 1.0
 */
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.gzunicorn.common.util.DateUtil;

public class SetCharacterEncodingFilter implements Filter {

	protected String encoding = null;

	protected FilterConfig filterConfig = null;

	protected boolean ignore = true;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//
		// if (ignore || (request.getCharacterEncoding() == null)) {
		// String encoding = selectEncoding(request);
		// if (encoding != null)
		// request.setCharacterEncoding(encoding);
		// }
		// 
		// chain.doFilter(request, response);

		// 这里最主要的是进行字符转码，解决中文的乱码问题。

		// 获得当前编码字符的类型。
		String curencoding = request.getCharacterEncoding();
		if (ignore || (request.getCharacterEncoding() == null)) {
			String encoding = selectEncoding(request);
			// equalsIgnoreCase 的作用和equals是一样的，但是前者是忽然大小写的转换。
			// 设置成UTF-8是为了适应AJAX的应用，因为到AJAX中，所有的内容编码都是转换成UTF-8。
			if (curencoding != null && curencoding.equalsIgnoreCase("utf-8")) {
				encoding = "utf-8";
			}
			if (encoding != null)
				request.setCharacterEncoding(encoding);
		}

		HttpServletRequest req = (HttpServletRequest) request;
		String name = (String) request.getParameter("method");
		String todaystt=DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");
		System.out.println(todaystt+" "+req.getRequestURI() + " ==>>" + name);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {

		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;

	}

	protected String selectEncoding(ServletRequest request) {

		return (this.encoding);

	}

}

