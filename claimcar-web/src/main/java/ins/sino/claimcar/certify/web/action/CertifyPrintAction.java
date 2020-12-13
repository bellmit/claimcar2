package ins.sino.claimcar.certify.web.action;

import ins.framework.dao.database.DatabaseDao;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.MoneyUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
/*import ins.sino.claimcar.claim.service.ConfigService;*/
import ins.sino.claimcar.claim.vo.PrpDAccidentDeductVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.service.AMLService;
import ins.sino.claimcar.flow.vo.AMLOneCertificateVo;
import ins.sino.claimcar.flow.vo.AMLThreeCertificateVo;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PolicyEndorseInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certifyPrint")
public class CertifyPrintAction {
	
	
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ManagerService managerService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	CompensateTaskService compensateTaskService;
	/*
	 * @Autowired ConfigService configService;
	 */
	@Autowired
	PadPayService padPayService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	AMLService amlService;

	@RequestMapping("/lossCarInfo.doc")
	public String lossCarInfo(Model model,HttpServletRequest req,Long mainId,String registNo,String sign) {
		
		// JRDataSource jrDataSource = new JRBeanCollectionDataSource(Facory.createBeanCollection());
		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
//		 registNo = "4000000201612010000165";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(mainId);
		String BigMoney="";
		double money1=0;
		BigDecimal money=null;	
		if(lossCarMainVo!=null){
			money1=DataUtils.NullToZero(lossCarMainVo.getSumCompFee()).doubleValue()+DataUtils.NullToZero(lossCarMainVo.getSumMatFee()).doubleValue()+DataUtils.NullToZero(lossCarMainVo.getSumRepairFee()).doubleValue()-DataUtils.NullToZero(lossCarMainVo.getSumRemnant()).doubleValue();
			money=new BigDecimal(money1);		
		
		     if(money!=null){
			    if(money.compareTo(BigDecimal.ZERO)==-1){
						double realPay =money.doubleValue()*(-1);
						BigMoney="负"+MoneyUtils.toChinese(realPay,"CNY");	
				}else{
					double realPay = money.doubleValue();
					BigMoney= MoneyUtils.toChinese(realPay,"CNY");	
				}
		    }
		}

		List<PrpLDlossCarCompVo> PrpLDlossCarComps = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarRepairVo> PrpLDlossCarRepairs = lossCarMainVo.getPrpLDlossCarRepairs();
		int size1 = 0,size2 = 0;
		if(PrpLDlossCarComps!=null&&PrpLDlossCarComps.size()>0){
			size1 = PrpLDlossCarComps.size();
		}else{
			PrpLDlossCarComps = new ArrayList<PrpLDlossCarCompVo>();
		}
		if(PrpLDlossCarRepairs!=null&&PrpLDlossCarRepairs.size()>0){
			size2 = PrpLDlossCarRepairs.size();
		}else{
			PrpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepairVo>();
		}

		 if(size1<size2){
			for(int i = 0; i<size2-size1; i++ ){
				PrpLDlossCarCompVo compVo = new PrpLDlossCarCompVo();
				PrpLDlossCarComps.add(compVo);
			}
			lossCarMainVo.setPrpLDlossCarComps(PrpLDlossCarComps);
		}else if(size1>size2){
			for(int j = 0; j<size1-size2; j++ ){
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				PrpLDlossCarRepairs.add(repairVo);
			}
			lossCarMainVo.setPrpLDlossCarRepairs(PrpLDlossCarRepairs);
		}
       
		
		List<PrpLDlossCarMainVo> lossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
      
		lossCarMainVos.add(lossCarMainVo);
		String 	lossCarType="";
		if(lossCarMainVo!=null){
			lossCarType	=lossCarMainVo.getDeflossCarType();
		}
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(lossCarMainVos);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("money1",money1+"");
		model.addAttribute("lossCarType", lossCarType);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		model.addAttribute("prpLCheckVo",checkVo);
		model.addAttribute("prpLRegistVo",registVo);
		model.addAttribute("BigMoney",BigMoney);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/lossCarInfo.jasper");

		return "iReportView";  // 对应jasper-defs.xml中的bean id
	}
	
	@RequestMapping("/verifyLossCarInfo.doc")
	public String verifyLossCarInfo(Model model,HttpServletRequest req,Long mainId,String registNo,String sign) {
		// JRDataSource jrDataSource = new JRBeanCollectionDataSource(Facory.createBeanCollection());
		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
//		 registNo = "4000000201612010000165";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		String IndemnityDuty = codeTranService.findCodeName("IndemnityDuty",checkDutyVo.getIndemnityDuty());
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(mainId);

		List<PrpLDlossCarCompVo> PrpLDlossCarComps = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarRepairVo> PrpLDlossCarRepairs = lossCarMainVo.getPrpLDlossCarRepairs();
		int size1 = 0,size2 = 0;
		if(PrpLDlossCarComps!=null&&PrpLDlossCarComps.size()>0){
			size1 = PrpLDlossCarComps.size();
		}else{
			PrpLDlossCarComps = new ArrayList<PrpLDlossCarCompVo>();
		}
		if(PrpLDlossCarRepairs!=null&&PrpLDlossCarRepairs.size()>0){
			size2 = PrpLDlossCarRepairs.size();
		}else{
			PrpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepairVo>();
		}

		if(size1<size2){
			for(int i = 0; i<size2-size1; i++ ){
				PrpLDlossCarCompVo compVo = new PrpLDlossCarCompVo();
				PrpLDlossCarComps.add(compVo);
			}
			lossCarMainVo.setPrpLDlossCarComps(PrpLDlossCarComps);
		}else if(size1>size2){
			for(int j = 0; j<size1-size2; j++ ){
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				PrpLDlossCarRepairs.add(repairVo);
			}
			lossCarMainVo.setPrpLDlossCarRepairs(PrpLDlossCarRepairs);
		}
        
		//交强险承保公司
		List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyAllInfo(registNo);
		String CIComCode = "";
		for(PrpLCMainVo vo:prpLCMainVos){
			if(Risk.DQZ.equals(vo.getRiskCode())){
				CIComCode = vo.getComCode();
				if(StringUtils.isNotBlank(CIComCode)){
					CIComCode = codeTranService.transCode("ComCodeFull",CIComCode);
				}
			}
		}
		List<PrpLDlossCarMainVo> lossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();

		lossCarMainVos.add(lossCarMainVo);
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(lossCarMainVos);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		model.addAttribute("IndemnityDuty",IndemnityDuty);
		model.addAttribute("prpLCheckVo",checkVo);
		model.addAttribute("CIComCode",CIComCode);
		model.addAttribute("prpLRegistVo",registVo);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/verifyLossCarInfo.jasper");

		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}

	@RequestMapping("/checkTask.doc")
	public String checkTask(Model model,HttpServletRequest req,String registNo,String sign){

		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
//		String registNo = "4000000201611010000198";
    	PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckCarVo checkMainCarVo = null;
		List<PrpLCheckCarVo> checkThirdCarList = new ArrayList<PrpLCheckCarVo>();
		List<PrpLCheckCarVo> checkCarList=null;
		PrpLCheckTaskVo prpLCheckTaskVo=null;
	    if(checkVo!=null){
        	 prpLCheckTaskVo = checkVo.getPrpLCheckTask();
        	 if(prpLCheckTaskVo!=null){
        		 checkCarList = prpLCheckTaskVo.getPrpLCheckCars();
        	 }
		 }
		if(checkCarList!=null && checkCarList.size()>0){
		  for(PrpLCheckCarVo checkCarVo:checkCarList){
			  int serialNo = checkCarVo.getSerialNo();
			   if(serialNo==1){
				  checkMainCarVo = checkCarVo;
			   }else if("1".equals(checkCarVo.getValidFlag())){
				   checkThirdCarList.add(checkCarVo);
			    }
		  }
		}

		List<PrpLCheckTaskVo> checkTaskVos = new ArrayList<PrpLCheckTaskVo>();
        if(checkVo!=null){
		checkTaskVos.add(checkVo.getPrpLCheckTask());
		}
        //将字段给翻译
        if(prpLCheckTaskVo!=null){
        	for(PrpLCheckPersonVo prpLCheckPerson:prpLCheckTaskVo.getPrpLCheckPersons()){
        		if("1".equals(prpLCheckPerson.getPersonSex())){
        			prpLCheckPerson.setPersonSex("男");
        		}else if("2".equals(prpLCheckPerson.getPersonSex())){
        			 prpLCheckPerson.setPersonSex("女");
        		}else if("9".equals(prpLCheckPerson.getPersonSex())){
        			 prpLCheckPerson.setPersonSex("未知");
        		}else{
        			
        		     }
        	   }
        	
        }
        
        int injuredCount1=0;//标的受伤人数
        int injuredCount3=0;//三者受伤人数
        int deathCount1=0;//标的死亡人数
        int deathCount3=0;//三者死亡人数
        if(prpLCheckTaskVo!=null){
        	List<PrpLCheckPersonVo> regs=prpLCheckTaskVo.getPrpLCheckPersons();
        	if(regs!=null && regs.size()>0){
        		for(PrpLCheckPersonVo personLossVo :regs){
        			if((personLossVo.getLossPartyId()==1 && "1".equals(personLossVo.getPersonPayType()))|| (personLossVo.getLossPartyId()==1 && StringUtils.isBlank(personLossVo.getPersonPayType()))){
        				injuredCount1=injuredCount1+1;
        			}else if(personLossVo.getLossPartyId()!=1 && "1".equals(personLossVo.getPersonPayType()) || (personLossVo.getLossPartyId()!=1 && StringUtils.isBlank(personLossVo.getPersonPayType()))){
        				injuredCount3=injuredCount3+1;
        			}else if(personLossVo.getLossPartyId()==1 && "2".equals(personLossVo.getPersonPayType())){
        				deathCount1=deathCount1+1;
        			}else if(personLossVo.getLossPartyId()!=1 && "2".equals(personLossVo.getPersonPayType())){
        				deathCount3=deathCount3+1;
        			}else{
        				
        			}
        		}
        	}
        }
        
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(checkTaskVos);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
	    model.addAttribute("prpLRegistVo",registVo);
		model.addAttribute("injuredCount1",injuredCount1+"");
		model.addAttribute("injuredCount3",injuredCount3+"");
		model.addAttribute("deathCount1",deathCount1+"");
		model.addAttribute("deathCount3",deathCount3+"");
		model.addAttribute("checkMainCarVo",checkMainCarVo);
		model.addAttribute("checkThirdCarList",checkThirdCarList);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/checkTask.jasper");

		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/prpLRegist.doc")
	public String prpLRegist(Model model,HttpServletRequest req,String registNo,String sign) {

		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
//		String registNo = "4000000201611010000198";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String damageCode = registVo.getDamageCode();
		
		if(StringUtils.isNotBlank(damageCode)){
//			codeTranService.findCodeName("DamageCode",damageCode);
			SysCodeDictVo sys = codeTranService.findTransCodeDictVo("DamageCode",damageCode);
			if(sys == null){
				sys = codeTranService.findTransCodeDictVo("DamageCode2",damageCode);
			}
			registVo.setDamageCode(sys.getCodeName());
		}
		String relation = registVo.getReportorRelation();
		if(StringUtils.isNotBlank(relation)){
			String name = codeTranService.findCodeName("InsuredIdentity",relation);
			registVo.setReportorRelation(name);
		}
		String isOnsit = registVo.getPrpLRegistExt().getIsOnSitReport();
		if(RadioValue.RADIO_YES.equals(isOnsit)){
			registVo.getPrpLRegistExt().setIsOnSitReport("是");
		}else{
			registVo.getPrpLRegistExt().setIsOnSitReport("否");
		}
		
		
		
		List<PrpLRegistVo> registVos = new ArrayList<PrpLRegistVo>();
		registVos.add(registVo);
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(registVos);
		
		List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyAllInfo(registNo);
		
		String policyNo="";//商业险保单
		for(PrpLCMainVo vo:prpLCMainVos){
			if(!(Risk.DQZ.equals(vo.getRiskCode()))){
				policyNo=vo.getPolicyNo();
				break;
			}
		}
		
		String damageText1="";//保单出险信息
		String damageText2="";
		String damageText3="";
		String damageText4="";
		int n=0;
		List<PrpLRegistVo> prpLRegistVos=registQueryService.findPrpLRegistByPolicyNo(policyNo);
		//将同一保单号的记录，按出险时间降序排
		
		Collections.sort(prpLRegistVos, new Comparator<PrpLRegistVo>() {
		@Override
		public int compare(PrpLRegistVo o1,PrpLRegistVo o2) {
				return o2.getDamageTime().compareTo(o1.getDamageTime());
			}
		});
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		if(prpLRegistVos!=null && prpLRegistVos.size()>0){
			for(int i=0;i<prpLRegistVos.size();i++){
				if(n==0){
					damageText1="报案号："+prpLRegistVos.get(i).getRegistNo()+"  "+"报案时间："+format.format(prpLRegistVos.get(i).getReportTime());
				}
				if(n==1){
					damageText2="报案号："+prpLRegistVos.get(i).getRegistNo()+"  "+"报案时间："+format.format(prpLRegistVos.get(i).getReportTime());
				}
				if(n==2){
					damageText3="报案号："+prpLRegistVos.get(i).getRegistNo()+"  "+"报案时间："+format.format(prpLRegistVos.get(i).getReportTime());
				}
				if(n==3){
					damageText4="更多出险信息见报案表";
					break;
				}
				n++;
			}
		}
		String textStr1="";//保单批改信息
		String textStr2="";
		String textStr3="";
		String textStr4="";
		int m=0;
		List<PolicyEndorseInfoVo> lists=policyViewService.findPrpPheadAndPrpPmainInfo(policyNo);
		if(lists!=null&&lists.size()>0){
			for(int i=0;i<lists.size();i++){
				
				if(m==0){
					textStr1="批单号："+lists.get(i).getEndorseNo()+" "+"批单日期："+lists.get(i).getEndorDate()+" "+"保额变化量："+lists.get(i).getChgAmount()+" "+"保费变化量："+lists.get(i).getChgPremium();
				}
				if(m==1){
					textStr2="批单号："+lists.get(i).getEndorseNo()+" "+"批单日期："+lists.get(i).getEndorDate()+" "+"保额变化量："+lists.get(i).getChgAmount()+" "+"保费变化量："+lists.get(i).getChgPremium();
				}
				if(m==2){
					textStr3="批单号："+lists.get(i).getEndorseNo()+" "+"批单日期："+lists.get(i).getEndorDate()+" "+"保额变化量："+lists.get(i).getChgAmount()+" "+"保费变化量："+lists.get(i).getChgPremium();
				}
				if(m==3){
					textStr4="其余批改请见批单";
					break;
				}
				m++;
			}
			
		}
		
		String CIPolicyNo = "";
		String CIComCode = "";
		String BIPolicyNo = "";
				
		for(PrpLCMainVo vo:prpLCMainVos){
			if(Risk.DQZ.equals(vo.getRiskCode())){
				CIPolicyNo = vo.getPolicyNo();
				CIComCode = vo.getComCode();
				if(StringUtils.isNotBlank(CIComCode)){
					CIComCode = codeTranService.transCode("ComCodeFull",CIComCode);
				}
			}else{
				BIPolicyNo = vo.getPolicyNo();
			}
		}
		//查该报案号下所有险别
		List<PrpLCItemKindVo> prpLCItemKindvos=policyViewService.findItemKinds(registNo,null);
		      
		    String injuredCount1="";//标的受伤人数
	        String injuredCount3="";//三者受伤人数
	        String deathCount1="";//标的受伤人数
	        String deathCount3="";//三者受伤人数
	        if(registVo!=null){
	        	List<PrpLRegistPersonLossVo> regs=registVo.getPrpLRegistPersonLosses();
	        	if(regs!=null && regs.size()>0){
	        		for(PrpLRegistPersonLossVo personLossVo :regs){
	        			if("1".equals(personLossVo.getLossparty())){
	        				injuredCount1=personLossVo.getInjuredcount()+"";
	        				deathCount1=personLossVo.getDeathcount()+"";
	        			}
	        			if("3".equals(personLossVo.getLossparty())){
	        				injuredCount3=personLossVo.getInjuredcount()+"";
	        				deathCount3=personLossVo.getDeathcount()+"";
	        			}
	        		}
	        	}
	        }
	        
	     //翻译使用性质 和行驶区域
	        if(prpLCMainVos!=null && prpLCMainVos.size()>0){
	        	for(PrpLCItemCarVo prpCItemCar:prpLCMainVos.get(0).getPrpCItemCars()){
	        		if(StringUtils.isNotBlank(prpCItemCar.getUseKindCode())){
	        			SysCodeDictVo sysC = codeTranService.findTransCodeDictVo("UseKind",prpCItemCar.getUseKindCode());//UseNature是字典表的codetype
	        			SysCodeDictVo sysM=codeTranService.findTransCodeDictVo("RunAreaCode",prpCItemCar.getRunAreaCode());
	        			if(sysM!=null){
	        				prpCItemCar.setRunAreaName(sysM.getCodeName());
	        			}
	        			if(sysC!=null){
	        			prpCItemCar.setUseNatureCode(sysC.getCodeName());
	        			}
	        		}
	        	}
	        }
	     //翻译机构代码和机构人员
	       if(prpLCMainVos!=null && prpLCMainVos.size()>0){
	        	PrpLCMainVo prpLCMainVo=prpLCMainVos.get(0);
	        	String ComCode=prpLCMainVo.getComCode();
	        	String userCode=prpLCMainVo.getOperatorCode();
	        	
	        	if(StringUtils.isNotBlank(ComCode)){
					ComCode = codeTranService.transCode("ComCode",ComCode);
					prpLCMainVo.setComCode(ComCode);
					
				}
	        	if(StringUtils.isNotBlank(userCode)){
	        		userCode=codeTranService.transCode("UserCode",userCode);
	        		prpLCMainVo.setOperatorCode(userCode);
	        	}
	        }
	       String promiseStr="";//特别约定
	        String str="";
	        if(prpLCMainVos!=null && prpLCMainVos.size()>0){
	        	PrpLCMainVo prpLCMainVo=null;
	        	for(PrpLCMainVo prpLC:prpLCMainVos){
	        		if(!"1101".equals(prpLC.getRiskCode())){
	        			prpLCMainVo=prpLC;
	        		}
	        	}
	        if(prpLCMainVo!=null){
	         List<PrpLCengageVo> prpCengageVos=prpLCMainVo.getPrpCengages();
	           if(prpCengageVos!=null && prpCengageVos.size()>0){
	    	      for(PrpLCengageVo prpLCengageVo:prpCengageVos){
	    		     if(!"1101".equals(prpLCengageVo.getRiskCode())){
	    			     promiseStr=promiseStr+prpLCengageVo.getClauses();
	    		       }
	    		    }
	    	        if(StringUtils.isNotEmpty(promiseStr)){
	    		    	 promiseStr=promiseStr+"(商业约定);";
	    		     }
	    	      }
	        	}
	        }
	        if(prpLCMainVos!=null && prpLCMainVos.size()>0){
	        	PrpLCMainVo prpLCMainVo=null;
	        	for(PrpLCMainVo prpLC:prpLCMainVos){
	        		if("1101".equals(prpLC.getRiskCode())){
	        			prpLCMainVo=prpLC;
	        		}
	        	}
	        	if(prpLCMainVo!=null){
	        	  List<PrpLCengageVo> prpCengageVos=prpLCMainVo.getPrpCengages();
		              if(prpCengageVos!=null && prpCengageVos.size()>0){
		    	         for(PrpLCengageVo prpLCengageVo:prpCengageVos){
		    		        if("1101".equals(prpLCengageVo.getRiskCode())){
		    		    	 str=str+prpLCengageVo.getClauses();
		    		        }
		    		      }
		    	           if(StringUtils.isNotEmpty(str)){
		    	    	  str=str+"(交强约定);";
		    	           }
		    	        }
		        	 }
	        }
	      if(StringUtils.isNotEmpty(promiseStr)&&StringUtils.isNotEmpty(str)){
	    	  promiseStr=promiseStr+str;
	          }
	       
	       String postNo ="";//邮政编码
	       if(prpLCMainVos!=null && prpLCMainVos.size()>0){
	    	   PrpLCMainVo prpLCMainVo=prpLCMainVos.get(0);
	    	   if(prpLCMainVo!=null){
	    		   List<PrpLCInsuredVo>PrpLCInsuredVos=prpLCMainVo.getPrpCInsureds();
	    		   if(PrpLCInsuredVos!=null && PrpLCInsuredVos.size()>0){
	    			   PrpLCInsuredVo prpLCInsuredVo = PrpLCInsuredVos.get(0);
	    			   postNo=prpLCInsuredVo.getPostCode();
	    		   }
	    	   }
	       }
	       //如果有商业就取商业信息，没有商业就取交强信息
	       PrpLCMainVo prpLCMain1Vo=new PrpLCMainVo();
	       if(prpLCMainVos!=null&& prpLCMainVos.size()>0){
	    	   for(PrpLCMainVo prplc:prpLCMainVos){
	    		   if(!"1101".equals(prplc.getRiskCode())){
	    			   prpLCMain1Vo=prplc;
	    			   break;
	    		   }
	    		 }
	    	   if(StringUtils.isBlank(prpLCMain1Vo.getRegistNo())){
	    	      for(PrpLCMainVo prplc:prpLCMainVos){
	    		     if("1101".equals(prplc.getRiskCode())){
	    			   prpLCMain1Vo=prplc;
	    			   break;
	    		     }
	    		   }
	    	   }
	       }
	       
	       if(prpLCMain1Vo!=null){
	    	   List<PrpLCItemCarVo> listcars= prpLCMain1Vo.getPrpCItemCars();
	    	   
	    	   if(listcars==null || listcars.size()==0){
	    		   PrpLCItemCarVo prpLCItemCarVo =new PrpLCItemCarVo();
	    		          listcars.add(prpLCItemCarVo);
	    	   }else{
	    		//翻译使用性质
	    		String  NatureCode= listcars.get(0).getUseKindCode();
	    		String NatureName=codeTranService.transCode("UseKind",NatureCode);
	    	    listcars.get(0).setUseNatureCode(NatureName);
	    		
	    	   }
	       } 
	       
	       if(prpLCMain1Vo==null){
	    	   List<PrpLCItemCarVo> listcarss=new ArrayList<PrpLCItemCarVo>();
	    	   PrpLCItemCarVo prpLCItemCarVo =new PrpLCItemCarVo();
	    	   listcarss.add(prpLCItemCarVo);
	    	   prpLCMain1Vo.setPrpCItemCars(listcarss);
	       }
	       
	    
	    model.addAttribute("postNo",postNo);
	    model.addAttribute("damageText1",damageText1);
	    model.addAttribute("damageText2",damageText2);
	    model.addAttribute("damageText3",damageText3);
	    model.addAttribute("damageText4",damageText4);
	    model.addAttribute("textStr1",textStr1);  
	    model.addAttribute("textStr2",textStr2); 
	    model.addAttribute("textStr3",textStr3); 
	    model.addAttribute("textStr4",textStr4); 
	    model.addAttribute("promiseStr",promiseStr);   
	    model.addAttribute("prpLCItemKindvos",prpLCItemKindvos);  
	    model.addAttribute("injuredCount1",injuredCount1);
	    model.addAttribute("injuredCount3",injuredCount3);
	    model.addAttribute("deathCount1",deathCount1);
	    model.addAttribute("deathCount3",deathCount3);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		model.addAttribute("prpLCMainVos",prpLCMainVos);
		model.addAttribute("CIPolicyNo",CIPolicyNo);
		model.addAttribute("CIComName",CIComCode);
		model.addAttribute("BIPolicyNo",BIPolicyNo);
		model.addAttribute("prpLCMainVo",prpLCMain1Vo);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		 
		model.addAttribute("url", "/WEB-INF/jasper/prpLRegist.jasper");

		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}
	
	@RequestMapping("/compensateInfo.doc")
	public String compensateInfo(Model model,HttpServletRequest req,String registNo,String compensateNo,String sign) {
    
		// JRDataSource jrDataSource = new JRBeanCollectionDataSource(Facory.createBeanCollection());
		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
//		 registNo = "4000000201612010000165";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String damageCode = registVo.getDamageCode();
		String damageAreaCode =registVo.getDamageAreaCode();
		if(StringUtils.isNotBlank(damageAreaCode)){
			registVo.setDamageAreaCode(codeTranService.transCode("AreaCode",damageAreaCode));
		}
		if(StringUtils.isNotBlank(damageCode)){
			SysCodeDictVo sys = codeTranService.findTransCodeDictVo("DamageCode",damageCode);
			if(sys == null){
				sys = codeTranService.findTransCodeDictVo("DamageCode2",damageCode);
			}
			registVo.setDamageCode(sys.getCodeName());
		}
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
		String sumpaidAmt="";//将本次实付赔款金额大写
		String sumAmt="";//将赔款合计大写
		if(compensateVo!=null){
			if(compensateVo.getSumPaidAmt()!=null){
			   if(compensateVo.getSumPaidAmt().compareTo(BigDecimal.ZERO)==-1){
				double realPay =compensateVo.getSumPaidAmt().doubleValue()*(-1);
				sumpaidAmt="负"+MoneyUtils.toChinese(realPay,"CNY");	
			   }else{
				double realPay = compensateVo.getSumPaidAmt().doubleValue();
				sumpaidAmt= MoneyUtils.toChinese(realPay,"CNY");	
			   }
			
			}
			if(compensateVo.getSumAmt()!=null){
				if(compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)==-1){
					double realPay =compensateVo.getSumAmt().doubleValue()*(-1);
					sumAmt="负"+MoneyUtils.toChinese(realPay,"CNY");	
				   }else{
					double realPay = compensateVo.getSumAmt().doubleValue();
					sumAmt= MoneyUtils.toChinese(realPay,"CNY");	
				   }
			}
			//翻译核陪人
			String userCode=compensateVo.getUnderwriteUser();
			userCode=codeTranService.transCode("UserCode",userCode);
			compensateVo.setUnderwriteUser(userCode);
			//翻译理算员
			String createUser =compensateVo.getCreateUser();
			if(StringUtils.isNotBlank(createUser)){
				createUser=codeTranService.transCode("UserCode",createUser);
				compensateVo.setCreateUser(createUser);
			}
		}
		//获取免赔率
		PrpDAccidentDeductVo accidentDeductVo=new PrpDAccidentDeductVo();
		if(compensateVo!=null){
			PrpLCItemCarVo itemCarVo = policyViewService.findItemCarByRegistNoAndPolicyNo(compensateVo.getRegistNo(),compensateVo.getPolicyNo());
			if(itemCarVo!=null){
				accidentDeductVo=compensateTaskService.finPrpDAccidentDeductVo(compensateVo.getRiskCode(),"A",itemCarVo.getClauseType(),compensateVo.getIndemnityDuty());
			}
		}
		//为了得到绝对免费额
		PrpLCItemKindVo prpLCItemKindVo =new PrpLCItemKindVo();
		if(compensateVo!=null){
		 prpLCItemKindVo=policyViewService.findItemKindByKindCode(compensateVo.getRegistNo(),"A");
		 }
		//通过报案号和保单号去查抄单表
		PrpLCMainVo prpLCMainVo=null;
		if(compensateVo!=null){
			prpLCMainVo=policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,compensateVo.getPolicyNo());
		}
	
		compensateVo.setHandler1Code(codeTranService.findCodeName("UserCode",compensateVo.getHandler1Code()));
		
		if(compensateVo !=null){
			if(compensateVo.getPrpLPayments().size()==0){
				PrpLPaymentVo prpLPayment=new PrpLPaymentVo();
				compensateVo.getPrpLPayments().add(prpLPayment);
			}
		}
		PrpLPaymentVo prpLPaymentVo=new PrpLPaymentVo();
		 PrpLPayCustomVo payCustomVo = null;
		if(compensateVo !=null){
			if(compensateVo.getPrpLPayments()!=null && compensateVo.getPrpLPayments().size()>0){
				prpLPaymentVo=compensateVo.getPrpLPayments().get(0);
				if(prpLPaymentVo!=null){
					payCustomVo = managerService.findPayCustomVoById(prpLPaymentVo.getPayeeId());
					prpLPaymentVo.setAccountNo(payCustomVo.getAccountNo());
					prpLPaymentVo.setPayeeName(payCustomVo.getPayeeName());
					if("1".equals(prpLPaymentVo.getOtherFlag())){
					    prpLPaymentVo.setOtherFlag("是");
					    if(StringUtils.isNotBlank(prpLPaymentVo.getOtherCause())){
					        String otherCause = CodeTranUtil.transCode("OtherCase", prpLPaymentVo.getOtherCause());
					        prpLPaymentVo.setOtherCause(otherCause);
                        }
					}else {
					    prpLPaymentVo.setOtherFlag("否");
                    }
				}		
			}
		}
		List<PrpLCompensateVo> compensateVos = new ArrayList<PrpLCompensateVo>();
		compensateVos.add(compensateVo);
		
		Map<String,String> paymentFeeMap = new HashMap<String,String>();
		
		for(PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()){
			if(paymentFeeMap.containsKey(chargeVo.getChargeCode())){
				BigDecimal sumFeeAmt = new BigDecimal(paymentFeeMap.get(chargeVo.getChargeCode())).add(chargeVo.getFeeAmt());
				paymentFeeMap.put(chargeVo.getChargeCode(),sumFeeAmt.toString());
			}else{
				paymentFeeMap.put(chargeVo.getChargeCode(),chargeVo.getFeeAmt().toString());
			}
		}
		
		//赔款计算书号
		String CICompensateNo = "";
		String BICompensateNo = "";
		List<PrpLCompensateVo> compeVoList = compensateTaskService.queryCompensate(registNo,"N");
		if(compensateVo!=null){
			if(Risk.DQZ.equals(compensateVo.getRiskCode())){//交强赔款计算书号
				CICompensateNo=compensateNo;
				Date tem=null;
				
				if(compeVoList != null && !compeVoList.isEmpty()){
					for(PrpLCompensateVo compeVo : compeVoList){
						if(!Risk.DQZ.equals(compeVo.getRiskCode())){//商业赔款计算书号
							int i=0;
							if(i==0){
							   tem=compeVo.getCreateTime();
							   BICompensateNo=compeVo.getCompensateNo();
							   i++;
							}else{
								if(tem.getTime()>=compeVo.getCreateTime().getTime()){
									BICompensateNo=compeVo.getCompensateNo();
								}
							}
							
							
						}
					}
				}
			}else{
				BICompensateNo=compensateNo;//商业赔款计算书号
				Date tem=null;
				if(compeVoList != null && !compeVoList.isEmpty()){
					for(PrpLCompensateVo compeVo : compeVoList){
						if(Risk.DQZ.equals(compeVo.getRiskCode())){//交强赔款计算书号
							int i=0;
							if(i==0){
							   tem=compeVo.getCreateTime();
							   CICompensateNo=compeVo.getCompensateNo();
							   i++;
							  }else{
								if(tem.getTime()>=compeVo.getCreateTime().getTime()){
									CICompensateNo=compeVo.getCompensateNo();
								}
							}
							
							
						}
					}
				}
			}
			
		}
		
		
		model.addAttribute("CICompensateNo",CICompensateNo);
		model.addAttribute("BICompensateNo",BICompensateNo);
		
		//交强险承保公司
		List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo vo : prpLCMainVos){
			if(Risk.DQZ.equals(vo.getRiskCode())){
				String CIComCode = vo.getComCode();
				if(StringUtils.isNotBlank(CIComCode)){
					compensateVo.setMakeCom(codeTranService.transCode("ComCodeFull",CIComCode));
				}
			}
		}
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(compensateVos);
		
		model.addAttribute("PrpLCItemKindVo",prpLCItemKindVo);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("accidentDeductVo",accidentDeductVo);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		model.addAttribute("prpLCheckVo",checkVo);
		model.addAttribute("prpLRegistVo",registVo);
		model.addAttribute("prpLCMainVo",prpLCMainVo);
		model.addAttribute("prpLCompensateVo",compensateVo);
		model.addAttribute("paymentFeeMap",paymentFeeMap);
		model.addAttribute("prpLPaymentVo",prpLPaymentVo);
        model.addAttribute("sumpaidAmt",sumpaidAmt);
		model.addAttribute("sumAmt",sumAmt);
		String duty = codeTranService.findCodeName("IndemnityDuty",checkDutyVo.getIndemnityDuty());
		checkDutyVo.setIndemnityDuty(duty);
		model.addAttribute("prpLCheckDutyVo",checkDutyVo);
		//已预付次数
		int CI = 0;//交强预付次数
		int BI = 0;//商业预付次数
		List<PrpLCompensateVo> ycompeVoList = compensateTaskService.queryCompensate(registNo,"Y");
		if(ycompeVoList != null && !ycompeVoList.isEmpty()){
			for(PrpLCompensateVo compeVo : ycompeVoList){
				if("1".equals(compeVo.getUnderwriteFlag())&& Risk.DQZ.equals(compeVo.getRiskCode())){
					CI++;
				}
				if("1".equals(compeVo.getUnderwriteFlag()) && !(Risk.DQZ.equals(compeVo.getRiskCode()))){
					BI++;
				}
			}
		}
		//已预付赔款

		Double CIsumprepay = 0D;//交强险预付赔款
		Double BIsumprepay = 0D;//商业险预付赔款
		List<PrpLCompensateVo> NcompeVoList = compensateTaskService.queryCompensate(registNo,"N");
		if(NcompeVoList != null && !NcompeVoList.isEmpty()){
			for(PrpLCompensateVo compeVo : NcompeVoList){
				if("1".equals(compeVo.getUnderwriteFlag())&& Risk.DQZ.equals(compeVo.getRiskCode())){
					CIsumprepay += DataUtils.NullToZero(compeVo.getSumPreAmt()).doubleValue();

				}
				if("1".equals(compeVo.getUnderwriteFlag())&& !(Risk.DQZ.equals(compeVo.getRiskCode()))){
					BIsumprepay += DataUtils.NullToZero(compeVo.getSumPreAmt()).doubleValue();

				}
			}
		}
		model.addAttribute("CIsumprepay",CIsumprepay+"");
		model.addAttribute("BIsumprepay",BIsumprepay+"");
		model.addAttribute("CIprePayTimes",CI+"");
		model.addAttribute("BIprePayTimes",BI+"");
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/compensateInfo.jasper");

		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}
	
	//赔款理算书附页
	@RequestMapping("/compensateInfofuye.doc")
	public String compensateInfofuye(Model model,HttpServletRequest req,String registNo,String compensateNo,String sign) {
	
		String rootPath =req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
	    PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
		List<PrpLCompensateVo> compensateVos=new ArrayList<PrpLCompensateVo>();
		compensateVos.add(compensateVo);
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(compensateVos);
		
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/compensateInfofuye.jasper");
		
		return "iReportView"; // 对应jasper-defs.xml中的bean id

	}
     
	//机动车赔款通知书/收据
	//mainId是PrpLPayment的payeeId字段
	@RequestMapping("/compensateInfoNote.doc")
	public String compensateInfoNote(Model model,HttpServletRequest req,String registNo,String compensateNo,String sign,Long mainId){
		
		String rootPath =req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
		PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
		String LiscNo="";//标的车车牌号
		
		List<PrpLLossItemVo> prpLLossItemVos=new ArrayList<PrpLLossItemVo>();
		PrpLPaymentVo prpLPaymentVo=new PrpLPaymentVo();
		PrpLRegistVo prpLregistVo=new PrpLRegistVo();
		String damageCode="";
		Date damageTime =null;
		if(compensateVo!=null){
			prpLLossItemVos=compensateVo.getPrpLLossItems();
			if(prpLLossItemVos!=null && prpLLossItemVos.size()>0){
				for(PrpLLossItemVo prpLLossItemVo:prpLLossItemVos){
				  if("1".equals(prpLLossItemVo.getItemId()))
				     LiscNo=prpLLossItemVo.getItemName();
				   }
				}
		 prpLregistVo =registQueryService.findByRegistNo(compensateVo.getRegistNo());
		 if( prpLregistVo!=null){
			 damageTime=prpLregistVo.getDamageTime();
			 damageCode=prpLregistVo.getDamageCode();
			 if(StringUtils.isNotBlank(damageCode)){
			 SysCodeDictVo sys = codeTranService.findTransCodeDictVo("DamageCode",damageCode);
			 damageCode=sys.getCodeName();
			 }
		 }
		}
		String riskName="";
		String comCode="";//分公司名称
		if(compensateVo!=null){
			
			if("1101".equals(compensateVo.getRiskCode())){
				riskName="机动车交通事故责任强制保";
			  comCode=	policyViewService.findPolicyComCode(compensateVo.getRegistNo(), "11");
			  }else{
				comCode=policyViewService.findPolicyComCode(compensateVo.getRegistNo(), "12");
				riskName="机动车综合商业保";
			  }
		}
		if(StringUtils.isNotEmpty(comCode)){
			if("00".equals(comCode.substring(0,2))){
				comCode=comCode.substring(0, 4)+"0000";
			}else{
				comCode=comCode.substring(0,2)+"000000";
			}
		 comCode = codeTranService.findCodeName("ComCodeFull",comCode);
		 
		 //如果同一计算书号下，有两个收款人，则根据payeeId区分
		 List<PrpLPaymentVo> PrpLPayment1s=compensateVo.getPrpLPayments();
		 List<PrpLPaymentVo> PrpLPayments= new ArrayList<PrpLPaymentVo>();
		 if(PrpLPayment1s!=null && PrpLPayment1s.size()>0){
			 for(PrpLPaymentVo prpLPayment:PrpLPayment1s){
				 if(mainId.equals(prpLPayment.getPayeeId())){
					 PrpLPayments.add(prpLPayment);
				 }
			 }
		 }
		 
		 if(PrpLPayments!=null && PrpLPayments.size()>0){
			 prpLPaymentVo=PrpLPayments.get(0);
		   }
		 PrpLPayCustomVo payCustomVo = new  PrpLPayCustomVo();
		 if(prpLPaymentVo!=null){
		     payCustomVo = managerService.findPayCustomVoById(prpLPaymentVo.getPayeeId());
		 
		 }
			if(payCustomVo!=null){
			  prpLPaymentVo.setAccountNo(payCustomVo.getAccountNo());
			  //SysCodeDictVo sys = codeTranService.findTransCodeDictVo("BankCode",payCustomVo.getBankName());
			 	  prpLPaymentVo.setBankName(payCustomVo.getBankOutlets());
			  
			  prpLPaymentVo.setClaimNo(compensateVo.getClaimNo());
			  prpLPaymentVo.setCompensateNo(compensateVo.getCompensateNo());
			  prpLPaymentVo.setPolicyNo(compensateVo.getPolicyNo());
			  prpLPaymentVo.setPayeeName(payCustomVo.getPayeeName());
		}
		}
			List<PrpLPaymentVo>	prpLPaymentVos=new ArrayList<PrpLPaymentVo>();
			prpLPaymentVos.add(prpLPaymentVo);
			String bigMoney="";
			String sumRealpay="";//赔款金额
			if(prpLPaymentVo!=null){
				if(prpLPaymentVo.getSumRealPay()!=null){
					//赔款金额格式化
					sumRealpay=new DecimalFormat("#,##0.00").format(prpLPaymentVo.getSumRealPay().doubleValue());
					if(prpLPaymentVo.getSumRealPay().compareTo(BigDecimal.ZERO)==-1){
						double realPay = prpLPaymentVo.getSumRealPay().doubleValue()*(-1);
						bigMoney="负"+MoneyUtils.toChinese(realPay,"CNY");	
					}else{
						double realPay = prpLPaymentVo.getSumRealPay().doubleValue();
						bigMoney= MoneyUtils.toChinese(realPay,"CNY");	
					}
				}
				
			}
			//将出险时间的年月日拆开
			String year="";
			String month="";
			String day="";
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		    String timeStr = format.format(damageTime);
		    if(!StringUtils.isEmpty(timeStr)){
		    	String[] str = timeStr.split("-");
		    	if(str != null){
		    		year = str[0];
		    		month = str[1];
		    		day = str[2];
		    	}
		    }
			//将当前时间的年月日拆开
		    String nowyear="";
			String nowmonth="";
			String nowday="";
		    Date nowDate=new Date();
		    DateFormat nowformat=new SimpleDateFormat("yyyy-MM-dd"); 
		    String nowtimeStr = nowformat.format(nowDate);
		    if(!StringUtils.isEmpty(nowtimeStr)){
		    	String[] nowstr = nowtimeStr.split("-");
		    	if(nowstr != null){
		    		nowyear = nowstr[0];
		    		nowmonth = nowstr[1];
		    		nowday = nowstr[2];
		    	}
		    }
			
      JRDataSource jrDataSource = new JRBeanCollectionDataSource(prpLPaymentVos);
      
     
      
      
        model.addAttribute("LiscNo",LiscNo);
        model.addAttribute("sumRealpay",sumRealpay);
        model.addAttribute("nowyear",nowyear);
        model.addAttribute("nowmonth",nowmonth);
        model.addAttribute("nowday",nowday); 
        model.addAttribute("year",year);
        model.addAttribute("month",month);
        model.addAttribute("day",day); 
        model.addAttribute("BigMoney",bigMoney);
        model.addAttribute("comName",comCode);
        model.addAttribute("riskName",riskName);
        model.addAttribute("damageName",damageCode);
        model.addAttribute("damageTime",damageTime);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/compensateFee.jasper");
		
		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}
	
	@RequestMapping("/prePadPayView.doc")
	public String prePadPayView(Model model,HttpServletRequest req,String registNo,String compensateNo,String sign) {
		
		String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
		
		PrpLCompensateVo compVo = new PrpLCompensateVo();// 预付计算书
		PrpLPadPayMainVo padPayMainVo = null; // 垫付计算书
		if("Y".equals(compensateNo.substring(0,1))){
			compVo = compensateTaskService.findCompByPK(compensateNo);
		}else{
			padPayMainVo = padPayService.findPadPayMainByComp(registNo,compensateNo);
			if(padPayMainVo!=null){
				compVo.setRegistNo(padPayMainVo.getRegistNo());
				compVo.setClaimNo(padPayMainVo.getClaimNo());
				compVo.setPolicyNo(padPayMainVo.getPolicyNo());
				compVo.setCompensateNo(padPayMainVo.getCompensateNo());
				compVo.setCompensateType("D");
			}
		}
		
		List<PrpLCompensateVo> compVoList = new ArrayList<PrpLCompensateVo>();
		compVoList.add(compVo);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo cMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,compVo.getPolicyNo());
		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateStringBegin = formatter.format(cMainVo.getStartDate());
//		String dateStringEnd = formatter.format(cMainVo.getEndDate());
		
		String insuredName = registVo.getPrpLRegistExt().getInsuredName(); // 被保险人
//		String insuredDate = "自"+dateStringBegin+"时起至"+dateStringEnd+"时止";// 保险期限
		Date damageTime = registVo.getDamageTime(); // 出险日期 	
		String damageReason = CodeTranUtil.transCode("DamageCode",registVo.getDamageCode()); // 出险原因
		String rescueReport = compVo.getRescueReport(); // 预支付原因
		if(padPayMainVo!=null){
			rescueReport = padPayMainVo.getRescueReport(); // 预支付原因
		}
		
		BigDecimal claimLoss = BigDecimal.ZERO; // 估计赔款
		BigDecimal thirdClaimLoss = BigDecimal.ZERO; // TODO 三者险赔款  如何计算
		
		List<PrpLPrePayVo> lossList = new ArrayList<PrpLPrePayVo>(); // 赔款List
		List<PrpLPrePayVo> chargeList = new ArrayList<PrpLPrePayVo>();// 费用List
		if("Y".equals(compVo.getCompensateType())){
			// 预付
			List<PrpLPrePayVo> PrpLPrePayVoList = compensateTaskService.queryPrePay(compensateNo);
			for(PrpLPrePayVo prePay:PrpLPrePayVoList){
				if("P".equals(prePay.getFeeType())){
					if(CodeConstants.KINDCODE.KINDCODE_B.equals(prePay.getKindCode())
							||CodeConstants.KINDCODE.KINDCODE_BZ.equals(prePay.getKindCode())){
						thirdClaimLoss = thirdClaimLoss.add(prePay.getPayAmt());
					}
					claimLoss = claimLoss.add(prePay.getPayAmt());
					PrpLPayCustomVo payCus = managerService.findPayCustomVoById(prePay.getPayeeId());
					prePay.setPayeeName(payCus.getPayeeName());
					prePay.setAccountNo(payCus.getAccountNo());
					prePay.setBankName(CodeTranUtil.transCode("BankCode",payCus.getBankName()));
					lossList.add(prePay);
				}else{
					prePay.setChargeName(CodeTranUtil.transCode("ChargeCode",prePay.getChargeCode()));
					PrpLPayCustomVo payCus = managerService.findPayCustomVoById(prePay.getPayeeId());
					prePay.setPayeeName(payCus.getPayeeName());
					prePay.setAccountNo(payCus.getAccountNo());
					prePay.setBankName(CodeTranUtil.transCode("BankCode",payCus.getBankName()));
					chargeList.add(prePay);
				}
			}
		}else{
			// 垫付只有赔款
			for(PrpLPadPayPersonVo padPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
				claimLoss = claimLoss.add(padPayPersonVo.getCostSum());
				thirdClaimLoss = thirdClaimLoss.add(padPayPersonVo.getCostSum());//垫付只有交强险
				PrpLPrePayVo prePayForPad = new PrpLPrePayVo();
				if("0".equals(padPayPersonVo.getLicenseNo())){
					prePayForPad.setLossName("地面/路人");
				}else{
					prePayForPad.setLossName(padPayPersonVo.getLicenseNo());
				}
				prePayForPad.setPayAmt(padPayPersonVo.getCostSum());
				PrpLPayCustomVo payCus = managerService.findPayCustomVoById(padPayPersonVo.getPayeeId());
				prePayForPad.setPayeeName(payCus.getPayeeName());
				prePayForPad.setAccountNo(payCus.getAccountNo());
				prePayForPad.setBankName(CodeTranUtil.transCode("BankCode",payCus.getBankName()));
				lossList.add(prePayForPad);
			}
		}
        if(lossList.size()==0){
        	lossList = null;
        }
        if(chargeList.size()==0){
        	chargeList = null;
        }
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(compVoList);
		model.addAttribute("jrMainDataSource",jrDataSource);
		model.addAttribute("SUBREPORT_DIR",rootPath);
		model.addAttribute("insuredName",insuredName);
//		model.addAttribute("insuredDate",insuredDate);
		model.addAttribute("insuredDateBegin",cMainVo.getStartDate());
		model.addAttribute("insuredDateEnd",cMainVo.getEndDate());
		model.addAttribute("damageTime",damageTime);
		model.addAttribute("damageReason",damageReason);
		model.addAttribute("rescueReport",rescueReport);
		model.addAttribute("claimLoss",claimLoss);
		model.addAttribute("thirdClaimLoss",thirdClaimLoss);
		model.addAttribute("lossList",lossList);
		model.addAttribute("chargeList",chargeList);
		
		if("1".equals(sign)){
			model.addAttribute("format", "doc"); // 报表格式 
		}else{
			model.addAttribute("format", "pdf"); // 报表格式 
		}
		model.addAttribute("url", "/WEB-INF/jasper/prePayView.jasper");

		return "iReportView"; // 对应jasper-defs.xml中的bean id
	}
	
	/**
	 * 
	 * <pre>反洗钱信息打印</pre>
	 * @param model
	 * @param req
	 * @param mainId
	 * @param registNo
	 * @param sign
	 * @return
	 * @modified:
	 * ☆LinYi(2017年7月7日 上午11:18:08): <br>
	 */
	@RequestMapping("/AMLInfo.doc")
    public String AMLInfo(Model model,HttpServletRequest req,Long mainId,String registNo,String claimNo,String sign) {
        
        String rootPath = req.getSession().getServletContext().getRealPath("/WEB-INF/jasper/")+"/";
        
        AMLVo amlVo = new AMLVo();
        
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        amlVo = amlService.findAMLInfo(mainId,registNo,claimNo,sign);
        List<AMLVo> amlLists=new ArrayList<AMLVo>();
        amlLists.add(amlVo);
        
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(amlLists);
        model.addAttribute("jrMainDataSource",jrDataSource);
        
        model.addAttribute("SUBREPORT_DIR",rootPath);
        
        //自然人、法人被保险人共有
        model.addAttribute("insuredName",amlVo.getInsuredName());    
        model.addAttribute("adress",amlVo.getAdress());
        model.addAttribute("isConsistent",amlVo.getIsConsistent());
        model.addAttribute("payAccountNo",amlVo.getPayAccountNo());
        model.addAttribute("acconutNo",amlVo.getAcconutNo());
        
        if(StringUtils.isNotBlank(amlVo.getInsureRelation())&&amlVo.getInsureRelation().startsWith("0")){
            model.addAttribute("insureRelation","0");
        }else {
            model.addAttribute("insureRelation",amlVo.getInsureRelation());
        }
        model.addAttribute("favoreelInsureRelation",amlVo.getFavoreelInsureRelation());
        
        
        AMLVo favoreel = new AMLVo();
        List<AMLVo> favoreelList = new ArrayList<AMLVo>();
        AMLThreeCertificateVo amlThreeCertificateVo = new AMLThreeCertificateVo();
        List<AMLThreeCertificateVo> amlThreeCertificateList = new ArrayList<AMLThreeCertificateVo>();
        AMLOneCertificateVo amlOneCertificateVo = new AMLOneCertificateVo();
        List<AMLOneCertificateVo> amlOneCertificateList = new ArrayList<AMLOneCertificateVo>();
        AMLVo favoreen = new AMLVo();
        List<AMLVo> favoreenList = new ArrayList<AMLVo>();
        
        favoreel.setSUBREPORT_DIR(rootPath);
        
        //判断被保险人与受益人是否相同
        if(amlVo.getIdentifyNumber().equals(amlVo.getFavoreeIdentifyCode())){
            model.addAttribute("flag","1");
            favoreelList = null;
            favoreenList = null;
        }else {
            model.addAttribute("flag","2");
            //受益人法人
            if("2".equals(amlVo.getCustomerType())){
                favoreel.setFavoreeName(amlVo.getFavoreeName());
                favoreel.setFavoreeAdress(amlVo.getFavoreeAdress());
                favoreel.setFavoreelBusinessArea(amlVo.getFavoreelBusinessArea());
                //TODO 判断三证合一
                if(!"00".equals(amlVo.getFavoreeCertifyType())){
                    amlThreeCertificateVo.setFavoreeIdentifyCode(amlVo.getFavoreeIdentifyCode());
                    amlThreeCertificateVo.setFavoreelRevenueRegistNo(amlVo.getFavoreelRevenueRegistNo());
                    amlThreeCertificateVo.setFavoreelBusinessCode(amlVo.getFavoreelBusinessCode());
                    if(amlVo.getFavoreeCertifyStartDate() != null&&amlVo.getFavoreeCertifyEndDate() != null){
                        amlThreeCertificateVo.setFavoreelCertifyDate(format.format(amlVo.getFavoreeCertifyStartDate())+" - "+format.format(amlVo.getFavoreeCertifyEndDate()));
                    }
                    amlThreeCertificateList.add(amlThreeCertificateVo);
                    amlOneCertificateList = null;
                }else {
                    amlOneCertificateVo.setFavoreeIdentifyCode(amlVo.getFavoreeIdentifyCode());
                    if(amlVo.getFavoreeCertifyStartDate() != null&&amlVo.getFavoreeCertifyEndDate() != null){
                        amlOneCertificateVo.setFavoreelCertifyDate(format.format(amlVo.getFavoreeCertifyStartDate())+" - "+format.format(amlVo.getFavoreeCertifyEndDate()));
                    }
                    amlOneCertificateList.add(amlOneCertificateVo);
                    amlThreeCertificateList = null;
                }
                
                favoreel.setAmlThreeCertificateList(amlThreeCertificateList);
                favoreel.setAmlOneCertificateList(amlOneCertificateList);
                favoreelList.add(favoreel);
                favoreenList = null;
            }else {
                //受益人自然人  
                favoreen.setFavoreeName(amlVo.getFavoreeName());
                favoreen.setFavoreenSex(amlVo.getFavoreenSex());
                if(StringUtils.isNotBlank(amlVo.getFavoreenNatioNality())){
                    if("CHN".equals(amlVo.getFavoreenNatioNality())){
                        favoreen.setFavoreenNatioNality("1");
                    }else {
                        favoreen.setFavoreenNatioNality("2");
                    }
                }
                if(StringUtils.isNotBlank(amlVo.getFavoreeCertifyType())){
                    if("07".equals(amlVo.getFavoreeCertifyType())||"51".equals(amlVo.getFavoreeCertifyType())){
                        favoreen.setFavoreeCertifyType("03");
                    } else if(!("01".equals(amlVo.getFavoreeCertifyType())||"02".equals(amlVo.getFavoreeCertifyType())||"07".equals(amlVo.getFavoreeCertifyType())||"51".equals(amlVo.getFavoreeCertifyType())||"04".equals(amlVo.getFavoreeCertifyType())||"43".equals(amlVo.getFavoreeCertifyType()))){
                        favoreen.setFavoreeCertifyType("99");
                    } else {
                        favoreen.setFavoreeCertifyType(amlVo.getFavoreeCertifyType());
                    }
                    
                }
                favoreen.setFavoreeIdentifyCode(amlVo.getFavoreeIdentifyCode());
                if(amlVo.getFavoreeCertifyStartDate() != null&&amlVo.getFavoreeCertifyEndDate() != null){
                    favoreen.setFavoreeCertifyDate(format.format(amlVo.getFavoreeCertifyStartDate())+" - "+format.format(amlVo.getFavoreeCertifyEndDate()));
                }
                favoreen.setFavoreenProfession(amlVo.getFavoreenProfession());
                favoreen.setFavoreenPhone(amlVo.getFavoreenPhone());
                favoreen.setFavoreenAdressType(amlVo.getFavoreenAdressType());
                favoreen.setFavoreeAdress(amlVo.getFavoreeAdress());
                favoreen.setSUBREPORT_DIR(rootPath);
                favoreenList.add(favoreen);
                favoreelList = null;
            }
        }
        model.addAttribute("favoreelList",favoreelList);
       
        model.addAttribute("favoreenList",favoreenList);
        
        //自然人客户
        if("1".equals(amlVo.getInsuredType())){        
            model.addAttribute("sex",amlVo.getSex());
            
            if(StringUtils.isNotBlank(amlVo.getEducationCode())){
                if("CHN".equals(amlVo.getEducationCode())){
                    amlVo.setEducationCode("1");
                }else {
                    amlVo.setEducationCode("2");
                }
            }         
            model.addAttribute("educationCode",amlVo.getEducationCode());        
            if(StringUtils.isNotBlank(amlVo.getIdentifyType())){
                if("07".equals(amlVo.getIdentifyType())||"51".equals(amlVo.getIdentifyType())){
                    amlVo.setIdentifyType("03");
                }
                if(!("01".equals(amlVo.getIdentifyType())||"02".equals(amlVo.getIdentifyType())||"07".equals(amlVo.getIdentifyType())||"51".equals(amlVo.getIdentifyType())||"04".equals(amlVo.getIdentifyType())||"43".equals(amlVo.getIdentifyType()))){
                    amlVo.setIdentifyType("99");
                }
            }
            model.addAttribute("identifyType",amlVo.getIdentifyType());
            model.addAttribute("identifyNumber",amlVo.getIdentifyNumber());
            if(StringUtils.isNoneBlank(amlVo.getCertifyDate())){ 
                model.addAttribute("certifyDate",amlVo.getCertifyDate());
            }
            model.addAttribute("profession",codeTranService.transCode("OccupationCode",amlVo.getProfession()));
            model.addAttribute("mobile",amlVo.getMobile());
            model.addAttribute("adressType",amlVo.getAdressType());
            model.addAttribute("url", "/WEB-INF/jasper/AMLInfoPersonal.jasper");
        }else {//法人客户
            AMLThreeCertificateVo threeVo = new AMLThreeCertificateVo();
            List<AMLThreeCertificateVo> three = new ArrayList<AMLThreeCertificateVo>();
            AMLOneCertificateVo oneVo = new AMLOneCertificateVo();
            List<AMLOneCertificateVo> one = new ArrayList<AMLOneCertificateVo>();
            model.addAttribute("businessArea",amlVo.getBusinessArea());
            if(!"00".equals(amlVo.getIdentifyType())){
                threeVo.setIdentifyNumber(amlVo.getIdentifyNumber());
                threeVo.setRevenueRegistNo(amlVo.getRevenueRegistNo());
                threeVo.setBusinessCode(amlVo.getBusinessCode());
                if(amlVo.getCertifyDate() != null){
                    threeVo.setCertifyDate(amlVo.getCertifyDate());
                }
                three.add(threeVo);
                one = null;
                
            }else {
                oneVo.setIdentifyNumber(amlVo.getIdentifyNumber());
                if(amlVo.getCertifyDate() != null){
                    oneVo.setCertifyDate(amlVo.getCertifyDate());
                }
                one.add(oneVo);
                three = null;
            }
            model.addAttribute("three",three);
            model.addAttribute("one",one);

            
            model.addAttribute("legalPerson",amlVo.getLegalPerson());    
            model.addAttribute("legalPhone",amlVo.getLegalPhone());
            
            
            if(StringUtils.isNotBlank(amlVo.getLegalIdentifyType())){
                if("07".equals(amlVo.getLegalIdentifyType())||"51".equals(amlVo.getLegalIdentifyType())){
                    amlVo.setLegalIdentifyType("03");
                }
                if(!("01".equals(amlVo.getLegalIdentifyType())||"02".equals(amlVo.getLegalIdentifyType())||"07".equals(amlVo.getLegalIdentifyType())||"51".equals(amlVo.getLegalIdentifyType())||"04".equals(amlVo.getLegalIdentifyType())||"43".equals(amlVo.getLegalIdentifyType()))){
                    amlVo.setLegalIdentifyType("99");
                }
            }
            model.addAttribute("legalIdentifyType",amlVo.getLegalIdentifyType());
            
            model.addAttribute("legalIdentifyCode",amlVo.getLegalIdentifyCode());
            if(amlVo.getLegalCertifyStartDate() != null&&amlVo.getLegalCertifyEndDate() != null){
                model.addAttribute("legalCertifyDate",format.format(amlVo.getLegalCertifyStartDate())+" - "+format.format(amlVo.getLegalCertifyEndDate()));
            }
            model.addAttribute("authorityName",amlVo.getAuthorityName());
            model.addAttribute("authorityPhone",amlVo.getAuthorityPhone());
            
            if(StringUtils.isNotBlank(amlVo.getAuthorityCertifyType())){
                if("07".equals(amlVo.getAuthorityCertifyType())||"51".equals(amlVo.getAuthorityCertifyType())){
                    amlVo.setAuthorityCertifyType("03");
                }
                if(!("01".equals(amlVo.getAuthorityCertifyType())||"02".equals(amlVo.getAuthorityCertifyType())||"07".equals(amlVo.getAuthorityCertifyType())||"51".equals(amlVo.getAuthorityCertifyType())||"04".equals(amlVo.getAuthorityCertifyType())||"43".equals(amlVo.getAuthorityCertifyType()))){
                    amlVo.setAuthorityCertifyType("99");
                }
            }
            model.addAttribute("authorityCertifyType",amlVo.getAuthorityCertifyType());
            
            model.addAttribute("authorityNo",amlVo.getAuthorityNo());
            if(amlVo.getAuthorityStartDate() != null&&amlVo.getAuthorityEndDate() != null){
                model.addAttribute("authorityDate",format.format(amlVo.getAuthorityStartDate())+" - "+format.format(amlVo.getAuthorityEndDate()));
            }      
            model.addAttribute("url", "/WEB-INF/jasper/AMLInfoUnit.jasper");
        }
        
        
        if("1".equals(sign)){
            model.addAttribute("format", "doc"); // 报表格式 
        }else{
            model.addAttribute("format", "pdf"); // 报表格式 
        }

        return "iReportView";  // 对应jasper-defs.xml中的bean id
    }
}


