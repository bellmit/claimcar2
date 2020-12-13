/******************************************************************************
* CREATETIME : 2015年12月17日 下午3:24:53
* FILE       : ins.sino.claimcar.losscar.web.action.DeflossFittingsInterAction
******************************************************************************/
package ins.sino.claimcar.fitting.web.action;

import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.service.JyViewDataService;
import ins.sino.claimcar.claimjy.vo.JyResHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.SendVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.OutputStream;


/**
 * 调用精友接口，获取精友地址
 * @author ★yangkun
 */
@Controller
@RequestMapping("/defLossFittings")
public class DeflossFittingsInterAction {
	private static Logger logger = LoggerFactory.getLogger(DeflossFittingsInterAction.class);

	
	@Autowired
	LossCarService lossCarService;
    @Autowired
    JyViewDataService jyViewDataService;
	@RequestMapping(value="/queryFittingSys.do")
	public void queryFittingSys(@FormModel("fittingVo") ClaimFittingVo claimFittingVo,
	                                  HttpServletRequest request,
	                                  HttpServletResponse response) throws Exception{
		
		SendVo sendVo = new SendVo();
		sendVo.setServerPort(request.getServerPort()+"");
		sendVo.setServerName(request.getServerName());
		sendVo.setContextPath(request.getContextPath());
		claimFittingVo.setSendVo(sendVo);
		String rightURL = SpringProperties.getProperty("JY_URL");
		claimFittingVo.setJyUrl(rightURL);
		SysUserVo userVo = WebUserUtils.getUser();

		try {
			String url = lossCarService.sendXMLData(claimFittingVo,userVo);
			logger.debug("重定向的配件URL="+url);
			response.sendRedirect(url);
		} catch (Exception e) {
			response.setHeader("content-type", "text/html;charset=UTF-8");
			byte[] dataByteArr = "精友访问失败，请重试\n".getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(dataByteArr);
		}
	}
	
	@RequestMapping(value="/queryDefLoss.do")
	public ModelAndView queryDefLoss(String lossNo,String operateType,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
//		if(lossNo == null ){
//			lossNo = (String)request.getAttribute("lossNo");
//			operateType = (String)request.getAttribute("operateType");
//		}
//		
//		ModelAndView mv = new ModelAndView();
//		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(Long.parseLong(lossNo));
//		List<PrpLDlossCarCompVo> carComps = lossCarMainVo.getPrpLDlossCarComps();
//		List<PrpLDlossCarMaterialVo> carMaterials = lossCarMainVo.getPrpLDlossCarMaterials();
//		
//		if(!"verifyPrice".equals(operateType)){
//			List<PrpLDlossCarRepairVo> carRepairs = new ArrayList<PrpLDlossCarRepairVo>();
//			List<PrpLDlossCarRepairVo> outRepairs = new ArrayList<PrpLDlossCarRepairVo>();
//			
//			for(PrpLDlossCarRepairVo carRepairVo : lossCarMainVo.getPrpLDlossCarRepairs()){
//				if(CodeConstants.RepairFlag.INNERREPAIR.equals(carRepairVo.getRepairFlag())){
//					carRepairs.add(carRepairVo);
//				}else{
//					outRepairs.add(carRepairVo);
//				}
//			}
//			mv.addObject("carRepairs",carRepairs);
//			mv.addObject("outRepairs",outRepairs);
//			
//		}
//
//		mv.addObject("lossCarMainVo",lossCarMainVo);
//		mv.addObject("carComps",carComps);
//		mv.addObject("carMaterials",carMaterials);
		
		if("certa".equals(operateType)){
			mv.setViewName("lossCar/DeflossJingYouReload");
		}else if("verifyPrice".equals(operateType)){
			mv.setViewName("verifyPrice/VerifyPriceJyReload");
		}else {
			mv.setViewName("verifyLoss/VerifyLossJyReload");
		}	
		
		return mv;
	}
	@RequestMapping(value="/queryJyViewData.do")
	public void queryJyViewData(String registNo,String id,
	                                  HttpServletRequest request,
	                                  HttpServletResponse response) throws Exception{
		
		SysUserVo userVo = WebUserUtils.getUser();
		SendVo sendVo = new SendVo();
		ClaimFittingVo claimFittingVo = new ClaimFittingVo();
		sendVo.setServerPort(request.getServerPort()+"");
		sendVo.setServerName(request.getServerName());
		sendVo.setContextPath(request.getContextPath());
		claimFittingVo.setSendVo(sendVo);
		JyResVo jyResVo = jyViewDataService.sendViewDataService(claimFittingVo,registNo, id, userVo);
		JyResHeadVo jyResHeadVo= jyResVo.getHead();
		String url ="";
		if("000".equals(jyResHeadVo.getResponseCode()) && jyResVo.getBody() != null){//成功
			url = jyResVo.getBody().getUrl();
		}else{//失败
			logger.debug("获取url失败"+url);
		}
		logger.debug("重定向的定损查看URL="+url);
		response.sendRedirect(url);
		
	}
}
