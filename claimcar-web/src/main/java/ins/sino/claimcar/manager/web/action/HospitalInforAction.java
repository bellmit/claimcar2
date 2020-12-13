package ins.sino.claimcar.manager.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.vo.PrpDHospitalVo;
import ins.sino.claimcar.flow.service.HospitalService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manager")
public class HospitalInforAction {
	// 常用格式定义
	private static final String FM_DATE_dd = "#yyyy-MM-dd";
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private CodeTranService codeTranService;
	
	
	@RequestMapping(value = "/hospitalList.do")
	/*
	 * 跳转到医院信息查询页面
	 */
	public String intermediaryList(Model model) {
		return "manager/hospitalList";
	}
	
	@RequestMapping(value="/hospitalFind.do")
	@ResponseBody
	public String hospitalFind(
			Model model,
			@FormModel("prpDHospitalVo") PrpDHospitalVo prpDHospitalVo,//页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,//分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) {//展示条数
		ResultPage<PrpDHospitalVo> page = hospitalService.find(
				prpDHospitalVo,
				start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "hospitalCName",
				"hospitalCode", "hospitalLevel", "contractName","hospitalClass",
				"contractTel","validFlag",
				"contractMobile","id");
		//logger.debug(jsonData);
		return jsonData;
	}
	
	/**
	 * 展示修改医院信息页面
	 * @CreateTime 2016年1月30日 下午3:46:50
	 * @author lichen
	 */
	@RequestMapping(value="/saveOrUpdateHospital.do")
	@ResponseBody
	public ModelAndView ModifyHospitalInforEdit(
			@RequestParam(value="id")long id){
		ModelAndView mv=new ModelAndView();
			PrpDHospitalVo prpDHospitalVo=hospitalService.findHospitalById(id);
			mv.addObject("prpDHospitalVo", prpDHospitalVo);//添加模型到
			System.out.println("3========"+prpDHospitalVo.getHospitalLevel()+prpDHospitalVo.getId());
		mv.setViewName("manager/hospitalEdit");
		return mv;	
	}
	
	
	@RequestMapping(value="/show.do")
	@ResponseBody
	public ModelAndView HospitalInforShow(
			@RequestParam(value="id")long id){
		ModelAndView mv=new ModelAndView();
			PrpDHospitalVo prpDHospitalVo=hospitalService.findHospitalById(id);
			mv.addObject("prpDHospitalVo", prpDHospitalVo);//添加模型到
		mv.setViewName("manager/hospitalShow");
		return mv;	
	}
	
	
	@RequestMapping(value="/save.do")
	@ResponseBody
	public ModelAndView saveEdit(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("manager/hospitalAdd");
		return mv;	
	}
	/**
	 * 医院信息修改保存
	 * @CreateTime 2016年1月30日 下午4:06:09
	 * @author lichen
	 */
	@RequestMapping(value="/updateHospital.do")
	@ResponseBody
	public AjaxResult updateHospitalInform(
			@FormModel(value="prpDHospitalVo")PrpDHospitalVo prpDHospitalVo
			){
		AjaxResult ajaxResult=new AjaxResult();
		try{
			prpDHospitalVo.setBank(StringUtils.trim(prpDHospitalVo.getBank()));
			prpDHospitalVo.setAccounts(StringUtils.trim(prpDHospitalVo.getAccounts()));
			prpDHospitalVo.setAccountName(StringUtils.trim(prpDHospitalVo.getAccountName()));
			hospitalService.updateOrSaveHospital(prpDHospitalVo);
			ajaxResult.setData("1");
			ajaxResult.setStatus(200);
			return ajaxResult;
		}catch(Exception e){
			ajaxResult.setData("2");
			return ajaxResult;
		}
	}
/*	public void updateHospitalInform(
			@FormModel(value="prpDHospitalVo")PrpDHospitalVo prpDHospitalVo
			){
		hospitalService.updateOrSaveHospital(prpDHospitalVo);
	}*/
	//新增
	@RequestMapping(value="/add.do")
	@ResponseBody
	public AjaxResult add(@FormModel(value="prpDHospitalVo")PrpDHospitalVo prpDHospitalVo){
		prpDHospitalVo.getAreaCode();
		AjaxResult ajaxResult=new AjaxResult();
		try{
			prpDHospitalVo.setBank(StringUtils.trim(prpDHospitalVo.getBank()));
			prpDHospitalVo.setAccounts(StringUtils.trim(prpDHospitalVo.getAccounts()));
			prpDHospitalVo.setAccountName(StringUtils.trim(prpDHospitalVo.getAccountName()));
			hospitalService.save(prpDHospitalVo);
			ajaxResult.setData("1");
			ajaxResult.setStatus(200);
			return ajaxResult;
		}catch(Exception e){
			ajaxResult.setData("2");
			return ajaxResult;
		}
		
	}
	
	
	
	/**
	 * 医院状态 激活或者注销
	 * @CreateTime 2016年1月30日 下午4:37:26
	 * @author lichen
	 */
	@RequestMapping(value="/hospitalCancelActive.do")
	@ResponseBody
	public AjaxResult validStatus(long id,String states){
		PrpDHospitalVo prpDHospitalVo=hospitalService.findHospitalById(id);
		AjaxResult ajaxResult=new AjaxResult();
		if(states.equals("1")){
		//prpDHospitalVo.setValidFlag("1");
			prpDHospitalVo.setValidFlag("1");
		}else{
			//prpDHospitalVo.setValidFlag("0");
			prpDHospitalVo.setValidFlag("0");
		}
		try{
			hospitalService.updateOrSaveHospital(prpDHospitalVo);
			ajaxResult.setData("1");
			//return ajaxResult;
		}catch(Exception e){
			ajaxResult.setData("2");
		}
		return ajaxResult;
		
	}
}
