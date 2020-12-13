package ins.sino.claimcar.carYj.web.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJComService;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJService;
import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrplyjPartoffersVo;
import ins.sino.claimcar.claimcarYJ.vo.SupplyYjVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/yjclaimcar")
public class YjclaimCarAction {
	private static Logger logger = LoggerFactory.getLogger(YjclaimCarAction.class);
	@Autowired
	ClaimcarYJComService claimcarYJComService;
	@Autowired
	ClaimcarYJService claimcarYJService;
	@Autowired
	LossCarService lossCarService;
	@RequestMapping("/showYjComInfo.do")
	public ModelAndView showYjComInfo(String id){
		ModelAndView mv=new ModelAndView();
		PrplyjPartoffersVo prplyjPartoffersVo=claimcarYJComService.findPrplyjPartoffersBypartId(id);
		if(prplyjPartoffersVo!=null && prplyjPartoffersVo.getPrpLyjlosscarComp()!=null){
			prplyjPartoffersVo.setEnquiryId(prplyjPartoffersVo.getPrpLyjlosscarComp().getEnquiryId());
			prplyjPartoffersVo.setEnquiryno(prplyjPartoffersVo.getPrpLyjlosscarComp().getEnquiryno());
		}
		mv.addObject("prplyjPartoffersVo", prplyjPartoffersVo);
		mv.setViewName("claimYj/ShowClaimYjInfo");
		return mv;
	}
	
	@RequestMapping("/supplyInfoEdit.do")
	public ModelAndView supply(Long lossMainId){
		ModelAndView mv=new ModelAndView();
		PrpLDlossCarMainVo losscarMainVo=lossCarService.findLossCarMainById(lossMainId);
		PrpLyjlosscarCompVo compYjVo=claimcarYJComService.findPrpLyjlosscarCompBypartId(losscarMainVo.getId().toString());
		List<PrpLDlossCarCompVo> compVos=losscarMainVo.getPrpLDlossCarComps();
		List<SupplyYjVo> supplyYjVos=new ArrayList<SupplyYjVo>();
		if(compYjVo!=null && compYjVo.getPrplyjPartoffers()!=null && compYjVo.getPrplyjPartoffers().size()>0){
			for(PrplyjPartoffersVo offerVo:compYjVo.getPrplyjPartoffers()){
				for(PrpLDlossCarCompVo compVo:compVos){
					if(offerVo.getPartenquiryId().equals(compVo.getId().toString())){
						SupplyYjVo yjVo=new SupplyYjVo();
						yjVo.setDlossNums(compVo.getQuantity()+"");
						yjVo.setDlossPrice(compVo.getMaterialFee()+"");
						yjVo.setDlossRestFee(compVo.getRestFee()+"");
						yjVo.setOriginalId(compVo.getOriginalId());
						yjVo.setPartcode(compVo.getCompCode());
						yjVo.setPartName(compVo.getCompName());
						yjVo.setQuotationAmount(offerVo.getPriceWithtax());
						yjVo.setRecycleFlag(compVo.getRecycleFlag());
						yjVo.setStatus(offerVo.getSupplyFlag());
						yjVo.setThirdpartenquiryid(offerVo.getPartenquiryId());
						yjVo.setYjPrice(offerVo.getPriceWithtax());
						yjVo.setPriceType(compVo.getPriceType());
						supplyYjVos.add(yjVo);
					}
				}
			}
		}
		String sizeFlag=CodeConstants.YN01.N;
		if(supplyYjVos!=null && supplyYjVos.size()>0){
			 sizeFlag=CodeConstants.YN01.Y;
		}
		mv.addObject("sizeFlag",sizeFlag);
		mv.addObject("lossMainId",lossMainId);
		mv.addObject("supplyYjVos", supplyYjVos);
		mv.setViewName("claimYj/SupplyClaimYjInfoEdit");
		return mv;
		
		
	}
	@RequestMapping("/supply.do")
	@ResponseBody
	public AjaxResult supplyEdit(@FormModel("supplyYjVos") List<SupplyYjVo> supplyYjVos,
			@FormModel("lossMainId") Long lossMainId){
		AjaxResult ajax=new AjaxResult();
		SysUserVo sysUserVo=WebUserUtils.getUser();
		List<SupplyYjVo> SupplyVos=new ArrayList<SupplyYjVo>();
		if(supplyYjVos!=null && supplyYjVos.size()>0){
			for(SupplyYjVo yjVo:supplyYjVos){
				if(yjVo!=null){
					SupplyVos.add(yjVo);
				}
			}
		}
		Map<String,String> map=claimcarYJService.claimcarYJOrder(lossMainId, sysUserVo, SupplyVos);
		ajax.setData(map.get("status"));
		ajax.setStatusText(map.get("errorMsg"));
		ajax.setStatus(HttpStatus.SC_OK);
		return ajax;
	}
	
}
