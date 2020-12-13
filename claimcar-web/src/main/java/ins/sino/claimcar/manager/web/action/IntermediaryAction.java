/******************************************************************************
 * CREATETIME : 2016年1月6日 下午5:34:21
 ******************************************************************************/
package ins.sino.claimcar.manager.web.action;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.AccBankNameVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.manager.vo.PrpdIntermUserVo;
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
 * <pre></pre>
 * 
 * @author ★LiuPing
 * @CreateTime 2016年1月6日
 */


@Controller
@RequestMapping("/manager")
public class IntermediaryAction {
	private static Logger logger = LoggerFactory
			.getLogger(IntermediaryAction.class);
	// 服务装载
	@Autowired
	private ManagerService managerService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PayCustomService payCustomService;
	
	@RequestMapping(value = "/intermediaryList.do")
	// 查询机构信息Action
	public String intermediaryList(Model model) {
		return "manager/IntermediaryList";
	}
	
	// 查看详细机构信息Action
	@RequestMapping(value = "/intermediaryView.do")
	public ModelAndView intermediaryView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String id = (String) request.getParameter("Id");
		PrpdIntermMainVo intermMainVo = managerService.findIntermById(id);
		List<PrpdIntermServerVo> intermServerVos = intermMainVo
				.getPrpdIntermServers();
		PrpdIntermBankVo intermBankVo = intermMainVo.getPrpdIntermBank();
		List<PrpdIntermUserVo> intermUserVo = intermMainVo.getPrpdIntermUsers();

		mv.addObject("prpdIntermMainVo", intermMainVo);
		mv.addObject("prpdIntermBankVo", intermBankVo);
		mv.addObject("prpdIntermServerVos", intermServerVos);
		mv.addObject("intermUserList", intermUserVo);

		mv.setViewName("manager/IntermediaryView");
		return mv;
	}

	/**
	 * 增加或修改机构信息Action
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/intermediaryEdit.do")
	public ModelAndView intermediaryEdit(HttpServletRequest request) {
		String id = request.getParameter("Id");
		String flag = request.getParameter("flag");
		ModelAndView mv = new ModelAndView();
		Map<String, String> serviceTypeMap = codeTranService
				.findCodeNameMap("ServiceType");
		PrpdIntermMainVo intermMainVo = new PrpdIntermMainVo();
		PrpdIntermBankVo intermBankVo = new PrpdIntermBankVo();
		List<PrpdIntermServerVo> intermServerVos = new ArrayList<PrpdIntermServerVo>();

		if (StringUtils.isNotBlank(id)) {
			intermMainVo = managerService.findIntermById(id);
			intermServerVos = intermMainVo.getPrpdIntermServers();
			intermBankVo = intermMainVo.getPrpdIntermBank();
			mv.addObject("intermUserList", intermMainVo.getPrpdIntermUsers());
		}
		for (PrpdIntermServerVo intermServerVo : intermServerVos) {
			String serverType = intermServerVo.getServiceType();
			intermServerVo.setServiceName(serviceTypeMap.get(serverType));
			serviceTypeMap.remove(serverType);
		}

		for (String serverType : serviceTypeMap.keySet()) {
			PrpdIntermServerVo intermServerVo = new PrpdIntermServerVo();
			intermServerVo.setServiceType(serverType);
			intermServerVo.setServiceName(serviceTypeMap.get(serverType));
			intermServerVos.add(intermServerVo);
		}
		String comCode = WebUserUtils.getComCode();
		if(StringUtils.isNotBlank(comCode)){
			if(comCode.startsWith("0000") || comCode.startsWith("0001")){
				comCode = "";
			}else{
				comCode = comCode.substring(0, 2);
			}
		}
		Map<String,String> resultMap = managerService.findUserCode
				(comCode==null?"":comCode,null,null);
		mv.addObject("resultMap", resultMap);
		mv.addObject("prpdIntermMain", intermMainVo);
		mv.addObject("prpdIntermBank", intermBankVo);
		mv.addObject("prpdIntermServers", intermServerVos);
		mv.addObject("flag", flag);

		mv.setViewName("manager/IntermediaryEdit");

		return mv;
	}
	
	// 银行信息弹窗

	@RequestMapping(value = "/intermediaryEdit_Bank.do")
	public ModelAndView ShowBankInfo(HttpServletRequest request) {
		
		String id=request.getParameter("Id");
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNotBlank(id)){
		    PrpdIntermMainVo intermMainVo = managerService.findIntermById(id);
			PrpdIntermBankVo intermBankVo =  intermMainVo.getPrpdIntermBank();
			
	        mv.addObject("prpdIntermBank", intermBankVo); 
	        
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
	    mv.setViewName("manager/IntermediaryEdit_Bank");
        
		return mv;
	}
	/**
	 * 保存公估机构及银行信息Action
	 * 
	 * @param session
	 * @param intermMainVo
	 * @param intermBankVo
	 * @return
	 */
	@RequestMapping(value = "/saveIntermInfo.do")
	@ResponseBody
	public AjaxResult saveIntermInfo(HttpSession session,
			@FormModel("prpdIntermMainVo") PrpdIntermMainVo intermMainVo,
			@FormModel("prpdIntermBankVo") PrpdIntermBankVo intermBankVo,
			@FormModel("prpdIntermUserVo")  List<PrpdIntermUserVo> intermUserVos){
		
		SysUserVo userVo = WebUserUtils.getUser();
		//银行信息去除前后空格
		intermBankVo.setAccountName(StringUtils.trim(intermBankVo.getAccountName()));
		intermBankVo.setAccountNo(StringUtils.trim(intermBankVo.getAccountNo()));
		intermBankVo.setCertifyNo(StringUtils.trim(intermBankVo.getCertifyNo()));
		intermBankVo.setBankType(intermBankVo.getBankName());
		//intermBankVo.setMobile(StringUtils.trim(intermBankVo.getMobile()));
		AjaxResult ajaxResult = new AjaxResult();
		PrpdIntermMainVo reIntermMainVo;
		try {
			if(StringUtils.isNotBlank(intermBankVo.getAccountNo())){
				String errorMessage = managerService.existAccountAtCheckmBank(intermBankVo.getAccountNo());
	    		  if(StringUtils.isNotBlank(errorMessage)){
			  			throw new IllegalArgumentException(errorMessage);
	    		  }
	    	 }
			reIntermMainVo = managerService.saveOrUpdateInterm(intermMainVo, intermBankVo,intermUserVos,userVo);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			ajaxResult.setData(reIntermMainVo.getId());
			session.setAttribute("ReIntermMainVo", reIntermMainVo);
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		
		return ajaxResult;
	}
	@RequestMapping(value = "/saveIntermServerInfo.do")
	@ResponseBody
	public AjaxResult saveIntermServerInfo(HttpServletRequest request,
			@FormModel("prpdIntermServerVos") List<PrpdIntermServerVo> prpdIntermServerVos) {
		String intermId=request.getParameter("intermId");
		PrpdIntermMainVo intermMainVo = managerService.findIntermById(intermId);
		managerService.saveOrUpdateIntermServer(intermMainVo,
				prpdIntermServerVos);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}

	@RequestMapping("/intermediaryFind.do")
	@ResponseBody
	public String findInterm(
			Model model,// 公估机构查询
			@FormModel("prpdIntermMainVo") PrpdIntermMainVo intermMainVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		Page<PrpdIntermMainVo> page = managerService.find(intermMainVo,start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id",
				"intermCode", "intermName", "upperCode",
				"prpdIntermBank.bankName:BankCode",
				"prpdIntermBank.accountNo");
		logger.debug(jsonData);
		return jsonData;
	}
	
	
	@RequestMapping(value = "/backdata.ajax")
	@ResponseBody //系统维护界面，判断公估机构代码是不是已经存在
	public AjaxResult backdata(String intermCode){
		String  flag="false";
		AjaxResult ajaxResult =new AjaxResult();
		try{
			String comCode = WebUserUtils.getComCode();
			PrpdIntermMainVo interMainVo =managerService.findIntermByCode(intermCode,comCode);
			if(interMainVo!=null ){
				flag="true";
			}
		
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(flag);
	}
	catch(Exception e){
		ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
	}
		return ajaxResult;
	}
	/**
	 * 添加员工信息
	 * <pre></pre>
	 * @param per_index
	 * @param intermSize
	 * @param intermId
	 * @return 
	 * @modified:
	 * ☆niuqaing(2016年8月30日 下午3:49:07): <br>
	 */
	@RequestMapping("/addPersRow.ajax")
	@ResponseBody
	public ModelAndView addPersRow(int per_index,int intermSize,String userCode,String userName) {
		ModelAndView modelAndView = new ModelAndView();
		List<PrpdIntermUserVo> userList = new ArrayList<PrpdIntermUserVo>();
		PrpdIntermUserVo intermUserVo= new PrpdIntermUserVo();
		intermUserVo.setUserCode(userCode);
		intermUserVo.setUserName(userName);
		userList.add(intermUserVo);
		modelAndView.addObject("intermUserList", userList);


		Map<String, String> resultMap = managerService.findUserCode(
				WebUserUtils.getComCode().substring(0,2), null, null);

		modelAndView.addObject("resultMap", resultMap);
		modelAndView.addObject("size",per_index);
		modelAndView.addObject("pe_Idx",intermSize);
	
		modelAndView.setViewName("manager/IntermdiaryEdit_User_Tr");
		return modelAndView;
	}
	
	@RequestMapping("/existIntermCode.do")
	@ResponseBody
	public AjaxResult existIntermCode(@FormModel("prpdIntermMainVo") PrpdIntermMainVo intermMainVo){
		String flag = managerService.existIntermCode(intermMainVo.getIntermCode());
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		return ajaxResult;
	}
	
	/**
	 * 行号查询界面初始化请求
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆WLL(2016年9月22日 上午11:01:15): <br>
	 */
	@RequestMapping(value = "/findBankNoQueryList.do")
	public ModelAndView findBankNoInit() {
		ModelAndView mv = new ModelAndView();	
		mv.setViewName("manager/IntermediaryEdit_BankNumQueryList");

		return mv;
	}
	
	/**
	 * 行号查询
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆WLL(2016年9月22日 上午11:01:15): <br>
	 */
	@RequestMapping(value = "/bankNumberFind.do")
	@ResponseBody
	public String bankNumberFind(
			Model model,
			@FormModel("AccBankNameVo") AccBankNameVo accBankNameVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<AccBankNameVo> page = payCustomService.findBankNum(accBankNameVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page, "bankCode", "bankName" );
		return jsonData;
	}
	
}





