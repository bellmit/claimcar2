package ins.platform.sysuser.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.schema.SysUser;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service(value = "sysUserService")
public class SysUserServiceSpringImpl implements SysUserService {
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
    private BaseDaoService baseDaoService;

	@Override
	public void updateByUserCode(String userCode, SysUserVo vo) {
		Assert.notNull(vo, "sysUser must have value.");
		Assert.hasText(vo.getUserCode(),
				"sysUser.getUserCode() must have value.");

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", vo.getUserCode());
		SysUser po = databaseDao.findUnique(SysUser.class, queryRule);
		Beans.from(vo).to(po);
		databaseDao.update(SysUser.class, po);
	}

	@Override
	public void updateById(String id, SysUserVo vo) {
		Assert.notNull(vo, "sysUser must have value.");
		SysUser po = new SysUser();
		Beans.from(vo).to(po);
		databaseDao.update(SysUser.class, po);
	}

	@Override
	public SysUserVo findByUserCode(String userCode) {
		Assert.hasText(userCode, "userCode must have value.");

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		SysUser po = databaseDao.findUnique(SysUser.class, queryRule);
		if(po == null){
			return null;
		}
		SysUserVo vo = new SysUserVo();
		Beans.from(po).to(vo);

		return vo;
	}

	@Override
	public SysUserVo findByEmail(String email) {
		Assert.hasText(email, "email must hava value.");
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("email", email);
		SysUser po = databaseDao.findUnique(SysUser.class, queryRule);
		if(po == null){
			return null;
		}
		SysUserVo vo = new SysUserVo();
		Beans.from(po).to(vo);
		return vo;
	}

	@Override
	public void deleteByUserCode(String userCode) {
		Assert.hasText(userCode, "userCode must have value.");
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		SysUser po = databaseDao.findUnique(SysUser.class, queryRule);
		// databaseDao.deleteByPK(SysUser.class, po.getId());
	}

	@Override
	public SysUserVo findByPK(String id) {
		Assert.notNull(id, "id must have value.");
		SysUser po = databaseDao.findByPK(SysUser.class, id);

		SysUserVo vo = new SysUserVo();
		Beans.from(po).to(vo);
		return vo;
	}

	//根据userCode查询名字
	@Override
	public List<SysUserVo> findByUserOrName(String userCode) {
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer(
				"from SysUser s ");
	//"from SysUser where 1=1 and validStatus = "+1);
		if (StringUtils.isNotBlank(userCode)) {
			queryString.append("where s.userCode  like ? ");
			paramValues.add("%"+userCode+ "%");
			queryString.append("or s.userName like ? ");
			paramValues.add("%"+userCode+ "%");
		}
		//StringBuffer queryString = new StringBuffer("from SysUser where  usercode like %"+userCode+"% or username like %"+userCode+"%");
		//StringBuffer queryString = new StringBuffer("from SysUser");
		List<SysUser> oldTUserPo = databaseDao.findAllByHql(SysUser.class,queryString.toString(),paramValues.toArray());
		List<SysUserVo> userVoList = new ArrayList<SysUserVo>();
		SysUserVo  userVo = new SysUserVo();
		for(int i = 0; i<oldTUserPo.size(); i++ ){
			SysUser prpUser = oldTUserPo.get(i);
			// po转vo
			userVo = Beans.copyDepth().from(prpUser).to(SysUserVo.class);
			userVoList.add(userVo);
		}
		return userVoList;
	}
	
	/**
	 * 修改密码前校验身份
	 */
	@Override
	public boolean findUserInfoByuserCode(String userCode, String oldPwd) {
		Assert.hasText(userCode, "userCode must have value.");
		Assert.hasText(oldPwd, "oldPwd must have value.");
		SysUserVo userVo = findByUserCode(userCode);
		if(userVo != null){
			if(oldPwd.equals(userVo.getPassword())){
				return true;
			}
		}
		return false;
	}

	@Override
	public void updatePwd(String userCode, String newpwd) {
	 String sql = " update ywuser.prpduser u set u.password ='"+ newpwd +"' where u.usercode = '"+userCode +"'";
	 baseDaoService.executeSQL(sql);
		
	}
	
	public void updateIdentifyNumber(String userCode,String identifyNumber){
		String sql = " update ywuser.prpduser u set u.identifyNumber ='"+ identifyNumber +"' where u.usercode = '"+userCode +"'";
		baseDaoService.executeSQL(sql);
	}
	
}
