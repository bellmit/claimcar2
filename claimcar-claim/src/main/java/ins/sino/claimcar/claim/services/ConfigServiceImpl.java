/**
 * 
 */
package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.exception.BusinessException;
import ins.framework.utils.Beans;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.po.PrpDAccidentDeduct;
import ins.sino.claimcar.claim.po.PrpDDeprecateRate;
import ins.sino.claimcar.claim.po.PrpLDLimit;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.vo.PrpDAccidentDeductVo;
import ins.sino.claimcar.claim.vo.PrpDDeprecateRateVo;
import ins.sino.claimcar.claim.vo.PrpLDLimitVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.Path;

/**
 * 立案 理算用到的配置类查询
 *
 */
//@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" },timeout=100000)
//@Path("configService")
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
	private static final Log logger = LogFactory.getLog(ConfigServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private RegistQueryService registQueryService;

	/*
	 * @see ins.sino.claimcar.claim.services.ConfigService#findPrpLDLimitList(java.lang.String)
	 * @param ciindemDuty
	 * @return
	 */
	@Override
	public List<PrpLDLimitVo> findPrpLDLimitList(String ciindemDuty,String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("ciindemDuty", ciindemDuty);
		queryRule.addEqual("year",getLimitYearByRegistNo(registNo));
		queryRule.addAscOrder("id");
		List<PrpLDLimit> prpLDLimitList = databaseDao.findAll(PrpLDLimit.class,queryRule);
		List<PrpLDLimitVo> prpLdlimitList = new ArrayList<PrpLDLimitVo>();
		prpLdlimitList = Beans.copyDepth().from(prpLDLimitList).toList(PrpLDLimitVo.class);
		return prpLdlimitList;
	}

	/**
	 * 查询交强险某个损失类型的限额
	 * @param damageType 损失类型 CodeConstrants.FeeTypeCode
	 * @param isCiIndemDuty 是否有责
	 * @param registNo 报案号
	 * @return
	 */
	@Override
	public double calBzAmount(String damageType, Boolean isCiIndemDuty, String registNo) {
		String ciIndemDuty;
		if (isCiIndemDuty) {
			ciIndemDuty = CodeConstants.DutyType.CIINDEMDUTY_Y;//有责
		} else {
			ciIndemDuty = CodeConstants.DutyType.CIINDEMDUTY_N;//无责
		}
		String year = getLimitYearByRegistNo(registNo);//获取交强险限额版本

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("lossItemNo",damageType);
		queryRule.addEqual("ciindemDuty",ciIndemDuty);
		queryRule.addEqual("year",year);
		PrpLDLimit prpLDLimit = databaseDao.findUnique(PrpLDLimit.class,queryRule);
		if (prpLDLimit == null){
			throw new BusinessException("根据损失类型、是否有责、年份版本，未查询到对应损失类型的交强险责任限额",false);
		}
		PrpLDLimitVo prpLDLimitVo;
		prpLDLimitVo = Beans.copyDepth().from(prpLDLimit).to(PrpLDLimitVo.class);

		return prpLDLimitVo.getLimitFee().doubleValue();
	}

	/**
	 * 获取交强责任限额Map
	 * @param registNo
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> getCIDutyAmt(String registNo){
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		Map<String,BigDecimal> dutyAmtMap = new HashMap<String,BigDecimal>();

		String indemDuty = CodeConstants.DutyType.CIINDEMDUTY_N;//默认无责
		if(checkDutyList!=null&&!checkDutyList.isEmpty()){
			for(PrpLCheckDutyVo checkDutyVo:checkDutyList ){
				if(checkDutyVo.getSerialNo()==1){
					if("1".equals(checkDutyVo.getCiDutyFlag())){
						indemDuty = CodeConstants.DutyType.CIINDEMDUTY_Y;//有责
					}
					break;
				}
			}
		}
		List<PrpLDLimitVo> prpLDLimitVoList = this.findPrpLDLimitList(indemDuty,registNo);
		if (prpLDLimitVoList == null || prpLDLimitVoList.size() == 0){
			throw new BusinessException("未查询到交强险责任限额",false);
		}
		if (prpLDLimitVoList.size() != 3){
			throw new BusinessException("查询到的交强险责任限额不是3条记录",false);
		}
		for (PrpLDLimitVo prpLDLimitVo : prpLDLimitVoList){
			if (CodeConstants.FeeTypeCode.PROPLOSS.equals(prpLDLimitVo.getLossItemNo())){
				dutyAmtMap.put("carLoss",prpLDLimitVo.getLimitFee());
			}else if (CodeConstants.FeeTypeCode.PERSONLOSS.equals(prpLDLimitVo.getLossItemNo())){
				dutyAmtMap.put("persLoss",prpLDLimitVo.getLimitFee());
			}else if (CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(prpLDLimitVo.getLossItemNo())){
				dutyAmtMap.put("mediLoss",prpLDLimitVo.getLimitFee());
			}
		}

		return dutyAmtMap;
	}

	/**
	 * 获取哪个年份版本交强险限额
	 * @param registNo
	 * @return
	 */
	private String getLimitYearByRegistNo(String registNo){
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		Date damageTime = prpLRegistVo.getDamageTime();//出险日期

		String year = "2014";//默认2014版本
		//第一版，只判断出险时间是否在综改上线时间之后，如果是全国统一使用新限额
		//String onlineDateStr = SpringProperties.getProperty("SG2020_ONLINE_DATE");
		//第二版，每个机构配置一个综改上线时间，如果上线时间在该机构上线后，则该机构使用新限额，包含标的车和三者车
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.newBussiness2020Flag,null);
		if (configValueVo != null) {
			String onlineDateStr = configValueVo.getConfigValue();
			//判断出险时间是否在综改上线时间之后
			Date onlineDate;
			try {
				onlineDate = DateUtils.strToDate(onlineDateStr, DateUtils.YToSec);
			} catch (ParseException e) {
				throw new BusinessException("2020年车险综合改革解析配置出险时间报错", false);
			}
			if (onlineDate.before(damageTime)) {
				year = "2020";//2020车险综合改革版本
			}
		}

		logger.info("报案号：registNo:" + registNo + ",出险时间：" + DateUtils.dateToStr(damageTime,DateUtils.YToSec) + ",机构代码："+ prpLRegistVo.getComCode() +",使用【" + year + "】年版本交强险限额");

		return year;
	}
	
	 /* 
	 * @see ins.sino.claimcar.claim.services.ConfigService#findDeprecateRate(java.lang.String, java.lang.String, java.lang.String)
	 * @param carKindCode
	 * @param riskCode
	 * @param clauseType
	 * @return
	 */
	@Override
	public PrpDDeprecateRateVo findDeprecateRate(String carKindCode,String riskCode,String clauseType){
		PrpDDeprecateRateVo deprecateRateVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("carKindCode", carKindCode);
		queryRule.addEqualIfExist("riskCode", riskCode);
		queryRule.addEqualIfExist("clauseType", clauseType);
		List<PrpDDeprecateRate> DeprecateRateList = databaseDao.findAll(PrpDDeprecateRate.class,queryRule);
		if(DeprecateRateList!=null && DeprecateRateList.size()>0){
			PrpDDeprecateRate dr = DeprecateRateList.get(0);
			deprecateRateVo = Beans.copyDepth().from(dr).to(PrpDDeprecateRateVo.class);
		}
		return deprecateRateVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.ConfigService#findAccidentDeduct(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * @param riskCode
	 * @param kindCode
	 * @param clauseType
	 * @param indemnityDuty
	 * @return
	 */
	@Override
	public PrpDAccidentDeductVo findAccidentDeduct(String riskCode,String kindCode,String clauseType,String indemnityDuty){
		PrpDAccidentDeductVo accidentDeductVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("riskCode", riskCode);
		queryRule.addEqualIfExist("kindCode", kindCode);
		queryRule.addEqualIfExist("clauseType", clauseType);
		queryRule.addEqualIfExist("indemnityDuty", indemnityDuty);
		
		List<PrpDAccidentDeduct> accidentDeductList = databaseDao.findAll(PrpDAccidentDeduct.class,queryRule);
		if(accidentDeductList!=null && !accidentDeductList.isEmpty()){
			PrpDAccidentDeduct dr = accidentDeductList.get(0);
			accidentDeductVo = Beans.copyDepth().from(dr).to(PrpDAccidentDeductVo.class);
		}
		return accidentDeductVo;
	}

}
