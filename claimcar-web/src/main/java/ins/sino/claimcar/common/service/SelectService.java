package ins.sino.claimcar.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ins.framework.dao.database.DatabaseDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="selectService")
public class SelectService {

	@Autowired
	private DatabaseDao databaseDao;
	
	public Map<String,String> findUserCode(String comCode,String userInfo,String gradeId){
		
		Map<String,String> resultMap = new HashMap<String,String>();
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		querySb.append(" SELECT userCode,userName FROM ywuser.PrpDuser WHERE 1=1");
		if(StringUtils.isNotBlank(userInfo)){
			querySb.append("  AND (userCode like ? OR userName like ?)");
			paramValues.add("%"+userInfo+"%");
			paramValues.add("%"+userInfo+"%");
		}
		if(StringUtils.isNotBlank(comCode)){
			if(comCode.startsWith("0000")){//总公司
				comCode = comCode.substring(0, 2);//总公司的可以查出深圳分公司的人员
			}else if(comCode.startsWith("00")){//深圳分公司和其他总部机构
				comCode = comCode.substring(0, 4);
			}else{
				comCode = comCode.substring(0, 2);
			}
			querySb.append("  AND comCode like ? ");
			paramValues.add(comCode+"%");
		}
		if(StringUtils.isNotBlank(gradeId)){
			querySb.append("  AND userCode in (SELECT usercode FROM saa_usergrade WHERE gradeid = ?)");
			paramValues.add(gradeId);
		}
		
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());

		for(Object[] result:objList){
			resultMap.put((String)result[0],(String)result[1]);
		}
		return resultMap;
	}

}
