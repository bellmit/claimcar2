package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 理算公式数据传输大对象
 * @author XkfLZtt
 *
 */
public class CompensateExp implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 报案号*/
	private String registNo;
	/** 损失类型代码*/
	private String feeTypeCode;
	/** 损失类型名称*/
	private String feeTypeName;
	/** 条款代码*/
	private String clauseType;
	/** 赔案类别*/
	private String claimType;
	/** 诉讼案标志位*/
	private String isSuitFlag;
	/** 协议金额*/
	private double exgratiaFee;
	/** 车牌号*/
	private String licenseNo;
	/** 材料费*/
	private double materialFee;
	/** 管理费*/
	private double managerFee;
	/** 工时费*/
	private double manHourFee;
	/** 残值*/
	private double sumRest;
	/** 核定修理费用*/
	private double sumLoss;
	/** 车损赔款*/
	private double sumRealLoss;
	/** 车损计算公式*/
	private String sumRealLossText;
	/** 施救费*/
	private double rescueFee;
	/** 施救费赔款*/
	private double rescueRealFee;
	/** 施救费计算公式*/
	private String rescueFeeText;
	/** 其它扣除*/
	private double otherDeductFee;
	/** 无责代赔金额*/
	private double absolvePayAmt;
	/** 责任比例*/
	private double indemnityDutyRate;
	/** 免赔率*/
	private double deductibleRate;
	/** 事故免赔率 或 绝对免赔率*/
	private double dutyDeductibleRate;
	/** 事故可选免赔率*/
	private double selectDeductibleRate;
	/** 赔付比例*/
	private double lossRate;
	/** 险别代码*/
	private String kindCode;
	/** 险别名称*/
	private String kindName;
	/** 险种代码*/
	private String riskCode;
	/** 损失类型*/
	private String lossFeeType;
	/** 不计免赔率*/
	private double exceptDeductRate;
	/** 不计免赔金额*/
	private double exceptDeductible;
	/** 理算金额*/
	private double sumDefLoss;
	/** 核定金额*/
	private double sumRealPay;
	/** 裁定金额*/
	private double sumRealPayJud;
	/** 核定损失金额*/
	private double sumRealPayLoss;
	/** 核定施救金额*/
	private double sumRealPayRescue;
	
	/** 免赔额*/
	private double deductFee;
	/** 实赔金额计算公式*/
	private String sumRealPayText;
	/**代位计算公式*/
	private String dwPayText;
	/** 理算计算公式*/
	private String compensateExp;
	/** 理算按險別展示计算公式*/
	private String extraCompensateExp;
	/** 理算最后的匯總公式*/
	private String otherExp;
	/** 终端版理算计算公式*/
	private String oldCompensateExp;
	/** 诉讼案理算计算公式*/
	private String suitCompensateExp;
	/** 出险时车辆实际价值*/
	private double damageItemRealCost;
	/** 投保时保险车辆新车购置价*/
	private double entryItemCarCost;
	/** 出险时新增设备实际价值 */
	private double deviceActualValue;
	/** 投保时保险车辆新增设备购置价 */
	private double devicePurchasePrice;
	/** 总的交强险已赔金额*/
	private double bZPaid;
	/** 定损小计交强险已赔金额*/
	private double sumLossBZPaid;
	/** 施救费交强险已赔金额*/
	private double rescueFeeBZPaid;
	/** 历史赔付金额*/
	private double historySumRealPay;
	/** 出险时险别保额*/
	private double damageAmount;
	/** 事故责任比例代码 */
	private String indemnityDuty;
	/** 免赔条件List*/
	private List deductCondList;
	/** 需要筛选的免赔条件List*/
	private List checkDeductConds = new ArrayList();
	/** 不需要筛选的免赔条件List*/
	private List noCheckDeductConds = new ArrayList();
	/** 险别List*/
	private List kindCodeList;
	/** 免赔条件*/
	private String deductCond;
	/** 免赔次数*/
	private String deductTimes;
	/** key 险别代码 value 免赔率*/
	private Map deductRateMap = new HashMap();
	/** key 险别代码 value 不计免赔责任免除率*/
	private Map exceptRateMap = new HashMap();
	/** 理算大对象的 损失项信息s */
	private List thirdPartyLossInfos;
	/** 交强无责代赔信息 */
	private List noDutyLossInfos;
	
	/** 理算大对象的 涉案车辆s */
	private List thirdPartyInfos;
	/** 理算大对象的 涉案代交强赔付对象s (商业计算书：标的车代责任对方交强车辆)*/
	private List thirdPartyRecoveryInfos;
	/** 承保时间*/
	private Date operateDate;
	/** 代步最高赔偿天数*/
	private double quantity;
	/** 单位保险金额*/
	private double unitAmount;
	/** 保额,新承保D1拆分为050701和050702,050701(司机)不再延用unitAmount */
	private double amount;
	/** 出险次数*/
	private double claimNum;
	/** 数量,天数*/
	private double lossQuantity;
	/** 交强损失类型*/
	private String damageType;
	/** 该险别是否投保了不计免赔特约 0:否、1:是*/
	private String checkPolicyM;
	/** 是否承保了Q2险  1:是   0：否*/
	private int checkPolicyQ2;
	/** 免赔率公式*/
	private String deductibleRateText;
	/** 受损信息数组*/
	private ThirdPartyLossInfo[] thirdPartyLossInfoAry;
	/** 出险车辆信息数组*/
	private ThirdPartyInfo[] thirdPartyInfoAry;

	/** 不计免赔标记 ,1-表示当前表达式是不计免赔的 add by liuping 2016-6-16 */
	private int msumFlag;
	/** 不计免赔率赔付金额*/
	private double msumRealPay;
	/** 不计免赔率赔付金额*/
	private double msumRealPayJud;
	/** 不计免赔率赔付损失金额 */
	private double msumRealPayLoss;
	/** 不计免赔率赔付施救金额*/
	private double msumRealPayRescue;
	/** 人员损失费用名称*/
	private String personChargeName;
	/** 人员损失序号*/
	private String lossIndex;
	/** 人员损失序号*/
	private int intLossIndex;
	/** 序号*/
	private int index;
	/** 分项损失赔付比例*/
	private double claimRate;
	/** 分项损失施救比例*/
	private double claimRescueRate;
	/** 损失项名称  车：车牌号  财：财物名称  人：人名  其他：车/财名称*/
	private String lossName;
	/** 计算书类型*/
	private int compensateType;
	/** 保留事故责任比例标志位*/
	private int isAlone;
    /** 是否将不计免赔金额纳入本次计算*/
	private String flagOfM;
	/** 出险日期*/
	private Date damageDate;
	/** 使用月数*/
	private int useMonths;
	/** 折旧率*/
	private double depreRate;
	/** 机构代码*/
	private String comCode;
	/** 人员描述文本*/
	private String lossText;
	/** 损失项描述文本*/
	private String itemText;
	/** 人员总损失金额*/
	private double sumLossAll;
	/** 出险原因代码*/
	private String damageCode;
	/** D1险下单个人员的损失项序号*/
	private int lossNumPerPerson;
	/** 同一个人员下的损失项数目*/
	private int lossAmountPerPerson;
	/** 此变量用于暂存计算中的金额 */
	private double tempMoney = 0;
	/**单项损失超限额调整 */
	private double itemSumRealPay=0;
	/**是否单项损失超限额调整 */
	private String itemFlag="";
	/**PNC-11214 */
	/** 判断D1险超限额标志位 */
	private String overFlag="";
	/** 代位求偿 */
	private String subrogationFlag;
	/** 没有找到第三方标志**/
	private String noFindThirdFlag;
	
	/** 是否承保  无法找到第三方特约险 **/
	private String kindCodeNTFlag;
	
	/** 2012-02-06 代位求偿需求添加
	 * 1:追偿
	 * 2:清付
	 * 3:自付(上代位后的新案件)
	 * 历史案件为null或””:普通理算
	 **/
	/** 代位类型 */
	private String payType;
	
	private double absrate=0;
	
	/** 2012-02-06 代位求偿需求添加
	 * 1:追偿
	 * 2:清付
	 * 3:自付
	 **/
	/** 损失项代位类型 */
	private String recoveryOrPayFlag;
	
	/**
	 * 代位(交强/商业)类型
	 * 1:代付交强 2:代付商业
	 */
	private String oppoentCoverageType;
	
	/** 被锁车辆交强应赔付 */
	private double lockedBzRealPay;
	
	/** 责任对方事故责任比例 */
	private double thirdCarDutyRate;
	
	/** 追交强获得赔款 */
	private double subrogationBzRealPay;
	
	/** 追商业获得赔款 */
	private double subrogationBRealPay;
	
	/** 追商业不计免赔 */
	private double msumSubrogation;
	
	/** 责任对方追偿款 */
	private double recoveryPay;
	
	/** 商业计算书：标的车代责任对方交强车辆*/
	private ThirdPartyRecoveryInfo[] thirdPartyRecoveryInfoAry;
	
	/** 损失类型：车/财/人/其他 */
	private String expType;
	private String roundFlag;//四舍五入标志 2:五入 1：四舍 0：不需四舍五入
	private String roundFlagM;//四舍五入标志 2:五入 1：四舍 0：不需四舍五入
	private String roundFlagExp;//每个理算大对象四舍五入标志 2:五入 1：四舍 0：不需四舍五入
	private  double  amountForX;//050461 发动机涉水险，对限额调整
	/** 车损险限额 */
	private double kindCodeAAmount;
//	 费改版本 1-费改前，2-费改后 此标志只用于代位计算
	private String clauseIssue;
	/**追偿计算书自付部分不计免赔率赔付金额*/
	private double msumRealPayZF;
	/**追偿计算书自付部分核定金额*/
	private double sumRealPayZF;
	/** 追偿计算书自付部分车损赔款*/
	private double sumRealLossZF;
	/** 追偿计算书自付部分施救费赔款*/
	private double rescueRealFeeZF;
	private String thirdPartyId;//涉案车辆Id号
	private String prpLplatRecoveryInfoId;//代位车辆代位ID
	
    private Object lossItem;
    private double sumPrePay;
    private String mSumRealPayText;
    
    private String totalPayText="";
    private boolean isNotFirst4CompulsoryCompensate = false;
    private double msumRealPayBeforeJud;
    
    private String cetainLossType;
    private double thirdPaidAmt;// 第三者已赔付金额  用于代位时取消锁定时记录扣减掉的取消追偿金额 代位求偿时展示
    //是否买了附加绝对免赔险
    private boolean bugFlag=false;
	
	@SuppressWarnings("unchecked")
	public void addDiscount(String code, double discount) {
		double sum = 0;
		if(deductRateMap.containsKey(code)) {
			sum = ((Double)deductRateMap.get(code)).doubleValue() + discount;
		}else {
			sum = discount;
		}
		if (sum > 100){
			sum = 100;
		}
		deductRateMap.put(code, new Double(sum));
	}
	@SuppressWarnings("unchecked")
	public void addExceptRate(String code, double discount) {
		double sum = 0;
		if(exceptRateMap.containsKey(code)) {
			sum = ((Double)exceptRateMap.get(code)).doubleValue() + discount;
		}else {
			sum = discount;
		}
		exceptRateMap.put(code, new Double(sum));
	}
	/**
	 * 将赔款金额按格式进行格式化输出
	 *
	 * @param pay
	 * @return
	 */
	public String formatPay(double sumRealPay){
		if(!Double.isNaN(sumRealPay)){
			double number = new BigDecimal(sumRealPay).setScale(2,4).doubleValue();
			return new DecimalFormat("#,##0.00").format(number);
		}else{
			return new DecimalFormat("#,##0.00").format(sumRealPay);
		}
	}

	/**
	 * 免赔条件筛选，如果同时选择了多个免赔条件需要筛选
	 */
	@SuppressWarnings("unchecked")
	public void checkDeduct(DeductRate deductRate){
		if(checkDeductConds != null && checkDeductConds.size() < 1){
			checkDeductConds.add(deductRate);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addThirdPartyLossInfos(ThirdPartyCompInfo thirdPartyCompInfo) {
		ThirdPartyLossInfo thirdPartyLossInfo = new ThirdPartyLossInfo();
		thirdPartyLossInfo.setLicenseNo(thirdPartyCompInfo.getLicenseNo());
		thirdPartyLossInfo.setSumLoss(thirdPartyCompInfo.getSumLoss());
		thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount());
		thirdPartyLossInfo.setObjectType(thirdPartyCompInfo.getObjectType());
		thirdPartyLossInfo.setObjectIndex(thirdPartyCompInfo.getObjectIndex());
		thirdPartyLossInfo.setSerialNo(thirdPartyCompInfo.getSerialNo());
		thirdPartyLossInfo.setSumRealPay(thirdPartyCompInfo.getSumRealPay());
		thirdPartyLossInfo.setSumRealPayJud(thirdPartyCompInfo.getSumRealPay());
		thirdPartyLossInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText());
		thirdPartyLossInfo.setLossName(thirdPartyCompInfo.getLossName());
		thirdPartyLossInfo.setFeeTypeCode(thirdPartyCompInfo.getFeeTypeCode());
		thirdPartyLossInfo.setLossItemType(thirdPartyCompInfo.getLossItemType());
		thirdPartyLossInfo.setBzDutyType(thirdPartyCompInfo.getBzDutyType());
		if(thirdPartyCompInfo.getBzDutyType()!=null && "1".equals(thirdPartyCompInfo.getBzDutyType())){
			thirdPartyLossInfo.setBzDutyFlag(true);
		}
		
		thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
		thirdPartyLossInfo.setExpType(thirdPartyCompInfo.getExpType());
		thirdPartyLossInfo.setDamageType(thirdPartyCompInfo.getDamageType());
		thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmout());
		thirdPartyLossInfo.setZfFlag(thirdPartyCompInfo.getZfFlag());
		thirdPartyLossInfo.setIsAdd("1");
		if(thirdPartyCompInfo.getPayAmount()>thirdPartyCompInfo.getAdjPayAmount()){
			//此处为第三方车赔给本车,而”16交强试算_20080201_财产_交强“中“16按损失项顺序返回实赔(代赔部分)”为本车赔给三方车..
			thirdPartyLossInfo.setOverFlag("1");
		}
		
		//把旧数据删除,把新数据添加进去..
		for(int i=0;i<thirdPartyLossInfos.size();i++){
			ThirdPartyLossInfo oldThirdPartyLossInfo = (ThirdPartyLossInfo) thirdPartyLossInfos.get(i);
			if (thirdPartyCompInfo.getObjectIndex() != null && thirdPartyCompInfo.getObjectIndex().equals(oldThirdPartyLossInfo.getObjectIndex())) {
				thirdPartyLossInfo.setDamageAmount(oldThirdPartyLossInfo.getDamageAmount());
				thirdPartyLossInfo.setSumRealPayJud(oldThirdPartyLossInfo.getSumRealPayJud());
				if (oldThirdPartyLossInfo.getInsteadFlag() != null && "0".equals(oldThirdPartyLossInfo.getInsteadFlag())) {
					oldThirdPartyLossInfo.setIsAdd("2");
				}
			}
		}
		thirdPartyLossInfos.add(thirdPartyLossInfo);
	}

	@SuppressWarnings("unchecked")
	public void checkNoDeduct(DeductRate deductRate){
		noCheckDeductConds.add(deductRate);
	}

	/**
	 * 将不需要筛选的List拷贝到需要筛选的List中
	 */
	@SuppressWarnings("unchecked")
	public void copyCheckDeductConds(){
		for (Iterator iter = noCheckDeductConds.iterator(); iter.hasNext();) {
			DeductRate deductRate = (DeductRate) iter.next();
			checkDeductConds.add(deductRate);
		}
	}

	/**
	 * 节字段功能
	 * @return
	 */
	public String subString(String string) {
		String tmpString = "";
		tmpString = string.substring(1);
		return tmpString;
	}
	
	/**
	 * 解决M险1分钱问题
	 * @return
	 */
	public void adjMsumRealPay() {
		/**截取小数点后两位的M险赔付金额 */
		//double m_result_2 = 0.00;
		/**M险小数点后两位后面的那部分金额 */
		/*double m_result_rest = 0.00;
		if (msumRealPay > 0) {
			String str = msumRealPay + "";
			int length = str.indexOf(".");
			if(str.length()>=length+3){
				m_result_2 = Double.parseDouble(str.substring(0, length + 3));
			}else{
				m_result_2 = msumRealPay;
			}
			m_result_rest = new BigDecimal(msumRealPay).setScale(8, 4).doubleValue()
					- m_result_2;
			sumRealPay = sumRealPay + m_result_rest;
			msumRealPay = m_result_2;
		} else {
//			/**解决单笔计算不准确的问题*/
//			sumRealPay = sumRealPay + 0.000001;
		//}
	}
	
	/**
	 * 将不需要筛选的List拷贝到需要筛选的List中
	 */
	public void deleteThirdPartyLossInfos(){
		thirdPartyLossInfos.clear();
	}
	
	public String getFeeTypeCode() {
		return feeTypeCode;
	}
	
	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}
	
	public String getFeeTypeName() {
		return feeTypeName;
	}
	
	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}
	
	public String getClauseType() {
		return clauseType;
	}
	
	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}
	
	public String getClaimType() {
		return claimType;
	}
	
	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public double getMaterialFee() {
		return materialFee;
	}
	
	public void setMaterialFee(double materialFee) {
		this.materialFee = materialFee;
	}
	
	public double getManagerFee() {
		return managerFee;
	}
	
	public void setManagerFee(double managerFee) {
		this.managerFee = managerFee;
	}
	
	public double getManHourFee() {
		return manHourFee;
	}
	
	public void setManHourFee(double manHourFee) {
		this.manHourFee = manHourFee;
	}
	
	public double getSumRest() {
		return sumRest;
	}
	
	public void setSumRest(double sumRest) {
		this.sumRest = sumRest;
	}
	
	public double getSumLoss() {
		return sumLoss;
	}
	
	public void setSumLoss(double sumLoss) {
		this.sumLoss = sumLoss;
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
	
	public double getRescueFee() {
		return rescueFee;
	}
	
	public void setRescueFee(double rescueFee) {
		this.rescueFee = rescueFee;
	}
	
	public double getRescueRealFee() {
		return rescueRealFee;
	}
	
	public void setRescueRealFee(double rescueRealFee) {
		this.rescueRealFee = rescueRealFee;
	}
	
	public String getRescueFeeText() {
		return rescueFeeText;
	}
	
	public void setRescueFeeText(String rescueFeeText) {
		this.rescueFeeText = rescueFeeText;
	}
	
	public double getOtherDeductFee() {
		return otherDeductFee;
	}
	
	public void setOtherDeductFee(double otherDeductFee) {
		this.otherDeductFee = otherDeductFee;
	}
	
	public double getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	
	public void setIndemnityDutyRate(double indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	
	public double getDeductibleRate() {
		return deductibleRate;
	}
	
	public void setDeductibleRate(double deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	
	public double getDutyDeductibleRate() {
		return dutyDeductibleRate;
	}
	
	public void setDutyDeductibleRate(double dutyDeductibleRate) {
		this.dutyDeductibleRate = dutyDeductibleRate;
	}
	
	public double getSelectDeductibleRate() {
		return selectDeductibleRate;
	}
	
	public void setSelectDeductibleRate(double selectDeductibleRate) {
		this.selectDeductibleRate = selectDeductibleRate;
	}
	
	public double getLossRate() {
		return lossRate;
	}
	
	public void setLossRate(double lossRate) {
		this.lossRate = lossRate;
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
	
	public String getRiskCode() {
		return riskCode;
	}
	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
	public String getLossFeeType() {
		return lossFeeType;
	}
	
	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}
	
	public double getExceptDeductRate() {
		return exceptDeductRate;
	}
	
	public void setExceptDeductRate(double exceptDeductRate) {
		this.exceptDeductRate = exceptDeductRate;
	}
	
	public double getExceptDeductible() {
		return exceptDeductible;
	}
	
	public void setExceptDeductible(double exceptDeductible) {
		this.exceptDeductible = exceptDeductible;
	}
	
	public double getSumDefLoss() {
		return sumDefLoss;
	}
	
	public void setSumDefLoss(double sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}
	
	public double getSumRealPay() {
		return sumRealPay;
	}
	
	public void setSumRealPay(double sumRealPay) {
		this.sumRealPay = sumRealPay;
	}
	
	public double getSumRealPayJud() {
		return sumRealPayJud;
	}
	
	public void setSumRealPayJud(double sumRealPayJud) {
		this.sumRealPayJud = sumRealPayJud;
	}
	
	public double getSumRealPayLoss() {
		return sumRealPayLoss;
	}
	
	public void setSumRealPayLoss(double sumRealPayLoss) {
		this.sumRealPayLoss = sumRealPayLoss;
	}
	
	public double getSumRealPayRescue() {
		return sumRealPayRescue;
	}
	
	public void setSumRealPayRescue(double sumRealPayRescue) {
		this.sumRealPayRescue = sumRealPayRescue;
	}
	
	public double getDeductFee() {
		return deductFee;
	}
	
	public void setDeductFee(double deductFee) {
		this.deductFee = deductFee;
	}
	
	public String getSumRealPayText() {
		return sumRealPayText;
	}
	
	public void setSumRealPayText(String sumRealPayText) {
		this.sumRealPayText = sumRealPayText;
	}
	
	public String getCompensateExp() {
		return compensateExp;
	}
	
	public void setCompensateExp(String compensateExp) {
		this.compensateExp = compensateExp;
	}
	
	public String getExtraCompensateExp() {
		return extraCompensateExp;
	}
	
	public void setExtraCompensateExp(String extraCompensateExp) {
		this.extraCompensateExp = extraCompensateExp;
	}
	
	public String getOtherExp() {
		return otherExp;
	}
	
	public void setOtherExp(String otherExp) {
		this.otherExp = otherExp;
	}
	
	public String getOldCompensateExp() {
		return oldCompensateExp;
	}
	
	public void setOldCompensateExp(String oldCompensateExp) {
		this.oldCompensateExp = oldCompensateExp;
	}
	
	public String getSuitCompensateExp() {
		return suitCompensateExp;
	}
	
	public void setSuitCompensateExp(String suitCompensateExp) {
		this.suitCompensateExp = suitCompensateExp;
	}
	
	public double getDamageItemRealCost() {
		return damageItemRealCost;
	}
	
	public void setDamageItemRealCost(double damageItemRealCost) {
		this.damageItemRealCost = damageItemRealCost;
	}
	
	public double getEntryItemCarCost() {
		return entryItemCarCost;
	}
	
	public void setEntryItemCarCost(double entryItemCarCost) {
		this.entryItemCarCost = entryItemCarCost;
	}
	
	public double getDeviceActualValue() {
		return deviceActualValue;
	}
	
	public void setDeviceActualValue(double deviceActualValue) {
		this.deviceActualValue = deviceActualValue;
	}
	
	public double getDevicePurchasePrice() {
		return devicePurchasePrice;
	}
	
	public void setDevicePurchasePrice(double devicePurchasePrice) {
		this.devicePurchasePrice = devicePurchasePrice;
	}
	
	public double getbZPaid() {
		return bZPaid;
	}
	
	public void setbZPaid(double bZPaid) {
		this.bZPaid = bZPaid;
	}
	
	public double getSumLossBZPaid() {
		return sumLossBZPaid;
	}
	
	public void setSumLossBZPaid(double sumLossBZPaid) {
		this.sumLossBZPaid = sumLossBZPaid;
	}
	
	public double getRescueFeeBZPaid() {
		return rescueFeeBZPaid;
	}
	
	public void setRescueFeeBZPaid(double rescueFeeBZPaid) {
		this.rescueFeeBZPaid = rescueFeeBZPaid;
	}
	
	public double getHistorySumRealPay() {
		return historySumRealPay;
	}
	
	public void setHistorySumRealPay(double historySumRealPay) {
		this.historySumRealPay = historySumRealPay;
	}
	
	public double getDamageAmount() {
		return damageAmount;
	}
	
	public void setDamageAmount(double damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	
	public List getDeductCondList() {
		return deductCondList;
	}
	
	public void setDeductCondList(List deductCondList) {
		this.deductCondList = deductCondList;
	}
	
	public List getCheckDeductConds() {
		return checkDeductConds;
	}
	
	public void setCheckDeductConds(List checkDeductConds) {
		this.checkDeductConds = checkDeductConds;
	}
	
	public List getNoCheckDeductConds() {
		return noCheckDeductConds;
	}
	
	public void setNoCheckDeductConds(List noCheckDeductConds) {
		this.noCheckDeductConds = noCheckDeductConds;
	}
	
	public List getKindCodeList() {
		return kindCodeList;
	}
	
	public void setKindCodeList(List kindCodeList) {
		this.kindCodeList = kindCodeList;
	}
	
	public String getDeductCond() {
		return deductCond;
	}
	
	public void setDeductCond(String deductCond) {
		this.deductCond = deductCond;
	}
	
	public String getDeductTimes() {
		return deductTimes;
	}
	
	public void setDeductTimes(String deductTimes) {
		this.deductTimes = deductTimes;
	}
	
	public Map getDeductRateMap() {
		return deductRateMap;
	}
	
	public void setDeductRateMap(Map deductRateMap) {
		this.deductRateMap = deductRateMap;
	}
	
	public Map getExceptRateMap() {
		return exceptRateMap;
	}
	
	public void setExceptRateMap(Map exceptRateMap) {
		this.exceptRateMap = exceptRateMap;
	}
	
	public List getThirdPartyLossInfos() {
		return thirdPartyLossInfos;
	}
	
	public void setThirdPartyLossInfos(List thirdPartyLossInfos) {
		this.thirdPartyLossInfos = thirdPartyLossInfos;
	}
	
	public List getThirdPartyInfos() {
		return thirdPartyInfos;
	}
	
	public void setThirdPartyInfos(List thirdPartyInfos) {
		this.thirdPartyInfos = thirdPartyInfos;
	}
	
	public List getThirdPartyRecoveryInfos() {
		return thirdPartyRecoveryInfos;
	}
	
	public void setThirdPartyRecoveryInfos(List thirdPartyRecoveryInfos) {
		this.thirdPartyRecoveryInfos = thirdPartyRecoveryInfos;
	}
	
	public Date getOperateDate() {
		return operateDate;
	}
	
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	
	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public double getUnitAmount() {
		return unitAmount;
	}
	
	public void setUnitAmount(double unitAmount) {
		this.unitAmount = unitAmount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getClaimNum() {
		return claimNum;
	}
	
	public void setClaimNum(double claimNum) {
		this.claimNum = claimNum;
	}
	
	public double getLossQuantity() {
		return lossQuantity;
	}
	
	public void setLossQuantity(double lossQuantity) {
		this.lossQuantity = lossQuantity;
	}
	
	public String getDamageType() {
		return damageType;
	}
	
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	
	public String getCheckPolicyM() {
		return checkPolicyM;
	}
	
	public void setCheckPolicyM(String checkPolicyM) {
		this.checkPolicyM = checkPolicyM;
	}
	
	public int getCheckPolicyQ2() {
		return checkPolicyQ2;
	}
	
	public void setCheckPolicyQ2(int checkPolicyQ2) {
		this.checkPolicyQ2 = checkPolicyQ2;
	}
	
	public String getDeductibleRateText() {
		return deductibleRateText;
	}
	
	public void setDeductibleRateText(String deductibleRateText) {
		this.deductibleRateText = deductibleRateText;
	}
	
	public ThirdPartyLossInfo[] getThirdPartyLossInfoAry() {
		return thirdPartyLossInfoAry;
	}
	
	public void setThirdPartyLossInfoAry(ThirdPartyLossInfo[] thirdPartyLossInfoAry) {
		this.thirdPartyLossInfoAry = thirdPartyLossInfoAry;
	}
	
	public ThirdPartyInfo[] getThirdPartyInfoAry() {
		return thirdPartyInfoAry;
	}
	
	public void setThirdPartyInfoAry(ThirdPartyInfo[] thirdPartyInfoAry) {
		this.thirdPartyInfoAry = thirdPartyInfoAry;
	}
	
	public double getMsumRealPay() {
		return msumRealPay;
	}
	
	public void setMsumRealPay(double msumRealPay) {
		this.msumRealPay = msumRealPay;
	}
	
	public double getMsumRealPayJud() {
		return msumRealPayJud;
	}
	
	public void setMsumRealPayJud(double msumRealPayJud) {
		this.msumRealPayJud = msumRealPayJud;
	}
	
	public double getMsumRealPayLoss() {
		return msumRealPayLoss;
	}

	public void setMsumRealPayLoss(double msumRealPayLoss) {
		this.msumRealPayLoss = msumRealPayLoss;
	}

	public double getMsumRealPayRescue() {
		return msumRealPayRescue;
	}
	
	public void setMsumRealPayRescue(double msumRealPayRescue) {
		this.msumRealPayRescue = msumRealPayRescue;
	}
	
	public String getPersonChargeName() {
		return personChargeName;
	}
	
	public void setPersonChargeName(String personChargeName) {
		this.personChargeName = personChargeName;
	}
	
	public String getLossIndex() {
		return lossIndex;
	}
	
	public void setLossIndex(String lossIndex) {
		this.lossIndex = lossIndex;
	}
	
	public int getIntLossIndex() {
		return intLossIndex;
	}
	
	public void setIntLossIndex(int intLossIndex) {
		this.intLossIndex = intLossIndex;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public double getClaimRate() {
		return claimRate;
	}
	
	public void setClaimRate(double claimRate) {
		this.claimRate = claimRate;
	}
	
	public double getClaimRescueRate() {
		return claimRescueRate;
	}
	
	public void setClaimRescueRate(double claimRescueRate) {
		this.claimRescueRate = claimRescueRate;
	}
	
	public String getLossName() {
		return lossName;
	}
	
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}
	
	public int getCompensateType() {
		return compensateType;
	}
	
	public void setCompensateType(int compensateType) {
		this.compensateType = compensateType;
	}
	
	public int getIsAlone() {
		return isAlone;
	}
	
	public void setIsAlone(int isAlone) {
		this.isAlone = isAlone;
	}
	
	public String getFlagOfM() {
		return flagOfM;
	}
	
	public void setFlagOfM(String flagOfM) {
		this.flagOfM = flagOfM;
	}
	
	public Date getDamageDate() {
		return damageDate;
	}
	
	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}
	
	public int getUseMonths() {
		return useMonths;
	}
	
	public void setUseMonths(int useMonths) {
		this.useMonths = useMonths;
	}
	
	public double getDepreRate() {
		return depreRate;
	}
	
	public void setDepreRate(double depreRate) {
		this.depreRate = depreRate;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public String getLossText() {
		return lossText;
	}
	
	public void setLossText(String lossText) {
		this.lossText = lossText;
	}
	
	public String getItemText() {
		return itemText;
	}
	
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}
	
	public double getSumLossAll() {
		return sumLossAll;
	}
	
	public void setSumLossAll(double sumLossAll) {
		this.sumLossAll = sumLossAll;
	}
	
	public String getDamageCode() {
		return damageCode;
	}
	
	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}
	
	public int getLossNumPerPerson() {
		return lossNumPerPerson;
	}
	
	public void setLossNumPerPerson(int lossNumPerPerson) {
		this.lossNumPerPerson = lossNumPerPerson;
	}
	
	public int getLossAmountPerPerson() {
		return lossAmountPerPerson;
	}
	
	public void setLossAmountPerPerson(int lossAmountPerPerson) {
		this.lossAmountPerPerson = lossAmountPerPerson;
	}
	
	public double getTempMoney() {
		return tempMoney;
	}
	
	public void setTempMoney(double tempMoney) {
		this.tempMoney = tempMoney;
	}
	
	public double getItemSumRealPay() {
		return itemSumRealPay;
	}
	
	public void setItemSumRealPay(double itemSumRealPay) {
		this.itemSumRealPay = itemSumRealPay;
	}
	
	public String getItemFlag() {
		return itemFlag;
	}
	
	public void setItemFlag(String itemFlag) {
		this.itemFlag = itemFlag;
	}
	
	public String getOverFlag() {
		return overFlag;
	}
	
	public void setOverFlag(String overFlag) {
		this.overFlag = overFlag;
	}
	
	public String getSubrogationFlag() {
		return subrogationFlag;
	}
	
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}
	
	public String getPayType() {
		return payType;
	}
	
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}
	
	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
	}
	
	public String getOppoentCoverageType() {
		return oppoentCoverageType;
	}
	
	public void setOppoentCoverageType(String oppoentCoverageType) {
		this.oppoentCoverageType = oppoentCoverageType;
	}
	
	public double getLockedBzRealPay() {
		return lockedBzRealPay;
	}
	
	public void setLockedBzRealPay(double lockedBzRealPay) {
		this.lockedBzRealPay = lockedBzRealPay;
	}
	
	public double getThirdCarDutyRate() {
		return thirdCarDutyRate;
	}
	
	public void setThirdCarDutyRate(double thirdCarDutyRate) {
		this.thirdCarDutyRate = thirdCarDutyRate;
	}
	
	public double getSubrogationBzRealPay() {
		return subrogationBzRealPay;
	}
	
	public void setSubrogationBzRealPay(double subrogationBzRealPay) {
		this.subrogationBzRealPay = subrogationBzRealPay;
	}
	
	public double getSubrogationBRealPay() {
		return subrogationBRealPay;
	}
	
	public void setSubrogationBRealPay(double subrogationBRealPay) {
		this.subrogationBRealPay = subrogationBRealPay;
	}
	
	public double getRecoveryPay() {
		return recoveryPay;
	}
	
	public void setRecoveryPay(double recoveryPay) {
		this.recoveryPay = recoveryPay;
	}
	
	public ThirdPartyRecoveryInfo[] getThirdPartyRecoveryInfoAry() {
		return thirdPartyRecoveryInfoAry;
	}
	
	public void setThirdPartyRecoveryInfoAry(ThirdPartyRecoveryInfo[] thirdPartyRecoveryInfoAry) {
		this.thirdPartyRecoveryInfoAry = thirdPartyRecoveryInfoAry;
	}
	
	public String getExpType() {
		return expType;
	}
	
	public void setExpType(String expType) {
		this.expType = expType;
	}
	
	public String getRoundFlag() {
		return roundFlag;
	}
	
	public void setRoundFlag(String roundFlag) {
		this.roundFlag = roundFlag;
	}
	
	public String getRoundFlagM() {
		return roundFlagM;
	}
	
	public void setRoundFlagM(String roundFlagM) {
		this.roundFlagM = roundFlagM;
	}
	
	public String getRoundFlagExp() {
		return roundFlagExp;
	}
	
	public void setRoundFlagExp(String roundFlagExp) {
		this.roundFlagExp = roundFlagExp;
	}
	
	public double getAmountForX() {
		return amountForX;
	}
	
	public void setAmountForX(double amountForX) {
		this.amountForX = amountForX;
	}
	
	public double getKindCodeAAmount() {
		return kindCodeAAmount;
	}
	
	public void setKindCodeAAmount(double kindCodeAAmount) {
		this.kindCodeAAmount = kindCodeAAmount;
	}
	
	public String getClauseIssue() {
		return clauseIssue;
	}
	
	public void setClauseIssue(String clauseIssue) {
		this.clauseIssue = clauseIssue;
	}
	
	public double getMsumRealPayZF() {
		return msumRealPayZF;
	}
	
	public void setMsumRealPayZF(double msumRealPayZF) {
		this.msumRealPayZF = msumRealPayZF;
	}
	
	public double getSumRealPayZF() {
		return sumRealPayZF;
	}
	
	public void setSumRealPayZF(double sumRealPayZF) {
		this.sumRealPayZF = sumRealPayZF;
	}
	
	public double getSumRealLossZF() {
		return sumRealLossZF;
	}
	
	public void setSumRealLossZF(double sumRealLossZF) {
		this.sumRealLossZF = sumRealLossZF;
	}
	
	public double getRescueRealFeeZF() {
		return rescueRealFeeZF;
	}
	
	public void setRescueRealFeeZF(double rescueRealFeeZF) {
		this.rescueRealFeeZF = rescueRealFeeZF;
	}
	
	public String getThirdPartyId() {
		return thirdPartyId;
	}
	
	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	
	public String getPrpLplatRecoveryInfoId() {
		return prpLplatRecoveryInfoId;
	}
	
	public void setPrpLplatRecoveryInfoId(String prpLplatRecoveryInfoId) {
		this.prpLplatRecoveryInfoId = prpLplatRecoveryInfoId;
	}
	
	public Object getLossItem() {
		return lossItem;
	}
	
	public void setLossItem(Object lossItem) {
		this.lossItem = lossItem;
	}
	
	public double getSumPrePay() {
		return sumPrePay;
	}
	
	public void setSumPrePay(double sumPrePay) {
		this.sumPrePay = sumPrePay;
	}
	
	public String getmSumRealPayText() {
		return mSumRealPayText;
	}
	
	public void setmSumRealPayText(String mSumRealPayText) {
		this.mSumRealPayText = mSumRealPayText;
	}
	
	public boolean isNotFirst4CompulsoryCompensate() {
		return isNotFirst4CompulsoryCompensate;
	}
	
	public void setNotFirst4CompulsoryCompensate(boolean isNotFirst4CompulsoryCompensate) {
		this.isNotFirst4CompulsoryCompensate = isNotFirst4CompulsoryCompensate;
	}
	
	public boolean isIsNotFirst4CompulsoryCompensate() {
		return isNotFirst4CompulsoryCompensate;
	}
	
	public double getMsumRealPayBeforeJud() {
		return msumRealPayBeforeJud;
	}
	
	public void setMsumRealPayBeforeJud(double msumRealPayBeforeJud) {
		this.msumRealPayBeforeJud = msumRealPayBeforeJud;
	}
	
	public String getTotalPayText() {
		return totalPayText;
	}
	
	public void setTotalPayText(String totalPayText) {
		this.totalPayText = totalPayText;
	}
	
	public String getCetainLossType() {
		return cetainLossType;
	}
	
	public void setCetainLossType(String cetainLossType) {
		this.cetainLossType = cetainLossType;
	}
	public List getNoDutyLossInfos() {
		return noDutyLossInfos;
	}
	public void setNoDutyLossInfos(List noDutyLossInfos) {
		this.noDutyLossInfos = noDutyLossInfos;
	}
	public double getAbsolvePayAmt() {
		return absolvePayAmt;
	}
	public void setAbsolvePayAmt(double absolvePayAmt) {
		this.absolvePayAmt = absolvePayAmt;
	}
	public String getNoFindThirdFlag() {
		return noFindThirdFlag;
	}
	public void setNoFindThirdFlag(String noFindThirdFlag) {
		this.noFindThirdFlag = noFindThirdFlag;
	}
	public String getKindCodeNTFlag() {
		return kindCodeNTFlag;
	}
	public void setKindCodeNTFlag(String kindCodeNTFlag) {
		this.kindCodeNTFlag = kindCodeNTFlag;
	}

	public int getMsumFlag() {
		return msumFlag;
	}

	public void setMsumFlag(int msumFlag) {
		this.msumFlag = msumFlag;
	}
	
	public String getDwPayText() {
		return dwPayText;
	}
	public void setDwPayText(String dwPayText) {
		this.dwPayText = dwPayText;
	}
	public double getMsumSubrogation() {
		return msumSubrogation;
	}
	public void setMsumSubrogation(double msumSubrogation) {
		this.msumSubrogation = msumSubrogation;
	}
	public double getThirdPaidAmt() {
		return thirdPaidAmt;
	}
	public void setThirdPaidAmt(double thirdPaidAmt) {
		this.thirdPaidAmt = thirdPaidAmt;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public double getAbsrate() {
		return absrate;
	}
	public void setAbsrate(double absrate) {
		this.absrate = absrate;
	}
	public boolean getBugFlag() {
		return bugFlag;
	}
	public void setBugFlag(boolean bugFlag) {
		this.bugFlag = bugFlag;
	}
	
	
	
}
