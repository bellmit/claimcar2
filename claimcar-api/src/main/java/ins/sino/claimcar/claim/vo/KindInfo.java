package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.CodeConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.dubbo.remoting.Codec;

public class KindInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 条款代码*/
	private String clauseType;
	/** 险别代码*/
	private String kindCode;
	/** 险别名称*/
	private String kindName;
	/** 不计免赔金额*/
	private Double mPay = 0.0;
	/** 不计免赔金额(诉讼)*/
	private Double mPayJud = 0.0;
	/** 费用List*/
	private List feeList = new ArrayList();
	/** 费用List*/
	private List compensateExpList = new ArrayList();
	/** 损失项List*/
	private List itemInfoList = new ArrayList();
	/** 险别中文描述*/
	private String kindText;
	/** 损失标志位 Person 人员;Other 其它*/
	private String lossFlag = "";
	/** 费用中文表达式*/
	private String feeExp = "";
	/** 费用数字表达式*/
	private String feeDigitExp = "";
	/** 赔付金额*/
	private Double itemPay = 0.0;
	/** 实赔金额*/
	private Double itemRealPay = 0.0;
	/** 诉讼金额*/
	private Double itemSuitPay = 0.0;
	/** 保留两位的实赔金额*/
	private Double item2RealPay = 0.0;
	/** 实赔金额表达式*/
	private String itemRealPayString = "";
	/** 赔付金额表达式*/
	private String itemPayExp = "";
	/** 实赔金额表达式(超限额)*/
	private String itemRealPayExp = "";
	/** 险别理算公式*/
	private String kindExp = "";
	/** 险别理算公式(诉讼案)*/
	private String kindSuitExp = "";
	/** 损失项理算公式*/
	private String itemExp = "";
	/** 损失项理算公式(诉讼案)*/
	private String itemSuitExp = "";
	/** 本项理算金额中文表达式*/
	private String compensateExp = "";
	/** 本项理算金额数字表达式*/
	private String compensateDigitExp = "";
	/** 损失项中文表达式*/
	private String lossExp = "";
	/** 损失项数字表达式*/
	private String lossDigitExp = "";
	/** 超限额调整标志位*/
	private String claimRateFlag = "";
	/** 免赔率*/
	private double deductibleRate = 0.0;
	/** 免赔率公式*/
	private String deductibleRateText = "";
	/** 总的交强险已赔金额*/
	private double bZPaid;
	/** 超限额标志位*/
	private String overFlag;
	/** 诉讼案标志位*/
	private String isSuitFlag;
	/** 免赔额 */
	private double deductFee;
	/** 为了交强计算书一分钱调整 */
	private List thirdPartyLossInfoList = new ArrayList();
	
	/**
	 * 代位(交强/商业)类型
	 * 1:代付交强 2:代付商业
	 */
	private String oppoentCoverageType;
	
	/**
	 * 数字文本合并 合成compensateDigitExp
	 * @return
	 */
	public String combDigitExp(String digitExp) {
		String combcompensateDigitExp = "";
		if ("".equals(compensateDigitExp)) {
			combcompensateDigitExp = compensateDigitExp + digitExp;
		} else {
			combcompensateDigitExp = compensateDigitExp + " + " + digitExp;
		}
		return combcompensateDigitExp;
	}
	
	/**
	 * 组织险别信息
	 */
	@SuppressWarnings("unchecked")
	public void addItemInfo(CompensateExp compensateExp, String payType) {
		boolean flag = false;
		if(compensateExp.getRiskCode() != "1101"){
			flag = CodeConstants.ISNEWCLAUSECODE_MAP.get(compensateExp.getRiskCode().trim());
		}
		/** 人员*/
		deductibleRate = compensateExp.getDeductibleRate();
		deductibleRateText = compensateExp.getDeductibleRateText();
		//诉讼案
		isSuitFlag = compensateExp.getIsSuitFlag();
		if (compensateExp.getLossIndex() != null ||CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) || 	 
			CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || 	 
			CodeConstants.KINDCODE.KINDCODE_R.equals(compensateExp.getKindCode())) {
			lossFlag = "Person";
			/** 解决一分钱的问题*/
			if (compensateExp.getItemSumRealPay() >= 0) {
				itemPay = itemPay + compensateExp.getItemSumRealPay();
			} else {
				itemPay = itemPay + compensateExp.getSumRealPay();
			}
			itemRealPay = itemRealPay + compensateExp.getSumRealPay();
			double sumCarLoss = compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getOtherDeductFee() + compensateExp.getRescueFee();
			if (payType != null && "2".equals(payType.trim()) && sumCarLoss != 0) {// 代位
				itemPay = itemPay * compensateExp.getRecoveryPay() / sumCarLoss;
			}
			//诉讼案
			if ("1".equals(compensateExp.getIsSuitFlag())) {
				itemSuitPay = itemSuitPay + compensateExp.getSumRealPayJud();
			}
			Double tmpItem2RealPay = new BigDecimal(compensateExp.getSumRealPay()).setScale(2,4).doubleValue();
			item2RealPay = item2RealPay + tmpItem2RealPay;
			
			String addFlag = "N";
			if (compensateExp.getClaimRate() >= 0) {
				claimRateFlag = "Y";
			} else {
				claimRateFlag = "N";
			}
			if (itemInfoList.size() == 0) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setLossIndex(compensateExp.getLossIndex());
				itemInfo.setLossName(compensateExp.getLicenseNo());
				itemInfo.setSumCarLoss(compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getOtherDeductFee() + compensateExp.getRescueFee()); // 总损失金额
				itemInfo.setRecoveryPay(compensateExp.getRecoveryPay()); // 责任对方追偿金额
				if (compensateExp.getItemSumRealPay() >= 0) {
					itemInfo.setItemPay(compensateExp.getItemSumRealPay());
				} else {
					itemInfo.setItemPay(compensateExp.getSumRealPay());
				}
				itemInfo.setItemRealPay(compensateExp.getSumRealPay());
				// 诉讼案
				if ("1".equals(compensateExp.getIsSuitFlag())) {
					itemInfo.setItemSuitPay(compensateExp.getSumRealPayJud());
				}
				itemInfo.addItemInfo(compensateExp);
				itemInfoList.add(itemInfo);
			} else {
				for (Iterator iter = itemInfoList.iterator(); iter.hasNext();) {
					ItemInfo itemInfo = (ItemInfo) iter.next();
					if (compensateExp.getLossIndex() != null) {
						if (compensateExp.getLossIndex().equals(itemInfo.getLossIndex())) {
							itemInfo.setSumCarLoss(compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getOtherDeductFee() + compensateExp.getRescueFee()); // 总损失金额
							itemInfo.setRecoveryPay(compensateExp.getRecoveryPay()); // 责任对方追偿金额
							itemInfo.addItemInfo(compensateExp);
							if (compensateExp.getItemSumRealPay() >= 0) {
								itemInfo.setItemPay(itemInfo.getItemPay() + compensateExp.getItemSumRealPay());
							} else {
								itemInfo.setItemPay(itemInfo.getItemPay() + compensateExp.getSumRealPay());
							}
							itemInfo.setItemRealPay(itemInfo.getItemRealPay() + compensateExp.getSumRealPay());
							// 诉讼案
							if ("1".equals(compensateExp.getIsSuitFlag())) {
								itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + compensateExp.getSumRealPayJud());
							}
							addFlag = "Y";
						}
					}
				}
				if ("N".equals(addFlag)) {
					ItemInfo itemInfo = new ItemInfo();
					itemInfo.setLossIndex(compensateExp.getLossIndex());
					itemInfo.setLossName(compensateExp.getLicenseNo());
					itemInfo.setSumCarLoss(compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getOtherDeductFee() + compensateExp.getRescueFee()); // 总损失金额
					itemInfo.setRecoveryPay(compensateExp.getRecoveryPay()); // 责任对方追偿金额
					itemInfo.addItemInfo(compensateExp);
					if (compensateExp.getItemSumRealPay() >= 0) {
						itemInfo.setItemPay(itemInfo.getItemPay() + compensateExp.getItemSumRealPay());
					} else {
						itemInfo.setItemPay(itemInfo.getItemPay() + compensateExp.getSumRealPay());
					}
					itemInfo.setItemRealPay(itemInfo.getItemRealPay() + compensateExp.getSumRealPay());
					//诉讼案
					if ("1".equals(compensateExp.getIsSuitFlag())) {
						itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + compensateExp.getSumRealPayJud());
					}
					itemInfoList.add(itemInfo);
				}
			}
			compensateExpList.add(compensateExp);
			if ("1".equals(compensateExp.getFlagOfM())) {
				mPay = mPay + compensateExp.getMsumRealPay();
				//诉讼案
				if ("1".equals(compensateExp.getIsSuitFlag())) {
					//诉讼调整后的不计免赔是按险别调整的，故一个险别只记录一个不计免赔的诉讼金额
					mPayJud = compensateExp.getMsumRealPayJud();
				}
			}
		} else {
			lossFlag = "Other";
			/** 解决一分钱的问题*/
			if (compensateExp.getItemSumRealPay() >= 0) {
				itemPay = itemPay + compensateExp.getItemSumRealPay();
			} else {
				itemPay = itemPay + compensateExp.getSumRealPay();
			}
			itemRealPay = itemRealPay + compensateExp.getSumRealPay();
			double sumCarLoss = compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getOtherDeductFee() + compensateExp.getRescueFee();
			if (payType != null && "2".equals(payType.trim()) && sumCarLoss != 0) {// 代位
				itemPay = itemPay * compensateExp.getRecoveryPay() / sumCarLoss;
			}
			// 诉讼案
			if ("1".equals(compensateExp.getIsSuitFlag())) {
				itemSuitPay = itemSuitPay + compensateExp.getSumRealPayJud();
			}
			Double tmpItem2RealPay = new BigDecimal(compensateExp.getSumRealPay()).setScale(2,4).doubleValue();
			item2RealPay = item2RealPay + tmpItem2RealPay;
			
			if (compensateExp.getClaimRate() >= 0) {
				claimRateFlag = "Y";
			} else {
				claimRateFlag = "N";
			}
			
			if ("".equals(lossExp)) {
				if (compensateExp.getSumLoss() != 0.0 || compensateExp.getOtherDeductFee() != 0.0) {
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode())){
						String lossName = compensateExp.getFeeTypeName();
						if(lossName == null || "".equals(lossName)){
							lossName = "损失金额";
						}
						
						if(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) {
							if ((compensateExp.getSumLoss() - compensateExp.getSumRest()) < compensateExp.getDamageAmount()) {
								lossExp = lossExp + lossName + "- 残值";
							} else {
								lossExp = lossExp + "保险保额" ;
							}
						} else {
							if (compensateExp.getDamageItemRealCost() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageItemRealCost() 
									|| (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) && flag)) {
									lossExp = lossExp + lossName;
								} else {
									lossExp = lossExp + "出险时车辆实际价值";
								}
							} else {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + lossName;
								} else {
									lossExp = lossExp + "保险保额";
								}
							}
						}
					} else if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
						
						if(flag){
							if (compensateExp.getSumLoss() < compensateExp.getDamageAmount()) {
								lossExp = lossExp + compensateExp.getFeeTypeName() + "-交强已赔付";
							} else {
								lossExp = lossExp + "保险保额"+ "-交强已赔付";;
							}
						} else {
							if (compensateExp.getDeviceActualValue() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDeviceActualValue()) {
									lossExp = lossExp + compensateExp.getFeeTypeName()+ "-交强已赔付";
								} else {
									lossExp = lossExp + "出险时新增设备实际价值"+ "-交强已赔付";
								}
							} else {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + compensateExp.getFeeTypeName()+ "-交强已赔付";
								} else {
									lossExp = lossExp + "保险保额";
								}
							}
						}
						
					} else if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
						if(flag){
							lossExp = lossExp + "["+compensateExp.getFeeTypeName()+"]"+"约定日赔偿金额";
						}
					} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode())){
						lossExp = lossExp + "定损金额 × 0.3";
					} else {
						lossExp = lossExp + compensateExp.getFeeTypeName();
					}
					if (compensateExp.getRescueFee() != 0.0 && !"050441".equals(compensateExp.getKindCode())) {
						lossExp = lossExp + " + " + "施救费";
					}
				} else if (compensateExp.getRescueFee() != 0.0 && !"050441".equals(compensateExp.getKindCode())) {
					lossExp = lossExp + "施救费";
				}
				if (compensateExp.getSumRest() > 0.0 && 
				   (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_D11.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_D12.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_E.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_Z1.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_Z2.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_S.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_U.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_X2.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_NY.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_Y.equals(compensateExp.getKindCode()) || 
					CodeConstants.KINDCODE.KINDCODE_K1.equals(compensateExp.getKindCode()))||CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode()) ||
					(CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) && flag)) {
					if(!(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag)
							&& !(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)){
						if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
							if (compensateExp.getDeviceActualValue() > compensateExp.getDamageAmount()) {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + " - " + "残值";
								}
							} else {
								lossExp = lossExp + " - " + "残值";
							}
						} else {
							lossExp = lossExp + " - " + "残值";
						}
					}
				}
				if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag){
					lossExp = lossExp + " × " + "数量/天数 - 一天的赔偿金额";
				}
			} else {
				if (compensateExp.getSumLoss() != 0.0 || compensateExp.getOtherDeductFee() != 0.0) {
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode())) {
						if(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) {
							if ((compensateExp.getSumLoss() - compensateExp.getSumRest()) < compensateExp.getDamageAmount()) {
								lossExp = lossExp + " + " + compensateExp.getFeeTypeName() + "- 残值";
							} else {
								lossExp = lossExp + " + " + "保险保额" ;
							}
						} else {
							if (compensateExp.getDamageItemRealCost() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageItemRealCost()
										|| (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) && flag)) {
									lossExp = lossExp + " + " + compensateExp.getFeeTypeName();
								} else {
									lossExp = lossExp + " + " + "出险时车辆实际价值";
								}
							} else {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + " + " + compensateExp.getFeeTypeName();
								} else {
									lossExp = lossExp + " + " + "保险保额";
								}
							}
						}
					} else if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
						if(flag){
							if (compensateExp.getSumLoss() < compensateExp.getDamageAmount()) {
								lossExp = lossExp +  " + " +compensateExp.getFeeTypeName()+ "-交强已赔付";
							} else {
								lossExp = lossExp +  " + " +"保险保额"+ "-交强已赔付";
							}
						} else {
							if (compensateExp.getDeviceActualValue() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDeviceActualValue() ) {
									lossExp = lossExp + " + " + compensateExp.getFeeTypeName()+ "-交强已赔付";
								} else {
									lossExp = lossExp + " + " + "出险时新增设备实际价值"+ "-交强已赔付";
								}
							} else {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + " + " + compensateExp.getFeeTypeName()+ "-交强已赔付";
								} else {
									lossExp = lossExp + " + " + "保险保额";
								}
							}
						}
						
					} else if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag){
						lossExp = lossExp + "+ ["+compensateExp.getFeeTypeName()+"]"+"约定日赔偿金额";
					} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode())){
						lossExp = lossExp + "+ 定损金额 × 0.3";
					}  else {
						lossExp = lossExp + " + " + compensateExp.getFeeTypeName();
					}
				} else if (compensateExp.getRescueFee() != 0.0 && !(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)) {
					lossExp = lossExp + " + " + "施救费";
				}
				if (compensateExp.getSumRest() > 0.0 && 
				   (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D11.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D12.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_E.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z1.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_S.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_U.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_NY.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Y.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_L.equals(compensateExp.getKindCode()))|| 
					CodeConstants.KINDCODE.KINDCODE_K1.equals(compensateExp.getKindCode())||
					CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode())||
					CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode())||
					(CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) && flag)) {
					if(!(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) && 
							!(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)){
						if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
							if (compensateExp.getDeviceActualValue() > compensateExp.getDamageAmount()) {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossExp = lossExp + " - " + "残值";
								}
							} else {
								lossExp = lossExp + " - " + "残值";
							}
						} else {
							lossExp = lossExp + " - " + "残值";
						}
					}
				}
				if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag){
					lossExp = lossExp + " × " + "数量/天数 - 一天的赔偿金额";
//					if(compensateExp.getLossQuantity() > compensateExp.getQuantity()){
//						lossExp = lossExp + " × " + "最高赔偿天数 - 一天的赔偿金额";
//					} else {
//					}
				}
			}
			if ("".equals(lossDigitExp)) {
				if (compensateExp.getSumLoss() != 0.0 || compensateExp.getOtherDeductFee() != 0.0) {
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || 
							CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode())) {
						if(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) {
							if ((compensateExp.getSumLoss() - compensateExp.getSumRest()) < compensateExp.getDamageAmount()) {
								lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss()) + "-" + compensateExp.formatPay(compensateExp.getSumRest());
							} else {
								lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDamageAmount());
							}
						} else {
							if (compensateExp.getDamageItemRealCost() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageItemRealCost()
										|| (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) && flag)) {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
								} else {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDamageItemRealCost());
								}
							} else {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
								} else {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDamageAmount());
								}
							}
						}
					} else if (equalsKindCode("X", compensateExp.getKindCode()) ) {
						if(flag){
							if (compensateExp.getSumLoss() < compensateExp.getDamageAmount()) {
								lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss()) + "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
							} else {
								lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDamageAmount()) + "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
							}
						} else {
							if (compensateExp.getDeviceActualValue() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDeviceActualValue()) {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								} else {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDeviceActualValue())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								}
							} else {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								} else {
									lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getDamageAmount());
								}
							}
						}
						
					} else if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag){
						lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getUnitAmount());
					} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode())){
						lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss())+"× 0.3";
					} else {
						lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
					}
					if (compensateExp.getRescueFee() != 0.0 && !(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)) {
						lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getRescueFee());
					}
				} else if (compensateExp.getRescueFee() != 0.0 && !(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)) {
					lossDigitExp = lossDigitExp + compensateExp.formatPay(compensateExp.getRescueFee());
				}
				if (compensateExp.getSumRest() > 0.0 && 
				   (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D11.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D12.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_E.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z1.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Z2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_S.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_U.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X2.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_NY.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_Y.equals(compensateExp.getKindCode()) ||
					CodeConstants.KINDCODE.KINDCODE_L.equals(compensateExp.getKindCode()))|| 
					CodeConstants.KINDCODE.KINDCODE_K1.equals(compensateExp.getKindCode())||
					CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode())||
					CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode())||
					(CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) && flag)) { 
					if(!(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) && 
							!(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag)){
						if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
							if (compensateExp.getDeviceActualValue() > compensateExp.getDamageAmount()) {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
								}
							} else {
								lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
							}
						} else {
							lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
						}
					}
				}
				if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) && flag){
					lossDigitExp = lossDigitExp + " × " + compensateExp.formatPay(compensateExp.getLossQuantity()) +"-" +compensateExp.formatPay(compensateExp.getUnitAmount());
//					if(compensateExp.getLossQuantity() > compensateExp.getQuantity()){
//						lossDigitExp = lossDigitExp + " × " + compensateExp.formatPay(compensateExp.getQuantity()) +"-" +compensateExp.formatPay(compensateExp.getUnitAmount());
//					} else {
//					}
				}
			} else {
				if (compensateExp.getSumLoss() != 0.0 || compensateExp.getOtherDeductFee() != 0.0) {
					// PNCCAR,ZHOUWEN-2009-12-30,PNC-17153(SUPPORT),X新增设备险,引用出险时车辆实际价值，无法理算出新增设备损失金额,UPDATE,BEGIN.
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) ||
							CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode())) {
						if(CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) && flag) {
							if ((compensateExp.getSumLoss() - compensateExp.getSumRest()) < compensateExp.getDamageAmount()) {
								lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss()) + "-" + compensateExp.formatPay(compensateExp.getSumRest());
							} else {
								lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getDamageAmount());
							}
						} else {
							if (compensateExp.getDamageItemRealCost() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageItemRealCost()
										|| (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) && flag)) {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss());
								} else {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getDamageItemRealCost());
								}
							} else {
								if (compensateExp.getSumLoss() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss());
								} else {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getDamageAmount());
								}
							}
						}
					} else if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
						if(flag){
							if (compensateExp.getSumLoss() < compensateExp.getDamageAmount()) {
								lossDigitExp = lossDigitExp +  " + " +compensateExp.formatPay(compensateExp.getSumLoss()) + "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
							} else {
								lossDigitExp = lossDigitExp +  " + " +compensateExp.formatPay(compensateExp.getDamageAmount()) + "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
							}
						} else {
							if (compensateExp.getDeviceActualValue() <= compensateExp.getDamageAmount()) {
								if (compensateExp.getSumLoss() <= compensateExp.getDeviceActualValue()) {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								} else {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getDeviceActualValue())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								}
							} else {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss())+ "-"+compensateExp.formatPay(compensateExp.getSumLossBZPaid());
								} else {
									lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getDamageAmount());
								}
							}
						}
					}  else if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
						lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getUnitAmount());
					} else if(CodeConstants.KINDCODE.KINDCODE_NT.equals(compensateExp.getKindCode())){
						lossDigitExp = lossDigitExp + " + "+ compensateExp.formatPay(compensateExp.getSumLoss())+"× 0.3";
					} else {
						lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getSumLoss());
					}
					// PNCCAR,ZHOUWEN-2009-12-30,PNC-17153(SUPPORT),X新增设备险,引用出险时车辆实际价值，无法理算出新增设备损失金额,UPDATE,END.
					if (compensateExp.getRescueFee() != 0.0 && !"050441".equals(compensateExp.getKindCode())) {
						lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getRescueFee());
					}
				} else if (compensateExp.getRescueFee() != 0.0 && !"050441".equals(compensateExp.getKindCode())) {
					lossDigitExp = lossDigitExp + " + " + compensateExp.formatPay(compensateExp.getRescueFee());
				}
				if (compensateExp.getSumRest() > 0.0 && 
						
				   (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp.getKindCode()) ||
						   CodeConstants.KINDCODE.KINDCODE_D11.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_D12.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_E.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_Z1.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_Z2.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_S.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_U.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_X2.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_NY.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_X1.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_Y.equals(compensateExp.getKindCode()) || 
						   CodeConstants.KINDCODE.KINDCODE_L.equals(compensateExp.getKindCode()))||
						   CodeConstants.KINDCODE.KINDCODE_K1.equals(compensateExp.getKindCode()) ||
						   CodeConstants.KINDCODE.KINDCODE_K2.equals(compensateExp.getKindCode())) { 
					if(!CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
						if (CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())) {
							if (compensateExp.getDeviceActualValue() > compensateExp.getDamageAmount()) {
								if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() <= compensateExp.getDamageAmount()) {
									lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
								}
							} else {
								lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
							}
						} else {
							lossDigitExp = lossDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
						}
					}
				}
				if(CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
					lossDigitExp = lossDigitExp + " × " + compensateExp.formatPay(compensateExp.getLossQuantity()) +"-" +compensateExp.formatPay(compensateExp.getUnitAmount());
//					if(compensateExp.getLossQuantity() > compensateExp.getQuantity()){
//						lossDigitExp = lossDigitExp + " × " + compensateExp.formatPay(compensateExp.getQuantity()) +"-" +compensateExp.formatPay(compensateExp.getUnitAmount());
//					} else {
//					}
				}
			}
//			if("050260".equals(compensateExp.getKindCode())){
//				if (compensateExp.getSumLossBZPaid() > 0 ){
//					if (compensateExp.getDeviceActualValue() <= compensateExp.getDamageAmount()) {
//						if (compensateExp.getSumLoss() <= compensateExp.getDeviceActualValue()) {
//							lossExp = lossExp + " - 扣交强险赔偿金额";
//							lossDigitExp = lossDigitExp + "-" + compensateExp.formatPay(compensateExp.getSumLossBZPaid());
//						} else {
//							lossExp = lossExp + " - 扣交强险赔偿金额";
//							lossDigitExp = lossDigitExp + "-" + compensateExp.formatPay(compensateExp.getSumLossBZPaid());
//						}
//					} else {
//						if ((compensateExp.getSumLoss() - compensateExp.getSumRest() - compensateExp.getSumLossBZPaid()) * compensateExp.getIndemnityDutyRate() < compensateExp.getDamageAmount()) {
//							lossExp = lossExp + " - 扣交强险赔偿金额";
//							lossDigitExp = lossDigitExp + "-" + compensateExp.formatPay(compensateExp.getSumLossBZPaid());
//						} 
//					}
//				}
//			}
			if(compensateExp.getSumLoss() != 0.0 || compensateExp.getRescueFee() != 0.0 || equalsKindCode("M", compensateExp.getKindCode())){
				feeList.add(compensateExp);
			}
			compensateExpList.add(compensateExp);
			if ("1".equals(compensateExp.getFlagOfM())) {
				mPay = mPay + compensateExp.getMsumRealPay();
			}
		}
		
		/** 超限额调整部分*/
		itemRealPayString = compensateExp.formatPay(itemRealPay);
//				MODIFY BY GAOYUNTAO 2012-06-06 BEGIN
//      		PMD，代码检查工具检查出的错误类型:Use equals() to compare strings instead of '==' or '!='
		if ((compensateExp.getClaimRate() > 0
				&& compensateExp.getClaimRate() < 1)
//				|| "1".equals(compensateExp.getItemFlag().trim()) || overFlag == "Y") {
				|| (compensateExp.getItemFlag() != null && "1".equals(compensateExp.getItemFlag().trim())) 
				|| (overFlag != null && "Y".equals(overFlag.trim()))) {
//				MODIFY BY GAOYUNTAO 2012-06-06 END
			overFlag = "Y";
			itemRealPayExp = "赔偿金额超过赔偿限额，所以：" + "\n\t" + "本险别理算金额 = "
					+ itemRealPayString + "\n";
		}
	}
	
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**
	 * 组织交强计算书险别信息
	 */
	@SuppressWarnings("unchecked")
	public void addItemInfo(ThirdPartyLossInfo thirdPartyLossInfo, String payType) {
		//诉讼案
		isSuitFlag = "1";
		if(thirdPartyLossInfo.getInsureComCode()==null){
			thirdPartyLossInfo.setInsureComCode("");
		}
		thirdPartyLossInfoList.add(thirdPartyLossInfo);//交强计算书一分钱调整..
		CompensateExp format = new CompensateExp();
		if (thirdPartyLossInfo.getLossIndex() != null && !"".equals(thirdPartyLossInfo.getLossIndex())) {
			lossFlag = "Person";
			if (payType != null && "2".equals(payType.trim()) && thirdPartyLossInfo.getSumLoss() != 0) {// 代位
				itemPay = itemPay + (thirdPartyLossInfo.getSumDefLoss() * thirdPartyLossInfo.getRecoveryPay()) / thirdPartyLossInfo.getSumLoss();
			} else {
				itemPay = itemPay + thirdPartyLossInfo.getSumDefLoss();
			}
			itemRealPay = itemRealPay + thirdPartyLossInfo.getSumRealPay();
			//诉讼案
			if ("1".equals(isSuitFlag)) {
				itemSuitPay = itemSuitPay + thirdPartyLossInfo.getSumRealPayJud();
			}
			if(!Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) && !Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
				Double tmpItem2RealPay = new BigDecimal(thirdPartyLossInfo.getSumRealPay()).setScale(2,4).doubleValue();
				item2RealPay = item2RealPay + tmpItem2RealPay;
			}
			
			String addFlag = "N";
			if (itemInfoList.size() == 0) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
				itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
				itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
				itemInfo.setItemPay(thirdPartyLossInfo.getSumDefLoss());
				itemInfo.setSumCarLoss(thirdPartyLossInfo.getSumLoss()); // 总损失金额
				itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
				if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
						thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
				} else {
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
				}
				itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
				itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
				//诉讼案
				if ("1".equals(isSuitFlag)) {
					itemInfo.setItemSuitPay(thirdPartyLossInfo.getSumRealPayJud());
				}
				itemInfo.addItemInfo(thirdPartyLossInfo);
				itemInfoList.add(itemInfo);
			} else {
				for (Iterator iter = itemInfoList.iterator(); iter.hasNext();) {
					ItemInfo itemInfo = (ItemInfo) iter.next();
					if (thirdPartyLossInfo.getLossIndex() != null
							&& !"".equals(thirdPartyLossInfo.getLossIndex())) {
						if (thirdPartyLossInfo.getLossIndex().equals(itemInfo.getLossIndex())) {
							itemInfo.setItemPay(itemInfo.getItemPay() + thirdPartyLossInfo.getSumDefLoss());
							itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
							itemInfo.setSumCarLoss(itemInfo.getSumCarLoss() + thirdPartyLossInfo.getSumLoss()); // 总损失金额
							itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
							//诉讼案
							if ("1".equals(isSuitFlag)) {
								itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
							}
							itemInfo.addItemInfo(thirdPartyLossInfo);
							addFlag = "Y";
						}
					}
				}
				if ("N".equals(addFlag)) {
					ItemInfo itemInfo = new ItemInfo();
					itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
					itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
					itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
					itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
					itemInfo.setSumCarLoss(thirdPartyLossInfo.getSumLoss()); // 总损失金额
					itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
					if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
						thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
					} else {
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
					}
					itemInfo.setItemPay(thirdPartyLossInfo.getSumDefLoss());
					itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
					//诉讼案
					if ("1".equals(isSuitFlag)) {
						itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
					}
					itemInfo.addItemInfo(thirdPartyLossInfo);
					itemInfoList.add(itemInfo);
				}
			}
		} else {
			lossFlag = "Other";
			if (payType != null && "2".equals(payType.trim()) && thirdPartyLossInfo.getSumLoss() != 0) {// 代位
				itemPay = itemPay + (thirdPartyLossInfo.getSumDefLoss() * thirdPartyLossInfo.getRecoveryPay()) / thirdPartyLossInfo.getSumLoss();
			} else {
				itemPay = itemPay + thirdPartyLossInfo.getSumDefLoss();
			}
			itemRealPay = itemRealPay + thirdPartyLossInfo.getSumRealPay();
			//诉讼案
			if ("1".equals(isSuitFlag)) {
				itemSuitPay = itemSuitPay + thirdPartyLossInfo.getSumRealPayJud();
			}
			if(!Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) && !Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
				Double tmpItem2RealPay = new BigDecimal(thirdPartyLossInfo.getSumRealPay()).setScale(2,4).doubleValue();
				item2RealPay = item2RealPay + tmpItem2RealPay;
			}
			
			String addFlag = "N";
			if (itemInfoList.size() == 0) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
				itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
				itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
				itemInfo.setItemPay(thirdPartyLossInfo.getSumDefLoss());
				itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
				itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
				itemInfo.setLossIdentifyName(thirdPartyLossInfo.getLossName());
				itemInfo.setSumCarLoss(thirdPartyLossInfo.getSumLoss()); // 总损失金额
				itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
				if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
					thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
				} else {
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
				}
				//诉讼案
				if ("1".equals(isSuitFlag)) {
					itemInfo.setItemSuitPay(thirdPartyLossInfo.getSumRealPayJud());
				}
				itemInfo.addItemInfo(thirdPartyLossInfo);
				itemInfoList.add(0,itemInfo);
			} else {
				for (Iterator iter = itemInfoList.iterator(); iter.hasNext();) {
					ItemInfo itemInfo = (ItemInfo) iter.next();
					if (thirdPartyLossInfo.getLossName() != null
							&& !"".equals(thirdPartyLossInfo.getLossName())) {
						if (thirdPartyLossInfo.getLossName().equals(itemInfo.getLossIdentifyName())) {
							itemInfo.setItemPay(itemInfo.getItemPay() + thirdPartyLossInfo.getSumDefLoss());
							itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
							itemInfo.setSumCarLoss(itemInfo.getSumCarLoss() + thirdPartyLossInfo.getSumLoss()); // 总损失金额
							itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
							//诉讼案
							if ("1".equals(isSuitFlag)) {
								itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
							}
							itemInfo.addItemInfo(thirdPartyLossInfo);
							addFlag = "Y";
						}
					}
				}
				if ("N".equals(addFlag)) {
					ItemInfo itemInfo = new ItemInfo();
					itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
					itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
					itemInfo.setItemPay(thirdPartyLossInfo.getSumDefLoss());
					itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
					itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
					itemInfo.setLossIdentifyName(thirdPartyLossInfo.getLossName());
					itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
					itemInfo.setSumCarLoss(thirdPartyLossInfo.getSumLoss()); // 总损失金额
					itemInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay()); // 责任对方追偿金额
					if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
						thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
					} else {
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
					}
					//诉讼案
					if ("1".equals(isSuitFlag)) {
						itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
					}
					itemInfo.addItemInfo(thirdPartyLossInfo);
					itemInfoList.add(0, itemInfo);
				}
			}
		}
		/** 超限额调整部分*/
		itemRealPayString = format.formatPay(itemRealPay);
//		MODIFY BY GAOYUNTAO 2012-06-06 BEGIN
//      PMD，代码检查工具检查出的错误类型:Use equals() to compare strings instead of '==' or '!='
//		if ((thirdPartyLossInfo.getOverFlag() != null && "1".equals(thirdPartyLossInfo.getOverFlag())) || overFlag == "Y") {
		if ((thirdPartyLossInfo != null && thirdPartyLossInfo.getOverFlag() != null && "1".equals(thirdPartyLossInfo.getOverFlag().trim())) || (overFlag != null && "Y".equals(overFlag.trim()))) {
//		MODIFY BY GAOYUNTAO 2012-06-06 END
			overFlag = "Y";
			itemRealPayExp = "因按事故责任比例应承担的赔偿金额超过交强险的赔偿限额，所以：" + "\n\t" 
			+ "本险别理算金额 = 交强险分项赔偿限额 " + "\n\t= "
			+ itemRealPayString;
		}
		/** 剩余限额调整部分*/
//		MODIFY BY GAOYUNTAO 2012-06-06 BEGIN
//      PMD，代码检查工具检查出的错误类型:Use equals() to compare strings instead of '==' or '!='
//		if ((thirdPartyLossInfo.getOverFlag() != null && "2".equals(thirdPartyLossInfo.getOverFlag())) || overFlag == "O") {
		if ((thirdPartyLossInfo.getOverFlag() != null && "2".equals(thirdPartyLossInfo.getOverFlag().trim())) || (overFlag != null && "O".equals(overFlag.trim()))) {
//	    MODIFY BY GAOYUNTAO 2012-06-06 END
			overFlag = "O";
			itemRealPayExp = "存在交强未赔足损失（三者车进行了超限额调整），且本车存在剩余限额，所以：" + "\n\t" 
			+ "本险别理算金额 = 交强险赔偿金额 " + "\n\t= "
			+ itemRealPayString;
		}
	}
	
	/**
	 * 组织垫付计算书诉讼案险别信息
	 */
	@SuppressWarnings("unchecked")
	public void addItemInfoOfAdvance(ThirdPartyLossInfo thirdPartyLossInfo) {
		//诉讼案
		isSuitFlag = "1";
		if(thirdPartyLossInfo.getInsureComCode()==null){
			thirdPartyLossInfo.setInsureComCode("");
		}
		thirdPartyLossInfoList.add(thirdPartyLossInfo);//交强计算书一分钱调整..
		CompensateExp format = new CompensateExp();
		if (thirdPartyLossInfo.getLossIndex() != null && !"".equals(thirdPartyLossInfo.getLossIndex())) {
			lossFlag = "Person";
			itemPay = itemPay + thirdPartyLossInfo.getSumLoss();
			itemRealPay = itemRealPay + thirdPartyLossInfo.getSumRealPay();
			//诉讼案
			if ("1".equals(isSuitFlag)) {
				itemSuitPay = itemSuitPay + thirdPartyLossInfo.getSumRealPayJud();
			}
			if(!Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) && !Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
				Double tmpItem2RealPay = new BigDecimal(thirdPartyLossInfo.getSumRealPay()).setScale(2,4).doubleValue();
				item2RealPay = item2RealPay + tmpItem2RealPay;
			}
			
			String addFlag = "N";
			if (itemInfoList.size() == 0) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
				itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
				itemInfo.setItemPay(thirdPartyLossInfo.getSumLoss());
				itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
				if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
					thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
				} else {
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
				}
				itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
				itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
				//诉讼案
				if ("1".equals(isSuitFlag)) {
					itemInfo.setItemSuitPay(thirdPartyLossInfo.getSumRealPayJud());
				}
				itemInfo.addItemInfo(thirdPartyLossInfo);
				itemInfoList.add(itemInfo);
			} else {
				for (Iterator iter = itemInfoList.iterator(); iter.hasNext();) {
					ItemInfo itemInfo = (ItemInfo) iter.next();
					if (thirdPartyLossInfo.getLossIndex() != null && !"".equals(thirdPartyLossInfo.getLossIndex())) {
						if (thirdPartyLossInfo.getLossIndex().equals(itemInfo.getLossIndex())) {
							itemInfo.setItemPay(itemInfo.getItemPay() + thirdPartyLossInfo.getSumLoss());
							itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
							//诉讼案
							if ("1".equals(isSuitFlag)) {
								itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
							}
							itemInfo.addItemInfo(thirdPartyLossInfo);
							addFlag = "Y";
						}
					}
				}
				if ("N".equals(addFlag)) {
					ItemInfo itemInfo = new ItemInfo();
					itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
					itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
					itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
					itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
					if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
						thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
					} else {
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
					}
					itemInfo.setItemPay(thirdPartyLossInfo.getSumLoss());
					itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
					//诉讼案
					if ("1".equals(isSuitFlag)) {
						itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
					}
					itemInfo.addItemInfo(thirdPartyLossInfo);
					itemInfoList.add(itemInfo);
				}
			}
		} else {
			lossFlag = "Other";
			itemPay = itemPay + thirdPartyLossInfo.getSumLoss();
			itemRealPay = itemRealPay + thirdPartyLossInfo.getSumRealPay();
			//诉讼案
			if ("1".equals(isSuitFlag)) {
				itemSuitPay = itemSuitPay + thirdPartyLossInfo.getSumRealPayJud();
			}
			if(!Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) && !Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
				Double tmpItem2RealPay = new BigDecimal(thirdPartyLossInfo.getSumRealPay()).setScale(2,4).doubleValue();
				item2RealPay = item2RealPay + tmpItem2RealPay;
			}
			
			String addFlag = "N";
			if (itemInfoList.size() == 0) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
				itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
				itemInfo.setItemPay(thirdPartyLossInfo.getSumLoss());
				itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
				itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
				itemInfo.setLossIdentifyName(thirdPartyLossInfo.getLossName());
				itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
				if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
					thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
				} else {
					itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
				}
				//诉讼案
				if ("1".equals(isSuitFlag)) {
					itemInfo.setItemSuitPay(thirdPartyLossInfo.getSumRealPayJud());
				}
				itemInfo.addItemInfo(thirdPartyLossInfo);
				itemInfoList.add(0,itemInfo);
			} else {
				for (Iterator iter = itemInfoList.iterator(); iter.hasNext();) {
					ItemInfo itemInfo = (ItemInfo) iter.next();
					if (thirdPartyLossInfo.getLossName() != null && !"".equals(thirdPartyLossInfo.getLossName())) {
						if (thirdPartyLossInfo.getLossName().equals(itemInfo.getLossIdentifyName())) {
							itemInfo.setItemPay(itemInfo.getItemPay() + thirdPartyLossInfo.getSumLoss());
							itemInfo.setItemRealPay(itemInfo.getItemRealPay() + thirdPartyLossInfo.getSumRealPay());
							//诉讼案
							if ("1".equals(isSuitFlag)) {
								itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
							}
							itemInfo.addItemInfo(thirdPartyLossInfo);
							addFlag = "Y";
						}
					}
				}
				if ("N".equals(addFlag)) {
					ItemInfo itemInfo = new ItemInfo();
					itemInfo.setLossIndex(thirdPartyLossInfo.getLossIndex());
					itemInfo.setLossName(thirdPartyLossInfo.getLicenseNo());
					itemInfo.setItemPay(thirdPartyLossInfo.getSumLoss());
					itemInfo.setItemRealPay(thirdPartyLossInfo.getSumRealPay());
					itemInfo.setPayCarAmout(thirdPartyLossInfo.getPayCarAmout());
					itemInfo.setLossIdentifyName(thirdPartyLossInfo.getLossName());
					itemInfo.setDamageType(thirdPartyLossInfo.getDamageType());
					if(thirdPartyLossInfo.getLicenseNo() != null && !"".equals(thirdPartyLossInfo.getLicenseNo()) && 
						thirdPartyLossInfo.getInsureComCode()!=null && !"11".equals(thirdPartyLossInfo.getInsureComCode().trim())){
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount() - thirdPartyLossInfo.getDamageAmount());
					} else {
						itemInfo.setAllAmount(thirdPartyLossInfo.getAllAmount());
					}
					//诉讼案
					if ("1".equals(isSuitFlag)) {
						itemInfo.setItemSuitPay(itemInfo.getItemSuitPay() + thirdPartyLossInfo.getSumRealPayJud());
					}
					itemInfo.addItemInfo(thirdPartyLossInfo);
					itemInfoList.add(0,itemInfo);
				}
			}
		}
		/** 超限额调整部分*/
		itemRealPayString = format.formatPay(itemRealPay);
//		MODIFY BY GAOYUNTAO 2012-06-06 BEGIN
//      PMD，代码检查工具检查出的错误类型:Use equals() to compare strings instead of '==' or '!='
//		if ((thirdPartyLossInfo.getOverFlag() != null && "1".equals(thirdPartyLossInfo.getOverFlag())) || overFlag == "Y") {
		if ((thirdPartyLossInfo != null && thirdPartyLossInfo.getOverFlag() != null && "1".equals(thirdPartyLossInfo.getOverFlag().trim())) || (overFlag != null &&"Y".equals(overFlag.trim()))) {
//		MODIFY BY GAOYUNTAO 2012-06-06 END
			overFlag = "Y";
			itemRealPayExp = "赔偿金额超过赔偿限额，所以：" + "\n\t"  + "本险别理算金额 = " + itemRealPayString;
		}
	}
	
	/**
	 * 生成费用表达式--规则调用
	 */
	public void createFeeExp() {
		for (Iterator iterator = feeList.iterator(); iterator.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iterator.next();
			/** 交强已赔付*/
			bZPaid = bZPaid + compensateExp.getbZPaid();
			if (iterator.hasNext()) {
			} else {
				if (bZPaid > 0 && (CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode()) || 
						CodeConstants.KINDCODE.KINDCODE_B.equals(compensateExp.getKindCode()) || 
						CodeConstants.KINDCODE.KINDCODE_D2.equals(compensateExp.getKindCode()))) {
					lossExp = lossExp + " - 扣交强险赔偿金额";
					lossDigitExp = lossDigitExp + "-" + compensateExp.formatPay(bZPaid);
				}
			}
		}
	}
	
	/**
	 * 调整一分钱的问题
	 */
	public void adjActuarialAmount() {
		//扣减一分钱
		/**
		Double tmpItemRealPay = 0.00;
		if(!Double.isInfinite(itemRealPay) && !Double.isNaN(itemRealPay)){
			tmpItemRealPay = new BigDecimal(itemRealPay).setScale(2,4).doubleValue();
		}
		Double overPay = 0.0;
//		MODIFY BY GAOYUNTAO 2012-06-20 BEGIN
//      PMD，代码检查工具检查出的错误类型:Use equals() to compare object references.
//		if (item2RealPay != tmpItemRealPay) {
		if (!item2RealPay.equals(tmpItemRealPay)) {
			overPay = item2RealPay - tmpItemRealPay;
			if(!Double.isInfinite(overPay) && !Double.isNaN(overPay)){
				overPay = new BigDecimal(overPay).setScale(5,4).doubleValue();
			}
		}
//		MODIFY BY GAOYUNTAO 2012-06-20 END
		int i = 1;
		for (Iterator iterator = compensateExpList.iterator(); iterator.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iterator.next();
			if (i == 1) {
				if (compensateExp.getSumRealPay() != 0) {
					compensateExp.setSumRealPay(compensateExp.getSumRealPay() - overPay);
					if (compensateExp.getClaimRate() <= 0 || compensateExp.getClaimRate() >= 1) {
						compensateExp.setSumDefLoss(compensateExp.getSumDefLoss() - overPay);
					}
					i++;
				}
			}
		}
		*/
	}
	
	
	/**
	 * 交强计算书调整一分钱的问题
	 */
	public void adjBZActuarialAmount() {
		//交强一分钱处理放到规则返回结果后处理，此处不删除方法是为了不修改规则
		/**
		//扣减一分钱
		Double tmpItemRealPay = 0.00;
		if(!Double.isInfinite(itemRealPay) && !Double.isNaN(itemRealPay)){
			tmpItemRealPay = new BigDecimal(itemRealPay).setScale(2,4).doubleValue();
		}
		Double overPay = 0.0;
//		MODIFY BY GAOYUNTAO 2012-06-20 BEGIN
//      PMD，代码检查工具检查出的错误类型:Use equals() to compare object references.
		if (item2RealPay != tmpItemRealPay) {
			overPay = item2RealPay - tmpItemRealPay;
			if(!Double.isInfinite(overPay) && !Double.isNaN(overPay)){
				overPay = new BigDecimal(overPay).setScale(5,4).doubleValue();
			}
		}
//		MODIFY BY GAOYUNTAO 2012-06-20
		int i = 1;
		for (Iterator iterator = thirdPartyLossInfoList.iterator(); iterator.hasNext();) {
			ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo) iterator.next();
			if (i == 1) {
				if (thirdPartyLossInfo.getSumRealPay() != 0) {
					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay() - overPay);
					i++;
				}
			}
		}
		*/
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
	 * 将赔款金额按格式进行格式化输出
	 *
	 * @param pay
	 * @return
	 */
	@SuppressWarnings("unused")
	private String formatPay(double pay) {
		if(!Double.isNaN(pay)){
			double number = new BigDecimal(pay).setScale(2,4).doubleValue();
			return new DecimalFormat("#,##0.00").format(number);
		}else{
			return new DecimalFormat("#,##0.00").format(pay);
		}
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

	
	public String getClauseType() {
		return clauseType;
	}

	
	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}

	
	public String getKindCode() {
		return kindCode;
	}

	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	
	public String getKindName() {
		return kindName;
	}

	
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	
	public Double getmPay() {
		return mPay;
	}

	
	public void setmPay(Double mPay) {
		this.mPay = mPay;
	}

	
	public Double getmPayJud() {
		return mPayJud;
	}

	
	public void setmPayJud(Double mPayJud) {
		this.mPayJud = mPayJud;
	}

	
	public List getFeeList() {
		return feeList;
	}

	
	public void setFeeList(List feeList) {
		this.feeList = feeList;
	}

	
	public List getCompensateExpList() {
		return compensateExpList;
	}

	
	public void setCompensateExpList(List compensateExpList) {
		this.compensateExpList = compensateExpList;
	}

	
	public List getItemInfoList() {
		return itemInfoList;
	}

	
	public void setItemInfoList(List itemInfoList) {
		this.itemInfoList = itemInfoList;
	}

	
	public String getKindText() {
		return kindText;
	}

	
	public void setKindText(String kindText) {
		this.kindText = kindText;
	}

	
	public String getLossFlag() {
		return lossFlag;
	}

	
	public void setLossFlag(String lossFlag) {
		this.lossFlag = lossFlag;
	}

	
	public String getFeeExp() {
		return feeExp;
	}

	
	public void setFeeExp(String feeExp) {
		this.feeExp = feeExp;
	}

	
	public String getFeeDigitExp() {
		return feeDigitExp;
	}

	
	public void setFeeDigitExp(String feeDigitExp) {
		this.feeDigitExp = feeDigitExp;
	}

	
	public Double getItemPay() {
		return itemPay;
	}

	
	public void setItemPay(Double itemPay) {
		this.itemPay = itemPay;
	}

	
	public Double getItemRealPay() {
		return itemRealPay;
	}

	
	public void setItemRealPay(Double itemRealPay) {
		this.itemRealPay = itemRealPay;
	}

	
	public Double getItemSuitPay() {
		return itemSuitPay;
	}

	
	public void setItemSuitPay(Double itemSuitPay) {
		this.itemSuitPay = itemSuitPay;
	}

	
	public Double getItem2RealPay() {
		return item2RealPay;
	}

	
	public void setItem2RealPay(Double item2RealPay) {
		this.item2RealPay = item2RealPay;
	}

	
	public String getItemRealPayString() {
		return itemRealPayString;
	}

	
	public void setItemRealPayString(String itemRealPayString) {
		this.itemRealPayString = itemRealPayString;
	}

	
	public String getItemPayExp() {
		return itemPayExp;
	}

	
	public void setItemPayExp(String itemPayExp) {
		this.itemPayExp = itemPayExp;
	}

	
	public String getItemRealPayExp() {
		return itemRealPayExp;
	}

	
	public void setItemRealPayExp(String itemRealPayExp) {
		this.itemRealPayExp = itemRealPayExp;
	}

	
	public String getKindExp() {
		return kindExp;
	}

	
	public void setKindExp(String kindExp) {
		this.kindExp = kindExp;
	}

	
	public String getKindSuitExp() {
		return kindSuitExp;
	}

	
	public void setKindSuitExp(String kindSuitExp) {
		this.kindSuitExp = kindSuitExp;
	}

	
	public String getItemExp() {
		return itemExp;
	}

	
	public void setItemExp(String itemExp) {
		this.itemExp = itemExp;
	}

	
	public String getItemSuitExp() {
		return itemSuitExp;
	}

	
	public void setItemSuitExp(String itemSuitExp) {
		this.itemSuitExp = itemSuitExp;
	}

	
	public String getCompensateExp() {
		return compensateExp;
	}

	
	public void setCompensateExp(String compensateExp) {
		this.compensateExp = compensateExp;
	}

	
	public String getCompensateDigitExp() {
		return compensateDigitExp;
	}

	
	public void setCompensateDigitExp(String compensateDigitExp) {
		this.compensateDigitExp = compensateDigitExp;
	}

	
	public String getLossExp() {
		return lossExp;
	}

	
	public void setLossExp(String lossExp) {
		this.lossExp = lossExp;
	}

	
	public String getLossDigitExp() {
		return lossDigitExp;
	}

	
	public void setLossDigitExp(String lossDigitExp) {
		this.lossDigitExp = lossDigitExp;
	}

	
	public String getClaimRateFlag() {
		return claimRateFlag;
	}

	
	public void setClaimRateFlag(String claimRateFlag) {
		this.claimRateFlag = claimRateFlag;
	}

	
	public double getDeductibleRate() {
		return deductibleRate;
	}

	
	public void setDeductibleRate(double deductibleRate) {
		this.deductibleRate = deductibleRate;
	}

	
	public String getDeductibleRateText() {
		return deductibleRateText;
	}

	
	public void setDeductibleRateText(String deductibleRateText) {
		this.deductibleRateText = deductibleRateText;
	}

	
	public double getbZPaid() {
		return bZPaid;
	}

	
	public void setbZPaid(double bZPaid) {
		this.bZPaid = bZPaid;
	}

	
	public String getOverFlag() {
		return overFlag;
	}

	
	public void setOverFlag(String overFlag) {
		this.overFlag = overFlag;
	}

	
	public String getIsSuitFlag() {
		return isSuitFlag;
	}

	
	public void setIsSuitFlag(String isSuitFlag) {
		this.isSuitFlag = isSuitFlag;
	}

	
	public double getDeductFee() {
		return deductFee;
	}

	
	public void setDeductFee(double deductFee) {
		this.deductFee = deductFee;
	}

	
	public List getThirdPartyLossInfoList() {
		return thirdPartyLossInfoList;
	}

	
	public void setThirdPartyLossInfoList(List thirdPartyLossInfoList) {
		this.thirdPartyLossInfoList = thirdPartyLossInfoList;
	}

	
	public String getOppoentCoverageType() {
		return oppoentCoverageType;
	}

	
	public void setOppoentCoverageType(String oppoentCoverageType) {
		this.oppoentCoverageType = oppoentCoverageType;
	}
	
}
