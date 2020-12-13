/******************************************************************************
 * CREATETIME : 2015年11月18日 下午3:21:31
 ******************************************************************************/
package ins.sino.claimcar.regist.web.action;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2015年11月18日
 */
@Controller
@RequestMapping("/policyQuery")
public class PolicyQueryAction {

	private static Logger log = LoggerFactory.getLogger(PolicyQueryAction.class);

	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
	@Autowired
	RegistService registService;

	/**
	 * 进入保单验证List
	 * @return
	 * @throws ParseException
	 * @modified: ☆LiuPing(2015年11月18日 下午4:48:00): <br>
	 */
	@RequestMapping(value = "/checkList.do", method = RequestMethod.GET)
	public ModelAndView checkList(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView();
		PolicyInfoVo policyInfoVo = new PolicyInfoVo();
		policyInfoVo.setDamageTime(new Date());
		mav.addObject("CallId",request.getParameter("CallId"));
		mav.addObject("policyInfoVo",policyInfoVo);
		mav.setViewName("policy/policyQuery/CheckList");
		return mav;
	}

	/**
	 * 进入保单验证List
	 * @return
	 * @throws ParseException
	 * @modified: ☆LiuPing(2015年11月18日 下午4:48:00): <br>
	 */
	@RequestMapping(value = "/policyList.do", method = RequestMethod.GET)
	public ModelAndView policyList(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView();
		PolicyInfoVo policyInfoVo = new PolicyInfoVo();
		policyInfoVo.setDamageTime(new Date());
		mav.addObject("CallId",request.getParameter("CallId"));
		mav.addObject("policyInfoVo",policyInfoVo);
		mav.setViewName("policy/policyQuery/PolicyList");
		return mav;
	}
	
	@RequestMapping(value = "/search.do/", method = RequestMethod.POST)
	@ResponseBody
	@AvoidRepeatableCommit
	public String search(
			@FormModel("policyInfoVo") PolicyInfoVo policyInfoVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception{
		try{
			//输入的保单查询条件，初步根据条件筛查出的保单超过100条则要求输入更加准确的条件
			String propertyName = ResultAmount.registResultAmt;
			if(!policyQueryService.calculateResultCount(policyInfoVo,propertyName)){
				throw new IllegalAccessException("查询结果过大（大于" + ReadConfigUtils.getResultCount(propertyName) + "），请输入更精确的查询条件");
			}
			Page<PolicyInfoVo> page = policyQueryService.findPrpCMainForPage(policyInfoVo,start,length);
			for(PolicyInfoVo vo :page.getResult()){
				vo.setCounts(page.getResult().size());
			}
			String jsonData = ResponseUtils.toDataTableJson(page,"policyNo","licenseNo","comCode","comCode:ComCode","insuredName","brandName",
					"frameNo","engineNo","startDate","endDate","riskType","validFlag","relatedPolicyNo","counts","startDateHour","endDateHour");
			return jsonData;
		}catch(Exception e){
			log.error("保单查询出错",e);
			throw e;
		}
	}

	@RequestMapping(value = "/policySearchs.do/", method = RequestMethod.POST)
	@ResponseBody
	@AvoidRepeatableCommit
	public String policySearchs(
			@FormModel("policyInfoVo") PolicyInfoVo policyInfoVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		//输入的保单查询条件，初步根据条件筛查出的保单超过100条则要求输入更加准确的条件
		String propertyName = ResultAmount.policyResultAmt;
		if(!policyQueryService.calculateResultCount(policyInfoVo,propertyName)){
			throw new IllegalAccessException("查询结果过大（大于" + ReadConfigUtils.getResultCount(propertyName) + "），请输入更精确的查询条件");
		}
		Page<PolicyInfoVo> page = policyQueryService.findPolicyNoForPage(policyInfoVo, start, length);
		//List<PolicyInfoVo> page = policyQueryService.findPrpCMainForPages(policyInfoVo,start,length);
		for(PolicyInfoVo vo :page.getResult()){
			vo.setCounts(page.getResult().size());
		}
		String jsonData = ResponseUtils.toDataTableJson(page,"policyNo","licenseNo","comCode","comCode:ComCode","insuredName","brandName",
				"frameNo","engineNo","startDate","endDate","riskType","validFlag","relatedPolicyNo","counts","startDateHour","endDateHour");
		return jsonData;
	}
	
	/**
	 * 检查关联的保单号
	 * @return
	 */
	@RequestMapping(value = "/relatedList.do", method = RequestMethod.GET)
	public ModelAndView relatedList(PolicyInfoVo policyInfoVo) {

		Page<PolicyInfoVo> page = policyQueryService.findRelatedPolicy(policyInfoVo);

		List<PolicyInfoVo> policyInfoVoList = page.getResult();
		List<PolicyInfoVo> policyInfoVoReSultList = new ArrayList<PolicyInfoVo>();
		//保单车架号不一致不能报案
		for(PolicyInfoVo vo : policyInfoVoList){
			if(policyInfoVo.getComCode().substring(0,4).equals("0002")||vo.getComCode().substring(0,4).equals("0002")){//深圳
				if(policyInfoVo.getComCode().substring(0,4).equals(vo.getComCode().substring(0,4))){
					if(StringUtils.isNotBlank(policyInfoVo.getFrameNo())){
						if(policyInfoVo.getFrameNo().equals(vo.getFrameNo())){
							policyInfoVoReSultList.add(vo);
						}
					}
				}
			}else{
				if(policyInfoVo.getComCode().substring(0,2).equals(vo.getComCode().substring(0,2))){
					if(StringUtils.isNotBlank(policyInfoVo.getFrameNo())){
						if(policyInfoVo.getFrameNo().equals(vo.getFrameNo())){
							policyInfoVoReSultList.add(vo);
						}
					}
				}
			}
		}
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("policyInfoVoList",policyInfoVoReSultList);
		mav.setViewName("policy/policyQuery/RelatedList");
		return mav;
	}
	
	

	@RequestMapping("/relatedSearch.do")
	@ResponseBody
	public AjaxResult relatedSearch(String BIPolicyNo,Date damageTime) throws Exception{
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PolicyInfoVo policyInfoVo = new PolicyInfoVo();
			policyInfoVo.setPolicyNo(BIPolicyNo);
			policyInfoVo.setDamageTime(damageTime);
			policyInfoVo.setCheckFlag("1");
			Page<PolicyInfoVo> page = policyQueryService.findPrpCMainForPage(policyInfoVo,0,10);
			if(page.getResult().size()>0){
				//检查关联的保单号
				page.getResult().get(0).setDamageTime(damageTime);
				Page<PolicyInfoVo> page1 = policyQueryService.findRelatedPolicy(page.getResult().get(0));
				
				if(page1.getResult().size()>0){
					if(page1.getResult().get(0).getRelatedPolicyNo()!=null && "1".equals(page1.getResult().get(0).getValidFlag())){
						//在承保
						if("0002".equals(page.getResult().get(0).getComCode().substring(0,4))||
								"0002".equals(page1.getResult().get(0).getComCode().substring(0,4))){//深圳
							if(page.getResult().get(0).getComCode().substring(0,4).equals(page1.getResult().get(0).getComCode().substring(0,4))){
								//加管控，车架号不同不能关联
								if(page.getResult().get(0).getFrameNo().equals(page1.getResult().get(0).getFrameNo())){
									ajaxResult.setStatusText("0");//同一家公司
								}else{
									ajaxResult.setStatusText("1");//不同家公司
								}
							}else{
								ajaxResult.setStatusText("1");//不同家公司
							}
						}else{//非深圳
							if(page.getResult().get(0).getComCode().substring(0,2).equals(page1.getResult().get(0).getComCode().substring(0,2))){
								//加管控，车架号不同不能关联
								if(page.getResult().get(0).getFrameNo().equals(page1.getResult().get(0).getFrameNo())){
									ajaxResult.setStatusText("0");//同一家公司
								}else{
									ajaxResult.setStatusText("1");//不同家公司
								}
							}else{
								ajaxResult.setStatusText("1");//不同家公司
							}
						}
						
					}else{
						ajaxResult.setStatusText("1");//不在承保
					}
				}else{
					ajaxResult.setStatusText("1");//不在承保
				}
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			return ajaxResult;
		}catch (Exception e){
			log.error("关联保单查询出错",e);
			throw e;
		}
	}


	/**
	 * 查询保单是否正在报案
	 * <pre></pre>
	 * @param plyNoList
	 * @return
	 * @modified:
	 * ☆WLL(2017年8月21日 下午4:30:48): <br>
	 */
	 @RequestMapping(value = "/checkHandlerRegist.ajax")
	 @ResponseBody
	 public AjaxResult checkHandlerRegist(String plyNo){
		 AjaxResult ajaxResult =new AjaxResult();
		List<PrpLWfTaskVo> tasList = new ArrayList<PrpLWfTaskVo>();
		if(plyNo != null){
			tasList = wfTaskQueryService.checkHandlerRegistTask(plyNo);		
			if(tasList != null && tasList.size()>0){
				ajaxResult.setStatusText("Y");
//				ajaxResult.setData(tasList);
			}else{
				ajaxResult.setStatusText("N");
			}
		
		}
		return ajaxResult;
	 }
	 
	 @RequestMapping(value = "/showHandlerRegist.do")
	 @ResponseBody
	 public ModelAndView showHandlerRegist(String plyNo){
		ModelAndView mv =new ModelAndView();
		List<PrpLWfTaskVo> tasList = new ArrayList<PrpLWfTaskVo>();
		if(plyNo != null){
			tasList = wfTaskQueryService.checkHandlerRegistTask(plyNo);
			if(tasList != null && tasList.size()>0){
				mv.addObject("wfTaskVos",tasList);
				mv.addObject("policyNo",plyNo);
			}
		}
		mv.setViewName("policy/policyQuery/ViewRegistEdit");
		
		return mv;
	 }
	 
	@RequestMapping("/queryPolicyIsGB.ajax")
	@ResponseBody
	public AjaxResult queryPolicyIsGB(String[] plyNoArr) {
		String flag = "isNotGB";
		String message = "";
		AjaxResult ajaxResult = new AjaxResult();
		Set<Map<String,String>> sets = policyQueryService.findPrpCMainBySQL(plyNoArr);
		if(sets !=null && !sets.isEmpty()){
			for(Map<String,String> map2:sets){
				if("1,2,3,4".indexOf(map2.get("coinsflag"))!=-1){
					Set<Map<String,String>> sets2 = policyQueryService.findPrpjpayrefrecBySQL(plyNoArr);
					if(sets2 !=null && !sets2.isEmpty()){
						//是联共保，查询是否实收保费
						for(Map<String,String> map3:sets2){
							if("0".equals(map3.get("payrefflag"))){
								if (!message.contains(map3.get("policyno"))) {
									message += map3.get("policyno")+"、";
								}
								flag = "isGB";
							}
						}
					}else {
						//为空默认为未实收保费
						if (!message.contains(map2.get("policyno"))) {
							message += map2.get("policyno") + "、";
						}
						flag = "isGB";
					}
				}
			}
		}
		if(message.length()>0){
			message = "保单"+message.substring(0,message.length()-1)+"尚未实收保费，是否继续报案？";
		}
		if("isGB".equals(flag)){
			ajaxResult.setStatusText("Y");//联共保且未实付
		}else {
			ajaxResult.setStatusText("N");
		}
		ajaxResult.setData(message);
		return ajaxResult;
	}
}