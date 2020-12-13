package ins.framework.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet Filter implementation class WebFilter
 */
public class WebAppFilter implements Filter {
	public static final String REQUEST_START_TIME = "REQUEST_START_TIME";

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// encodingFilter.destroy();
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		request.setAttribute(REQUEST_START_TIME, new Date());
		//
		// // 1.字符编码过滤
		// encodingFilter.doFilter(request, response, filterChain);
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			String url = req.getServletPath();
//			System.out.println(dispatcherType + "=>" + url);
		}

		// XssHttpServletRequestWrapper xssRequest = new
		// XssHttpServletRequestWrapper(
		// (HttpServletRequest) request);
		// 启用代理
		// filterChain.doFilter(xssRequest, response);
		// 不启用代理
		filterChain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("===================PlatformFilter============init===============");
		// encodingFilter.init(filterConfig);
		//
		// String encoding = getInitParameterWithDefalut(filterConfig,
		// "encoding", "utf-8");
		// encodingFilter.setEncoding(encoding);

	}

	private String getInitParameterWithDefalut(FilterConfig filterConfig, String name, String defaultValue) {
		String value = filterConfig.getInitParameter("encoding");
		if (StringUtils.isEmpty(value)) {
			value = defaultValue;
		}
		return value;
	}
}
