package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CompensateListVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 损失项数量*/
	private int listAmount;
	/** 正在计算损失项序号*/
	private int listNum;
	/** 理算类型 1: 商业  2: 交强*/
	private int compensateType;
	/** 诉讼案标志位*/
	private String isSuitFlag;
	/** 协议金额*/
	private double exgratiaFee;
	/** 损失项List*/
	private List compensateList;
	/** 返回损失项信息*/
	private List returnCompensateList = new ArrayList();
	/** 超限额控制后险别赔付信息*/
	private List kindPayInfoList = new ArrayList();
	/** 分险别赔付信息Map*/
	private Map kindPayInfoMap = new HashMap();
	/** 险别赔付项Map*/
	private Map kindLossTextMap = new HashMap();
	/** 返回损失项信息*/
	private List returnBZCompensateList = new ArrayList();
	/** 险别项下赔付信息*/
	private List returnKindPayList = new ArrayList();
	/** 根据费用代码获取人员损失类型的数据传输对象*/
	private List PersonDamageInfoList = new ArrayList();
	/** 同一个人员下面的理算金额列表*/
	private List personLossList = new ArrayList();
	/** 按人员序号保存的不同人员理算金额列表*/
	private Map personLossListMap = new HashMap();
	/** 损失项序号*/
	private int lossIndex = 0;
	/** 险别List*/
	private List kindInfoList = new ArrayList();
	/** 损失人员数量*/
	private int lossPersonsNum = 0;
	/** 组织人员损失数据*/
	private double[][] personLossArray;
	/** 有责车辆个数*/
	private int dutyCarNum = 0;
	/** 是否存在有责车辆标志 0:没有 1:有*/
	private String bzDutyCar;
	/** 2012-02-06 代位求偿需求添加
	 * 1:追偿
	 * 2:清付
	 * 3:自付(上代位后的新案件)
	 * 历史案件为null或””:普通理算
	 **/
	/** 代位类型 */
	private String payType;
	/**
	 * 追商业获得总赔款
	 */
	private double subrogationBRealPay;
	/**
	 * 追交强获得总赔款
	 */
	private double subrogationBzRealPay;
	/** 商业计算书：标的车代责任对方交强车辆*/
	private List thirdPartyRecoveryInfoAry = new ArrayList();
	private PrpLCItemKindVo prpLCItemKindVo;//保单所承保险别信息
	private ClaimFeeCondition claimFeeCondition;//查勘、定损、人伤损失等信息
	private  String  isBiPCi;//是否商业扣交强 1:商业扣交强 0：正常交强理算
	private double sumRealPayForA = 0;//记录A险总赔付金额，用于代位求偿案件获取自付部分赔款金额
	private double sumRealPayForAM = 0;//记录A险总赔付金额，用于代位求偿案件获取自付部分赔款金额不计免赔金额
	/**  理算或者立案 compensate claim**/
	private String calculateType;
	private String reCaseFlag;
	private String registNo;
	private Map<String,Double> leftAmountMap = new HashMap<String,Double>();
	private List<PrpLCheckDutyVo> checkDutyList = new ArrayList<PrpLCheckDutyVo>();
	private BigDecimal carLossRate; // 车辆损失金额占车财总损失金额的比例
	
	@SuppressWarnings("unchecked")
	public void setCompensateExpInfo (CompensateExp compensateExp){
		if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode())){
			if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
				if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
				} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
				} else {
					sumRealPayForA = sumRealPayForA + compensateExp.getSumRealPay();
					sumRealPayForAM = sumRealPayForAM + compensateExp.getMsumRealPay();
				}
			}
		}
		returnCompensateList.add(compensateExp);
	}
	@SuppressWarnings("unchecked")
	public void setreturnBZCompensateList(ThirdPartyCompInfo thirdPartyCompInfo, ThirdPartyInfo thirdPartyInfo, String message) {
		
		if (thirdPartyRecoveryInfoAry != null) {
			for (int i = 0; i < thirdPartyRecoveryInfoAry.size(); i++) {
				ThirdPartyRecoveryInfo thirdPartyRecoveryInfo = (ThirdPartyRecoveryInfo) thirdPartyRecoveryInfoAry.get(i);
				if (thirdPartyRecoveryInfo != null) {
					if (thirdPartyRecoveryInfo.getRecoverySumRealPay() == null) {
						thirdPartyRecoveryInfo.setRecoverySumRealPay(new BigDecimal(0.00));
					}
					if (thirdPartyRecoveryInfo.getMainCarLicenseNo() != null) {
						String mainCarLicenseNo = thirdPartyRecoveryInfo.getMainCarLicenseNo();
						String mainCarLossName = thirdPartyRecoveryInfo.getMainCarLossName();
						if (mainCarLicenseNo != null && thirdPartyCompInfo.getObjectLicenseNo() != null
							&& thirdPartyCompInfo.getObjectLicenseNo().trim().equals(mainCarLicenseNo.trim())// 确认为赔付标的车
							&& thirdPartyRecoveryInfo.getThirdCarlicenseNo() != null && thirdPartyCompInfo.getLicenseNo() != null
							&& thirdPartyCompInfo.getLicenseNo().trim().equals(thirdPartyRecoveryInfo.getThirdCarlicenseNo().trim())) {// 确认为代位三者车所赔付
							
							if (Double.isNaN(thirdPartyCompInfo.getSumRealPay())) {
								thirdPartyCompInfo.setSumRealPay(0.00);
							}
							if (mainCarLossName != null && thirdPartyCompInfo.getLossName() != null
								&& mainCarLossName.trim().equals(thirdPartyCompInfo.getLossName().trim())) {
								thirdPartyRecoveryInfo.setRecoverySumRealPay(thirdPartyRecoveryInfo.getRecoverySumRealPay().add(new BigDecimal(thirdPartyCompInfo.getSumRealPay())));
								if (thirdPartyCompInfo.getLossFeeName() != null && "定损金额".equals(thirdPartyCompInfo.getLossFeeName().trim())) {
									if (thirdPartyRecoveryInfo.getLossOfRecoverySumRealPay() == null) {
										thirdPartyRecoveryInfo.setLossOfRecoverySumRealPay(new BigDecimal(0.00));
									}
									thirdPartyRecoveryInfo.setLossOfRecoverySumRealPay(thirdPartyRecoveryInfo.getLossOfRecoverySumRealPay().add(new BigDecimal(thirdPartyCompInfo.getSumRealPay())));
								} else if (thirdPartyCompInfo.getLossFeeName() != null && "施救费".equals(thirdPartyCompInfo.getLossFeeName().trim())) {
									if (thirdPartyRecoveryInfo.getRescueOfRecoverySumRealPay() == null) {
										thirdPartyRecoveryInfo.setRescueOfRecoverySumRealPay(new BigDecimal(0.00));
									}
									thirdPartyRecoveryInfo.setRescueOfRecoverySumRealPay(thirdPartyRecoveryInfo.getRescueOfRecoverySumRealPay().add(new BigDecimal(thirdPartyCompInfo.getSumRealPay())));
								}
							}
							
						}
					}
				}
			}
		}
		
		returnBZCompensateList.add(thirdPartyCompInfo);
	}
	
	@SuppressWarnings("unchecked")
	public void setreturnKindPayList(CompensateKindPay compensateKindPay) {
		returnKindPayList.add(compensateKindPay);
	}
	
	/**
	 * 获取险别赔付信息Map
	 */
	@SuppressWarnings("unchecked")
	public void setKindPayInfoMap(CompensateExp compensateExp){
		double sumLoss = 0.00;
		double rescueFee = 0.00;
		double adjSumLoss = 0.00;
		//暂不使用
		//double adjRescueFee = 0.00;
		if ((CodeConstants.KINDCODE.KINDCODE_A).equals(compensateExp.getKindCode())) {
			//是否为新条款
			boolean flag = false;
			if(!"1101".equals(compensateExp.getRiskCode().trim())){
				flag = CodeConstants.ISNEWCLAUSECODE_MAP.get(compensateExp.getRiskCode().trim());
			}
			if(flag){
				if(compensateExp.getSumLoss() < compensateExp.getDamageAmount()){//定损〈保额
					if(compensateExp.getRescueFee() < compensateExp.getDamageAmount()){//施救费 < 保额
						if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
							if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else {
								sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
								rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
							}
						} else {
							sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid());
							rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid());
						}
					} else {//施救费 >=保额
						if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
							if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else {
								sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid());
								rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) ;
							}
						} else {
							sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
							rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
						}
					}
				} else {
					if(compensateExp.getRescueFee() < compensateExp.getDamageAmount()){//施救费 < 保额
						if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
							if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else {
								sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid());
								rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid());
							}
						} else {
							sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
							rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
						}
					} else {//施救费 >=保额
						if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
							if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
								sumLoss = 0.00;
								rescueFee = 0.00;
							} else {
								sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid());
								rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid());
							}
						} else {
							sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
							rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
						}
					}
				}
			} else {
				if("B".equals(compensateExp.getClaimType())){ //代位求偿案件
					if(compensateExp.getOppoentCoverageType() != null && "2".equals(compensateExp.getOppoentCoverageType())){//代商业
						sumLoss = 0.00;
						rescueFee = 0.00;
					} else if (compensateExp.getOppoentCoverageType() != null && "1".equals(compensateExp.getOppoentCoverageType())){//代交强
						sumLoss = 0.00;
						rescueFee = 0.00;
					} else {
						sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
						rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
					}
				} else {
					sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
					rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
				}
			}
		} else if (CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode().trim())){//无法找到第三方特约险
			if(compensateExp.getSumLoss() < compensateExp.getDamageAmount()){//定损〈保额
				if(compensateExp.getRescueFee() < compensateExp.getDamageAmount()){//施救费 < 保额
					sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
					rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
				} else {//施救费 >=保额
					sumLoss = (compensateExp.getSumLoss() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
					rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
				}
			} else {//定损 >= 保额
				if(compensateExp.getRescueFee() < compensateExp.getDamageAmount()){//施救费 < 保额
					sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
					rescueFee = (compensateExp.getRescueFee() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
				} else {//施救费 >=保额
					sumLoss = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid())* compensateExp.getIndemnityDutyRate();
					rescueFee = (compensateExp.getDamageAmount() * compensateExp.getLossQuantity() - compensateExp.getRescueFeeBZPaid()) * compensateExp.getIndemnityDutyRate();
				}
			}
		} else{
			sumLoss = (compensateExp.getSumLoss() - compensateExp.getSumRest()) * compensateExp.getLossQuantity();
			rescueFee = compensateExp.getRescueFee() * compensateExp.getLossQuantity();
			if (CodeConstants.KINDCODE.KINDCODE_D11.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D12.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_K1.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_Y.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_NZ.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_V1.equals(compensateExp.getKindCode())) {
				
				adjSumLoss = (compensateExp.getSumLoss() + compensateExp.getRescueFee() - compensateExp.getbZPaid()-compensateExp.getOtherDeductFee())*
						compensateExp.getLossQuantity() * compensateExp.getIndemnityDutyRate() * (1-compensateExp.getAbsrate()/100);
				
			} else if(CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())
					|| CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_X2.equals(compensateExp.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_NY.equals(compensateExp.getKindCode())){
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())){
					if(compensateExp.getSumLoss()<compensateExp.getDeviceActualValue()){
						adjSumLoss = (compensateExp.getSumLoss() + compensateExp.getRescueFee() -compensateExp.getSumRest() - compensateExp.getbZPaid())* compensateExp.getLossQuantity() * compensateExp.getIndemnityDutyRate();
					} else {
						adjSumLoss = (compensateExp.getDeviceActualValue() + compensateExp.getRescueFee() -compensateExp.getSumRest() - compensateExp.getbZPaid())* compensateExp.getLossQuantity() * compensateExp.getIndemnityDutyRate();
					}
				} else {
					
					adjSumLoss = (compensateExp.getSumLoss() + compensateExp.getRescueFee() -compensateExp.getOtherDeductFee() - compensateExp.getbZPaid())* compensateExp.getLossQuantity() * compensateExp.getIndemnityDutyRate()*(1-(compensateExp.getAbsrate()/100));
				}
			} else if(CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_E.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_L.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_Z1.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_Z2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_S.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_U.equals(compensateExp.getKindCode())){
				adjSumLoss = (compensateExp.getSumLoss() + compensateExp.getRescueFee() - compensateExp.getSumRest() - compensateExp.getbZPaid())* compensateExp.getLossQuantity();
			} else if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
				if(compensateExp.getLossQuantity() > compensateExp.getQuantity()){
					adjSumLoss = compensateExp.getSumLoss()* (compensateExp.getQuantity() -1);
				} else {
					adjSumLoss = compensateExp.getSumLoss()* (compensateExp.getLossQuantity() -1);
				}
			} else {
				adjSumLoss = (compensateExp.getSumLoss() + compensateExp.getRescueFee()  - compensateExp.getSumRest()- compensateExp.getbZPaid())* compensateExp.getLossQuantity();
			}
		}
		
		if(kindPayInfoMap.containsKey(compensateExp.getKindCode())){
			CompensateKindPay oldcompensateKindPay = (CompensateKindPay)kindPayInfoMap.get(compensateExp.getKindCode());
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(oldcompensateKindPay.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_Z.equals(oldcompensateKindPay.getKindCode())) {
				oldcompensateKindPay.setKindSum(oldcompensateKindPay.getKindSum() + sumLoss);
				oldcompensateKindPay.setKindRescueSum(oldcompensateKindPay.getKindRescueSum() + rescueFee);
				oldcompensateKindPay.setKindLossNum(oldcompensateKindPay.getKindLossNum()+1);
			} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode().trim())){//无法找到第三方特约险
				if("car".equals(compensateExp.getExpType()) || "other".equals(compensateExp.getExpType())){//车 、 其他 录入
					oldcompensateKindPay.setKindSum(oldcompensateKindPay.getKindSum() + sumLoss);
					oldcompensateKindPay.setKindRescueSum(oldcompensateKindPay.getKindRescueSum() + rescueFee);
					oldcompensateKindPay.setKindLossNum(oldcompensateKindPay.getKindLossNum()+1);
				} else if("prop".equals(compensateExp.getExpType())){//财产录入
					oldcompensateKindPay.setKindSumForX(oldcompensateKindPay.getKindSumForX() + sumLoss);
					oldcompensateKindPay.setKindRescueSumForX(oldcompensateKindPay.getKindRescueSumForX() + rescueFee);
					oldcompensateKindPay.setKindLossNumForX(oldcompensateKindPay.getKindLossNumForX()+1);
				}
			} else {
				//精神损害抚慰金责任险（三者险） 、精神损害抚慰金责任险（车上人员(司机)）、精神损害抚慰金责任险（车上人员(乘客)）
				if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) 
						|| CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) 
						|| CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode())){
					sumLoss = adjSumLoss;
					rescueFee = 0;
				}
				if(sumLoss < 0) {
					sumLoss = 0;
				}
				oldcompensateKindPay.setKindSum(oldcompensateKindPay.getKindSum() + sumLoss + rescueFee);
				oldcompensateKindPay.setAdjKindSum(oldcompensateKindPay.getAdjKindSum() + adjSumLoss);
				oldcompensateKindPay.setKindLossNum(oldcompensateKindPay.getKindLossNum()+1);
			}
			//oldcompensateKindPay.setKindLossNum(oldcompensateKindPay.getKindLossNum()+1);
			oldcompensateKindPay.addCompensateExp(compensateExp);
			kindPayInfoMap.put(compensateExp.getKindCode(), oldcompensateKindPay);
		}else{
			CompensateKindPay compensateKindPay = new CompensateKindPay();
			compensateKindPay.setKindCode(compensateExp.getKindCode());
			compensateKindPay.setDamageAmount(compensateExp.getDamageAmount());
			compensateKindPay.setUnitAmount(compensateExp.getUnitAmount());
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateKindPay.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateKindPay.getKindCode())) {
				//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。END) {
				compensateKindPay.setKindSum(sumLoss);
				compensateKindPay.setKindRescueSum(rescueFee);
				compensateKindPay.setKindLossNum(1);
			} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateKindPay.getKindCode().trim())){//无法找到第三方特约险
				if("car".equals(compensateExp.getExpType()) || "other".equals(compensateExp.getExpType())){//车 、 其他 录入
					compensateKindPay.setKindSum(sumLoss);
					compensateKindPay.setKindRescueSum(rescueFee);
					compensateKindPay.setKindLossNum(1);
				} else if("prop".equals(compensateExp.getExpType())){//财产录入
					compensateKindPay.setKindSumForX(sumLoss);
					compensateKindPay.setKindRescueSumForX(rescueFee);
					compensateKindPay.setKindLossNumForX(1);
				}
			} else {
				//精神损害抚慰金责任险（三者险） 、精神损害抚慰金责任险（车上人员(司机)）、精神损害抚慰金责任险（车上人员(乘客)）
				if ( CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) || 
						CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) 
						|| CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode())) {
					sumLoss = adjSumLoss;
					rescueFee = 0;
				}
				if(sumLoss < 0) {
					sumLoss = 0;
				}
				compensateKindPay.setKindSum(sumLoss + rescueFee);
				compensateKindPay.setAdjKindSum(adjSumLoss);
				compensateKindPay.setKindLossNum(1);
			}
			compensateKindPay.setDamageItemRealCost(compensateExp.getDamageItemRealCost());
//			compensateKindPay.setKindLossNum(1);
			compensateKindPay.addCompensateExp(compensateExp);
			kindPayInfoMap.put(compensateExp.getKindCode(), compensateKindPay);
		}
	}
	
	/**
	 * 理算报告--整理理算报告所需数据
	 */
	@SuppressWarnings("unchecked")
	public void getCompensateTextData(CompensateExp compensateExp, String payType) {
		String addFlag = "N";
		if (kindInfoList.size() == 0) {
			KindInfo kindInfo = new KindInfo();
			kindInfo.setClauseType(compensateExp.getClauseType());
			kindInfo.setKindCode(compensateExp.getKindCode());
			kindInfo.setKindName(compensateExp.getKindName());
			kindInfo.setDeductFee(compensateExp.getDeductFee());
			kindInfo.setOppoentCoverageType(compensateExp.getOppoentCoverageType());
			kindInfo.addItemInfo(compensateExp, payType);
			kindInfoList.add(kindInfo);
		} else {
			for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
				KindInfo kindInfo = (KindInfo) iter.next();
				if (compensateExp.getKindCode().equals(kindInfo.getKindCode())) {
					kindInfo.setOppoentCoverageType(compensateExp.getOppoentCoverageType());
					kindInfo.addItemInfo(compensateExp, payType);
					addFlag = "Y";
				}
			}
			if ("N".equals(addFlag)) {
				KindInfo kindInfoNew = new KindInfo();
				kindInfoNew.setClauseType(compensateExp.getClauseType());
				kindInfoNew.setKindCode(compensateExp.getKindCode());
				kindInfoNew.setKindName(compensateExp.getKindName());
				kindInfoNew.setDeductFee(compensateExp.getDeductFee());
				kindInfoNew.setOppoentCoverageType(compensateExp.getOppoentCoverageType());
				kindInfoNew.addItemInfo(compensateExp, payType);
				kindInfoList.add(kindInfoNew);
			}
		}
	}
	
	/**
	 * 理算报告--整理交强险计算书理算报告所需数据(诉讼案与旧格式一起组织)
	 */
	@SuppressWarnings("unchecked")
	public void getBZCompensateTextData_Suit(ThirdPartyLossInfo thirdPartyLossInfo, String payType) {
		String addFlag = "N";
		exgratiaFee = exgratiaFee - thirdPartyLossInfo.getSumRealPay();
		if (thirdPartyLossInfo.getIsAdd() != null && "2".equals(thirdPartyLossInfo.getIsAdd())) {
			return;
		}
		if (!"0".equals(thirdPartyLossInfo.getInsteadFlag())) {
			return;
		} else {
			if ("1".equals(thirdPartyLossInfo.getLossItemType())) {
				return;
			}
		}
		if (kindInfoList.size() == 0) {
			KindInfo kindInfo = new KindInfo();
			kindInfo.setClauseType("F41");
			kindInfo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
			kindInfo.setKindName("交强险");
			kindInfo.setDeductFee(0);
			if (this.getCompensateType() != 3) {
				kindInfo.addItemInfo(thirdPartyLossInfo, payType);
			} else {
				kindInfo.addItemInfoOfAdvance(thirdPartyLossInfo);
			}
			kindInfoList.add(kindInfo);
		} else {
			for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
				KindInfo kindInfo = (KindInfo) iter.next();
				if (CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindInfo.getKindCode())) {
					if (this.getCompensateType() != 3) {
						kindInfo.addItemInfo(thirdPartyLossInfo, payType);
					} else {
						kindInfo.addItemInfoOfAdvance(thirdPartyLossInfo);
					}
					addFlag = "Y";
				}
			}
			if ("N".equals(addFlag)) {
				KindInfo kindInfoNew = new KindInfo();
				kindInfoNew.setClauseType("F41");
				kindInfoNew.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
				kindInfoNew.setKindName("交强险");
				kindInfoNew.setDeductFee(0);
				if (this.getCompensateType() != 3) {
					kindInfoNew.addItemInfo(thirdPartyLossInfo, payType);
				} else {
					kindInfoNew.addItemInfoOfAdvance(thirdPartyLossInfo);
				}
				kindInfoList.add(kindInfoNew);
			}
		}
	}
	
	/**
	 * 理算报告--整理理算报告所需数据--M险
	 */
	@SuppressWarnings("unchecked")
	public void getCompensateTextDataM(){
		Double mSumRealPay = 0.0;
		Double mSumRealPayJud = 0.0;
		String clauseType = "";
		KindInfo kindInfoNew = new KindInfo();
		CompensateExp compensateExp = new CompensateExp();
		for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
			KindInfo kindInfo = (KindInfo) iter.next();
			clauseType = kindInfo.getClauseType();
			mSumRealPay = mSumRealPay + kindInfo.getmPay();
			//诉讼案
			if ("1".equals(kindInfo.getIsSuitFlag())) {
				mSumRealPayJud = mSumRealPayJud + kindInfo.getmPayJud();
			}
		}
		kindInfoNew.setClauseType(clauseType);
		kindInfoNew.setKindCode("M");
		kindInfoNew.setKindName("不计免赔率特约条款");
		compensateExp.setKindCode("M");
		compensateExp.setKindName("不计免赔率特约条款");
		kindInfoNew.addItemInfo(compensateExp, "");
		kindInfoNew.setItemPay(mSumRealPay);
		kindInfoNew.setItemRealPay(mSumRealPay);
		kindInfoNew.setItemSuitPay(mSumRealPayJud);
		kindInfoList.add(kindInfoNew);
	}
	
	/**
	 * 理算报告--生成理算报告
	 */
	public void createCompensateText() {
		//主险理算报告
		String compensateMainText = "";
		//主险险别名称
		String compensateMainKindName = "";
		//主险赔付金额
		String compensateMainKindNum = "";
		//附加险险理算报告
		String compensateOtherText = "";
		//附加险理算金额
		Double compensateOtherNum = 0.0;
		//总理算金额
		Double compensateNum = 0.0;
		//汇总理算报告
		//代码走查7
		StringBuffer sumCompensateText = new  StringBuffer();
		StringBuffer compensateText = new  StringBuffer();
		CompensateExp compensateExpFunc = new CompensateExp();
		for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
			KindInfo kindInfo = (KindInfo) iter.next();
			compensateNum = compensateNum + kindInfo.getItemRealPay();
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(kindInfo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_B.equals(kindInfo.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_G.equals(kindInfo.getKindCode())){
				compensateMainText = compensateMainText + kindInfo.getKindExp();
				if (compensateMainKindName.length()>0) {
					compensateMainKindName = compensateMainKindName + "+" + kindInfo.getKindName();  
					compensateMainKindNum = compensateMainKindNum + "+" + compensateExpFunc.formatPay(kindInfo.getItemRealPay());
				} else {
					compensateMainKindName = kindInfo.getKindName(); 
					compensateMainKindNum = compensateExpFunc.formatPay(kindInfo.getItemRealPay());
				}
			} else {
				//附加险要求和
				compensateOtherText = compensateOtherText + kindInfo.getKindExp();
				compensateOtherNum = compensateOtherNum + kindInfo.getItemRealPay();
			} 
		}
		sumCompensateText.append("\n本案理算金额=");
		if (compensateMainText.length() > 0) {
			sumCompensateText.append(compensateMainKindName);
		}
		if (compensateOtherText.length() > 0) {
			if (compensateMainText.length() > 0) {
				sumCompensateText.append("+ 附加险赔款").append('\n');
			} else {
				sumCompensateText.append("附加险赔款").append('\n');
			}
		} else {
			sumCompensateText.append('\n');
		}
		if (compensateMainText.length() > 0) {
			sumCompensateText.append("\t=").append(compensateMainKindNum);
		}
		if (compensateOtherText.length() > 0) {
			
			if (compensateMainText.length() > 0) {
				sumCompensateText.append("+ ").append(compensateExpFunc.formatPay(compensateOtherNum)).append('\n');
				sumCompensateText.append("\t=").append(compensateExpFunc.formatPay(compensateNum));
			} else {
				sumCompensateText.append("\t=").append(compensateExpFunc.formatPay(compensateNum)).append('\n');
			}
		}
		compensateText.append(compensateMainText).append(compensateOtherText).append(sumCompensateText);
		for (Iterator iter = returnCompensateList.iterator(); iter.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iter.next();
			compensateExp.setOldCompensateExp(compensateText.toString());
		}
	}
	
	/**
	 * 理算报告--生成理算报告(诉讼案)
	 */
	public void createCompensateText_Suit() {
		//主险理算报告
		String compensateMainText = "";
		//主险险别名称
		String compensateMainKindName = "";
		//主险赔付金额
		String compensateMainKindNum = "";
		//附加险险理算报告
		String compensateOtherText = "";
		//附加险理算金额
		Double compensateOtherNum = 0.0;
		//总理算金额
		Double compensateNum = 0.0;
		//汇总理算报告
		String sumCompensateText = "";
		String compensateText = "";
		CompensateExp compensateExpFunc = new CompensateExp();
		for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
			KindInfo kindInfo = (KindInfo) iter.next();
			compensateNum = compensateNum + kindInfo.getItemSuitPay();
			
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(kindInfo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_B.equals(kindInfo.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_G.equals(kindInfo.getKindCode())) {
				compensateMainText = compensateMainText + kindInfo.getKindSuitExp();
				if (compensateMainKindName.length()>0) {
					compensateMainKindName = compensateMainKindName + "+" + kindInfo.getKindName();  
					compensateMainKindNum = compensateMainKindNum + "+" + compensateExpFunc.formatPay(kindInfo.getItemSuitPay());
				} else {
					compensateMainKindName = kindInfo.getKindName(); 
					compensateMainKindNum = compensateExpFunc.formatPay(kindInfo.getItemSuitPay());
				}
			} else {
				//附加险要求和
				compensateOtherText = compensateOtherText + kindInfo.getKindSuitExp();
				compensateOtherNum = compensateOtherNum + kindInfo.getItemSuitPay();
			} 
			//诉讼案裁定金额
			exgratiaFee += kindInfo.getmPayJud() - kindInfo.getmPay();
		}
		sumCompensateText = "\n本案裁定金额=";
		if (compensateMainText.length() > 0) {
			sumCompensateText = sumCompensateText + compensateMainKindName;
		}
		if (compensateOtherText.length() > 0) {
			if (compensateMainText.length() > 0) {
				sumCompensateText = sumCompensateText + "+ 附加险赔款" + "\n";
			} else {
				sumCompensateText = sumCompensateText + "附加险赔款" + "\n";
			}
		} else {
			sumCompensateText = sumCompensateText + "\n";
		}
		if (compensateMainText.length() > 0) {
			sumCompensateText = sumCompensateText + "\t=" + compensateMainKindNum;
		}
		if (compensateOtherText.length() > 0) {
			
			if (compensateMainText.length() > 0) {
				sumCompensateText = sumCompensateText + "+ " + compensateExpFunc.formatPay(compensateOtherNum) + "\n";
				sumCompensateText = sumCompensateText + "\t=" + compensateExpFunc.formatPay(compensateNum);
			} else {
				sumCompensateText = sumCompensateText + "\t=" + compensateExpFunc.formatPay(compensateNum) + "\n";
			}
		}
		compensateText = compensateMainText + compensateOtherText + sumCompensateText;
		for (Iterator iter = returnCompensateList.iterator(); iter.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iter.next();
			compensateExp.setSuitCompensateExp(compensateText);
		}
	}
	
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	
	/**
	 * 理算报告--生成理算报告(交强计算书旧格式)
	 */
	public void createBZCompensateText() {
		//主险理算报告
		String bzCompensateMainText = "";
		//主险险别名称
		String bzCompensateMainKindName = "";
		//主险赔付金额
		String bzCompensateMainKindNum = "";
		//附加险险理算报告
		String bzCompensateOtherText = "";
		//附加险理算金额
		//Double bzCompensateOtherNum = 0.0;
		//总理算金额
		Double bzCompensateNum = 0.0;
		//汇总理算报告
		//代码走查7
		StringBuffer bzCompensateText = new  StringBuffer();
		CompensateExp compensateExpFunc = new CompensateExp();
		for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
			KindInfo kindInfo = (KindInfo) iter.next();
			bzCompensateNum = bzCompensateNum + kindInfo.getItemRealPay();
			if (CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindInfo.getKindCode())) {
				bzCompensateMainText = bzCompensateMainText + kindInfo.getKindExp();
				if (bzCompensateMainKindName.length()>0) {
					bzCompensateMainKindName = bzCompensateMainKindName + "+" + kindInfo.getKindName();  
					bzCompensateMainKindNum = bzCompensateMainKindNum + "+" + compensateExpFunc.formatPay(kindInfo.getItemRealPay());
				} else {
					bzCompensateMainKindName = kindInfo.getKindName(); 
					bzCompensateMainKindNum = compensateExpFunc.formatPay(kindInfo.getItemRealPay());
				}
			}
		}
		bzCompensateText.append(bzCompensateMainText).append(bzCompensateOtherText);
		for (Iterator iter = returnCompensateList.iterator(); iter.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iter.next();
			for(Iterator it = compensateExp.getThirdPartyLossInfos().iterator();it.hasNext();){
				ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo)it.next();
				thirdPartyLossInfo.setOldCompensateExp(bzCompensateText.toString());
			}
		}
	}
	
	/**
	 * 理算报告--生成交强计算书理算报告(诉讼案)
	 */
	public void createBZCompensateText_Suit() {
		//主险理算报告
		String bzCompensateMainText = "";
		//主险险别名称
		String bzCompensateMainKindName = "";
		//主险赔付金额
		String bzCompensateMainKindNum = "";
		//附加险险理算报告
		String bzCompensateOtherText = "";
		//附加险理算金额
		Double bzCompensateOtherNum = 0.0;
		//总理算金额
		Double bzCompensateNum = 0.0;
		//汇总理算报告
		String bzSumCompensateText = "";
		String bzCompensateText = "";
		CompensateExp compensateExpFunc = new CompensateExp();
		for (Iterator iter = kindInfoList.iterator(); iter.hasNext();) {
			KindInfo kindInfo = (KindInfo) iter.next();
			bzCompensateNum = bzCompensateNum + kindInfo.getItemSuitPay();
			
			if (CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindInfo.getKindCode())) {
				bzCompensateMainText = bzCompensateMainText + kindInfo.getKindSuitExp();
				if (bzCompensateMainKindName.length()>0) {
					bzCompensateMainKindName = bzCompensateMainKindName + "+" + kindInfo.getKindName();  
					bzCompensateMainKindName = bzCompensateMainKindNum + "+" + compensateExpFunc.formatPay(kindInfo.getItemSuitPay());
				} else {
					bzCompensateMainKindName = kindInfo.getKindName(); 
					bzCompensateMainKindNum = compensateExpFunc.formatPay(kindInfo.getItemSuitPay());
				}
			} else {
				//附加险要求和
				bzCompensateOtherText = bzCompensateOtherText + kindInfo.getKindSuitExp();
				bzCompensateOtherNum = bzCompensateOtherNum + kindInfo.getItemSuitPay();
			} 
			//诉讼案裁定金额
			exgratiaFee += kindInfo.getmPayJud() - kindInfo.getmPay();
		}
		bzSumCompensateText = "本案裁定金额=";
		if (bzCompensateMainText.length() > 0) {
			bzSumCompensateText = bzSumCompensateText + bzCompensateMainKindName;
		}
		if (bzCompensateMainText.length() > 0) {
			bzSumCompensateText = bzSumCompensateText + "\n\t=" + bzCompensateMainKindNum;
		}
		if (bzCompensateOtherText.length() > 0) {
			if (bzCompensateMainText.length() > 0) {
				bzSumCompensateText = bzSumCompensateText + " + " + compensateExpFunc.formatPay(bzCompensateOtherNum) + "\n";
				bzSumCompensateText = bzSumCompensateText + "\t=" + compensateExpFunc.formatPay(bzCompensateNum);
			} else {
				bzSumCompensateText = bzSumCompensateText + "\t=" + compensateExpFunc.formatPay(bzCompensateNum) + "\n";
			}
		}
		if (bzCompensateMainText.length() > 0) {
			bzCompensateText = bzCompensateMainText + bzSumCompensateText + "\n";
		}
		for (Iterator iter = returnCompensateList.iterator(); iter.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iter.next();
			for(Iterator it = compensateExp.getThirdPartyLossInfos().iterator();it.hasNext();){
				ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo)it.next();
				thirdPartyLossInfo.setSuitCompensateExp(bzCompensateText);
			}
		}
	}
	
	/**
	 * 将险别赔付信息MAP复制给险别赔付信息LIST
	 */
	@SuppressWarnings("unchecked")
	public void copyMapToList() {
		Iterator iter = kindPayInfoMap.keySet().iterator();
		ArrayList kindList = new ArrayList();
		while (iter.hasNext()) {
			String str = (String) iter.next();
			kindList.add(kindPayInfoMap.get(str));
		}
		returnKindPayList = kindList;
	}
	
	/**
	 * 返回填充n个空格的字符串
	 *
	 * @param n
	 * @return
	 */
	@SuppressWarnings("unused")
	private String space(int n) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < n; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	/**
	 * 将不需要筛选的List拷贝到需要筛选的List中
	 */
	public void deleteReturnBZCompensateList(){
		returnBZCompensateList.clear();
	}
	
	/**
	 * 将同一个人下面的理算金额 (*费用-交强险已赔付金额)×事故责任比例 保存到list中
	 */
	@SuppressWarnings("unchecked")
	public void savePersonLossToList(double sum) {
		personLossList.add(sum);
	}

	/**
	 * 按人员序号将不同人员的理算金额list保存到map中
	 */
	@SuppressWarnings("unchecked")
	public void savePersonLossListToMap(int no) {
		personLossListMap.put(no, personLossList);
		personLossList.clear();
	}
	
	/**
	 * 判断一个人员下的费用信息是否超限额,并返回赔付比例
	 * @return outQuota
	 */
	public double judgeQuota(CompensateExp compensateExp,double unitAmount){
		double outQuota = 1;
		int no = compensateExp.getIntLossIndex();
		int index = compensateExp.getLossNumPerPerson();
		double sum = 0;
		if(personLossArray != null && personLossArray[no] != null && personLossArray[no].length >0){
			for(double money:personLossArray[no]){
				sum += money;
			}
		}
		if(sum > unitAmount){
			double money = personLossArray[no][index];
			outQuota = money/sum;
		}
		compensateExp.setTempMoney(sum);//暂存一个人下面的理算金额
		return outQuota;
	}
	
	/**
	 * 判断一个人员下的费用信息是否超限额,并返回超限额标志位
	 * @return overFlag
	 */
	public String judgeOverFlag(CompensateExp compensateExp,double unitAmount){
		String  overFlag = "";
		int no = compensateExp.getIntLossIndex();
		double sum = 0;
		if(personLossArray != null && personLossArray[no] != null && personLossArray[no].length >0){
			for(double money:personLossArray[no]){
				sum += money;
			}
		}
		if(sum > unitAmount){
			overFlag = "1";
		}
		return overFlag;
	}
	
	/**
	 * 组织D1险人员损失数据
	 *
	 */
	public void organizePersonData(CompensateExp compensateExp){
		int intLossIndex = compensateExp.getIntLossIndex();
		int lossNumPerPerson = compensateExp.getLossNumPerPerson();
		int lossAmountPerPerson = compensateExp.getLossAmountPerPerson();
		if(personLossArray == null){
			personLossArray = new double[lossPersonsNum][];
		}
		if(personLossArray[intLossIndex] == null){
			personLossArray[intLossIndex] = new double[lossAmountPerPerson];
		}
		
		personLossArray[intLossIndex][lossNumPerPerson]
		    = (compensateExp.getSumLoss() - compensateExp.getbZPaid()) * compensateExp.getIndemnityDutyRate() * (1-compensateExp.getAbsrate()/100) ;
	}
	
	private boolean equalsKindCode(String kindCode, String becompared) {
		boolean isEquals = false;
		if(kindCode == null || "".equals(kindCode.trim())
				|| becompared == null || "".equals(becompared.trim())){
			isEquals = false;
		}else{
			String kindCodeValue = kindCode.trim();
			String becomparedValue = becompared.trim();
			
			if(kindCode.trim().equalsIgnoreCase(becompared.trim())){
				isEquals =  true;
			}else if(kindCodeValue != null && kindCodeValue.trim().equalsIgnoreCase(becompared.trim())){
				isEquals =  true;
			}else if(becomparedValue != null && becomparedValue.trim().equalsIgnoreCase(kindCode.trim())){
				isEquals =  true;
			}else if(kindCodeValue != null && becomparedValue != null 
					&& kindCodeValue.trim().equalsIgnoreCase(becomparedValue.trim())){
				isEquals =  true;
			}
			String kindCodeStr = "D1|S|Y3|K3|R";
			if(kindCodeStr.contains(kindCode.trim())){
				becomparedValue = becompared.trim();
			}else if(kindCodeStr.contains(becompared.trim())){
				kindCodeValue = kindCode.trim();
			}			
			if(kindCode.trim().equalsIgnoreCase(becompared.trim())){
				isEquals =  true;
			}else if(kindCodeValue != null && kindCodeValue.trim().equalsIgnoreCase(becompared.trim())){
				isEquals =  true;
			}else if(becomparedValue != null && becomparedValue.trim().equalsIgnoreCase(kindCode.trim())){
				isEquals =  true;
			}else if(kindCodeValue != null && becomparedValue != null 
					&& kindCodeValue.trim().equalsIgnoreCase(becomparedValue.trim())){
				isEquals =  true;
			}
		}
		
		return isEquals;
	}
	
	public int getListAmount() {
		return listAmount;
	}
	
	public void setListAmount(int listAmount) {
		this.listAmount = listAmount;
	}
	
	public int getListNum() {
		return listNum;
	}
	
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	
	public int getCompensateType() {
		return compensateType;
	}
	
	public void setCompensateType(int compensateType) {
		this.compensateType = compensateType;
	}
	
	public String getIsSuitFlag() {
		return isSuitFlag;
	}
	
	public void setIsSuitFlag(String isSuitFlag) {
		this.isSuitFlag = isSuitFlag;
	}
	
	public double getExgratiaFee() {
		return exgratiaFee;
	}
	
	public void setExgratiaFee(double exgratiaFee) {
		this.exgratiaFee = exgratiaFee;
	}
	
	public List getCompensateList() {
		return compensateList;
	}
	
	public void setCompensateList(List compensateList) {
		this.compensateList = compensateList;
	}
	
	public List getReturnCompensateList() {
		return returnCompensateList;
	}
	
	public void setReturnCompensateList(List returnCompensateList) {
		this.returnCompensateList = returnCompensateList;
	}
	
	public List getKindPayInfoList() {
		return kindPayInfoList;
	}
	
	public void setKindPayInfoList(List kindPayInfoList) {
		this.kindPayInfoList = kindPayInfoList;
	}
	
	public Map getKindPayInfoMap() {
		return kindPayInfoMap;
	}
	
	public void setKindPayInfoMap(Map kindPayInfoMap) {
		this.kindPayInfoMap = kindPayInfoMap;
	}
	
	public Map getKindLossTextMap() {
		return kindLossTextMap;
	}
	
	public void setKindLossTextMap(Map kindLossTextMap) {
		this.kindLossTextMap = kindLossTextMap;
	}
	
	public List getReturnBZCompensateList() {
		return returnBZCompensateList;
	}
	
	public void setReturnBZCompensateList(List returnBZCompensateList) {
		this.returnBZCompensateList = returnBZCompensateList;
	}
	
	public List getReturnKindPayList() {
		return returnKindPayList;
	}
	
	public void setReturnKindPayList(List returnKindPayList) {
		this.returnKindPayList = returnKindPayList;
	}
	
	public List getPersonDamageInfoList() {
		return PersonDamageInfoList;
	}
	
	public void setPersonDamageInfoList(List personDamageInfoList) {
		PersonDamageInfoList = personDamageInfoList;
	}
	
	public List getPersonLossList() {
		return personLossList;
	}
	
	public void setPersonLossList(List personLossList) {
		this.personLossList = personLossList;
	}
	
	public Map getPersonLossListMap() {
		return personLossListMap;
	}
	
	public void setPersonLossListMap(Map personLossListMap) {
		this.personLossListMap = personLossListMap;
	}
	
	public int getLossIndex() {
		return lossIndex;
	}
	
	public void setLossIndex(int lossIndex) {
		this.lossIndex = lossIndex;
	}
	
	public List getKindInfoList() {
		return kindInfoList;
	}
	
	public void setKindInfoList(List kindInfoList) {
		this.kindInfoList = kindInfoList;
	}
	
	public int getLossPersonsNum() {
		return lossPersonsNum;
	}
	
	public void setLossPersonsNum(int lossPersonsNum) {
		this.lossPersonsNum = lossPersonsNum;
	}
	
	public double[][] getPersonLossArray() {
		return personLossArray;
	}
	
	public void setPersonLossArray(double[][] personLossArray) {
		this.personLossArray = personLossArray;
	}
	
	public int getDutyCarNum() {
		return dutyCarNum;
	}
	
	public void setDutyCarNum(int dutyCarNum) {
		this.dutyCarNum = dutyCarNum;
	}
	
	public String getBzDutyCar() {
		return bzDutyCar;
	}
	
	public void setBzDutyCar(String bzDutyCar) {
		this.bzDutyCar = bzDutyCar;
	}
	
	public String getPayType() {
		return payType;
	}
	
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public double getSubrogationBRealPay() {
		return subrogationBRealPay;
	}
	
	public void setSubrogationBRealPay(double subrogationBRealPay) {
		this.subrogationBRealPay = subrogationBRealPay;
	}
	
	public double getSubrogationBzRealPay() {
		return subrogationBzRealPay;
	}
	
	public void setSubrogationBzRealPay(double subrogationBzRealPay) {
		this.subrogationBzRealPay = subrogationBzRealPay;
	}
	
	public List getThirdPartyRecoveryInfoAry() {
		return thirdPartyRecoveryInfoAry;
	}
	
	public void setThirdPartyRecoveryInfoAry(List thirdPartyRecoveryInfoAry) {
		this.thirdPartyRecoveryInfoAry = thirdPartyRecoveryInfoAry;
	}
	
	public PrpLCItemKindVo getPrpLCItemKindVo() {
		return prpLCItemKindVo;
	}
	
	public void setPrpLCItemKindVo(PrpLCItemKindVo prpLCItemKindVo) {
		this.prpLCItemKindVo = prpLCItemKindVo;
	}
	
	public ClaimFeeCondition getClaimFeeCondition() {
		return claimFeeCondition;
	}
	
	public void setClaimFeeCondition(ClaimFeeCondition claimFeeCondition) {
		this.claimFeeCondition = claimFeeCondition;
	}
	
	public String getIsBiPCi() {
		return isBiPCi;
	}
	
	public void setIsBiPCi(String isBiPCi) {
		this.isBiPCi = isBiPCi;
	}
	
	public double getSumRealPayForA() {
		return sumRealPayForA;
	}
	
	public void setSumRealPayForA(double sumRealPayForA) {
		this.sumRealPayForA = sumRealPayForA;
	}
	
	public double getSumRealPayForAM() {
		return sumRealPayForAM;
	}
	
	public void setSumRealPayForAM(double sumRealPayForAM) {
		this.sumRealPayForAM = sumRealPayForAM;
	}
	public String getCalculateType() {
		return calculateType;
	}
	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}
	public Map<String,Double> getLeftAmountMap() {
		return leftAmountMap;
	}
	public void setLeftAmountMap(Map<String,Double> leftAmountMap) {
		this.leftAmountMap = leftAmountMap;
	}
	public String getReCaseFlag() {
		return reCaseFlag;
	}
	public void setReCaseFlag(String reCaseFlag) {
		this.reCaseFlag = reCaseFlag;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public List<PrpLCheckDutyVo> getCheckDutyList() {
		return checkDutyList;
	}
	public void setCheckDutyList(List<PrpLCheckDutyVo> checkDutyList) {
		this.checkDutyList = checkDutyList;
	}
	public BigDecimal getCarLossRate() {
		return carLossRate;
	}
	public void setCarLossRate(BigDecimal carLossRate) {
		this.carLossRate = carLossRate;
	}
}
