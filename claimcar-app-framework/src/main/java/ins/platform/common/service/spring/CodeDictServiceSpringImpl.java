package ins.platform.common.service.spring;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.schema.SysCodeDict;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.SysCodeDictVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "codeDictService")
public class CodeDictServiceSpringImpl implements CodeDictService {

	@Autowired
	DatabaseDao databaseDao;

	/*** 缓存管理:缓存对象<key，List<SysCodeDict>> */
	private static CacheService codeDictListCache = CacheManager.getInstance("T_CodeDict_List");

	@SuppressWarnings("unchecked")
	@Override
	public List<SysCodeDictVo> findCodeListByRiskCom(String codeType,String riskCode,String comCode) {
		String cacheKey = codeType+"_"+riskCode+"_"+comCode;
		List<SysCodeDictVo> codeDictList = null;
		// 缓存处理
		codeDictList = (List<SysCodeDictVo>)codeDictListCache.getCache(cacheKey);
		if( !ObjectUtils.isEmpty(codeDictList)){
			return codeDictList;
		}
		StringBuffer queryString = new StringBuffer();
		queryString.append(" from SysCodeDict s where s.codeType = ? and isValid = ?");

		if( !ObjectUtils.isEmpty(riskCode)){
			// PrpDcodeRisk表集合
			String[] riskCodes = riskCode.split(",");
			String risk = "(";
			for(int i = 0; i<riskCodes.length; i++ ){
				risk += "'"+riskCodes[i]+"'";
				if(i!=riskCodes.length-1){
					risk += ",";
				}
			}
			risk += ")";
			String riskQueryString = "(select r.codeCode from PrpDcodeRisk r where r.codeType = "+codeType+" or r.riskCode = '0000' or r.riskCode in "+risk+" )";

			queryString.append(" and s.id.codeCode in "+riskQueryString);
		}
		if( !ObjectUtils.isEmpty(comCode)&&comCode.length()>2){
			// PrpDcodeCom表集合,深圳特别处理
			String upperComCode = comCode.substring(0,2).concat("000000");
			if(comCode.startsWith("0002")){
				upperComCode = comCode.substring(0,4).concat("0000");
			}
			String comQueryString = "(select c.codeCode from PrpDcodeCom c where c.validStatus = '1' and c.codeType = '"+codeType+"' or c.id.comCode = '"+upperComCode+"')";

			queryString.append(" and s.codeCode in "+comQueryString);

		}
		queryString.append(" order by s.serialNo ");
		List<SysCodeDict> dictList = databaseDao.findAllByHql(SysCodeDict.class,queryString.toString(),codeType,"Y");

		codeDictList = new ArrayList<SysCodeDictVo>();
		for(SysCodeDict po:dictList){
			SysCodeDictVo vo = Beans.copyDepth().from(po).to(SysCodeDictVo.class);
			codeDictList.add(vo);
		}

		codeDictListCache.putCache(cacheKey,codeDictList);
		return codeDictList;
	}

	
	@Override
	public List<SysCodeDictVo> findCodeListByQuery(String codeType,List codeList) {
		List<SysCodeDictVo> codeDictList =  new ArrayList<SysCodeDictVo>();
		StringBuffer queryString = new StringBuffer();
		queryString.append(" from SysCodeDict s where s.codeType = ? and isValid =? ");
		if(codeList!=null && codeList.size()>0){
			queryString.append(" and s.codeCode not in (?)");
		}
		queryString.append(" order by s.serialNo ");
		
		List<SysCodeDict> dictList = new ArrayList<SysCodeDict>();
		if(codeList!=null && codeList.size()>0){
			dictList = databaseDao.findAllByHql(SysCodeDict.class,queryString.toString(),codeType,"1",codeList);
		}else{
			dictList = databaseDao.findAllByHql(SysCodeDict.class,queryString.toString(),codeType,"1");	
		}
		
		for(SysCodeDict po:dictList){
			SysCodeDictVo vo = Beans.copyDepth().from(po).to(SysCodeDictVo.class);
			codeDictList.add(vo);
		}
		
		return codeDictList;
	}
	@Override
	public void clearCache(){
		codeDictListCache.clearAllCacheManager();
	}

}
