package ins.sino.claimcar.szpoliceaccident.web.action;

import java.util.List;

import ins.sino.claimcar.trafficplatform.service.SzpoliceCaseService;
import ins.sino.claimcar.trafficplatform.vo.AccidentResInfo;
import ins.framework.dao.database.support.Page;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.apc.annotation.AvoidRepeatableCommit;
import ins.platform.utils.ReadConfigUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants.ResultAmount;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.regist.web.action.PolicyQueryAction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;




import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/szpolice")
public class SzpoliceAction {
	
	private static Logger logger = LoggerFactory.getLogger(PolicyQueryAction.class);
	
	@Autowired
	SzpoliceCaseService szPoliceCaseService;

	@RequestMapping("/szPoliceCaseInfoList.do")
	@ResponseBody
	public String szPoliceCaseInfoList(Model model,// 公估机构查询
			@FormModel("AccidentResInfo") AccidentResInfo accidentResInfo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length){

	Page page= szPoliceCaseService.findPolicyAccidentForPage(accidentResInfo,start,length);
	
	
	
	String jsonData = ResponseUtils.toDataTableJson(page, "accidentNo",
			"registNo", "reporterName","reporterPhoneNo", "accidentDate","accidentAddress","accidentCause",
			"reportMode","plateNos","surveyorName","surveyorPhone");
	logger.debug(jsonData);
	return jsonData;
	}
	
	
	@RequestMapping(value = "/szpoliceInfoShow.do")
	public String szpoliceInfoShow(HttpServletRequest request) throws ParseException {
		return "flowQuery/szpoliceInfoShow";
	}
	
	@RequestMapping(value = "/accidentDetails")
	public String accidentDetails(Model model){
		
		return "";
		
	}
	
}
	
//  	@RequestMapping(value = "/accidentSearchs.do", method = RequestMethod.POST)
//	@ResponseBody
//	@AvoidRepeatableCommit
//	public String accidentSearchs(
//			@FormModel("accidentResInfo") AccidentInformationInfoVo accidentResInfo,
//			@RequestParam(value = "start", defaultValue = "0") Integer start,
//			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
//		
//  		
//try{
//			
//		//	Page<AccidentInformationInfoVo> page = szPoliceCaseService.findPolicyAccidentForPage(accidentResInfo,start,length);
//			for(AccidentInformationInfoVo vo :page.getResult()){
//				vo.setCounts(page.getResult().size());
//			}
//			String jsonData = ResponseUtils.toDataTableJson(page,"registNo","accidentNo","reporterName","reporterPhoneNo","accidentDate","plateNo");
//			return jsonData;
//		}catch(Exception e){
//			log.error("保单查询出错",e);
//			throw e;
////		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","accidentNo","reporterName","reporterPhoneNo","accidentDate","plateNo");
////		return jsonData;
//	}
// }
  	
	

