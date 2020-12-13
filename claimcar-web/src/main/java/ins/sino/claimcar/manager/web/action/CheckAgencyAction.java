/******************************************************************************
* CREATETIME : 2019年7月30日 上午10:03:48
******************************************************************************/
package ins.sino.claimcar.manager.web.action;

import ins.framework.dao.database.support.Page;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.checkagency.service.CheckAgencyService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckServerVo;
import ins.sino.claimcar.manager.vo.PrpdcheckUserVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * 
 * <pre></pre>
 * @author ★XHY
 */
@Controller
@RequestMapping("/checkagency")
public class CheckAgencyAction {
	private Logger logger = LoggerFactory.getLogger(CheckAgencyAction.class);
	
	@Autowired 
	private CheckAgencyService checkAgencyService;
	
	@Autowired
	private CodeTranService codeTranService;
	
	@Autowired
	private PayCustomService payCustomService;
	/**
	 * 进入查勘机构页面
	 * <pre></pre>
	 * @param model
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午2:23:14): <br>
	 */
	@RequestMapping(value = "/checkagencyList")
	public String checkagencyList(Model model) {
		SysUserVo user = WebUserUtils.getUser();
		if(user != null){
			String userComCode = null; 
			if(user.getComCode().startsWith("0000") || user.getComCode().startsWith("0001") || user.getComCode().startsWith("0002")){
				userComCode = user.getComCode().substring(0,4) + "0000";
			} else {
				userComCode = user.getComCode().substring(0,2) + "000000";
			}
			model.addAttribute("userComCode",userComCode);
		}
		return "manager/CheckAgencyList";
	}
	
	/**
	 * 增加或修改查勘机构信息
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午2:42:15): <br>
	 */
	@RequestMapping(value = "/checkAgencyEdit")
	public ModelAndView checkAgencyEdit(HttpServletRequest request) {
		String id = request.getParameter("Id");
		String flag = request.getParameter("flag");
		ModelAndView mv = new ModelAndView();
		Map<String, String> serviceTypeMap = codeTranService
				.findCodeNameMap("ServiceType");
		PrpdCheckBankMainVo checkBankMainVo = new PrpdCheckBankMainVo();
		PrpdcheckBankVo checkBankVo = new PrpdcheckBankVo();
		List<PrpdcheckServerVo> checkServerVos = new ArrayList<PrpdcheckServerVo>();

		if (StringUtils.isNotBlank(id)) {
			checkBankMainVo = checkAgencyService.findcheckById(id);
			checkServerVos = checkBankMainVo.getPrpdcheckServers();
			checkBankVo = checkBankMainVo.getPrpdcheckBank();
			mv.addObject("checkUserList", checkBankMainVo.getPrpdcheckUsers());
		}
		for (PrpdcheckServerVo checkServerVo : checkServerVos) {
			String serverType = checkServerVo.getServiceType();
			checkServerVo.setServiceName(serviceTypeMap.get(serverType));
			serviceTypeMap.remove(serverType);
		}

		for (String serverType : serviceTypeMap.keySet()) {
			PrpdcheckServerVo checkServerVo = new PrpdcheckServerVo();
			checkServerVo.setServiceType(serverType);
			checkServerVo.setServiceName(serviceTypeMap.get(serverType));
			checkServerVos.add(checkServerVo);
		}
		String comCode = WebUserUtils.getComCode();
		if(StringUtils.isNotBlank(comCode)){
			if(comCode.startsWith("0000") || comCode.startsWith("0001")){
				comCode = "";
			}else{
				comCode = comCode.substring(0, 2);
			}
		}
		Map<String,String> resultMap = checkAgencyService.findUserCode
				(comCode==null?"":comCode,null,null);
		mv.addObject("resultMap", resultMap);
		mv.addObject("prpdCheckBankMain", checkBankMainVo);
		mv.addObject("prpdcheckBank", checkBankVo);
		mv.addObject("prpdcheckServers", checkServerVos);
		mv.addObject("flag", flag);

		mv.setViewName("manager/CheckAgencyEdit");
		return mv;
	}
	
	/**
	 * 判断是否存在此查勘机构编码
	 * <pre></pre>
	 * @param checkBankMainVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月31日 下午3:47:53): <br>
	 */
	@RequestMapping("/existCheckAgencyCode")
	@ResponseBody
	public AjaxResult existCheckCode(@FormModel("prpdCheckBankMainVo") PrpdCheckBankMainVo checkBankMainVo){
		String flag = checkAgencyService.existcheckCode(checkBankMainVo.getCheckCode());
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		return ajaxResult;
	}
	
	/**
	 * 保存查勘机构及银行信息
	 * <pre></pre>
	 * @param session
	 * @param checkBankMainVo
	 * @param checkBankVo
	 * @param checkUserVos
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午3:11:06): <br>
	 */
	@RequestMapping(value = "/saveCheckAgencyInfo")
	@ResponseBody
	public AjaxResult saveCheckAgencyInfo(HttpSession session,
			@FormModel("prpdCheckBankMainVo") PrpdCheckBankMainVo checkBankMainVo,
			@FormModel("prpdcheckBankVo") PrpdcheckBankVo checkBankVo,
			@FormModel("prpdcheckUserVo")  List<PrpdcheckUserVo> checkUserVos){
		
		SysUserVo userVo = WebUserUtils.getUser();
		//银行信息去除前后空格
		checkBankVo.setAccountName(StringUtils.trim(checkBankVo.getAccountName()));
		checkBankVo.setAccountNo(StringUtils.trim(checkBankVo.getAccountNo()));
		checkBankVo.setCertifyNo(StringUtils.trim(checkBankVo.getCertifyNo()));
		checkBankVo.setMobile(StringUtils.trim(checkBankVo.getMobile()));
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if(StringUtils.isNotBlank(checkBankVo.getAccountNo())){
				String errorMessage = checkAgencyService.existAccountAtIntermBank(checkBankVo.getAccountNo());
	    		  if(StringUtils.isNotBlank(errorMessage)){
			  			throw new IllegalArgumentException(errorMessage);
	    		  }
	    	 }
			PrpdCheckBankMainVo reCheckBankMainVo = checkAgencyService.saveOrUpdateCheck(checkBankMainVo, checkBankVo,checkUserVos,userVo);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			ajaxResult.setData(reCheckBankMainVo.getId());
			session.setAttribute("reCheckBankMainVo", reCheckBankMainVo);
			
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText("保存失败，" + e.getMessage());
			logger.error("保存查勘机构信息失败",e);
		}
		return ajaxResult;
	}
	

	/**
	 * 編輯銀行信息
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月31日 下午3:41:13): <br>
	 */
	@RequestMapping(value = "/checkAgencyEdit_Bank")
	public ModelAndView ShowBankInfo(HttpServletRequest request) {
		String id=request.getParameter("Id");
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNotBlank(id)){
		    PrpdCheckBankMainVo checkMainVo = checkAgencyService.findcheckById(id);
		    
			PrpdcheckBankVo checkBankVo =  checkMainVo.getPrpdcheckBank();
			
	        mv.addObject("prpdcheckBank", checkBankVo); 
	        
		}
		//将查出的数据放入Map中去
		Map<String,String> bankCodeMap = new HashMap<String,String>();
		List<PrplOldaccbankCodeVo> listVo=payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo!=null && listVo.size()>0){
			for(PrplOldaccbankCodeVo vo:listVo){
				bankCodeMap.put(vo.getBankCode(), vo.getBankName());
			}
		}
		mv.addObject("bankCodeMap",bankCodeMap);
	    mv.setViewName("manager/CheckAgencyEdit_Bank");   
		return mv;
	}
	
	@RequestMapping("/checkAgencyFind")
	@ResponseBody
	public String findCheckAgency(
			Model model,// 查勘机构查询
			@FormModel("prpdCheckBankMainVo") PrpdCheckBankMainVo checkMainVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		Page<PrpdCheckBankMainVo> page = checkAgencyService.find(checkMainVo,start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id",
				"checkCode", "checkName", "upperCode",
				"prpdcheckBank.bankName:BankCode",
				"prpdcheckBank.accountNo");
		logger.debug(jsonData);
		return jsonData;
	}
	/**
	 * 进入检索行号页面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月31日 下午5:41:38): <br>
	 */
	@RequestMapping(value = "/findBankNoQueryList")
	public ModelAndView findBankNoInit() {
		ModelAndView mv = new ModelAndView();	
		mv.setViewName("manager/CheckAgencyEdit_BankNumQueryList");
		return mv;
	}
	
	/**
	 * 添加员工信息
	 * <pre></pre>
	 * @param per_index
	 * @param checkSize
	 * @param userCode
	 * @param userName
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月1日 下午5:59:44): <br>
	 */
	@RequestMapping("/addPersRow.ajax")
	@ResponseBody
	public ModelAndView addPersRow(int per_index,int checkSize,String userCode,String userName) {
		ModelAndView modelAndView = new ModelAndView();
		List<PrpdcheckUserVo> userList = new ArrayList<PrpdcheckUserVo>();
		PrpdcheckUserVo checkUserVo= new PrpdcheckUserVo();
		checkUserVo.setUserCode(userCode);
		checkUserVo.setUserName(userName);
		userList.add(checkUserVo);
		modelAndView.addObject("checkUserList", userList);


		Map<String, String> resultMap = checkAgencyService.findUserCode(
				WebUserUtils.getComCode().substring(0,2), null, null);

		modelAndView.addObject("resultMap", resultMap);
		modelAndView.addObject("size",per_index);
		modelAndView.addObject("pe_Idx",checkSize);
	
		modelAndView.setViewName("manager/CheckAgencyEdit_User_Tr");
		return modelAndView;
	}
	
	@RequestMapping(value = "/saveCheckServerInfo")
	@ResponseBody
	public AjaxResult saveCheckServerInfo(HttpServletRequest request,
			@FormModel("prpdcheckServerVos") List<PrpdcheckServerVo> prpdcheckServerVos) {
		String checkId=request.getParameter("checkBankId");
		PrpdCheckBankMainVo checkMainVo = checkAgencyService.findcheckById(checkId);
		checkAgencyService.saveOrUpdateCheckServer(checkMainVo,
				prpdcheckServerVos);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
}
