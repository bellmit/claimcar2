package ins.platform.shiro.web.util;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.core.SpringProperties;

public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {
	
	public MyShiroFilterFactoryBean(){
		super();
	}
	
	@Override
	public Object getObject() throws Exception {
		return super.createInstance();
	}
	
	@Override
	public boolean isSingleton() {
        return false;
    }

	@Override
	public String getLoginUrl() {
		String casserver = SpringProperties.getProperty("cas.casserver");
		String caswebapp = SpringProperties.getProperty("cas.webapp.local");
		String loginUrl = casserver + "/login?service=" + caswebapp + "/cas";
		return loginUrl;
	}
}
