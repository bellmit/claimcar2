package ins.sino.claimcar.rule.utils;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.UrlResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

/**
 * 规则执行类
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-7-22
 * @since (2014-7-22 下午03:51:29): <br>
 */
public class Drools5RuleAgent {

	private static Logger log = LoggerFactory.getLogger(Drools5RuleAgent.class);
	private static final String urlSuffix = "/LATEST";
	/**
	 * ruleCache 的缓存
	 */
	private static CacheService ruleCache = CacheManager.getInstance("Drools5RuleAgent_KnowledgeBase");
	
	/**
	 * 远程方式执行规则
	 * 
	 * @param pkgname 规则包
	 * @param objects 规则因子数据
	 * @author ☆HuangYi(2014-7-22 下午03:51:49): <br>
	 * @throws Exception
	 */
	public static void executeRules(String pkgname,Object... objects) {
		KnowledgeBase kbase = readKnowledgeBaseByPkg(pkgname);
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		for(Object object:objects){
			ksession.insert(object);
		}
		ksession.fireAllRules();
		for(Object object:objects){
			ksession.retract(ksession.getFactHandle(object));
		}
		ksession.dispose();
	}
	
	
	private static KnowledgeBase readKnowledgeBaseByPkg(String pkgname) {
		KnowledgeBase kbase = (KnowledgeBase)ruleCache.getCache(pkgname);
		if(kbase!=null) return kbase;
		String ruleServerUrl = SpringProperties.getProperty("rule.server.url");
		String ruleServerName = SpringProperties.getProperty("rule.server.username");
		String ruleServerPass = SpringProperties.getProperty("rule.server.password");
		//KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		Properties properties = new Properties();
		properties.setProperty( "drools.dialect.java.compiler.lnglevel","1.6" );
		PackageBuilderConfiguration cfg = new PackageBuilderConfiguration( properties );
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		
		
		URL url = null;
		String urlStr = ruleServerUrl+pkgname+urlSuffix;
		try{
			url = new URL(urlStr);
		}catch(MalformedURLException e){
			log.error(e.getMessage(),e);
			throw new IllegalArgumentException(urlStr+"获取规则错误,"+e.getMessage(),e);
		}// 规则地址
		UrlResource urlResource = (UrlResource)ResourceFactory.newUrlResource(url);
		urlResource.setBasicAuthentication("enabled");
		urlResource.setUsername(ruleServerName);// 用户
		urlResource.setPassword(ruleServerPass);// 密码
		kbuilder.add(urlResource,ResourceType.PKG);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if(errors.size()>0){
			for(KnowledgeBuilderError error:errors){
				if(error!=null) log.error(error.getMessage());
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}else{
			kbase = KnowledgeBaseFactory.newKnowledgeBase();
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
			ruleCache.putCache(pkgname,kbase);
			log.info("规则进行缓存："+pkgname);
			return kbase;
		}
	}
	/**
	 * 清除缓存
	 */
	public void clearCache(){
		
		ruleCache.clearAllCacheManager();
	}
}
