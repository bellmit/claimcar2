package ins.sino.claimcar.manager.web.action;




import java.util.List;

import ins.framework.dao.database.support.Page;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.sino.claimcar.flow.service.AppraisaService;
import ins.sino.claimcar.manager.vo.PrpdAppraisaVo;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manager")
public class AppraisaInforAction {
  
	private static Logger logger = LoggerFactory.getLogger(RepairFactoryAction.class);
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	AppraisaService appraisaService;
	
	/**
	 * 请求伤残鉴定机构页面
	 * @return
	 */
	@RequestMapping("/appraisaInforList.do")
	public ModelAndView appraisaList(){
		ModelAndView mav=new ModelAndView();
		
		mav.setViewName("manager/AppraisaqueryList");
		return mav;
	}
	  
	@RequestMapping(value="/appraisaFind.do")
	@ResponseBody
	public String hospitalFind(
			Model model,
			@FormModel("prpdAppraisaVo") PrpdAppraisaVo prpdAppraisaVo,//页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,//分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) {//展示条数
		Page<PrpdAppraisaVo> page =appraisaService.findAllAppraisa(prpdAppraisaVo, start, length);
		List<PrpdAppraisaVo> prpdAppraisaVos=page.getResult();
		if(prpdAppraisaVos!=null && prpdAppraisaVos.size()>0){
			String creatUser="";
			for(PrpdAppraisaVo prpdAppraisa:prpdAppraisaVos){
				creatUser=codeTranService.transCode("UserCode",prpdAppraisa.getCreatUser());
				prpdAppraisa.setCreatUser(creatUser);
				if("0".equals(prpdAppraisa.getValidStatus())){
					prpdAppraisa.setValidStatus("无效");
				}else{
					prpdAppraisa.setValidStatus("有效");
				}
			}
			
		}
		
		
		String jsonData = ResponseUtils.toDataTableJson(page, "appraisaName",
				"appraisaCode", "creatUser", "creatTime","validStatus","id");
		//logger.debug(jsonData);
		return jsonData;
	}
	
	/**
	 * 修改伤残鉴定机构信息页面
	 * @CreateTime 2016年1月30日 下午3:46:50
	 * @author lichen
	 */
	@RequestMapping(value="/AppraisaEdit.do")
	@ResponseBody
	public ModelAndView ModifyAppraisaInforEdit(
			@RequestParam(value="id")long id){
		ModelAndView mv=new ModelAndView();
		  PrpdAppraisaVo prpdAppraisaVo=appraisaService.findAppraisaById(id);
		
		 
	     mv.addObject("prpdAppraisaVo", prpdAppraisaVo);//添加模型到
			
		 mv.setViewName("manager/appraisaEdit");
		return mv;	
	}
	
	/**
	 * 修改伤残鉴定机构信息页面
	 * @CreateTime 2016年1月30日 下午3:46:50
	 * @author lichen
	 */
	@RequestMapping(value="/addAppraisa.do")
	@ResponseBody
	public ModelAndView addAppraisaEdit(){
		ModelAndView mv=new ModelAndView();
		  
		 mv.setViewName("manager/addAppraisa");
		return mv;	
	}
	
	/*
	 * 前端发送异步请求，校验伤残机构代码是否存在
	 */
	@RequestMapping(value="/appraisaVerify.do")
	@ResponseBody
	public AjaxResult findFactoryCode(String appraisaCode,String sign){
		boolean  exists=appraisaService.findAppraisaCode(appraisaCode);
		AjaxResult ajaxResult=new AjaxResult();
		if(exists){
			//appraisaCode可用
			ajaxResult.setData("1");
			ajaxResult.setStatus(200);
		}else{
			//appraisaCode不可用
			ajaxResult.setData("2");
		}
		return ajaxResult;
	}
	
	
	 /* *
	  * 前端发送ajax请求，保存新增伤残鉴定机构信息
	  */
	 
	@RequestMapping(value = "/saveOrUpdateAppraisa.do")
	@ResponseBody
	public AjaxResult saveOrUpdateAppraisa(@FormModel(value = "prpdAppraisaVo") PrpdAppraisaVo prpdAppraisaVo
		) {
		String userCode = WebUserUtils.getUserCode();
		AjaxResult ajaxResult = new AjaxResult();
		appraisaService.savaOrUpDate(prpdAppraisaVo, userCode);
		ajaxResult.setData("1");
		ajaxResult.setStatus(200);
	try{
			
		}
		catch(Exception e){
			ajaxResult.setData("2");
		}
		return ajaxResult;
	}
	
	 /* *
	  * 前端发送ajax请求，通过伤残机构代码查找伤残机构名称
	  */
	 
	@RequestMapping(value = "/findAppraisaName.do")
	@ResponseBody
	public AjaxResult findAppraisa(String appraisaCode) {
		AjaxResult ajaxResult = new AjaxResult();
		String appraisaName=appraisaService.findAppraisaName(appraisaCode);
		ajaxResult.setData(appraisaName);
		ajaxResult.setStatus(200);
	try{
			
		}catch(Exception e){
			ajaxResult.setData("2");
		}
		return ajaxResult;
	}
}
