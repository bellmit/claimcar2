package ins.platform.common.apc;

import ins.framework.web.AjaxResult;
import ins.platform.common.apc.annotation.AvoidRepeatableCommit;
import ins.platform.common.apc.annotation.ParamConfig;
import ins.platform.common.util.EhcacheUtil;
import ins.platform.common.util.IPUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
@Aspect
public class AvoidRepeatableCommitAspect {

	private static final String CACHE_NAME = "AvoidRepeatableCommitList";
	private static final Logger logger = LoggerFactory.getLogger(AvoidRepeatableCommitAspect.class);
	private static final EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();

	@Around("@annotation(avoidRepeatableCommit)")
	public Object around(ProceedingJoinPoint point,AvoidRepeatableCommit avoidRepeatableCommit) throws Throwable {
		HttpServletRequest request = ( (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes() ).getRequest();
		String ip = IPUtil.getClientIp(request);
		String userCode = "unknown";
		try{
			userCode = WebUserUtils.getUserCode();
		}catch(Exception e){
		}
		Method method = ( (MethodSignature)point.getSignature() ).getMethod();
		String nameAndArgs = this.getArgsNameAndValues(method,point.getArgs());
		// 目标类、方法
		String ipKey = String.format("%s#%s",method.getDeclaringClass().getName(),method.getName());
		String key = String.format("%s_%s_%s_%d",ip,userCode,Math.abs(nameAndArgs.hashCode()),Math.abs(ipKey.hashCode()));
		logger.info("AvoidRepeatableCommitList ehcache key: "+ip+userCode+nameAndArgs+ipKey);
		// avoidRepeatableCommit = method.getAnnotation(AvoidRepeatableCommit.class);
		// final long timeout = ObjectUtils.isEmpty(avoidRepeatableCommit.timeout()) ? 30 : avoidRepeatableCommit.timeout();
		Object value = ehcacheUtil.get(CACHE_NAME,key);
		// logger.info("AvoidRepeatableCommitList ehcache value: "+value);
		if( !ObjectUtils.isEmpty(value)){
			logger.info("重复提交");
			if(ins.framework.web.AjaxResult.class.equals(method.getReturnType())){
				AjaxResult ajaxResult = new AjaxResult();
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NO_CONTENT);
				ajaxResult.setStatusText("请勿重复提交");
				return ajaxResult;
			}else if(String.class.equals(method.getReturnType())){
				return "DoNotRepeatSubmit";
			}else{
				return null;
			}
		}
		/*new Timer().schedule(new TimerTask() {

			long to = timeout;
			public void run() {
				to-- ;
				System.out.println("to: "+to);
				if(to==0){
					cacheService.remove(key);
					cancel();
				}
			}
		},0,1000);*/
		// 执行方法
		Object object = null;
		try{
			ehcacheUtil.put(CACHE_NAME,key,key);
			object = point.proceed();
		}
		finally{
			ehcacheUtil.remove(CACHE_NAME,key);
		}
		return object;
	}

	private static final List<String> types = Arrays
			.asList(new String[]{"java.lang.Integer","java.lang.Double","java.lang.Float","java.lang.Long","java.lang.Short","java.lang.Byte","java.lang.Boolean","java.lang.Char","java.lang.String","java.util.Date","java.sql.Date","java.sql.Time","java.sql.Timestamp","java.math.BigDecimal","java.math.BigInteger","int","double","long","short","byte","boolean","char","float"});

	private String getArgsNameAndValues(Method method,Object[] args) {
		try{
			StringBuffer sb = new StringBuffer();
			ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
			String[] parameterNames = pnd.getParameterNames(method);
			for(int i = 0; i<parameterNames.length; i++ ){
				if( !ObjectUtils.isEmpty(args[i]) ){
					if(types.contains(args[i].getClass().getName())){
						sb.append(parameterNames[i]+"="+args[i]+"&");
					}else if (args[i] instanceof Collection){
						Collection newObj = (Collection)args[i];
						Iterator iter = newObj.iterator();
						while (iter.hasNext()) {
							sb.append(getFieldNameAndValues(iter.next()));
						}
					}else if (args[i] instanceof Object){
						sb.append(getFieldNameAndValues(args[i]));
					}
				}
			}
			if( !ObjectUtils.isEmpty(sb)&&sb.length()>0){
				return sb.toString().substring(0,sb.toString().length()-1);
			}
		}catch(Exception e){
			logger.error("AvoidRepeatableCommitAspect getFieldsName error: ",e);
		}
		return "null";
	}

	public String getFieldNameAndValues(Object object) {
		try {
			ParamConfig pcObject = object.getClass().getAnnotation(ParamConfig.class);
			if (pcObject == null) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
			Field[] fd = object.getClass().getDeclaredFields();
			for (Field f : fd) {
				ParamConfig pc=f.getAnnotation(ParamConfig.class);
				if (pc == null) {
					continue;
				}
				Class clazz = pc.type();
				String fildName = pc.name();
				if(ObjectUtils.isEmpty(fildName)){
					fildName =f.getName();
				}
				if(ObjectUtils.isEmpty(fildName)){
					continue;
				}
				f.setAccessible(true);
				Object obj = null;
				try {
					obj = f.get(object);
				} catch (Exception e) {
					logger.error("get field value error: ",e);
					throw new RuntimeException(e);
				}
				if (ObjectUtils.isEmpty(obj)) {
					continue;
				}
				if(types.contains(obj.getClass().getName())){
					sb.append(fildName+"="+obj+"&");
				}else if(Collection.class == clazz){
					Collection newObj = (Collection)obj;
					Iterator iter = newObj.iterator();
					while (iter.hasNext()) {
						sb.append(getFieldNameAndValues(iter.next()));
					}
				}else if(Object.class == clazz){
					sb.append(getFieldNameAndValues(obj));
				}
			}
			if( !ObjectUtils.isEmpty(sb)&&sb.length()>0){
				return sb.toString();
			}
		}catch (Exception e){
			logger.error("getFieldNameAndValues error: ",e);
		}
		return "";
	}

}
