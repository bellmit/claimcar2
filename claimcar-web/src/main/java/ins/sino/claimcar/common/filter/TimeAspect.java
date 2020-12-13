package ins.sino.claimcar.common.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 统计调用时间处理
 * @author j2eel
 *
 */
@Aspect
@Component
public class TimeAspect {
	
//	private static final Logger logger = LoggerFactory.getLogger("com.sinosoft.performance.audit");
	private static final Logger logger = LoggerFactory.getLogger(TimeAspect.class);
	
	@Around("@annotation(timed)")
	public Object around(ProceedingJoinPoint point , Timed timed) throws Throwable{
		
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();
		Long endTime = System.currentTimeMillis();
		logger.info(timed.value() + "耗时:[{}]ms" ,endTime - startTime);
		return obj;
		
	}

}
