package ins.sino.claimcar.base.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.other.service.ShangHaiDataService;
import ins.sino.claimcar.other.vo.ShangHaiDataVo;

/**
 * @author huanggs
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("shangHaiDataService")
public class ShangHaiDataServiceImpl implements ShangHaiDataService {

	private static Logger logger = LoggerFactory.getLogger(ShangHaiDataServiceImpl.class);
	private String startDate = null;
	private String endDate = null;

	// 0-查询案件数量   1- 详细数据
	private  final int QUERY_COUNT = 0;
	private  final int QUERY_DETAIL = 1;

	@Autowired
	BaseDaoService baseDaoService;

	/**
	 * 根据请求类型进行对象转换
	 * 
	 * @param objects
	 * @param requestType
	 * @throws ParseException
	 */
	public List<ShangHaiDataVo> exchangeObjects(List<Object[]> objects, String requestType) throws ParseException {
		List<ShangHaiDataVo> resultVoList = new ArrayList<ShangHaiDataVo>();

		if (requestType.equals("50") || requestType.equals("80")) {
			// 报案
			for (int i = 0; i < objects.size(); i++) {
				ShangHaiDataVo shangHaiDataVo = new ShangHaiDataVo();
				Object[] obj = objects.get(i);
				shangHaiDataVo.setRegistNo(obj[0] == null ? "" : obj[0].toString());
				shangHaiDataVo.setRiskCode(obj[1] == null ? "" : obj[1].toString());
				if (obj[2] != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					shangHaiDataVo.setRegistTime(sdf.format(obj[2]));
				}
				shangHaiDataVo.setClaimSeqNo(obj[3] == null ? "" : obj[3].toString());
				resultVoList.add(shangHaiDataVo);
			}
		} else if (requestType.equals("52") || requestType.equals("82")) {
			// 结案
			for (int i = 0; i < objects.size(); i++) {
				ShangHaiDataVo shangHaiDataVo = new ShangHaiDataVo();
				Object[] obj = objects.get(i);
				shangHaiDataVo.setRegistNo(obj[0] == null ? "" : obj[0].toString());
				shangHaiDataVo.setClaimNo(obj[1] == null ? "" : obj[1].toString());
				shangHaiDataVo.setClaimSeqNo(obj[2] == null ? "" : obj[2].toString());
				shangHaiDataVo.setRiskCode(obj[3] == null ? "" : obj[3].toString());
				shangHaiDataVo.setSumPaid(obj[4] == null ? "" : obj[4].toString());
				shangHaiDataVo.setSumAmt(obj[5] == null ? "" : obj[5].toString());
				if (obj[6] != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					shangHaiDataVo.setEndCaseTime(sdf.format(obj[6]));
				}
				if (obj[7] != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					shangHaiDataVo.setEndUnderwriteTime(sdf.format(obj[7]));
				}
				resultVoList.add(shangHaiDataVo);
			}
		} else if (requestType.equals("54") || requestType.equals("83")) {
			// 注销
			for (int i = 0; i < objects.size(); i++) {
				ShangHaiDataVo shangHaiDataVo = new ShangHaiDataVo();
				Object[] obj = objects.get(i);
				shangHaiDataVo.setRegistNo(obj[0] == null ? "" : obj[0].toString());
				shangHaiDataVo.setClaimSeqNo(obj[1] == null ? "" : obj[1].toString());
				shangHaiDataVo.setRiskCode(obj[2] == null ? "" : obj[2].toString());
				shangHaiDataVo.setCancelType(obj[3] == null ? "" : obj[3].toString());
				if (obj[4] != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					shangHaiDataVo.setCancelUnderwriteTime(sdf.format(obj[4]));
				}
				resultVoList.add(shangHaiDataVo);
			}
		}
		return resultVoList;
	}

	@Override
	public List<Map<String, Object>> createExcelRecord(List<ShangHaiDataVo> results, String requestType)
			throws Exception {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (requestType.equals("50") || requestType.equals("80")) {
			if (requestType.equals("50")) {
				map.put("sheetName", "交强报案明细");
			} else if (requestType.equals("80")) {
				map.put("sheetName", "商业报案明细");
			}
			listmap.add(map);
			int num = 0;
			for (ShangHaiDataVo resultVo : results) {
				Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
				num = num + 1;
				mapValue.put("number", Integer.toString(num));
				mapValue.put("registNo", resultVo.getRegistNo());
				mapValue.put("riskCode", resultVo.getRiskCode());
				mapValue.put("registTime", resultVo.getRegistTime());
				mapValue.put("claimSeqNo", resultVo.getClaimSeqNo());
				listmap.add(mapValue);
			}
		} else if (requestType.equals("52") || requestType.equals("82")) {
			if (requestType.equals("52")) {
				map.put("sheetName", "交强结案明细");
			} else if (requestType.equals("82")) {
				map.put("sheetName", "商业结案明细");
			}
			listmap.add(map);
			int num = 0;
			for (ShangHaiDataVo resultVo : results) {
				Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
				num = num + 1;
				mapValue.put("number", Integer.toString(num));
				mapValue.put("registNo", resultVo.getRegistNo());
				mapValue.put("claimNo", resultVo.getClaimNo());
				mapValue.put("claimSeqNo", resultVo.getClaimSeqNo());
				mapValue.put("riskCode", resultVo.getRiskCode());
				mapValue.put("sumPaid", resultVo.getSumPaid());
				mapValue.put("sumAmt", resultVo.getSumAmt());
				mapValue.put("EndCaseTime", resultVo.getEndCaseTime());
				mapValue.put("EndUnderwriteTime", resultVo.getEndUnderwriteTime());
				listmap.add(mapValue);
			}
		} else if (requestType.equals("54") || requestType.equals("83")) {
			if (requestType.equals("54")) {
				map.put("sheetName", "交强注销明细");
			} else if (requestType.equals("83")) {
				map.put("sheetName", "商业注销明细");
			}
			listmap.add(map);
			int num = 0;
			for (ShangHaiDataVo resultVo : results) {
				Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
				num = num + 1;
				mapValue.put("number", Integer.toString(num));
				mapValue.put("registNo", resultVo.getRegistNo());
				mapValue.put("claimSeqNo", resultVo.getClaimSeqNo());
				mapValue.put("riskCode", resultVo.getRiskCode());
				mapValue.put("cancelType", resultVo.getCancelType());
				mapValue.put("CancelUnderwriteTime", resultVo.getCancelUnderwriteTime());
				listmap.add(mapValue);
			}
		}
		return listmap;
	}

	/**
	 * 根据日期查询上海理赔统计数据
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 */
	public List<String> findShangHaiCountData(Date startDate, Date endDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.startDate = sdf.format(startDate);
		this.endDate = sdf.format(endDate);
		StringBuilder sb = new StringBuilder();
		String[] requestType = { "50", "80", "52", "82", "54", "83", "zero_1101", "zero_1206", "amount_1101","amount_1206" };
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < requestType.length; i++) {
			//sb.append(getCountByType(startDate , endDate , requestType[i], QUERY_COUNT));
			String singleSQL = getCountByType(startDate , endDate , requestType[i], QUERY_COUNT);
			Object singleObj =  baseDaoService.findListBySql(singleSQL).get(0) ;
			if(singleObj!= null){
				String singleResult = singleObj.toString();
				result.add(singleResult);
			}
		}
		return result;
	}

	
	/**
	 * 查询详细数据 
	 * 上海请求类型(requestType)
	 * 交强报案-50,商业报案-80,交强结案-52,商业结案-82,交强注销-54,商业注销-83 
	 * @throws Exception
	 */
	public List<ShangHaiDataVo> getDetailByType(Date startDate, Date endDate, String requestType) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.startDate = sdf.format(startDate);
		this.endDate = sdf.format(endDate);
		String sql = getCountByType(startDate, endDate, requestType, QUERY_DETAIL);
		List<Object[]> objects = baseDaoService.findListBySql(sql);
		logger.info(requestType + "sheet1sheet:     " + requestType + sql);
		return exchangeObjects(objects, requestType);
	}
	
	/**
	 * 上海请求类型(requestType) 交强报案-50,商业报案-80,交强结案-52,商业结案-82,交强注销-54,商业注销-83
	 * queryType 0-查数量  1-查详细数据
	 * @throws ParseException
	 */
	public String getCountByType(Date startDate, Date endDate , String requestType, int queryType) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.startDate = sdf.format(startDate);
			this.endDate = sdf.format(endDate);

		StringBuilder sb = new StringBuilder();
		if ((requestType.equals("50") || requestType.equals("80"))) {
			return getRegistSql(requestType, queryType);
		} else if ((requestType.equals("52") || requestType.equals("82"))) {
			return getEndCaseSql(requestType, queryType);
		} else if ((requestType.equals("54") || requestType.equals("83"))) {
			return getCancelSql(requestType, queryType);
		} else if ((requestType.equals("zero_1101") || requestType.equals("zero_1206"))) {
			return getZeroEndCaseSql(requestType);
		} else if ((requestType.equals("amount_1101") || requestType.equals("amount_1206"))) {
			return getSumPaidSql(requestType);
		}
		return null;
	}

	
		
	// 获取报案sql
	private String getRegistSql(String requestType, int queryType) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		if (queryType == 0) {
			sqlJoinUtils.append(" select count(*) from (  ");
		}
		sqlJoinUtils.append(" select a.registno,b.riskcode,a.reporttime,c.claimseqno ");
		sqlJoinUtils.append(" from claimuser.prplregist a,  claimuser.prplcmain b, claimuser.ciclaimplatformlog c ");
		sqlJoinUtils.append(" where a.registno = b.registno ");
//		sqlJoinUtils.append(" and trunc(a.reporttime) >= to_date('" + startDate + "', 'yyyy-mm-dd') ");
//		sqlJoinUtils.append(" and trunc(a.reporttime) <= to_date('" + endDate + "', 'yyyy-mm-dd') ");
		sqlJoinUtils.append(" AND a.reporttime >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.reporttime < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append(" and substr(b.comcode, 0, 2) = '22'  ");
		if(requestType.equals("50")){
			sqlJoinUtils.append(" and b.riskcode = '1101' ");
		}else {
			sqlJoinUtils.append(" and b.riskcode <> '1101' ");
		}
		sqlJoinUtils.append(" and c.requesttype = '" + requestType + "' ");
		sqlJoinUtils.append(" and a.registno = c.bussno  ");
		sqlJoinUtils.append(" and c.status = '1'  ");
		if (queryType == 0) {
			sqlJoinUtils.append(" )  ");
		}
		return sqlJoinUtils.getSql();
	}

	// 获取结案sql
	private String getEndCaseSql(String requestType, int queryType) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		if (queryType == 0) {
			sqlJoinUtils.append(" select count(*) from ( ");
		}
		sqlJoinUtils.append(" select b.registno,b.claimno,c.claimseqno,b.riskcode, b.sumamt+b.SumFee sumpaid,b.sumamt,a.endcasedate,c.requesttime ");
		sqlJoinUtils.append(" from (select * from prplendcase t where t.endcasedate = (SELECT min(q.endcasedate) FROM prplendcase q WHERE q.claimno = t.claimno)) a,claimuser.prplcompensate b,claimuser.ciclaimplatformlog c ");
		sqlJoinUtils.append(" where  a.compensateno = b.compensateno ");
//		sqlJoinUtils.append(" and trunc(a.endcasedate) >= to_date('" + startDate + "', 'yyyy-mm-dd') ");
//		sqlJoinUtils.append(" and trunc(a.endcasedate) <= to_date('" + endDate + "', 'yyyy-mm-dd') ");
		sqlJoinUtils.append(" AND a.endcasedate >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.endcasedate < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append(" and substr(b.comcode, 0, 2) = '22' ");
		if(requestType.equals("52")){
			sqlJoinUtils.append(" and b.riskcode = '1101' ");
		}else {
			sqlJoinUtils.append(" and b.riskcode <> '1101' ");
		}
		sqlJoinUtils.append(" and c.requesttype = '" + requestType + "' ");
		sqlJoinUtils.append(" and a.claimno = c.bussno ");
		sqlJoinUtils.append(" and c.status = '1' ");
		sqlJoinUtils.append(" and b.sumamt > 0 ");
		if (queryType == 0) {
			sqlJoinUtils.append(" )  ");
		}
		return sqlJoinUtils.getSql();
	}

	// 获取注销sql
	private String getCancelSql(String requestType, int queryType) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		if (queryType == 0) {
			sqlJoinUtils.append(" select count(*) from (  ");
		}
		sqlJoinUtils.append(" select a.claimno, b.claimseqno, a.riskcode, b.requestname cancelType, b.requesttime  ");
		sqlJoinUtils.append(" from claimuser.prplclaim a, claimuser.ciclaimplatformlog b ");
		sqlJoinUtils.append(" where a.validflag = '0' ");
//		sqlJoinUtils.append(" and trunc(a.canceltime) >= to_date('" + startDate + "', 'yyyy-mm-dd')  ");
//		sqlJoinUtils.append(" and trunc(a.canceltime) <= to_date('" + endDate + "', 'yyyy-mm-dd')  ");
		sqlJoinUtils.append(" AND a.canceltime >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.canceltime < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append("  and substr(a.comcode, 0, 2) = '22' ");
		if(requestType.equals("54")){
			sqlJoinUtils.append(" and a.riskcode = '1101' ");
		}else {
			sqlJoinUtils.append(" and a.riskcode <> '1101' ");
		}
		sqlJoinUtils.append(" and a.registno = b.bussno ");
		sqlJoinUtils.append(" and b.requesttype = '" + requestType + "' ");
		sqlJoinUtils.append(" and b.status = 1 ");
		sqlJoinUtils.append(" union all ");
		sqlJoinUtils.append(" SELECT a.registno, c.claimseqno, b.riskcode, c.requestname, c.requesttime  ");
		sqlJoinUtils.append(" FROM claimuser.prplregist  a, claimuser.prplcmain  b,  claimuser.ciclaimplatformlog c  ");
		sqlJoinUtils.append(" WHERE a.canceltime is not null  ");
//		sqlJoinUtils.append(" and trunc(a.canceltime) >= to_date('" + startDate + "', 'yyyy-mm-dd')  ");
//		sqlJoinUtils.append(" and trunc(a.canceltime) <= to_date('" + endDate + "', 'yyyy-mm-dd')  ");
		sqlJoinUtils.append(" AND a.canceltime >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.canceltime < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append(" and substr(a.comcode, 0, 2) = '22' ");
		sqlJoinUtils.append(" and a.registno = b.registno  ");
		if(requestType.equals("54")){
			sqlJoinUtils.append(" and b.riskcode = '1101' ");
		}else {
			sqlJoinUtils.append(" and b.riskcode <> '1101' ");
		}
		sqlJoinUtils.append(" and a.registno = c.bussno ");
		sqlJoinUtils.append(" and c.requesttype = '" + requestType + "' ");
		sqlJoinUtils.append("  and c.status = '1' ");
		if (queryType == 0) {
			sqlJoinUtils.append(" )  ");
		}
		return sqlJoinUtils.getSql();
	}

	// 获取0结案sql
	private String getZeroEndCaseSql(String requestType) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();	
		sqlJoinUtils.append(" select count(*)  from ( ");
		sqlJoinUtils.append(" select c.claimseqno,b.riskcode, b.sumamt+b.SumFee sumpaid,b.sumamt,a.endcasedate,c.requesttime ");
		sqlJoinUtils.append(" from (select * from prplendcase t where t.endcasedate = (SELECT min(q.endcasedate) FROM prplendcase q WHERE q.claimno = t.claimno)) a,claimuser.prplcompensate b,claimuser.ciclaimplatformlog c ");
		sqlJoinUtils.append(" where  a.compensateno = b.compensateno ");

//		sqlJoinUtils.append(" and trunc(a.endcasedate) >= to_date('" + startDate + "', 'yyyy-mm-dd') ");
//		sqlJoinUtils.append(" and trunc(a.endcasedate) <= to_date('" + endDate + "', 'yyyy-mm-dd') ");
		sqlJoinUtils.append(" AND a.endcasedate >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.endcasedate < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append(" and substr(b.comcode, 0, 2) = '22' ");
		if(requestType.equals("zero_1101")){
			sqlJoinUtils.append(" and a.riskcode = '1101' ");
			sqlJoinUtils.append(" and c.requesttype = '52' ");
		}else {
			sqlJoinUtils.append(" and a.riskcode <> '1101' ");
			sqlJoinUtils.append(" and c.requesttype = '82' ");
		}
		sqlJoinUtils.append(" and a.claimno = c.bussno ");
		sqlJoinUtils.append(" and c.status = '1' ");
		sqlJoinUtils.append(" and b.sumamt = 0  ");
		sqlJoinUtils.append(" ) ");
		return sqlJoinUtils.getSql();
	}

	// 获取赔款金额sql
	private String getSumPaidSql(String requestType) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		sqlJoinUtils.append(" select sum(b.sumamt + b.SumFee) sumpaid ");
		sqlJoinUtils.append(" from (select * from prplendcase t  where t.endcasedate =  ( ");
		sqlJoinUtils.append(" SELECT min(q.endcasedate)  FROM prplendcase q  WHERE q.claimno = t.claimno)) a, claimuser.prplcompensate b ");
		sqlJoinUtils.append(" where a.compensateno = b.compensateno ");
//		sqlJoinUtils.append(" and trunc(a.endcasedate) >= to_date('" + startDate + "', 'yyyy-mm-dd')  ");
//		sqlJoinUtils.append(" and trunc(a.endcasedate) <= to_date('" + endDate + "', 'yyyy-mm-dd') ");
		sqlJoinUtils.append(" AND a.endcasedate >= TRUNC(TO_DATE( '" + startDate + "', 'yyyy-mm-dd' )) ");
		sqlJoinUtils.append(" AND a.endcasedate < TRUNC(TO_DATE( '" + endDate + "', 'yyyy-mm-dd' )+1) ");
		sqlJoinUtils.append(" and substr(b.comcode, 0, 2) = '22' ");
		if(requestType.equals("amount_1101")){
			sqlJoinUtils.append(" and a.riskcode = '1101' ");
		}else {
			sqlJoinUtils.append(" and a.riskcode <> '1101' ");
		}
		return sqlJoinUtils.getSql();
	}
}
