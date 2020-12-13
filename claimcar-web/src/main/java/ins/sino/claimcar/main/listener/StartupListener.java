package ins.sino.claimcar.main.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StartupListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory
			.getLogger(StartupListener.class);

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void contextInitialized(ServletContextEvent sce) {

		String profile = System.getProperty("spring.profiles.active");
		if (profile == null || "".equals(profile.trim())) {
			System.setProperty("spring.profiles.active", "development");
			Environment env = WebApplicationContextUtils
					.getRequiredWebApplicationContext(sce.getServletContext())
					.getEnvironment();
			if (env instanceof StandardServletEnvironment) {
				StandardServletEnvironment sse = (StandardServletEnvironment) env;
				sse.setActiveProfiles("development");
			}

			profile = System.getProperty("spring.profiles.active");
		}
		logger.info("******** Running in ", profile, " Mode ********");
		// 初始化ServiceFactory
		// ServiceFactory.initServiceFactory(sce.getServletContext());
		logger.info("/************初始化定时任务 ********************/");
		// QuartzService quartzService = (QuartzService) Springs
		// .getBean("quartzService");
		// quartzService.initSchedule();

	}
}
