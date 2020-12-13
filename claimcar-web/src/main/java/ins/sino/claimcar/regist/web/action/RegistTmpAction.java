package ins.sino.claimcar.regist.web.action;

import freemarker.core.ParseException;
import ins.framework.exception.BusinessException;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCMainVo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/regist")
public class RegistTmpAction {

	@Autowired
	private RegistTmpService registTmpService;

	@RequestMapping(value = "/ReportTmpEdit.do")
	@ResponseBody
	public ModelAndView registTmpEdit(HttpServletRequest request) {
		String iName = request.getParameter("iName");
		String liNo = request.getParameter("liNo");
		String damageTime = request.getParameter("damageTime");
		Date nowDate = new Date();
		
		try {
			// 中文字符编码转换
			iName = new String(iName.getBytes("ISO-8859-1"), "UTF-8");
			liNo = new String(liNo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ModelAndView mv = new ModelAndView();
		mv.addObject("iName", iName);
		mv.addObject("liNo", liNo);
		mv.addObject("nowDate", nowDate);
		mv.addObject("damageTime", damageTime);
		mv.setViewName("regist/registEdit/ReportTmpEdit");
		return mv;
	}

	/**
	 * 保存临时保单报案信息
	 * 
	 * @param session
	 * @param mainVo
	 * @param itemKindVos
	 * @param itemCarVo
	 * @return
	 */
	@RequestMapping(value = "/saveReportTmpEdit.do")
	@ResponseBody
	public AjaxResult saveReportTmpEdit(
			HttpSession session,
			@FormModel(value = "mainVo") PrpLTmpCMainVo mainVo,
			@FormModel(value = "itemKindVos") List<PrpLTmpCItemKindVo> itemKindVos,
			@FormModel(value = "itemCarVo") PrpLTmpCItemCarVo itemCarVo) {

		//判断出险时间是否是2020年综改之后,如果综改后页面选择了商业险，则以1230险种生成临时保单
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.newBussiness2020Flag,null);
		if (configValueVo != null) {
			String onlineDateStr = configValueVo.getConfigValue();
			//判断出险时间是否在综改上线时间之后
			Date onlineDate;
			try {
				onlineDate = DateUtils.strToDate(onlineDateStr, DateUtils.YToSec);
			} catch (java.text.ParseException e) {
				throw new BusinessException("2020年车险综合改革解析配置出险时间报错", false);
			}
			if (onlineDate.before(mainVo.getDamageTime()) && mainVo.getRiskCode().contains("1201")) {
				mainVo.setRiskCode(mainVo.getRiskCode().replace("1201","1230"));
			}
		}

		String registTmpNo = registTmpService.registTmpSave(mainVo,
				itemKindVos, itemCarVo,null);// 接收保存成功的数据的报案号用于跳转后传值
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(registTmpNo);
		return ajaxResult;
	}

	/**
	 * 添加一行险别信息的ajax请求
	 * 
	 * @param kindSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addCItemKind.ajax")
	@ResponseBody
	public ModelAndView addCItemKind(int kindSize) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<PrpLTmpCItemKindVo> prpLTmpCItemKindVos = new ArrayList<PrpLTmpCItemKindVo>();
		PrpLTmpCItemKindVo prpLTmpCItemKindVo = new PrpLTmpCItemKindVo();
		prpLTmpCItemKindVos.add(prpLTmpCItemKindVo);

		mv.addObject("prpLTmpCItemKinds", prpLTmpCItemKindVos);
		mv.addObject("kindSize", kindSize);
		mv.setViewName("regist/registEdit/ReportTmpEdit_CItemKind");
		return mv;
	}

}
