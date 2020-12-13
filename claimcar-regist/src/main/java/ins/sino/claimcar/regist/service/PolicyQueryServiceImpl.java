/******************************************************************************
 * CREATETIME : 2015年11月18日 下午3:26:39
 ******************************************************************************/
package ins.sino.claimcar.regist.service;

import com.google.gson.Gson;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.ReadConfigUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SqlParamVo;
import ins.sino.claimcar.regist.po.PrpCInsured;
import ins.sino.claimcar.regist.po.PrpCItemCar;
import ins.sino.claimcar.regist.po.PrpCMain;
import ins.sino.claimcar.regist.po.PrpCMainSub;
import ins.sino.claimcar.regist.vo.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import ins.platform.utils.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 报案时保单查询，关联保单查询服务类
 * @author ★LiuPing
 * @CreateTime 2015年11月18日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path(value = "policyQueryService")
public class PolicyQueryServiceImpl implements PolicyQueryService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private BaseDaoService baseDaoService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;


	private static Logger logger = LoggerFactory.getLogger(PolicyQueryServiceImpl.class);
	
	/**
	 * 判断保单查询条数是否大于100条
	 */
	public boolean calculateResultCount(PolicyInfoVo policyInfoVo,String propertyName){
		Date date = new Date();
		List<Object> paramValues = new ArrayList<Object>();
		int resultCount = ReadConfigUtils.getResultCount(propertyName);
		StringBuffer queryString = new StringBuffer(" select count(pm.policyno) from YWUSER.PRPCMAIN pm, YWUSER.PRPCITEMCAR pc"
				+ " WHERE  pm.policyno = pc.policyno ");
		conditionQuery(policyInfoVo,paramValues,queryString);
		queryString.append(" and rownum<= ?");
		paramValues.add(resultCount);
		logger.info("保单条数查询sql=" + queryString.toString());
		logger.info("开始执行保单查询方法,耗时" + (System.currentTimeMillis()-date.getTime()));
		long count = baseDaoService.getCountBySql(queryString.toString(),paramValues.toArray());
		logger.info("结束执行保单查询方法，耗时" + (System.currentTimeMillis()-date.getTime()));
		if(count == ReadConfigUtils.getResultCount(propertyName)){//大于100条数据
			return false;
		}
		return true; 
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#findPrpCMainForPage
	 * (ins.sino.claimcar.regist.vo.PolicyInfoVo, int, int)
	 */
	@Override
	public Page<PolicyInfoVo> findPrpCMainForPage(PolicyInfoVo policyInfoVo,int start,int pageSize) throws Exception {
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
		// sql查询语句
		StringBuffer queryString = new StringBuffer(
				" select * from (" 
				+ "select pm.policyNo,pc.licenseNo,pm.insuredName,pc.frameNo,pc.engineNo,pc.brandName, pm.comCode,pm.startDate startDate, "
				+ " to_char(startdate,'YYYYMMdd') || ' ' || decode(length(startHour),1,'0','') || startHour startHour,"); // ,pc.*,pi.*
		queryString.append(" (select (case when ps.policyNo = pm.policyNo then ps.mainpolicyno else ps.policyNo   end) mainpolicyno FROM PrpCMainSub ps WHERE (ps.mainpolicyno = pm.policyNo or ps.policyNo = pm.policyNo) and rownum = 1) relatedPolicyNo,");
		queryString.append(" nvl((select ph.validdate "
				+ " from YWUSER.PRPPHEAD ph "
				+ " where ph.policyno = pm.policyno "
				+ " and ph.endortype like '%37%' "
				+ " and rownum = 1),pm.enddate) endDate, ");
		queryString.append(" nvl((select to_char(ph.validdate,'YYYYMMdd') ||  decode(length(ph.validhour),1,'0','')|| ph.validhour "+" from YWUSER.PRPPHEAD ph "+" where ph.policyno = pm.policyno "+"  and ph.endortype like '%37%' "+"  and rownum = 1),to_char(pm.enddate,'YYYYMMdd') || ' ' || decode(length(pm.endhour),1,'0','') || pm.endhour) endDateHour, ");
		queryString.append("(case  when pm.underwriteflag <> '1' and pm.underwriteflag <> '3' then '0' ");
		queryString.append("when substr(pm.othflag, 3, 1) = '1' and ");
		queryString.append(" nvl((select  to_char(ph.validdate,'YYYYMMdd') || decode(length(ph.validhour),1,'0','') || ph.validhour"
				+" from YWUSER.PRPPHEAD ph"+" where ph.policyno = pm.policyno "
				+"and (ph.endortype like '%21%' or ph.endortype like '%76%') and rownum = 1), '0') <= ? ");
		paramValues.add(formatter.format(policyInfoVo.getDamageTime()));
		queryString.append("then '3' ");
		queryString.append("when substr(pm.othflag, 6, 1) = '1' and ");
		queryString
				.append("nvl((select  to_char(ph.validdate,'YYYYMMdd') ||  decode(length(ph.validhour),1,'0','')|| ph.validhour "+"from YWUSER.PRPPHEAD ph "+"where ph.policyno = pm.policyno "+"and ph.endortype like '%37%' "+"and rownum = 1),"+" to_char(pm.enddate,'YYYY-MM-dd') || decode(length(pm.endhour),1,'0','')|| pm.endhour) <= ? ");
		paramValues.add(formatter.format(policyInfoVo.getDamageTime()));
		queryString.append("then '4' ");
		queryString.append("when (to_char(pm.startdate,'YYYYMMdd') || decode(length(pm.starthour),1,'0','') || pm.starthour) > ? then '2' ");
		paramValues.add(formatter.format(policyInfoVo.getDamageTime()));
		queryString
				.append("when (select count(*) from ywuser.PrppheadSub phs where phs.endorseno in"+" (select endorseno from (select ph.* from YWUSER.PRPPHEAD ph where ph.endortype in ('74', '75') order by ph.inputtime desc) ph1 "+" where ph1.policyno = pm.policyno "+" and (ph1.endortype <> '74' or "+" (ph1.endortype = '74' and rownum = 1)))"+" and '"+formatter
						.format(policyInfoVo.getDamageTime())+"' >= to_char(phs.pausedate,'YYYYMMdd') || decode(length(phs.pausehour),1,'0','') || phs.pausehour "
								+ "and '"+formatter.format(policyInfoVo.getDamageTime())+"' < to_char(phs.comebackdate,'YYYYMMdd') || decode(length(phs.comebackhour),1,'0','') || phs.comebackhour) > 0 then '5' ");
		queryString
				.append(" when nvl((select  to_char(ph.validdate,'YYYYMMdd') ||  decode(length(ph.validhour),1,'0','')|| ph.validhour "+" from YWUSER.PRPPHEAD ph "+" where ph.policyno = pm.policyno "+"  and ph.endortype like '%37%' "+"  and rownum = 1), to_char(pm.enddate,'YYYYMMdd') || decode(length(pm.endhour),1,'0','') || pm.endhour) >= ? then '1' ");
		paramValues.add(formatter.format(policyInfoVo.getDamageTime()));
		queryString.append("else '0' end) vaildFlag,");
		queryString.append(" decode(pm.riskcode,'1101','交强','商业') riskType, pm.underWriteFlag ");
		queryString.append("from YWUSER.PRPCMAIN pm, YWUSER.PRPCITEMCAR pc");
		queryString.append(" WHERE pm.policyno = pc.policyno ");
		conditionQuery(policyInfoVo,paramValues,queryString);
		queryString.append(")");
		
		if("1".equals(policyInfoVo.getOnlyValid())){
			queryString.append("  where vaildFlag in ('1','3') ");
			queryString.append(" AND replace(endDateHour,' ','')  >= ? ");
			paramValues.add(formatter.format(policyInfoVo.getDamageTime()));
		}
		queryString.append(" Order By startDate Desc ");
		logger.info("保单查询的sql=" + queryString.toString());
		// 执行查询
		Date date = new Date();
		logger.info("开始执行保单查询方法,耗时" + (System.currentTimeMillis()-date.getTime()));
		Page<Object[]> page = baseDaoService.pagedSQLQuery(queryString.toString(),start,pageSize,paramValues.toArray());
		logger.info("结束执行保单查询方法，耗时" + (System.currentTimeMillis()-date.getTime()));
		
		List<PolicyInfoVo> policyInfoVos = new ArrayList<PolicyInfoVo>();
		if(page!=null&&page.getTotalCount()>0){
			for(Object[] obj:page.getResult()){
				PolicyInfoVo policyInfo = new PolicyInfoVo();
				policyInfo.setPolicyNo((obj[0] != null?obj[0].toString():null));
				policyInfo.setLicenseNo((obj[1] != null?obj[1].toString():null));
				policyInfo.setInsuredName((obj[2] != null?obj[2].toString():null));
				policyInfo.setFrameNo((obj[3] != null?obj[3].toString():null));
				policyInfo.setEngineNo((obj[4] != null?obj[4].toString():null));
				policyInfo.setBrandName((obj[5] != null?obj[5].toString():null));
				policyInfo.setComCode((obj[6] != null?obj[6].toString():null));
				policyInfo.setStartDate((obj[7] != null?DateUtils.strToDate(obj[7].toString(),DateUtils.YToDay):null));
				policyInfo.setStartDateHour((obj[8] != null?formatter.parse(obj[8].toString()):null));
				policyInfo.setRelatedPolicyNo((obj[9] != null?obj[9].toString():null));
				policyInfo.setEndDate((obj[10] != null?DateUtils.strToDate(obj[10].toString(),DateUtils.YToDay):null));
				policyInfo.setEndDateHour((obj[11] != null?formatter.parse(obj[11].toString()):null));
				policyInfo.setValidFlag((obj[12] != null?obj[12].toString():null));
				policyInfo.setRiskType((obj[13] != null?obj[13].toString():null));
				policyInfoVos.add(policyInfo);
			}
		}
		Page pageReturn = new Page(start,page.getTotalCount(),page.getPageSize(),policyInfoVos);
		return pageReturn; 
	}
	
	/**
	 * <pre>条件查询公共方法</pre>
	 * @param policyInfoVo
	 * @param paramValues
	 * @param queryString
	 * @modified:
	 * ☆XiaoHuYao(2019年1月28日 下午6:29:57): <br>
	 */
	private void conditionQuery(PolicyInfoVo policyInfoVo,List<Object> paramValues,StringBuffer queryString) {
		// 根据页面查询条件的单选，拼接语句的查询条件
		switch (Integer.valueOf(policyInfoVo.getCheckFlag())) {
		case 1:// 保单号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getPolicyNo().isEmpty()){
				if(policyInfoVo.getPolicyNo().trim().length()<=7&&policyInfoVo.getPolicyNo().trim().length()>=4){
					queryString.append(" AND reverse(pm.policyNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getPolicyNo())+"%");
				}else{
					queryString.append(" AND pm.policyNo = ?");
					paramValues.add(policyInfoVo.getPolicyNo().trim());
				}
			}
			break; 
		case 2:// 车牌号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getLicenseNo().trim().isEmpty()){
				if(policyInfoVo.getLicenseNo().trim().length()>=4&&policyInfoVo.getLicenseNo().trim().length()<7){
					queryString.append(" AND reverse(pc.licenseNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getLicenseNo().trim())+"%");

					/*
					 * queryString.append(" AND pc.licenseNo LIKE ?");
					 * //去除左边%才能够用索引
					 * paramValues.add(policyInfoVo.getLicenseNo().trim()+"%");
					 */
				}else{
					queryString.append(" AND pc.licenseNo = ?");
					paramValues.add(policyInfoVo.getLicenseNo().trim());
				}
			}
			break;
		case 3:// 被保险人查询
			if( !policyInfoVo.getInsuredName().trim().isEmpty()){
				queryString.append(" AND pm.insuredName LIKE ? ");
				paramValues.add(policyInfoVo.getInsuredName().trim()+"%");
			}
			break;
		case 4:// 发动机号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getEngineNo().trim().isEmpty()){
				if(policyInfoVo.getEngineNo().trim().length()>=4&&policyInfoVo.getEngineNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.engineNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getEngineNo())+"%");
				}else{
					queryString.append(" AND pc.engineNo = ?");
					paramValues.add(policyInfoVo.getEngineNo().trim());
				}
			}
			break;
		case 5:// 车架号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getFrameNo().trim().isEmpty()){
				if(policyInfoVo.getFrameNo().trim().length()>=4&&policyInfoVo.getFrameNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.frameNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getFrameNo())+"%");
				}else{
					queryString.append(" AND pc.frameNo = ?");
					paramValues.add(policyInfoVo.getFrameNo().trim());
				}
				if(policyInfoVo.getComCode()!=null){
					queryString.append(" and pm.comCode like ? ");
					paramValues.add(policyInfoVo.getComCode().substring(0,2)+"%");
				}

			}
			break;
		case 6:// vin号（车架号）查询，支持精准或后4~7为查询//改成跟车架号一样
			/*
			 * if (!policyInfoVo.getVinNo().isEmpty()) { if
			 * (policyInfoVo.getVinNo().length() >= 4 &&
			 * policyInfoVo.getVinNo().length() <= 7) {
			 * queryString.append(" AND pc.vinNo LIKE ?"); paramValues.add("%" +
			 * policyInfoVo.getVinNo()); } else {
			 * queryString.append(" AND pc.vinNo = ?");
			 * paramValues.add(policyInfoVo.getVinNo()); } } break;
			 */
			if( !policyInfoVo.getVinNo().trim().isEmpty()){
				if(policyInfoVo.getVinNo().trim().length()>=4&&policyInfoVo.getVinNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.frameNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getVinNo())+"%");
				}else{
					queryString.append(" AND pc.frameNo = ?");
					paramValues.add(policyInfoVo.getVinNo().trim());
				}
			}
			break;
		case 7:// 被保险人身份证号查询
			if( !policyInfoVo.getInsuredIdNo().trim().isEmpty()){
				queryString.append(" AND exists (select 1 from YWUSER.PRPCINSURED pi where  pm.policyno = pi.policyno  AND pi.INSUREDFLAG = 1");
				queryString.append(" AND pi.identifyNumber = ? )");
				paramValues.add(policyInfoVo.getInsuredIdNo().trim());
			}
			break;
		}
		// //查询二级机构
		// queryString.append(" AND pc.otherNature like pm.comcode?%");
		// paramValues.add(policyInfoVo.getLicenseColor());
		// 车牌底色查询，非必填项
		// if( !policyInfoVo.getLicenseColor().isEmpty()){
		if( !StringUtils.isBlank(policyInfoVo.getLicenseColor())){

			queryString.append(" AND pc.licenseColorCode = ?");
			paramValues.add(policyInfoVo.getLicenseColor());
		}
	}

	private Page assemblyPolicyInfo(Page page,PolicyInfoVo policyInfoVo) {

		// 循环查询结果集合
		for(int i = 0; i<page.getResult().size(); i++ ){

			// 获取当前序号下的保单相关对象数组
			Object[] obj = (Object[])page.getResult().get(i);

			if(obj[0]!=null&&obj[1]!=null){

				PolicyInfoVo plyVo = new PolicyInfoVo();

				// 将获取的承保主表信息，复制到PolicyInfoVo对象中
				PrpCMain pm = (PrpCMain)obj[0];
				Beans.copy().from(pm).to(plyVo);

				// 将获取的标的车表信息，复制到PolicyInfoVo对象中
				PrpCItemCar pc = (PrpCItemCar)obj[1];
				Beans.copy().from(pc).to(plyVo);

				String relatedPolicyNo = getRelatedPolicyNo(plyVo.getPolicyNo());
				plyVo.setRelatedPolicyNo(relatedPolicyNo);

				// 当保单为1-核保通过，3-自动核保通过，判定保单有效，之后通过出险时间进行判断
				if(( "1".equals(plyVo.getUnderWriteFlag())||"3".equals(plyVo.getUnderWriteFlag()) )){

					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
					String dateString = formatter.format(plyVo.getEndDate());
					String startDateString = formatter.format(plyVo.getStartDate());
					String headDateString = "";
					List<PrppheadVo> vos = registQueryService.findByPolicyNo(plyVo.getPolicyNo());// 获取批单

					// 开始
					List<String> endorTypeList = new ArrayList<String>();
					List<String> endorseNo = new ArrayList<String>();
					endorTypeList.add("74");// 停驶延期批改
					endorTypeList.add("75");// 恢复停驶批改
					// List<PrppheadVo> prppheadVoList2 = new
					// ArrayList<PrppheadVo>();
					List<PrppheadVo> prppheadVoList1 = registQueryService.findByOther(plyVo.getPolicyNo(),endorTypeList,"inputTime");
					for(int k = 0; k<prppheadVoList1.size(); k++ ){
						if("75".equals(prppheadVoList1.get(k).getEndorType())&&k!=0){// 有恢复
							// prppheadVoList2.remove(k-1);
							endorseNo.remove(k-1);// 去除停驶的
						}
						endorseNo.add(prppheadVoList1.get(k).getEndorseNo());
						// prppheadVoList2.add(prppheadVoList1.get(k));
					}

					List<PrppheadSubVo> headSubVoList = registQueryService.findPrppheadSubVoByOther(plyVo.getPolicyNo(),endorseNo);
					// List<PrppheadSubVo> headSubVoList =
					// registQueryService.findPrppheadSubByPolicyNo(plyVo.getPolicyNo());
					// 停驶区间
					String pauseFlag = "0";
					for(PrppheadSubVo vo:headSubVoList){
						String pauseDateString = vo.getPauseDates();
						String comebackDateString = vo.getComebackDates();
						pauseDateString = pauseDateString.replace("-","");
						comebackDateString = comebackDateString.replace("-","");
						if(vo.getPauseHours().length()>1){
							pauseDateString = pauseDateString.substring(0,8)+vo.getPauseHours()+"0000";
						}else{
							pauseDateString = pauseDateString.substring(0,8)+"0"+vo.getPauseHours()+"0000";
						}

						if(vo.getComebackHours().length()>1){
							comebackDateString = comebackDateString.substring(0,8)+vo.getComebackHours()+"0000";
						}else{
							comebackDateString = comebackDateString.substring(0,8)+"0"+vo.getComebackHours()+"0000";
						}
						long pauseDate = 0;
						long comebackDate = 0;
						try{
							Date pauseDates = formatter.parse(pauseDateString);
							pauseDate = pauseDates.getTime();// 停驶开始时间
							Date comebackDates = formatter.parse(comebackDateString);
							comebackDate = comebackDates.getTime();// 复驶开始时间
							long damageTime = policyInfoVo.getDamageTime().getTime();
							if(damageTime>=pauseDate&&damageTime<comebackDate){// 属于停驶区间
								pauseFlag = "1";
							}

						}catch(ParseException e){
							logger.error("保单查询筛选出错",e);
						}

					}

					String endorType = "0";
					// 没有批单的情况
					if(vos!=null&&vos.size()>0){
						// 退保日期
						for(PrppheadVo vo:vos){
							if(vo.getEndorType().length()>2){
								String a[] = vo.getEndorType().split(",");
								for(int j = 0; j<a.length; j++ ){
									if("21".equals(a[j])||"76".equals(a[j])){
										headDateString = formatter.format(vo.getValidDate());
										if(vo.getValidHour().toString().length()>1){
											headDateString = headDateString.substring(0,8)+vo.getValidHour().toString()+"0000";
											break;
										}else{
											headDateString = headDateString.substring(0,8)+"0"+vo.getValidHour().toString()+"0000";
											break;
										}
									}
								}
							}else{
								if("21".equals(vo.getEndorType())||"76".equals(vo.getEndorType())){
									headDateString = formatter.format(vo.getValidDate());
									if(vo.getValidHour().toString().length()>1){
										headDateString = headDateString.substring(0,8)+vo.getValidHour().toString()+"0000";
									}else{
										headDateString = headDateString.substring(0,8)+"0"+vo.getValidHour().toString()+"0000";
									}
								}
							}
							if(StringUtils.isNotBlank(headDateString)){
								break;
							}
						}

						// 终保日期
						for(PrppheadVo vo:vos){
							String endDateFlags = "0";
							if(vo.getEndorType().length()>2){
								String a[] = vo.getEndorType().split(",");
								for(int j = 0; j<a.length; j++ ){
									if("37".equals(a[j])){
										dateString = formatter.format(vo.getValidDate());
										if(vo.getValidHour().toString().length()>1){
											dateString = dateString.substring(0,8)+vo.getValidHour().toString()+"0000";
											plyVo.setEndDate(vo.getValidDate());
											endDateFlags = "1";
											break;
										}else{
											dateString = dateString.substring(0,8)+"0"+vo.getValidHour().toString()+"0000";
											plyVo.setEndDate(vo.getValidDate());
											endDateFlags = "1";
											break;
										}
									}else{
										if(plyVo.getEndHour().toString().length()>1){
											dateString = dateString.substring(0,8)+plyVo.getEndHour().toString()+"0000";
										}else{
											dateString = dateString.substring(0,8)+"0"+plyVo.getEndHour().toString()+"0000";
										}
									}
								}
							}else{
								if("37".equals(vo.getEndorType())){
									dateString = formatter.format(vo.getValidDate());
									if(vo.getValidHour().toString().length()>1){
										dateString = dateString.substring(0,8)+vo.getValidHour().toString()+"0000";
										plyVo.setEndDate(vo.getValidDate());
										endDateFlags = "1";
										break;
									}else{
										dateString = dateString.substring(0,8)+"0"+vo.getValidHour().toString()+"0000";
										plyVo.setEndDate(vo.getValidDate());
										endDateFlags = "1";
										break;
									}
								}else{
									if(plyVo.getEndHour().toString().length()>1){
										dateString = dateString.substring(0,8)+plyVo.getEndHour().toString()+"0000";
									}else{
										dateString = dateString.substring(0,8)+"0"+plyVo.getEndHour().toString()+"0000";
									}
								}
							}
							if("1".equals(endDateFlags)){
								break;
							}
						}
					}else{// 没有批单的情况下取prpcmain表
						if(plyVo.getEndHour().toString().length()>1){
							dateString = dateString.substring(0,8)+plyVo.getEndHour().toString()+"0000";
						}else{
							dateString = dateString.substring(0,8)+"0"+plyVo.getEndHour().toString()+"0000";
						}
						// dateString = dateString.substring(0, 8)+"235959";
					}

					if(plyVo.getStartHour().toString().length()>1){
						startDateString = startDateString.substring(0,8)+plyVo.getStartHour().toString()+"0000";
					}else{
						startDateString = startDateString.substring(0,8)+"0"+plyVo.getStartHour().toString()+"0000";
					}

					long endDate = 0;
					long startDate = 0;
					long headDate = 0;
					try{
						Date a = formatter.parse(dateString);
						endDate = a.getTime();
						plyVo.setEndDateHour(a);
						Date b = formatter.parse(startDateString);
						startDate = b.getTime();
						plyVo.setStartDateHour(b);
						if(StringUtils.isNotBlank(headDateString)){
							Date c = formatter.parse(headDateString);
							headDate = c.getTime();
						}

					}catch(ParseException e){
						logger.error("保单查询筛选出错",e);
					}// 毫秒
					if(StringUtils.isNotBlank(headDateString)){
						if(headDate>policyInfoVo.getDamageTime().getTime()){// 退保日期
							endorType = "1";// 可以报案
						}
					}
					if("1".equals(plyVo.getOthFlag().substring(2,3))&&( "0".equals(endorType) )){
						plyVo.setValidFlag("3");// 已退保
					}else if("1".equals(plyVo.getOthFlag().substring(5,6))&&( policyInfoVo.getDamageTime().getTime()>endDate )){
						plyVo.setValidFlag("4");// 已终保
						// }else
						// if(policyInfoVo.getDamageTime().getTime()<plyVo.getStartDate().getTime()){
					}else if(policyInfoVo.getDamageTime().getTime()<startDate){
						plyVo.setValidFlag("2");// 未起保
					}else if(pauseFlag.equals("1")){
						plyVo.setValidFlag("5");// 停驶状态
					}else if(( endDate>policyInfoVo.getDamageTime().getTime() )||( policyInfoVo.getDamageTime().getTime()==endDate )){
						plyVo.setValidFlag("1");// 有效
					}else{
						plyVo.setValidFlag("0");// 无效
					}
				}else{// UnderWriteFlag不是1或3时直接判定保单无效
					plyVo.setValidFlag("0");
				}
				String riskCode = pm.getRiskCode();
				if(riskCode.equals("1101")){
					plyVo.setRiskType("交强");
				}else{
					plyVo.setRiskType("商业");
				}

				// 仅查询有效保单
				if("1".equals(policyInfoVo.getOnlyValid())&& !"1".equals(plyVo.getValidFlag())&& !"3".equals(plyVo.getValidFlag())){
					page.getResult().remove(i);
					i-- ;
				}else{
					page.getResult().set(i,plyVo);
				}
			}
		}
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#getRelatedPolicyNo
	 * (java.lang.String)
	 */
	@Override
	public String getRelatedPolicyNo(String policyNo) {
		String mainSubSql = "FROM PrpCMainSub ps WHERE ps.id.mainpolicyno = ? or ps.id.policyNo = ? ";
		List<Object> paramValues = new ArrayList<Object>();
		paramValues.add(policyNo);
		List<PrpCMainSub> prpCMainSubList = databaseDao.findAllByHql(PrpCMainSub.class,mainSubSql,policyNo,policyNo);

		if(prpCMainSubList!=null&&prpCMainSubList.size()>0){
			PrpCMainSub cmainSub = prpCMainSubList.get(0);// 只有一条记录
			if(policyNo.equals(cmainSub.getId().getPolicyNo())){
				return cmainSub.getId().getMainpolicyno();
			}else{
				return cmainSub.getId().getPolicyNo();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#findPrpcMainByPolicyNos
	 * (java.util.List)
	 */
	@Override
	public List<PrpLCMainVo> findPrpcMainByPolicyNos(List<String> policyNoList) {
		List<PrpLCMainVo> voList = new ArrayList<PrpLCMainVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("policyNo",policyNoList);
		List<PrpCMain> list = databaseDao.findAll(PrpCMain.class,queryRule);

		// 迭代保单信息，深度拷贝无法将子表主键中的policyNo复制到抄单表，所以单独处理
		for(PrpCMain prpCMain:list){
			// 将保单信息拷贝到抄单表中
			PrpLCMainVo prpCMainVo = Beans.copyDepth().from(prpCMain).to(PrpLCMainVo.class);
			// 单独处理保单号
			if(prpCMainVo.getPrpCInsureds().size()>0){
				for(int i = 0; i<prpCMainVo.getPrpCInsureds().size(); i++ ){
					prpCMainVo.getPrpCInsureds().get(i).setPolicyNo(prpCMain.getPrpCInsureds().get(i).getId().getPolicyNo());
				}
			}
			// 单独处理保单号和序号
			if(prpCMainVo.getPrpCItemCars().size()>0){
				for(int i = 0; i<prpCMainVo.getPrpCItemCars().size(); i++ ){
					prpCMainVo.getPrpCItemCars().get(i).setPolicyNo(prpCMain.getPrpCItemCars().get(i).getId().getPolicyNo());
					prpCMainVo.getPrpCItemCars().get(i).setItemNo(prpCMain.getPrpCItemCars().get(i).getId().getItemNo());
					prpCMainVo.getPrpCItemCars().get(i).setCarCheckReason(prpCMain.getPrpCItemCars().get(i).getCarcheckReason());
				}
			}
			// 单独处理保单号和序号
			if(prpCMainVo.getPrpCItemKinds().size()>0){
				for(int i = 0; i<prpCMainVo.getPrpCItemKinds().size(); i++ ){
					PrpLCItemKindVo prpLCItemKindVo = prpCMainVo.getPrpCItemKinds().get(i);
					prpLCItemKindVo.setPolicyNo(prpCMain.getPrpCItemKinds().get(i).getId().getPolicyNo());
					prpLCItemKindVo.setItemKindNo(prpCMain.getPrpCItemKinds().get(i).getId().getItemKindNo());
					if( !"1101".equals(prpCMainVo.getRiskCode())){// 商业险
						prpLCItemKindVo.setNoDutyFlag("0");
						if( !"Y".equals(prpLCItemKindVo.getCalculateFlag())){
							if( !prpLCItemKindVo.getKindCode().endsWith("M")&&prpLCItemKindVo.getFlag().length()>4&&"1".equals(prpLCItemKindVo
									.getFlag().substring(4,5))){
								prpLCItemKindVo.setNoDutyFlag("1");
							}
						}else{// 主险
							if(prpLCItemKindVo.getFlag().length()>4&&"1".equals(prpLCItemKindVo.getFlag().substring(4,5))){
								prpLCItemKindVo.setNoDutyFlag("1");
							}
						}
					}
				}
			}
			voList.add(prpCMainVo);
		}
		return voList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#findRelatedPolicy
	 * (ins.sino.claimcar.regist.vo.PolicyInfoVo)
	 */
	@Override
	public Page<PolicyInfoVo> findRelatedPolicy(PolicyInfoVo policyInfoVo) {
		String policyNo = policyInfoVo.getPolicyNo();// 当前保单号
		String relatePlyNo = policyInfoVo.getRelatedPolicyNo();// 关联的保单号

		if(StringUtils.isBlank(relatePlyNo)){
			relatePlyNo = getRelatedPolicyNo(policyNo);
		}

		String queryString = "from PrpCMain pm, PrpCItemCar pc WHERE pm.policyNo = pc.id.policyNo  AND pm.policyNo = ? ";
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString,1,3,relatePlyNo);

		// 对查询结果进行数据处理
		Page pageReturn = assemblyPolicyInfo(page,policyInfoVo);
		// 返回结果ResultPage
		return pageReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#findByPolicyNo(java
	 * .lang.String)
	 */
	@Override
	public int findByPolicyNo(String policyNo) {
		String relatePlyNo = getRelatedPolicyNo(policyNo);// 关联的保单号
		String queryString = "from PrpCMain pm, PrpCItemCar pc WHERE pm.policyNo = pc.id.policyNo  AND pm.policyNo = ? ";
		// 执行查询
		Page<Object[]> page = databaseDao.findPageByHql(queryString,1,3,relatePlyNo);
		return page.getResult().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ins.sino.claimcar.regist.service.PolicyQueryService#findPrpCMainForPages
	 * (ins.sino.claimcar.regist.vo.PolicyInfoVo, int, int)
	 */
	@Override
	public List<PolicyInfoVo> findPrpCMainForPages(PolicyInfoVo policyInfo,int start,int pageSize) throws Exception {
		Page<PolicyInfoVo> page = findPrpCMainForPage(policyInfo,start,pageSize);
		/*
		 * String mainSubSql =
		 * "FROM from PrpCMain pm, PrpCItemCar pc WHERE pm like ? and pc.frameNo = ? "
		 * ; List<Object> paramValues = new ArrayList<Object>();
		 * paramValues.add(policyNo); paramValues.add(policyNo);
		 */
		// List<PrpCMainSub> prpCMainSubList =
		// databaseDao.findAllByHql(PrpCMainSub.class,mainSubSql,policyNo,policyNo);
		// 循环查询结果集合

		List<PolicyInfoVo> policyInfoVos = new ArrayList<PolicyInfoVo>();
		List<PolicyInfoVo> policyInfoVoList = new ArrayList<PolicyInfoVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PolicyInfoVo policyInfoVo = page.getResult().get(i);
			QueryRule queryRule = QueryRule.getInstance();
			// hql查询语句
			StringBuffer queryString = new StringBuffer("from PrpCMain pm, PrpCItemCar pc"+" WHERE pm.policyNo = pc.id.policyNo");
			// 定义参数list，ps：执行查询时需要转换成object数组
			List<Object> paramValues = new ArrayList<Object>();
			queryString.append(" and pm.comCode like ? ");
			paramValues.add(policyInfoVo.getComCode().substring(0,2)+"%");

			queryString.append("and pc.frameNo = ? ");
			paramValues.add(policyInfoVo.getFrameNo());

			if( !policyInfoVo.getPolicyNo().isEmpty()){
				queryString.append("and pc.id.policyNo = ?");
				paramValues.add(policyInfoVo.getPolicyNo());
			}

			// String queryString =
			// "FROM from PrpCMain pm, PrpCItemCar pc WHERE pm.comcode like ? and pc.frameNo = ? ";
			/*
			 * List<Object> paramValues = new ArrayList<Object>();
			 * paramValues.add(policyInfoVo.getComCode().substring(0, 2)+"%");
			 * paramValues.add(policyInfoVo.getFrameNo()); if
			 * (!policyInfoVo.getPolicyNo().isEmpty()) { queryString
			 * =queryString+"and pm.policyNo = ?";
			 * paramValues.add(policyInfoVo.getPolicyNo()); }
			 */

			// PrpLCMainVo

			/*
			 * if (!policyInfoVo.getPolicyNo().isEmpty()) {
			 * queryRule.addEqual("id.policyNo", policyInfoVo.getPolicyNo()); }
			 * queryRule.addEqual("frameNo", policyInfoVo.getFrameNo());
			 * queryRule
			 * .addLike("otherNature",policyInfoVo.getComCode().substring(0,
			 * 2)+"%");
			 */
			// 执行查询
			Page page1 = databaseDao.findPageByHql(queryString.toString(),( start/pageSize+1 ),pageSize,paramValues.toArray());

			// List<PrpCItemCar> list =
			// databaseDao.findAllByHql(PrpCItemCar.class, queryString,
			// paramValues.toString());

			// System.out.println(policyInfoVo.getComCode().substring(0,
			// 2)+"%"+"ppppppp"+policyInfoVo.getPolicyNo()+"kkk"+policyInfoVo.getFrameNo());
			// List<PrpCItemCar> list =
			// databaseDao.findAll(PrpCItemCar.class,queryRule);
			// 对查询结果进行数据处理
			Page pageReturn = assemblyPolicyInfo(page1,policyInfo);
			/*
			 * for(PrpCItemCar po:list){
			 * policyInfoVo.setPolicyNo(po.getPrpCMain().getPolicyNo());
			 * policyInfoVos.add(policyInfoVo); }
			 */
			if(pageReturn.getTotalCount()>0){
				for(int j = 0; j<pageReturn.getTotalCount(); j++ ){
					policyInfoVos.add((PolicyInfoVo)pageReturn.getResult().get(j));
				}
			}
		}
		policyInfoVoList.addAll(policyInfoVos);
		return policyInfoVoList;

	}

	private String reverse(String str) {
		if(str!=null){
			return new StringBuilder(str.trim()).reverse().toString();
		}

		return null;
	}

	@Override
	public Page<PolicyInfoVo> findPolicyNoForPage(PolicyInfoVo policyInfoVo,int start,int pageSize) {

		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();

		// hql查询语句
		StringBuffer queryString = new StringBuffer(
				"from PrpCMain pm, PrpCItemCar pc, PrpCInsured pi"+" WHERE pm.policyNo = pc.id.policyNo AND pm.policyNo = pi.id.policyNo AND pi.id.serialNo = 1");

		// 根据页面查询条件的单选，拼接语句的查询条件
		switch (Integer.valueOf(policyInfoVo.getCheckFlag())) {
		case 1:// 保单号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getPolicyNo().isEmpty()){
				if(policyInfoVo.getPolicyNo().trim().length()<=7&&policyInfoVo.getPolicyNo().trim().length()>=4){
					queryString.append(" AND reverse(pm.policyNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getPolicyNo())+"%");
				}else{
					queryString.append(" AND pm.policyNo = ?");
					paramValues.add(policyInfoVo.getPolicyNo().trim());
				}
			}
			break;
		case 2:// 车牌号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getLicenseNo().trim().isEmpty()){
				if(policyInfoVo.getLicenseNo().trim().length()>=4&&policyInfoVo.getLicenseNo().trim().length()<7){
					queryString.append(" AND reverse(pc.licenseNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getLicenseNo().trim())+"%");

					/*
					 * queryString.append(" AND pc.licenseNo LIKE ?");
					 * //去除左边%才能够用索引
					 * paramValues.add(policyInfoVo.getLicenseNo().trim()+"%");
					 */
				}else{
					queryString.append(" AND pc.licenseNo = ?");
					paramValues.add(policyInfoVo.getLicenseNo().trim());
				}
			}
			break;
		case 3:// 被保险人查询
			if( !policyInfoVo.getInsuredName().trim().isEmpty()){
				queryString.append(" AND pm.insuredName LIKE ?");
				// 去除左边%才能够用索引
				paramValues.add(policyInfoVo.getInsuredName().trim()+"%");
			}
			break;
		case 4:// 发动机号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getEngineNo().trim().isEmpty()){
				if(policyInfoVo.getEngineNo().trim().length()>=4&&policyInfoVo.getEngineNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.engineNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getEngineNo())+"%");
				}else{
					queryString.append(" AND pc.engineNo = ?");
					paramValues.add(policyInfoVo.getEngineNo().trim());
				}
			}
			break;
		case 5:// 车架号查询，支持精准或后4~7为查询
			if( !policyInfoVo.getFrameNo().trim().isEmpty()){
				if(policyInfoVo.getFrameNo().trim().length()>=4&&policyInfoVo.getFrameNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.frameNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getFrameNo())+"%");
				}else{
					queryString.append(" AND pc.frameNo = ?");
					paramValues.add(policyInfoVo.getFrameNo().trim());
				}
				if(policyInfoVo.getComCode()!=null){
					queryString.append(" and pm.comCode like ? ");
					paramValues.add(policyInfoVo.getComCode().substring(0,2)+"%");
				}

			}
			break;
		case 6:// vin号（车架号）查询，支持精准或后4~7为查询//改成跟车架号一样
			/*
			 * if (!policyInfoVo.getVinNo().isEmpty()) { if
			 * (policyInfoVo.getVinNo().length() >= 4 &&
			 * policyInfoVo.getVinNo().length() <= 7) {
			 * queryString.append(" AND pc.vinNo LIKE ?"); paramValues.add("%" +
			 * policyInfoVo.getVinNo()); } else {
			 * queryString.append(" AND pc.vinNo = ?");
			 * paramValues.add(policyInfoVo.getVinNo()); } } break;
			 */
			if( !policyInfoVo.getVinNo().trim().isEmpty()){
				if(policyInfoVo.getVinNo().trim().length()>=4&&policyInfoVo.getVinNo().trim().length()<=7){
					queryString.append(" AND reverse(pc.frameNo) LIKE ?");
					paramValues.add(reverse(policyInfoVo.getVinNo())+"%");
				}else{
					queryString.append(" AND pc.frameNo = ?");
					paramValues.add(policyInfoVo.getVinNo().trim());
				}
			}
			break;
		case 7:// 被保险人身份证号查询
			if( !policyInfoVo.getInsuredIdNo().trim().isEmpty()){
				queryString.append(" AND pi.identifyNumber = ?");
				paramValues.add(policyInfoVo.getInsuredIdNo().trim());
			}
			break;
		}
		// //查询二级机构
		// queryString.append(" AND pc.otherNature like pm.comcode?%");
		// paramValues.add(policyInfoVo.getLicenseColor());
		// 车牌底色查询，非必填项
		// if( !policyInfoVo.getLicenseColor().isEmpty()){
		if( !StringUtils.isBlank(policyInfoVo.getLicenseColor())){

			queryString.append(" AND pc.licenseColorCode = ?");
			paramValues.add(policyInfoVo.getLicenseColor());
		}
		// 被保险人身份证号查询，非必填项
		/*
		 * if( !policyInfoVo.getInsuredIdNo().isEmpty()){
		 * queryString.append(" AND pi.identifyNumber = ?");
		 * paramValues.add(policyInfoVo.getInsuredIdNo()); }
		 */
		if("1".equals(policyInfoVo.getOnlyValid())){
			queryString.append(" AND (pm.underWriteFlag = ? ");
			paramValues.add("1");
			queryString.append(" or  pm.underWriteFlag = ? )");
			paramValues.add("3");
			queryString.append(" AND (pm.endDate > ? ");
			paramValues.add(policyInfoVo.getDamageTime());
			queryString.append(" or pm.endDate = ?)");
			paramValues.add(policyInfoVo.getDamageTime());
		}
		//29962 理赔默认菜单问题 保单查询 查询的内容按照机构权限过滤，如果没有配置机构权限，应该查不到内容
		String userCode = ServiceUserUtils.getUserCode();
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
		Map<String, List<SaaFactorPowerVo>> factorPowerMap = userPowerVo.getPermitFactorMap();
		if (!"0000000000".equals(userCode)) {
			if (null != factorPowerMap && !factorPowerMap.isEmpty()) {
				List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
				if(factorPowerVoList == null || factorPowerVoList.size() == 0){
					return new Page<>();
				}else{
					logger.info("保单查询开始权限判断");
					boolean isMainCom = false;
					for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
						String comCode = factorPowerVo.getDataValue();
						if("0001%".equals(comCode)){//排除总公司
							isMainCom = true;
							break;
						}
					}
					if(!isMainCom){
						queryString.append(" AND (");
						for(int i = 0; i < factorPowerVoList.size(); i++){
							SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
							String comCode = factorPowerVo.getDataValue();
							if(i > 0){
								queryString.append("  OR  ");
							}
							queryString.append(this.genSubPowerHql(factorPowerVo,"comCode","pm"));// 得到alias. fieldCode = ?);
							paramValues.add(comCode);
						}
						queryString.append(" )");
					}
				}
			} else {
				return new Page<>();
			}
		}

		queryString.append(" Order By pm.startDate Desc ");
		logger.info("保单查询 sql : " + queryString);
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(),( start/pageSize+1 ),pageSize,paramValues.toArray());
		assemblyPolicyInfos(page,policyInfoVo);
		return page;
	}

	private Page assemblyPolicyInfos(Page page,PolicyInfoVo policyInfoVo) {

		// 循环查询结果集合
		for(int i = 0; i<page.getResult().size(); i++ ){

			// 获取当前序号下的保单相关对象数组
			Object[] obj = (Object[])page.getResult().get(i);

			if(obj[0]!=null&&obj[1]!=null){

				PolicyInfoVo plyVo = new PolicyInfoVo();

				// 将获取的承保主表信息，复制到PolicyInfoVo对象中
				PrpCMain pm = (PrpCMain)obj[0];
				Beans.copy().from(pm).to(plyVo);

				// 将获取的标的车表信息，复制到PolicyInfoVo对象中
				PrpCItemCar pc = (PrpCItemCar)obj[1];
				Beans.copy().from(pc).to(plyVo);

				String relatedPolicyNo = getRelatedPolicyNo(plyVo.getPolicyNo());
				plyVo.setRelatedPolicyNo(relatedPolicyNo);
				String riskCode = pm.getRiskCode();
				if(riskCode.equals("1101")){
					plyVo.setRiskType("交强");
				}else{
					plyVo.setRiskType("商业");
				}
				page.getResult().set(i,plyVo);
			}
		}
		return page;
	}

	/**
	 * 保单实收判断，对接新收付之后，调用新收付接口根据返回值进行判断
	 * @param plyNoArr
	 * @return
	 */
	@Override
	public Set<Map<String,String>> findPrpjpayrefrecBySQL(String[] plyNoArr) {
		Set<String> set = new HashSet<String>();
		for(String policyno:plyNoArr){
			set.add(policyno.trim());
		}
		plyNoArr = set.toArray(new String[set.size()]);
		Set<Map<String,String>> sets = new HashSet<Map<String,String>>();
		Gson gson = new Gson();
		String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/queryPolicyPlanInfo";
		if (newPaymentUrl.trim().length() == 0) {
			logger.info("业务号：{} 未配置收付地址，保单实收验证失败！", Arrays.toString(plyNoArr));
			return sets;
		}

		for (String policyno : plyNoArr) {
			QueryPrpPayrefrecDto payrefrecDto = new QueryPrpPayrefrecDto();
			payrefrecDto.setCertiType("P");
			payrefrecDto.setCertiNo(policyno);
			String paymentJson = gson.toJson(payrefrecDto);
			logger.info("保单实收信息请求收付数据为：" + paymentJson);
			// 从收付查询保单实收信息
			String policyRefResult = queryPolicyPayRefInfo(paymentJson, newPaymentUrl);
			logger.info("保单实收信息收付响应数据为：" + policyRefResult);
			ReturnInfo returnInfo = gson.fromJson(policyRefResult, ReturnInfo.class);
			if (returnInfo != null && returnInfo.getPaymentInfos() != null) {
				List<PaymentInfo> paymentInfos = returnInfo.getPaymentInfos();
				for (PaymentInfo paymentInfo : paymentInfos) {
					Map<String,String> map = new HashMap<String,String>();
					map.put("policyno", paymentInfo.getPolicyNo());
					map.put("coinsflag", "2");
					map.put("payrefflag",paymentInfo.getPayRefFlag());
					sets.add(map);
				}
			}
		}

		return sets;
	}

	@Override
	public Set<Map<String,String>> findPrpCMainBySQL(String[] plyNoArr) {
		Set<String> set = new HashSet<String>();
		for(String policyno:plyNoArr){
			set.add(policyno.trim());
		}
		plyNoArr = set.toArray(new String[set.size()]);
		String condition = "(";
		Set<Map<String,String>> sets = new HashSet<Map<String,String>>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		for(String policyno:plyNoArr){
			condition += "?"+",";
			sqlUtil.addParamValue(policyno);
		}
		condition = condition.substring(0,condition.length()-1)+")";
		sqlUtil.append("select policyno,coinsflag from ywuser.prpcmain where policyno in "+condition+"");
		String sql = sqlUtil.getSql();
		Object[] paramValues = sqlUtil.getParamValues();
		List<Object[]> list = baseDaoService.getAllBySql(sql,paramValues);
		if(list!=null&& !list.isEmpty()){
			for(Object[] object:list){
				Map<String,String> map = new HashMap<String,String>();
				map.put("policyno",object[0].toString());
				map.put("coinsflag",object[1].toString());
				sets.add(map);
			}
			return sets;
		}
		return null;
	}

	@Override
	public List<Map<String,String>> findPrpCCoins(String[] plyNoArr) {
		Set<String> set = new HashSet<String>();
		for(String policyno:plyNoArr){
			set.add(policyno.trim());
		}
		plyNoArr = set.toArray(new String[set.size()]);
		List<Map<String,String>> prpccoinsList = new ArrayList<Map<String,String>>();
		String condition = "(";
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		for(String policyno:plyNoArr){
			condition += "?"+",";
			sqlUtil.addParamValue(policyno);
		}
		condition = condition.substring(0,condition.length()-1)+")";
		sqlUtil.append("select policyNo,mainPolicyNo,coinsCode,coinsName,coinsType,coinsRate,chiefFlag from ywuser.prpccoins where policyno in "+condition+"");
		String sql = sqlUtil.getSql();
		Object[] paramValues = sqlUtil.getParamValues();
		List<Object[]> list = baseDaoService.getAllBySql(sql,paramValues);
		if(list!=null&& !list.isEmpty()){
			for(Object[] object:list){
				Map<String,String> map = new HashMap<String,String>();
				if(StringUtils.isNotBlank(object[0].toString())){
					String policyNo = object[0].toString().substring(11,15);
					if("1206".equals(policyNo)){
						map.put("policyNo",object[0].toString());
					}else if("1101".equals(policyNo)){
						map.put("policyNo",null);// 只显示商业，不显示交强，不为商业或交强，显示
					}else{
						map.put("policyNo",object[0].toString());
					}
				}
				if(StringUtils.isNotBlank(object[1].toString())){
					String mainPolicyNo = object[1].toString().substring(11,15);
					if("1206".equals(mainPolicyNo)){
						map.put("mainPolicyNo",null);// 只显示交强，不显示商业，不为商业或交强，显示
					}else if("1101".equals(mainPolicyNo)){
						map.put("mainPolicyNo",object[1].toString());
					}else{
						map.put("mainPolicyNo",object[1].toString());
					}
				}
				map.put("coinsCode",object[2].toString());
				map.put("coinsName",object[3].toString());
				map.put("coinsType",object[4].toString());
				map.put("coinsRate",object[5].toString());
				map.put("chiefFlag",object[6].toString());
				prpccoinsList.add(map);
			}
			return prpccoinsList;
		}
		return null;
	}

	@Override
	public boolean existPolicy(String policyNo,Date damageTime) {

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addLessThan("startDate",damageTime);
		queryRule.addGreaterThan("endDate",damageTime);
		List<PrpCMain> prpCMainList = databaseDao.findAll(PrpCMain.class,queryRule);
		if(prpCMainList==null||prpCMainList.size()<=0){
			return false;
		}else{
			return true;
		}

	}

	@Override
	public List<PolicyInfoVo> findPolicyNoList(String insuredName,String licenseNo,String identifyNumber,String identifyType,String engineNo,
												String frameNo) {

		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		// hql查询语句
		sqlUtil.append(" from PrpCMain pm, PrpCItemCar pc, PrpCInsured pi WHERE pm.policyNo = pc.id.policyNo AND pm.policyNo = pi.id.policyNo AND pi.id.serialNo = 1 ");
		// 被保险人
		sqlUtil.append(" AND pm.insuredName=? ");
		sqlUtil.addParamValue(insuredName);
		// 车牌号
		if(StringUtils.isNotBlank(licenseNo)){
			sqlUtil.append(" AND pc.licenseNo =? ");
			sqlUtil.addParamValue(licenseNo);
		}
		// 身份证号
		sqlUtil.append(" AND pi.identifyNumber =? ");
		sqlUtil.addParamValue(identifyNumber);
		// 新增查詢條件：
		sqlUtil.append(" AND pi.identifyType =? ");
		sqlUtil.addParamValue(identifyType);
		sqlUtil.append(" AND pc.engineNo =? ");
		sqlUtil.addParamValue(engineNo);
		sqlUtil.append(" AND pc.frameNo =? ");
		sqlUtil.addParamValue(frameNo);

		Object[] pamas = sqlUtil.getParamValues();// 数值
		String sql = sqlUtil.getSql();
		List<Object[]> lists = databaseDao.findAllByHql(sql,pamas);
		List<PolicyInfoVo> infoVoList = new ArrayList<PolicyInfoVo>();
		if(lists!=null&&lists.size()>0){
			for(Object[] objects:lists){
				PrpCMain prpCMain = (PrpCMain)objects[0];
				PrpCItemCar prpCItemCar = (PrpCItemCar)objects[1];
				PrpCInsured prpCInsured = (PrpCInsured)objects[2];
				PolicyInfoVo infoVo = new PolicyInfoVo();
				infoVo.setPolicyNo(prpCMain.getPolicyNo());
				infoVo.setLicenseNo(prpCItemCar.getLicenseNo());
				infoVo.setRiskType(prpCMain.getRiskCode());
				infoVo.setStartDate(prpCMain.getStartDate());
				infoVo.setStartHour(prpCMain.getStartHour());
				infoVo.setEndDate(prpCMain.getEndDate());
				infoVo.setEndHour(prpCMain.getEndHour());
				infoVo.setIdentifyType(prpCInsured.getIdentifyType());
				infoVo.setIdentifyNumber(prpCInsured.getIdentifyNumber());
				infoVoList.add(infoVo);
			}
		}

		return infoVoList;
	}

	@Override
	public String findPrpCMian(String policyNo) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("policyNo",policyNo);
		List<PrpCMain> prpcmains = databaseDao.findAll(PrpCMain.class,query);
		if(prpcmains!=null&&prpcmains.size()>0){
			return prpcmains.get(0).getServiceMobile();
		}else{
			return null;
		}
	}

	/**
	 * 从收付查询共保保单实收信息
	 * @param requestParams 请求数据
	 * @param newPaymentUrl 新收付地址
	 * @return 新收付返回结果
	 */
	private String queryPolicyPayRefInfo(String requestParams, String newPaymentUrl) {

		BufferedReader bfreader = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		StringBuilder buffer = new StringBuilder();

		try {
			URL url = new URL(newPaymentUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			// post方式不能使用缓存
			httpURLConnection.setUseCaches(false);
			// 配置本次连接的Content-Type，配置为text/xml
			httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			// 维持长连接
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setConnectTimeout(20 * 1000);
			httpURLConnection.setReadTimeout(20 * 1000);
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.connect();
		} catch (Exception ex) {
			logger.info("连接新收付地址失败！", ex);
			return buffer.toString();
		}
		try {
			outputStream = httpURLConnection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(requestParams.getBytes("utf-8"));
			out.flush();
			bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String strLine = "";
			while ((strLine = bfreader.readLine()) != null) {
				buffer.append(strLine);
			}
		} catch (Exception e) {
			logger.info("读取新收付接口返回数据失败！", e);
		} finally {
			try {
				if (bfreader != null) {
					bfreader.close();
				}

				if (out != null) {
					out.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				logger.info("保单实收查询流关闭异常！", e);
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String genSubPowerHql(SaaFactorPowerVo factorPowerVo,String fieldCode,String alias) {

		SqlParamVo subPowerSqlVo = new SqlParamVo();
		String dataOper = factorPowerVo.getDataOper().toLowerCase();
		if("=".equals(dataOper)||"<".equals(dataOper)||">".equals(dataOper)||"<=".equals(dataOper)||">=".equals(dataOper)||"like".equals(dataOper)){// 不同操作符不同处理
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
		}
		return subPowerSqlVo.getSql().toString();

	}
}
