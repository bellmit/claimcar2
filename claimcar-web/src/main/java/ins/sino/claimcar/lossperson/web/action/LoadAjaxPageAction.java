/******************************************************************************
 * CREATETIME : 2014年6月15日 下午7:45:26
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import ins.framework.common.DateTime;
import ins.framework.service.CodeTranService;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceHisVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;

/**
 * <pre></pre>
 * @author ★LiuPing
 */
@Controller
@RequestMapping("/loadAjaxPage/")
public class LoadAjaxPageAction {

	@Autowired
	CodeDictService codeDictService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	ManagerService managerService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	ClaimTextService claimTextSerVice;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	LossChargeService lossChargeService;
	@Autowired
	AssignService assignService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	@Autowired
	PersTraceHandleService persTraceHandleService;

	


	/**
	 * 人员伤亡信息-选项卡
	 * @param persTracesNum
	 * @return
	 * @modified: ☆XMSH(2016年1月4日 下午5:02:45): <br>
	 */
	@RequestMapping("/loadCasualtyTabCon.ajax")
	public ModelAndView loadCasualtyTabCon(Long persTraceId,int tabPageNo,String registNo,String flowNodeCode) {
		ModelAndView mav = new ModelAndView();

		PrpLDlossPersTraceVo persTraceVo = persTraceService.findPersTraceVo(persTraceId);

		String injuredPart = "";
		if(persTraceVo!=null){
			List<PrpLDlossPersExtVo> persExtVos = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
			for(PrpLDlossPersExtVo persExt:persExtVos){
				injuredPart += persExt.getInjuredPart()+",";
			}
		}
		
		//获取车损信息
		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(registNo);
		// 修改bug，增加单独调度出来的三者车辆
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(registNo);
		if(carMainVoList!=null&& !carMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo carMainVo:carMainVoList){
				Integer serialNo = carMainVo.getSerialNo();
				if( !dataSourceMap.isEmpty()&& !dataSourceMap.containsKey(serialNo.toString()) &&
						"1".equals(carMainVo.getValidFlag())){
					String serialNoStr = serialNo==1 ? "标的车" : "三者车";
					dataSourceMap.put(serialNo.toString(),serialNoStr+"("+carMainVo.getLicenseNo()+")");
				}
			}
		}
		
		mav.addObject("flowNodeCode",flowNodeCode);
		mav.addObject("injuredPart",injuredPart);
		mav.addObject("dataSourceMap",dataSourceMap);
		mav.addObject("tabPageNo",tabPageNo);
		mav.addObject("registNo",registNo);
		mav.addObject("persTrace",persTraceVo);
		mav.addObject("comCode",WebUserUtils.getComCode());
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoView/CasualtyInfo_TabCon.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 加载伤亡人员编辑弹窗
	 * @param traceMainId
	 * @param registNo
	 * @param tabPageNo
	 * @param persTraceId
	 * @return
	 * @modified: ☆XMSH(2016年1月28日 下午2:42:22): <br>
	 */
	@RequestMapping("/loadTabWin.ajax")
	public ModelAndView loadTabWin(Long traceMainId,String registNo,int tabPageNo,String persTraceId,String reconcileFlag,Date checkDate) {
		ModelAndView mav = new ModelAndView();
		PrpLDlossPersTraceVo persTraceVo = null;
		String hasDriver = "";//是否保车上司机 D11
		String hasPassenger = "";//是否保车上乘客 D12
		String injuredPart = "";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isBlank(persTraceId)){
			List<PrpLDlossPersTraceVo> persTraceVos = null;// 人员跟踪表
			persTraceVos = persTraceService.findPersTraceVo(registNo,traceMainId);// 人伤跟踪人员信息
			mav.addObject("prpLDlossPersTraces",persTraceVos);
		}
		if(StringUtils.isBlank(persTraceId)){// 新增
			persTraceVo = new PrpLDlossPersTraceVo();
			persTraceVo.setPersTraceMainId(traceMainId);

			List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFees = new ArrayList<PrpLDlossPersTraceFeeVo>();
			PrpLDlossPersTraceFeeVo traceFeeVo = new PrpLDlossPersTraceFeeVo();
			PrpLDlossPersTraceFeeVo traceFeeVo1 = new PrpLDlossPersTraceFeeVo();
			PrpLDlossPersTraceFeeVo traceFeeVo2 = new PrpLDlossPersTraceFeeVo();
			PrpLDlossPersTraceFeeVo traceFeeVo3 = new PrpLDlossPersTraceFeeVo();
			PrpLDlossPersTraceFeeVo traceFeeVo4 = new PrpLDlossPersTraceFeeVo();
			traceFeeVo.setFeeTypeCode("1");// 上海和全国医疗费
			traceFeeVo1.setFeeTypeCode("3");// 上海误工费，全国营养费
			traceFeeVo2.setFeeTypeCode("4");// 上海住院伙食补助，全国住院伙食补助
			if(registVo.getComCode().startsWith("22")){//上海
				traceFeeVo3.setFeeTypeCode("5");// 护理费
				traceFeeVo4.setFeeTypeCode("11");// 营养费
			}else{
				traceFeeVo3.setFeeTypeCode("6");// 误工费
				traceFeeVo4.setFeeTypeCode("7");// 护理费
			}
			prpLDlossPersTraceFees.add(traceFeeVo);
			prpLDlossPersTraceFees.add(traceFeeVo1);
			prpLDlossPersTraceFees.add(traceFeeVo2);
			prpLDlossPersTraceFees.add(traceFeeVo3);
			prpLDlossPersTraceFees.add(traceFeeVo4);
			mav.addObject("prpLDlossPersTraceFees",prpLDlossPersTraceFees);
			
			if("1".equals(reconcileFlag)){
				PrpLDlossPersHospitalVo persHospital = new PrpLDlossPersHospitalVo();
				persHospital.setInHospitalDate(checkDate);
				mav.addObject("persHospital",persHospital);
			}
		}else{
			persTraceVo = persTraceService.findPersTraceVo(Long.decode(persTraceId));
			List<PrpLDlossPersExtVo> persExtVos = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
			if(persExtVos!=null&&persExtVos.size()>0){
				for(PrpLDlossPersExtVo persExt:persExtVos){
					injuredPart += persExt.getInjuredPart()+",";
				}
			}
		}
		
		//获取车损信息
		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(registNo);
		// 修改bug，增加单独调度出来的三者车辆
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(registNo);
		if(carMainVoList!=null&& !carMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo carMainVo:carMainVoList){
				Integer serialNo = carMainVo.getSerialNo();
				if( !dataSourceMap.isEmpty()&& !dataSourceMap.containsKey(serialNo.toString()) &&
						"1".equals(carMainVo.getValidFlag())){
					String serialNoStr = serialNo==1 ? "标的车" : "三者车";
					dataSourceMap.put(serialNo.toString(),serialNoStr+"("+carMainVo.getLicenseNo()+")");
				}
			}
		}
		
		//获取查勘录入的人员
		List<PrpLCheckPersonVo> checkPersonVos = checkTaskService.findCheckPersonVo(registNo);
		List<SysCodeDictVo> checkPersonList = new ArrayList<SysCodeDictVo>();
		if(checkPersonVos!=null&&checkPersonVos.size()>0){
			for(PrpLCheckPersonVo checkPersonVo : checkPersonVos){
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(checkPersonVo.getId().toString());
				dictVo.setCodeName(checkPersonVo.getPersonName());
				checkPersonList.add(dictVo);
			}
		}
		
		//获取保单险别信息
		Map<String,String> kindMap = new HashMap<String,String>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
		for(PrpLCItemKindVo itemKind:itemKinds){
			kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
		}
		
		//车上司机
		if(kindMap.containsKey("D11")){
			hasDriver = "1";
		}else{
			hasDriver = "0";
		}
		//车上乘客
		if(kindMap.containsKey("D12")){
			hasPassenger = "1";
		}else{
			hasPassenger = "0";
		}
		
		mav.addObject("hasDriver",hasDriver);
		mav.addObject("hasPassenger",hasPassenger);
		mav.addObject("comCode",registVo.getComCode());
		mav.addObject("checkPersonList",checkPersonList);
		mav.addObject("userName",WebUserUtils.getUserName());
		
		mav.addObject("persTrace",persTraceVo);
		mav.addObject("injuredPart",injuredPart);
		mav.addObject("dataSourceMap",dataSourceMap);
		mav.addObject("tabPageNo",tabPageNo);
		mav.addObject("registNo",registNo);
		if(registVo.getDamageTime() != null) {
			mav.addObject("damageTime", format.format(registVo.getDamageTime()));
		}
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/CasualtyInfo_TabCon.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
	
	@RequestMapping("/loadPersonInfo.ajax")
	public ModelAndView loadPersonInfo(String persTraceId) {
		ModelAndView mav = new ModelAndView();
		String injuredPart = "";
		PrpLDlossPersTraceVo persTraceVo = persTraceService.findPersTraceVo(Long.decode(persTraceId));
		List<PrpLDlossPersExtVo> persExtVos = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
		if(persExtVos!=null&&persExtVos.size()>0){
			for(PrpLDlossPersExtVo persExt:persExtVos){
				injuredPart += persExt.getInjuredPart()+",";
			}
		}
		
		//获取车损信息
		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(persTraceVo.getRegistNo());
		// 修改bug，增加单独调度出来的三者车辆
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(persTraceVo.getRegistNo());
		if(carMainVoList!=null&& !carMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo carMainVo:carMainVoList){
				Integer serialNo = carMainVo.getSerialNo();
				if( !dataSourceMap.isEmpty()&& !dataSourceMap.containsKey(serialNo.toString())){
					String serialNoStr = serialNo==1 ? "标的车" : "三者车";
					dataSourceMap.put(serialNo.toString(),serialNoStr+"("+carMainVo.getLicenseNo()+")");
				}
			}
		}
		
		//获取查勘录入的人员
		List<PrpLCheckPersonVo> checkPersonVos = checkTaskService.findCheckPersonVo(persTraceVo.getRegistNo());
		List<SysCodeDictVo> checkPersonList = new ArrayList<SysCodeDictVo>();
		if(checkPersonVos!=null&&checkPersonVos.size()>0){
			for(PrpLCheckPersonVo checkPersonVo : checkPersonVos){
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(checkPersonVo.getId().toString());
				dictVo.setCodeName(checkPersonVo.getPersonName());
				checkPersonList.add(dictVo);
			}
		}
		
		//获取保单险别信息
		Map<String,String> kindMap = new HashMap<String,String>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(persTraceVo.getRegistNo(),"Y");
		for(PrpLCItemKindVo itemKind:itemKinds){
			kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
		}
		
		mav.addObject("checkPersonList",checkPersonList);

		mav.addObject("persTrace",persTraceVo);
		mav.addObject("injuredPart",injuredPart);
		mav.addObject("flowNodeCode","PLBig");
		mav.addObject("tabPageNo",0);
		mav.addObject("dataSourceMap",dataSourceMap);
		mav.addObject("registNo",persTraceVo.getRegistNo());
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoView/CasualtyInfo_TabCon.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 费用修改历史记录窗口
	 * @param traceMainId
	 * @return
	 * @modified: ☆XMSH(2016年2月17日 下午5:40:10): <br>
	 */
	@RequestMapping("/loadFeeEditRecord.ajax")
	public ModelAndView loadFeeEditRecord(Long traceMainId) {
		ModelAndView mav = new ModelAndView();
		PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(traceMainId);
		List<PrpLDlossPersTraceVo> persTraceVos = persTraceMainVo.getPrpLDlossPersTraces();

		if(persTraceVos!=null&&persTraceVos.size()>0){
			for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
				List<PrpLDlossPersTraceHisVo> persTraceHisVos = persTraceService
						.findPersTraceHisVo(persTraceMainVo.getRegistNo(),traceMainId,persTraceVo
								.getPrpLDlossPersInjured().getPersonName());
				persTraceVo.setPersTraceHises(persTraceHisVos);
			}
		}
		mav.addObject("persTraceMainVo",persTraceMainVo);
		mav.addObject("persTraceVos",persTraceVos);
		mav.addObject("LoadPagePath","lossperson/feeModifyRecord/FeeModifyRecord.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
    
	@RequestMapping("/bigFeeEditRecord.ajax")
	public ModelAndView bigFeeEditRecord(String registNo) {
		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		ModelAndView mav = new ModelAndView();
		Long traceMainId = null;
		List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVoByRegistNo(registNo);
		if(persTraceVos!=null&&persTraceVos.size()>0){
			for(PrpLDlossPersTraceVo dlossPersTraceVo:persTraceVos){
				if(traceMainId==null){
					traceMainId = dlossPersTraceVo.getPersTraceMainId();
					break;
				}
			}
			if(traceMainId!=null){
				persTraceMainVo = persTraceService.findPersTraceMainVoById(traceMainId);
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
					List<PrpLDlossPersTraceHisVo> persTraceHisVos = persTraceService.findPersTraceHisVo(registNo,
							traceMainId,persTraceVo.getPrpLDlossPersInjured().getPersonName());
					persTraceVo.setPersTraceHises(persTraceHisVos);
				}
			}
		}
		mav.addObject("persTraceMainVo",persTraceMainVo);
		mav.addObject("persTraceVos",persTraceVos);
		mav.addObject("LoadPagePath","lossperson/feeModifyRecord/FeeModifyRecord.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
	
/**/
	/**
	 * 新增一条受伤部位
	 * @param injuredPartSize
	 * @param tabPageNo
	 * @return
	 * @modified: ☆XMSH(2016年1月4日 下午5:03:17): <br>
	 */
	@RequestMapping("/loadInjuredPartTr.ajax")
	public ModelAndView loadInjuredPartTr(int injuredPartSize,String registNo,String injuredPart) {
		ModelAndView mav = new ModelAndView();
		PrpLDlossPersExtVo persExt = new PrpLDlossPersExtVo();
		persExt.setInjuredPart(injuredPart);
		persExt.setRegistNo(registNo);
		mav.addObject("injuredPartSize",injuredPartSize);
		mav.addObject("persExt",persExt);
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_InjuredPart_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 新增一条住院信息
	 * @param hospitalCaseSize
	 * @param tabPageNo
	 * @return
	 * @modified: ☆XMSH(2016年1月5日 下午2:26:27): <br>
	 */
	@RequestMapping("/loadHospitalCaseTr.ajax")
	public ModelAndView loadHospitalCaseTr(int hospitalCaseSize,String registNo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("hospitalCaseSize",hospitalCaseSize);
		mav.addObject("registNo",registNo);
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_HospitalCase_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 本次跟踪记录
	 * @param RecordFeeTypeStr
	 * @return
	 * @modified: ☆XMSH(2016年2月24日 下午3:27:22): <br>
	 */
	@RequestMapping(value = "/initRecordFeeType.ajax")
	public ModelAndView initRecordFeeType(String RecordFeeTypeStr,String registNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		List<String> FeeTypeList = new ArrayList<String>();
		if( !StringUtils.isEmpty(RecordFeeTypeStr)){
			String[] FeeArray = RecordFeeTypeStr.split(",");
			FeeTypeList = Arrays.asList(FeeArray);
		}

		ModelAndView mav = new ModelAndView();
		List<SysCodeDictVo> sysCodes = new ArrayList<SysCodeDictVo>();
		
		if(registVo.getComCode().startsWith("22")){//上海
			sysCodes = codeDictService.findCodeListByQuery("SHFeetype",FeeTypeList);
		}else{
			sysCodes = codeDictService.findCodeListByQuery("FeeType",FeeTypeList);
		}
		
		mav.addObject("sysCodes",sysCodes);
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_FeeDialog.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 新增跟踪记录费用信息
	 * 
	 * @param size
	 * @param feeTypes
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年1月28日 下午2:43:33): <br>
	 */
	@RequestMapping(value = "/loadFeeTr.ajax")
	public ModelAndView loadFeeTr(int size,String[] feeTypes,String registNo) {
		ModelAndView mv = new ModelAndView();
		List<PrpLDlossPersTraceFeeVo> persTraceFeeVos = new ArrayList<PrpLDlossPersTraceFeeVo>();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(feeTypes!=null){
			for(String feeType:feeTypes){
				PrpLDlossPersTraceFeeVo persTraceFeeVo = new PrpLDlossPersTraceFeeVo();
				persTraceFeeVo.setFeeTypeCode(feeType);
				persTraceFeeVo.setRegistNo(registNo);
				String feeTypeName = "";
				if(registVo.getComCode().startsWith("22")){
					feeTypeName = codeTranService.transCode("SHFeetype",feeType);
				}else{
					feeTypeName = codeTranService.transCode("FeeType",feeType);
				}
				persTraceFeeVo.setFeeTypeName(feeTypeName);

				persTraceFeeVos.add(persTraceFeeVo);
			}
		}

		mv.addObject("persTraceFeeVos",persTraceFeeVos);
		mv.addObject("size",size);
		mv.addObject("registNo",registNo);
		mv.addObject("comCode",registVo.getComCode());

		mv.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_TraceRecord_Tr.jspf");
		mv.setViewName("app/LoadAjaxPage");
		return mv;
	}

	/**
	 * 新增一条护理人员记录
	 * 
	 * @param nurseInfoSize
	 * @param tabPageNo
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年1月9日 上午10:22:05): <br>
	 */
	@RequestMapping("/loadNurseInfoTr.ajax")
	public ModelAndView loadNurseInfoTr(int nurseInfoSize,String registNo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("nurseInfoSize",nurseInfoSize);
		mav.addObject("registNo",registNo);
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_NurseInfo_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 新增一条被抚养人记录
	 * 
	 * @param raiseInfoSize
	 * @param tabPageNo
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年1月9日 上午10:22:24): <br>
	 */
	@RequestMapping("/loadRaiseInfoTr.ajax")
	public ModelAndView loadRaiseInfoTr(int raiseInfoSize,String registNo) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("raiseInfoSize",raiseInfoSize);
		mav.addObject("registNo",registNo);
		mav.addObject("LoadPagePath","lossperson/common/casualtyInfoEdit/TabCon_RaiseInfo_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 选择新增的费用赔款信息
	 * 
	 * @param chargeCodes
	 * @param intermFlag
	 * @return
	 * @modified: ☆XMSH(2016年1月28日 下午2:44:19): <br>
	 */
	@RequestMapping(value = "/initChargeType.ajax")
	public ModelAndView initChargeType(String chargeCodes,String intermFlag) {
		List<String> chargeCodeList = new ArrayList<String>();

		// 司内定损不能选择公估费
		if("0".equals(intermFlag)){
			if(StringUtils.isBlank(chargeCodes)){
				chargeCodes = "13";
			}else{
				chargeCodes += ",13";
			}
		}
		if(chargeCodes!=null&& !"".equals(chargeCodes)){
			String[] chargeArray = chargeCodes.split(",");
			chargeCodeList = Arrays.asList(chargeArray);
		}

		ModelAndView mav = new ModelAndView();
		List<SysCodeDictVo> sysCodes = codeDictService.findCodeListByQuery("ChargeCode",chargeCodeList);
		mav.addObject("sysCodes",sysCodes);
		mav.addObject("LoadPagePath","lossperson/persTraceEdit/PersTraceEdit_ChargeDialog.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 新增一条费用赔款信息记录
	 * 
	 * @param feePayInfoSize
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年1月9日 上午10:22:49): <br>
	 */
	@RequestMapping(value = "/loadChargeTr.ajax")
	public ModelAndView loadChargeTr(int size,String[] chargeTypes,String registNo,String intermCode) {
		ModelAndView mv = new ModelAndView();
		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
//		Map<String,String> serverMap = new HashMap<String,String>();

		if(chargeTypes!=null){
			for(String chargeCode:chargeTypes){
				PrpLDlossChargeVo lossChargeVo = new PrpLDlossChargeVo();
				lossChargeVo.setChargeCode(chargeCode);
				lossChargeVo.setRegistNo(registNo);
				lossChargeVo.setBusinessType(FlowNode.PLoss.name());
				String chargeName = codeTranService.transCode("ChargeCode",chargeCode);
				lossChargeVo.setChargeName(chargeName);
				lossChargeVos.add(lossChargeVo);
			}
		}
		
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
		for(PrpLCItemKindVo itemKind:itemKinds){
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(itemKind.getKindCode());
			dictVo.setCodeName(itemKind.getKindName());
			dictVos.add(dictVo);
		}

		mv.addObject("dictVos",dictVos);
		mv.addObject("lossChargeVos",lossChargeVos);
		mv.addObject("size",size);

		mv.addObject("LoadPagePath","lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf");
		mv.setViewName("app/LoadAjaxPage");
		return mv;
	}

	/**
	 * 刷新意见列表
	 * 
	 * @param persTraceMainId
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年3月26日 下午5:50:32): <br>
	 */
	@RequestMapping("/updateOpinionList.ajax")
	public ModelAndView updateOpinionList(Long persTraceMainId,String registNo) {
		ModelAndView mav = new ModelAndView();

		List<PrpLClaimTextVo> prpLClaimTextVos = claimTextSerVice.findClaimTextList(persTraceMainId,registNo,FlowNode.PLoss.name());// 意见列表

		mav.addObject("prpLClaimTextVos",prpLClaimTextVos);
		mav.addObject("LoadPagePath","lossperson/persTraceEdit/PersTraceEdit_Opinion_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
	
	/**
	 * 更新费用信息列表
	 * @param persTraceMainId
	 * @return
	 * @modified:
	 * ☆XMSH(2016年7月8日 下午3:05:43): <br>
	 */
	@RequestMapping("/updateFeePayList.ajax")
	public ModelAndView updateFeePayList(Long persTraceMainId,String registNo) {
		ModelAndView mav = new ModelAndView();

		List<PrpLDlossChargeVo> prpLDlossChargeVos = lossChargeService.findLossChargeVos(persTraceMainId,FlowNode.PLoss.name());// 费用赔款信息
		
		//费用险别
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
		for(PrpLCItemKindVo itemKind:itemKinds){
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(itemKind.getKindCode());
			dictVo.setCodeName(itemKind.getKindName());
			dictVos.add(dictVo);
		}
		mav.addObject("dictVos",dictVos);
		mav.addObject("lossChargeVos",prpLDlossChargeVos);
		mav.addObject("LoadPagePath","lossperson/persTraceEdit/PersTraceEdit_Charge_Tr.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 提交页面
	 * 
	 * @param currentNode
	 * @param currentName
	 * @param auditStatus
	 * @param majorcaseFlag
	 * @return
	 * @throws Exception 
	 * @modified: ☆XMSH(2016年2月24日 下午3:28:22): <br>
	 */
	@RequestMapping("/loadSubmitPersTraceNext.ajax")
	public ModelAndView loadSubmitPersTraceNext(Long traceMainId,String currentNode,String currentName,
			String auditStatus,String majorcaseFlag,String caseProcessType,String flowTaskId,String isDeroFlag,BigDecimal sumChargeFee) throws Exception {
		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		nextVo.setFlowTaskId(flowTaskId);
		nextVo.setAuditStatus(auditStatus);
		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(currentNode);
		nextVo.setComCode(WebUserUtils.getComCode());
		SysUserVo userVo = WebUserUtils.getUser();
		Date appointmentTime = null;
		String isAppointTime = "N";// 默认不显示预约时间
		Date PLFirstTime = null;
		int[] ArrDays = {15,30,60,90,180,360};
		PrpLDlossPersTraceMainVo persTraceMain = persTraceService.findPersTraceMainVoById(traceMainId);
		persTraceMain.setCaseProcessType(caseProcessType);
		persTraceMain.setMajorcaseFlag(majorcaseFlag);
		persTraceMain.setIsDeroFlag(isDeroFlag);
		persTraceMain.setSumChargeFee(sumChargeFee);

		List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(persTraceMain.getRegistNo(),persTraceMain.getId());
		if(persTraceVos!=null&&persTraceVos.size()>0){
			for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
				if(ValidFlag.VALID.equals(persTraceVo.getValidFlag())&&ValidFlag.INVALID.equals(persTraceVo.getEndFlag())){// 有效的且没有跟踪完毕显示预约时间
					isAppointTime = "Y";
				}
			}
		}

		int traceTimes = persTraceMain.getTraceTimes()==null ? 0 : persTraceMain.getTraceTimes().intValue();
		if(FlowNode.PLFirst.name().equalsIgnoreCase(currentNode)){// 首次跟踪
			PLFirstTime = new Date();
			appointmentTime = new DateTime(PLFirstTime).addDay(ArrDays[0]);
		}else if(traceTimes<6){
			PLFirstTime = persTraceMain.getPlfSubTime();
			appointmentTime = new DateTime(PLFirstTime).addDay(ArrDays[traceTimes]);
		}else{
			PLFirstTime = new Date();
			appointmentTime = new DateTime(PLFirstTime).addDay(ArrDays[4]);
		}

		/*if(AuditStatus.SUBPLNEXT.equalsIgnoreCase(auditStatus)){// 提交后续跟踪任务提交到自己
			nextVo.setNextNode(FlowNode.PLNext.name());
			nextVo.setNextName(FlowNode.PLNext.getName());
			nextVo.setAssignUser(WebUserUtils.getUserCode());
			nextVo.setAssignName(WebUserUtils.getUserName());
			nextVo.setAssignCom(WebUserUtils.getComCode());
		}else if(AuditStatus.SUBPLCHARGE.equalsIgnoreCase(auditStatus)){// 提交费用审核 轮询
			if("10".equals(caseProcessType)){//无需赔付
				nextVo.setNextNode(FlowNode.PLCharge_LV0.name());
				nextVo.setNextName(FlowNode.PLCharge_LV0.getName());
			}else{
				nextVo.setNextNode(FlowNode.PLCharge.name());
				nextVo.setNextName(FlowNode.PLCharge.getName());
				nextVo.setAssignName("人伤费用审核岗");
			}
		}else if(AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus)){// 提交跟踪审核
			nextVo.setNextNode(FlowNode.PLVerify.name());
			nextVo.setNextName(FlowNode.PLVerify.getName());
			nextVo.setAssignName("人伤跟踪审核岗");
		}*/
		
		//判断是否总公司有做过审核操作
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("registNo",persTraceMain.getRegistNo());
		if(flowTaskId == null){
			params.put("taskId",BigDecimal.ZERO.doubleValue());
		} else{
			params.put("taskId",Double.valueOf(flowTaskId));
		}
		String existHeadOffice = managerService.isSubmitHeadOffice(params);
		
		nextVo = persTraceHandleService.organizNextVo(persTraceMain, nextVo, userVo, caseProcessType,existHeadOffice);

		// 设置默认下一个处理人
		nextVo.setAssignCom(WebUserUtils.getComCode());

		boolean existPLBigTask = wfTaskHandleService.existTaskByNodeCode(persTraceMain.getRegistNo(),FlowNode.PLBig,
				persTraceMain.getId().toString(),"");
		if("1".equals(majorcaseFlag)&&!existPLBigTask){//发起大案审核
			nextVo.setOtherNodes(FlowNode.PLBig_LV1.name());
			nextVo.setOtherNodesName(FlowNode.PLBig_LV1.getName());
			nextVo.setOtherAssignUser("");
		}

		mav.addObject("appointmentTime",appointmentTime);
		mav.addObject("isAppointTime",isAppointTime);
		mav.addObject("nextVo",nextVo);
		mav.addObject("majorcaseFlag",majorcaseFlag);
		mav.addObject("LoadPagePath","lossperson/persTraceEdit/casualtyInfo/SubmitPersTraceNext.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	@RequestMapping("/loadSubmitPLVerify.ajax")
	public ModelAndView loadSubmitPLVerify(Double traceMainId, Double taskId, String registNo, String currentNode, String currentName, String auditStatus) throws Exception {
		ModelAndView mav = new ModelAndView();
		PrpLDlossPersTraceMainVo traceMainVo = persTraceService.findPersTraceMainVoById(traceMainId.longValue());
		int maxLevel = traceMainVo.getMaxLevel();//分公司最高级
		int verifyLevel = traceMainVo.getVerifyLevel();//审核级别
		SubmitNextVo nextVo = new SubmitNextVo();
		List<SysCodeDictVo> dataSourceList = new ArrayList<SysCodeDictVo>();

		// 本方法中多处需要用到，故单独放在所有调用之前
		String[] nodeName = currentNode.split("_");
		// str.substring(2) 从指定参数位置开始截取，直至字符串结束
		String level = nodeName[1].substring(2);

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(taskId);// 人伤任务
		if (AuditStatus.SUBMITVERIFY.equalsIgnoreCase(auditStatus) || AuditStatus.SUBMITCHARGE.equalsIgnoreCase(auditStatus)) {
			//判断是否总公司有做过审核操作
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("registNo", registNo);
			if (taskId == null) {
				params.put("taskId", BigDecimal.ZERO.doubleValue());
			} else {
				params.put("taskId", Double.valueOf(taskId));
			}
			String existHeadOffice = managerService.isSubmitHeadOffice(params);
			if (Integer.parseInt(level) < CodeConstants.TopOfficeVerifyLevel.LEVEL9 && CodeConstants.CommonConst.TRUE.equals(existHeadOffice)) {
				throw new IllegalArgumentException("该任务存在总公司审核操作历史，需提交上级！");
			}
		}
		if (AuditStatus.SUBMITVERIFY.equalsIgnoreCase(auditStatus)) {// 跟踪审核通过
			if (Integer.parseInt(level) < verifyLevel) {
				throw new Exception("审核权限不够，请提交上级！");
			}
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(FlowNode.PLNext.name());
			dictVo.setCodeName(FlowNode.PLNext.getName());
			dataSourceList.add(dictVo);
			//指定处理人为原处理人
			nextVo.setAssignUser(taskVo.getAssignUser());
			String userName = codeTranService.transCode("UserCode", taskVo.getAssignUser());
			nextVo.setAssignName(userName);
			nextVo.setAssignCom(taskVo.getAssignCom());

		} else if (AuditStatus.SUBMITCHARGE.equalsIgnoreCase(auditStatus)) {// 费用审核通过
			if (Integer.parseInt(level) < verifyLevel) {
				throw new Exception("审核权限不够，请提交上级！");
			}
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(FlowNode.END.name());
			dictVo.setCodeName(FlowNode.END.getName());
			dataSourceList.add(dictVo);
			//指定处理人为原处理人
			nextVo.setAssignUser(taskVo.getAssignUser());
			String userName = codeTranService.transCode("UserCode", taskVo.getAssignUser());
			nextVo.setAssignName(userName);
			nextVo.setAssignCom(taskVo.getAssignCom());
			// nextNodeMap.put(FlowNode.END.name(),FlowNode.END.getName());
		} else if (AuditStatus.BACKPLNEXT.equalsIgnoreCase(auditStatus)) {// 退回人伤后续跟踪
			if (Integer.parseInt(level) > maxLevel) {
				throw new Exception("总公司级别不能直接退回人伤后续跟踪，请退回下级！");
			}
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(FlowNode.PLNext.name());
			dictVo.setCodeName(FlowNode.PLNext.getName());
			dataSourceList.add(dictVo);
			//指定处理人为原处理人
			nextVo.setAssignUser(taskVo.getAssignUser());
			String userName = codeTranService.transCode("UserCode", taskVo.getAssignUser());
			nextVo.setAssignName(userName);
			nextVo.setAssignCom(taskVo.getAssignCom());
		} else if (AuditStatus.AUDIT.equalsIgnoreCase(auditStatus)) {// 提交上级
			int currentLevel = Integer.parseInt(level);
			if (currentLevel >= 1 && currentLevel < maxLevel) {//在分公司级别
				for (int i = maxLevel; currentLevel < i; currentLevel++) {
					FlowNode nextNode = FlowNode.valueOf(nodeName[0] + "_LV" + (currentLevel + 1));
					SysCodeDictVo dictVo = new SysCodeDictVo();
					dictVo.setCodeCode(nextNode.name());
					dictVo.setCodeName(nextNode.getName());
					dataSourceList.add(dictVo);
				}
			} else if (currentLevel >= CodeConstants.TopOfficeVerifyLevel.LEVEL9 && currentLevel < CodeConstants.TopOfficeVerifyLevel.LEVEL12) {// 9-11级只能逐级提交
				int nextLevel = currentLevel + 1;
				FlowNode nextNode = FlowNode.valueOf(nodeName[0] + "_LV" + nextLevel);
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(nextNode.name());
				dictVo.setCodeName(nextNode.getName());
				dataSourceList.add(dictVo);
			} else if (currentLevel == maxLevel) {//在分公司最高级别的时候只能提交到总公司最低级（9级）
				FlowNode nextNode = FlowNode.valueOf(nodeName[0] + "_LV9");
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(nextNode.name());
				dictVo.setCodeName(nextNode.getName());
				dataSourceList.add(dictVo);
			} else {
				throw new Exception("已经处于最高级别，不能提交上级！");
			}
		} else if (AuditStatus.BACKLOWER.equalsIgnoreCase(auditStatus)) {// 退回下级
			if ("LV9".equals(nodeName[1])) {
				SysCodeDictVo dictVo = new SysCodeDictVo();
				FlowNode node = FlowNode.valueOf(nodeName[0] + "_LV" + maxLevel);
				dictVo.setCodeCode(node.name());
				dictVo.setCodeName(node.getName());
				dataSourceList.add(dictVo);
			} else {
				List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskId, currentNode, nodeName[0]);
				for (PrpDNodeVo nodeVo : nodeList) {
					SysCodeDictVo dictVo = new SysCodeDictVo();
					dictVo.setCodeCode(nodeVo.getNodeCode());
					dictVo.setCodeName(nodeVo.getNodeName());
					dataSourceList.add(dictVo);
				}
			}

			if (dataSourceList == null || dataSourceList.size() == 0) {
				throw new Exception("已经处于最低级别，不能退回下级！");
			}
		}

		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(currentNode);
		nextVo.setAuditStatus(auditStatus);
		nextVo.setComCode(WebUserUtils.getComCode());

		mav.addObject("nextVo", nextVo);
		mav.addObject("dataSourceList", dataSourceList);
		mav.addObject("LoadPagePath", "lossperson/persTraceVerify/casualtyInfo/SubmitPLVerify.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}

	/**
	 * 费用审核修改
	 * 
	 * @return
	 * @throws Exception 
	 * @modified: ☆XMSH(2016年2月25日 上午10:53:37): <br>
	 */
	@RequestMapping("/loadSubmitChargeAdjust.ajax")
	public ModelAndView loadSubmitChargeAdjust(String persTraceMainId) throws Exception {
		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		nextVo.setNextNode(FlowNode.PLNext.name());
		nextVo.setNextName(FlowNode.PLNext.getName());

		nextVo.setCurrentName(FlowNode.PLChargeMod.getName());
		nextVo.setCurrentNode(FlowNode.PLChargeMod.name());
		// nextVo.setFlowId(flowId);
		// 设置默认下一个处理人
		PrpLDlossPersTraceMainVo mainVo = 
				persTraceService.findPersTraceMainVoById(Long.parseLong(persTraceMainId));
		PrpLWfTaskVo oldTaskVo = findTaskIn(mainVo.getRegistNo(),persTraceMainId,FlowNode.PLNext);
		nextVo.setAssignUser(oldTaskVo.getHandlerUser());
		nextVo.setAssignCom(oldTaskVo.getHandlerCom());
		nextVo.setComCode(policyViewService.getPolicyComCode(mainVo.getRegistNo()));

		mav.addObject("nextVo",nextVo);
		mav.addObject("LoadPagePath","lossperson/persTraceChargeAdjust/SubmitChargeMod.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
	
	private PrpLWfTaskVo findTaskIn(String registNo,String traceMainId,FlowNode nextNode) throws Exception{
		PrpLWfTaskVo oldTaskVo = null;
		List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo,traceMainId,nextNode);
		if(endTaskVoList == null || endTaskVoList.size() == 0){
			endTaskVoList = wfTaskHandleService.findEndTask(registNo,traceMainId,FlowNode.PLFirst);
		}
		if(endTaskVoList != null && endTaskVoList.size() > 0){
			oldTaskVo = endTaskVoList.get(0);
		}else{
			throw new Exception("未查找到人伤后续跟踪或人伤首次跟踪节点的任务！");
		}
		return oldTaskVo;
	}

	@RequestMapping("/loadSubmitPLBig.ajax")
	public ModelAndView loadSubmitPLBig(String currentNode,String currentName,String auditStatus,
	                                    float allSumReportFee,float allSumdefLoss,String registNo) {
		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		
		allSumReportFee = 0;// 所有人员合计总估损金额
		allSumdefLoss = 0;// 所有人员合计总定损金额
		//直接通过报案号查询出所有的人伤信息
		List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(registNo);
		if(persTraceVoList != null && !persTraceVoList.isEmpty()){
			for(PrpLDlossPersTraceVo persTraceVo : persTraceVoList){
				allSumReportFee += DataUtils.NullToZero(persTraceVo.getSumReportFee()).doubleValue();
				allSumdefLoss += DataUtils.NullToZero(persTraceVo.getSumdefLoss()).doubleValue();
			}
		}
		// 人伤大案审核 提交规则修改（分公司一级和总公司一个-->对应人伤大案审核一级和二级）
		if(FlowNode.PLBig_LV1.name().equals(currentNode)){
			if(Float.isNaN(allSumReportFee) || Float.valueOf(allSumReportFee) == null){
				allSumReportFee = 0;
			}
			if(Float.isNaN(allSumdefLoss) || Float.valueOf(allSumdefLoss) == null){
				allSumdefLoss = 0;
			}
			if(allSumReportFee<200000&&allSumdefLoss<200000){// 大案审核一级权限
				nextVo.setNextNode(FlowNode.END.name());
				nextVo.setNextName(FlowNode.END.getName());
			}else{
				nextVo.setNextNode(FlowNode.PLBig_LV2.name());
				nextVo.setNextName(FlowNode.PLBig_LV2.getName());
			}
		}else{
			nextVo.setNextNode(FlowNode.END.name());
			nextVo.setNextName(FlowNode.END.getName());
		}

		// 关于权限金额的提交规则，暂时待确认。估损金额或定损金额大于十万的提交到二级，二级审核通过
//		if(FlowNode.PLBig_LV1.name().equals(currentNode)){
//			if(allSumReportFee<100000&&allSumdefLoss<100000){// 大案审核一级权限
//				nextVo.setNextNode(FlowNode.END.name());
//				nextVo.setNextName(FlowNode.END.getName());
//			}else{
//				nextVo.setNextNode(FlowNode.PLBig_LV2.name());
//				nextVo.setNextName(FlowNode.PLBig_LV2.getName());
//			}
//		}else if(FlowNode.PLBig_LV2.name().equals(currentNode)){
//			if(allSumReportFee<200000&&allSumdefLoss<200000){// 大案审核二级权限
//				nextVo.setNextNode(FlowNode.END.name());
//				nextVo.setNextName(FlowNode.END.getName());
//			}else{
//				nextVo.setNextNode(FlowNode.PLBig_LV3.name());
//				nextVo.setNextName(FlowNode.PLBig_LV3.getName());
//			}
//		}else if(FlowNode.PLBig_LV3.name().equals(currentNode)){
//			if(allSumReportFee<300000&&allSumdefLoss<300000){// 大案审核三级权限
//				nextVo.setNextNode(FlowNode.END.name());
//				nextVo.setNextName(FlowNode.END.getName());
//			}else{
//				nextVo.setNextNode(FlowNode.PLBig_LV4.name());
//				nextVo.setNextName(FlowNode.PLBig_LV4.getName());
//			}
//		}else if(FlowNode.PLBig_LV4.name().equals(currentNode)){
//			if(allSumReportFee<400000&&allSumdefLoss<400000){// 大案审核四级权限
//				nextVo.setNextNode(FlowNode.END.name());
//				nextVo.setNextName(FlowNode.END.getName());
//			}else{
//				nextVo.setNextNode(FlowNode.PLBig_LV5.name());
//				nextVo.setNextName(FlowNode.PLBig_LV5.getName());
//			}
//		}else{
//			nextVo.setNextNode(FlowNode.END.name());
//			nextVo.setNextName(FlowNode.END.getName());
//		}

		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(currentNode);
		// nextVo.setFlowId(flowId);
		// 设置默认下一个处理人
		String comCode = WebUserUtils.getComCode();
		if(FlowNode.PLBig_LV1.name().equals(currentNode)){
			comCode = CodeConstants.TOPCOM;
		}
		nextVo.setAssignUser(null);
		nextVo.setAssignCom(comCode);
		nextVo.setComCode(comCode);
		nextVo.setAuditStatus(auditStatus);

		mav.addObject("nextVo",nextVo);
		mav.addObject("LoadPagePath","lossperson/persTracePLBig/SubmitPLBig.jspf");
		mav.setViewName("app/LoadAjaxPage");
		return mav;
	}
	
	@RequestMapping("/loadChkPerson.ajax")
	@ResponseBody
	public String loadChkPerson(String chkPersonId) {
		String jsonArray = "";
		if(StringUtils.isNotEmpty(chkPersonId)){
			Long id = Long.valueOf(chkPersonId);
			PrpLCheckPersonVo chkPersonVo = checkTaskService.findCheckPersonVpById(id);
			jsonArray = JSONArray.toJSONString(chkPersonVo);
		}
		return jsonArray;
	}

}
