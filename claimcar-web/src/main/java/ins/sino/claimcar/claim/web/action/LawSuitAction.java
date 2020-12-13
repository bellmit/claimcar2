package ins.sino.claimcar.claim.web.action;



import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.claim.service.LawSiutService;
import ins.sino.claimcar.claim.service.PrpdLawFirmService;
import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.other.service.LawFirmService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/lawSuit")
public class LawSuitAction {
	@Autowired
	RegistQueryService registqueryservice;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	LawSiutService lawsiutService;
	@Autowired
	PrpdLawFirmService prpdLawFirmService;
	@Autowired
	LawFirmService lawFirmService;

	
	@RequestMapping("/lawSuitEdit.do")
	public ModelAndView lawSuitEdit(String registNo,String nodeCode) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date();
		String startDate=format.format(start);
		String endDate=startDate;
		PrpLRegistVo prplregistvo=registqueryservice.findByRegistNo(registNo);
		//初始化界面
        List<PrpLLawSuitVo> prpLLawSuitVo = new ArrayList<PrpLLawSuitVo>();// 诉讼信息
        
        prpLLawSuitVo = lawsiutService.findByRegistNo(registNo);
        
		ModelAndView modelAndView = new ModelAndView();
		List<PrpLDlossCarMainVo> prpLDlossCarMainList=lossCarService.findLossCarMainByRegistNo(registNo); //带入标的车车牌
		String licenseNo="";
		if(prpLDlossCarMainList.size() >0 && prpLDlossCarMainList != null){
			for(PrpLDlossCarMainVo losscar : prpLDlossCarMainList){
				//int a=losscar.getSerialNo();
				if(losscar.getSerialNo()==1){
					if(licenseNo.equals("")){
					 licenseNo=losscar.getLicenseNo();
					 break;
					}				 
					 //break;
				}
		}
		
			
		}
	//	String licenseNo=prpLLawSuitVo.get(0).getLicenseNo(); //带入标的车车牌号
		modelAndView.addObject("licenseNo", licenseNo);
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("endDate", endDate);
	/*	if(prpLLawSuitVo.size()>0){
			modelAndView.addObject("prpLLawSuitVo",prpLLawSuitVo);
			modelAndView.setViewName("lawSuit/LawSuitShowS");
		}else{
			modelAndView.setViewName("lawSuit/LawSuitInit");
		}*///先关闭，后面再打开
		//律师事务所名称
//		String comCode = WebUserUtils.getComCode();
//		if(comCode.subSequence(0, 4).equals("0002")){//深圳
//			comCode = comCode.subSequence(0, 4).toString();
//		}else{
//			comCode = comCode.subSequence(0, 2).toString();
//		}
//		List<PrpdLawFirmVo> vos = prpdLawFirmService.findPrpdLawFirmVo(comCode);
		//取消查询律师事务所的权限限制
		List<PrpdLawFirmVo> vos = lawFirmService.findAllPrpdLawFirm();
		modelAndView.addObject("prpdLawFirmVos",vos);
		
		if(prpLLawSuitVo.size()>0){
			modelAndView.addObject("prpLLawSuitVo",prpLLawSuitVo);
			modelAndView.setViewName("lawSuit/LawSuitShowEdit");
		}else{
			modelAndView.setViewName("lawSuit/LawSuitEdit");
		}
		modelAndView.addObject("registvo",prplregistvo);
		return modelAndView;
	}
	
	//
	@RequestMapping("/lawSuitByLEdit.do")
	public ModelAndView lawSuitByLEdit(String registNo,String nodeCode) {
        System.out.println("LawSuitAction.ClaimEdit()======");
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date();
		String startDate=format.format(start);
		PrpLRegistVo prplregistvo=registqueryservice.findByRegistNo(registNo);
		//初始化界面
        List<PrpLLawSuitVo> prpLLawSuitVo = new ArrayList<PrpLLawSuitVo>();// 诉讼信息
        prpLLawSuitVo = lawsiutService.findByRegistNo(registNo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("startDate", startDate);
		
		String comCode = WebUserUtils.getComCode();
		if(comCode.subSequence(0, 4).equals("0002")){//深圳
			comCode = comCode.subSequence(0, 4).toString();
		}else{
			comCode = comCode.subSequence(0, 2).toString();
		}
		List<PrpdLawFirmVo> vos = prpdLawFirmService.findPrpdLawFirmVo(comCode);
		modelAndView.addObject("prpdLawFirmVos",vos);
		if(prpLLawSuitVo.size()>0){
			modelAndView.addObject("prpLLawSuitVo",prpLLawSuitVo);
			modelAndView.setViewName("lawSuit/LawSuitShowEdit");
		}else{
			modelAndView.setViewName("lawSuit/LawSuitEdit");
		}
		modelAndView.addObject("registvo",prplregistvo);
		return modelAndView;
	}
	@RequestMapping("/add.ajax")
	@ResponseBody
	public ModelAndView addRowInfo(String id,String registNo,String nodeCode,String cacelId){
		ModelAndView modelAndView = new ModelAndView();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date();
		String startDate=format.format(start);
		String endDate=startDate;
		List<PrpLDlossCarMainVo> prpLDlossCarMainList=lossCarService.findLossCarMainByRegistNo(registNo); //带入标的车车牌
		String licenseNo="";
		if(prpLDlossCarMainList.size() >0 && prpLDlossCarMainList != null){
			for(PrpLDlossCarMainVo losscar : prpLDlossCarMainList){
				if(losscar.getSerialNo()==1){
					if(licenseNo.equals("")){
					 licenseNo=losscar.getLicenseNo();
					 break;
					}				 
					 //break;
				}
		}
		
			
		}
		//律师事务所名称
		String comCode = WebUserUtils.getComCode();
		if(comCode.subSequence(0, 4).equals("0002")){//深圳
			comCode = comCode.subSequence(0, 4).toString();
		}else{
			comCode = comCode.subSequence(0, 2).toString();
		}
		List<PrpdLawFirmVo> vos = prpdLawFirmService.findPrpdLawFirmVo(comCode);
		modelAndView.addObject("prpdLawFirmVos",vos);
		modelAndView.addObject("licenseNo", licenseNo);
		modelAndView.setViewName("lawSuit/LawSuitEdit_Law");
		modelAndView.addObject("id", id);
		modelAndView.addObject("cacelId", cacelId);
		modelAndView.addObject("registNo", registNo);
		modelAndView.addObject("nodeCode", nodeCode);
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("endDate", endDate);
		modelAndView.addObject("licenseNo", licenseNo);
		return modelAndView;
	}
	
	@RequestMapping("/saveMessage.do")
	@ResponseBody
	public AjaxResult save(@FormModel("prpLLawSuitVo") PrpLLawSuitVo lawsuitVo){
				AjaxResult ajaxResult=new AjaxResult();
				try{
					lawsuitVo.setNodeCode("check");
					lawsiutService.save(lawsuitVo);
					ajaxResult.setData("1");
					ajaxResult.setStatus(200);
					return ajaxResult;
				}catch(Exception e){
					ajaxResult.setData("2");
					return ajaxResult;
				}
	}
	
	@RequestMapping("/updateMessage.do")
	@ResponseBody
	public AjaxResult updateMessage(@FormModel("prpLLawSuitVo") PrpLLawSuitVo lawsuitVo){
				AjaxResult ajaxResult=new AjaxResult();
				try{
					lawsiutService.updateLawSuit(lawsuitVo);
					ajaxResult.setData("1");
					ajaxResult.setStatus(200);
					return ajaxResult;
				}catch(Exception e){
					ajaxResult.setData("2");
					return ajaxResult;
				}
	}
	@RequestMapping("/deteleMessage.do")
	@ResponseBody
	public AjaxResult deteleMessage(@FormModel("prpLLawSuitVo") PrpLLawSuitVo lawsuitVo){
				AjaxResult ajaxResult=new AjaxResult();
				if(lawsuitVo.getId()!=null){
					try{
						lawsiutService.deleteLawSuit(lawsuitVo);
						ajaxResult.setData("1");
						ajaxResult.setStatus(200);
						return ajaxResult;
					}catch(Exception e){
						ajaxResult.setData("2");
						return ajaxResult;
					}
				}else{
					ajaxResult.setData("1");
					ajaxResult.setStatus(200);
					return ajaxResult;
				}
				
	}

	/**
	 * 校验是否录入诉讼信息
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/findLawSuit.ajax")
	@ResponseBody
	public AjaxResult findLawSuit(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		List<PrpLLawSuitVo> prpLLawSuitVoList = lawsiutService.findByRegistNo(registNo);
		ajaxResult.setData(prpLLawSuitVoList.size());
		return ajaxResult;
	}
	
}


