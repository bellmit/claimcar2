package ins.sino.claimcar.carplatform.constant;


/**
 * 交易请求类型
 */
public enum RequestType {
	
	/** 报案登记 */
	RegistInfoCI("50","交强报案登记",PlatfromType.CI),
	
	/** 报案登记 */
	RegistInfoBI("V3101","商业报案登记",PlatfromType.BI),
	
	/** 交强立案登记 */
	ClaimCI("51","交强立案登记",PlatfromType.CI),
	
	/** 商业立案登记 */
	ClaimBI("V3102","商业立案登记",PlatfromType.BI),
	
	/** 交强结案登记 */
	EndCaseCI("52","交强结案登记",PlatfromType.CI),
	
	/** 商业结案登记 */
	EndCaseBI("V3104","商业结案登记",PlatfromType.BI),
	
	/** 交强单证收集 */
	CertifyCI("57","交强单证收集",PlatfromType.CI),
	
	/** 商业单证收集 */
	CertifyBI("V3109","商业单证收集",PlatfromType.BI),
	
	/** 交强理算核赔登记 */
	VClaimCI("58","交强理算核赔登记",PlatfromType.CI),
	
	/** 商业理算核赔登记 */
	VClaimBI("V3110","商业理算核赔登记",PlatfromType.BI),
	
	/** 查勘登记 --交强 **/
	CheckCI("55","交强查勘登记",PlatfromType.CI),
	
	/** 查勘登记 --商业 **/
	CheckBI("V3107","商业查勘登记",PlatfromType.BI),
	
	/** 案件注销-- 交强 */
	CancelInfoCI("54","交强案件注销",PlatfromType.CI),
	
	/** 案件注销-- 商业 */
	CancelInfoBI("V34","商业案件注销",PlatfromType.BI),
	
	/** 定核损登记-- 交强 */
	LossInfoCI("56","交强定核损登记",PlatfromType.CI),
	
	/** 定核损登记-- 商业 */
	LossInfoBI("V3108","商业定核损登记",PlatfromType.BI),
	
	/** 重开赔案登记-- 交强 */
	ReOpenAppCI("5A","交强重开赔案登记",PlatfromType.CI),
	
	/** 重开赔案登记-- 商业 */
	ReOpenAppBI("V3111","商业重开赔案登记",PlatfromType.BI),
	
	/** 交强赔款支付登记 */
	PaymentCI("59","交强赔款支付登记",PlatfromType.CI),
	
	/** 商业赔款支付登记 */
	PaymentBI("V3106","商业赔款支付登记",PlatfromType.BI),
	
	
	
	//------------//

	/** 交强 风险预警承保信息 */
	PolicyRiskWarnCI("5B","风险预警承保信息",PlatfromType.CI),

	/** 交强 风险预警理赔信息 */
	ClaimRiskWarnCI("5C","风险预警理赔信息",PlatfromType.CI),
	
	/** 交强 被代位信息 */
	SubrogationRiskWarnCI("61","被代位信息",PlatfromType.CI),
	
	/** 交强 结算码查询 */
	RecoveryRiskWarnCI("62","结算码查询",PlatfromType.CI),
	
	/** 商业 风险预警承保信息 */
	PolicyRiskWarnBI("V3112","风险预警承保信息",PlatfromType.BI),

	/** 商业 风险预警理赔信息 */
	ClaimRiskWarnBI("V3113","风险预警理赔信息",PlatfromType.BI),
	
	/** 商业 被代位信息 */
	SubrogationRiskWarnBI("V3204","被代位信息",PlatfromType.BI),
	
	/** 商业 结算码查询 */
	RecoveryRiskWarnBI("V3205","结算码查询",PlatfromType.BI),
	
	/** 锁定确定 查询 */

	LockQueryBI("V3201","锁定确定查询",PlatfromType.BI),
	
	/** 案件交互查询 */
	SubrogationCheck("11","案件交互查询",PlatfromType.HS),
	
	/** 互审确认 */
	Check("12","互审确认",PlatfromType.HS),
	
	/** 开始追偿确认 */
	RecoveryConfirm("V3114","开始追偿确认",PlatfromType.BI),
	
	/** 开始追偿确认 */
	RecoveryReturnConfirm("V3115","追偿回款确认",PlatfromType.BI),
	
	/** 未决最新估损金额上传 */
	EstimatedLossAmount("V3116","未决估损金额上传",PlatfromType.BI),
	

	LockConfirmQueryBI("V3201","锁定确定查询",PlatfromType.BI),
	
	/** 锁定确定 */
	LockConfirmBI("V3202","锁定确定",PlatfromType.BI),
	
	/** 锁定取消 */
	LockCancelBI("V3203","锁定取消",PlatfromType.BI),

	/** 代位求偿理赔交强信息 */
	SubrogationClaimCI("63","代位求偿理赔信息",PlatfromType.CI),

	/** 代位求偿理赔商业信息 */
	SubrogationClaimBI("V3206","代位求偿理赔信息",PlatfromType.BI),
	
	/** 结算查询 */
	AccountQuery("21","结算查询",PlatfromType.CA),
	
	/**
	 * 代位案件承保信息查询
	 */
	SubrogationPolicyCI("64","代位案件承保信息",PlatfromType.CI),
	
	/**
	 * 代位案件承保信息查询
	 */
	BizSubrogationPolicyBI("V3207","代位案件承保信息",PlatfromType.BI),
	
	/**
	 * 查询三者车风险预警信息
	 */
	ThirdCarInfo("V3113","查询三者车风险预警信息",PlatfromType.BI),

	// -------------------------------上海平台

	/** 报案登记 -- 上海交强 */
	RegistInfoCI_SH("50","交强报案登记_上海 ",PlatfromType.CI),
	
	/** 报案登记 -- 上海商业 */
	RegistInfoBI_SH("80","商业报案登记 _上海",PlatfromType.CI),
	
	/** 立案登记 -- 上海交强 */
	ClaimInfoCI_SH("51","交强立案登记_上海 ",PlatfromType.CI),
	
	/** 立案登记 -- 上海商业 */
	ClaimInfoBI_SH("81","商业立案登记 _上海",PlatfromType.CI),
	
	/** 查勘、定损、核损登记 -- 上海交强 */
	LossInfoCI_SH("56","交强查勘/定损/核损登记 _上海",PlatfromType.CI),
	
	/** 查勘、定损、核损登记 -- 上海商业 */
	LossInfoBI_SH("86","商业查勘/定损/核损登记 _上海",PlatfromType.CI),
	
	/** 交强理算核赔登记 */
	VClaimCI_SH("55","交强理算核赔登记_上海",PlatfromType.CI),
	
	/** 商业理算核赔登记 */
	VClaimBI_SH("84","商业理算核赔登记_上海",PlatfromType.CI),
	
	/** 结案追加 -- 上海交强 */
	EndCaseAddInfoCI_SH("53","交强结案追加 _上海",PlatfromType.CI),
	
	/** 结案追加 -- 上海商业 */
	EndCaseAddInfoBI_SH("85","商业结案追加 _上海",PlatfromType.CI),
	
	/** 赔款支付 -- 上海交强 */
	PaymentCI_SH("58","交强赔款支付_上海",PlatfromType.CI),
	
	/** 赔款支付 -- 上海商业 */
	PaymentBI_SH("58","商业赔款支付_上海",PlatfromType.CI),
	
	/** 赔款金额确认 -- 上海交强 */
	EndCaseCI_SH("52","交强赔款金额确认_上海",PlatfromType.CI),
	
	/** 赔款金额确认 -- 上海商业 */
	EndCaseBI_SH("82","商业赔款金额确认_上海",PlatfromType.CI),
	
	/** 案件注销-- 上海交强 */
	CancelInfoCI_SH("54","交强案件注销_上海",PlatfromType.CI),
	
	/** 案件注销-- 上海商业 */
	CancelInfoBI_SH("83","商业案件注销_上海",PlatfromType.CI),
	
	/** 交强单证收集 */
	CertifyCI_SH("57","交强单证收集_上海",PlatfromType.CI),
	
	/** 商业单证收集 */
	CertifyBI_SH("57","商业单证收集_上海",PlatfromType.CI),
	
	/** 上海代位求偿信息抄回请求 */
	SubrogationClaim_SH("C1","上海代位求偿信息抄回",PlatfromType.CI),
	;
	
	private String code;
	private String name;
	private PlatfromType platformType;

	private RequestType(String code,String name,PlatfromType platformType){
		this.code = code;
		this.name = name;
		this.platformType = platformType;
	}

	/**
	 * 交易类型代码，这里是业务系统自定义，与平台的requestcode无关
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 本类型中文名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public PlatfromType getPlatformType() {
		return platformType;
	}

	/**
	 * 获取枚举类型
	 *
	 * @param code
	 */
	public static RequestType getEnumByCode(String code) {
		RequestType[] enums = RequestType.values();
		for (RequestType en : enums) {
			if (en.getCode().equals(code)) {
				return en;
			}
		}
		throw new IllegalArgumentException("上传平台请求类型不存在");
	}
}
