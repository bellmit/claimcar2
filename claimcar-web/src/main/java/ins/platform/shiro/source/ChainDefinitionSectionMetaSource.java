package ins.platform.shiro.source;

import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaRoleTaskVo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

	private String filterChainDefinitions;

	@Autowired
	private SaaUserPowerService saaUserPowerService;

	/**
	 * 默认premission字符串
	 */
	public static final String PREMISSION_STRING = "anyRoles[{0}]";

	public Section getObject() throws BeansException {

		// 获取所有Resource
		List<Resource> list = new ArrayList<Resource>();
		list.add(new Resource("","/logout","logout"));
		// list.add(new Resource("", "/rpc/**", "authcBasic"));
		List<SaaRoleTaskVo> saaRoleTaskVoList = saaUserPowerService.findAllSaaRoleTask();
		Map<String,String> urlMap = new HashMap<String,String>();
		if(saaRoleTaskVoList!=null&&saaRoleTaskVoList.size()>0){
			// 合并拥有同一url权限的角色
			for(SaaRoleTaskVo vo:saaRoleTaskVoList){
				String url = vo.getSaaTask().getUrl();
				String roleCode = vo.getSaaRole().getRoleCode();
				if(urlMap.containsKey(url)){
					urlMap.put(url,urlMap.get(url)+","+roleCode);
				}else{
					urlMap.put(url,roleCode);
				}
			}

			Map<String,String> addedUrlMap = new HashMap<String,String>();
			Iterator<String> keyIt = urlMap.keySet().iterator();
			while(keyIt.hasNext()){
				String url = keyIt.next();
				if(url.indexOf("*")!= -1){
					continue;
				}
				list.add(new Resource("",url,"authc"+MessageFormat.format(PREMISSION_STRING,urlMap.get(url))));
				addedUrlMap.put(url,"");
			}
			Iterator<String> upperKeyIt = urlMap.keySet().iterator();
			while(upperKeyIt.hasNext()){
				String url = upperKeyIt.next();
				if(addedUrlMap.containsKey(url)||"/**".equals(url)){
					continue;
				}
				list.add(new Resource("",url,"authc"+MessageFormat.format(PREMISSION_STRING,urlMap.get(url))));
			}
		}
		if(urlMap.get("/**")!=null){
			list.add(new Resource("","/**","authc"+MessageFormat.format(PREMISSION_STRING,urlMap.get("/**"))));
		}else{
			list.add(new Resource("","/**","authc"));
		}

		Ini ini = new Ini();
		// 加载默认的url
		ini.load(filterChainDefinitions);
		Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		// 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
		// 里面的键就是链接URL,值就是存在什么条件才能访问该链接
		for(Iterator<Resource> it = list.iterator(); it.hasNext();){

			Resource resource = it.next();
			// 如果不为空值添加到section中
			if(StringUtils.isNotEmpty(resource.getValue())&&StringUtils.isNotEmpty(resource.getPermission())){
				section.put(resource.getValue(),resource.getPermission());
			}

		}
		return section;
	}

	/**
	 * 通过filterChainDefinitions对默认的url过滤定义
	 * @param filterChainDefinitions 默认的url过滤定义
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public Class<?> getObjectType() {
		return this.getClass();
	}

	public boolean isSingleton() {
		return false;
	}

}
