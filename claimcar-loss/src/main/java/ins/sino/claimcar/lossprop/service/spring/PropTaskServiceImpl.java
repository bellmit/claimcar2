package ins.sino.claimcar.lossprop.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.lossprop.service.PropLossAdjustServiceImpl;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true",registry = {"default"})
@Path("propTaskService")
public class PropTaskServiceImpl implements PropTaskService{

	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	PropLossService propLossService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	
	@Autowired
	private DeflossService deflossService;
	@Autowired
	private WfTaskHandleService taskHandleService;
	@Autowired
	private PropLossAdjustServiceImpl propAdjustServie;
	/*
	 * 通过报案号查询 案件下所有的财产损失信息
	 */
	@Override
	public List<PrpLdlossPropMainVo> findPropMainListByRegistNo(String registNo){
		List<PrpLdlossPropMain> prpLdlossPropMains=null;
		List<PrpLdlossPropMainVo> prpLdlossPropMainVos=null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addNotEqual("underWriteFlag", CodeConstants.VeriFlag.CANCEL);
		prpLdlossPropMains=databaseDao.findAll(PrpLdlossPropMain.class, queryRule);
		if(prpLdlossPropMains!=null&&prpLdlossPropMains.size()>=1){
			prpLdlossPropMainVos=new ArrayList<PrpLdlossPropMainVo>();
			for(PrpLdlossPropMain prpLdlossPropMain:prpLdlossPropMains){//遍历转换
				PrpLdlossPropMainVo prpLdlossPropMainVo=new PrpLdlossPropMainVo();
				prpLdlossPropMainVo=Beans.copyDepth().from(prpLdlossPropMain).to(PrpLdlossPropMainVo.class);
				prpLdlossPropMainVos.add(prpLdlossPropMainVo);
			}
		}
		return prpLdlossPropMainVos;
	}
	
	@Override
	public PrpLdlossPropMainVo findPropMainVoById(Long id) {
		PrpLdlossPropMainVo propMainVo = propLossService.findPropMainVoById(id);
		return propMainVo;
	}

	/** 更新财产信息 **/
	@Override
	public void updateDLossProp(PrpLdlossPropMainVo dlossPropMain) {
		PrpLdlossPropMain propMain = databaseDao.findByPK(PrpLdlossPropMain.class,dlossPropMain.getId());
		if(propMain!=null){
			Beans.copy().from(dlossPropMain).excludeNull().to(propMain);
			databaseDao.update(PrpLdlossPropMain.class,propMain);
		}
	}

	/**
	 * 根据条件查询数据
	 * <pre></pre>
	 * @param registNo
	 * @param lossState
	 * @param underWriteFlag
	 * @param serialNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月26日 下午3:57:10): <br>
	 */
	@Override
	public List<PrpLdlossPropMainVo> findPrpLdlossPropMainVoListByCondition(String registNo,List<String> lossState,
			List<String> underWriteFlag,String serialNo) {
		List<PrpLdlossPropMainVo> prpLdlossPropMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(lossState != null && !"".equals(lossState)){
			queryRule.addIn("lossState",lossState);	
		}
		if(underWriteFlag != null && !"".equals(underWriteFlag)){
			queryRule.addIn("underWriteFlag",underWriteFlag);	
		}
		if(serialNo != null && !"".equals(serialNo)){
			queryRule.addEqual("serialNo",serialNo);
		}
		
		queryRule.addEqual("validFlag","1");
		
		List<PrpLdlossPropMain> prpLdlossPropMainList = databaseDao.findAll(PrpLdlossPropMain.class,queryRule);
		if(prpLdlossPropMainList != null && !prpLdlossPropMainList.isEmpty()){
			prpLdlossPropMainVo = Beans.copyDepth().from(prpLdlossPropMainList).toList(PrpLdlossPropMainVo.class);
		}
		return prpLdlossPropMainVo;
	}

	/**
	 * 根据QueryRule查找记录
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	@Override
	public List<PrpLdlossPropMainVo> findPrpLdlossPropMainVoListByRule(QueryRule queryRule) {
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = null;
		
		List<PrpLdlossPropMain> prpLdlossPropMainList = databaseDao.findAll(PrpLdlossPropMain.class,queryRule);
		if(prpLdlossPropMainList != null && !prpLdlossPropMainList.isEmpty()){
			prpLdlossPropMainVoList = Beans.copyDepth().from(prpLdlossPropMainList).toList(PrpLdlossPropMainVo.class);
		}
		return prpLdlossPropMainVoList;
	}

	@Override
	public boolean isDLossAllPassed(String registNo) {
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = deflossService.findLossCarMainByRegistNo(registNo);
	    List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = findPropMainListByRegistNo(registNo);
	    boolean vLoss = true;
	    
	    if(prpLDlossCarMainVoList != null){
		    for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
			    if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
				   vLoss = false;
			    }
		    }
	    }
	    if(prpLdlossPropMainVoList != null){
		    for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
			    if(!CodeConstants.VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
			 	   vLoss = false;
			    }
		    } 
	    }
	    if(vLoss){//判断未接收的定损任务   排除复检
		    List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),FlowNode.DLProp.name(),FlowNode.DLCar.name());
		    if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
			    for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
				    if(!prpLWfTaskVo.getSubNodeCode().equals(FlowNode.DLChk.name())){
					    vLoss =  false;
					    break;
				    }
			    }
		    }
	    }
	    return vLoss;
	}
	
	/**
	 * 修改定损对外接口
	 */
	public String propModifyLaunch(Long lossId,SysUserVo sysUserVo){
		
		return propAdjustServie.propModifyLaunch(lossId, sysUserVo);
	}

	@Override
	public String propAdditionLaunch(Long id, SysUserVo userVo,
			String licenseNo, String remarks) {
		
		return propAdjustServie.propAdditionLaunch(id,userVo, licenseNo, remarks);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateDLossPropByRegistNo(List<PrpLdlossPropMainVo> PrpLdlossPropMainList) {
		if (PrpLdlossPropMainList != null && PrpLdlossPropMainList.size() > 0) {
			for (PrpLdlossPropMainVo prpLdlossPropMainVo : PrpLdlossPropMainList) {
				PrpLdlossPropMain prpLdlossPropMain = databaseDao.findByPK(PrpLdlossPropMain.class, prpLdlossPropMainVo.getId());
				if(prpLdlossPropMainVo.getInvoiceFee()==null&&prpLdlossPropMain.getInvoiceFee()==null){
					continue;
				}else{
					prpLdlossPropMain.setInvoiceFee(prpLdlossPropMainVo.getInvoiceFee());
                    deflossService.updateDlossPropMain(prpLdlossPropMain);
				}
//				if (prpLdlossPropMain.getPrpLdlossPropFees() != null && prpLdlossPropMain.getPrpLdlossPropFees().size() > 0) {
//					if(prpLdlossPropMainVo.getPrpLdlossPropFees() != null && prpLdlossPropMainVo.getPrpLdlossPropFees().size() > 0){
//						for (int i = 0; i < prpLdlossPropMain.getPrpLdlossPropFees().size(); i++) {
//							System.out.println();
//							prpLdlossPropMain.getPrpLdlossPropFees().get(i).setInvoiceFee(prpLdlossPropMainVo.getPrpLdlossPropFees().get(i).getInvoiceFee());
//						}
//					}
//				}
			}
		}
	}
	
	@Override
	public List<PrpLdlossPropMainVo> findlossPropMainByUnderWriteFlag(String registNo,String underWriteFlag,String flag){
		List<PrpLdlossPropMainVo> lossPropMainVoList = new ArrayList<PrpLdlossPropMainVo>();
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		if("1".equals(flag)){
			queryRule.addEqual("underWriteFlag", underWriteFlag);
		}else if("0".equals(flag)){
			queryRule.addNotEqual("underWriteFlag", underWriteFlag);
		}
		List<PrpLdlossPropMain> lossPropMainList = databaseDao.findAll(PrpLdlossPropMain.class, queryRule);
		if(lossPropMainList!=null && lossPropMainList.size()>0){
			lossPropMainVoList = Beans.copyDepth().from(lossPropMainList).toList(PrpLdlossPropMainVo.class);
		}
		return lossPropMainVoList;
	}
	
}
