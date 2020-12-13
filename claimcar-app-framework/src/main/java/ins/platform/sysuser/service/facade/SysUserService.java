package ins.platform.sysuser.service.facade;

import ins.platform.vo.SysUserVo;

import java.util.List;

/**
 * 
 * 用户登录服务接口<br>
 * 继承基础的CRUD方法后，这里只需要声明业务特定的方法即可
 * 
 */

public interface SysUserService {

	/**
	 * 根据用户名查询用户
	 * 
	 * @param userCode 用户名
	 * @return 用户
	 */
	public SysUserVo findByUserCode(String userCode);


	/**
	 * 根据注册邮箱查询用户
	 * 
	 * @param email 注册邮箱
	 * @return 用户
	 */
	public SysUserVo findByEmail(String email);

	/**
	 * 根据用户userCode删除一个用户
	 * 
	 * @param userCode userCode
	 */
	public void deleteByUserCode(String userCode);

	/**
	 * 根据userCode修改用户
	 * 
	 * @param userCode userCode
	 * @param sysUser sysUser
	 */
	public void updateByUserCode(String userCode, SysUserVo sysUser);


	/**
	 * 根据用户ID修改用户
	 * 
	 * @param id id
	 * @param sysUser sysUser
	 */
	public void updateById(String id, SysUserVo sysUser);
	/**
	 * 根据主键获取对象. 例如：以下代码获取id=2的记录
	 * 
	 * <pre>
	 * 		&lt;code&gt;
	 * User user = service.findByPK(2);
	 * &lt;/code&gt;
	 * </pre>
	 * 
	 * @param id
	 *            PK
	 * @return 匹配的对象
	 */
	public SysUserVo findByPK(String id);
	

	
	//查询名字或者数字的转换
	public List<SysUserVo> findByUserOrName(String userCode);
	
	public boolean findUserInfoByuserCode(String userCode,String oldPwd); 
	
	public void updatePwd(String userCode,String newpwd);
	
	public void updateIdentifyNumber(String userCode,String identifyNumber);
}
