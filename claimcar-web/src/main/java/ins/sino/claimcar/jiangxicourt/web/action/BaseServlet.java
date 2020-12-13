package ins.sino.claimcar.jiangxicourt.web.action;

/******************************************************************************
 * CREATETIME : 2016年11月30日 下午11:46:29
 ******************************************************************************/
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 支持使用@Autowired自动注入的Servlet
 * @author ★LiuPing
 * @CreateTime 2016年11月30日
 */
public class BaseServlet extends HttpServlet {

	/**  */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		super.init();
		WebApplicationContextUtils.getWebApplicationContext(getServletContext())
									.getAutowireCapableBeanFactory()
									.autowireBean(this);
	}
}
