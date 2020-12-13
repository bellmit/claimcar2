package ins.sino.claimcar.manager.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.service.DigtalMapService;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/digtalmap")
public class DigtalMapAction {
	
	@Autowired
	private DigtalMapService digtalMapservice;
	
	private Logger logger = LoggerFactory.getLogger(DigtalMapAction.class);
	
	/*
	 * 跳转到电子地图管理
	 */
	@RequestMapping(value="/digitalmap.do")
	public String digitalmapManager(Model model) {
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		model.addAttribute("isOpen",configValueVo.getConfigValue());
		return "manager/DigitalMap";
	}
	
	@RequestMapping(value="/operateDigtalMap/{isOpen}")
	@ResponseBody
	public AjaxResult operateDigtalMap(@PathVariable(value = "isOpen")int isOpen ){
		 AjaxResult ajaxResult=new AjaxResult();
		 try{
			 String userCode = WebUserUtils.getUserCode();
			 digtalMapservice.operateDigtalMap(userCode,isOpen);
			 ajaxResult.setStatus(HttpStatus.SC_OK);
		 }catch(Exception e){
			 ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			 logger.error("开启电子地图开关失败",e);
		 }
		 return ajaxResult;
	 }
}
