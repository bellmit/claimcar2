package ins.platform.common.web.util;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

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
	
}
