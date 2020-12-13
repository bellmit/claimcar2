package ins.platform.shiro.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.SpringProperties;

import ins.platform.common.web.util.SpringUtils;
import ins.sino.claimcar.claim.vo.PrpLKeyWordsCfgVo;
import ins.sino.claimcar.claim.vo.PrpLWhiteListCfgVo;
import ins.sino.claimcar.other.service.WhitelistCfgService;

public class InjectSQLFilter implements Filter {

	private static final Log logger = LogFactory.getLog(InjectSQLFilter.class);

	private boolean openWhiteList = false;
	private boolean openFilter = false;
	private static List<String> keyWordList = null;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {
		
		if (keyWordList == null) {
			initKeyWords();
		}

		if (!openWhiteList) {
			filterChain.doFilter(req, resp);
			return;
		}

		HttpServletRequest originalRequest = (HttpServletRequest) req;
		
		StringBuffer reqPath = new StringBuffer();
		reqPath.append("");
		String path = originalRequest.getServletPath();
        if (path != null && path.trim().length() > 0) {
            reqPath.append(path);
        }
        String pathInfo = originalRequest.getPathInfo();
        if (pathInfo != null && pathInfo.trim().length() > 0) {
            reqPath.append(pathInfo);
        }
        String queryString = originalRequest.getQueryString();
        if (queryString != null && queryString.trim().length() > 0) {
            reqPath.append("?" + queryString);
        }
		String servletPath = reqPath.toString();

		List<String> filterkeyWords = isUnsafe(originalRequest.getParameterMap());
		if (filterkeyWords.size() == 0) {
			filterChain.doFilter(req, resp);
		} else {
			// 写入白名单
			StringBuilder keyWordSb = new StringBuilder();
			for (String keyWord : filterkeyWords) {
				keyWordSb.append(keyWord).append(",");
			}
			String keyWordStr = keyWordSb.toString();
			if (keyWordStr.length() > 500) {
				keyWordStr = keyWordStr.substring(0, 500);
			}
			if(servletPath != null && servletPath.length() > 500) {
				servletPath = servletPath.substring(0, 500);
			}
			boolean hasWhiteList = whiteListDeal(servletPath, keyWordStr.substring(0, keyWordStr.length() - 1));
			if (!openFilter) {
				filterChain.doFilter(req, resp);
			} else {
				if (hasWhiteList) {
					filterChain.doFilter(req, resp);
				} else {
					throw new ServletException("你输入的信息中包含系统敏感或特殊字符，请与系统管理员联系");
				}
			}
		}

	}

	private boolean whiteListDeal(String url, String keywords) {
		boolean hasWhiteList = false;
		try {
			WhitelistCfgService whitelistCfgService = (WhitelistCfgService) SpringUtils.getObject("whitelistCfgService");
			PrpLWhiteListCfgVo whitelistCfgVo = whitelistCfgService.findWhitelistInfoByURL(url);
			if (whitelistCfgVo != null) {
				hasWhiteList = true;
			} else {
				// 如果没有开启拦截器则录入白名单列表，则保存白名单信息
				if(!openFilter) {
					whitelistCfgVo = new PrpLWhiteListCfgVo();
					whitelistCfgVo.setKeywords(keywords);
					whitelistCfgVo.setStatus("1");
					whitelistCfgVo.setUrl(url);
					
					whitelistCfgService.saveWhitelistInfo(whitelistCfgVo);
				}
			}
		} catch (Exception e) {
			logger.error("WhiteListDeal error", e);
		}
		return hasWhiteList;
	}

	@SuppressWarnings("rawtypes")
	private List<String> isUnsafe(Map parameterMap) {
		List<String> filterkeyWords = new ArrayList<String>();
		Iterator iter = parameterMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String[] param = (String[]) parameterMap.get(key);
			for (int i = 0; i < param.length; i++) {
				String lowerCase = param[i].toLowerCase();
				if(keyWordList != null && keyWordList.size() > 0) {
					for (int j = 0; j < keyWordList.size(); j++) {
						if (lowerCase.indexOf(keyWordList.get(j)) >= 0) {
							filterkeyWords.add(keyWordList.get(j));
						}
					}
				}
			}
		}
		return filterkeyWords;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	/**
	 * 	初始化关键字列表
	 * @throws ServletException
	 */
	public void initKeyWords() throws ServletException {

		keyWordList = new ArrayList<String>();
		try {
			// 开启白名单标识
			String openWhiteListFlag = SpringProperties.getProperty("OPEN_WHITE_LIST");
			// 开启拦截标识
			String openFilterFlag = SpringProperties.getProperty("OPEN_FILTER");
			// 开启了白名单
			if (openWhiteListFlag != null && "1".equals(openWhiteListFlag)) {
				openWhiteList = true;
			}
			// 打开了拦截器
			if (openFilterFlag != null && "1".equals(openFilterFlag)) {
				openFilter = true;
			}
			// 初始化关键字列表
			WhitelistCfgService whitelistCfgService = (WhitelistCfgService) (WhitelistCfgService) SpringUtils.getObject("whitelistCfgService");
			PrpLKeyWordsCfgVo keyWordsVo = new PrpLKeyWordsCfgVo();
			keyWordsVo.setStatus("1");
			List<PrpLKeyWordsCfgVo> keyWordCfgVos = whitelistCfgService.findAllValidKeyWords(keyWordsVo);
			if (keyWordCfgVos != null && keyWordCfgVos.size() > 0) {
				for (int i = 0; i < keyWordCfgVos.size(); i++) {
					PrpLKeyWordsCfgVo keyWordVo = keyWordCfgVos.get(i);
					if (keyWordVo != null) {
						keyWordList.add(keyWordVo.getKeyword());
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("InjectSQLFilter init error", e);
		}
	}

	@Override
	public void destroy() {

	}

}
