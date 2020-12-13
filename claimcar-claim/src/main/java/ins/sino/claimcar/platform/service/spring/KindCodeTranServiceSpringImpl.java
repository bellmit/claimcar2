package ins.sino.claimcar.platform.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.platform.service.KindCodeTranService;
import ins.sino.claimcar.platform.vo.transKindCodeVo.SysKindcodeconvertVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "kindCodeTranService")
public class KindCodeTranServiceSpringImpl implements KindCodeTranService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Override
	public SysKindcodeconvertVo findTransKindCodeCovertVo(String comCode,
			String riskCode, String codeType, String codeCode) {
		SysKindcodeconvertVo sysCodeDictVo = null;
//		QueryRule queryRule = QueryRule.getInstance();
//		SysKindcodeconvertId kcId = new SysKindcodeconvertId();
//		kcId.setCodeCode(codeCode);
//		kcId.setCodeType(codeType);
//		kcId.setComCode(comCode);
//		kcId.setRiskType(riskCode);
//		queryRule.addEqual("SysKindcodeconvertId", kcId);		
//		SysKindcodeconvert sysCodeDict = databaseDao.findUnique(SysKindcodeconvert.class, queryRule);
//		if(sysCodeDict != null){
//			sysCodeDictVo = new SysKindcodeconvertVo();
//			Beans.copy().from(sysCodeDict).to(sysCodeDictVo);
//		}
		return sysCodeDictVo;
	}

	@Override
	public String findTransCiCode(String comCode, String riskCode,
			String codeType, String codeCode) {
	/*	SysKindcodeconvertVo sysCodeDictVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		SysKindcodeconvertId kcId = new SysKindcodeconvertId();
		kcId.setCodeCode(codeCode);
		kcId.setCodeType(codeType);
		kcId.setComCode(comCode);
		kcId.setRiskType(riskCode);
		queryRule.addEqual("id", kcId);
		SysKindcodeconvert sysCodeDict = databaseDao.findByPK(SysKindcodeconvert.class, kcId);*/
		
		/* Hql*/
//		SqlJoinUtils sqlUtil = new SqlJoinUtils();
////		List<String> paramValues = new ArrayList<String>();
//		sqlUtil.append("  from SysKindcodeconvert kc where 1=1 ");
//		
//		sqlUtil.append(" and kc.id.codeCode = ? ");
//		sqlUtil.addParamValue(codeCode);
//		
//		sqlUtil.append(" and kc.id.codeType = ? ");
//		sqlUtil.addParamValue(codeType);
//		
//		
//		sqlUtil.append(" and kc.id.comCode = ? ");
//		sqlUtil.addParamValue(comCode);
//		
//		sqlUtil.append(" and kc.id.riskType = ? ");
//		sqlUtil.addParamValue(riskCode);
		//SysKindcodeconvert sysCodeDict = databaseDao.findUniqueByHql(SysKindcodeconvert.class, sqlUtil.getSql(), sqlUtil.getParamValues());//(sqlUtil.getSql(), paramValues.toArray());
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select *  from SYS_KINDCODECONVERT kc where 1=1 ");	
		sqlUtil.append(" and kc.codeCode = ? ");
		sqlUtil.addParamValue(codeCode);
		
		sqlUtil.append(" and kc.codeType = ? ");
		sqlUtil.addParamValue(codeType);
		
		sqlUtil.append(" and kc.comCode = ? ");
		sqlUtil.addParamValue(comCode);
		
		sqlUtil.append(" and kc.riskType = ? ");
		sqlUtil.addParamValue(riskCode);
		
		List<Object[]> sysCodeDict = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		if(sysCodeDict != null&&sysCodeDict.size()>0){
			return (String)sysCodeDict.get(0)[5];
		}
		return "";
	}

}
