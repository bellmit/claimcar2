package ins.sino.claimcar.claim.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 损失项序号*/
	private String lossIndex;
	/** 损失项名称*/
	private String lossName;
	/** 费用List*/
	private List feeList = new ArrayList();
	/** 损失项描述*/
	private String lossTable;
	/** 费用清单*/
	private String feeTable = "";
	/** 费用中文表达式*/
	private String feeExp = "";
	/** 本项理算金额中文表达式*/
	private String compensateExp = "";
	/** 费用数字表达式*/
	private String feeDigitExp = "";
	/** 本项理算金额数字表达式*/
	private String compensateDigitExp = "";
	/** 赔付金额*/
	private Double itemPay = 0.0;
	/** 实赔金额*/
	private Double itemRealPay = 0.0;
	/** 诉讼金额*/
	private Double itemSuitPay = 0.0;
	/** 保留两位的实赔金额*/
	private Double item2RealPay = 0.0;
	/** 赔付金额表达式*/
	private String itemPayExp = "";
	/** 损失项理算公式*/
	private String itemExp = "";
	/** 损失项理算公式（诉讼案)*/
	private String itemSuitExp = "";
	/** 责任比例*/
	private double indemnityDutyRate = 0.0;
	/** 免赔率*/
	private double deductibleRate = 0.0;
	/** 免赔率公式*/
	private String deductibleRateText;
	/** 赔付车辆限额 */
	private double payCarAmout;
	/** 分摊责任限额之和 */
	private double allAmount;
	/** 损失标识*/
	private String lossIdentifyName;
	/** 无责车损标志位*/
	private String noDutyCarLossFlag = "0";
	/** 车损赔款*/
	private double sumRealLoss;
	/** 车损计算公式*/
	private String sumRealLossText;
	/** 施救费赔款*/
	private double rescueRealFee;
	/** 施救费计算公式*/
	private String rescueFeeText;
	/** 实赔金额计算公式*/
	private String sumRealPayText;
	/** 理算金额*/
	private double sumDefLoss;
	/** 车财产标志位 52:财产 49:医疗 51:死伤*/
	private String damageType;
	/** 车辆总损失(定损+施救费-扣除其他) */
	private double sumCarLoss;
	/** 2012-02-06 代位求偿需求添加 */
	/** 责任对方追偿款 */
	private double recoveryPay;
	
	public double getSumCarLoss() {
		return sumCarLoss;
	}
	public void setSumCarLoss(double sumCarLoss) {
		this.sumCarLoss = sumCarLoss;
	}
	public String getDamageType() {
		return damageType;
	}
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	public String getNoDutyCarLossFlag() {
		return noDutyCarLossFlag;
	}
	public void setNoDutyCarLossFlag(String noDutyCarLossFlag) {
		this.noDutyCarLossFlag = noDutyCarLossFlag;
	}
	public String getLossIdentifyName() {
		return lossIdentifyName;
	}
	public void setLossIdentifyName(String lossIdentifyName) {
		this.lossIdentifyName = lossIdentifyName;
	}
	public double getAllAmount() {
		return allAmount;
	}
	public void setAllAmount(double allAmount) {
		this.allAmount = allAmount;
	}
	public double getPayCarAmout() {
		return payCarAmout;
	}
	public void setPayCarAmout(double payCarAmout) {
		this.payCarAmout = payCarAmout;
	}
	public String getDeductibleRateText() {
		return deductibleRateText;
	}
	public void setDeductibleRateText(String deductibleRateText) {
		this.deductibleRateText = deductibleRateText;
	}
	public double getDeductibleRate() {
		return deductibleRate;
	}
	public void setDeductibleRate(double deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	public double getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	public void setIndemnityDutyRate(double indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	public String getItemExp() {
		return itemExp;
	}
	public void setItemExp(String itemExp) {
		this.itemExp = itemExp;
	}
	public String getCompensateDigitExp() {
		return compensateDigitExp;
	}
	public void setCompensateDigitExp(String compensateDigitExp) {
		this.compensateDigitExp = compensateDigitExp;
	}
	public String getCompensateExp() {
		return compensateExp;
	}
	public void setCompensateExp(String compensateExp) {
		this.compensateExp = compensateExp;
	}
	public String getFeeDigitExp() {
		return feeDigitExp;
	}
	public void setFeeDigitExp(String feeDigitExp) {
		this.feeDigitExp = feeDigitExp;
	}
	public String getFeeExp() {
		return feeExp;
	}
	public void setFeeExp(String feeExp) {
		this.feeExp = feeExp;
	}
	public Double getItemPay() {
		return itemPay;
	}
	public void setItemPay(Double itemPay) {
		this.itemPay = itemPay;
	}
	public String getItemPayExp() {
		return itemPayExp;
	}
	public void setItemPayExp(String itemPayExp) {
		this.itemPayExp = itemPayExp;
	}
	public String getLossIndex() {
		return lossIndex;
	}
	public void setLossIndex(String lossIndex) {
		this.lossIndex = lossIndex;
	}
	public String getLossName() {
		return lossName;
	}
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}
	
	@SuppressWarnings("unchecked")
	public void addItemInfo(CompensateExp compensateExp) {
		indemnityDutyRate = compensateExp.getIndemnityDutyRate();
		deductibleRate = compensateExp.getDeductibleRate();
		deductibleRateText = compensateExp.getDeductibleRateText();
		if (equalsKindCode("A", compensateExp.getKindCode()) ||
//			PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。begin
			equalsKindCode("050201", compensateExp.getKindCode())){//农机损失保险
//			PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
			sumRealLossText = compensateExp.getSumRealLossText();
			rescueFeeText = compensateExp.getRescueFeeText();
			sumRealPayText = compensateExp.getSumRealPayText();
			sumRealLoss = compensateExp.getSumRealLoss();
			rescueRealFee = compensateExp.getRescueRealFee();
			sumDefLoss = compensateExp.getSumDefLoss();
		}
		feeList.add(compensateExp);
	}
	
	@SuppressWarnings("unchecked")
	public void addItemInfo(ThirdPartyLossInfo thirdPartyLossInfo) {
		feeList.add(thirdPartyLossInfo);
	}
	
	/**
	 * 生成费用表达式--规则调用
	 */
	public void createFeeExp() {
		Double bzPaid = 0.0;
		for (Iterator iterator = feeList.iterator(); iterator.hasNext();) {
			CompensateExp compensateExp = (CompensateExp) iterator.next();
			/** 人员最开始的那个人员描述模块*/
			lossTable = compensateExp.getFeeTypeName() + " " + lossName;
			/** 人员最开始的那个费用列表*/
			if ("".equals(feeTable)) {
				if (0.00 == compensateExp.getRescueFee()) {
					/** 车损模块专用*/
					if (compensateExp.getLossIndex() == null) {
						if (compensateExp.getSumLoss() > 0.0) {
							feeTable = feeTable + "\t" + "定损金额" + "：" + compensateExp.formatPay(compensateExp.getSumLoss());
							if (compensateExp.getRescueFee() > 0.0) {
								feeTable = feeTable + "\n\t" + "施救费" + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
							}
						} else if (compensateExp.getRescueFee() > 0.0) {
							feeTable = feeTable + "\t" + "施救费" + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
						}
					} else {
						feeTable = feeTable + "\t" + compensateExp.getPersonChargeName() + "：" + compensateExp.formatPay(compensateExp.getSumLoss());
					}
				} else {
					/** 车损模块专用*/
					if (compensateExp.getLossIndex() == null) {
						if (compensateExp.getSumLoss() > 0.0) {
							feeTable = feeTable + "\t" + "定损金额" + "：" + compensateExp.formatPay(compensateExp.getSumLoss());
							if (compensateExp.getRescueFee() > 0.0) {
								feeTable = feeTable + "\n\t" + "施救费" + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
							}
						} else if (compensateExp.getRescueFee() > 0.0) {
							feeTable = feeTable + "\t" + "施救费" + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
						}
					} else {
						feeTable = feeTable + "\t" + compensateExp.getPersonChargeName() + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
					}
				}
				
			} else {
				if (0.00 == compensateExp.getRescueFee()) {
					feeTable = feeTable + "\n" + "\t" + compensateExp.getPersonChargeName() + "：" + compensateExp.formatPay(compensateExp.getSumLoss());
				} else {
					feeTable = feeTable + "\n" + "\t" + compensateExp.getPersonChargeName() + "：" + compensateExp.formatPay(compensateExp.getRescueFee());
				}
			}
			/** 费用中文表达式*/
			/** 车损模块专用*/
			if (compensateExp.getLossIndex() == null) {
				String overAdjString = "";
				Double overAdjAmount = 0.0;
				//取保额及出险时车辆实际价值中较小的一个进行超限额控制
				if (compensateExp.getDamageAmount() > compensateExp.getDamageItemRealCost()) {
					overAdjString = "出险时车辆实际价值";
					overAdjAmount = compensateExp.getDamageItemRealCost();
				} else {
					overAdjString = "出险时险别保险金额";
					overAdjAmount = compensateExp.getDamageAmount();
				}
				if ("F34".equals(compensateExp.getClauseType())
					 || "F35".equals(compensateExp.getClauseType())
					 || "F44".equals(compensateExp.getClauseType())
					 || "F45".equals(compensateExp.getClauseType())
					 ) {
					overAdjString = "出险时险别保险金额";
					overAdjAmount = compensateExp.getDamageAmount();
				}
				if (equalsKindCode("A", compensateExp.getKindCode()) || 
					equalsKindCode("X", compensateExp.getKindCode()) || 
					equalsKindCode("X1", compensateExp.getKindCode())||
//					PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。begin
					equalsKindCode("050201", compensateExp.getKindCode())){//农机损失保险
//					PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
					if (compensateExp.getSumLoss() > 0.0) {
						if ((compensateExp.getSumLoss() * compensateExp.getIndemnityDutyRate())> overAdjAmount) {
							feeExp = feeExp + overAdjString;
						} else {
							feeExp = feeExp + "定损金额";
						}
						//加施救费
						if (compensateExp.getRescueFee() > 0.0) {
							//A险施救费单占险额
							if ((compensateExp.getRescueFee() * compensateExp.getIndemnityDutyRate())> compensateExp.getDamageAmount() && equalsKindCode("A", compensateExp.getKindCode())) {
								feeExp = feeExp + " + " + "出险时险别保险金额";
							} else {
								feeExp = feeExp + " + 施救费";
							}
						}
					} else if (compensateExp.getRescueFee() > 0.0) {
						//A险施救费单占险额
						if ((compensateExp.getRescueFee() * compensateExp.getIndemnityDutyRate()) > compensateExp.getDamageAmount() && equalsKindCode("A", compensateExp.getKindCode())) {
							feeExp = feeExp + "出险时险别保险金额";
						} else {
							feeExp = feeExp + "施救费";
						}
					}
				} else {
					if (compensateExp.getSumLoss() > 0.0) {
						feeExp = feeExp + "定损金额";
						if (compensateExp.getRescueFee() > 0.0) {
							feeExp = feeExp + " + 施救费";
						}
					} else if (compensateExp.getRescueFee() > 0.0) {
						feeExp = feeExp + "施救费";
					}
				}
			} else {
				feeExp = feeExp + compensateExp.getPersonChargeName();
			}
			if (compensateExp.getSumRest() > 0.0 && 
			   (equalsKindCode("A", compensateExp.getKindCode()) || 
				equalsKindCode("B", compensateExp.getKindCode()) || 
				equalsKindCode("G", compensateExp.getKindCode()) || 
				equalsKindCode("D1", compensateExp.getKindCode())|| 
				equalsKindCode("D2", compensateExp.getKindCode())|| 
				equalsKindCode("E", compensateExp.getKindCode()) || 
				equalsKindCode("Z", compensateExp.getKindCode()) || 
				equalsKindCode("X", compensateExp.getKindCode()) || 
				equalsKindCode("T", compensateExp.getKindCode()) || 
				equalsKindCode("C6", compensateExp.getKindCode())|| 
				equalsKindCode("C9", compensateExp.getKindCode())|| 
				equalsKindCode("S", compensateExp.getKindCode()) || 
				equalsKindCode("U", compensateExp.getKindCode()) || 
				equalsKindCode("X2", compensateExp.getKindCode())|| 
				equalsKindCode("NX", compensateExp.getKindCode())||
				equalsKindCode("X1", compensateExp.getKindCode())||
				//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
				equalsKindCode("050201", compensateExp.getKindCode())||//农机损失保险
				equalsKindCode("050704", compensateExp.getKindCode())||//机上人员责任保险
				equalsKindCode("050601", compensateExp.getKindCode()))) {//农机第三者责任保险
				//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
				feeExp = feeExp + " - 残值";
			}
			/** 费用数字表达式*/
			/** if里的是车损模块专用*/
			if (compensateExp.getLossIndex() == null) {
				Double overAdjDigiAmount = 0.0;
				//取保额及出险时车辆实际价值中较小的一个进行超限额控制
				if (compensateExp.getDamageAmount() > compensateExp.getDamageItemRealCost()) {
					overAdjDigiAmount = compensateExp.getDamageItemRealCost();
				} else {
					overAdjDigiAmount = compensateExp.getDamageAmount();
				}
				if ("F34".equals(compensateExp.getClauseType()) || 
					"F35".equals(compensateExp.getClauseType()) || 
					"F44".equals(compensateExp.getClauseType()) || 
					"F45".equals(compensateExp.getClauseType())) {
					overAdjDigiAmount = compensateExp.getDamageAmount();
					}
				if (equalsKindCode("A", compensateExp.getKindCode()) || 
					equalsKindCode("X", compensateExp.getKindCode()) || 
					equalsKindCode("X1", compensateExp.getKindCode())||
					//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。begin
					equalsKindCode("050201", compensateExp.getKindCode())){//农机损失保险
					//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
					if (compensateExp.getSumLoss() > 0.0) {
						if ((compensateExp.getSumLoss() * compensateExp.getIndemnityDutyRate()) > overAdjDigiAmount) {
							feeDigitExp = feeDigitExp + compensateExp.formatPay(overAdjDigiAmount);
						} else {
							feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
						}
						//加施救费
						if (compensateExp.getRescueFee() > 0.0) {
							//A险施救费单占险额
							if ((compensateExp.getRescueFee() * compensateExp.getIndemnityDutyRate())> compensateExp.getDamageAmount() && equalsKindCode("A", compensateExp.getKindCode())) {
								feeDigitExp = feeDigitExp + " + " + compensateExp.formatPay(compensateExp.getDamageAmount());
							} else {
								feeDigitExp = feeDigitExp + " + " + compensateExp.formatPay(compensateExp.getRescueFee());
							}
						}
					} else if (compensateExp.getRescueFee() > 0.0) {
						//A险施救费单占险额
						if ((compensateExp.getRescueFee() * compensateExp.getIndemnityDutyRate()) > compensateExp.getDamageAmount() && equalsKindCode("A", compensateExp.getKindCode())) {
							feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getDamageAmount());
						} else {
							feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getRescueFee());
						}
					}
				} else {
					if (compensateExp.getSumLoss() > 0.0) {
						feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
						if (compensateExp.getRescueFee() > 0.0) {
							feeDigitExp = feeDigitExp + "＋" + compensateExp.formatPay(compensateExp.getRescueFee());
						}
					} else if (compensateExp.getRescueFee() > 0.0) {
						feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getRescueFee());
					}
				}
			} else {
				if (0.00 == compensateExp.getRescueFee()) {
					feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getSumLoss());
				} else {
					feeDigitExp = feeDigitExp + compensateExp.formatPay(compensateExp.getRescueFee());
				}
			}
			if (compensateExp.getSumRest() > 0.0 && 
			   (equalsKindCode("A", compensateExp.getKindCode()) || 
				equalsKindCode("B", compensateExp.getKindCode()) || 
				equalsKindCode("G", compensateExp.getKindCode()) || 
				equalsKindCode("D1", compensateExp.getKindCode())|| 
				equalsKindCode("D2", compensateExp.getKindCode())|| 
				equalsKindCode("E", compensateExp.getKindCode()) || 
				equalsKindCode("Z", compensateExp.getKindCode()) || 
				equalsKindCode("X", compensateExp.getKindCode()) || 
				equalsKindCode("T", compensateExp.getKindCode()) || 
				equalsKindCode("C6", compensateExp.getKindCode())|| 
				equalsKindCode("C9", compensateExp.getKindCode())|| 
				equalsKindCode("S", compensateExp.getKindCode()) || 
				equalsKindCode("U", compensateExp.getKindCode()) || 
				equalsKindCode("X2", compensateExp.getKindCode())|| 
				equalsKindCode("NX", compensateExp.getKindCode())||
				equalsKindCode("X1", compensateExp.getKindCode())||
				//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
				equalsKindCode("050201", compensateExp.getKindCode())||//农机损失保险
				equalsKindCode("050704", compensateExp.getKindCode())||//机上人员责任保险
				equalsKindCode("050601", compensateExp.getKindCode()))) {//农机第三者责任保险
				//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
				feeDigitExp = feeDigitExp + " - " + compensateExp.formatPay(compensateExp.getSumRest());
			}
			
			/** 交强已赔付*/
			bzPaid = bzPaid + compensateExp.getbZPaid();
			/** 赔付金额*/
			/** 赔付金额表达式*/
			itemPayExp = itemPay + "元";
			if (iterator.hasNext()) {
				feeExp = feeExp + " + ";
				feeDigitExp = feeDigitExp + " + ";
			} else {
				if (bzPaid > 0) {
					
					// pnc-8071 A险理算计算中车损赔款在理算汇总(旧系统格式)中计算结果与根据公式计算所得的结果不一致，单号RDAB200835020000002712 
					// modify by zhouwen(20081028)
					
					if(compensateExp.getSumRealLoss()>0 || compensateExp.getRescueRealFee()>0){
						// 车损赔款 <= 出险时险别保额(没有超限额)
						if(compensateExp.getSumRealLoss() <= compensateExp.getDamageAmount()){
							feeExp = feeExp + " - 定损交强已赔付金额";
							feeDigitExp = feeDigitExp + " - " + compensateExp.getSumLossBZPaid();
						}
						// 施救费赔款 <= 出险时险别保额(没有超限额)
						if(compensateExp.getRescueRealFee() <= compensateExp.getDamageAmount()){
							feeExp = feeExp + " - 施救费交强已赔付金额";
							feeDigitExp = feeDigitExp + " - " + compensateExp.getRescueFeeBZPaid();
						}
					}else{
						feeExp = feeExp + " - 扣交强险赔偿金额";
					feeDigitExp = feeDigitExp + " - " + compensateExp.formatPay(bzPaid);
					}
				}
				if (compensateExp.getDeductFee() > 0 && compensateExp.getClauseType().startsWith("F3")) {
					feeExp = feeExp + " - 不计免赔额";
					feeDigitExp = feeDigitExp + " - " + compensateExp.getDeductFee();
				}
			}
		}
	}
	
	/**
	 * 生成交强计算书费用表达式--规则调用(旧格式和诉讼格式)
	 */
	public void createBZFeeExp_Suit() {
		CompensateExp format = new CompensateExp();
		for (Iterator iterator = feeList.iterator(); iterator.hasNext();) {
			ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo) iterator.next();
			/** 初始化无责车辆损失信息*/
			if(thirdPartyLossInfo.getBzCompensateText()!=null){
				if (thirdPartyLossInfo.getBzCompensateText().indexOf("有责方车辆个数") != -1) {
					noDutyCarLossFlag = "1";
				}
				if (thirdPartyLossInfo.getBzCompensateText().indexOf("有责方车辆个数 - 1") != -1) {
					noDutyCarLossFlag = "2";
				}
			}
			/** 人员最开始的那个人员描述模块*/
			if(thirdPartyLossInfo.getLossIndex()!=null){
				if(thirdPartyLossInfo.getLicenseNo() == null || "".equals(thirdPartyLossInfo.getLicenseNo())){
					lossTable = "\n\t路面人员："+thirdPartyLossInfo.getLossName();
				} else if ("null".equals(thirdPartyLossInfo.getLicenseNo().trim())){
					lossTable = "\n\t路面人员："+thirdPartyLossInfo.getLossName();
				}
				else{
					//lossTable = "\n\t车牌号码为："+thirdPartyLossInfo.getLicenseNo()+" 的";
					if("050".equals(thirdPartyLossInfo.getLossItemType())){
						lossTable = "\n\t本车人员损失："+thirdPartyLossInfo.getLossName();
					}else if("010".equals(thirdPartyLossInfo.getLossItemType())){
						//第三者人员损失
						lossTable = "\n\t三者车人员损失："+thirdPartyLossInfo.getLossName();
					}else{
						lossTable = "\n\t车上人员损失："+thirdPartyLossInfo.getLossName();
					}
				}
			}else{
//				if("0".equals(thirdPartyLossInfo.getInsteadFlag())){
//					if(lossTable!=null){
//						lossTable = lossTable + "\n\t" + thirdPartyLossInfo.getLossName() + "：";
//					}else{
						lossTable = "\n\t" + thirdPartyLossInfo.getLossName() + "：";
//					}
//				}
			}
			
			/** 人员最开始的那个费用列表*/
			if ("".equals(feeTable)) {
				/** 车损模块专用*/
				if (thirdPartyLossInfo.getLossIndex() == null) {
					if("0".equals(thirdPartyLossInfo.getInsteadFlag())){
						if(thirdPartyLossInfo.getLossFeeName()!=null){
							feeTable = feeTable + "\t" + thirdPartyLossInfo.getLossFeeName() + "：" + format.formatPay(thirdPartyLossInfo.getSumLoss()) + "元";
						} else {
							feeTable = feeTable + "\t" + thirdPartyLossInfo.getLossFeeName() + "：" + format.formatPay(thirdPartyLossInfo.getSumLoss()) + "元";
						}
					}
				}else {//人员
					if(thirdPartyLossInfo.getLossFeeName()!=null && !"".equals(thirdPartyLossInfo.getLossFeeName())){
						feeTable = feeTable + "\t" + thirdPartyLossInfo.getLossFeeName() + "：" + format.formatPay(thirdPartyLossInfo.getSumLoss()) + "元";
					}
				}
			} else {
				if (thirdPartyLossInfo.getLossIndex() == null) {
					if("0".equals(thirdPartyLossInfo.getInsteadFlag())){
						if (thirdPartyLossInfo.getSumLoss() > 0.0) {
							feeTable = feeTable + "\n\t" + thirdPartyLossInfo.getLossFeeName() + "：" + format.formatPay(thirdPartyLossInfo.getSumLoss()) + "元";
						}
					}
				} else {
					if(thirdPartyLossInfo.getLossFeeName()!=null && !"".equals(thirdPartyLossInfo.getLossFeeName())){
						feeTable = feeTable + "\n\t" + thirdPartyLossInfo.getLossFeeName() + "：" + format.formatPay(thirdPartyLossInfo.getSumLoss()) + "元";
					}
				}
			}
			/** 费用中文表达式*/
			/** 车损模块专用*/
			if (thirdPartyLossInfo.getLossIndex() != null) {
				feeExp = feeExp + thirdPartyLossInfo.getLossFeeName();
			} else {
				if (thirdPartyLossInfo.getSumLoss() >= 0.0) {
					feeExp = feeExp + thirdPartyLossInfo.getLossFeeName();
				} else {
					feeExp = feeExp + thirdPartyLossInfo.getLossFeeName();
				}
			}
			
			/** 费用数字表达式*/
			/** if里的是车损模块专用*/
			if (thirdPartyLossInfo.getLossIndex() == null) {
				if (thirdPartyLossInfo.getSumLoss() >= 0.0) {
					feeDigitExp = feeDigitExp + format.formatPay(thirdPartyLossInfo.getSumLoss());
				}
			} else {
				feeDigitExp = feeDigitExp + format.formatPay(thirdPartyLossInfo.getSumLoss());
			}
			/** 赔付金额*/
			/** 赔付金额表达式*/
			itemPayExp = itemPay + "元";
			if (iterator.hasNext()) {
				feeExp = feeExp + " + ";
				feeDigitExp = feeDigitExp + " + ";
			}
		}
		for (Iterator iterator = feeList.iterator(); iterator.hasNext();) {
			ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo) iterator.next();
			if(thirdPartyLossInfo.getNoDutyCarPay() != null && thirdPartyLossInfo.getNoDutyCarPay()>0){
				feeExp = feeExp + " - 无责方赔付金额";
				feeDigitExp = feeDigitExp + " - " + format.formatPay(thirdPartyLossInfo.getNoDutyCarPay());
			}
		}
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

	private boolean equalsKindCode(String kindCode, String becompared) {
        /*
		HashMap<String, String> kindCode_Map = new HashMap<String, String>(0);
		
		kindCode_Map.put("BZ","050100");//机动车强制责任保险
    	kindCode_Map.put("A","050200");//机动车损失保险
    	kindCode_Map.put("L","050210");//车身划痕损失险条款
    	kindCode_Map.put("E","050220");//火灾、爆炸、自燃损失险条款
    	kindCode_Map.put("F","050231");//玻璃单独破碎险条款
    	kindCode_Map.put("Q2","050241");//家庭自用汽车多次出险增加免赔率特约条款
    	kindCode_Map.put("Q4","050251");//选择修理厂特约条款
    	kindCode_Map.put("Q3","050252");//指定修理厂特约条款
    	kindCode_Map.put("X","050260");//新增加设备损失保险条款
    	kindCode_Map.put("T","050270");//机动车停驶损失险条款
    	kindCode_Map.put("U","050280");//附加换件特约条款
    	kindCode_Map.put("X1","050291");//发动机特别损失险条款
    	kindCode_Map.put("X2","050320");//附加恐怖活动、群体性暴力事件车辆损失险
    	kindCode_Map.put("Z","050310");//自燃损失险条款
    	kindCode_Map.put("Z1","050320");//附加恐怖活动、群体性暴力事件车辆损失险
    	kindCode_Map.put("Q1","050330");//可选免赔额特约条款
    	kindCode_Map.put("K1","050350");//起重、装卸、挖掘车辆损失扩展条款
    	kindCode_Map.put("K2","050360");//特种车辆固定设备、仪器损坏扩展条款
    	kindCode_Map.put("C9","050370");//约定区域通行费用特约条款
    	kindCode_Map.put("C1","050381");//送油、充电服务特约条款
    	kindCode_Map.put("C2","050382");//拖车服务特约条款
    	kindCode_Map.put("C3","050384");//更换轮胎服务特约条款
    	kindCode_Map.put("C4","050385");//代步机动车服务特约条款
    	kindCode_Map.put("NZ","050410");//随车行李物品损失保险
    	kindCode_Map.put("NX","050421");//新车特约条款A
    	kindCode_Map.put("NY","050422");//新车特约条款B
    	kindCode_Map.put("C5","050621");//异地出险住宿费特约条款
    	kindCode_Map.put("G","050500");//盗抢险
    	kindCode_Map.put("C6","050510");//租车人人车失踪险条款
    	kindCode_Map.put("C8","050530");//免税机动车关税责任险条款
    	kindCode_Map.put("B","050600");//第三者责任保险
    	kindCode_Map.put("C7","050611");//法律费用特约条款
    	kindCode_Map.put("V1","050630");//附加油污污染责任保险
    	kindCode_Map.put("R","050640");//附加交通事故精神损害赔偿责任保险
    	kindCode_Map.put("D1","050700");//车上人员责任险
    	kindCode_Map.put("D11","050701");//车上人员责任险（司机）
    	kindCode_Map.put("D12","050702");//车上人员责任险（乘客）
    	kindCode_Map.put("D2","050800");//车上货物责任险
    	kindCode_Map.put("M","050900");//不计免赔率特约条款
    	//PNCCAR,代码走查：旧承保无以下险别，此处无需增加, FUXIN,DELETE,20100308,BEGIN
//    	kindCode_Map.put("M1","050911");//不计免赔率（车损险）
//    	kindCode_Map.put("M2","050912");//不计免赔率（三者险）
//    	kindCode_Map.put("M3","050921");//不计免赔率（机动车盗抢险）
//    	kindCode_Map.put("M4","050922");//不计免赔率（车身划痕损失险）
//    	kindCode_Map.put("M5","050923");//不计免赔率（新增加设备损失保险）
//    	kindCode_Map.put("M6","050924");//不计免赔率（发动机特别损失险）
//    	kindCode_Map.put("M7","050925");//不计免赔率（车上货物责任险）
//    	kindCode_Map.put("M8","050926");//不计免赔率（附加油污污染责任险）
//    	kindCode_Map.put("M9","050928");//不计免赔率（车上人员责任险（司机））
//    	kindCode_Map.put("M10","050929");//不计免赔率（车上人员责任险（乘客））
    	//PNCCAR,PNC-15648 , FUXIN,DELETE,20100308,END
    	kindCode_Map.put("Y3","050940");//教练车特约条款
    	kindCode_Map.put("K3","050970");//K3粤港、粤澳两地车区域扩展条款
    	kindCode_Map.put("S","050950");//附加机动车出境保险
    	kindCode_Map.put("J","050380");//机动车辆救助特约险条款
    	kindCode_Map.put("Q","050340");//绝对免赔额特约条款
    	kindCode_Map.put("D5","050703");//车上人员责任险(驾驶证考试用车)
    	kindCode_Map.put("W","050650");//无过失责任险
    	
    	kindCode_Map.put("050100","BZ");//机动车强制责任保险
    	kindCode_Map.put("050200","A");//机动车损失保险
    	kindCode_Map.put("050210","L");//车身划痕损失险条款
    	kindCode_Map.put("050220","E");//火灾、爆炸、自燃损失险条款
    	kindCode_Map.put("050231","F");//玻璃单独破碎险条款
    	kindCode_Map.put("050241","Q2");//家庭自用汽车多次出险增加免赔率特约条款
    	kindCode_Map.put("050251","Q4");//选择修理厂特约条款
    	kindCode_Map.put("050252","Q3");//指定修理厂特约条款
    	kindCode_Map.put("050260","X");//新增加设备损失保险条款
    	kindCode_Map.put("050270","T");//机动车停驶损失险条款
    	kindCode_Map.put("050280","U");//附加换件特约条款
    	kindCode_Map.put("050291","X1");//发动机特别损失险条款
    	kindCode_Map.put("050320","X2");//附加恐怖活动、群体性暴力事件车辆损失险
    	kindCode_Map.put("050310","Z");//自燃损失险条款
    	kindCode_Map.put("050320","Z1");//附加恐怖活动、群体性暴力事件车辆损失险
    	kindCode_Map.put("050330","Q1");//可选免赔额特约条款
    	kindCode_Map.put("050350","K1");//起重、装卸、挖掘车辆损失扩展条款
    	kindCode_Map.put("050360","K2");//特种车辆固定设备、仪器损坏扩展条款
    	kindCode_Map.put("050370","C9");//约定区域通行费用特约条款
    	kindCode_Map.put("050381","C1");//送油、充电服务特约条款
    	kindCode_Map.put("050382","C2");//拖车服务特约条款
    	kindCode_Map.put("050384","C3");//更换轮胎服务特约条款
    	kindCode_Map.put("050385","C4");//代步机动车服务特约条款
    	kindCode_Map.put("050410","NZ");//随车行李物品损失保险
    	kindCode_Map.put("050421","NX");//新车特约条款A
    	kindCode_Map.put("050422","NY");//新车特约条款B
    	kindCode_Map.put("050621","C5");//异地出险住宿费特约条款
    	kindCode_Map.put("050500","G");//盗抢险
    	kindCode_Map.put("050510","C6");//租车人人车失踪险条款
    	kindCode_Map.put("050530","C8");//免税机动车关税责任险条款
    	kindCode_Map.put("050600","B");//第三者责任保险
    	kindCode_Map.put("050611","C7");//法律费用特约条款
    	kindCode_Map.put("050630","V1");//附加油污污染责任保险
    	kindCode_Map.put("050640","R");//附加交通事故精神损害赔偿责任保险
    	kindCode_Map.put("050700","D1");//车上人员责任险
    	kindCode_Map.put("050701","D1");//D11车上人员责任险（司机）
    	kindCode_Map.put("050702","D1");//D12车上人员责任险（乘客）
    	kindCode_Map.put("050800","D2");//车上货物责任险
    	kindCode_Map.put("050900","M");//不计免赔率特约条款
    	kindCode_Map.put("050911","M");//M1不计免赔率（车损险）
    	kindCode_Map.put("050912","M");//M2不计免赔率（三者险）
    	kindCode_Map.put("050921","M");//M3不计免赔率（机动车盗抢险）
    	kindCode_Map.put("050922","M");//M4不计免赔率（车身划痕损失险）
    	kindCode_Map.put("050923","M");//M5不计免赔率（新增加设备损失保险）
    	kindCode_Map.put("050924","M");//M6不计免赔率（发动机特别损失险）
    	kindCode_Map.put("050925","M");//M7不计免赔率（车上货物责任险）
    	kindCode_Map.put("050926","M");//M8不计免赔率（附加油污污染责任险）
    	kindCode_Map.put("050928","M");//M9不计免赔率（车上人员责任险（司机））
    	kindCode_Map.put("050929","M");//M10不计免赔率（车上人员责任险（乘客））
    	kindCode_Map.put("050940","Y3");//教练车特约条款
    	kindCode_Map.put("050941","Y3");//Y3教练车特约条款（车损险）
    	kindCode_Map.put("050942","Y3");//Y4教练车特约条款（三者险）
    	kindCode_Map.put("050943","Y3");//Y5教练车特约条款（车上人员责任险（司机))
    	kindCode_Map.put("050944","Y3");//Y6教练车特约条款（车上人员责任险（乘客)）
    	kindCode_Map.put("050950","S");//附加机动车出境保险
    	kindCode_Map.put("050951","S");//S1附加机动车出境保险（车损险）
    	kindCode_Map.put("050952","S");//S2附加机动车出境保险（三者险）
    	kindCode_Map.put("050970","K3");//K3粤港、粤澳两地车区域扩展条款
    	kindCode_Map.put("050971","K3");//K3粤港、粤澳两地车区域扩展条款（车损险）
    	kindCode_Map.put("050972","K3");//K4粤港、粤澳两地车区域扩展条款（盗抢险）
    	kindCode_Map.put("050973","K3");//K5粤港、粤澳两地车区域扩展条款（玻璃单独破碎险）
    	kindCode_Map.put("050974","K3");//K6粤港、粤澳两地车区域扩展条款（火灾、爆炸、自燃损失险）
    	kindCode_Map.put("050380","J");//机动车辆救助特约险条款
    	kindCode_Map.put("050340","Q");//绝对免赔额特约条款
    	kindCode_Map.put("050703","D5");//车上人员责任险(驾驶证考试用车)
    	kindCode_Map.put("050650","W");//无过失责任险
    	//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。begin
    	kindCode_Map.put("050201","050201");//农机损失保险
    	kindCode_Map.put("050704","050704");//机上人员责任保险
    	kindCode_Map.put("050601","050601");//农机第三者责任保险
    	//PICCCAR XIACHENGCHENG BY ADD 2013-05-08 北京农机险产品条款更变需求，配合规则调整添加。end
		boolean isEquals = false;
		if (kindCode == null || "".equals(kindCode.trim()) || becompared == null || "".equals(becompared.trim())) {
			isEquals = false;
		} else {
			String kindCodeValue = kindCode_Map.get(kindCode.trim());
			String becomparedValue = kindCode_Map.get(becompared.trim());
			if (kindCode.trim().equalsIgnoreCase(becompared.trim())) {
				isEquals = true;
			} else if (kindCodeValue != null && kindCodeValue.trim().equalsIgnoreCase(becompared.trim())) {
				isEquals = true;
			} else if (becomparedValue != null && becomparedValue.trim().equalsIgnoreCase(kindCode.trim())) {
				isEquals = true;
			} else if (kindCodeValue != null && becomparedValue != null && kindCodeValue.trim().equalsIgnoreCase(becomparedValue.trim())) {
				isEquals = true;
			}
		}
		return isEquals;*/
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
	
	public List getFeeList() {
		return feeList;
	}
	public void setFeeList(List feeList) {
		this.feeList = feeList;
	}
	public String getFeeTable() {
		return feeTable;
	}
	public void setFeeTable(String feeTable) {
		this.feeTable = feeTable;
	}
	public String getLossTable() {
		return lossTable;
	}
	public void setLossTable(String lossTable) {
		this.lossTable = lossTable;
	}
	public Double getItemRealPay() {
		return itemRealPay;
	}
	public void setItemRealPay(Double itemRealPay) {
		this.itemRealPay = itemRealPay;
	}
	public Double getItem2RealPay() {
		return item2RealPay;
	}
	public void setItem2RealPay(Double item2RealPay) {
		this.item2RealPay = item2RealPay;
	}
	public Double getItemSuitPay() {
		return itemSuitPay;
	}
	public void setItemSuitPay(Double itemSuitPay) {
		this.itemSuitPay = itemSuitPay;
	}
	public String getItemSuitExp() {
		return itemSuitExp;
	}
	public void setItemSuitExp(String itemSuitExp) {
		this.itemSuitExp = itemSuitExp;
	}
	public String getRescueFeeText() {
		return rescueFeeText;
	}
	public void setRescueFeeText(String rescueFeeText) {
		this.rescueFeeText = rescueFeeText;
	}
	public double getRescueRealFee() {
		return rescueRealFee;
	}
	public void setRescueRealFee(double rescueRealFee) {
		this.rescueRealFee = rescueRealFee;
	}
	public double getSumRealLoss() {
		return sumRealLoss;
	}
	public void setSumRealLoss(double sumRealLoss) {
		this.sumRealLoss = sumRealLoss;
	}
	public String getSumRealLossText() {
		return sumRealLossText;
	}
	public void setSumRealLossText(String sumRealLossText) {
		this.sumRealLossText = sumRealLossText;
	}
	public double getSumDefLoss() {
		return sumDefLoss;
	}
	public void setSumDefLoss(double sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}
	public String getSumRealPayText() {
		return sumRealPayText;
	}
	public void setSumRealPayText(String sumRealPayText) {
		this.sumRealPayText = sumRealPayText;
	}
	public double getRecoveryPay() {
		return recoveryPay;
	}
	public void setRecoveryPay(double recoveryPay) {
		this.recoveryPay = recoveryPay;
	}
	
	
}
