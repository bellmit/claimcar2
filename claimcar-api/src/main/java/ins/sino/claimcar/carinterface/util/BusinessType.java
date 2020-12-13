/******************************************************************************
 * CREATETIME : 2016年9月21日 上午10:28:14
 ******************************************************************************/
package ins.sino.claimcar.carinterface.util;



/**
 * <pre>
 * 业务类型
 * </pre>
 * @author ★Luwei
 */
public enum BusinessType {

	/** 方正 */
	Founder_carRegist("1","理赔送方正(车险报案)"),
	Founder_scheduleInfo("13","理赔送方正(调度信息更新)"),
	Founder_registCancel("14","理赔送方正(报案注销推)"),
	Founder_PolicyRelation("15","理赔送方正(保单关联)"),
	Founder_cancelDfloss("16","理赔送方正(调度注销定损)"),
	Founder_registCancelCtAtn("52","理赔送方正(公估车险案件注销)"),
    
	/** 再保 */
	Reinsurance_verify("2","核赔通过送再保已决数据分摊试算"),
	Reinsurance_claim("3","立案送再保未决数据分摊试算"),
	Reinsurance_cancel("4","再保处理结案/注销/拒赔以及重开赔案业务"),
	Reinsurance_assessor("19","公估费送再保"),
	Reinsurance_checkFee("20","查勘费送再保"),
	Reinsurance_vatBack("22","VAT回写送再保"),
	Reinsurance_washTransaction("23","冲销送再保"),
	/** 收付 */
	Payment_prePay("5","预付送收付"),
	Payment_padPay("6","垫付送收付"),
	Payment_compe("7","实赔送收付"),
	Payment_recPay("8","追偿送收付"),
	Payment_recLoss("9","损余回收送收付"),
	Payment_assessor("10","公估费送收付"),
	Payment_dw("11","追偿确认送收付"),
	Payment_qf("12","清付确认送收付"),
	Payment_checkFee("13","查勘费送收付"),

	
	/** 消保 */
	HNIS_claim("13","立案送河南消保"),
	HNIS_endCase("14","结案送河南消保"),
	GZIS_claim("15","立案送贵州消保"),
	GZIS_endCase("16","结案送贵州消保"),
	GXIS_regist("20","报案送广西消保"),
	GXIS_endCase("21","结案送广西消保"),
	
	/** 账户信息修改 */
	ModAccount("17","修改账户信息送收付"),
	AssessorFee_Invoice("18","公估费送发票"),
	AssessorFee_BackTicket("99","公估费退票送收付"),
	CheckFee_Invoice("100","查勘费送发票"),
	CheckFee_BackTicket("101","查勘费退票送收付"),
	

	/** 警保 **/
	PolicyLinkAge_currentVersion("30","警保版本号"),
	PolicyLinkAge_caseInfos("31","警保案件列表"),
	PolicyLinkAge_caseDetails("32","警保案件详细信息"),
	
	Payment_backPrePay("33","预付更新送收付"),
	ControlExpert("98","德联易控详细信息"),
	
	/** 河南快赔 **/
	HNQC_PhotoVerify("34","河南快赔照片审核"),
	HNQC_DLossResult("35","河南快赔定损结果"),
	HNQC_endCas("39","河南快赔结案、拒赔、注销，0结案"),
	
	/** 车童网 **/
	CT_Regist("36","理赔送车童网(车险报案)"),
	CT_registCancel("37","理赔送车童网(报案注销推)"),
	CT_claimCancel("38","理赔送车童网(立案注销推)"),
	CT_cancelDfloss("40","理赔送车童网(调度注销定损)"),
	CT_scheduleChange("41","理赔送车童网(调度改派推)"),
	CT_handOver("42","理赔送车童网(平级移交推)"),


	CT_claimCancelRestore("50","理赔送车童网(立案注销恢复推)"),
	CT_caseCancleNoPass("81","理赔送车童网(案件注销申请不通过)"),


	/** 民太安 **/
	MTA_Regist("45","理赔送民太安(车险报案)"),
	MTA_registCancel("43","理赔送民太安(报案注销推)"),




	MTA_claimCancel("46","理赔送民太安(立案注销推)"),
	MTA_cancelDfloss("47","理赔送民太安(调度注销定损)"),
	MTA_scheduleChange("48","理赔送民太安(调度改派推)"),
	MTA_handOver("49","理赔送民太安(平级移交推)"),
	MTA_claimCancelRestore("51","理赔送民太安(立案注销恢复推)"),
	MTA_caseCancleNoPass("80","理赔送民太安(案件注销申请不通过)"),
	
	GENILEX_Regist("82","精励联讯(报案接口推送)"),
	GENILEX_Check("83","精励联讯(查勘接口推送)"),
	GENILEX_Dloss("84","精励联讯(定损接口推送)"),
	GENILEX_EndCase("85","精励联讯(结案接口推送)"),
	CIITC_Regist("86","事故信息查询接口"),
	CIITC_Compe("87","中保信（理赔信息）"),

	/**山东风险预警系统**/
	SDEW_regist("C0301","山东风险预警系统(报案登记)"),
	SDEW_claim_CI("C0302_CI","山东风险预警系统(交强立案登记)"),
	SDEW_claim_BI("C0302_BI","山东风险预警系统(商业立案登记)"),
	SDEW_check_CI("C0303_CI","山东风险预警系统(交强查勘登记)"),
	SDEW_check_BI("C0303_BI","山东风险预警系统(商业查勘登记)"),
	SDEW_vLoss("C0304","山东风险预警系统(定核损登记)"),
	SDEW_vClaim("C0305","山东风险预警系统(理算核赔登记)"),
	SDEW_endCase("C0306","山东风险预警系统(结案登记)"),
	SDEW_claimCancel("C0307","山东风险预警系统(案件注销登记)"),
	SDEW_reOpen("C0308","山东风险预警系统(重开案件登记)"),
	SDEW_imgUpload("C04","山东风险预警系统(图片上传)"),
	SDEW_imgCompar("C05","山东风险预警系统(影像比对)"),
	SDEW_EWInfo("C06","山东风险预警系统(预警推送)"),
	SDEW_riskCase("C07","山东风险预警系统(重复/虚假案件回写)"),
	SDEW_riskImg_CI("C08_CI","山东风险预警系统(交强重复/虚假案件标记)"),
	SDEW_riskImg_BI("C08_BI","山东风险预警系统(商业重复/虚假案件标记)"),
	
	/** 深圳警保保安信息上传 --商业 */
	SZReg_BI("SZReg_BI","深圳警保商业报案信息上传"),
	
	/** 深圳警保保安信息上传 --交强 */
	SZReg_CI("SZReg_CI","深圳警保交强报案信息上传"),
	
	/** 深圳警保商业理赔信息上传 --商业 */
	SZClaim_BI("SZClaim_BI","深圳警保商业理赔信息上传"),
	
	/** 深圳警保交强理赔信息上传 --交强 */
	SZClaim_CI("SZClaim_CI","深圳警保交强理赔信息上传"),

	/** 广东车辆数据综合服务平台数据采集-理赔部分 **/
	GDEW_regist("C0301","广东风险预警系统(报案登记)"),
	GDEW_claim_CI("C0302_CI","广东风险预警系统(交强立案登记)"),
	GDEW_claim_BI("C0302_BI","广东风险预警系统(商业立案登记)"),
	GDEW_check_CI("C0303_CI","广东风险预警系统(交强查勘登记)"),
	GDEW_check_BI("C0303_BI","广东风险预警系统(商业查勘登记)"),
	GDEW_vLoss("C0304","广东风险预警系统(定核损登记)"),
	GDEW_vClaim("C0305","广东风险预警系统(理算核赔登记)"),
	GDEW_endCase("C0306","广东风险预警系统(结案登记)"),
	GDEW_claimCancel("C0307","广东风险预警系统(案件注销登记)"),
	GDEW_reOpen("C0308","广东风险预警系统(重开案件登记)"),
	GDEW_imgUpload("C04","广东风险预警系统(图片上传)"),
	GDEW_imgCompar("C05","广东风险预警系统(影像比对)"),
	GDEW_EWInfo("C06","广东风险预警系统(预警推送)"),
	GDEW_riskCase("C07","广东风险预警系统(重复/虚假案件回写)"),
	GDEW_riskImg_CI("C08_CI","广东风险预警系统(交强重复/虚假案件标记)"),
	GDEW_riskImg_BI("C08_BI","广东风险预警系统(商业重复/虚假案件标记)"),

	/** 河南交管平台数据对接-理赔部分 **/
	HNEW_regist("C0301","河南风险预警系统(报案登记)"),
	HNEW_claim_CI("C0302_CI","河南风险预警系统(交强立案登记)"),
	HNEW_claim_BI("C0302_BI","河南风险预警系统(商业立案登记)"),
	HNEW_check_CI("C0303_CI","河南风险预警系统(交强查勘登记)"),
	HNEW_check_BI("C0303_BI","河南风险预警系统(商业查勘登记)"),
	HNEW_vLoss("C0304","河南风险预警系统(定核损登记)"),
	HNEW_vClaim("C0305","河南风险预警系统(理算核赔登记)"),
	HNEW_endCase("C0306","河南风险预警系统(结案登记)"),
	HNEW_claimCancel("C0307","河南风险预警系统(案件注销登记)"),
	HNEW_reOpen("C0308","河南风险预警系统(重开案件登记)"),
	HNEW_imgUpload("C04","河南风险预警系统(图片上传)"),
	HNEW_imgCompar("C05","河南风险预警系统(影像比对)"),
	HNEW_EWInfo("C06","河南风险预警系统(预警推送)"),
	HNEW_riskCase("C07","河南风险预警系统(重复/虚假案件回写)"),
	HNEW_riskImg_CI("C08_CI","河南风险预警系统(交强重复/虚假案件标记)"),
	HNEW_riskImg_BI("C08_BI","河南风险预警系统(商业重复/虚假案件标记)"),
	JY_Task("86","精友(任务状态同步)"),
	JY_CleanData("86","精友(推定全损数据清空接口)"),
	JY_ZeroNotice("86","精友(定损零结通知接口)"),
	JY_ViewData("86","精友(定损查看接口)"),
	JY_Regist("86","精友(报案请求接口)"),
	YJ_claimAdd("YJ01","阳杰询价新增接口"),
	YJ_claimSupply("YJ04","阳杰下单接口"),
	YJ_DLCHK("YJ_01","阳杰汽配复勘新增接口"),
	YJ_VLoss("YJ_02","阳杰汽配核损接口"),
	SDEW_riskImg("C08","山东风险预警系统(重复/虚假案件标记)"),
	/**自助理赔**/
	SELFCLAIM_CORE_002("A006","理赔通知接口"),
	/** 费用/赔款送vat */
	INVOICE_VAT("Invoice","推送发票费用/赔款"),
	COURT_Search("90","高院查询接口"),
	SZWARNINGINSTANCE("91","深圳警情信息下载"),
	DL_CarInfo("DL_CAR","德联易控接口"),
	SC_policyInfo("SC_policyInfo","保险理赔信息写入"),
	/**vat相关接口**/
	BILL_Image("BILL_Image","发票图片信息推送VAT接口"),
	BILL_Result("BILL_Result","发票验真接口"),
	Batch_Recall("Batch_Recall","批次请求撤回接口"),
	BILL_BACK("BILL_BACK","发票打回接口"),
	BILL_Register("BILL_Register","发票绑定接口"),
	BILL_ImageCount("BILL_ImageCount","发票归集")
;
	
	
	private String code;
	private String name;
	
	private BusinessType(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	

}
