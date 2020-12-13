package ins.platform.shiro.service.spring;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.core.SpringProperties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyLogoutFilter extends LogoutFilter {

    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        return getRedirectUrl();
    }

    public String getRedirectUrl() {
        String casserver = SpringProperties.getProperty("cas.casserver");
        String caswebapp = SpringProperties.getProperty("cas.webapp.local");
        String logoutUrl = casserver + "/logout?service=" + caswebapp;
        return logoutUrl;
    }
}
