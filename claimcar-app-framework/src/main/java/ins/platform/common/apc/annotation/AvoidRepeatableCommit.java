package ins.platform.common.apc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 避免重复提交
 * @author ★Lundy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AvoidRepeatableCommit {

	/**
	 * 指定时间内不可重复提交,单位秒
	 * @return
	 */
	long timeout() default 30;
}
