/******************************************************************************
* CREATETIME : 2016年6月28日 下午4:25:13
******************************************************************************/
package ins.platform.shiro.source;

import com.google.common.base.Objects;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月28日
 */
public class ShiroUser {

	private static final long serialVersionUID = -1373760761780840081L;
	protected String loginName;
	protected String name;

	public ShiroUser(String loginName,String name){
		this.loginName = loginName;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getLoggerinName() {
		return loginName;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return loginName;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if(this==obj){
			return true;
		}
		if(obj==null){
			return false;
		}
		if(getClass()!=obj.getClass()){
			return false;
		}
		ShiroUser other = (ShiroUser)obj;
		if(loginName==null){
			if(other.loginName!=null){
				return false;
			}
		}else if( !loginName.equals(other.loginName)){
			return false;
		}
		return true;
	}

}
