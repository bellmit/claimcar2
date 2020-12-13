package ins.sino.claimcar.lossperson.web.action;


import ins.platform.common.util.CodeTranUtil;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * @author zjd
 *
 */
@Controller
@RequestMapping("/defloss")
public class LossAction {

	private static Logger logger = LoggerFactory.getLogger(LossAction.class);
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService;


	@Autowired
	private DeflossHandleService deflossHandleService;
	

	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private PersTraceDubboService prpDubboService;
	
	@RequestMapping(value = "/findDefloss.do")
	public ModelAndView preAddDefloss(String registNo,String index) {
		ModelAndView mv = new ModelAndView();
	
		
		List<PrpLDlossCarMainVo>  lossCarMainVo= lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLdlossPropMainVo>  propMainVos= propTaskService.findPropMainListByRegistNo(registNo);
		
		
		/*BigDecimal sumLossFees = null;
		if(propMainVos!=null){
			for(int i=0;i<propMainVos.size();i++){
				List<PrpLdlossPropFeeVo> prpLdlossPropFees = propMainVos.get(i).getPrpLdlossPropFees();
				for(int j=0;i<prpLdlossPropFees.size();j++){
					sumLossFees =sumLossFees.add(prpLdlossPropFees.get(j).getVeriRecylePrice());
				}
				propMainVos.get(i).setSumLossFees(sumLossFees);
			}findPersTraceList
		}*/
		List<PrpLDlossPersTraceVo> vos = findPersTraceList(registNo);
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVos = prpDubboService.findPersTraceMainVoList(registNo);
		//取人伤费用核损人员
		if(vos!=null && vos.size()>0){
			if(prpLDlossPersTraceMainVos!=null && prpLDlossPersTraceMainVos.size()>0){
			  for(PrpLDlossPersTraceVo vo :vos){
				  vo.setUndwrtCode(prpLDlossPersTraceMainVos.get(0).getUndwrtFeeCode());
			    }
			}
		}
		
		 //取跟踪次数y
		if(prpLDlossPersTraceMainVos!=null && prpLDlossPersTraceMainVos.size()>0){
			if(prpLDlossPersTraceMainVos.get(0).getTraceTimes()!=null){
		      for(PrpLDlossPersTraceVo trace:vos){
			     trace.setRemark(prpLDlossPersTraceMainVos.get(0).getTraceTimes().toString());
			   }
			}
		}
		mv.addObject("lossCarMainVo", lossCarMainVo);
		mv.addObject("propMainVos", propMainVos);
		mv.addObject("prpLDlossPersTraceMainVos", vos);
		if(FlowNode.DLoss.equals(index)){
	    mv.setViewName("lossperson/common/loss/LossEdit");
	    }else{
	    mv.setViewName("lossperson/common/loss/VLossEdit");
	    }
		return mv;
	}
	//人伤单证打印按钮
	@RequestMapping(value = "/peoplePrint.do")
	public ModelAndView peoplePrint(String registNo){
		ModelAndView mv = new ModelAndView();
		 List<PrpLDlossPersTraceVo> PrpLDlossPersTraces= prpDubboService.findPrpLDlossPersTraceVoListByRegistNo(registNo);
		 List<PrpLDlossPersTraceVo> names= new ArrayList<PrpLDlossPersTraceVo>();
		 if(PrpLDlossPersTraces!=null && PrpLDlossPersTraces.size()>0){
			 for(PrpLDlossPersTraceVo prpLDlossPersTraceVo : PrpLDlossPersTraces){
				 if(prpLDlossPersTraceVo.getEndFlag().equals("1") && prpLDlossPersTraceVo.getValidFlag().equals("1")){
					 names.add(prpLDlossPersTraceVo);
				 }
			 }
		 }
		
		 mv.addObject("PrpLDlossPersTraceList", names);
		 mv.setViewName("loss-common/LosspersonPrint");
		 return mv;
	}
    
	public List<PrpLDlossPersTraceVo> findPersTraceList(String registNo){
		List<PrpLDlossPersTraceVo> traceList = new ArrayList<PrpLDlossPersTraceVo>();
		
		List<PrpLDlossPersTraceMainVo> lossPersMainList = prpDubboService.findPersTraceMainVoList(registNo);
		//组织医院信息
		if(lossPersMainList!=null && lossPersMainList.size()>0){
			for(PrpLDlossPersTraceMainVo traceMainVo : lossPersMainList){
				List<PrpLDlossPersTraceVo> traces = traceMainVo.getPrpLDlossPersTraces();
				if(traces!=null && traces.size()>0){
					for(PrpLDlossPersTraceVo traceVo: traces){
						traceVo.setCreateUser(CodeTranUtil.transCode("UserCode", traceVo.getOperatorCode()));//定损
						traceVo.setUpdateUser(CodeTranUtil.transCode("UserCode", traceVo.getUndwrtCode()));
						traceList.add(traceVo);
					}
				}	
			}
		}	
		
		return traceList;
	}
}
