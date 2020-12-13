/******************************************************************************
 * CREATETIME : 2016年3月4日 下午6:29:41
 ******************************************************************************/
package ins.sino.claimcar.padpay.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.padpay.po.PrpLPadPayPerson;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/** 垫付 服务
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("padPayService")
public class PadPayServiceImpl implements PadPayService {
	private Logger logger = LoggerFactory.getLogger(PadPayServiceImpl.class);

	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	ClaimService claimService;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	CheckTaskService checkTaskService;
	
	@Autowired
	PersTraceDubboService persTraceDubboService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	CompensateService compensateService;
	
	@Autowired
	LossCarService lossCarService;
	
	@Autowired
	CompensateTaskService  compensateTaskService;
	
    @Autowired
    WfFlowQueryService wfFlowQueryService;
    @Autowired
	private ConfigService configService;
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#initCMainInfo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLCMainVo initCMainInfo(String registNo){
		PrpLCMainVo cMainVo = null;
		List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo cMainVoTemp : cMainVos){
			if("1101".equals(cMainVoTemp.getRiskCode())){
				cMainVo = cMainVoTemp;
			}
		}
		return cMainVo;
	}
	
	/**
	 * 根据报案号 和计算书查询
	 * @param registNo
	 * @param compensateNo
	 * @return
	 */
	public PrpLPadPayMainVo findPadPayMainByComp(String registNo,String compensateNo){
		PrpLPadPayMainVo padPayMainVo = null;
		
		QueryRule pfqr = QueryRule.getInstance();
		pfqr.addEqual("registNo",registNo);
		pfqr.addEqual("compensateNo",compensateNo);
		List<PrpLPadPayMain> padPayMains = databaseDao.findAll(PrpLPadPayMain.class,pfqr);
		if(padPayMains!=null && padPayMains.size()>0){
			PrpLPadPayMain padPayMain = padPayMains.get(0);
			padPayMainVo = Beans.copyDepth().from(padPayMain).to(PrpLPadPayMainVo.class);
		}
		
		return padPayMainVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#initPadPay(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param claimNo
	 * @return
	 */
	@Override
	public PrpLPadPayMainVo initPadPay(String registNo,String claimNo,PrpLWfTaskVo wfTaskVo){
		logger.info("报案号registNo={}，立案号={},进入初始化垫付任务申请登记方法",registNo,claimNo);
		PrpLPadPayMainVo padPayMainVo = null;
		List<PrpLPadPayMain> padPayMainPo = null;
		// wfTaskVo = null 时还未生成工作流任务，为任务发起时首次初始化
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(StringUtils.isNotBlank(claimNo)){
			queryRule.addEqual("claimNo",claimNo);
		}
		if(wfTaskVo != null&&!StringUtils.isBlank(wfTaskVo.getCompensateNo())){
			queryRule.addEqual("compensateNo",wfTaskVo.getCompensateNo());
		}
		if(wfTaskVo==null){
			//queryRule.addNotEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);
			queryRule.addSql(" (underwriteFlag <> '7' or underwriteFlag is null) ");
		}
		padPayMainPo = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		if(padPayMainPo!=null&&padPayMainPo.size()>0){
			padPayMainVo = Beans.copyDepth().from(padPayMainPo.get(0)).to(PrpLPadPayMainVo.class);
			// 垫付已经暂存，但人伤又新的数据
		}else{
			logger.info("报案号registNo={}，立案号={},初始化垫付主表数据",registNo,claimNo);
			padPayMainVo = new PrpLPadPayMainVo();
			List<PrpLPadPayPersonVo> padPayPersonVos = new ArrayList<PrpLPadPayPersonVo>();
			PrpLPadPayPersonVo padPayPersonVo = new PrpLPadPayPersonVo();
			padPayPersonVos.add(padPayPersonVo);
			padPayMainVo.setPrpLPadPayPersons(padPayPersonVos);
		}
		logger.info("报案号registNo={}，立案号={},结束初始化垫付任务申请登记方法",registNo,claimNo);
		return padPayMainVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getPersonNameMap(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param claimNo
	 * @return
	 */
	@Override
	public Map<String,String> getPersonNameMap(String registNo,String claimNo) {
		Map<String,String> map = new HashMap<String,String>();
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = 
		persTraceDubboService.findPersTraceMainVoList(registNo);
		PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
		if(persTraceMainVos!=null&&persTraceMainVos.size()>0){
			for(PrpLDlossPersTraceMainVo persTraceMainVo:persTraceMainVos){
			    if(!"7".equals(persTraceMainVo.getUnderwriteFlag())){
			        List<PrpLDlossPersTraceVo> persTraceVos = persTraceMainVo.getPrpLDlossPersTraces();
	                if(persTraceVos!=null&&persTraceVos.size()>0){
	                    for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
	                        if(CodeConstants.ValidFlag.VALID.equals(persTraceVo.getValidFlag())){
	                            String key = persTraceVo.getId().toString();
	                            String value = persTraceVo.getPrpLDlossPersInjured().getPersonName();
	                            //在垫付发起页面，非互赔自赔案，可以垫付给标的车人员这是错误的。
	                            if(StringUtils.isNotBlank(claimVo.getCaseFlag())&&
	                                    "0".equals(claimVo.getCaseFlag())){//非互赔自赔案
	                                if(persTraceVo.getPrpLDlossPersInjured().getSerialNo()!=1){
	                                    map.put(key,value); 
	                                }
	                            }else{
	                                map.put(key,value);
	                            }
	                        }
	                    }
	                }
			    }
			}
		}
		map.put("","");
		return map;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getRegistVo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLRegistVo getRegistVo(String registNo){
		return registQueryService.findByRegistNo(registNo);
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getClaimNo(java.lang.String)
	 * @param claimNo
	 * @return
	 */
	@Override
	public PrpLClaimVo getClaimNo(String claimNo){
		return claimService.findByClaimNo(claimNo);
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#queryCheckDuty(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLCheckDutyVo queryCheckDuty(String registNo){
		return checkTaskService.findCheckDuty(registNo,1);
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getColorCode(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public String getColorCode(String registNo){
		String colorCode = "";
		PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(registNo,1);
		if(checkCarVo!=null){
			PrpLCheckCarInfoVo carInfoVo= checkCarVo.getPrpLCheckCarInfo();
			colorCode = carInfoVo.getLicenseColor();
		}
		return colorCode;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getLicenseNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public Map<String,String> getLicenseNo(String registNo) {
		Map<String,String> map = new HashMap<String,String>();
		List<PrpLCheckCarVo> checkCarVos = checkTaskService.findCheckCarVo(registNo);
		List<PrpLDlossCarMainVo> carMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(checkCarVos != null && checkCarVos.size() > 0){
			for(PrpLCheckCarVo checkCarVo:checkCarVos){
				if("1".equals(checkCarVo.getValidFlag()) && checkCarVo.getSerialNo() != 1){
					PrpLCheckCarInfoVo checkCarInfo = checkCarVo.getPrpLCheckCarInfo();
					if(checkCarInfo != null){
						map.put(checkCarInfo.getLicenseNo(),checkCarInfo.getLicenseNo());
					}
				}
			}
		}
		if(carMainVoList!=null && carMainVoList.size()>0){
			for(PrpLDlossCarMainVo vo:carMainVoList){
				if("1".equals(vo.getValidFlag()) && !map.containsKey(vo.getLicenseNo()) && !"1".equals(vo.getDeflossCarType())){
					map.put(vo.getLicenseNo(), vo.getLicenseNo());
				}
			}
		}
		map.put("0","地面/路人");
		return map;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getCustom(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public Map<String,String> getCustom(String registNo){
		Map<String,String> map = new HashMap<String,String>();
		List<PrpLPayCustomVo> payCustomVos = managerService.findPayCustomVoByRegistNo(registNo);
		if(payCustomVos != null && payCustomVos.size() > 0){
			for(PrpLPayCustomVo payCustomVo : payCustomVos){
				String bankNo = payCustomVo.getBankNo();
				String idNo = bankNo.substring(bankNo.length()-4,bankNo.length());
				
				String value = payCustomVo.getPayeeName() + idNo;
				map.put(payCustomVo.getId().toString(),value);
			}
		}
		map.put("0","");
		return map;
	}
	
	// 获取收款放账号和开户银行
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getPayCustom(java.lang.Long)
	 * @param payCusId
	 * @return
	 */
	@Override
	public Map<String,String> getPayCustom(Long payCusId){
		Map<String,String> map = new HashMap<String,String>();
		PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payCusId);
		if(payCustomVo != null){
			map.put("accNo",payCustomVo.getAccountNo());
			map.put("bank",payCustomVo.getBankName());
		}
		return map;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#getPersonName(java.lang.Long)
	 * @param id
	 * @return
	 */
	@Override
	public Map<String,String> getPersonName(Long id){
		Map<String,String> map = new HashMap<String,String>();
		PrpLDlossPersInjuredVo persInjuredVo = persTraceDubboService.findPersInjuredByPK(id);
		PrpLDlossPersTraceVo persTraceVo = persTraceDubboService.findPersTraceByPK(id);
		if(persInjuredVo!=null){
			map.put("age",persInjuredVo.getPersonAge().toString());
			map.put("sex",persInjuredVo.getPersonSex());
			map.put("licenseNo",persInjuredVo.getLicenseNo());
			String idNo = "1".equals(persInjuredVo.getCertiType())
					?persInjuredVo.getCertiCode():"";
			map.put("idNo",idNo);
		}
		if(persTraceVo!=null){
			map.put("fee",DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).toString());
		}
		return map;
	}
	
	/**
	 * 是否已经存在垫付主表
	 * @param registNo
	 * @param claimNo
	 * @return
	 * @modified:
	 */
	private boolean isExistPadPayMain(String registNo,String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addSql(" (underwriteFlag <> '7' or underwriteFlag is null) ");
		List<PrpLPadPayMain> poList = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		return !poList.isEmpty() && poList.size()>0 ? true : false;
	}
	
	// 自动支付
	private void aotoPayment(PrpLPadPayMainVo padPayMainVo,PrpLPadPayMain padPayMain) {
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(padPayMainVo.getRegistNo());
		List<PrpLPaymentVo> paymentVo = new ArrayList<PrpLPaymentVo>();
		if(padPayMainVo.getPrpLPadPayPersons().size()>0){
			for(PrpLPadPayPersonVo vo:padPayMainVo.getPrpLPadPayPersons()){
				PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
				prpLPaymentVo.setSumRealPay(vo.getCostSum());
				prpLPaymentVo.setPayeeId(vo.getPayeeId());
				paymentVo.add(prpLPaymentVo);
			}
		}
		//自动支付
		String isFast = RadioValue.RADIO_NO;
		if(paymentVo != null && paymentVo.size() > 0){
			List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo, paymentVo);
			isFast = (msgParamVoList != null && msgParamVoList.size() > 0) ? RadioValue.RADIO_YES : RadioValue.RADIO_NO;
		}
		padPayMain.setIsFastReparation(isFast);
		
		
		BigDecimal sumAmt =new BigDecimal(0);
		for(PrpLPaymentVo vo : paymentVo){
			sumAmt=sumAmt.add(vo.getSumRealPay());
		}
		padPayMain.setIsAutoPay(sumAmt.compareTo(BigDecimal.ZERO)==1 ? RadioValue.RADIO_YES  : RadioValue.RADIO_NO);//大于0 则自动支付
	}
	
	@Override
	public PrpLPadPayMainVo save(PrpLPadPayMainVo padPayMainVo,String userCode,String comCode){
		logger.info("报案号={},进入垫付保存方法",(padPayMainVo == null? null :padPayMainVo.getRegistNo()));
		PrpLPadPayMain padPayMain = null;
		// id是空的直接copy对象
		if(padPayMainVo.getId()==null){
			//padPayMain = new PrpLPadPayMain();
			if(isExistPadPayMain(padPayMainVo.getRegistNo(),padPayMainVo.getClaimNo())){
				return null;
			}
			logger.info("报案号={},新增一条垫付记录",(padPayMainVo == null? null :padPayMainVo.getRegistNo()));
			padPayMain = Beans.copyDepth().from(padPayMainVo).to(PrpLPadPayMain.class);
			BillNoService billNoService = new BillNoService();
			String compeNo = billNoService.getPadPayNo
					(policyViewService.getPolicyComCode(padPayMainVo.getRegistNo()),Risk.DQZ);
			padPayMain.setCompensateNo(compeNo);
			padPayMain.setValidFlag(ValidFlag.VALID);
			padPayMain.setCreateUser(userCode);
			padPayMain.setCreateTime(new Date());
			padPayMain.setUpdateUser(userCode);
			padPayMain.setUpdateTime(new Date());
			padPayMain.setComCode(comCode);
			
			aotoPayment(padPayMainVo,padPayMain);
			
			// 维护主子表关系
			List<PrpLPadPayPerson> padPayPersonPos = new ArrayList<PrpLPadPayPerson>();
			int i = 0;
			for(PrpLPadPayPersonVo padPayPersonVo : padPayMainVo.getPrpLPadPayPersons()){
				PrpLPadPayPerson padPayPerson = new PrpLPadPayPerson();
				padPayPerson = Beans.copyDepth().from(padPayPersonVo).to(PrpLPadPayPerson.class);
				padPayPerson.setSerialNo(i+"");i++;
				padPayPerson.setPrpLPadPayMain(padPayMain);
				
				padPayPersonPos.add(padPayPerson);
			}
			padPayMain.setPrpLPadPayPersons(padPayPersonPos);
		}else{
			padPayMain = databaseDao.findByPK(PrpLPadPayMain.class, padPayMainVo.getId());
			Beans.copy().from(padPayMainVo).excludeNull().to(padPayMain);
			
			aotoPayment(padPayMainVo,padPayMain);//自动支付
			
			List<PrpLPadPayPerson> padPayPersons = padPayMain.getPrpLPadPayPersons();
			
			this.mergeList(padPayMain,padPayMainVo.getPrpLPadPayPersons(),padPayPersons,"id",PrpLPadPayPerson.class,"setPrpLPadPayMain");
			int i = 0;
			for(PrpLPadPayPerson tempPay : padPayMain.getPrpLPadPayPersons()){
				tempPay.setSerialNo(i+"");i++;
				if(tempPay.getId() == null){
					tempPay.setFlag(ValidFlag.VALID);
				}
			}
		}
		
		// 添加管控，如果银行账号相同的数据在系统中已存在，且户名不同，提示账户已存在户名不同，且不允许保存 硬管控
		List<PrpLPadPayPerson> padPayPersonList = padPayMain.getPrpLPadPayPersons();
		if(padPayPersonList != null && !padPayPersonList.isEmpty()){
			validPayCustomInfo(padPayPersonList);
		}
		
		padPayMain.setFlag("save");
		databaseDao.save(PrpLPadPayMain.class,padPayMain);
		
		padPayMainVo.setId(padPayMain.getId());
		padPayMainVo = Beans.copyDepth().from(padPayMain).to(PrpLPadPayMainVo.class);
		return padPayMainVo;
	}
	
	
	private void validPayCustomInfo(List<PrpLPadPayPerson> padPayPersonList) {
		for(PrpLPadPayPerson payment:padPayPersonList){
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payment.getPayeeId());
			PrpLPayCustomVo existPayCus = managerService.adjustExistSamePayCusDifName(payCustomVo);
			if(existPayCus != null && StringUtils.isNotBlank(existPayCus.getPayeeName())){
				throw new IllegalArgumentException(
						"保存失败！该账号已存于案件"+existPayCus.getRegistNo()+"，且户名为"+existPayCus.getPayeeName()+"！");
			}
		}
	}
	
	@Override
	public String LaunchPadPayTask(String registNo,String claimNo,String comCode,String userCode) {

		String validResult = this.padPayTaskVlaid(registNo);// 再次校验是否可以发起垫付任务
		if( !"ok".equals(validResult)){
			throw new IllegalArgumentException("发起垫付任务失败，错误信息："+validResult);
		}

		PrpLPadPayMainVo padPayMainVo = this.getPadPayInfo(registNo,claimNo);
		String padPayNo = padPayMainVo != null ? padPayMainVo.getCompensateNo() : "";

//		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PadPay,"","1")){
//			validResult = "该案件已存在已处理的垫付任务！";
//			throw new IllegalArgumentException("发起垫付任务失败，错误信息："+validResult);
//		}

		PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setCurrentNode(FlowNode.ClaimCI);
		submitVo.setNextNode(FlowNode.PadPay);
		// 查询立案已处理
		List<PrpLWfTaskVo> wfTaskVos = null;
		wfTaskVos = wfTaskHandleService.findEndTask(registNo,claimNo,FlowNode.ClaimCI);
		if (wfTaskVos != null && wfTaskVos.size() > 0) {
			submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
			submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
		} else {
			validResult = "该案件不存在已处理的交强立案节点！";
			throw new IllegalArgumentException("发起垫付任务失败，错误信息：" + validResult);
		}
		submitVo.setComCode(policyViewService.getPolicyComCode(claimVo.getRegistNo()));
		submitVo.setTaskInUser(userCode);
		submitVo.setTaskInKey(claimNo);
		submitVo.setHandleIdKey(padPayNo);
		// 指定处理人
		submitVo.setAssignCom(policyViewService.getPolicyComCode(registNo));  //承保机构 / 保单机构
		submitVo.setAssignUser(null);  // 牛强改  垫付任务处理到池子
		
		//提交
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addPrePayTask(claimVo, submitVo);
		Double taskId = wfTaskVo.getTaskId().doubleValue();
		String handlerIdKey = wfTaskVo.getHandlerIdKey();
//		String handlerUser = wfTaskVo.getHandlerUser();
//		String handlerCom = wfTaskVo.getHandlerCom();
		
		return validResult;
	}
	
	@Override
	public void submitPadPay(String taskId,Long padId,Integer level,
	    String comCode,String userCode,String nextUserCode,String nextComCode) throws Exception{
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(taskId));
		PrpLPadPayMain padPayPo = databaseDao.findByPK(PrpLPadPayMain.class,padId);
		PrpLPadPayMainVo padPayVo = Beans.copyDepth().from(padPayPo).to(PrpLPadPayMainVo.class);
		String registNo = padPayVo.getRegistNo();

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setFlowTaskId(new BigDecimal(taskId));
		submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
		submitVo.setTaskInKey(padPayVo.getClaimNo());// 进入的关键业务号
		submitVo.setTaskInUser(userCode);

		submitVo.setCurrentNode(FlowNode.PadPay);
		submitVo.setNextNode(FlowNode.VClaim_CI_LV1);
	
		PrpLWfTaskVo oldTaskVo = findTaskIn(registNo, padPayVo.getCompensateNo(),FlowNode.VClaim_CI_LV1);
		if(oldTaskVo != null && FlowNode.PadPay.name().equals(oldTaskVo.getYwTaskType())){
			submitVo.setAssignCom(oldTaskVo.getHandlerCom());
			submitVo.setAssignUser(oldTaskVo.getHandlerUser());
		}else{
			submitVo.setAssignCom(nextComCode);
			submitVo.setAssignUser(nextUserCode);
		}

		wfTaskHandleService.submitPadpay(padPayVo,submitVo);
		
		//更新主表
		updatePadPay(padPayPo.getRegistNo(),"submit");
	}
	
	@Override
	public PrpLWfTaskVo findTaskIn(String registNo,String handleIdKey,FlowNode nextNode) throws Exception{
		PrpLWfTaskVo oldTaskVo = null;
		List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo,handleIdKey,nextNode);
		if(endTaskVoList == null || endTaskVoList.size() == 0){
			endTaskVoList = wfTaskHandleService.findEndTask(registNo,handleIdKey,FlowNode.VClaim_CI_LV1);
		}
		if(endTaskVoList != null && endTaskVoList.size() > 0){
			oldTaskVo = endTaskVoList.get(0);
		}
		return oldTaskVo;
	}
	
	/**
	 * <pre>更新主表信息</pre>
	 * @param registNo,submitType
	 * @modified:
	 * ☆Luwei(2016年10月18日 下午2:37:24): <br>
	 */
	private void updatePadPay(String registNo,String submitType){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLPadPayMain> padPayMainList = databaseDao.findAll(PrpLPadPayMain.class, queryRule);
		if (padPayMainList != null && padPayMainList.size() > 0) {
			for(PrpLPadPayMain padPayMain : padPayMainList){
				padPayMain.setFlag(submitType);
				padPayMain.setUpdateTime(new Date());
				databaseDao.update(PrpLPadPayMain.class,padPayMain);
			}
		}
	}
	
	@Override
	public PrpLPadPayMainVo getPadPayInfo(String registNo, String claimNo) {
		PrpLPadPayMainVo padPayMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		// queryRule.addEqual("claimNo",claimNo);
		queryRule.addSql(" (underwriteFlag <> '7' or underwriteFlag is null) ");
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class, queryRule);
		if (padPayMain != null && padPayMain.size() > 0) {
			padPayMainVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return padPayMainVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.padpay.
	 * service.PadPayService#dropPerByPK
	 * (java.lang.Long)
	 * @param id
	 */
	@Override
	public void dropPerByPK(Long id){
		databaseDao.deleteByPK(PrpLPadPayPerson.class,id);
	}
	
	@Override
	public List<PrpLPadPayMainVo> findPadPayMainByRegistNo(String registNo) {
		return compensateService.findPadPayMainByRegistNo(registNo);
	}
	
	@Override
	public String padPayTaskVlaid(String registNo) {
		String retData = "ok";
		
		//没有交强险保单的报案不生成待申请任务
		boolean kind = false;
		PrpLClaimVo claimVo = null;
		List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
		if(claimVos != null && claimVos.size() > 0){
			for(PrpLClaimVo claim : claimVos){
				if(Risk.DQZ.equals(claim.getRiskCode())){
					claimVo = claim;kind = true;
				}
			}
		}
		
		// 没有交强险保单的报案不生成待申请任务
		if(!kind){
			return "报案信息不存在交强险保单的报案或交强险立案，不能发起垫付流程！";
		}
		
		if(claimVo !=null && claimVo.getEndCaseTime() != null){
			return "已结案的案件不能发起垫付申请！";
		}
		
		//该案件为代位求偿案件，不能发起垫付任务！
		if(RadioValue.RADIO_YES.equals(claimVo.getIsSubRogation())){
			return "该案件为代位求偿案件，不能发起垫付任务！";
		}
		
		//查勘任务未提交，不能发起垫付任务。
		if(!wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.Chk,"","1")){
			return "查勘任务未提交，不能发起垫付任务！";
		}
		
		// 交强险未处理
		if(!wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.ClaimCI,"","1")){
			return "该案件交强险立案未处理，请核对！";
		}
		
		//0表示不存在没有被注销的交强预付，1表示存在没有被注销的交强预付
		String flag="0";
		List<PrpLCompensateVo> compensates=compensateService.findCompensate(registNo,"Y");
		if(compensates!=null && compensates.size()>0){
			for(PrpLCompensateVo vo:compensates){
				if("1101".equals(vo.getRiskCode()) && !"7".equals(vo.getUnderwriteFlag())){
					flag="1";
					break;
				}
			}
			if("0".equals(flag)){
				PrpLWfTaskVo prpLWfTaskVo=wfTaskHandleService.findWftaskInByRegistnoAndSubnode(registNo,"PrePayCI");
				if(prpLWfTaskVo!=null){
					flag="1";
				}
			}
		}
		if("1".equals(flag)){
			return "该案件在已存在预付任务，不能发起垫付任务。";
		}
		
//		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PadPay,"","")){
//			return "该案件在已存在垫付任务，不能再次发起垫付任务。";
//		}
		
		//根据业务表判断是否发起过垫付
		boolean isPadTask = false;
		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PadPay,"","0")){
			isPadTask = true;
		}
		PrpLPadPayMainVo padPay = getPadPayInfo(registNo,null);
		if((padPay != null && "submit".equals(padPay.getFlag())) || isPadTask){
			return "该案件在已存在垫付任务，不能再次发起垫付任务。";
		}
		
		if(!wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PLoss,"","1")){
			return "不存在人伤跟踪任务或者人伤跟踪任务未处理，不能发起垫付任务！";
		}
		
        //判断是否存在人伤在in表，在就能发起
        List<PrpLWfTaskVo> volist = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.PLoss.toString());
        if(volist!=null&&volist.size()>0){
        }else{
            List<PrpLDlossPersTraceMainVo> List = persTraceDubboService.findPersTraceMainVoList(registNo);
            if(List!=null&&List.size()>0){
                if(List.size() > 1){
                    Collections.sort(List, new Comparator<PrpLDlossPersTraceMainVo>() {
                        @Override
                        public int compare(PrpLDlossPersTraceMainVo o1,PrpLDlossPersTraceMainVo o2) {
                                return o2.getCreateTime().compareTo(o1.getCreateTime());
                            }
                        });
                }
                PrpLDlossPersTraceMainVo persTraceMainVo = List.get(0);
                if("7".equals(persTraceMainVo.getUnderwriteFlag())){
                    return "人伤任务已注销，不能发起垫付任务！";
                }
            }
        }
		// return "存在未处理的交强险计算书，不能发起垫付申请。";
		// return "该案件在理算环节已存在垫付任务，不能发起垫付任务。";
		// return "已存在交强预付任务，不能发起垫付任务。";
		
		// 理算环节存在已提交核赔且未核赔通过的交强险计算书时，不能发起垫付申请任务，否则点击“报案号”
		// 链接时提示“理算环节存在未通过的交强计算书，不能发起垫付申请。计算书号为：*******。”
		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.CompeCI,"","")){
			return "该案件已存在交强理算任务，不能发起垫付任务！";
		}
		/*PrpLCompensateVo compeVo = compensateService.queryCompensate(registNo,claimNo);
		if(compeVo!=null&&"".equals(compeVo.getUnderwriteFlag())){// !"".equals(compeVo.getUnderwriteFlag()
			return "理算环节存在未通过的交强计算书，不能发起垫付申请。计算书号为："+compeVo.getCompensateNo()+"。";
		}
		if(compeVo==null){
			return "存在未处理的交强险计算书，不能发起垫付申请。";
		}*/
		
		return retData;
	}
	
	/**
	 * 子表vOList转为 Po
	 * @param PrpLPadPayMain 主表
	 * @param voList
	 * @param poList
	 * @param idName 主键
	 * @param paramClass 子表class类
	 * @param method 主子表关联方法如"setPrpLDlossCarMain"
	 * @modified: ☆yangkun(2015年12月10日 上午11:32:05): <br>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void mergeList(PrpLPadPayMain padPayMain,List voList,List poList,String idName,Class paramClass,String method) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Integer, Object> keyMap = new HashMap<Integer, Object>();
		Map<Object, Object> poMap = new HashMap<Object, Object>();
		
		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			Object key;
			try {
				key = PropertyUtils.getProperty(element, idName);
				map.put(key, element);
				keyMap.put(i, key);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		for (Iterator it = poList.iterator(); it.hasNext();) {
			Object element = (Object) it.next();
			try {
				Object key = PropertyUtils.getProperty(element, idName);
				poMap.put(key, null);
				if (!map.containsKey(key)) {
					//delete(element);
					databaseDao.deleteByObject(paramClass,element);
					it.remove();
				} else {
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			try{
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if (key == null || !poMap.containsKey(key)) {
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					setMethod = paramClass.getDeclaredMethod(method, padPayMain.getClass());
					setMethod.invoke(poElement,padPayMain);
					
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	/*
	 * 垫付注销
	 * 
	 */
	/* 
	 * @see ins.sino.claimcar.padpay.service.PadPayService#padPayCancel
	 * (java.lang.String, ins.platform.vo.SysUserVo)
	 * @param taskId
	 * @param user
	 */
	@Override
	public void padPayCancel(String taskId,SysUserVo user) throws Exception {
		BigDecimal bd=new BigDecimal(taskId);
		Date date = new Date();
		// 将处理中（wftaskin表）的任务工作流数据移动到wftaskout表，并将该任务的处理状态（HandlerStatus）与WorkStatus置为注销状态（9）
		// 同时，删除wftaskin表的数据   note added by maofengning 2019年10月17日11:11:51
		wfTaskHandleService.cancelTask(user.getUserCode(), bd);
		PrpLWfTaskVo wfTaskVo=wfTaskHandleService.queryTask(Double.valueOf(taskId));
		PrpLPadPayMain  prpLPadPayMain = new PrpLPadPayMain();
		try {
			if(wfTaskVo!=null){
				PrpLPadPayMainVo padPayVo = this.getPadPayInfo(wfTaskVo.getRegistNo(),wfTaskVo.getClaimNo());
				if(padPayVo == null){
					prpLPadPayMain = this.setPrpLPadPayMain(prpLPadPayMain, wfTaskVo, user);
					prpLPadPayMain.setFlag("cancel");
					databaseDao.save(PrpLPadPayMainVo.class, prpLPadPayMain);
				}else{
					PrpLPadPayMain comPo = databaseDao.findByPK(PrpLPadPayMain.class, padPayVo.getId());
					comPo.setUnderwriteFlag("7");
					comPo.setUnderwriteDate(date);
					comPo.setUnderwriteUser(user.getUserCode());
					comPo.setFlag("cancel");
					databaseDao.update(PrpLPadPayMain.class, comPo);
				}
			}
		} catch (Exception e) {
			logger.info("垫付注销时，处理完工作流数据，更新业务数据失败！", e);
			throw new RuntimeException("垫付注销时，处理完工作流数据，更新业务数据失败！", e);
		}
	}
	
	private PrpLPadPayMain setPrpLPadPayMain(PrpLPadPayMain prpLPadPayMainVo,PrpLWfTaskVo wfTaskVo,SysUserVo user){
		prpLPadPayMainVo.setCompensateNo(wfTaskVo.getCompensateNo());
		prpLPadPayMainVo.setClaimNo(wfTaskVo.getClaimNo());
		prpLPadPayMainVo.setRegistNo(wfTaskVo.getRegistNo());
		prpLPadPayMainVo.setCreateTime(new Date());
		prpLPadPayMainVo.setCreateUser(wfTaskVo.getTaskInUser());
		prpLPadPayMainVo.setUnderwriteFlag("7");
		prpLPadPayMainVo.setUnderwriteDate(new Date());
		prpLPadPayMainVo.setUnderwriteUser(user.getUserCode());
		prpLPadPayMainVo.setUpdateTime(new Date());
		prpLPadPayMainVo.setUpdateUser(wfTaskVo.getTaskInUser());
		
		return prpLPadPayMainVo;
	}

	@Override
	public PrpLPadPayMainVo findPadPayMainByCompNo(String compensateNo) {
		PrpLPadPayMainVo padPayMainVo = null;
		
		QueryRule pfqr = QueryRule.getInstance();
		pfqr.addEqual("compensateNo",compensateNo);
		List<PrpLPadPayMain> padPayMains = databaseDao.findAll(PrpLPadPayMain.class,pfqr);
		if(padPayMains!=null && padPayMains.size()>0){
			PrpLPadPayMain padPayMain = padPayMains.get(0);
			padPayMainVo = Beans.copyDepth().from(padPayMain).to(PrpLPadPayMainVo.class);
		}
		
		return padPayMainVo;
	}

    @Override
    public List<PrpLPadPayPersonVo> findPrpLPadPayPersonBySettleNo(String settleNo) {
        List<PrpLPadPayPersonVo> prpLPadPayPersonVoList = new ArrayList<PrpLPadPayPersonVo>();
        QueryRule pfqr = QueryRule.getInstance();
        pfqr.addEqual("settleNo",settleNo);
        List<PrpLPadPayPerson> prpLPadPayPersons = databaseDao.findAll(PrpLPadPayPerson.class,pfqr);
        if(prpLPadPayPersons!=null && prpLPadPayPersons.size()>0){
            prpLPadPayPersonVoList = Beans.copyDepth().from(prpLPadPayPersons).toList(PrpLPadPayPersonVo.class);
        }
        return prpLPadPayPersonVoList;
    
    }

    @Override
    public List<PrpLPadPayMainVo> findPadPayMainBySettleNo(String settleNo) {
        
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append(" from PrpLPadPayMain payMain where 1=1 ");
        sqlUtil.append(" and exists(select 1 from PrpLPadPayPerson person where payMain.id=person.prpLPadPayMain.id and person.settleNo=?) ");
        sqlUtil.addParamValue(settleNo);
        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        
        List<PrpLPadPayMain> padPayMainList = databaseDao.findAllByHql(PrpLPadPayMain.class, sql, values);
        List<PrpLPadPayMainVo> mainList = new ArrayList<PrpLPadPayMainVo>();
        mainList = Beans.copyDepth().from(padPayMainList).toList(PrpLPadPayMainVo.class);
        return mainList;
    }

	@Override
	public boolean saveBeforeCheck(List<PrpLPadPayPersonVo> prpLPadPayPersonVos) {
		for(PrpLPadPayPersonVo prpLPadPayPersonVo :prpLPadPayPersonVos){
			if(!"2".equals(prpLPadPayPersonVo.getPayObjectKind())){
				if(StringUtils.isBlank(prpLPadPayPersonVo.getOtherCause())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public PrpLPadPayMainVo getPadPayInfoByClaimNo(String claimNo) {

		PrpLPadPayMainVo padPayMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addSql(" (underwriteFlag <> '7' or underwriteFlag is null) ");
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class, queryRule);
		if (padPayMain != null && padPayMain.size() > 0) {
			padPayMainVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return padPayMainVo;
	
	}

	/**
	 * 获取医疗费有责垫付限额
	 * @param registNo
	 * @return
	 */
	@Override
	public double getPadLimitAmount(String registNo){
		return configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,true,registNo);
	}
	
}
