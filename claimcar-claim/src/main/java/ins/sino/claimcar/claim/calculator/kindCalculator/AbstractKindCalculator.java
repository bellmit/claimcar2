package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.framework.lang.Springs;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpDAccidentDeductVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.commom.vo.Refs;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class AbstractKindCalculator implements KindCalculator, Serializable {
	
	/**  */
	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory.getLog(AbstractKindCalculator.class);
    
    protected String kindCode;
    
    protected CompensateVo compensateVo;
    
    protected PrpLCItemKindVo prpLcItemKind;
    
    
    
    protected PrpLCompensateVo prpLcompensate;
    
    protected PrpLLossItemVo prpLloss;
    
    protected PrpLLossPropVo prpLpropLoss;
    
    protected PrpLLossPersonVo prpLpersonLoss;
    
    protected ThirdPartyDepreRate thirdPartyDepreRate;
    
    protected Object lossItem;
    
    protected Boolean cprc;//??????????????????
    
    private PolicyViewService policyViewService = (PolicyViewService)Springs.getBean("policyViewService");
    
    private ConfigService configService = (ConfigService)Springs.getBean("configService");
	private LossCarService lossCarService =	(LossCarService)Springs.getBean("lossCarService");
	private PlatLockService platLockService = (PlatLockService)Springs.getBean("platLockService");
	private CheckTaskService checkTaskService = (CheckTaskService)Springs.getBean("checkTaskService");
	protected CompensateService compensateService;
    public String getKindCode() {
        return kindCode;
    }
    
    public AbstractKindCalculator(CompensateVo compensateVo ,Object lossItem ,CompensateService compensateService) {
        this.compensateVo = compensateVo;
        this.prpLcompensate = compensateVo.getPrpLCompensateVo();
        this.lossItem = lossItem;
        this.compensateService = compensateService;
        
        boolean validParameter = 
            (this.lossItem instanceof PrpLLossItemVo) || 
            (this.lossItem instanceof PrpLLossPropVo) || 
            (this.lossItem instanceof PrpLLossPersonFeeVo);
        Assert.isTrue(validParameter, 
                      "Invalid parameter.Parameter lossItem is not the type of PrpLloss PrpLpropLoss PrpLpersonLoss Prplotherloss");
        
        //????????????????????????????????????????????????
        this.kindCode = (String)Refs.get(this.lossItem , "kindCode");
        
        //TODO ????????????
        //???????????????????????????????????????????????????????????????PrpLcItemKind???
        //??????????????????????????????PrpLcItemKind???
        //prpLcItemKind = compensateService.queryPrpLcItemKind4KindCode(kindCode, prpLcompensate);
        for(PrpLCItemKindVo itemKindVo : compensateVo.getPrpLCItemKindVoList()){
        	if(itemKindVo.getKindCode().equals(kindCode)){
        		prpLcItemKind = itemKindVo; 
        		break;
        	}
        }
        
       
        cprc = compensateService.isCprcCase(prpLcompensate.getRegistNo());
        thirdPartyDepreRate = compensateVo.getThirdPartyDepreRate();
        if(thirdPartyDepreRate ==null){
        	thirdPartyDepreRate = compensateService.queryThirdPartyDepreRate(prpLcompensate.getRegistNo());
        }
        
      //  Assert.notNull(prpLcItemKind);
        if(prpLcItemKind==null){
        	throw new IllegalArgumentException("????????????????????????"+kindCode);
        }
    }
    
    @Override
    public BigDecimal calIndemnityDutyRate(BigDecimal value) {
    	if(value!=null){
			return this.refSetValue("dutyRate",value);
		}
    	if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){
			PrpLLossItemVo prpLLossItemVo = (PrpLLossItemVo)lossItem;
			PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findDlossCarMainVoByRegistNoAndId(prpLcompensate.getRegistNo(),prpLLossItemVo.getDlossId());

			Integer serialNo = null;
			if(prpLDlossCarMainVo != null){
				serialNo = prpLDlossCarMainVo.getSerialNo();
			}else{
				PrpLCheckCarVo prpLCheckCarVo = checkTaskService.findCheckCarVoByRegistNoAndId(prpLcompensate.getRegistNo(),prpLLossItemVo.getDlossId());
				serialNo = prpLCheckCarVo.getSerialNo();
			}
			// ???????????????
			logger.info(serialNo +"??????????????????   "+ prpLLossItemVo.getPayFlag());
			if(serialNo==1&&CodeConstants.PayFlagType.COMPENSATE_PAY.equals(prpLLossItemVo.getPayFlag())){
				value = this.queryMainCarIndemnityDutyRate();
			}else{
				// ???????????????
				if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
					// ??????????????????
					PrpLPlatLockVo prpLPlatLockVo = platLockService.findPrpLPlatLockVoById(prpLLossItemVo.getSubrogationId());
					List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(this.prpLcompensate.getRegistNo());
					for(PrpLCheckDutyVo prpLCheckDutyVo:prpLCheckDutyVoList){
						if(prpLPlatLockVo.getOppoentLicensePlateNo().trim().equals(prpLCheckDutyVo.getLicenseNo())){
							value = prpLCheckDutyVo.getIndemnityDutyRate();
							break;
						}
					}
				}else{
					PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(this.prpLcompensate.getRegistNo(),serialNo);
					value = prpLCheckDutyVo.getIndemnityDutyRate();
				}
			}	
    	}else{
    		
    		value = this.queryMainCarIndemnityDutyRate();
    	}
    	
        //??????????????????????????????1??????????????????????????????????????????????????????????????????????????????
        if(value == null){
            value = new BigDecimal(100.00d);
        }
        
        return refSetValue("dutyRate",value);
    }
    
    private BigDecimal refSetValue(String fileName,BigDecimal value){
    	Refs.set(this.lossItem , fileName , value);
    	
    	return value;
    	
    }
    
    /**
     * ??????????????? ???????????????
     * ??????????????? ??? PrpDAccidentDeduct ????????????
     */
    @Override
    public BigDecimal calDutyDeductibleRate(BigDecimal value){
		value = BigDecimal.ZERO;

    	//G??????????????????????????????
    	if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)){
    		PrpLLossItemVo lossItemVo = (PrpLLossItemVo)this.lossItem;
    		BigDecimal sumLoss = lossItemVo.getSumLoss().add(lossItemVo.getRescueFee()).subtract(lossItemVo.getOtherDeductAmt());
    		if(cprc){
    			if(sumLoss.compareTo(lossItemVo.getItemAmount())!=-1){
    				value = new BigDecimal(20d);
    			}
    		}else{
    			double amount = lossItemVo.getItemAmount().doubleValue();
    			if(thirdPartyDepreRate.getActualValue()<amount){
    				amount = thirdPartyDepreRate.getActualValue();
    			}
    			if(sumLoss.doubleValue()>=amount){
    				value = new BigDecimal(20d);
    			}
    			
    		}
    		
    	}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)){
    		if(StringUtils.isNotBlank(prpLcompensate.getRiskCode()) && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcompensate.getRiskCode())!=null && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcompensate.getRiskCode())) {
    			value = new BigDecimal(0d);
    		}else {
    			value = new BigDecimal(15d);
    		}
    		
    	}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_R)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_E)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_V1)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z1)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){
    		if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2) && StringUtils.isNotBlank(prpLcompensate.getRiskCode()) && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcompensate.getRiskCode())!=null && CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcompensate.getRiskCode())) {
    			value = new BigDecimal(0d);
    		}else {
    			value = new BigDecimal(20d);
    		}
    		
//    	}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NT)){
//    		value = new BigDecimal(30d);
    		
    	}else{
    		PrpLCItemCarVo itemCarVo = policyViewService.findItemCarByRegistNoAndPolicyNo(prpLcompensate.getRegistNo(),prpLcompensate.getPolicyNo());
        	String clauseType =itemCarVo.getClauseType();
        	String strKindCode = kindCode;
        	//????????????????????????????????????   ??????????????????
        	if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z2)
        		|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NZ)){
        		strKindCode = CodeConstants.KINDCODE.KINDCODE_A;
        	}
        	
        	PrpDAccidentDeductVo accidentDeductVo = configService.findAccidentDeduct(prpLcompensate.getRiskCode(),strKindCode,clauseType,prpLcompensate.getIndemnityDuty());
        	value = BigDecimal.ZERO;
        	if(accidentDeductVo !=null){
        		value = accidentDeductVo.getDeductibleRate();
        		
        		if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A) && 
        				"1".equals(prpLcompensate.getPrpLCompensateExt().getDisastersFlag())){
        			value = BigDecimal.ZERO;
        		}
        	}
    	}
    	
        Assert.notNull(value);
        value = MoneyFormator.format4Rate(value);
        final String dutyDeductibleRateFieldName = "deductDutyRate";
        Refs.set(this.lossItem , dutyDeductibleRateFieldName , value);
        return value;
    }
    
    @Override
    public BigDecimal calSelectDeductibleRate(BigDecimal value) {
    	value = new BigDecimal(0d);
    	
    	if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_C)
    			||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)){
    		value = new BigDecimal("0");
    	}if(CodeConstants.KINDCODE.KINDCODE_G.equals(kindCode)){
    		// G????????????????????????????????????
    		PrpLLossItemVo lossItemVo = (PrpLLossItemVo)this.lossItem;
    		BigDecimal sumLoss = lossItemVo.getSumLoss().add(lossItemVo.getRescueFee()).subtract(lossItemVo.getOtherDeductAmt());
    		//????????????????????????????????????????????????????????????  ????????? ??????????????????
			if(sumLoss.compareTo(lossItemVo.getItemAmount())==-1
					&& CodeConstants.ISNEWCLAUSECODE_MAP.get(lossItemVo.getRiskCode())){
				value = BigDecimal.ZERO;
			}else{
				value = this.sommeDeductibleRate(value);
			}
    	}else{
    		value = this.sommeDeductibleRate(value);
    	}
		
        Assert.notNull(value);
        value = MoneyFormator.format4Rate(value);
        logger.info("value = " + value);
        final String selectDeductibleRateFieldName = "deductAddRate";
        Refs.set(this.lossItem , selectDeductibleRateFieldName , value);
        return value;
    }
    
    /**
	 * ?????????????????????????????????
	 * 
	 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????5%?????????????????????????????????25% TODO
	 * ?????????????????????
	 * @return BigDecimal
	 */
	public BigDecimal sommeDeductibleRate(BigDecimal selectDeductibleRate) {
		String strKindCode = kindCode;
		//???????????????????????? ?????????????????????????????????
		if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z1)){
			strKindCode = CodeConstants.KINDCODE.KINDCODE_G;
		}
		
		//????????????????????????????????????   ??????????????????
    	if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z2)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NZ)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)){
    		strKindCode = CodeConstants.KINDCODE.KINDCODE_A;
    	}
    	
    	List<PrpLClaimDeductVo> claimDeductList = compensateVo.getClaimDeductList();
    	
		if(claimDeductList != null && !claimDeductList.isEmpty()) {
			for(PrpLClaimDeductVo prpLClaimDeductVo:claimDeductList){
				if(strKindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)){
					PrpLLossItemVo lossItemVo = (PrpLLossItemVo)this.lossItem;
					BigDecimal sumLoss = lossItemVo.getSumLoss().add(lossItemVo.getRescueFee()).subtract(lossItemVo.getOtherDeductAmt());
					if(sumLoss.compareTo(lossItemVo.getItemAmount())==-1){
						// ??????????????? Yes?????????
						if(strKindCode.equals(prpLClaimDeductVo.getKindCode().trim())){
							// G????????????????????? ??????????????????170 180??????
							if("170".equals(prpLClaimDeductVo.getDeductCondCode())||"180".equals(prpLClaimDeductVo.getDeductCondCode())){
								selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
							}
						}
					}else{
						// ??????????????? Yes?????????
						if(strKindCode.equals(prpLClaimDeductVo.getKindCode().trim())){
							selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
						}
					}
					
				}else{
					// ??????????????? Yes?????????
					if(strKindCode.equals(prpLClaimDeductVo.getKindCode().trim())){
						selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
					}
				}
			}
		}
		
		return selectDeductibleRate;
	}

    public BigDecimal calExceptDeductRate(BigDecimal value) {
    	boolean isMitemKind = policyViewService.isMKindCode(prpLcItemKind.getRegistNo(),prpLcItemKind.getKindCode());
        double exceptDeductRate = 0d;
        
        BigDecimal deductDutyRate = (BigDecimal)Refs.get(this.lossItem, "deductDutyRate");
        BigDecimal deductAddRate = (BigDecimal)Refs.get(this.lossItem, "deductAddRate");
        logger.info("kindCode = " + kindCode);
        if (isMitemKind) {
        	exceptDeductRate = deductDutyRate.doubleValue()* (100 - deductAddRate.doubleValue()) / 100;
//            if (CodeConstants.KINDCODE.KINDCODE_G.equals(kindCode)) {
//                exceptDeductRate = calDutyDeductibleRate(null).doubleValue();
//            } else {
//                exceptDeductRate =
//                        100 - (100 - this.calDutyDeductibleRate(null).doubleValue()) * 
//                              (100 - this.calSelectDeductibleRate(null).doubleValue()) / 100 
//                        - this.calSelectDeductibleRate(null).doubleValue();
//            	  exceptDeductRate = 100 - (100 - deductDutyRate.doubleValue()) * (100 - deductAddRate.doubleValue()) / 100 
//                          - deductAddRate.doubleValue();
        	
        	
//            }
            
        }

        logger.info("exceptDeductRate = " + exceptDeductRate);
        final String exceptDeductibleRateFieldName = "deductOffRate";
        value = MoneyFormator.format4Rate(new BigDecimal(exceptDeductRate));
        Refs.set(this.lossItem , exceptDeductibleRateFieldName , value);
        return value;
    }

    @Override
    public BigDecimal calAmount(BigDecimal value) {
    	value = this.calKindAmount(value);
    	
        //??????????????????????????????????????????????????????PrpLcItemkind.amount???
        if(value == null){
            value = this.prpLcItemKind.getAmount();
        }
        value = MoneyFormator.format(value);
        final String amountFieldName = "itemAmount";
        Refs.set(this.lossItem , amountFieldName , value);
        
        return value;
    }

	private BigDecimal calKindAmount(BigDecimal value){
		
		if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_C)
				||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_T)
				||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_RF)){//????????????
			
			BigDecimal eMotorAmount =BigDecimal.ZERO;//eMotor?????????
			Map<String,BigDecimal> emotorMap = compensateVo.geteMotorMap();
			if(emotorMap!=null && !emotorMap.isEmpty()){
				if(emotorMap.get(kindCode)!=null){
					eMotorAmount = emotorMap.get(kindCode);
				}
			}
			
			value = prpLcItemKind.getAmount().subtract(eMotorAmount);
			logger.info("eMotor???????????? = " + eMotorAmount+" ????????????="+value);
			
			PrpLLossPropVo prpLLossPropVo = (PrpLLossPropVo)this.lossItem;
			prpLLossPropVo.setMaxQuantity(value.divide(prpLcItemKind.getUnitAmount(),2,BigDecimal.ROUND_HALF_UP));
			prpLLossPropVo.setUnitAmount(prpLcItemKind.getUnitAmount());
			return value;
    	}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_NZ)
    			|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z2)){//????????????
    		
    		BigDecimal eMotorAmount = BigDecimal.ZERO;//eMotor?????????
			Map<String,BigDecimal> emotorMap = compensateVo.geteMotorMap();
			if(emotorMap!=null && !emotorMap.isEmpty()){
				if(emotorMap.get(kindCode)!=null){
					eMotorAmount = emotorMap.get(kindCode);
				}
			}
            
            value = prpLcItemKind.getAmount().subtract(eMotorAmount);
            logger.info("eMotor???????????? = " + eMotorAmount+" ????????????="+value);
            return value;
    	}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D12) || 
    			kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D11)){
    		//??????????????????????????????????????????????????????=?????? / (????????? - 1) - ???????????????????????????????????????
    		
//			PrpLLossPersonFeeVo personLoss = (PrpLLossPersonFeeVo)this.lossItem;
//			BigDecimal sumRealPay4KindAndPerson = BigDecimal.ZERO;
//			//???????????????????????????????????????
////			sumRealPay4KindAndPerson = compensateService.querySumRealPay4KindAndPerson(this.prpLcompensate.getRegistNo(),
////					this.kindCode,personLoss.getPersonId());
//
//			value = this.prpLcItemKind.getAmount();
//			BigDecimal searCount = thirdPartyDepreRate.getSearCount();
//			// ???????????????????????????
//			searCount = searCount.subtract(new BigDecimal(1));
//			value = MoneyFormator.format(value.divide(searCount,2,BigDecimal.ROUND_HALF_UP).subtract(sumRealPay4KindAndPerson));
			
			
			if(prpLcItemKind.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_D11)){
				value = this.prpLcItemKind.getAmount();
			}else{
				value = this.prpLcItemKind.getUnitAmount();
			}
			
			PrpLLossPersonFeeVo personLoss = (PrpLLossPersonFeeVo)this.lossItem;
			personLoss.setQuantity(prpLcItemKind.getQuantity());
			
			return value;
		}else{
			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_F)
					|| kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X1)
					||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X2)){//F???????????????????????????
				
				prpLcItemKind.setAmount(compensateVo.getAmountKindA());
			}
			
			return this.queryOnceSubAmount();
		}
	
	}
    
    @Override
    public BigDecimal calItemValue(BigDecimal value) {
//    	PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLcItemKind.getRegistNo(),prpLcItemKind.getPolicyNo());
        //??????????????????????????????????????????????????????????????????????????????????????????
        if(value == null){
            //??????????????????????????????????????????
            value = BigDecimal.valueOf(thirdPartyDepreRate.getActualValue());
        }
        
        value = MoneyFormator.format(value);
        //??????????????????????????????????????????
        if(lossItem instanceof PrpLLossItemVo){
            final String itemValueFieldName = "itemValue";
            Refs.set(this.lossItem , itemValueFieldName , value);
        }
        
        return value;
    }

    @Override
    public BigDecimal calDepreRate(BigDecimal value) {
    	PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLcItemKind.getRegistNo(),prpLcItemKind.getPolicyNo());
        //????????????????????????????????????????????????????????????????????????????????????
        if(value == null && lossItem instanceof PrpLLossItemVo){
            //??????????????????????????????????????????
            value = BigDecimal.valueOf(thirdPartyDepreRate.getActualValue());
        }
        
        
        //??????????????????????????????????????????
//        if(lossItem instanceof PrpLLossItemVo){
//            value = MoneyFormator.format4Rate(value);
//            final String depreRateFieldName = "depreRate";
//            Refs.set(this.lossItem , depreRateFieldName , value);
//        }
        
        return value;
    }
    
    /**
     * ????????????????????????????????????
     * @return
     */
    protected BigDecimal queryMainCarIndemnityDutyRate(){
        return MoneyFormator.format4Rate(this.prpLcompensate.getIndemnityDutyRate());
    }
    
    public BigDecimal calDeductibleRate(BigDecimal value){
        if(value == null){
            value = this.calDeductibleRateByDuty_Select();
        }
        final String deductibleRateFieldName = "deductibleRate";
        Refs.set(this.lossItem , deductibleRateFieldName , value);
        return value;
    }
    
    /**
     * ?????????????????????????????????????????????????????????
     * @param value
     * @return
     */
    protected BigDecimal calDeductibleRateByDuty_Select(){
        BigDecimal deductDutyRate = (BigDecimal)Refs.get(this.lossItem, "deductDutyRate");
        BigDecimal deductAddRate = (BigDecimal)Refs.get(this.lossItem, "deductAddRate");
        Assert.notNull(deductDutyRate);
        Assert.notNull(deductAddRate);
        double deductibleRate = 0.00;
        if (CodeConstants.KINDCODE.KINDCODE_G.equals(kindCode)) {
            deductibleRate = (100.00d - deductDutyRate.doubleValue() - deductAddRate.doubleValue());
        } else {
            deductibleRate = (100.00d - deductDutyRate.doubleValue()) * (100.00d - deductAddRate.doubleValue()) / 100.00d;
        }
        BigDecimal value = new BigDecimal(deductibleRate);

        final String deductibleRateFieldName = "deductibleRate";
        Refs.set(this.lossItem , deductibleRateFieldName , value);
        return value;
    }
    
    /**
     * ????????????????????????????????????
     * @param value
     * @return
     */
    protected BigDecimal calDeductibleRateBySelect(){
        BigDecimal deductAddRate = (BigDecimal)Refs.get(this.lossItem, "deductAddRate");
        Assert.notNull(deductAddRate);
        double deductibleRate = 100d - deductAddRate.doubleValue();
        BigDecimal value = new BigDecimal(deductibleRate);
        final String deductibleRateFieldName = "deductOffRate";
        Refs.set(this.lossItem , deductibleRateFieldName , value);
        return value;
    }
    
    /**
     * ????????????????????????????????? = ?????????????????? - eMotor?????????
     * @return
     */
    protected BigDecimal queryAllSubAmount(){
        //?????????????????????
        BigDecimal amount = prpLcItemKind.getAmount();
        //eMotor?????????
        BigDecimal eMotorAmount = compensateService.queryEmotorKindLossfee(prpLcItemKind.getPolicyNo(),this.kindCode);
        
        BigDecimal result = MoneyFormator.format(amount.subtract(eMotorAmount));
        BigDecimal zero = MoneyFormator.format(new BigDecimal(0));
        if(result.compareTo(zero) <= 0){
            result = zero;
        }
        return result;
    }
    
    /**
     * ??????????????????????????????????????? = ?????????????????? - ?????????????????????????????????????????????????????????
     * @return
     */
	protected BigDecimal queryOnceSubAmount(){
		BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
		Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
		if(kindPaidMap!=null && !kindPaidMap.isEmpty() && kindPaidMap.containsKey(kindCode)){
			sumRealPay4Kind = kindPaidMap.get(kindCode);
		}
		BigDecimal result = prpLcItemKind.getAmount().subtract(DataUtils.NullToZero(sumRealPay4Kind));
		BigDecimal zero = MoneyFormator.format(new BigDecimal(0));
		
		if(result.compareTo(zero) <= 0){
			result = zero;
		}
		
		return result;
    }
}