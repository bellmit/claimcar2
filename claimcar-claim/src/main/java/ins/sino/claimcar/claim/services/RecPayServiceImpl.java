package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.po.PrplReplevyDetail;
import ins.sino.claimcar.claim.po.PrplReplevyMain;
import ins.sino.claimcar.claim.vo.PrplReplevyDetailVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.newpayment.service.ClaimToNewPaymentService;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("recPayService")
public class RecPayServiceImpl implements RecPayService {
	private static Logger logger = LoggerFactory.getLogger(RecPayServiceImpl.class);
	
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	BillNoService billNoService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimToPaymentService claimToPaymentService;
	@Autowired
	ClaimToNewPaymentService claimToNewPaymentService;
	
	@Override
	public PrplReplevyMainVo saveOrUpdatePrplReplevyMain(PrplReplevyMainVo prplReplevyMainVo, Double flowTaskId) {
		Date date = new Date();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		String handlerIdKey = wfTaskVo.getHandlerIdKey();
		String handlerUser = prplReplevyMainVo.getCreateUser();
		String handlerCom = prplReplevyMainVo.getComCode();
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(prplReplevyMainVo.getRegistNo(),prplReplevyMainVo.getPolicyNo());
		if (cMainVo != null) {
			prplReplevyMainVo.setMakeCom(cMainVo.getMakeCom());
		}
		if (StringUtils.isEmpty(prplReplevyMainVo.getCompensateNo())) {
			prplReplevyMainVo.setCompensateNo(billNoService.getRecPayNo(prplReplevyMainVo.getComCode(),prplReplevyMainVo.getRiskCode()));
		}
		//save
		PrplReplevyMain prplReplevyMain = null;
		if (prplReplevyMainVo.getId() != null) {
			prplReplevyMain = databaseDao.findByPK(PrplReplevyMain.class,prplReplevyMainVo.getId());
			Beans.copy().from(prplReplevyMainVo).excludeNull().to(prplReplevyMain);//将页面上的VO省略空。将值赋给po
			
			List<PrplReplevyDetail> subPoList = prplReplevyMain.getPrplReplevyDetails();
			//当id不为空时，进行的维护主子表关系
			this.mergeList(prplReplevyMain,prplReplevyMainVo.getPrplReplevyDetails(),subPoList, "id",PrplReplevyDetail.class, "setPrplReplevyMain");
			prplReplevyMain = setUser(prplReplevyMain);
		} else {
			prplReplevyMain = Beans.copyDepth().from(prplReplevyMainVo).to(PrplReplevyMain.class);
			String userCode = prplReplevyMainVo.getHandlerCode();
			prplReplevyMain.setCreateUser(userCode);
			prplReplevyMain.setCreateTime(date);
			prplReplevyMain.setUpdateUser(userCode);
			prplReplevyMain.setUpdateTime(date);
			// 当id为空时维护主子表关系
			List<PrplReplevyDetail> subPoList = new ArrayList<PrplReplevyDetail>();
			for (PrplReplevyDetailVo subVo : prplReplevyMainVo.getPrplReplevyDetails()) {
				PrplReplevyDetail subPo = new PrplReplevyDetail();
				subPo = Beans.copyDepth().from(subVo).to(PrplReplevyDetail.class);
				subPo.setPrplReplevyMain(prplReplevyMain);
				subPoList.add(subPo);
			}
			prplReplevyMain.setPrplReplevyDetails(subPoList);
			prplReplevyMain = setUser(prplReplevyMain);
		}
		if(prplReplevyMain.getId() == null){
			databaseDao.save(PrplReplevyMain.class, prplReplevyMain);
		}else{
			databaseDao.update(PrplReplevyMain.class, prplReplevyMain);
		}
		
		prplReplevyMainVo.setId(prplReplevyMain.getId());
		//zhubin
		handlerIdKey = prplReplevyMainVo.getCompensateNo();
		wfTaskHandleService.tempSaveTaskByRecPay(flowTaskId, handlerIdKey,handlerUser, handlerCom, prplReplevyMainVo);
		prplReplevyMainVo = Beans.copyDepth().from(prplReplevyMain).to(PrplReplevyMainVo.class);
		return prplReplevyMainVo;
	}
	
	private PrplReplevyMain setUser(PrplReplevyMain prplReplevyMain) {
		for (PrplReplevyDetail rd : prplReplevyMain.getPrplReplevyDetails()) {
			rd.setRegistNo(prplReplevyMain.getRegistNo());
			rd.setCreateTime(prplReplevyMain.getCreateTime());
			rd.setCreateUser(prplReplevyMain.getCreateUser());
			rd.setUpdateTime(prplReplevyMain.getUpdateTime());
			rd.setUpdateUser(prplReplevyMain.getUpdateUser());
		}
		return prplReplevyMain;
	}
	
//	@Override
//	public PrplReplevyDetail saveOrUpdatePrplReplevyDetail(PrplReplevyDetailVo prplReplevyDetailVo){
//		PrplReplevyDetail prplReplevyDetail=Beans.copyDepth().from(prplReplevyDetailVo)
//				.to(PrplReplevyDetail.class);
//		if(prplReplevyDetailVo.getId()!=null){
//			databaseDao.update(PrplReplevyDetail.class, prplReplevyDetail);
//		}else{
//			databaseDao.save(PrplReplevyDetail.class, prplReplevyDetail);
//		}
//		return prplReplevyDetail;
//	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.claim.services.RecPayService#commitRecPay(java.lang.Double, ins.sino.claimcar.claim.vo.PrplReplevyMainVo)
	 */
	@Override
	public void commitRecPay(Double flowTaskId,PrplReplevyMainVo prplReplevyMainVo,SysUserVo userVo){
		Date date = new Date();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);//查询当前taskVo
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
	    PrplReplevyMainVo replevyMainVo = this.saveOrUpdatePrplReplevyMain(prplReplevyMainVo, flowTaskId);
	    submitVo.setTaskInKey(replevyMainVo.getId().toString());
		submitVo.setFlowId(taskVo.getFlowId());
		submitVo.setFlowTaskId(new BigDecimal(flowTaskId));
		submitVo.setComCode(policyViewService.getPolicyComCode(taskVo.getRegistNo()));
		submitVo.setTaskInUser(userVo.getUserCode());
		submitVo.setAssignCom(userVo.getComCode());
		submitVo.setAssignUser(userVo.getUserCode());
		submitVo.setCurrentNode(FlowNode.RecPay);
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandlertime(date);
		submitVo.setHandleruser(userVo.getUserCode());
		PrpLWfTaskVo prpLWfTaskVo=wfTaskHandleService.submitRecPay(replevyMainVo, submitVo);
		//如果追偿赔款和费用都为0则不送收付
		if((prplReplevyMainVo.getSumRealReplevy() != null && !(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumRealReplevy())==0))||
				(prplReplevyMainVo.getSumReplevyFee()!=null && !(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumReplevyFee())==0))){
			try{
				// claimToPaymentService.recPayToPayment(prplReplevyMainVo);//追偿送收付
				claimToNewPaymentService.recPayToNewPayment(prplReplevyMainVo);
			}
			catch(Exception e){
				// TODO Auto-generated catch block
				logger.info("追偿计算书：" + prplReplevyMainVo.getCompensateNo() + "追偿送收付异常！", e);
				e.printStackTrace();
			}
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.claim.services.RecPayService#findPrplReplevyMainVoByPK(java.lang.Long)
	 */
	@Override
	public PrplReplevyMainVo findPrplReplevyMainVoByPK(Long id) {

		PrplReplevyMainVo prplReplevyMainVo = new PrplReplevyMainVo();
		if (id != null) {
			PrplReplevyMain prplReplevyMain = databaseDao.findByPK(PrplReplevyMain.class, id);

			prplReplevyMainVo = Beans.copyDepth().from(prplReplevyMain).to(PrplReplevyMainVo.class);
		}

		return prplReplevyMainVo;

	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.claim.services.RecPayService#findPrplReplevyMainVoByClaimNo(java.lang.String)
	 */
	@Override
	public PrplReplevyMainVo findPrplReplevyMainVoByClaimNo(String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("claimNo",claimNo);
		List<PrplReplevyMain> prplReplevyMains=databaseDao.findAll(PrplReplevyMain.class, queryRule);
		PrplReplevyMainVo prplReplevyMainVo = new PrplReplevyMainVo();
		if(prplReplevyMains!=null&&prplReplevyMains.size()>0){
			prplReplevyMainVo=Beans.copyDepth().from(prplReplevyMains.get(0)).to(PrplReplevyMainVo.class);
		}
		return prplReplevyMainVo;
	}
	
	/**
	 * 
	 * 子表vOList转为 b
	 * @param lossCarMain 主表
	 * @param voList 
	 * @param poList
	 * @param idName 主键
	 * @param paramClass  子表class类
	 * @param method  主子表关联方法如"setPrpLDlossCarMain"
	 * @modified:
	 * ☆yangkun(2015年12月10日 上午11:32:05): <br>
	 */
	public void mergeList(PrplReplevyMain prplReplevyMain,List voList, List poList, String idName,Class paramClass, String method){
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
					setMethod = paramClass.getDeclaredMethod(method, prplReplevyMain.getClass());
					setMethod.invoke(poElement,prplReplevyMain);
					
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}

	@Override
	public BigDecimal startRecPay(String claimNo,String registNo) {
		PrpLWfTaskVo prpLWfTaskVo = new PrpLWfTaskVo();
		BigDecimal taskid = null;
		List<PrpLWfTaskVo> taskOutVos = wfTaskHandleService.findTaskOutVo(registNo, "VClaim");
		if(taskOutVos != null && taskOutVos.size()>0){
			//取PrpLWfTaskOut表中核赔的最后一次完成的时间
			for(PrpLWfTaskVo vo:taskOutVos){
				if(claimNo.equals(vo.getClaimNo())){
					prpLWfTaskVo = vo;
					break;
				}
			}
			prpLWfTaskVo.setUpperTaskId(prpLWfTaskVo.getTaskId());
			prpLWfTaskVo.setTaskInNode(prpLWfTaskVo.getSubNodeCode());

			prpLWfTaskVo.setTaskId(null);
			prpLWfTaskVo.setTaskName("追偿");
			prpLWfTaskVo.setNodeCode("RecPay");
			prpLWfTaskVo.setSubNodeCode("RecPay");
			prpLWfTaskVo.setHandlerStatus("0");
			prpLWfTaskVo.setWorkStatus("0");
			prpLWfTaskVo.setTaskInTime(new Date());
			prpLWfTaskVo.setAssignUser("");
			prpLWfTaskVo.setHandlerUser("");
			prpLWfTaskVo.setHandlerCom("");
			prpLWfTaskVo.setHandlerTime(null);
			
			prpLWfTaskVo.setTaskOutTime(null);
			prpLWfTaskVo.setTaskOutKey("");
			prpLWfTaskVo.setTaskOutUser("");
			
			prpLWfTaskVo.setShowInfoXML("");
			prpLWfTaskVo.setYwTaskType("");
			prpLWfTaskVo.setTaskOutNode("");
			prpLWfTaskVo.setMoney(null);
			
			taskid = wfTaskHandleService.recPayLaunch(prpLWfTaskVo);
		}
		return taskid;
	}

	@Override
	public PrplReplevyMainVo findPrplReplevyMainVoByComPensateNo(
			String comPensateNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("compensateNo",comPensateNo);
		List<PrplReplevyMain> prplReplevyMains=databaseDao.findAll(PrplReplevyMain.class, queryRule);
		PrplReplevyMainVo prplReplevyMainVo = new PrplReplevyMainVo();
		if(prplReplevyMains!=null&&prplReplevyMains.size()>0){
			prplReplevyMainVo=Beans.copyDepth().from(prplReplevyMains.get(0)).to(PrplReplevyMainVo.class);
		}
		return prplReplevyMainVo;
	}

}
