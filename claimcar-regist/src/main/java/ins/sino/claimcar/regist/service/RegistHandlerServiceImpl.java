package ins.sino.claimcar.regist.service;

import ins.framework.dao.database.DatabaseDao;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.schema.SysAreaDict;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLEndorVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.regist.vo.PrpCiInsureValidVo;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAndCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAvgVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrplOldClaimRiskInfoVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 报案服务类
 * @author ZhangYu
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "registHandlerService")
public class RegistHandlerServiceImpl implements RegistHandlerService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	ManagerService managerService;
	@Autowired
	RegistService registService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	private ClaimAvgConfigService claimAvgConfigService;
	@Autowired
    private RegistProService registProService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	PolicyQueryService policyQueryService;
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#save(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.util.List, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public PrpLRegistVo save(PrpLRegistVo prpLRegist, List<PrpLCMainVo> prpLCMain, boolean flag,String BIPolicyNo,String CIPolicyNo) throws ParseException {
		supplementInfo(prpLRegist, prpLCMain);
		PrpLRegistVo prpLRegistVo = registService.saveOrUpdate(prpLRegist);
		List<PrpLCMainVo> prpLCMains = new ArrayList<PrpLCMainVo>();
		if (flag) {
			//新增报案时保存抄单数据
			//savePrpLCMains(prpLCMains, prpLRegistVo);
			if(StringUtils.isNotBlank(BIPolicyNo) && !BIPolicyNo.equals("undefined")){
				//registProService.insertPrpLCmainVoPro(prpLRegistVo.getRegistNo(), BIPolicyNo);
				PrpLCMainVo prpLCMainVo = prpLCMainService.saveRegistPolicy(prpLRegistVo.getRegistNo(), BIPolicyNo, prpLRegistVo.getDamageTime());
				if(prpLCMainVo != null){
					prpLCMains.add(prpLCMainVo);
				}
				 //更新承保的数据到报案子表
				if(prpLCMainVo != null){
					prpLRegistVo.getPrpLRegistExt().setInsuredName(prpLCMainVo.getInsuredName());
					prpLRegistVo.getPrpLRegistExt().setFrameNo(prpLCMainVo.getPrpCItemCars().get(0).getFrameNo());
					prpLRegistVo.getPrpLRegistExt().setInsuredCode(prpLCMainVo.getInsuredCode());
				}
			
			}
            if(StringUtils.isNotBlank(CIPolicyNo) && !CIPolicyNo.equals("undefined")){
            	//registProService.insertPrpLCmainVoPro(prpLRegistVo.getRegistNo(), CIPolicyNo);
            	PrpLCMainVo prpLCMainVo = prpLCMainService.saveRegistPolicy(prpLRegistVo.getRegistNo(), CIPolicyNo, prpLRegistVo.getDamageTime());
            	if(prpLCMainVo != null){
            		prpLCMains.add(prpLCMainVo);
            	}
            if(!StringUtils.isNotBlank(BIPolicyNo) || BIPolicyNo.equals("undefined")){
				 //更新承保的数据到报案子表
				if(prpLCMainVo != null){
					prpLRegistVo.getPrpLRegistExt().setInsuredName(prpLCMainVo.getInsuredName());
					prpLRegistVo.getPrpLRegistExt().setFrameNo(prpLCMainVo.getPrpCItemCars().get(0).getFrameNo());
					prpLRegistVo.getPrpLRegistExt().setInsuredCode(prpLCMainVo.getInsuredCode());
				}
			}
            }
			//新增报案时保存报案时案均数据
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
			String year = fmt.format(new Date());
			BigDecimal avgYear = new BigDecimal(year);
			String comCode = "";
			List<String> riskCodes = new ArrayList<String>();
			if(prpLCMains.size()>0){
				for (PrpLCMainVo lCMainVo : prpLCMains) {
					comCode = lCMainVo.getComCode();
					riskCodes.add(lCMainVo.getRiskCode());
				}
			}else{
				for (PrpLCMainVo lCMainVo : prpLCMain) {
					comCode = lCMainVo.getComCode();
					riskCodes.add(lCMainVo.getRiskCode());
				}
			}
			List<PrpLRegistAvgVo> registAvgVos = new ArrayList<PrpLRegistAvgVo>();
			
			List<PrpDClaimAvgVo> claimAvgVos = claimAvgConfigService.findAvgConfig(avgYear, comCode, riskCodes);
			if (claimAvgVos == null || claimAvgVos.size() == 0) {
				avgYear = avgYear.subtract(new BigDecimal(1));
				claimAvgVos = claimAvgConfigService.findAvgConfig(avgYear, comCode, riskCodes);
			}
			if (claimAvgVos != null && claimAvgVos.size() > 0) {
				if(prpLCMains.size() > 0){
					for (PrpLCMainVo lCMainVo : prpLCMains) {
						for (PrpLCItemKindVo kindVo : lCMainVo.getPrpCItemKinds()) {
							for (PrpDClaimAvgVo vo : claimAvgVos) {
								if (StringUtils.equals(vo.getKindCode(), kindVo.getKindCode())) {
									PrpLRegistAvgVo registAvgVo = new PrpLRegistAvgVo();
									registAvgVo.setRegistNo(prpLRegistVo.getRegistNo());
									registAvgVo.setAvgYear(vo.getAvgYear());
									registAvgVo.setComCode(vo.getComCode());
									registAvgVo.setRiskCode(vo.getRiskCode());
									registAvgVo.setKindCode(vo.getKindCode());
									registAvgVo.setAvgType(vo.getAvgType());
									registAvgVo.setAvgAmount(vo.getAvgAmount());
									registAvgVo.setValidFlag(CodeConstants.ValidFlag.VALID);
									registAvgVo.setCreateUser(prpLRegist.getUpdateUser());
									registAvgVo.setCreateTime(new Date());
									registAvgVo.setUpdateUser(prpLRegist.getUpdateUser());
									registAvgVo.setUpdateTime(new Date());
									registAvgVos.add(registAvgVo);
								}
							}
						}
					}
				} else {
					for (PrpLCMainVo lCMainVo : prpLCMain) {
						for (PrpLCItemKindVo kindVo : lCMainVo.getPrpCItemKinds()) {
							for (PrpDClaimAvgVo vo : claimAvgVos) {
								if (StringUtils.equals(vo.getKindCode(), kindVo.getKindCode())) {
									PrpLRegistAvgVo registAvgVo = new PrpLRegistAvgVo();
									registAvgVo.setRegistNo(prpLRegistVo.getRegistNo());
									registAvgVo.setAvgYear(vo.getAvgYear());
									registAvgVo.setComCode(vo.getComCode());
									registAvgVo.setRiskCode(vo.getRiskCode());
									registAvgVo.setKindCode(vo.getKindCode());
									registAvgVo.setAvgType(vo.getAvgType());
									registAvgVo.setAvgAmount(vo.getAvgAmount());
									registAvgVo.setValidFlag(CodeConstants.ValidFlag.VALID);
									registAvgVo.setCreateUser(prpLRegist.getUpdateUser());
									registAvgVo.setCreateTime(new Date());
									registAvgVo.setUpdateUser(prpLRegist.getUpdateUser());
									registAvgVo.setUpdateTime(new Date());
									registAvgVos.add(registAvgVo);
								}
							}
						}
					}
				}
				registService.updatePrpLRegistAvgByVos(registAvgVos);
			}
			
			
			//报案新增保存是保存风险提示信息
			Map<String, String> registRiskInfoMap = new HashMap<String, String>();
			
			supplementRiskInfo(registRiskInfoMap, prpLRegistVo.getDamageTime(), prpLRegistVo.getPolicyNo(), prpLRegistVo.getPrpLRegistExt().getPolicyNoLink(),prpLRegistVo);
			//代位求偿
			registRiskInfoMap.put("DWQC", prpLRegistVo.getPrpLRegistExt().getIsSubRogation());
			registService.saveRiskInfos(prpLRegistVo, registRiskInfoMap);
		} else {
			//非报案新增时，更新代位求偿信息
			registService.updateRiskInfoForSubRogation(prpLRegistVo, prpLRegistVo.getPrpLRegistExt().getIsSubRogation());
		}
		//更新共保信息
		List<PrpLCMainVo> cmains = prpLCMainService.findPrpLCMainsByRegistNo(prpLRegistVo.getRegistNo());
		if(cmains!=null && !cmains.isEmpty()){
			for(PrpLCMainVo cmain : cmains){
				String[] plyNoArr  = new String[1];
				plyNoArr[0]=cmain.getPolicyNo();
				Set<Map<String,String>> sets = policyQueryService.findPrpCMainBySQL(plyNoArr);
				if(sets!=null && !sets.isEmpty()){
					for(Map<String,String> map : sets){
						if("1,2,3,4".indexOf(map.get("coinsflag"))!=-1){
							cmain.setCoinsFlag(map.get("coinsflag"));
						}else{
							cmain.setCoinsFlag("0");
						}
					} 
				}else {
					cmain.setCoinsFlag("0");
				}

				//平安联盟案件，将平安赔案号记录到claimNo上
				if (StringUtils.isNotBlank(prpLRegist.getPaicReportNo())){
					for (PrpLCMainVo prpLCMainVo:prpLCMain){
						if (cmain.getPolicyNo().equals(prpLCMainVo.getPolicyNo())){
							cmain.setClaimNo(prpLCMainVo.getClaimNo());
							break;
						}
					}
				}
				prpLCMainService.updatePrpLCMain(cmain);
			}
		}
		return prpLRegistVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#queryCarModel(java.lang.String)
	 */
	@Override
	public String queryCarModel(String registNo){
		String resultStr = "";
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		querySb.append(" SELECT a.carBrand FROM PrpLCitemCar t,"
				+ "ywuser.PrpDcarModel a WHERE a.modelcode = t.modelcode");
		if(StringUtils.isNotBlank(registNo)){
			querySb.append("  AND t.registNo = ? ");
			paramValues.add(registNo);
		}
		List<Object[]> objList = databaseDao.findRangeBySql
				(querySb.toString(),0,1000,paramValues.toArray());
		if(objList!=null && !objList.isEmpty()){
			Object result =objList.get(0);
			resultStr = (String)result;
		}
		return resultStr;
	}
	
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#savePrpLCMains(java.util.List, ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public void savePrpLCMains(List<PrpLCMainVo> prpLCMains, PrpLRegistVo prpLRegist) {
		prpLCMainService.saveOrUpdate(prpLCMains, prpLRegist.getRegistNo());
	}
	

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#relationPolicy(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.util.List, ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo)
	 */
	@Override
	public List<PrpLCMainVo> relationPolicy(PrpLRegistVo prpLRegistVo, List<PrpLCMainVo> toSaveMainVoList, PrpLRegistRelationshipHisVo relationshipHisVo) {
		// TODO Auto-generated method stub
		//将报案的损失子表置空，只更新主表和扩展表
		prpLRegistVo.setPrpLRegistCarLosses(null);
		prpLRegistVo.setPrpLRegistPersonLosses(null);
		prpLRegistVo.setPrpLRegistPropLosses(null);
		prpLRegistVo.setReportType(CodeConstants.ReportType.CI_BI);
		//获取第一个元素，当然toSave集合里只有一个元素
		PrpLCMainVo returnMainVo = toSaveMainVoList.get(0);
		//新增抄单表保存
		for(PrpLCMainVo vo : toSaveMainVoList){
			//保单表添加共保信息
			try{
				String[] plyNoArr = new String[1];
				plyNoArr[0]=vo.getPolicyNo();
				Set<Map<String,String>> sets = policyQueryService.findPrpCMainBySQL(plyNoArr);
				if(sets !=null && !sets.isEmpty()){
					for(Map<String,String> map : sets){
						if("1,2,3,4".indexOf(map.get("coinsflag"))!=-1){
							vo.setCoinsFlag(map.get("coinsflag"));
						}else{
							vo.setCoinsFlag("0");
						}
					} 
				}else {
					vo.setCoinsFlag("0");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		savePrpLCMains(toSaveMainVoList, prpLRegistVo);
		//保存关联与取消轨迹
		registService.saveRelationshipHis(relationshipHisVo);
		//查询保存后的抄单表数据，会有两条数据
		List<PrpLCMainVo> returnMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLRegistVo.getRegistNo());
		if (returnMainVoList != null && returnMainVoList.size() > 0) {
			for (int i = 0; i < returnMainVoList.size(); i ++) {
				//如果时交强险保单，则将保单号写入报案扩展表
				if (returnMainVoList.get(i) != null && StringUtils.equals(returnMainVoList.get(i).getRiskCode(), "1101")) {
					prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(returnMainVoList.get(i).getPolicyNo());
				} else {//否则是商业险，将保单号和险别代码写入报案主表
					prpLRegistVo.setPolicyNo(returnMainVoList.get(i).getPolicyNo());
					prpLRegistVo.setRiskCode(returnMainVoList.get(i).getRiskCode());
				}
				//修改完成如果当前循环中的保单号，与刚刚保存的保单号不一致，那么剔除掉
				if (returnMainVo != null && !StringUtils.equals(returnMainVo.getPolicyNo(), returnMainVoList.get(i).getPolicyNo())) {
					returnMainVoList.remove(i);
					i --;
				}
			}
		}
		
		//将报案的损失子表置空，只更新主表和扩展表
		//registService.saveOrUpdate(prpLRegistVo);
		registService.saveOrUpdatePrpLRegist(prpLRegistVo);
		return returnMainVoList;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#reportCancel(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public PrpLRegistVo reportCancel(PrpLRegistVo prpLRegistVo) {
		prpLRegistVo.setPrpLRegistCarLosses(null);
		prpLRegistVo.setPrpLRegistPersonLosses(null);
		prpLRegistVo.setPrpLRegistPropLosses(null);
		prpLRegistVo = registService.updatePrpLRegist(prpLRegistVo);
		return prpLRegistVo;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#noPolicyFindNoConfirm(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.util.List)
	 */
	@Override
	public void noPolicyFindNoConfirm(PrpLRegistVo registVo, List<PrpLCMainVo> prpLCMains) {
		// TODO Auto-generated method stub
		String userCode = ServiceUserUtils.getUserCode();
		String flag = registVo.getRiskCode().substring(0, 2);
		if (prpLCMains != null && prpLCMains.size() > 0) {
			for (PrpLCMainVo vo : prpLCMains) {
				vo.setValidFlag(CodeConstants.ValidFlag.VALID);
				vo.setCreateTime(new Date());
				vo.setCreateUser(userCode);
				vo.setUpdaterCode(userCode);
				vo.setUpdateTime(new Date());
				if (prpLCMains.size() == 2) {
					if (StringUtils.equals(vo.getRiskCode().substring(0, 2), "12")) {
						registVo.setPolicyNo(vo.getPolicyNo());
						registVo.setRiskCode(vo.getRiskCode());
					} else {
						registVo.getPrpLRegistExt().setPolicyNoLink(vo.getPolicyNo());
					}
				} else {
					registVo.setPolicyNo(vo.getPolicyNo());
					registVo.setRiskCode(vo.getRiskCode());
					registVo.getPrpLRegistExt().setPolicyNoLink("");
				}
				//保单表添加共保信息
				try{
					String[] plyNoArr = new String[1];
					plyNoArr[0]=vo.getPolicyNo();
					Set<Map<String,String>> sets = policyQueryService.findPrpCMainBySQL(plyNoArr);
					if(sets !=null && !sets.isEmpty()){
						for(Map<String,String> map : sets){
							if("1,2,3,4".indexOf(map.get("coinsflag"))!=-1){
								registVo.setIsGBFlag(map.get("coinsflag"));
								vo.setCoinsFlag(map.get("coinsflag"));
							}else{
								vo.setCoinsFlag("0");
								registVo.setIsGBFlag("0");
							}
						} 
					}else {
						registVo.setIsGBFlag("0");
						vo.setCoinsFlag("0");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		PrpLCItemCarVo itemCarVo = prpLCMains.get(0).getPrpCItemCars().get(0);
		registVo.setTempRegistFlag(CodeConstants.TempReport.NOT_TEMPREPORT);
		registVo.setUpdateUser(userCode);
		registVo.setUpdateTime(new Date());
		registVo.getPrpLRegistExt().setUpdateUser(userCode);
		registVo.getPrpLRegistExt().setUpdateTime(new Date());
		
		 //更新承保的数据到报案子表
		if (prpLCMains != null && prpLCMains.size() > 0) {
			if (prpLCMains.size() == 2) {
				for (PrpLCMainVo vo : prpLCMains) {
					if(("12").equals(vo.getRiskCode().substring(0, 2))){
						registVo.getPrpLRegistExt().setInsuredName(vo.getInsuredName());
						for(PrpLCItemCarVo cars:vo.getPrpCItemCars()){
							if(("12").equals(cars.getRiskCode().substring(0, 2))){
								registVo.getPrpLRegistExt().setFrameNo(cars.getFrameNo());
							}
						}
						registVo.getPrpLRegistExt().setInsuredCode(vo.getInsuredCode());
					}
				}
				
			}else{
				for (PrpLCMainVo vo : prpLCMains) {
						registVo.getPrpLRegistExt().setInsuredName(vo.getInsuredName());
						registVo.getPrpLRegistExt().setFrameNo(vo.getPrpCItemCars().get(0).getFrameNo());
						registVo.getPrpLRegistExt().setInsuredCode(vo.getInsuredCode());
				}
			}
		}
		registVo.getPrpLRegistCarLosses().get(0).setBrandName(itemCarVo.getBrandName());
		//承保没有标的车取录入的标的车
		if(StringUtils.isNotBlank(itemCarVo.getLicenseNo()) && StringUtils.isNotBlank(itemCarVo.getLicenseNo().trim())){
			registVo.getPrpLRegistCarLosses().get(0).setLicenseNo(itemCarVo.getLicenseNo().trim());
			registVo.getPrpLRegistCarLosses().get(0).setLicenseType(itemCarVo.getLicenseKindCode());
		}else{
			List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyForPrpLTmpCMain(registVo.getRegistNo());
			if(prpLCMainVos!=null && prpLCMainVos.size() > 0){
				if(prpLCMainVos.get(0).getPrpCItemCars()!=null && prpLCMainVos.get(0).getPrpCItemCars().size()>0){
						if(prpLCMainVos.get(0).getPrpCItemCars().get(0).getLicenseNo()!=null){
						registVo.getPrpLRegistCarLosses().get(0).setLicenseNo(prpLCMainVos.get(0).getPrpCItemCars().get(0).getLicenseNo());
						if(StringUtils.isBlank(registVo.getPrpLRegistExt().getLicenseNo()) || 
								StringUtils.isBlank(registVo.getPrpLRegistExt().getLicenseNo().trim())){
							registVo.getPrpLRegistExt().setLicenseNo(prpLCMainVos.get(0).getPrpCItemCars().get(0).getLicenseNo());
						}
					}
				}
			}
		}
		registVo.getPrpLRegistCarLosses().get(0).setFrameNo(itemCarVo.getFrameNo());
		registService.saveOrUpdate(registVo);
		
		//无保转有保风险提示信息删除再添加
		registService.deleteByRegistNo(registVo);
		//报案新增保存是保存风险提示信息
		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		try {
			supplementRiskInfo(registRiskInfoMap, registVo.getDamageTime(), registVo.getPolicyNo(), registVo.getPrpLRegistExt().getPolicyNoLink(),registVo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//代位求偿
		registRiskInfoMap.put("DWQC", registVo.getPrpLRegistExt().getIsSubRogation());
		registService.saveRiskInfos(registVo, registRiskInfoMap);

		if(flag.equals("12")){
			registService.updateRiskInfo(registVo,"BI-No");
		}else if(flag.equals("11")){
			registService.updateRiskInfo(registVo,"CI-No");
		}
		prpLCMainService.deleteByRegistNo(registVo);
		//PrpCiInsureValid
		//设置投保确认码
		for(PrpLCMainVo vo:prpLCMains){
			List<PrpCiInsureValidVo> vos= prpLCMainService.findPrpCiInsureValidByPolicyNo(vo.getPolicyNo());
			if(vos!=null && vos.size()>0){
				vo.setValidNo(vos.get(0).getValidNo());
			}
		}
		prpLCMainService.saveOrUpdate(prpLCMains, registVo.getRegistNo());
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#supplementRiskInfo(java.util.Map, java.util.Date, java.lang.String, java.lang.String, ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */

	@Override
	public Map<String, String> supplementRiskInfo(Map<String, String> registRiskInfoMap, Date damageDate, String policyNo, String linkpolicyNo,PrpLRegistVo registVo) throws ParseException {
		// TODO Auto-generated method stub
		
		PrpLRegistAndCMainVo cMainVo = new PrpLRegistAndCMainVo();
		//保单信息拼装
		if (!StringUtils.isEmpty(policyNo)) {
			//临时保单报案
			if(StringUtils.equals("T", policyNo.substring(0, 1))){
				if (StringUtils.equals("1101", policyNo.substring(12, 16))) {
					//交强险信息拼装
					registRiskInfoMap.put("CI-No", policyNo);
					//TODO有效期内出险次数
					List<PrplOldClaimRiskInfoVo> InfoVo1 = registService.findPrploldclaimriskinfoByPolicyNo(policyNo);
					PrpLRegistAndCMainVo vo = registService.findPrpLCMainVoByPolicyNo(policyNo);
					//cMainVo = vo;
					if (vo.getPrpLRegistVo() != null) {
						List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
						//将同一保单号的记录，按出险时间降序排
						Collections.sort(revos, new Comparator<PrpLRegistVo>() {
							@Override
							public int compare(PrpLRegistVo o1, PrpLRegistVo o2) {
								return o2.getDamageTime().compareTo(o1.getDamageTime());
							}
						});

						if (revos != null && revos.size() > 0) {
							//将最新一次的出险时间，往前推7天
							Date newTime = DateUtils.addDays(revos.get(0).getDamageTime(), -7);
							//交强险保单七天内出险次数
							int CIDangerinSum = 0;
							for (PrpLRegistVo prpLre : revos) {
								if (prpLre.getDamageTime().getTime() >= newTime.getTime()) {
									CIDangerinSum = CIDangerinSum + 1;
								}
							}
							registRiskInfoMap.put("CI-DangerInSum", String.valueOf(CIDangerinSum));

							//是否48小时之内是否重复报案
							Date LastNewTime = revos.get(0).getDamageTime();//表中存在的最后一次出险时间
							Date TwoLaterTime = DateUtils.addDays(LastNewTime, 2);//表中存在最后一次出险时间的后两天的时间
							// 表中存在的最后一次出险时间两天前的时间
							Date TwoEarlierTime = DateUtils.addDays(LastNewTime, -2);
							Date SecondTime = null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
							Date TwoSecondTime = null;//表中时间两天后的出险时间
							Date TwoSecondEarlierTime = null; // 表中时间两天前的出险时间
							if (damageDate != null) {
								if (damageDate.getTime() != LastNewTime.getTime()) {
									// 上一次出险时间的前后48小时内再次出险，均提示疑似重复报案
									if (TwoEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoLaterTime.getTime()) {
										registRiskInfoMap.put("CIRepeatReport", "1");
									}
								} else {
									if (revos.size() >= 2) {
										SecondTime = revos.get(1).getDamageTime();
										TwoSecondTime = DateUtils.addDays(SecondTime, 2);
										TwoSecondEarlierTime = DateUtils.addDays(SecondTime, -2);
										if (TwoSecondEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoSecondTime.getTime()) {
											registRiskInfoMap.put("CIRepeatReport", "1");
										}
									}
								}
							} else {
								if (revos.size() >= 2) {
									SecondTime = revos.get(1).getDamageTime();
									TwoSecondTime = DateUtils.addDays(SecondTime, 2);
									if (LastNewTime.getTime() >= SecondTime.getTime() && LastNewTime.getTime() <= TwoSecondTime.getTime()) {
										registRiskInfoMap.put("CIRepeatReport", "1");
									}
								}
							}
						}
					}

					if (vo.getPrpLCMainVo() != null) {
						List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
						registRiskInfoMap.put("CI-DangerNum", String.valueOf(vo1.size() + InfoVo1.size()));
						//交强保单生效后7天内出险
						int CIPolicy_A7 = 0;
						int CIPolicy_B7 = 0;
						//将最新时间弄出来以基准
						for (PrpLCMainVo prpLCMainVo : vo1) {
							Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(), 7);
							// TODO 朱俊德 需要标记这个方法的用途，从而优化 findPrpLCMainVoByPolicyNo 方法，查询需要的历史案件进行计算，或者重新写一个按保单查询案件的方法
							for (PrpLRegistVo prpLRegistVo : vo.getPrpLRegistVo()) {
								if (nows.getTime() > prpLRegistVo.getDamageTime().getTime()) {
									CIPolicy_A7 = CIPolicy_A7 + 1;
								}
								if (DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7) {
									CIPolicy_B7 = CIPolicy_B7 + 1;
								}

							}
							registRiskInfoMap.put("CIPolicy-B7", String.valueOf(CIPolicy_B7));
							registRiskInfoMap.put("CIPolicy-A7", String.valueOf(CIPolicy_A7));

						}
					} else {
						registRiskInfoMap.put("CI-DangerNum", String.valueOf(InfoVo1.size()));
					}


					//商业险信息拼装
					if (!StringUtils.isEmpty(linkpolicyNo)) {
						registRiskInfoMap.put("BI-No", linkpolicyNo);


//						List<PrplOldClaimRiskInfoVo> InfoVo2 = registService.findPrploldclaimriskinfoByPolicyNo(linkpolicyNo);
						//车身划痕
						List<PrpLEndorVo> prpEndorVos = compensateTaskService.findPrpEndorListByPolicyNo(linkpolicyNo);
						List<PrpLCItemKindVo> prpLCItemKindVos = new ArrayList<PrpLCItemKindVo>();
						if (registVo != null) {

							prpLCItemKindVos = policyViewService.findItemKindVos(linkpolicyNo, registVo.getRegistNo(), "L");

						}
						//车身划痕出险次数
						int BI_CSHH = 0;
						BigDecimal BI_HHJE = new BigDecimal("0");
						/*if(InfoVo2!=null && InfoVo2.size()>0){
							for(PrplOldClaimRiskInfoVo info:InfoVo2){
								if("1".equals(info.getIsDanageKindL())){
									BI_CSHH++;
								}
							}
						}*/

						if (prpEndorVos != null && prpEndorVos.size() > 0) {
							for (PrpLEndorVo prpLEndorVo : prpEndorVos) {
								if ("L".equals(prpLEndorVo.getKindCode())) {
									BI_CSHH++;
								}
							}
						}
						registRiskInfoMap.put("BI-CSHH", BI_CSHH + "");//车身划痕出险次数

						//车身划痕剩余保额
						if (prpLCItemKindVos != null && prpLCItemKindVos.size() > 0) {
							for (PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVos) {
								if ("L".equals(prpLCItemKindVo.getKindCode())) {
									BI_HHJE = prpLCItemKindVo.getAmount();
								}
							}
						}
						/*if(InfoVo2!=null && InfoVo2.size()>0){
						    for(PrplOldClaimRiskInfoVo infoVo:InfoVo2){
						      if("1".equals(infoVo.getIsDanageKindL())){
						    	  BI_HHJE=BI_HHJE.subtract(infoVo.getKindLPayment());
						      }
						     }
						}*/
						if (prpEndorVos != null && prpEndorVos.size() > 0) {
							for (PrpLEndorVo prpLEndorVo : prpEndorVos) {
								if ("L".equals(prpLEndorVo.getKindCode())) {
									BI_HHJE = BI_HHJE.subtract(prpLEndorVo.getEndorAmount());
								}
							}
						}
						registRiskInfoMap.put("BI-HHJE", BI_HHJE + "");//车身划痕剩余保额

						//TODO有效期内出险次数
						vo = registService.findPrpLCMainVoByPolicyNo(linkpolicyNo);
						if (vo.getPrpLRegistVo() != null) {
							List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
							//将同一保单号的记录，按出险时间降序排
							Collections.sort(revos, new Comparator<PrpLRegistVo>() {
								@Override
								public int compare(PrpLRegistVo o1, PrpLRegistVo o2) {
									return o2.getDamageTime().compareTo(o1.getDamageTime());
								}
							});
							//将最新一次的出险时间，往前推7天
							if (revos != null && revos.size() > 0) {
								Date newTime = DateUtils.addDays(revos.get(0).getDamageTime(), -7);
								//商业险保单七天内出险次数
								int BIDangerinSum = 0;
								for (PrpLRegistVo prpLre : revos) {
									if (prpLre.getDamageTime().getTime() >= newTime.getTime()) {
										BIDangerinSum = BIDangerinSum + 1;
									}
								}

								registRiskInfoMap.put("BI-DangerInSum", String.valueOf(BIDangerinSum));

								//是否48小时之内是否重复报案
								Date LastNewTime = revos.get(0).getDamageTime();//表中存在的最后一次出险时间
								Date TwoLaterTime = DateUtils.addDays(LastNewTime, 2);//表中存在最后一次出险时间的后两天的时间
								Date TwoEarlierTime = DateUtils.addDays(LastNewTime, -2);
								Date SecondTime = null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
								Date TwoSecondTime = null;//表中时间两天后的出险时间
								Date TwoSecondEarlierTime = null;//表中出险时间两天前
								if (damageDate != null) {
									if (damageDate.getTime() != LastNewTime.getTime()) {
										if (damageDate.getTime() >= LastNewTime.getTime() && damageDate.getTime() <= TwoLaterTime.getTime() ||
										damageDate.getTime() >= TwoEarlierTime.getTime() && damageDate.getTime() <= LastNewTime.getTime()) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									} else {
										if (revos.size() >= 2) {
											SecondTime = revos.get(1).getDamageTime();
											TwoSecondTime = DateUtils.addDays(SecondTime, 2);
											TwoSecondEarlierTime = DateUtils.addDays(SecondTime, -2);
											if (damageDate.getTime() >= SecondTime.getTime() && damageDate.getTime() <= TwoSecondTime.getTime() ||
											damageDate.getTime() >= TwoSecondEarlierTime.getTime() && damageDate.getTime() <= SecondTime.getTime()) {
												registRiskInfoMap.put("BIRepeatReport", "1");
											}
										}
									}
								} else {
									if (revos.size() >= 2) {
										SecondTime = revos.get(1).getDamageTime();
										TwoSecondTime = DateUtils.addDays(SecondTime, 2);
										if (LastNewTime.getTime() >= SecondTime.getTime() && LastNewTime.getTime() <= TwoSecondTime.getTime()) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									}
								}
							}
						}
						if (vo.getPrpLCMainVo() != null) {
							List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
//							registRiskInfoMap.put("BI-DangerNum",String.valueOf(vo1.size()+InfoVo2.size()));
							registRiskInfoMap.put("BI-DangerNum", String.valueOf(vo1.size()));
							//商业保单生效后7天内出险
							int BIPolicy_A7 = 0;
							int BIPolicy_B7 = 0;
							for (PrpLCMainVo prpLCMainVo : vo1) {
								Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(), 7);
								for (PrpLRegistVo prpLRegistVo : vo.getPrpLRegistVo()) {
									if (nows.getTime() > prpLRegistVo.getDamageTime().getTime()) {
										BIPolicy_A7 = BIPolicy_A7 + 1;
									}
									if (DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7) {
										BIPolicy_B7 = BIPolicy_B7 + 1;
									}

								}
								registRiskInfoMap.put("BIPolicy-B7", String.valueOf(BIPolicy_B7));
								registRiskInfoMap.put("BIPolicy-A7", String.valueOf(BIPolicy_A7));
							}

						}
						/*else{
							registRiskInfoMap.put("BI-DangerNum",String.valueOf(InfoVo2.size()));
						}*/
					}
				} else {
					//商业先信息拼装
					registRiskInfoMap.put("BI-No", policyNo);
					//TODO
					List<PrplOldClaimRiskInfoVo> InfoVo1 = registService.findPrploldclaimriskinfoByPolicyNo(policyNo);
					List<PrpLEndorVo> prpEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(policyNo);
					List<PrpLCItemKindVo> prpLCItemKindVos=new ArrayList<PrpLCItemKindVo>();
					if(registVo!=null){
						prpLCItemKindVos=policyViewService.findItemKindVos(policyNo,registVo.getRegistNo(),"L");
						
					}
					//车身划痕出险次数
					int BI_CSHH=0;
					BigDecimal BI_HHJE=new BigDecimal("0");
					if(InfoVo1!=null && InfoVo1.size()>0){
						for(PrplOldClaimRiskInfoVo info:InfoVo1){
							if("1".equals(info.getIsDanageKindL())){
								BI_CSHH++;
							}
						}
					}
					
					if(prpEndorVos!=null && prpEndorVos.size()>0){
						for(PrpLEndorVo prpLEndorVo:prpEndorVos){
							if("L".equals(prpLEndorVo.getKindCode())){
								BI_CSHH++;	
							}
						}
					}
					registRiskInfoMap.put("BI-CSHH",BI_CSHH+"");//车身划痕出险次数
					
					//车身划痕剩余保额
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVos){
							if("L".equals(prpLCItemKindVo.getKindCode())){
								BI_HHJE=prpLCItemKindVo.getAmount();
							}
						}
					}
					if(InfoVo1!=null && InfoVo1.size()>0){
					    for(PrplOldClaimRiskInfoVo infoVo:InfoVo1){
					      if("1".equals(infoVo.getIsDanageKindL())){
					    	   BI_HHJE=BI_HHJE.subtract(infoVo.getKindLPayment());
					      }
					     }
					}
					if(prpEndorVos!=null && prpEndorVos.size()>0){
						for(PrpLEndorVo prpLEndorVo:prpEndorVos){
						  if("L".equals(prpLEndorVo.getKindCode())){
							  BI_HHJE= BI_HHJE.subtract(prpLEndorVo.getEndorAmount());
						  }
						}
					}
					registRiskInfoMap.put("BI-HHJE",BI_HHJE+"");//车身划痕剩余保额
					
					PrpLRegistAndCMainVo vo = registService.findPrpLCMainVoByPolicyNo(policyNo);
					cMainVo = vo;
					if(vo.getPrpLRegistVo()!=null){
						List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
						//将同一保单号的记录，按出险时间降序排
						Collections.sort(revos, new Comparator<PrpLRegistVo>() {
						@Override
						public int compare(PrpLRegistVo o1,PrpLRegistVo o2) {
								return o2.getDamageTime().compareTo(o1.getDamageTime());
							}
						});
						//将最新一次的出险时间，往前推7天
						if(revos!=null && revos.size()>0){
						Date newTime =DateUtils.addDays(revos.get(0).getDamageTime(),-7);
						//商业险保单七天内出险次数
						int BIDangerinSum=0;
						for(PrpLRegistVo prpLre:revos){
							if(prpLre.getDamageTime().getTime()>=newTime.getTime()){
								BIDangerinSum=BIDangerinSum+1;
							}
						}
						
						registRiskInfoMap.put("BI-DangerInSum",String.valueOf(BIDangerinSum));
						
						//是否48小时之内是否重复报案
						Date LastNewTime =revos.get(0).getDamageTime();//表中存在的最后一次出险时间
						Date TwoLaterTime =DateUtils.addDays(LastNewTime,2);//表中存在最后一次出险时间的后两天的时间
						Date TwoEarlierTime = DateUtils.addDays(LastNewTime,-2);
						Date SecondTime =null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
						Date TwoSecondTime =null;//表中时间两天后的出险时间
						Date TwoSecondEarlierTime =null;
					   if(damageDate!=null){
						   if(damageDate.getTime()!=LastNewTime.getTime()){
							   if (damageDate.getTime() >= LastNewTime.getTime() && damageDate.getTime() <= TwoLaterTime.getTime() ||
									   damageDate.getTime() >= TwoEarlierTime.getTime() && damageDate.getTime() <= LastNewTime.getTime()) {
								   registRiskInfoMap.put("BIRepeatReport", "1");
							   }
						   }else{
							   if(revos.size()>=2){
								   SecondTime=revos.get(1).getDamageTime();
								   TwoSecondTime=DateUtils.addDays(SecondTime,2);
								   TwoSecondEarlierTime = DateUtils.addDays(SecondTime,-2);
								   if (damageDate.getTime() >= SecondTime.getTime() && damageDate.getTime() <= TwoSecondTime.getTime() ||
										   damageDate.getTime() >= TwoSecondEarlierTime.getTime() && damageDate.getTime() <= SecondTime.getTime()) {
									   registRiskInfoMap.put("BIRepeatReport", "1");
								   }
							   }
						   }
					   }else{
						   if(revos.size()>=2){
							   SecondTime=revos.get(1).getDamageTime();
							   TwoSecondTime=DateUtils.addDays(SecondTime,2);
						      if(LastNewTime.getTime()>=SecondTime.getTime() && LastNewTime.getTime()<=TwoSecondTime.getTime()){
							     registRiskInfoMap.put("BIRepeatReport", "1");
						        }
						    }
					 }
						}
					}
					if(vo.getPrpLCMainVo() != null){
						List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
						registRiskInfoMap.put("BI-DangerNum",String.valueOf(vo1.size()+InfoVo1.size()));
						//商业保单生效后7天内出险
						int BIPolicy_A7 = 0;
						int BIPolicy_B7 = 0;
						for(PrpLCMainVo prpLCMainVo:vo1){
							Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
							for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
								if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
									BIPolicy_A7 = BIPolicy_A7+1;
								}
								if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
									BIPolicy_B7 = BIPolicy_B7+1;
								}
								
							}
							registRiskInfoMap.put("BIPolicy-B7", String.valueOf(BIPolicy_B7));
							registRiskInfoMap.put("BIPolicy-A7", String.valueOf(BIPolicy_A7));
						}
					}else{
						registRiskInfoMap.put("BI-DangerNum",String.valueOf(InfoVo1.size()));
					}
					//交强险信息拼装
					if (!StringUtils.isEmpty(linkpolicyNo)) {
						registRiskInfoMap.put("CI-No", linkpolicyNo);
						//TODO
						vo = registService.findPrpLCMainVoByPolicyNo(linkpolicyNo);
						List<PrplOldClaimRiskInfoVo> InfoVo2 = registService.findPrploldclaimriskinfoByPolicyNo(linkpolicyNo);
						if(vo.getPrpLRegistVo()!=null){
							List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
							//将同一保单号的记录，按出险时间降序排
							Collections.sort(revos, new Comparator<PrpLRegistVo>() {
							@Override
							public int compare(PrpLRegistVo o1,PrpLRegistVo o2) {
									return o2.getDamageTime().compareTo(o1.getDamageTime());
								}
							});
							//将最新一次的出险时间，往前推7天
							if(revos!=null && revos.size()>0){
							Date newTime =DateUtils.addDays(revos.get(0).getDamageTime(),-7);
							//交强险保单七天内出险次数
							int CIDangerinSum=0;
							for(PrpLRegistVo prpLre:revos){
								if(prpLre.getDamageTime().getTime()>=newTime.getTime()){
									CIDangerinSum=CIDangerinSum+1;
								}
							}
							
							registRiskInfoMap.put("CI-DangerInSum",String.valueOf(CIDangerinSum));
							
							//是否48小时之内是否重复报案
							Date LastNewTime =revos.get(0).getDamageTime();//表中存在的最后一次出险时间
							Date TwoLaterTime =DateUtils.addDays(LastNewTime,2);//表中存在最后一次出险时间的后两天的时间
							Date TwoEarlierTime =DateUtils.addDays(LastNewTime,2);
							Date SecondTime =null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
							Date TwoSecondTime =null;//表中时间两天后的出险时间
							Date TwoSecondEarlierTime = null;
						   if(damageDate!=null){
							   if(damageDate.getTime()!=LastNewTime.getTime()){
							       if(damageDate.getTime()>=LastNewTime.getTime() && damageDate.getTime()<=TwoLaterTime.getTime() ||
								   damageDate.getTime() >= TwoEarlierTime.getTime() && damageDate.getTime() <= LastNewTime.getTime()){
								     registRiskInfoMap.put("CIRepeatReport", "1");
								    }
							   }else{
								   if(revos.size()>=2){
									   SecondTime=revos.get(1).getDamageTime();
									   TwoSecondTime=DateUtils.addDays(SecondTime,2);
								     if(damageDate.getTime()>=SecondTime.getTime() && damageDate.getTime()<=TwoSecondTime.getTime() ||
									 damageDate.getTime() >= TwoSecondEarlierTime.getTime() && damageDate.getTime() <= SecondTime.getTime()){
									   registRiskInfoMap.put("CIRepeatReport", "1");
								      }
								   }
							   }
						   }else{
							   if(revos.size()>=2){
								   SecondTime=revos.get(1).getDamageTime();
								   TwoSecondTime=DateUtils.addDays(SecondTime,2);
							      if(LastNewTime.getTime()>=SecondTime.getTime() && LastNewTime.getTime()<=TwoSecondTime.getTime()){
								     registRiskInfoMap.put("CIRepeatReport", "1"); 
							        }
							    }
						 }
							}
						}
						if(vo.getPrpLCMainVo() != null){
							List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
							registRiskInfoMap.put("CI-DangerNum",String.valueOf(vo1.size()+InfoVo2.size()));
							//交强保单生效后7天内出险
							int CIPolicy_A7 = 0;
							int CIPolicy_B7 = 0;
							for(PrpLCMainVo prpLCMainVo:vo1){
								Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
								for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
									if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
										CIPolicy_A7 = CIPolicy_A7+1;
									}
									if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
										CIPolicy_B7 = CIPolicy_B7+1;
									}
									
								}
								registRiskInfoMap.put("CIPolicy-B7", String.valueOf(CIPolicy_B7));
								registRiskInfoMap.put("CIPolicy-A7", String.valueOf(CIPolicy_A7));
								
							}
						}else{
							registRiskInfoMap.put("CI-DangerNum",String.valueOf(InfoVo2.size()));
						}
						
					}
				}
			}else{
				if (StringUtils.equals("1101", policyNo.substring(11, 15))) {
					//交强险信息拼装
					registRiskInfoMap.put("CI-No", policyNo);
					//TODO
					PrpLRegistAndCMainVo vo = registService.findPrpLCMainVoByPolicyNo(policyNo);
					cMainVo = vo;
					List<PrplOldClaimRiskInfoVo> InfoVo1 = registService.findPrploldclaimriskinfoByPolicyNo(policyNo);
					if(vo.getPrpLRegistVo()!=null) {
						List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
						//将同一保单号的记录，按出险时间降序排
						Collections.sort(revos, new Comparator<PrpLRegistVo>() {
							@Override
							public int compare(PrpLRegistVo o1, PrpLRegistVo o2) {
								return o2.getDamageTime().compareTo(o1.getDamageTime());
							}
						});
						//将最新一次的出险时间，往前推7天
						if (revos != null && revos.size() > 0) {
							Date newTime = DateUtils.addDays(revos.get(0).getDamageTime(), -7);

							//交强险保单七天内出险次数
							int CIDangerinSum = 0;
							for (PrpLRegistVo prpLre : revos) {
								if (prpLre.getDamageTime().getTime() >= newTime.getTime()) {
									CIDangerinSum = CIDangerinSum + 1;
								}
							}

							registRiskInfoMap.put("CI-DangerInSum", String.valueOf(CIDangerinSum));
							//是否48小时之内是否重复报案
							Date LastNewTime = revos.get(0).getDamageTime();//表中存在的最后一次出险时间
							Date TwoLaterTime = DateUtils.addDays(LastNewTime, 2);//表中存在最后一次出险时间的后两天的时间
							Date TwoEarlierTime = DateUtils.addDays(LastNewTime, -2);
							Date SecondTime = null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
							Date TwoSecondTime = null;//表中时间两天后的出险时间
							Date TwoSecondEarlierTime = null;
							if (damageDate != null) {
								if (damageDate.getTime() != LastNewTime.getTime()) {
									if (TwoEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoLaterTime.getTime()) {
										registRiskInfoMap.put("CIRepeatReport", "1");
									}
								} else {
									if (revos.size() >= 2) {
										SecondTime = revos.get(1).getDamageTime();
										TwoSecondTime = DateUtils.addDays(SecondTime, 2);
										TwoSecondEarlierTime = DateUtils.addDays(SecondTime, -2);
										if (TwoSecondEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoSecondTime.getTime()) {
											registRiskInfoMap.put("CIRepeatReport", "1");
										}
									} else {
										String currentRegistNo = registVo.getRegistNo();
										if (currentRegistNo == null) {
											registRiskInfoMap.put("CIRepeatReport", "1");
										}
									}
								}
							} else {
								if (revos.size() >= 2) {
									SecondTime = revos.get(1).getDamageTime();
									TwoSecondTime = DateUtils.addDays(SecondTime, 2);
									if (LastNewTime.getTime() >= SecondTime.getTime() && LastNewTime.getTime() <= TwoSecondTime.getTime()) {
										registRiskInfoMap.put("CIRepeatReport", "1");
									}
								}
							}
						}
					}
					if(vo.getPrpLCMainVo() != null){
						List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
						registRiskInfoMap.put("CI-DangerNum",String.valueOf(vo1.size()+InfoVo1.size()));
						//交强保单生效后7天内出险
						int CIPolicy_A7 = 0;
						int CIPolicy_B7 = 0;
						for(PrpLCMainVo prpLCMainVo:vo1){
							Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
							for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
								if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
									CIPolicy_A7 = CIPolicy_A7+1;
								}
								if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
									CIPolicy_B7 = CIPolicy_B7+1;
								}
								
							}
							registRiskInfoMap.put("CIPolicy-B7", String.valueOf(CIPolicy_B7));
							registRiskInfoMap.put("CIPolicy-A7", String.valueOf(CIPolicy_A7));
							
						}
					}else{
						registRiskInfoMap.put("CI-DangerNum",String.valueOf(InfoVo1.size()));
					}

					//商业险信息拼装
					if (!StringUtils.isEmpty(linkpolicyNo)) {
						registRiskInfoMap.put("BI-No", linkpolicyNo);
						//TODO
						vo = registService.findPrpLCMainVoByPolicyNo(linkpolicyNo);
						List<PrplOldClaimRiskInfoVo> InfoVo2 = registService.findPrploldclaimriskinfoByPolicyNo(linkpolicyNo);
						List<PrpLEndorVo> prpEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(linkpolicyNo);
						List<PrpLCItemKindVo> prpLCItemKindVos=new ArrayList<PrpLCItemKindVo>();
						if(registVo!=null){
							prpLCItemKindVos=policyViewService.findItemKindVos(linkpolicyNo,registVo.getRegistNo(),"L");
							
						}
						//车身划痕出险次数
						int BI_CSHH=0;
						BigDecimal BI_HHJE=new BigDecimal("0");
						if(InfoVo2!=null && InfoVo2.size()>0){
							for(PrplOldClaimRiskInfoVo info:InfoVo2){
								if("1".equals(info.getIsDanageKindL())){
									BI_CSHH++;
								}
							}
						}
						
						if(prpEndorVos!=null && prpEndorVos.size()>0){
							for(PrpLEndorVo prpLEndorVo:prpEndorVos){
								if("L".equals(prpLEndorVo.getKindCode())){
									BI_CSHH++;	
								}
							}
						}
						registRiskInfoMap.put("BI-CSHH",BI_CSHH+"");//车身划痕出险次数
						
						//车身划痕剩余保额
						if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
							for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVos){
								if("L".equals(prpLCItemKindVo.getKindCode())){
									BI_HHJE=prpLCItemKindVo.getAmount();
								}
							}
						}
						if(InfoVo2!=null && InfoVo2.size()>0){
						    for(PrplOldClaimRiskInfoVo infoVo:InfoVo2){
						      if("1".equals(infoVo.getIsDanageKindL())){
						    	  BI_HHJE=BI_HHJE.subtract(infoVo.getKindLPayment());
						      }
						     }
						}
						if(prpEndorVos!=null && prpEndorVos.size()>0){
							for(PrpLEndorVo prpLEndorVo:prpEndorVos){
							  if("L".equals(prpLEndorVo.getKindCode())){
								  BI_HHJE=BI_HHJE.subtract(prpLEndorVo.getEndorAmount());
							  }
							}
						}
						registRiskInfoMap.put("BI-HHJE",BI_HHJE+"");//车身划痕剩余保额

						if (vo.getPrpLRegistVo() != null) {
							List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
							//将同一保单号的记录，按出险时间降序排
							Collections.sort(revos, new Comparator<PrpLRegistVo>() {
								@Override
								public int compare(PrpLRegistVo o1, PrpLRegistVo o2) {
									return o2.getDamageTime().compareTo(o1.getDamageTime());
								}
							});
							//将最新一次的出险时间，往前推7天
							if (revos != null && revos.size() > 0) {
								Date newTime = DateUtils.addDays(revos.get(0).getDamageTime(), -7);
								//商业险保单七天内出险次数
								int BIDangerinSum = 0;
								for (PrpLRegistVo prpLre : revos) {
									if (prpLre.getDamageTime().getTime() >= newTime.getTime()) {
										BIDangerinSum = BIDangerinSum + 1;
									}
								}

								registRiskInfoMap.put("BI-DangerInSum", String.valueOf(BIDangerinSum));
								//是否48小时之内是否重复报案
								Date LastNewTime = revos.get(0).getDamageTime();//表中存在的最后一次出险时间
								Date TwoLaterTime = DateUtils.addDays(LastNewTime, 2);//表中存在最后一次出险时间的后两天的时间
								Date TwoEarlierTime = DateUtils.addDays(LastNewTime, -2);
								Date SecondTime = null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
								Date TwoSecondTime = null;//表中时间两天后的出险时间
								Date TwoSecondEarlierTime = null;
								if (damageDate != null) {
									if (damageDate.getTime() != LastNewTime.getTime()) {
										if (damageDate.getTime() >= LastNewTime.getTime() && damageDate.getTime() <= TwoLaterTime.getTime() ||
										damageDate.getTime() >= TwoEarlierTime.getTime() && damageDate.getTime() <= LastNewTime.getTime()) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									} else {
										if (revos.size() >= 2) {
											SecondTime = revos.get(1).getDamageTime();
											TwoSecondTime = DateUtils.addDays(SecondTime, 2);
											TwoSecondEarlierTime = DateUtils.addDays(SecondTime, -2);
											if (damageDate.getTime() >= SecondTime.getTime() && damageDate.getTime() <= TwoSecondTime.getTime() ||
											damageDate.getTime() >= TwoSecondEarlierTime.getTime() && damageDate.getTime() <= SecondTime.getTime()) {
												registRiskInfoMap.put("BIRepeatReport", "1");
											}
										}
									}
								} else {
									if (revos.size() >= 2) {
										SecondTime = revos.get(1).getDamageTime();
										TwoSecondTime = DateUtils.addDays(SecondTime, 2);
										if (LastNewTime.getTime() >= SecondTime.getTime() && LastNewTime.getTime() <= TwoSecondTime.getTime()) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									}
								}
							}
						}
						if(vo.getPrpLCMainVo() != null){
							List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
							registRiskInfoMap.put("BI-DangerNum",String.valueOf(vo1.size()+InfoVo2.size()));
							//商业保单生效后7天内出险
							int BIPolicy_A7 = 0;
							int BIPolicy_B7 = 0;
							for(PrpLCMainVo prpLCMainVo:vo1){
								Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
								for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
									if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
										BIPolicy_A7 = BIPolicy_A7+1;
									}
									if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
										BIPolicy_B7 = BIPolicy_B7+1;
									}
									
								}
								registRiskInfoMap.put("BIPolicy-B7", String.valueOf(BIPolicy_B7));
								registRiskInfoMap.put("BIPolicy-A7", String.valueOf(BIPolicy_A7));
							}
						}else{
							registRiskInfoMap.put("BI-DangerNum",String.valueOf(InfoVo2.size()));
						}
						
					}
				} else {
					//商业先信息拼装
					registRiskInfoMap.put("BI-No", policyNo);
					//TODO
					PrpLRegistAndCMainVo vo = registService.findPrpLCMainVoByPolicyNo(policyNo);
					cMainVo = vo;
					List<PrplOldClaimRiskInfoVo> InfoVo1 = registService.findPrploldclaimriskinfoByPolicyNo(policyNo);
					List<PrpLEndorVo> prpEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(policyNo);
					List<PrpLCItemKindVo> prpLCItemKindVos=new ArrayList<PrpLCItemKindVo>();
					if(registVo!=null){
						prpLCItemKindVos=policyViewService.findItemKindVos(policyNo,registVo.getRegistNo(),"L");
						
					}
					//车身划痕出险次数
					int BI_CSHH=0;
					BigDecimal BI_HHJE=new BigDecimal("0");
					if(InfoVo1!=null && InfoVo1.size()>0){
						for(PrplOldClaimRiskInfoVo info:InfoVo1){
							if("1".equals(info.getIsDanageKindL())){
								BI_CSHH++;
							}
						}
					}
					
					if(prpEndorVos!=null && prpEndorVos.size()>0){
						for(PrpLEndorVo prpLEndorVo:prpEndorVos){
							if("L".equals(prpLEndorVo.getKindCode())){
								BI_CSHH++;	
							}
						}
					}
					registRiskInfoMap.put("BI-CSHH",BI_CSHH+"");//车身划痕出险次数
					
					//车身划痕剩余保额
					if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
						for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVos){
							if("L".equals(prpLCItemKindVo.getKindCode())){
								BI_HHJE=prpLCItemKindVo.getAmount();
							}
						}
					}
					if(InfoVo1!=null && InfoVo1.size()>0){
					    for(PrplOldClaimRiskInfoVo infoVo:InfoVo1){
					      if("1".equals(infoVo.getIsDanageKindL())){
					    	  BI_HHJE=BI_HHJE.subtract(infoVo.getKindLPayment());
					      }
					     }
					}
					if(prpEndorVos!=null && prpEndorVos.size()>0){
						for(PrpLEndorVo prpLEndorVo:prpEndorVos){
						  if("L".equals(prpLEndorVo.getKindCode())){
							  BI_HHJE=BI_HHJE.subtract(prpLEndorVo.getEndorAmount());
						  }
						}
					}
					registRiskInfoMap.put("BI-HHJE",BI_HHJE+"");//车身划痕剩余保额

					if (vo.getPrpLRegistVo() != null) {
						List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
						//将同一保单号的记录，按出险时间降序排
						Collections.sort(revos, new Comparator<PrpLRegistVo>() {
							@Override
							public int compare(PrpLRegistVo o1, PrpLRegistVo o2) {
								return o2.getDamageTime().compareTo(o1.getDamageTime());
							}
						});
						//将最新一次的出险时间，往前推7天
						if (revos != null && revos.size() > 0) {
							Date newTime = DateUtils.addDays(revos.get(0).getDamageTime(), -7);
							//商业险保单七天内出险次数
							int BIDangerinSum = 0;
							for (PrpLRegistVo prpLre : revos) {
								if (prpLre.getDamageTime().getTime() >= newTime.getTime()) {
									BIDangerinSum = BIDangerinSum + 1;
								}
							}

							registRiskInfoMap.put("BI-DangerInSum", String.valueOf(BIDangerinSum));
							//是否48小时之内是否重复报案
							Date LastNewTime = revos.get(0).getDamageTime();//表中存在的最后一次出险时间
							Date TwoLaterTime = DateUtils.addDays(LastNewTime, 2);//表中存在最后一次出险时间的后两天的时间
							// 表中最后一次出险时间两天前
							Date TwoEarlierTime = DateUtils.addDays(LastNewTime, -2);
							Date SecondTime = null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
							Date TwoSecondTime = null;//表中时间两天后的出险时间
							Date TwoSecondEarlierTime = null;
							if (damageDate != null) {
								if (damageDate.getTime() != LastNewTime.getTime()) {
									if (TwoEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoLaterTime.getTime()) {
										registRiskInfoMap.put("BIRepeatReport", "1");
									}
								} else {
									if (revos.size() >= 2) {
										SecondTime = revos.get(1).getDamageTime();
										TwoSecondTime = DateUtils.addDays(SecondTime, 2);
										TwoSecondEarlierTime = DateUtils.addDays(SecondTime, -2);
										if (TwoSecondEarlierTime.getTime() <= damageDate.getTime() && damageDate.getTime() <= TwoSecondTime.getTime()) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									} else {
										String currentRegistNo = registVo.getRegistNo();
										if (currentRegistNo == null) {
											registRiskInfoMap.put("BIRepeatReport", "1");
										}
									}
								}
							} else {
								if (revos.size() >= 2) {
									SecondTime = revos.get(1).getDamageTime();
									TwoSecondTime = DateUtils.addDays(SecondTime, 2);
									if (LastNewTime.getTime() >= SecondTime.getTime() && LastNewTime.getTime() <= TwoSecondTime.getTime()) {
										registRiskInfoMap.put("BIRepeatReport", "1");
									}
								}
							}
						}
					}
					if(vo.getPrpLCMainVo() != null){
						List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
						registRiskInfoMap.put("BI-DangerNum",String.valueOf(vo1.size()+InfoVo1.size()));
						//商业保单生效后7天内出险
						int BIPolicy_A7 = 0;
						int BIPolicy_B7 = 0;
						for(PrpLCMainVo prpLCMainVo:vo1){
							Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
							for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
								if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
									BIPolicy_A7 = BIPolicy_A7+1;
								}
								if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
									BIPolicy_B7 = BIPolicy_B7+1;
								}
								
							}
							registRiskInfoMap.put("BIPolicy-B7", String.valueOf(BIPolicy_B7));
							registRiskInfoMap.put("BIPolicy-A7", String.valueOf(BIPolicy_A7));
						}
					}else{
						registRiskInfoMap.put("BI-DangerNum",String.valueOf(InfoVo1.size()));
					}
					//交强险信息拼装
					if (!StringUtils.isEmpty(linkpolicyNo)) {
						registRiskInfoMap.put("CI-No", linkpolicyNo);
						//TODO
						List<PrplOldClaimRiskInfoVo> InfoVo2 = registService.findPrploldclaimriskinfoByPolicyNo(linkpolicyNo);
						 vo = registService.findPrpLCMainVoByPolicyNo(linkpolicyNo);
						 if(vo.getPrpLRegistVo()!=null){
								List<PrpLRegistVo> revos = vo.getPrpLRegistVo();
								//将同一保单号的记录，按出险时间降序排
								Collections.sort(revos, new Comparator<PrpLRegistVo>() {
								@Override
								public int compare(PrpLRegistVo o1,PrpLRegistVo o2) {
										return o2.getDamageTime().compareTo(o1.getDamageTime());
									}
								});
								//将最新一次的出险时间，往前推7天
								if(revos!=null && revos.size()>0){
								Date newTime =DateUtils.addDays(revos.get(0).getDamageTime(),-7);
								//交强险保单七天内出险次数
								int CIDangerinSum=0;
								for(PrpLRegistVo prpLre:revos){
									if(prpLre.getDamageTime().getTime()>=newTime.getTime()){
										CIDangerinSum=CIDangerinSum+1;
									}
								}
								
								registRiskInfoMap.put("CI-DangerInSum",String.valueOf(CIDangerinSum));
								//是否48小时之内是否重复报案
								Date LastNewTime =revos.get(0).getDamageTime();//表中存在的最后一次出险时间
								Date TwoLaterTime =DateUtils.addDays(LastNewTime,2);//表中存在最后一次出险时间的后两天的时间
								Date TwoEarlierTime =DateUtils.addDays(LastNewTime,-2);
								Date SecondTime =null;//当表中存在的最新时间为本次出险时间的时候，则取上一次出险时间为基准
								Date TwoSecondTime =null;//表中时间两天后的出险时间
								Date TwoSecondEarlierTime =null;
							   if(damageDate!=null){
								   if(damageDate.getTime()!=LastNewTime.getTime()){
								       if(damageDate.getTime()>=LastNewTime.getTime() && damageDate.getTime()<=TwoLaterTime.getTime() ||
									   damageDate.getTime() >= TwoEarlierTime.getTime() && damageDate.getTime() < LastNewTime.getTime()){
									     registRiskInfoMap.put("CIRepeatReport", "1");
									    }
								   }else{
									   if(revos.size()>=2){
										   SecondTime=revos.get(1).getDamageTime();
										   TwoSecondTime=DateUtils.addDays(SecondTime,2);
										   TwoSecondEarlierTime = DateUtils.addDays(SecondTime,-2);
									     if(damageDate.getTime()>=SecondTime.getTime() && damageDate.getTime()<=TwoSecondTime.getTime() ||
										 damageDate.getTime() >= TwoSecondEarlierTime.getTime() && damageDate.getTime() <= SecondTime.getTime()){
										   registRiskInfoMap.put("CIRepeatReport", "1");
									      }
									   }
								   }
							   }else{
								   if(revos.size()>=2){
									   SecondTime=revos.get(1).getDamageTime();
									   TwoSecondTime=DateUtils.addDays(SecondTime,2);
								      if(LastNewTime.getTime()>=SecondTime.getTime() && LastNewTime.getTime()<=TwoSecondTime.getTime()){
									     registRiskInfoMap.put("CIRepeatReport", "1"); 
								        }
								    }
							 }
								}
							}
						if(vo.getPrpLCMainVo() != null){
							List<PrpLCMainVo> vo1 = vo.getPrpLCMainVo();
							registRiskInfoMap.put("CI-DangerNum",String.valueOf(vo1.size()+InfoVo2.size()));
							//交强保单生效后7天内出险
							int CIPolicy_A7 = 0;
							int CIPolicy_B7 = 0;
							for(PrpLCMainVo prpLCMainVo:vo1){
								Date nows = DateUtils.addDays(prpLCMainVo.getStartDate(),7);
								for(PrpLRegistVo prpLRegistVo:vo.getPrpLRegistVo()){
									if(nows.getTime() > prpLRegistVo.getDamageTime().getTime()){
										CIPolicy_A7 = CIPolicy_A7+1;
									}
									if(DateUtils.compareDays(prpLCMainVo.getEndDate(), prpLRegistVo.getDamageTime()) <= 7 ){
										CIPolicy_B7 = CIPolicy_B7+1;
									}
									
								}
								registRiskInfoMap.put("CIPolicy-B7", String.valueOf(CIPolicy_B7));
								registRiskInfoMap.put("CIPolicy-A7", String.valueOf(CIPolicy_A7));
								
							}
						}else{
							registRiskInfoMap.put("CI-DangerNum",String.valueOf(InfoVo2.size()));
						}
						
					}
				}
			}

			//异地出险DifferentDanger
			if(cMainVo.getPrpLCMainVo()!=null && cMainVo.getPrpLCMainVo().size()>0){
				String code = cMainVo.getPrpLCMainVo().get(0).getComCode().substring(0, 4)+"0000";
				//根据机构代码查地区代码
				//String[] a = areaDictService.findAreaByComCode(code);
				//String[] a = areaDictService.findAreaByComCode(code,new BigDecimal(2));
				SysAreaDict po = areaDictService.findAreaByComCode(code,new BigDecimal(2));
				if(registVo!=null&&registVo.getDamageAreaCode()!=null){
					if(po!=null &&registVo.getDamageAreaCode().equals(po.getAreaCode())){
						registRiskInfoMap.put("DifferentDanger", "1");
					}
				}
			}
			
			
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(damageDate);
		
		//是否夜间出险
		SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
		long start = hf.parse("22:00").getTime();
		long end = hf.parse("06:00").getTime();
		String damageStr = String.valueOf(damageDate.getHours()) + ":" + String.valueOf(damageDate.getMinutes());
		long dh = hf.parse(damageStr).getTime();
		if(start<=dh || end>=dh){
			registRiskInfoMap.put("YJCX", "1");
		}
		
		//报案时间超过48小时
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date dateNow = new Date();
		if (calendar.getTime().getTime() < dateNow.getTime()) {
			registRiskInfoMap.put("BA-D48", "1");
		}
		
		
		//保单生效后7天内出险
	/*	for(PrpLCMainVo vo : vos){
			registRiskInfoMap.put("Policy-A7", "1");
			Date nows = DateUtils.addDays(vo.getStartDate(),7);
			if()
		}*/
		
		
		
		//保单失效前7天内出险
		
		/*registRiskInfoMap.put("Policy-B7", "1");*/
		
		
		//车身划痕出险次数
		
		//剩余金额
		
		return registRiskInfoMap;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#updateRiskInfo(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public void updateRiskInfo(PrpLRegistVo prpLRegistVo) throws ParseException{
		// TODO Auto-generated method stub
		
	//报案新增保存是保存风险提示信息
	Map<String, String> registRiskInfoMap = new HashMap<String, String>();
	supplementRiskInfo(registRiskInfoMap, prpLRegistVo.getDamageTime(), prpLRegistVo.getPolicyNo(), prpLRegistVo.getPrpLRegistExt().getPolicyNoLink(),prpLRegistVo);
	//代位求偿
	registRiskInfoMap.put("DWQC", prpLRegistVo.getPrpLRegistExt().getIsSubRogation());
	registService.deleteByRegistNo(prpLRegistVo);
	registService.saveRiskInfos(prpLRegistVo, registRiskInfoMap);
	}
	
	
	/**
	 * 报案登记新增时，初始化信息
	 * @param prpLRegist
	 * @param prpLCMains
	 */
	private void supplementInfo(PrpLRegistVo prpLRegist, List<PrpLCMainVo> prpLCMains) {
		String userCode = ServiceUserUtils.getUserCode();
		String comCode = ServiceUserUtils.getComCode();
		/*String userCode = "0000000000";
		String comCode = "10000000";*/
		if("1".equals(prpLRegist.getIsQuickCase())){
		    userCode = "AUTO";
		    if(prpLCMains!=null&&prpLCMains.size()>0){
                for(PrpLCMainVo prpLCMainVo:prpLCMains){
                    comCode = prpLCMainVo.getComCode();
                    if( !"1101".equals(prpLCMainVo.getRiskCode())){
                        break;
                    }
                }
            }
		}
		/*平安联盟案件也默认设置AUTO*/
		if("1".equals(prpLRegist.getSelfClaimFlag()) || StringUtils.isNotBlank(prpLRegist.getPaicReportNo())){
		    userCode = "AUTO";
		    if(prpLCMains!=null&&prpLCMains.size()>0){
                for(PrpLCMainVo prpLCMainVo:prpLCMains){
                    comCode = prpLCMainVo.getComCode();
                    if( !"1101".equals(prpLCMainVo.getRiskCode())){
                        break;
                    }
                }
            }
		}
		
		if (StringUtils.isEmpty(prpLRegist.getRegistNo())
				|| StringUtils.isEmpty(prpLRegist.getRegistTaskFlag())) {
			//初登人员和机构
			
			System.out.println(userCode);
			prpLRegist.setFirstRegUserCode(userCode);
			prpLRegist.setFirstRegComCode(comCode);
			prpLRegist.setFirstRegUserName("初登姓名");
			//创建人和时间
			prpLRegist.setCreateUser(userCode);
			prpLRegist.setCreateTime(new Date());
			//设置案件紧急程度
			if (StringUtils.equals(prpLRegist.getPrpLRegistExt().getCheckType(), "3")) {
				prpLRegist.setMercyFlag(CodeConstants.CaseTag.EMERGENCY);
			} else {
				prpLRegist.setMercyFlag(CodeConstants.CaseTag.NORMAL);
			}
			//迭代保单信息
			if (prpLCMains != null && prpLCMains.size() > 0) {
				for (PrpLCMainVo vo : prpLCMains) {
					if ( StringUtils.equals(vo.getPolicyNo(),prpLRegist.getPolicyNo()) ) {
						prpLRegist.setComCode(vo.getComCode());
						prpLRegist.setRiskCode(vo.getRiskCode());
					}
					vo.setValidFlag(CodeConstants.ValidFlag.VALID);
					vo.setCreateUser(userCode);
					vo.setCreateTime(new Date());
					vo.setUpdateUser(userCode);
					vo.setUpdateTime(new Date());
					//保单表添加共保信息
					try{
						String[] plyNoArr = new String[1];
						plyNoArr[0]=vo.getPolicyNo();
						Set<Map<String,String>> sets = policyQueryService.findPrpCMainBySQL(plyNoArr);
						if(sets!=null && !sets.isEmpty()){
							for(Map<String,String> map : sets){
								if("1,2,3,4".indexOf(map.get("coinsflag"))!=-1){
									prpLRegist.setIsGBFlag(map.get("coinsflag"));
								}else{
									prpLRegist.setIsGBFlag("0");
								}
							} 
						}else {
							prpLRegist.setIsGBFlag("0");
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		
		
		prpLRegist.setUpdateUser(userCode);
		prpLRegist.setUpdateTime(new Date());
		prpLRegist.getPrpLRegistExt().setUpdateUser(userCode);
		prpLRegist.getPrpLRegistExt().setUpdateTime(new Date());
		
		List<PrpLRegistCarLossVo> carList = prpLRegist.getPrpLRegistCarLosses();
		//迭代车辆信息,设置初始值
		if (carList != null && carList.size() > 0) {
			for (int i = 0; i < carList.size(); i ++) {
				PrpLRegistCarLossVo vo = carList.get(i);
				if (vo != null) {
					String brand = vo.getBrand();
					String address = prpLRegist.getPrpLRegistExt().getCheckAddressCode();
					this.setRepairFactory(vo,brand,address,prpLRegist.getRegistNo());
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(userCode);
						vo.setCreateTime(new Date());
					}
//					if(vo.getLossparty().equals("1")){
//						vo.setLicenseType(prpLCMains.get(0).getPrpCItemCars().get(0).getLicenseKindCode());
//					}
					vo.setUpdateUser(userCode);
					vo.setUpdateTime(new Date());
					//默认将界面驾驶员信息写入车辆损失表中
					if (i == 0) {
						vo.setThriddrivername(prpLRegist.getDriverName());
						vo.setThriddriverphone(prpLRegist.getDriverPhone());
						vo.setThriddrivingno(prpLRegist.getDriverIdfNo());
					}
				} else {
					carList.remove(i);
					i --;
				}
			}
		} else {
			prpLRegist.getPrpLRegistExt().setIsCarLoss("0");
		}
		
		List<PrpLRegistPropLossVo> propList = prpLRegist.getPrpLRegistPropLosses();
		//迭代物损信息，设置初始值
		if (propList != null && propList.size() > 0) {
			for (int i = 0; i < propList.size(); i ++) {
				PrpLRegistPropLossVo vo = propList.get(i);
				if (vo != null) {
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(userCode);
						vo.setCreateTime(new Date());
					}
					vo.setUpdateUser(userCode);
					vo.setUpdateTime(new Date());
				} else {
					propList.remove(i);
					i --;
				}
			}
		} else if (StringUtils.isBlank(prpLRegist.getPaicReportNo())){//过滤掉平安联盟案件，平安联盟案件以报文接收为准
			prpLRegist.getPrpLRegistExt().setIsPropLoss("0");
		}
		
		List<PrpLRegistPersonLossVo> personList = prpLRegist.getPrpLRegistPersonLosses();
		//迭代人伤信息，设置初始值
		if (personList != null && personList.size() > 0) {
			boolean flag = true;
			for (PrpLRegistPersonLossVo vo : personList) {
				if (vo != null) {
					if (vo.getInjuredcount() > 0 || vo.getDeathcount() > 0) {
						flag = false;
					}
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(userCode);
						vo.setCreateTime(new Date());
					}
					vo.setUpdateUser(userCode);
					vo.setUpdateTime(new Date());
				}
			}
			/*if (flag) {
				prpLRegist.getPrpLRegistExt().setIsPersonLoss("0");
			}*/
		}
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistHandlerService#setRepairFactory(ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setRepairFactory(PrpLRegistCarLossVo vo,String brandName,
	       String address,String registNo){
		//查询标的车的品牌
		if(StringUtils.isEmpty(brandName)&&"1".equals(vo.getLossparty())){
			brandName = this.queryCarModel(registNo);
		}
		vo.setBrand(brandName);
		//查询出该出险地区所有修理厂
		List<PrpLRepairFactoryVo> list = null;
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+ServiceUserUtils.getUserCode());

		list = managerService.findFactoryByArea(address,brandName);
		System.out.println("======================================================"+ServiceUserUtils.getUserCode());

		List<PrpLRepairFactoryVo> listTemp = new ArrayList<PrpLRepairFactoryVo>();
		if(list!=null&&!list.isEmpty()){
			for(PrpLRepairFactoryVo repairVo : list){
				String flag = repairVo.getPreferredFlag();
				if(CodeConstants.RadioValue.RADIO_YES.equals(flag)){
					listTemp.add(repairVo);
				}
			}
		}
		if(listTemp != null && listTemp.size() > 0){
			//随机一条修理厂信息
			int index=(int)(Math.random()*listTemp.size());
			PrpLRepairFactoryVo factory = listTemp.get(index);
			vo.setRepairCode(factory.getId().toString());
			vo.setRepairName(factory.getFactoryName());
			if(factory.getMobile() != null && factory.getMobile() != ""){
				vo.setRepairMobile(factory.getMobile());
			}else{
				vo.setRepairMobile(factory.getTelNo());
			}
			vo.setRepairAddress(factory.getAddress());
			vo.setRepairLinker(factory.getLinker());
		}
		if(listTemp.size()==0&&list!=null&&list.size()>0){
			//随机一条修理厂信息
			int index=(int)(Math.random()*list.size());
			PrpLRepairFactoryVo factory = list.get(index);
			vo.setRepairCode(factory.getId().toString());
			vo.setRepairName(factory.getFactoryName());
			if(factory.getMobile() != null && factory.getMobile() != ""){
				vo.setRepairMobile(factory.getMobile());
			}else{
				vo.setRepairMobile(factory.getTelNo());
			}
			vo.setRepairAddress(factory.getAddress());
			vo.setRepairLinker(factory.getLinker());
		}
	}

}
