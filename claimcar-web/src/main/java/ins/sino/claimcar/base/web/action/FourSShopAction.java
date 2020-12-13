package ins.sino.claimcar.base.web.action;

import ins.framework.dao.database.support.Page;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.sino.claimcar.manager.vo.PrpLFourSShopInfoVo;
import ins.sino.claimcar.other.service.FourSShopService;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/fourSShop")
public class FourSShopAction {

	@Autowired
	FourSShopService fourSShopService;

	private static Logger logger = LoggerFactory
			.getLogger(FourSShopAction.class);

	@RequestMapping("/fourSShopEdit.do")
	@ResponseBody
	public ModelAndView fourSShopEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String fid = request.getParameter("fourSId");
		String flag = request.getParameter("flag");
		if (flag != null && flag.equals("s")) {
			modelAndView.addObject("flag", flag);
		}
		if (fid != null) {
			Long id = Long.parseLong(fid);
			PrpLFourSShopInfoVo fourSShopInfoVo = fourSShopService
					.findFourSShopByPK(id);
			modelAndView.addObject("prpLFourSShopInfo", fourSShopInfoVo);
		}
		modelAndView.setViewName("base/fourSShop/FourSShopEdit");

		return modelAndView;
	}

	@RequestMapping("/fourSShopList.do")
	@ResponseBody
	public ModelAndView fourSShopList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/fourSShop/FourSShopList");
		return modelAndView;
	}

	@RequestMapping("/saveFourSShopInfo.do")
	@ResponseBody
	public AjaxResult saveFourSShopInfo(
			@FormModel(value = "prpLFourSShopInfoVo") PrpLFourSShopInfoVo fourSShopVo) {
		AjaxResult ajaxResult = new AjaxResult();
		fourSShopService.saveOrUpdatefourSShop(fourSShopVo);
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}

	@RequestMapping("/fourSShopFind.do")
	@ResponseBody
	public String search(
			@FormModel(value = "prpLFourSShopInfoVo") PrpLFourSShopInfoVo fourSShopVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		Page<PrpLFourSShopInfoVo> page = fourSShopService
				.findAllFourSShopByHql(fourSShopVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id",
				"fourSShopName", "startTime", "foursLevel:FourSLevel",
				"areaCode:AreaCode", "factoryName", "pushRepairAddress",
				"sendRepair:YN10");
		logger.debug(jsonData);
		return jsonData;
	}

	@RequestMapping("/fourSShopDelete.do")
	@ResponseBody
	public AjaxResult deleteFourSShopInfo(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		String fid = request.getParameter("fid");
		if (fid != null) {
			Long id = Long.parseLong(fid);
			fourSShopService.deleteFourSShopByPK(id);
			ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		}
		return ar;
	}
}
