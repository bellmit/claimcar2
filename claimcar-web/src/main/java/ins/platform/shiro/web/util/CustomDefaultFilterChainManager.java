package ins.platform.shiro.web.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;

import org.apache.shiro.config.Ini;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;

public class CustomDefaultFilterChainManager extends DefaultFilterChainManager {
	
	private Map<String, String> filterChainDefinitionMap = null;
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;
	
	public CustomDefaultFilterChainManager() {
		setFilters(new LinkedHashMap<String, Filter>());
		setFilterChains(new LinkedHashMap<String, NamedFilterList>());
		addDefaultFilters(true);
	}
	
	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}
	
	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}
	
	public void setCustomFilters(Map<String, Filter> customFilters) {
		for(Map.Entry<String, Filter> entry : customFilters.entrySet()) {
			addFilter(entry.getKey(), entry.getValue(), false);
		}
	}
	
	public void setDefaultFilterChainDefinitions(String definitions) {
		Ini ini = new Ini();
		ini.load(definitions);
		Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}
		setFilterChainDefinitionMap(section);
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}
	
	public void setLoginUrl(String loginUrl) {
	this.loginUrl = loginUrl;
	}
	public String getSuccessUrl() {
	return successUrl;
	}
	
	public void setSuccessUrl(String successUrl) {
	this.successUrl = successUrl;
	}
	
	public String getUnauthorizedUrl() {
	return unauthorizedUrl;
	}
	
	public void setUnauthorizedUrl(String unauthorizedUrl) {
	this.unauthorizedUrl = unauthorizedUrl;
	}
	
	@PostConstruct
	public void init() {
		Map<String, Filter> filters = getFilters();
		if (!CollectionUtils.isEmpty(filters)) {
			for (Map.Entry<String, Filter> entry : filters.entrySet()) {
				String name = entry.getKey();
				Filter filter = entry.getValue();
				applyGlobalPropertiesIfNecessary(filter);
				if (filter instanceof Nameable) {
					((Nameable) filter).setName(name);
				}
				addFilter(name, filter, false);
			}
		}
		Map<String, String> chains = getFilterChainDefinitionMap();
		if (!CollectionUtils.isEmpty(chains)) {
			for (Map.Entry<String, String> entry : chains.entrySet()) {
				String url = entry.getKey();
				String chainDefinition = entry.getValue();
				createChain(url, chainDefinition);
			}
		}
	}
	
	protected void initFilter(Filter filter) {
		//ignore
	}
	
	public FilterChain proxy(FilterChain original, List<String> chainNames) {
		NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
		for(String chainName : chainNames) {
			configured.addAll(getChain(chainName));
		}
		return configured.proxy(original);
	}
	
	private void applyGlobalPropertiesIfNecessary(Filter filter) {
		applyLoginUrlIfNecessary(filter);
		applySuccessUrlIfNecessary(filter);
		applyUnauthorizedUrlIfNecessary(filter);
	}
	
	private void applyLoginUrlIfNecessary(Filter filter) {
		//请参考源码
	}
	
	private void applySuccessUrlIfNecessary(Filter filter) {
		//请参考源码
	}
	
	private void applyUnauthorizedUrlIfNecessary(Filter filter) {
		//请参考源码
	}

}
