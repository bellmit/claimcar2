package ins.sino.claimcar.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * MDC日志拦截
 * @author j2eel
 *
 */
public class LogFilter implements Filter{

	public static final String TRACE_ID = "traceId";
    public static final String REQUEST_URL = "RequestURL";
    
    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
	
	/**
	 * 排除链接
	 */
	public List<String> excludes = new ArrayList<String>();
	
	/**
	 * 过滤开关
	 */
	public boolean enabled = false;
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		String tempExcludes = filterConfig.getInitParameter("excludes");
		String tempEnabled = filterConfig.getInitParameter("enabled");
		if (StringUtils.isNotEmpty(tempExcludes)) {
			String[] url = tempExcludes.split(",");
			for (int i = 0; url != null && i < url.length; i++) {
				excludes.add(url[i]);
			}
		}
		if (StringUtils.isNotEmpty(tempEnabled)) {
			enabled = Boolean.valueOf(tempEnabled);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (handleExcludeURL(req, resp)) {
			chain.doFilter(request, response);
			return;
		}
		try {
			MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
			MDC.put(REQUEST_URL, ((HttpServletRequest) request).getRequestURI() + (((HttpServletRequest) request).getQueryString() == null ? "" : "?" + ((HttpServletRequest) request).getQueryString()));
			Long startTime = System.currentTimeMillis();
			
			chain.doFilter(request, response);
			
			Long endTime = System.currentTimeMillis();
			logger.info("此请求耗时:[{}]ms",endTime - startTime);

		} finally {
			MDC.remove(TRACE_ID);
			MDC.remove(REQUEST_URL);
		}
	}

	
	private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
		if (!enabled) {
			return true;
		}
		if (excludes == null || excludes.isEmpty()) {
			return false;
		}
		String url = request.getServletPath();
		for (String pattern : excludes) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
