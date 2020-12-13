package ins.sino.claimcar.common.web.action;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.web.AjaxResult;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/checkView")
public class CheckViewAction {

	
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@RequestMapping(value = "/checkView.ajax")
	@ResponseBody
	public AjaxResult checkView(String registNo){
		//wfTaskHandleService.findEndTask(registNo,null,FlowNode.Chk);
		
		AjaxResult ajaxResult =new AjaxResult();
		BigDecimal taskId=null;
		try{
		//先查PrpLwfTaskIn表
		List<PrpLWfTaskVo> prpLWfTaskVoList =wfTaskHandleService.findPrpLWfTaskInByRegistNo(registNo);
		if(prpLWfTaskVoList !=null&&prpLWfTaskVoList.size()>0){
			for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
				if(prpLWfTaskVo.getSubNodeCode().equals("Chk")){
					taskId=prpLWfTaskVo.getTaskId();
					
				}
			}
		}
		//查PrpLwfTaskOut表
		if(taskId==null){
			List<PrpLWfTaskVo> prpLWfTaskVoList1 =wfTaskHandleService.findPrpLWfTaskOutByRegistNo(registNo);
	          if(prpLWfTaskVoList1 !=null&&prpLWfTaskVoList1.size()>0){
				for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList1){
					if(prpLWfTaskVo.getSubNodeCode().equals("Chk") && !(prpLWfTaskVo.getWorkStatus().equals("7")) && !(prpLWfTaskVo.getWorkStatus().equals("8"))){
						taskId=prpLWfTaskVo.getTaskId();
					}
				}
			}
		}
		
		
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(taskId);
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		return ajaxResult;
		
	}
}

