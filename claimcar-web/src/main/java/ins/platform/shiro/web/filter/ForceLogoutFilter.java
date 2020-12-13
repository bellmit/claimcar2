package ins.platform.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

public class ForceLogoutFilter extends AccessControlFilter {  
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {  
        Session session = getSubject(request, response).getSession(false);  
        if(session == null) {  
            return true;  
        }  
        return session.getAttribute("session_force_logout_key") == null || session.getAttribute("session_force_logout_key") != Boolean.TRUE;  
    }  
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {  
        try {  
            getSubject(request, response).logout();//强制退出  
        } catch (Exception e) {/*ignore exception*/}  
        String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";  
        WebUtils.issueRedirect(request, response, loginUrl);  
        return false;  
    }  
}  
