package ins.sino.claimcar.certify.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.flow.service.AMLService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/certifyPrintSearch")
public class CertifyPrintSearchAction {

	@Autowired
	LossCarService lossCarService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CompensateTaskService compensateTaskService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	CodeTranService codeTranService;
	
	@Autowired
    AMLService AMLService;

	@RequestMapping("/lossCarList.do")
	public ModelAndView lossCarList() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/lossCarList");
		return mv;
	}
	
	@RequestMapping("/verifyLossCarList.do")
	public ModelAndView verifyLossCarList() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/verifyLossCarList");
		return mv;
	}
	
	@RequestMapping("/checkTaskList.do")
	public ModelAndView checkTaskList() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/checkTaskList");
		return mv;
	}
	
	@RequestMapping("/vehicleRegistList.do")
	public ModelAndView vehicleRegistList() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/vehicleRegistList");
		return mv;
	}
	
	@RequestMapping("/compensateInfoList.do")
	public ModelAndView compensateInfoList(){
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/compensateInfoList");
		return mv;
	}
	
	@RequestMapping("/prePadPayQueryList.do")
	public ModelAndView prePadPayQueryList() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/prePadPayQueryList");
		return mv;
	}
	
	
	@RequestMapping("/policyInfoList.do")
	public ModelAndView policyInfoList(){
		ModelAndView mv = new ModelAndView();
		Date endDate = new Date();
		String prpUrl = SpringProperties.getProperty("PRPCAR_URL");
		
		Date startDate = DateUtils.addDays(endDate, -15);
		mv.addObject("taskInTimeStart", startDate);
		mv.addObject("taskInTimeEnd", endDate);
		mv.addObject("prpUrl",prpUrl);
		mv.setViewName("certifyprint/policyInfoList");
		return mv;
	}
	
	//机动车辆保险赔款理算书附页
	@RequestMapping("/compensateInfofuyeList.do")
	public ModelAndView compensateInfofuyeList(){
		ModelAndView mv = new ModelAndView();

		mv.setViewName("certifyprint/compensateInfofuyeList");
		return mv;
	}
	
	//机动车赔款通知书/收据
	@RequestMapping("/compensateInfoNoteList.do")
	public ModelAndView compensateInfoNoteList(){
		ModelAndView mv = new ModelAndView();
		
        mv.setViewName("certifyprint/compensateInfoNoteList");
		return mv;
	}
	
   //核损按钮清单打印
	@RequestMapping("/verifyLossCarListButton.do")
	public ModelAndView verifyLossCarListButton(String registNo){
		ModelAndView mv =new ModelAndView();
		List<PrpLDlossCarMainVo> lossCarMainVos=new ArrayList<PrpLDlossCarMainVo>();
		 List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByRegistNo(registNo);
		 if(carMainVos!=null && carMainVos.size()>0){
			 for(PrpLDlossCarMainVo vo:carMainVos){
				 if("1".equals(vo.getUnderWriteFlag())){
					 if("1".equals(vo.getDeflossCarType())){
						 vo.setDeflossCarType("标的车");
					 }else if("3".equals(vo.getDeflossCarType())){
						 vo.setDeflossCarType("三者车");
					 }else{
						 
					 }
					 lossCarMainVos.add(vo);
				 }
			 }
		 }
		 mv.addObject("lossCarMainVos", lossCarMainVos);
		 mv.setViewName("certifyprint/verifyLossCarListButton");
		return mv;
	}
	
	//赔款计算书打印(页面按钮)
	@RequestMapping("/compensateInfoListButton.do")
	public ModelAndView compensateInfoListButton(String registNo){
		ModelAndView mv =new ModelAndView();
		List<PrpLCompensateVo> compensateList=new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compeVos=compensateTaskService.findNotCancellCompensatevosByRegistNo(registNo);
		List<PrpLCompensateVo> compensates = wfTaskHandleService.getPrplComepensate(compeVos);
		if(compensates!=null && compensates.size()>0){
			for(PrpLCompensateVo vo:compensates){
				//正常赔案
				if("N".equals(vo.getCompensateType())){
					compensateList.add(vo);
				}
			}
		}
		mv.addObject("compensateList", compensateList);
		mv.setViewName("certifyprint/compensateInfoListButton");
		return mv;
	}
	
	//赔款收据打印(页面按钮)
	@RequestMapping("/compensateInfoListNoteButton.do")
	public ModelAndView compensateInfoListNoteButton(String registNo){
		ModelAndView mv =new ModelAndView();
		List<PrpLCompensateVo> compensateList=new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compeVos=compensateTaskService.findNotCancellCompensatevosByRegistNo(registNo);
		List<PrpLCompensateVo> compensates = wfTaskHandleService.getPrplComepensate(compeVos);
		if(compensates!=null && compensates.size()>0){
			for(PrpLCompensateVo vo:compensates){
				//正常赔案
				if("N".equals(vo.getCompensateType())){
					compensateList.add(vo);
				}
			}
		}
		
		List<PrpLPaymentVo> paymentList=new ArrayList<PrpLPaymentVo>();
		List<PrpLPaymentVo> paymentLists=new ArrayList<PrpLPaymentVo>();
             if(compensateList!=null && compensateList.size()>0){
					 for(PrpLCompensateVo compensateVo: compensateList){
						 paymentList=compensateVo.getPrpLPayments();
							if(paymentList!=null&& paymentList.size()>0){
								for(PrpLPaymentVo prpLPaymentVo :paymentList){
									if("0".equals(prpLPaymentVo.getOtherFlag())){
										prpLPaymentVo.setOtherFlag("否");
									}else if("1".equals(prpLPaymentVo.getOtherFlag())){
										prpLPaymentVo.setOtherFlag("是");
									}else{
										
									}
									PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(prpLPaymentVo.getPayeeId());
									if(payCustomVo!=null){
									  prpLPaymentVo.setAccountNo(payCustomVo.getAccountNo());
									  SysCodeDictVo sys = codeTranService.findTransCodeDictVo("BankCode",payCustomVo.getBankName());
									  if(sys!=null){
										  prpLPaymentVo.setBankName(sys.getCodeName());
									  }
									  prpLPaymentVo.setClaimNo(compensateVo.getClaimNo());
									  prpLPaymentVo.setCompensateNo(compensateVo.getCompensateNo());
									  prpLPaymentVo.setPolicyNo(compensateVo.getPolicyNo());
									  prpLPaymentVo.setPayeeName(payCustomVo.getPayeeName());
									  paymentLists.add(prpLPaymentVo);
									}
								}
							}
						 
					 }
				 }
				 
				
			
		mv.addObject("paymentLists", paymentLists);
		mv.setViewName("certifyprint/compensateInfoListNoteButton");
		return mv;
	}
	
	//单证打印格式选择
	@RequestMapping("/certifyprintType.do")
	public ModelAndView certifyprintType(Long mainId,String registNo,String compensateNo,String index){
		
		ModelAndView mv =new ModelAndView();
		mv.addObject("index",index);//单证打印的标志位。区别哪个单证打印跳到此来
		mv.addObject("mainId",mainId);
		mv.addObject("registNo",registNo);
		mv.addObject("compensateNo",compensateNo);
		mv.setViewName("certifyprint/certifyprintType");
		return mv;
	}
	
	@RequestMapping(value = "/lossCarListSearch.do")
	@ResponseBody
	public String lossCarListSearch(@RequestParam(value = "registNo") String registNo,// 报案号
									@RequestParam(value = "claimNo") String claimNo,// 立案号
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLDlossCarMainVo> page = lossCarService.findLossCarMainPageByRegistNo(registNo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page,"id","registNo","licenseNo","deflossCarType:DefLossItemType");
		// logger.debug(jsonData);
		return jsonData;
	}
	
	/**
	 * 原始保单及出险前批单打印
	 * @param registNo
	 * @param policyNo
	 * @param taskInTimeStart
	 * @param taskInTimeEnd
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/originalPolicyListSearch.do")
	@ResponseBody
	public String originalPolicyListSearch(@RequestParam(value = "registNo") String registNo,//报案号
			                               @RequestParam(value = "policyNo") String policyNo,//保单号
			                               @RequestParam(value = "taskInTimeStart") Date taskInTimeStart,//开始时间
			                               @RequestParam(value = "taskInTimeEnd") Date taskInTimeEnd,
			                               @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
										   @RequestParam(value = "length", defaultValue = "10") Integer length){
		
		ResultPage<PrpLCMainVo> page= registQueryService.findPrpLCMainPageByRegistNo(registNo, policyNo, taskInTimeStart, taskInTimeEnd, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","policyNo","endorseNo","riskCode","pingendorseNo");
		return jsonData ;
	}
	
	
	
	@RequestMapping(value = "/vehicleRegistListSearch.do")
	@ResponseBody
	public String vehicleRegistListSearch(@RequestParam(value = "registNo") String registNo,
									@RequestParam(value = "policyNo") String policyNo,
									@RequestParam(value = "insuredName") String insuredName,
									@RequestParam(value = "licenseNo") String licenseNo,
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLRegistVo> page = registQueryService.findRegistPageByRegistNo(registNo,policyNo,insuredName,licenseNo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page,searchCallBack(),"registNo","policyNo");
		// logger.debug(jsonData);
		return jsonData;
	}
	
	@RequestMapping(value = "/compensateInfoListSearch.do")
	@ResponseBody//1234
	public String compensateInfoListSearch(@RequestParam(value = "registNo") String registNo,
									@RequestParam(value = "claimNo") String claimNo,
									@RequestParam(value = "compensateNo") String compensateNo,
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLCompensateVo> page = compensateTaskService.findCompensatePage(registNo,claimNo,compensateNo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","compensateNo","policyNo","riskCode","claimNo");
		// logger.debug(jsonData);
		return jsonData;
	}
	//机动车辆保险赔款理算书附页
	@RequestMapping(value = "/compensateInfofuyeListSearch.do")
	@ResponseBody//1234
	public String compensateInfofuyeListSearch(@RequestParam(value = "registNo") String registNo,
									@RequestParam(value = "claimNo") String claimNo,
									@RequestParam(value = "compensateNo") String compensateNo,
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLCompensateVo> page = compensateTaskService.findCompensatePage(registNo,claimNo,compensateNo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","compensateNo","policyNo","riskCode","claimNo");
		// logger.debug(jsonData);
		return jsonData;
	}
	
	//机动车辆通知书/收据
	@RequestMapping(value = "/compensateInfoNoteListSearch.do")
	@ResponseBody//1234
	public String compensateInfoNoteListSearch(@RequestParam(value = "compensateNo") String compensateNo,
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLPaymentVo> page = compensateTaskService.findPrpLPaymentPage(compensateNo, start, length);
	
		 String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","claimNo","policyNo","sumRealPay","otherFlag","payeeName","accountNo","bankName","payeeId");
	
		
		
		return jsonData;
	}
	
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				PrpLRegistVo registVo = (PrpLRegistVo)object;
				
				dataMap.put("insuredName",registVo.getPrpLRegistExt().getInsuredName());
				dataMap.put("licenseNo",registVo.getPrpLRegistExt().getLicenseNo());
				
				return dataMap;
			}
		};
		return callBack;
	}
	//批单打印
	@RequestMapping(value = "/pcertifyprint.do")
	@ResponseBody
	public ModelAndView pcertifyprint(String pingendorseNo,String riskCode){
		ModelAndView mv =new ModelAndView();
		List<String> endorseNoList =new ArrayList<String>();
		String prpUrl = SpringProperties.getProperty("PRPCAR_URL");
		String []a=pingendorseNo.split("_");
		for(int i=0;i<a.length;i++){
			endorseNoList.add(a[i]);
		}
		mv.addObject("riskCode", riskCode);
		mv.addObject("endorseNoList", endorseNoList);
		mv.addObject("prpUrl", prpUrl);
		mv.setViewName("certifyprint/pcertifyprint");
		System.out.print("_______________________________________________________"+riskCode);
		return mv;
	}
	/**
	 * 预付垫付计算书打印查询
	 * <pre></pre>
	 * @param registNo
	 * @param claimNo
	 * @param compensateNo
	 * @param compeType
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆WLL(2017年3月10日 下午2:24:13): <br>
	 */
	@RequestMapping(value = "/prePadPayQueryListSearch.do")
	@ResponseBody
	public String prePadPayQueryListSearch(@RequestParam(value = "registNo") String registNo,
									@RequestParam(value = "claimNo") String claimNo,
									@RequestParam(value = "compensateNo") String compensateNo,
									@RequestParam(value = "compeType") String compeType,// 计算书类型-预付 、垫付
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
									@RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数

		ResultPage<PrpLCompensateVo> page = compensateTaskService.findPreOrPadPayPage(registNo,claimNo,compensateNo,compeType,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","compensateNo","claimNo");
		return jsonData;
	}

	/**
     * 
     * <pre>反洗钱信息</pre>
     * @return
     * @modified:
     * ☆LinYi(2017年7月4日 下午2:55:43): <br>
     */
    @RequestMapping(value = "/AMLInfoList.do")
    @ResponseBody
    public ModelAndView btnIntermediaryList(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("certifyprint/AMLInfoList");
        return mv;
    }
    
    /**
     * 
     * <pre>反洗钱信息打印查询</pre>
     * @param registNo
     * @param claimNo
     * @param compensateNo
     * @param start
     * @param length
     * @return
     * @modified:
     * ☆LinYi(2017年7月5日 下午2:37:33): <br>
     */
    @RequestMapping(value = "/AMLInfoListSearch.do")
    @ResponseBody
    public String AMLInfoListSearch(PrpLWfTaskQueryVo prpLWfTaskQueryVo,
                                              @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
                                              @RequestParam(value = "length", defaultValue = "10") Integer length) {// 展示条数
        ResultPage<PrpLPayCustomVo> page = AMLService.findAMLList(prpLWfTaskQueryVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page,"id","registNo","payeeName","accountNo","claimNo");
        return jsonData;
    }
}
