package ins.sino.claimcar;

import ins.framework.common.DateTime;
import ins.sino.claimcar.flow.constant.FlowNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CodeConstants {

	public static final String Gradeid = "5195";// 自助查勘岗位表Id
    public static final String compName="鼎和保险";//公司名称
	public static final String TOPCOM = "00010000";
	public static final String STARFLAG = "starFlag";// 脱敏处理开关
	public static final String OUTREPARIFLAG = "outRepariFlag";// 外修开关
	public static final String ASSESSORFEE = "assessorFee"; // 公估费用金额开关
	public static final String SWITCHMAP = "switchMap"; // 电子地图开关
	public static final String AssignDis = "assignDis"; // 不轮询开关 
	public static final String newBillDate = "newBillDate";// 新旧发票数据时间分界点
	/**
	 * 下一节点任务指定给当前人开关 1-是 0-否
	 */
	public static final String ISSPECIFIEDUSER = "isSpecifiedUser";
	public static final String MobileCheck = "mobileCheck"; // 移动查勘开关

	public static final String controlExpert = "controlExpert"; // 德联易控信息开关
	public static final String PINGANTOPALTFORMFLAG = "pingAnToPaltFormFlag"; //平安案件送收付开关
	public static final String CTCheck = "CTCheck"; // 车童开关
	public static final String MTACheck = "MTACheck"; // 民太安开关
	public static final String ILOGVALIDFLAG = "iLogValidFlag"; // iLog开关
	public static final String RULEVALIDFLAG = "ruleValidFlag"; // iLog业务开关
	public static final String REGISTTOPALTFORMFLAG = "registToPaltFormFlag"; // 报案送平台的报文开关
	public static final String GENILEX = "GENILEX"; // 精励联讯开关
	public static final String REPAIRPHONE = "REPAIRPHONE"; // 推送修手机号码开关
	public static final String PlatformOptimized = "platformOptimized";	//平台优化开关
	public static final String HNKPFNODE = "CO9"; //河南快赔父节结点
	public static final String HNKPSNODE1 = "CO91"; //河南快赔子节结点
	public static final String HNKPSNODE2 = "CO92"; //河南快赔子节结点
	public static final String HNKPSNODE3 = "CO93"; //河南快赔子节结点
	public static final String APPCODEL1="claim-certify";//影像类型代码
	public static final String APPNAMEL1="车险理赔-索赔清单";//影像类型名称
	public static final String APPCODEL2="claim-picture";//影像类型代码
	public static final String APPNAMEL2="车险理赔-损失图片";//影像类型名称
	public static final String APPCODECLAIM="claimcar";//影像类型代码
	public static final String APPNAMECLAIM="车险理赔";//影像类型名称
	public static final String APPROLE="admin";//影像访问者身份编码
	public static final String YXQUERYCODE="ECM0002";//影像查看类型代码
	public static final String YXUPDATECODE="ECM0001";//影像修改类型代码
	public static final String YXResourceCODE="ECM0010";//影像资源请求代码
	public static final String PolicyNoFromt_SH = "22"; // 上海保单开头
	public static final String SURVEY = "SURVEY"; // 发起调查开关
	public static final String YXSUMCODE="ECM0006";//影像统计代码
	public static final String YXDOWNLOADCODE="ECM0010";//影像资源下载代码
	public static final String YXRIGHTCODE="CRUT";//影像资料权限
	public static final String PAYTOVAT = "payToVat"; //赔款送vat
	public static final String licenseNoStart = "粤Z";
	public static final String YXRIGHTCODED="CRUTD";//影像资料权限
	public static final String YXQUERYPARAM="YXQUERYPARAM";//普通影像上传
	public static final String YXASSESSORSFEE="YXASSESSORSFEE";//公估费影像上传
	public static final String YXCHECKSFEE="YXCHECKSFEE";//查勘费影像上传
	public static final String headOffice = "00000000";
	public static final String secretType = "MD5";
	public static final String SCVERSION = "101001";
	// 鼎和财产保险股份有限公司深圳分公司
	public static final String DHICCODE = "DHIC";
	public static final String CEDHICCODE = "DHIC001";//德联易控专用
	public static final String DHICNAME = "鼎和财产保险股份有限公司深圳分公司";
	public static final String JBPFNODE_SZ="JBPFNODE_SZ";
	public static final String JBPFNODE="JBPFNODE";
	
	//2020年车险综合改革分机构配置
	public static final String newBussiness2020Flag = "newBussiness2020Flag";

	//人伤赔偿标准标识
	public static final String paystandard="yearAreaCode";
	
	//发票标识
	public static final String BILLCODE="BILL";

	//总公司审核开关
	public static final String headOfficeAudit = "headOfficeAudit";

	//反欺诈 车辆查询开关
	public static final String sendVehicleQuery = "sendVehicleQuery";
	//反欺诈 人员查询开关
	public static final String sendPersonQuery = "sendPersonQuery";
	//反欺诈 报案电话查询开关
	public static final String sendReportPhoneQuery = "sendReportPhoneQuery";

	/** 是否临时报案 */
	public static class TempReport{
		public static final String NOT_TEMPREPORT = "0";
		public static final String TEMPREPORT = "1";
	}

	/** 案件紧急程度 */
	public static class CaseTag{
		public static final String NORMAL = "0";
		public static final String EMERGENCY = "1";
	}
	
	/** 是否单车事故或者通用是否 */
	public static class IsSingleAccident{
		public static final String NOT = "0";
		public static final String YES = "1";
	}
	
	/** 保单关联与取消 */
	public static class RelateOperatType{
		public static final String CANCEL_RELATION = "0";
		public static final String TO_RELATION = "1";
		public static final String CANCEL_RELATION_CHECK = "2";
		public static final String TO_RELATION_CHECK  = "3";
	}
	
	/** 定损类别 或者1 是蓝票，2是红票*/
	public static class DeflossType{
		public static final String CarLoss = "1";
		public static final String PropLoss = "2";
	}
	
	/** 报案任务状态 */
	public static class RegistTaskFlag{

		/** 已暂存 */
		public static final String PEND = "0";
		/** 已提交 */
		public static final String SUBMIT = "1";
		/** 已注销 */
		public static final String CANCELED = "7";
	}
	
	/** 工作流整体状态 add by LiuPing */
	public static class FlowStatus {

		/** 正常处理 */
		public static final String NORMAL = "N";
		/** 注销 */
		public static final String CANCEL = "C";
		/** 完成 */
		public static final String END = "E";
	}

	/** 工作流状态 add by LiuPing */
	public static class HandlerStatus {

		/** 未处理-初始状态 */
		public static final String INIT = "0";
		/** 已接收未处理 */
		public static final String START = "1";
		/** 正在处理 */
		public static final String DOING = "2";
		/** 完成处理 */
		public static final String END = "3";
		/** 注销 */
		public static final String CANCEL = "9";
	}

	/** 工作状态 add by LiuPing */
	public static class WorkStatus {

		/** 未处理-初始状态 */
		public static final String INIT = "0";
		/** 已接收未处理 */
		public static final String START = "1";
		/** 正在处理 */
		public static final String DOING = "2";
		/** 完成处理 */
		public static final String END = "3";
		/** 被挂起 */
		public static final String PAUSE = "4";
		/** 处理完成 已回退 */
		public static final String BACK = "5";
		/** 还未处理 被回退 */
		public static final String BYBACK = "6";
		/** 已移交 */
		public static final String TURN = "7";
		/** 已改派 */
		public static final String CHANGE = "8";
		/** 注销 */
		public static final String CANCEL = "9";

		/** 未处理-虚拟节点，不能点击进去处理 */
		public static final String VIRT = "A";
	}

	/** 报案保单类型 */
	public static class ReportType{

		/** 商业 */
		public static final String BI = "1";
		/** 交强 */
		public static final String CI = "2";
		/** 交强+商业 */
		public static final String CI_BI = "3";
	}
	
	/** 注销/拒赔标志 */
	public static class CancelFlag{

		/** 注销 */
		public static final String CANCEL = "C";
		/** 拒赔 */
		public static final String REJEST = "R";
	}
	
	/** 损失方 */
	public static class LossParty{

		/** 地面 */
		public static final String GROUND = "0";
		/** 标的 */
		public static final String TARGET = "1";
		/** 三者 */
		public static final String THIRD = "3";
	}
	
	public static class ScheduleDefSource{

		/** 查勘后定损调度 */
		public static final String SCHEDULECHECK = "0";
		/** 直接定损调度 */
		public static final String SCHEDULEDEF = "1";
		/** 追加定损调度 */
		public static final String SCHEDULEADD = "2";
		/** 修改定损调度 */
		public static final String SCHEDULEMODIFY = "3";
	}
	
	/** 定损来源 */
	public static class defLossSourceFlag{

		/** 正常定损 */
		public static final String SCHEDULDEFLOSS = "1";
		/** 追加定损 */
		public static final String ADDTIONALDEFLOSS = "2";
		/** 修改定损 */
		public static final String MODIFYDEFLOSS = "3";
	}
	
	/**
	 * 定损方式
	 * 
	 * <pre></pre>
	 * @author ★ZhouYanBin
	 */
	public static class CetainLossType{

		/** 修复定损 */
    	public static final String DEFLOSS_REPAIR="01";
		/** 推定全损 */
    	public static final String DEFLOSS_ALL="02";
		/** 盗抢折旧 */
    	public static final String DEFLOSS_ROBBERY="03";
		/** 被代位协议定损 */
    	public static final String DEFLOSS_AGREE="04";
		/** 无损失 */
    	public static final String DEFLOSS_NULL="05";
		/** 快处快赔 */
    	public static final String DEFLOSS_HNQC="06";
	}

	public static class RepairFlag{

		/** 内修 */
		public static final String INNERREPAIR = "0";
		/** 外修 */
		public static final String OUTREPAIR = "1";
	}
	
	/** 案件注销类型 **/
	public static class CancelType{

		/** 报案注销 */
		public static final String REGIST_CANCEL = "1";
		/** 立案注销 */
		public static final String CLAIM_CANCEL = "2";
	}
	
	/** 人伤费用修改标志 **/
	public static class ModifyFlag{

		/** 未修改 */
		public static final String NONE = "0";
		/** 修改 */
		public static final String MODIFY = "1";
		/** 新增 */
		public static final String ADD = "2";
		
	}
	
	/** 调度状态 */
	public static class ScheduleStatus{

		/** 未调度 */
		public static final String NOT_SCHEDULED = "0";
		/** 正在处理 */
		public static final String SCHEDULING = "1";
		/** 未定义 */
		//public static final String NOTSCHEDULED = "2";
		/** 查看调度到人 */
		public static final String CHECK_SCHEDULED = "3";
		/** 未定义 */
		//public static final String NOTSCHEDULED = "4";
		/** 定损调度到人 */
		public static final String DEFLOSS_SCHEDULED = "5";
		/** 调度改派 */
		public static final String SCHEDULED_CHANGE = "6";
		/** 调度完毕 */
		public static final String SCHEDULED_FINISH = "7";
		/** 无需调度 */
		public static final String NO_SCHEDULE = "8";
		/** 调度注销 */
		public static final String SCHEDULED_CANCEL = "9";
	}
	
	/** 调度查勘项类型 */
	public static class ScheduleItemType{

		/** 调度查勘标的车 */
		public static final String SCHEDULEITEMTYPE_MAINCAR = "1";
		/** 调度查勘三者车 */
		public static final String SCHEDULEITEMTYPE_THIRDCAR = "2";
		/** 调度查勘财产损失 */
		public static final String SCHEDULEITEMTYPE_PROP = "3";
		/** 调度查勘人伤损失 */
		public static final String SCHEDULEITEMTYPE_PERSON = "4";
		
	}
	
	/** 有效标志 */
	public static class ValidFlag{

		/** 无效 */
		public static final String INVALID = "0";
		/** 有效 */
		public static final String VALID = "1";

		/** 无效 */
		public static final String N = "N";
		/** 有效 */
		public static final String Y = "Y";
	}
	
	/** 有效标志 ，是否标志*/
	public static class YN01 {

		/** 无效 ，否*/
		public static final String N = "0";
		/** 有效 ，是*/
		public static final String Y = "1";
	}

	/** 调度类型 */
	public static class ScheduleType{

		/** 查勘调度 */
		public static final String CHECK_SCHEDULE = "1";
		/** 定损调度 */
		public static final String DEFLOSS_SCHEDULE = "2";
	}
	
	/** 案件意见 */
	public static class ClaimText{

		/** 案件备注 */
		public static final String REMARK = "0";
		/** 意见 */
		public static final String OPINION = "1";
	}
	
	
	/** 交强险责任类型 */
	public static class DutyType{

		/** 是 */
		public static final String CIINDEMDUTY_Y = "1";
		/** 否 */
		public static final String CIINDEMDUTY_N = "0";
	}
	
	/** 事故类型 */
	public static class DamageType{

		/** 单方事故 */
		public static final String DAMAGETYPE_SINGLE = "01";
		/** 双方事故 */
		public static final String DAMAGETYPE_BOTH = "02";
		/** 多方事故 */
		public static final String DAMAGETYPE_MORE = "03";
		/** 其他 */
		public static final String DAMAGETYPE_ELSE = "99";
	}
	
	/** 查勘类别 */
	public static class CheckClass{

		/** 司内查勘 */
		public static final String CHECKCLASS_N = "0";
		/** 公估查勘 */
		public static final String CHECKCLASS_Y = "1";
	}
	
	/** 查勘审核标志 */
	public static class VerifyCheckFlag{

		/** 是 */
		public static final String VERIFYCHECK_Y = "1";
		/** 否 */
		public static final String VERIFYCHECK_N = "0";
	}
	
	/** 单选按钮值、是否查询OUT表数据 */
	public static class RadioValue{

		/** 是、查询的是OUT表 */
		public static final String RADIO_YES = "1";
		/** 否、查询的是IN表 */
		public static final String RADIO_NO = "0";
	}
	
	/** 河南快赔接口数据类型 */
	public static class HNQCDataType{

		/** 是否符合在线定损要求 */
		public static final String OFFLINEHANDING = "1";
		/** 照片审核结果 */
		public static final String PHOTOVERIFY = "0";
	}
	
	/** 核价 核损 标志 */
	public static class VeriFlag{

		/** 初始化 */
		public static final String INIT = "0";
		/** 通过 */
		public static final String PASS = "1";
		/** 退回定损 */
		public static final String BACK = "2";
		/** 核价不通过提交核损 */
		public static final String NOSUBMIT = "3";
		/** 预核损通过（提交复检） */
		public static final String RELOSS = "4";
		/** 定损注销 */
		public static final String CANCEL = "6";
		/** 自动通过 */
		public static final String AUTOPASS = "9";
	}
	
	public static class PersVeriFlag{

		/** 初始化 */
		public static final String INIT = "0";
		/** 跟踪审核通过 */
		public static final String VERIFYPASS = "1";
		/** 费用审核通过 */
		public static final String CHARGEPASS = "2";
		/** 费用审核修改 */
		public static final String CHARGEADJUST = "3";
		/** 自动审核通过 */
		public static final String AUTOPASS = "9";
	}
	
	/** 核赔类型 **/
	public static class VClaimType{

		/** 退回 **/
		public static final String VC_RETURN = "vc_return";
		/** 提交上级 **/
		public static final String VC_AUDIT = "vc_audit";
		/** 核赔通过 **/
		public static final String VC_ADOPT = "vc_adopt";
	}

	public static class AuditStatus{

		/** 提交上级 */
		public static final String AUDIT = "audit";
		/** 退回定损 */
		public static final String BACKLOSS = "backLoss";
		/** 退回下级 */
		public static final String BACKLOWER = "backLower";
		/** 复检 */
		public static final String RELOSS = "toReLoss";
		/** 复勘 */
		public static final String RECHECK = "toRecheck";
		/** 暂存 */
		public static final String SAVE = "save";
		/** 提交费用审核 */
		public static final String SUBPLCHARGE = "subPLCharge";
		/** 提交后续跟踪 */
		public static final String SUBPLNEXT = "subPLNext";
		/** 提交跟踪审核 */
		public static final String SUBPLVERIFY = "subPLVerify";
		/** 定损完成 */
		public static final String SUBMITLOSS = "submitLoss";
		/** 查勘完成 */
		public static final String SUBMITCHECK = "submitCheck";
		/** 复勘完成 */
		public static final String SUBMITCHKRE = "submitChkRe";
		/** 同意核损 */
		public static final String SUBMITVLOSS = "submitVloss";
		/** 同意核价 */
		public static final String SUBMITVPRICE = "submitVprice";
		/** 不同意核价,提交核损 */
		public static final String SUBMITVPRICENO = "noPassVerip";
		/** 跟踪审核通过 */
		public static final String SUBMITVERIFY = "submitVerify";
		/** 退回人伤后续跟踪 */
		public static final String BACKPLNEXT = "backPLNext";
		/** 费用审核通过 **/
		public static final String SUBMITCHARGE="submitCharge";
		/** 损余回收提交 **/
		public static final String SUBMITRECLOSS="submitRecLoss";
		/** 大案审核通过 **/
		public static final String CHKBIGEND="chkBigEnd";
			
	}
	
	
	/** 费用类型 */
	public static class FeeType {

		/** 赔款 */
		public static final String PAY = "P";
		/** 费用 */
		public static final String FEE = "F";
	}

	public class ClaimFlag {

		/** 强制立案 */
		public static final String CLAIMFORCE = "5";
		/** 自动立案 */
		public static final String AUTOCLAIM = "3";
		/** 手动立案 */
		public static final String HANDCLAIM = "1";

	}
	
	/** 出险日期 判断是否在 2008-01-31 之后 */
	public static final DateTime DAMAGE_DATE = new DateTime("2008-01-31");
	
	/** 交强责任类型 */
	public static final class CiIndemDuty{

		/** 无责 */
		public static final double CIINDEMDUTY_N= 0;
		/** 有责 */
		public static final double CIINDEMDUTY_Y= 100;
	}
	
	 /**
	 * @author phillips
	 * @description 损失分类
	 */
   public class LossType {

		/** 车损 */
       public static final String CAR_LOSS = "01";
		/** 玻璃单独破碎 */
       public static final String SINGLE_GLASS = "02";
		/** 全车盗抢 */
       public static final String CAR_GRAB = "03";
		/** 车身划痕 */
       public static final String CAR_NICK = "04";
		/** 火灾 爆炸 */
       public static final String CAR_BLAST = "05";
		/** 自燃 */
       public static final String SPONTCOMBUSTION = "06";
       /** 涉水 */
       public static final String CAR_WATER  = "07";
       /** 单独车轮损失险 */
       public static final String CAR_SINGER  = "08";
       
		/** 其他 */
//       public static final String OTHER = "07";
   }
   
	public static HashMap<String,String> LossFeeKind_Map = new HashMap<String,String>();
	static{
		LossFeeKind_Map.put(LossType.CAR_LOSS,KINDCODE.KINDCODE_A);
		LossFeeKind_Map.put(LossType.CAR_BLAST,KINDCODE.KINDCODE_E);
//		LossFeeKind_Map.put(LossType.OTHER,KINDCODE.KINDCODE_A);
		LossFeeKind_Map.put(LossType.CAR_GRAB,KINDCODE.KINDCODE_G);
		LossFeeKind_Map.put(LossType.CAR_NICK,KINDCODE.KINDCODE_L);
		LossFeeKind_Map.put(LossType.SINGLE_GLASS,KINDCODE.KINDCODE_F);
		LossFeeKind_Map.put(LossType.SPONTCOMBUSTION,KINDCODE.KINDCODE_Z);
	} 
	

    
	public static HashMap<String,String> LossFee2020Kind_Map = new HashMap<String,String>();
	static{
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.CAR_LOSS,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1231+LossType.CAR_LOSS,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1232+LossType.CAR_LOSS,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1233+LossType.CAR_LOSS,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.CAR_GRAB,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1231+LossType.CAR_GRAB,KINDCODE.KINDCODE_G);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1232+LossType.CAR_GRAB,KINDCODE.KINDCODE_G);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.SINGLE_GLASS,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.SPONTCOMBUSTION,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.CAR_WATER,KINDCODE.KINDCODE_A);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.CAR_SINGER,KINDCODE.KINDCODE_W1);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1233+LossType.CAR_SINGER,KINDCODE.KINDCODE_W1);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1230+LossType.CAR_NICK,KINDCODE.KINDCODE_L);
		LossFee2020Kind_Map.put(NEW2020KIND.kind1231+LossType.CAR_SINGER,KINDCODE.KINDCODE_W1);
	}
	public static HashMap<String,String> KindLossFee_Map = new HashMap<String,String>();
	static{
		LossFeeKind_Map.put(KINDCODE.KINDCODE_A,LossType.CAR_LOSS);
		LossFeeKind_Map.put(KINDCODE.KINDCODE_E,LossType.CAR_BLAST);
		LossFeeKind_Map.put(KINDCODE.KINDCODE_G,LossType.CAR_GRAB);
		LossFeeKind_Map.put(KINDCODE.KINDCODE_L,LossType.CAR_NICK);
		LossFeeKind_Map.put(KINDCODE.KINDCODE_F,LossType.SINGLE_GLASS);
		LossFeeKind_Map.put(KINDCODE.KINDCODE_Z,LossType.SPONTCOMBUSTION);
	} 
   
   	/**
	 * @author phillips
	 * @description 损失费用分类
	 */
   public class LossFeeType {

		public static final String THIRDPARTY_CAR_LOSS = "01"; // 第三者车辆损失
		public static final String THIRDPARTY_PERSON_LOSS = "02"; // 第三者人员损失
		public static final String THIRDPARTY_CAR_PROP = "03"; // 第三者车上财产
		public static final String THIRDPARTY_OTH_PROP = "04"; // 地面财产
		public static final String THIS_CAR_LOSS = "05"; // 本车车辆损失
		public static final String THIS_PERSON_LOSS = "06"; // 本车车上乘客损失
		public static final String THIS_PERSON_DRIVER_LOSS = "61"; // 本车车上司机损失
		public static final String THIS_CAR_PROP = "07"; // 本车财产损失
		public static final String OTH = "99"; // 其他
		// public static final String THIRDPARTY_CAR_PERSON = "08"; //第三者车上人员损失
		public static final String THIS_CAR_NEWEQUIPMENT = "08"; // 标的车车上新增设备
		public static final String THIRDPARTY_OTH_PERSON = "09"; // 地面人员损失
		public static final String THIRDPARTY_PERSON_LOSS_Medical = "021";// 第三者人员损失-医疗费
   }
   
   public static HashMap<String,String> LossFeeType_Map = new HashMap<String,String>();
   static{
		LossFeeType_Map.put("01","第三者车辆损失");
		LossFeeType_Map.put("02","第三者人员损失");
		LossFeeType_Map.put("03","第三者车上财产");
		LossFeeType_Map.put("04","地面财产");
		LossFeeType_Map.put("05","本车车辆损失");
		LossFeeType_Map.put("06","本车车上乘客损失");
		LossFeeType_Map.put("07","本车财产损失");
		LossFeeType_Map.put("08","标的车车上新增设备");
		LossFeeType_Map.put("09","地面人员损失");
		LossFeeType_Map.put("61","本车车上司机损失");
		LossFeeType_Map.put("99","其他");
   }
   
   public class ClaimType{

	/** 特殊(逃逸) */
   	public static final String ESCAPE_CASE = "5";
	/** 互碰自赔 */
   	public static final String SPAY_CASE = "2";	
   	/** 代位求偿 */
   	public static final String EACHHIT_SELFLOSS_CICASE_SUB = "3";
   	/** 正常案件 */
   	public static final String NORMAL = "1";
   	
   }
   
   	/**
	 * @author Luwei
	 * @description 全国平台请求类型 1，请求类型 2，请求节点
	 */
   public static final HashMap<String,String> REQUESTTYPE_BASE = new HashMap<String,String>();
   static{
		REQUESTTYPE_BASE.put("50","Regis_CI");
		/** 报案交强 **/
	   REQUESTTYPE_BASE.put("V3101","Regis_BI");
	   
		REQUESTTYPE_BASE.put("51","Claim_CI");
		/** 立案交强 **/
	   REQUESTTYPE_BASE.put("V3102","Claim_BI");
	   
		REQUESTTYPE_BASE.put("52","EndCase_CI");
		/** 交强结案登记 **/
	   REQUESTTYPE_BASE.put("V3104","EndCase_BI");
	   
		REQUESTTYPE_BASE.put("54","Cancel_CI");
		/** 案件注销-- 交强 */
	   REQUESTTYPE_BASE.put("V34","Cancel_BI");
	   
		REQUESTTYPE_BASE.put("55","Check_CI");
		/** 查勘登记 --交强 **/
	   REQUESTTYPE_BASE.put("V3107","Check_BI");
	   
		REQUESTTYPE_BASE.put("56","Loss_CI");
		/** 定核损登记 --交强 **/
	   REQUESTTYPE_BASE.put("V3108","Loss_BI");
	   
		REQUESTTYPE_BASE.put("57","Certify_CI");
		/** 交强单证收集 **/
	   REQUESTTYPE_BASE.put("V3109","Certify_BI");
	   
	   REQUESTTYPE_BASE.put("58","VClaim_CI");
		REQUESTTYPE_BASE.put("V3110","VClaim_BI");
		/** 商业理算核赔登记 */
	   
		REQUESTTYPE_BASE.put("59","Payment_CI");
		/** 交强赔款支付登记-- 交强 */
	   REQUESTTYPE_BASE.put("V3106","Payment_BI");
	   
		REQUESTTYPE_BASE.put("5A","ReOpen_CI");
		/** 重开赔案登记-- 交强 */
	   REQUESTTYPE_BASE.put("V3111","ReOpen_BI");
   }
   
   	/**
	 * @author Luwei
	 * @description 上海平台请求类型 1，请求类型 2，请求节点
	 */
   public static final HashMap<String,String> REQUESTTYPE_SH = new HashMap<String,String>();
   static{
		REQUESTTYPE_SH.put("50","Regis_CI");
		/** 报案交强 **/
	   REQUESTTYPE_SH.put("80","Regis_BI");
	   
		REQUESTTYPE_SH.put("51","Claim_CI");
		/** 立案交强 **/
	   REQUESTTYPE_SH.put("81","Claim_BI");
	   
		REQUESTTYPE_SH.put("52","EndCase_CI");
		/** 赔款金额确认 -- 上海交强 */
	   REQUESTTYPE_SH.put("82","EndCase_BI");
	   
		REQUESTTYPE_SH.put("53","EndCaseAdd_CI");
		/** 交强结案追加登记 **/
	   REQUESTTYPE_SH.put("85","EndCaseAdd_BI");
	   
		REQUESTTYPE_SH.put("54","Cancel_CI");
		/** 案件注销-- 上海交强 */
	   REQUESTTYPE_SH.put("83","Cancel_BI");
	   
		REQUESTTYPE_SH.put("56","Loss_CI");
		/** 查勘,定核损登记 --交强 **/
	   REQUESTTYPE_SH.put("86","Loss_BI");
	   
		REQUESTTYPE_SH.put("57","Certify_CI");
		/** 交强单证收集 */
	   REQUESTTYPE_SH.put("57","Certify_BI");
	   
		REQUESTTYPE_SH.put("58","Payment_CI");
		/** 赔款支付 -- 上海交强 */
	   REQUESTTYPE_SH.put("58","Payment_BI");
	   
		/** 1.2.8厘算核赔数据交互 */
	   REQUESTTYPE_SH.put("84","VClaim_BI");
	   REQUESTTYPE_SH.put("55","VClaim_CI");
   }
   
   	/**
	 * 车辆损失险
	 */
   public static final List<String> KINDCODE_A_LIST = new ArrayList<String>();
   static{
	   KINDCODE_A_LIST.add(KINDCODE.KINDCODE_A);
	   KINDCODE_A_LIST.add(KINDCODE.KINDCODE_A1);
	   
   }
	
	/**
	 * 险别/责任代码对应关系Map
	 * @author ZHANGYADONG
	 */
	public class KINDCODE{
		/** 机动车强制责任保险 */
		public static final String KINDCODE_BZ = "BZ";
		/** 机动车损失保险 */
		public static final String KINDCODE_A = "A";
		/** 全面型机动车损失保险（IACJQL0001） */
		public static final String KINDCODE_A1 = "A1";
		/** 附加绝对免赔率特约条款（机动车损失保险） */
		public static final String KINDCODE_AG = "AG";
		/** 盗抢险 */
		public static final String KINDCODE_G = "G";
		/** 全车盗抢绝对免赔率特约险*/
		public static final String KINDCODE_GG = "GG";
		/** 第三者责任保险 */
		public static final String KINDCODE_B = "B";
		/** 附加绝对免赔率特约条款（机动车第三者责任保险）*/
		public static final String KINDCODE_BG = "BG";
		/** 附加精神损害抚慰金责任险（机动车第三者责任保险）*/
		public static final String KINDCODE_BS = "BS";
		/** 附加法定节假日限额翻倍险*/
		public static final String KINDCODE_B2 = "B2";
		/** 附加医保外用药责任险（机动车第三者责任保险）*/
		public static final String KINDCODE_BP = "BP";
		/** 车上人员责任险（司机） */
		public static final String KINDCODE_D11 = "D11";
		/** 附加绝对免赔率特约条款（机动车车上人员责任保险（司机）） */
		public static final String KINDCODE_D11G = "D11G";
		/** 附加精神损害抚慰金责任险（机动车车上人员责任保险（司机）） */
		public static final String KINDCODE_D11S = "D11S";
		/** 附加医保外用药责任险（机动车车上人员责任保险（司机）） */
		public static final String KINDCODE_D11P = "D11P";
		
		/** 车上人员责任险（乘客） */
		public static final String KINDCODE_D12 = "D12";
		/** 附加绝对免赔率特约条款（机动车车上人员责任保险（乘客）） */
		public static final String KINDCODE_D12G = "D12G";
		/** 附加精神损害抚慰金责任险（机动车车上人员责任保险（乘客）） */
		public static final String KINDCODE_D12S = "D12S";
		/** 附加医保外用药责任险（机动车车上人员责任保险（乘客）） */
		public static final String KINDCODE_D12P = "D12P";
		
		/** 车上货物责任险 */
		public static final String KINDCODE_D2 = "D2";
		/** 随车行李物品损失保险 */
		public static final String KINDCODE_NZ = "NZ";
		/** 车身划痕损失险条款 */
		public static final String KINDCODE_L = "L";
		/** 自燃损失险条款 */
		public static final String KINDCODE_Z = "Z";
		/** 火灾、爆炸、自燃损失险条款 */
		public static final String KINDCODE_E = "E";
		/** 玻璃单独破碎险条款 */
		public static final String KINDCODE_F = "F";
		/** 新增加设备损失保险条款 */
		public static final String KINDCODE_X = "X";
		/** 代步费用特约条款 */
		public static final String KINDCODE_C = "C";
		/** 机动车停驶损失险条款 */
		public static final String KINDCODE_T = "T";
		/** 修理期间费用补偿险 */
		public static final String KINDCODE_RF = "RF";
		/** 附加换件特约条款 */
		public static final String KINDCODE_U = "U";
		/** 机动车特别损失险条款 */
		public static final String KINDCODE_X1 = "X1";
		/** 发动机涉水损失险 */
		public static final String KINDCODE_X2 = "X2";
		/** 新增设备损失险 */
		public static final String KINDCODE_X3 = "X3";
		/** 起重、装卸、挖掘车辆损失扩展条款 */
		public static final String KINDCODE_K1 = "K1";
		/** 特种车辆固定设备、仪器损坏扩展条款 */
		public static final String KINDCODE_K2 = "K2";
		/** 租车人人车失踪险条款 */
		public static final String KINDCODE_Z1 = "Z1";
		/** 约定区域通行费用特约条款 */
		public static final String KINDCODE_Z2 = "Z2";
		/** 指定修理厂险 */
		public static final String KINDCODE_Z3 = "Z3";
		/** 附加油污污染责任保险 */
		public static final String KINDCODE_V1 = "V1";
		/** 附加交通事故精神损害赔偿责任保险 */
		public static final String KINDCODE_R = "R";
		/** 附加交通事故精神损害赔偿责任保险 */
		public static final String KINDCODE_SS = "SS";
		/** 不计免赔率特约条款 */
		public static final String KINDCODE_M = "M";
		/** 可选免赔额特约条款 */
		public static final String KINDCODE_M1 = "M1";
		/** 多次出险增加免赔率特约条款 */
		public static final String KINDCODE_M2 = "M2";
		/** 附加绝对免赔率特约险 */
		public static final String KINDCODE_M3 = "M3";
		/** 附加机动车出境保险 */
		public static final String KINDCODE_S = "S";
		/** 系安全带补偿特约险条款 */
		public static final String KINDCODE_S1 = "S1";
		/** 教练车特约条款 */
		public static final String KINDCODE_Y = "Y";
		/** 机动车损失保险无法找到第三方特约险 */
		public static final String KINDCODE_NT = "NT";
		/** 新车特约条款 */
		public static final String KINDCODE_NY = "NY";
		/** 附加车轮单独损坏除外特约险 */
		public static final String KINDCODE_W1 = "W1";
		public static final String KINDCODE_DP = "DP";//DP-附加医保外医疗费用责任险
		/** 道路救援服务特约条款*/
		public static final String KINDCODE_RS = "RS";
		/** 车辆安全检测特约条款*/
		public static final String KINDCODE_VS = "VS";
		/** 代为驾驶服务特约条款*/
		public static final String KINDCODE_DS = "DS";
		/** 代为送检服务特约条款*/
		public static final String KINDCODE_DC = "DC";

		
		
		// public static final String KINDCODE_D1 = "D1";//车上人员责任险
		// public static final String KINDCODE_NX = "NX";//新车特约条款A
		// public static final String KINDCODE_Q2 = "Q2";//家庭自用汽车多次出险增加免赔率特约条款
		// public static final String KINDCODE_Q4 = "Q4";//选择修理厂特约条款
		// public static final String KINDCODE_Q3 = "Q3";//指定修理厂特约条款
		// public static final String KINDCODE_Q1 = "Q1";//可选免赔额特约条款
		// public static final String KINDCODE_C7 = "C7";//法律费用特约条款
		// public static final String KINDCODE_D1 = "D1";//车上人员责任险
	   
	   
	   

	}
	
	/**
	 * 险别名称对应表(后续根据鼎和生成环境调整)
	 */
	public static final Map<String,String> KINDCODE_MAP = new HashMap<String,String>();
	static {
		KINDCODE_MAP.put(KINDCODE.KINDCODE_BZ,"机动车强制责任保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_A,"机动车损失保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_A1,"机动车损失保险（IACJQL0001）");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_G,"盗抢险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_B,"第三者责任保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_D11,"车上人员责任险（司机）");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_D12,"车上人员责任险（乘客）");
		
		KINDCODE_MAP.put(KINDCODE.KINDCODE_D2,"车上货物责任险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_NZ,"随车行李物品损失保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_W1,"附加车轮单独损坏险");
		
		KINDCODE_MAP.put(KINDCODE.KINDCODE_L,"车身划痕损失险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_Z,"自燃损失险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_E,"火灾、爆炸、自燃损失险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_F,"玻璃单独破碎险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_X,"新增加设备损失保险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_X3,"新增加设备损失保险条款");
		
		KINDCODE_MAP.put(KINDCODE.KINDCODE_C,"代步车费用特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_T,"机动车停驶损失险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_RF,"修理期间费用补偿险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_U,"附加换件特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_X1,"发动机特别损失险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_X2,"发动机涉水损失险");
		
		KINDCODE_MAP.put(KINDCODE.KINDCODE_K1,"起重、装卸、挖掘车辆损失扩展条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_K2,"特种车辆固定设备、仪器损坏扩展条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_Z1,"租车人人车失踪险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_Z2,"约定区域通行费用特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_Z3,"指定修理厂险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_V1,"附加油污污染责任保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_R,"附加交通事故精神损害赔偿责任保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_SS,"精神损害抚慰金责任险");

		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_BS,"附加精神损害抚慰金责任险(机动车第三者责任保险)");
		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_D11S,"附加精神损害抚慰金责任险(机动车车上人员责任保险(司机)");
		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_D12S,"附加精神损害抚慰金责任险(机动车车上人员责任保险(乘客)");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_BS,"附加精神损害抚慰金责任险（特种车第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_D11S,"附加精神损害抚慰金责任险（特种车车上人员责任保险（司机）");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_D12S,"附加精神损害抚慰金责任险（特种车车上人员责任保险（乘客））");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_BS,"附加精神损害抚慰金责任险（摩托车、拖拉机第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_D11S,"附加精神损害抚慰金责任险（摩托车、拖拉机车上人员责任保险（司机））");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_D12S,"附加精神损害抚慰金责任险（摩托车、拖拉机车上人员责任保险（乘客））");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_BS,"附加精神损害抚慰金责任险（机动车第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_D11S,"附加精神损害抚慰金责任险（机动车车上人员责任保险（司机）");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_D12S,"附加精神损害抚慰金责任险（机动车车上人员责任保险（乘客））");

		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_BP,"附加医保外用药责任险（机动车第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_D11P,"附加医保外用药责任险（机动车车上人员责任保险（司机））");
		KINDCODE_MAP.put(NEW2020KIND.kind1230+KINDCODE.KINDCODE_D12P,"附加医保外用药责任险（机动车车上人员责任保险（乘客））");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_BP,"附加医保外用药责任险（特种车第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_D11P,"附加医保外用药责任险（特种车车上人员责任保险（司机））");
		KINDCODE_MAP.put(NEW2020KIND.kind1231+KINDCODE.KINDCODE_D12P,"附加医保外用药责任险（特种车车上人员责任保险（乘客））");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_BP,"附加医保外用药责任险（摩托车、拖拉机第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_D11P,"附加医保外用药责任险（摩托车、拖拉机车上人员责任保险（司机））");
		KINDCODE_MAP.put(NEW2020KIND.kind1232+KINDCODE.KINDCODE_D12P,"附加医保外用药责任险（摩托车、拖拉机车上人员责任保险（乘客）））");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_BP,"附加医保外医疗费用责任险（机动车第三者责任保险）");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_D11P,"附加医保外用药责任险（机动车车上人员责任保险（司机））");
		KINDCODE_MAP.put(NEW2020KIND.kind1233+KINDCODE.KINDCODE_D12P,"附加医保外用药责任险（机动车车上人员责任保险（乘客））");

		KINDCODE_MAP.put(KINDCODE.KINDCODE_M,"不计免赔率特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_M1,"可选免赔额特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_M2,"多次出险增加免赔率特约条款");
		
		KINDCODE_MAP.put(KINDCODE.KINDCODE_S,"附加机动车出境保险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_S1,"系安全带补偿特约险条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_Y,"教练车特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_NT,"无法找到第三方特约险");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_NY,"新车特约条款");
		
		// KINDCODE_MAP.put(KINDCODE.KINDCODE_Q2,"家庭自用汽车多次出险增加免赔率特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_RS,"道路救援服务特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_VS,"车辆安全检测特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_DS,"代为驾驶服务特约条款");
		KINDCODE_MAP.put(KINDCODE.KINDCODE_DC,"代为送检服务特约条款");
		// KINDCODE_MAP.put(KINDCODE.KINDCODE_Q3,"指定修理厂特约条款");
		// KINDCODE_MAP.put(KINDCODE.KINDCODE_Q1,"可选免赔额特约条款");
		// KINDCODE_MAP.put(KINDCODE.KINDCODE_NX,"新车特约条款A");
		// KINDCODE_MAP.put(KINDCODE.KINDCODE_D1,"车上人员责任险");
		
	}
	
	// 是否承保M险的flag位
	public static final int ISM_FLAG_BEGIN = 4;
	public static final int ISM_FLAG_END = 5;
		
	/**
	 * 计算书类型
	 */
    public static final class CompensateKind{

		/** 商业险实赔 */
    	public static final String BI_COMPENSATE = "1";
		/** 交强险实赔 */
    	public static final String CI_COMPENSATE = "2";
		/** 垫付 */
    	public static final String ADVANCE = "3";
		/** 商业险费用计算书 */
    	public static final String BI_COMPENSATE_CHARGE = "4";
		/** 交强险费用计算书 */
    	public static final String CI_COMPENSATE_CHARGE = "5";
		/** 预付 */
    	public static final String PRY_PAY = "6";
    }

	// 损失标识
    public static final class DamageFlag{

		public static final String BI_CLAIM = "10"; // 商业险出险案件
		public static final String CI_CLAIM = "01"; // 交强险出险案件
		public static final String BICI_CLAIM = "11"; // 商业险+交强险出险案件
    }
    
    //欺诈类型
    public static final class Fraudlogo{

		public static final String Fraudlogo_01 = "01"; // 欺诈放弃索赔
		public static final String Fraudlogo_02 = "02"; // 交强险出险案件
		public static final String Fraudlogo_03 = "03"; // 疑似欺诈
    }
    
    /**
	 * 自动通过人员代码
	 */
    public static final class AutoPass{

		/** 自动核赔人员 */
    	public static final String UNDWRT = "AutoUndwrt";
		/** 自动立案人员 */
    	public static final String CLAIM = "AutoClaim";
		/** 极速理算人员 */
    	public static final String FASTCOMPENSATE = "AutoCompes";
		/** 强制立案人员 */
    	public static final String FORCECLAIM = "ForceClaim";
    }
    
    /**
	 * @author phillips
	 * @description 损失费用分类 或  人伤类型
	 */
    public class FeeTypeCode {

    	/** 财产损失 、简易人伤*/
		public static final String PROPLOSS = "01"; // 财产损失
		/** 死亡伤残、伤残 */
        public static final String PERSONLOSS = "02";
		/** 医疗审核、死亡 */
		public static final String MEDICAL_EXPENSES = "03"; // 医疗费
    }

    
    /**
	 * 理算标志
	 * 
	 * <pre></pre>
	 * @author ★ZhouYanBin
	 */
    public static final class LossState{

		/** 未理算 */
    	public static final String UN_COMPENSATE = "00";
		/** 交强理算 */
    	public static final String CI_COMPENSATE = "10";
		/** 商业理算 */
    	public static final String BI_COMPENSATE = "01";
    }
    
    /**
	 * 核赔标志
	 */
    public static final class UnderWriteFlag{

		/** 未核赔 */
    	public static final String NORMAL = "0";
		/** 人工核赔通过 */
    	public static final String MANAL_UNDERWRITE = "1";
		/** 核赔退回 */
    	public static final String BACK_UNDERWRITE = "2";
		/** 无需核赔 (自动核赔通过) */
    	public static final String AUTO_UNDERWRITE = "3";
		/** 计算书注销 */
    	public static final String CANCELFLAG = "7";
		/** 核赔作废/计算书作废/放弃索赔 */
    	public static final String CANCEL_UNDERWRITE = "8";
		/** 待核赔 */
    	public static final String WAIT_UNDERWRITE = "9";
    }
    
    /**
	 * 公估定损审核标志
	 */
    public static final class AssessorUnderWriteFlag{
    	
		/** 定损 */
    	public static final String Loss = "0";
		/** 核损通过 或查勘提交 */
    	public static final String Verify = "1";
		/** 发起了公估任务 */
    	public static final String IntermTask = "2";
		/** 公估审核通过 */
    	public static final String IntermVerify = "3";
    }
    
    /**
	 * 公估定损任务类型
	 */
    public static final class TaskType{

		/** 查勘 */
    	public static final String CHK = "0";
		/** 车辆 */
    	public static final String CAR = "1";
		/** 财产 */
    	public static final String PROP = "2";
		/** 人伤 */
    	public static final String PERS = "3";
    }
    
    /**
	 * 查勘费定损审核标志
	 */
    public static final class AcheckUnderWriteFlag{
    	
		/** 定损 */
    	public static final String Loss = "0";
		/** 核损通过 或查勘提交 */
    	public static final String Verify = "1";
		/** 发起了查勘费任务 */
    	public static final String CheckTask = "2";
		/** 查勘费审核通过 */
    	public static final String CheckVerify = "3";
    }
    
    /**
	 * 查勘费定损任务类型
	 */
    public static final class CheckTaskType{

		/** 查勘 */
    	public static final String CHK = "0";
		/** 车辆 */
    	public static final String CAR = "1";
		/** 财产 */
    	public static final String PROP = "2";
		/** 人伤*/
    	public static final String PERS = "3";
    	
    }
    
    
    /**
	 * 核赔任务
	 */
	// verifyType-->类型 （1-交强实赔、冲销核赔，2-商业实赔、冲销核赔，3-交强预付、冲销核赔，4-商业预付、冲销核赔，5-垫付申请核赔，6-拒赔核赔）
	// 全部、商业实赔、交强实赔、商业预付、交强预付、垫付、拒赔、交强预付冲销、商业预付冲销、交强实赔冲销、商业实赔冲销。
    public static final class VerifyClaimTask{
    	public static final String COMPE_CI = "1";
    	public static final String COMPE_BI = "2";
    	public static final String PREPAY_CI = "3";
    	public static final String PREPAY_BI = "4";
    	public static final String PADPAY = "5";
    	public static final String CANCEL = "6";
    }
    
    /**
	 * @author phillips
	 * @description 交强险责任限额 第一位 1.有责 0.无责 2-3位 FeeTypeCode(见上面费用损失类型)
	 */
    public static final HashMap<String,BigDecimal> BZ_LIMIT_AMOUNT = new HashMap<String,BigDecimal>();
    static{
    	BZ_LIMIT_AMOUNT.put("102",new BigDecimal("50000.00"));
    	BZ_LIMIT_AMOUNT.put("002",new BigDecimal("10000.00"));
    	BZ_LIMIT_AMOUNT.put("101",new BigDecimal("2000.00"));
    	BZ_LIMIT_AMOUNT.put("001",new BigDecimal("400.00"));
    	BZ_LIMIT_AMOUNT.put("103",new BigDecimal("8000.00"));
    	BZ_LIMIT_AMOUNT.put("003",new BigDecimal("1600.00"));
    	BZ_LIMIT_AMOUNT.put("N102",new BigDecimal("110000.00"));
    	BZ_LIMIT_AMOUNT.put("N002",new BigDecimal("11000.00"));
    	BZ_LIMIT_AMOUNT.put("N101",new BigDecimal("2000.00"));
    	BZ_LIMIT_AMOUNT.put("N001",new BigDecimal("100.00"));
    	BZ_LIMIT_AMOUNT.put("N103",new BigDecimal("10000.00"));
    	BZ_LIMIT_AMOUNT.put("N003",new BigDecimal("1000.00"));
    }
    
    /**
	 * 险种代码是否是新条款(2014年费改之后的条款)
	 */
    public static HashMap<String,Boolean> ISNEWCLAUSECODE_MAP = new HashMap<String,Boolean>();
    static{
    	ISNEWCLAUSECODE_MAP.put("1201",false);
    	ISNEWCLAUSECODE_MAP.put("1202",false);
    	ISNEWCLAUSECODE_MAP.put("1203",false);
    	ISNEWCLAUSECODE_MAP.put("1204",false);
    	ISNEWCLAUSECODE_MAP.put("1205",false);
    	ISNEWCLAUSECODE_MAP.put("1206",true);
    	ISNEWCLAUSECODE_MAP.put("1207",true);
    	ISNEWCLAUSECODE_MAP.put("1208",true);
    	ISNEWCLAUSECODE_MAP.put("1209",true);
    	/**
    	 * 为了不影响2014年推出的新险种，故将2020年推出的新险种设置为true
    	 */
    	ISNEWCLAUSECODE_MAP.put("1230",true);
    	ISNEWCLAUSECODE_MAP.put("1231",true);
    	ISNEWCLAUSECODE_MAP.put("1232",true);
    	ISNEWCLAUSECODE_MAP.put("1233",true);
    }
    

    /**
	 * 险种代码是否是新条款(2020年费改之后的条款)
	 */
    public static HashMap<String,Boolean> ISNEWCLAUSECODE2020_MAP = new HashMap<String,Boolean>();
    static{
    	ISNEWCLAUSECODE2020_MAP.put("1201",false);
    	ISNEWCLAUSECODE2020_MAP.put("1202",false);
    	ISNEWCLAUSECODE2020_MAP.put("1203",false);
    	ISNEWCLAUSECODE2020_MAP.put("1204",false);
    	ISNEWCLAUSECODE2020_MAP.put("1205",false);
    	ISNEWCLAUSECODE2020_MAP.put("1206",false);
    	ISNEWCLAUSECODE2020_MAP.put("1207",false);
    	ISNEWCLAUSECODE2020_MAP.put("1208",false);
    	ISNEWCLAUSECODE2020_MAP.put("1209",false);
    	ISNEWCLAUSECODE2020_MAP.put("1230",true);
    	ISNEWCLAUSECODE2020_MAP.put("1231",true);
    	ISNEWCLAUSECODE2020_MAP.put("1232",true);
    	ISNEWCLAUSECODE2020_MAP.put("1233",true);
    	ISNEWCLAUSECODE2020_MAP.put(null,false);
    	ISNEWCLAUSECODE2020_MAP.put("",false);
    }
    /**
	 * 不显示的附加险
	 */
    public static HashMap<String,Boolean> NOSUBRISK_MAP = new HashMap<String,Boolean>();
    static{
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_Z3,false);// 指定专修厂特约条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_NT,false);// 找到第三方特约险
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_D2,false);// 车上货物责任险
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_NY,false);// 新车特约条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_Y,false);// 教练车特约条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_S,false);// 附加机动车出境保险
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_S1,false);// 系安全带补偿特约险条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_M1,false);// 可选免赔额特约条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_M2,false);// 多次出险增加免赔率特约条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_K1,false);// 起重、装卸、挖掘车辆损失扩展条款
		NOSUBRISK_MAP.put(KINDCODE.KINDCODE_M3,false);// 附加绝对免赔率特约险
		// NOSUBRISK_MAP.put(KINDCODE.KINDCODE_W1,false);// 附加车轮单独损坏除外特约险

    }
    
    /**
	 * 赔付类型 1:代付,2:清付,3:自付4-无责代赔
	 * 
	 * <pre></pre>
	 * @author ★ZhouYanBin
	 */
    public static final class PayFlagType{

		/** 代付 追偿 */
		public static final String INSTEAD_PAY = "1";
		/** 清付 */
		public static final String CLEAR_PAY = "2";
		/** 自付 */
		public static final String COMPENSATE_PAY = "3";
		/** 无责代赔 */
		public static final String NODUTY_INSTEAD_PAY = "4";
    }//
    
	// 代位险种类型
    public static final class CoverageType{

		/** 交强险 */
		public static final String CI_DQZ = "1";
		/** 商业三者险 */
		public static final String BI_B = "2";
		/** 商业车损险 */
		public static final String BI_A = "3";
    }
    
    
	/**
	 * 是否互碰自赔
	 * @author lanlei
	 */
    public static final class caseFlagType{

		/** 正常案件 */
		public static final String NORMAL_CASE = "0";
		/** 互碰自赔 */
		public static final String SELF_CASE = "1";
		
    }
    
	/**
	 * 是否代位求偿
	 * @author lanlei
	 */
    public static final class subRogationFlagType{

		/** 正常案件 */
		public static final String NORMAL_CASE = "0";
		/** 代位求偿 */
		public static final String SUBROGA_CASE = "1";
		
    }
    
	/**
	 * 理算车财人损失类型
	 * @author lanlei
	 */
    public static final class LossTypeCarComp{

		/** 本车车辆损失 */
		public static final String THIS_CAR_LOSS = "1";
		/** 第三者车辆损失 */
		public static final String THIRDPARTY_CAR_LOSS = "3";
    }
    
    public static final class LossTypePropComp{

		/** 第三者其他财产 */
		public static final String THIRDPARTY_OTH_PROP = "0";
		/** 本车车上财 */
		public static final String THIS_CAR_PROP = "1";
		/** 第三者车上财产 */
		public static final String THIRDPARTY_CAR_PROP = "2";
    }
    
    public static final class LossTypePersComp{

		/** 死亡伤残 */
		public static final String PERSON_LOSS_DEATHDIS = "02";
		/** 医疗费用 */
		public static final String PERSON_LOSS_MEDICAL = "03";
    }
    
    
    /**
	 * 保单类型
	 * 
	 * <pre></pre>
	 * @author ★ZhouYanBin
	 */
    public static final class PolicyType{

		/** 商业险保单 */
    	public static final String POLICY_DAA = "12";
		/** 交强险保单 */
    	public static final String POLICY_DZA = "11";
    	
    }
    
    /**
	 * 理算费用来源
	 * @author lanlei
	 */
    public static final class chargeNodeCode{

		/** 查勘环节 */
		public static final String CHARGE_CHECK = "1";
		/** 定损环节 */
		public static final String CHARGE_LOSS = "2";
		/** 人伤环节 */
		public static final String CHARGE_PLOSS = "3";
		
    }
    
	/**
	 * 理算扣除类型
	 * @author lanlei
	 */
    public static final class DeductType{

		/** 1-预付 */
		public static final String DEDUCT_PRE = "1";
		/** 2-垫付 */
		public static final String DEDUCT_PAD = "2";
		/** 3-无扣除 */
		public static final String DEDUCT_NONE = "3";
		
    }
    
	/**
	 * 理算损失类型
	 * 
	 * <pre></pre>
	 * @author ★ZhouYanBin
	 */
    public static final class ExpType{

		// 车
    	public static final String CAR = "car";
		// 财
    	public static final String PROP = "prop";
		// 人
    	public static final String PERSON = "person";
		// 其他
    	public static final String OTHER = "other";
    }
    
    /**
	 * 计算书文本。
	 */
    public enum CompensateText {
		text1("赔付金额超过本险別保险金额，故按保险金额进行计算"),
		text2("实际赔付金额 = %s , 实际不计免赔金额 = %s \r\n"),
		text3("地面损失"),
		text4("不计免赔金额"),
		text5("险别赔付合计"),
		text6("(1 - 事故责任免赔率) × (1 - 可选免赔率)"),
		text8("(1 - 可选免赔率)"),
		text9("交强险互碰自赔"),
		text10("本项理算金额"),
		text11("赔付超限额，进行超限额调整"),
		text12("本项赔偿金额 × 本车责任限额 / 本车赔偿金额合计"),
		text13("费用赔款信息"),
		text14("协议金额合计"),
		text15("\r\n\r\n本计算书是交强垫付计算书，实赔金额允许手工调整。"),
        text17("******************************************** %s ********************************************"),
		text18("交强险重开计算书，赔付金额以实际赔付金额为准。"),
		text19("(1 - 事故责任免赔率)"),
        
        /**
		 * 参数配置如下： 1.定损金额文本。 2.定损金额数据。 3.不计免赔率。 4.不计免赔额。
		 */
		reCaseSumRealPayText("交强险重开赔款计算书超限额调整 = 交强剩余限额 × 原实赔金额 / 重开总赔付金额"+"\r\n=%s × %s / %s"+
                             "\r\n=%s"),
        
        /**
		 * 参数配置如下： 1.定损金额文本。 2.定损金额数据。 3.不计免赔率。 4.不计免赔额。//"\r\n=%s × %s%%" 两个百分号 有一个是转义
		 */
		mSumRealPayText("     不计免赔金额 = %s × 事故责任免赔率  × (1 - 可选免赔率)"+"\r\n=%s × %s%% × (1 - %s%%)"+
                        "\r\n=%s"),
                        
		// mSumRealPayText("     不计免赔金额 = %s × 不计免赔率"+"\r\n=%s × %s%%"+
//                       "\r\n=%s"),
        
        /**
		 * 参数配置如下： 1.赔付损失项.lossName。 2.赔付损失项.lossFeeName。 3.赔付损失项.sumLoss。
		 */
		textComplex1("\r\n" +
				"交强险互碰自赔: %s \r\n" + "本项理算金额= %s \r\n" +
				"          = %s" + "\r\n"),

        /**
		 * 参数配置如下： 1.本车赔付分项.bzCompensateText。 2.本车赔付分项.payAmount。 3.本车.dutyAmount。 4.本车.payAmount。 5.本车赔付分项.sumRealPay。
		 */
		textComplex2("%s赔付超限额，进行超限额调整:\r\n" + "= 本项赔偿金额 × 本车责任限额 / 本车赔偿金额合计\r\n" + "= %s × %s / %s \r\n" +
				"= %s"),
        
         /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.sumRealPay。 2.ThirdPartyCompInfo.sumPrePay。 3.ThirdPartyCompInfo.sumRealPay - ThirdPartyCompInfo.sumPrePay。
		 */
		 compulsorySubPrePayText("\r\n赔款 = 实赔金额 - 交强预付金额" +
				 "\r\n=%s - %s" +
				 "\r\n=%s"),
         
        /**
		 * 参数配置如下： 1.noDutySumRealPay。无责代赔金额和。
		 */
		compulsoryNoDutyPayText("\r\n本险别无责代赔金额 = %s"),
         
        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.lockedBzRealPay。 3.CompensateExp.deductibleRate。
		 */
		textComplex3("代责任对方交强赔款 = @该责任对方交强险应赔付追偿方金额@ × %s \r\n"+"                 = @%s@ × %s"),
		// 交强公式 end

        /**
		 * 施救费用赔款
		 */
		rescueFeeText("施救费用赔款 = (核定施救费用 - 施救费交强险已赔付金额) × 事故责任比例 \r\n"+"            = (%s - %s) × %s%%"),
		rescueFeeText2020("施救费用赔款 = min(险别保额  ,  (核定施救费用 - 施救费交强险已赔付金额)× 事故责任比例) \r\n"+"            = min(%s , (%s - %s) × %s%%)"),
        
		rescueFeeText_over("施救费用赔款 = (险别保额 - 施救费交强险已赔付金额) × 事故责任比例 \r\n"+"            = (%s - %s) × %s%%"),
		
		rescueFeeText_A1("施救费用赔款 = 核定施救费用 - 被保险人已从第三方获得的施救费赔偿金额\r\n"+"            = %s - %s"),
        
		rescueFeeText_A1_over("施救费用赔款 = 险别保额 - 被保险人已从第三方获得的施救费赔偿金额\r\n"+"            = %s - %s"),

		rescueFeeText_unfull("施救费用赔款 = (核定施救费用 - 施救费交强险已赔付金额) × (保险金额 / 新车购置价) × 事故责任比例\r\n"+"            = (%s - %s) × (%s / %s) × %s%%"),
	              
      	/**
		 * 代付 施救费用赔款
		 */        
		rescueFeeText_replace_unfull("施救费用赔款 = (核定施救费用 - 施救费交强险已赔付金额) × (保险金额 / 新车购置价) × 责任对方事故责任比例\r\n"+"            = (%s - %s) × (%s / %s) × %s"),

		rescueFeeText_replace_unfull_CPRC("施救费用赔款 = (保额 - 施救费交强险已赔付金额) × 责任对方事故责任比例\r\n"+"            = (%s - %s) × %s"),

		rescueFeeText_replace("施救费用赔款 = (核定施救费用 - 施救费交强险已赔付金额) × 责任对方事故责任比例\r\n"+"            = (%s - %s) × %s"),
		
		rescueFeeText_replace_A1("施救费用赔款 = 核定施救费用 - 被保险人已从第三方获得的施救费赔偿金额 \r\n"+"            = %s - %s"),

        /**
		 * 车辆在代付情况下使用的计算书文本。
		 */
		sumRealLossTextBelowDamageItemRealCost4ZC("车损赔款 = (定损金额或保额 - 残值 - 交强险已赔付金额 - 其他扣除) × 责任对方事故责任比例 × (出险时险别保险金额 / 投保时保险车辆新车购置价)\r\n"+"        = (%s - %s - %s - %s) × %s × (%s / %s)"),

		sumRealLossTextBelowDamageItemRealCost4ZC_CPRC("车损赔款 = (定损金额或保额 - 残值 - 交强险已赔付金额 - 其他扣除) × 责任对方事故责任比例 \r\n"+"        = (%s - %s - %s - %s) × %s"),
		
		sumRealLossTex_replace_A1("车损赔款 = 实际修复费用 - 被保险人已从第三方获得的施救费赔偿金额 \r\n"+"            = %s - %s"),

        /**
		 * 车损赔款 公式 车辆在自付情况下使用的计算书文本。
		 */
		sumRealLossTextBelowDamageItemRealCost4ZF("车损赔款 = (定损金额或保额  - 交强险已赔付金额  - 无责代赔金额  - 其他扣除) × 事故责任比例 × (出险时险别保险金额 / 投保时保险车辆新车购置价)\r\n"+"        = (%s - %s - %s -%s) × %s%% × (%s / %s)"),

		sumRealLossTextBelowDamageItemRealCost4ZF_CPRC("车损赔款 = (定损金额  - 交强险已赔付金额 - 无责代赔金额 - 其他扣除) × 事故责任比例\r\n "+"        = (%s - %s - %s - %s) × %s%%"),
		sumRealLossTextBelowDamageItemRealCost4ZF2020_CPRC("车损赔款 =min (险别保额 , (定损金额  - 交强险已赔付金额 - 无责代赔金额 - 其他扣除) × 事故责任比例)\r\n "+"               =min (%s , (%s - %s - %s - %s) × %s%%))"),
		sumRealLossTextBelowDamageItemRealCostOVER4ZF_CPRC("车损赔款 = (保额 - 交强险已赔付金额  - 无责代赔金额) × 事故责任比例\r\n"+"        = (%s - %s - %s) × %s%%"),
		
		sumRealLossTextBelowDamageItemRealCost4ZF_CPRC_A1("车损赔款 = 定损金额  - 被保险人已从第三方获得的施救费赔偿金额\r\n"+"        = %s - %s"),
        
		sumRealLossTextBelowDamageItemRealCostOVER4ZF_CPRC_A1("车损赔款 = 保险金额 - 被保险人已从第三方获得的施救费赔偿金额\r\n"+"        = %s - %s"),
                                                  
      	/**
		 * 车辆在自付情况下损失金额大于实际价值情况下使用的计算书文本。
		 */
		sumRealLossTextOverRealCost4ZF("车损赔款 = (出险时车辆实际价值  - 交强险已赔付金额  - 无责代赔金额 ) × 事故责任比例\r\n"+"        = (%s - %s - %s) × %s%%"),
		 /**
		 * 车损险最终赔付 1 施救费，车损赔款都小于保额
		 */
		sumRealPayText6("赔款 = @(车损赔款 + 施救费赔款)@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@ × (1 - %s%%) × (1 - %s%%) - %s"),
		
		 /**
		 * 损失金额小于免赔额
		 */
		sumRealPayText7("赔款 = (车损赔款 + 施救费赔款) × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额\r\n"+"     = (%s + %s) × (1 - %s%%) × (1 - %s%%) - %s \r\n"+
		             "\r\n" +
"损失金额小于免赔额，故赔款为0。"),
		             
		             


        
		/**
		 * 车损险最终赔付 1 施救费，车损赔款都小于保额
		 */
		sumRealPayText2020_6("赔款 = @(车损赔款 + 施救费赔款)@ - 免赔额   \r\n"+"     = @(%s + %s)@  - %s"),
		sumRealPayText2020_A("赔款 = min(责任限额  , (车损赔款 + 施救费赔款) - 免赔额)   \r\n"+"     = min(%s,(%s + %s)  - %s)"),
		/**
		 * 车损险最终赔付 1 施救费+车损赔款都小于保额,承保了附加险
		 */
		sumRealPayText2020_FJ6("赔款 = @(车损赔款 + 施救费赔款)@ × (1 - 绝对免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@  × (1 - %s%%) - %s"),
		sumRealPayText2020_FJ_A("赔款 = (车损赔款 + 施救费赔款  - 免赔额) × (1 - 绝对免赔率) \r\n"+"     = (%s + %s - %s)  × (1 - %s%%) "),
		 /**
		 * 损失金额小于免赔额
		 */
		sumRealPayText2020_7("赔款 = (车损赔款 + 施救费赔款- 免赔额) \r\n"+"     = (%s + %s) - %s \r\n"+
		             "\r\n" +
		"损失金额小于免赔额，故赔款为0。"),
		 /**
		 * 损失金额小于免赔额,承保了附加险
		 */
		sumRealPayText2020_FJ7("赔款 = (车损赔款 + 施救费赔款 - 免赔额)× (1 - 绝对免赔率) \r\n"+"     = (%s + %s - %s) × (1 - %s%%)  \r\n"+
		             "\r\n" +
		"损失金额小于免赔额，故赔款为0。"),
		 /**
		 * 车损赔款 大于保额
		 */
		sumRealPayText8("赔款 = @(保险金额 + 施救费赔款)@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@ × (1 - %s%%) × (1 - %s%%) - %s"),
		
		 /**
		 * 施救费赔款 大于保额
		 */
		sumRealPayText9("赔款 = @(车损赔款 + 保险金额)@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额  \r\n"+"     = @(%s + %s)@ × (1 - %s%%) × (1 - %s%%) - %s"),
		
		 /**
		 * 施救费，车损赔款都大于保额
		 */
		sumRealPayText10("赔款 = @(保险金额 + 保险金额)@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@ × (1 - %s%%) × (1 - %s%%) - %s"),
		              

        
		 /**
		 * 车损赔款 大于保额
		 */
		sumRealPayText2020_8("赔款 = @(保险金额 + 施救费赔款)@ - 免赔额   \r\n"+"     = @(%s + %s)@  - %s"),
		/**
		 * 车损赔款 大于保额,承保附加险
		 */
		sumRealPayText2020_FJ8("赔款 = @(保险金额 + 施救费赔款)@ × (1 - 绝对免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@ × (1 - %s%%) - %s"),

		 /**
		 * 施救费赔款 大于保额
		 */
		sumRealPayText2020_9("赔款 = @(车损赔款 + 保险金额)@  - 免赔额  \r\n"+"     = @(%s + %s)@ - %s"),
		/**
		 * 施救费赔款 大于保额,承保附加险
		 */
		sumRealPayText2020_FJ9("赔款 = @(车损赔款 + 保险金额)@ × (1 - 绝对免赔率)  - 免赔额  \r\n"+"     = @(%s + %s)@ × (1 - %s%%) - %s"),

		 /**
		 * 施救费，车损赔款都大于保额
		 */
		sumRealPayText2020_10("赔款 = @(保险金额 + 保险金额)@ - 免赔额   \r\n"+"     = @(%s + %s)@ - %s"),
		
		 /**
		 * 施救费，车损赔款都大于保额
		 */
		sumRealPayText2020_FJ10("赔款 = @(保险金额 + 保险金额)@ × (1 - 绝对免赔率) - 免赔额   \r\n"+"     = @(%s + %s)@ × (1 - %s%%) - %s"),
		
          /**
		 * 无法找到第三方 施救费公式
		 */
		rescueFeeText_NT("施救费用赔款 = 核定施救费用 - 施救费交强险已赔付金额 \r\n"+
                       "            = %s - %s"),
                       
		rescueFeeText_overNT("施救费用赔款 = 险别保额 - 施救费交强险已赔付金额 \r\n"+
                               "            = %s - %s"),
                               
		rescueFeeText_unfull_NT("施救费用赔款 = (核定施救费用 - 施救费交强险已赔付金额) × (保险金额 / 新车购置价)\r\n"+"            = (%s - %s) × (%s / %s) "),
		                              
		sumRealPayTextA_NT("赔款 = (车损赔款 + 施救费赔款) × (1 - 加扣免赔率) - 免赔额\r\n"+"     = (%s + %s) × (1- %s%%) - %s"),
		                         
		sumRealPayOverTextA_NT1("赔款 = (险别保额 + 施救费赔款) × (1-加扣免赔率) - 免赔额\r\n"+"     = (%s + %s) × (1- %s%%)  - %s"),
		                       
		sumRealPayOverTextA_NT2("赔款 = (施救费赔款 + 车损赔款 ) × (1-加扣免赔率) - 免赔额\r\n"+"     = (%s + %s) × (1- %s%%)  - %s"),
		                       
		sumRealPayOverTextA_NT3("赔款 = (险别保额+ 险别保额) × (1-加扣免赔率) - 免赔额\r\n"+"     = (%s + %s) × (1- %s%%) - %s"),
		                               
		sumRealPayTextA_IsNT("赔款 = (车损赔款 + 施救费赔款) × 无法找到第三方免赔率 - 无责代赔金额\r\n"+"     = (%s + %s) × %s%% - %s"),

                                                  
		sumRealLossTextZF_NTCPRC("车损赔款 = 定损金额 - 交强险已赔付金额 - 其他扣除 \r\n"+
                               "        = %s - %s - %s"),
		sumRealLossOverTextZF_NTCPRC("车损赔款 = 险别保额  - 交强险已赔付金额  \r\n"+
                                       "        = %s - %s"),
		sumRealLossTextBelowDamageItemRealCost4ZF_NT("车损赔款 = (定损金额  - 交强险已赔付金额 - 其他扣除) × (出险时险别保险金额 / 投保时保险车辆新车购置价)\r\n"+"        = (%s - %s - %s) × (%s / %s)"),
                                               
		sumRealLossTextOverCost4ZF_NT("车损赔款 = (车辆实际价值 - 交强险已赔付金额 )\r\n"+
                                    "        = %s - %s"),                                  

        /**
		 * 车辆在代付情况下损失金额大于实际价值情况下使用的计算书文本。
		 */
		sumRealLossTextOverRealCost4ZC("车损赔款 = (出险时车辆实际价值 - 残值 - 交强险已赔付金额 - 其他扣除) × 责任对方事故责任比例\r\n"+"        = (%s - %s - %s - %s) × %s"),
        /**
		 * ADD BY FZD 定损方式是推定全损，损失程度是全部损失且定损金额小于保额，用公式2
		 */
		sumRealLossTextOverRealCost4ZC2("车损赔款 = (定损金额 - 残值 - 交强险已赔付金额 - 其他扣除) × 责任对方事故责任比例\r\n"+"        = (%s - %s - %s - %s) × %s"),
        /**
		 * ADD BY FZD 定损方式是推定全损，损失程度是全部损失且定损金额大于保额，用公式3
		 */
		sumRealLossTextOverRealCost4ZC3("车损赔款 = (保额 - 残值 - 交强险已赔付金额 - 其他扣除) × 责任对方事故责任比例\r\n"+"        = (%s - %s - %s - %s) × %s"),

		sumRealLossTextOverRealCost4ZC3_CRPC("车损赔款 = (保额 - 残值 - 交强险已赔付金额 - 其他扣除)  × 责任对方事故责任比例 \r\n"+
                                      "        = (%s - %s - %s - %s)"),

        /**
		 * 应用条件：compensateExp.getSumRealLoss() > compensateExp.getDamageAmount()。
		 */
		sumRealPayText1("代责任对方商业赔款 = @(出险时险别保险金额 + 施救费赔款)@ × %s - 已预赔金额\r\n"+"                 = @(%s + %s)@ × %s - %s"),

        /**
		 * 应用条件：compensateExp.getRescueRealFee() > compensateExp.getDamageAmount()。
		 */
		sumRealPayText2("代责任对方商业赔款 = @(车损赔款 + 出险时险别保险金额)@ × %s - 已预赔金额 \r\n"+"                 = @(%s + %s)@ × %s - %s"),

        /**
		 * 应用条件：compensateExp.getRescueRealFee() > compensateExp.getDamageAmount()。
		 */
		sumRealPayText3("代责任对方商业赔款 = @(车损赔款 + 施救费赔款)@ × %s - 已预赔金额\r\n"+"                 = @(%s + %s)@ × %s - %s"),

        /**
         */
		sumRealPayText4("车损赔款 = (定损金额或保额 - 残值 - 追交强金额之和[按定损施救占比分配] - 其他扣除) × 事故责任比例 × (出险时险别保险金额 / 投保时保险车辆新车购置价)\r\n"+"        = (%s - %s - %s - %s) × %s × (%s / %s)"),


		sumRealPayText4_CPRC("车损赔款 = (定损金额或保额 - 残值 - 追交强金额之和[按定损施救占比分配] - 交强险已赔付金额 - 其他扣除) × 事故责任比例\r\n"+"        = (%s - %s - %s - %s - %s) × %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.damageItemRealCost。 2.CompensateExp.sumRest。 3.part。 double part4SumRealLoss = (compensateList.getSubrogationBzRealPay() * (compensateExp.getDamageItemRealCost() -
		 * compensateExp.getSumRest()) / (compensateExp.getDamageItemRealCost() - compensateExp.getSumRest() + compensateExp.getRescueFee())); 4.CompensateExp.otherDeductFee。
		 * 5.CompensateExp.indemnityDutyRate。
		 */
		sumRealPayText5("车损赔款 = (出险时车辆实际价值 - 残值 - 追交强金额之和[按定损施救占比分配] - 其他扣除) × 事故责任比例 \r\n"+"        = (%s - %s - %s - %s) × %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.rescueFee。 2.part。 double part = (compensateList.getSubrogationBzRealPay() * compensateExp.getRescueFee() / (:sumLoss - compensateExp.getSumRest() +
		 * compensateExp.getRescueFee())); :sumLoss 不超额取compensateExp.sumLoss ， 否则取compensateExp.damageAmount。 4.CompensateExp.indemnityDutyRate。
		 */
		rescueFeeText1("施救费用赔款 = (核定施救费用 - 追交强金额之和[按定损施救占比分配]) × 事故责任比例\r\n"+"            = (%s - %s) × %s"),
        
        /**
		 * 参数配置如下： 1.CompensateExp.rescueFee。 2.part。 double part = (compensateList.getSubrogationBzRealPay() * compensateExp.getRescueFee() / (:sumLoss - compensateExp.getSumRest() +
		 * compensateExp.getRescueFee())); :sumLoss 不超额取compensateExp.sumLoss ， 否则取compensateExp.damageAmount。 3.CompensateExp.damageAmount。 4.CompensateExp.entryItemCarCost。
		 * 5.CompensateExp.indemnityDutyRate。 6.CompensateExp.damageItemRealCost。 7.CompensateExp.damageItemRealCost。
		 */
		rescueFeeText1_unfull("施救费用赔款 = (核定施救费用 - 追交强金额之和[按定损施救占比分配]) × (保险金额 / 新车购置价) × 事故责任比例 × (出险时车辆实际价值 / 出险时车辆实际价值) \r\n"+"            = (%s - %s) × (%s × %s) × %s × (%s / %s)"),


		rescueFeeText1_unfull_CPRC("施救费用赔款 = (核定施救费用 - 追交强金额之和[按定损施救占比分配]) × (保险金额 / 新车购置价) × 事故责任比例 \r\n"+"            = (%s - %s) × (%s × %s) × %s"),


		sumRealPayText_dw("赔款 = @(定损金额 + 核定施救费)@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额\r\n"+"     = @(%s + %s)@ × (1 - %s%%) × (1 - %s%%) - %s"),
						
		sumRealPayText_dw_A1("赔款 = @(车损赔款 + 施救费用赔款 - 无责代赔金额)@ × (1 - 绝对免赔率) × 事故责任比例\r\n"+"     = @(%s + %s - %s)@ × (1 - %s%%) × %s"),
		sumRealPayText2020_dw("赔款 = @(定损金额 + 核定施救费)@  - 免赔额\r\n"+"     = @(%s + %s)@ - %s"),

		sumRealPayText2020_dw_AG("赔款 = @(车损赔款 + 施救费用赔款 - 无责代赔金额)@ × (1 - 绝对免赔率) × 事故责任比例\r\n"+"     = @(%s + %s - %s)@ × (1 - %s%%) × %s"),


		// 追偿金额=代位实际赔款-我方应承担赔款
		sumRealPayText_dwFinall_A("追偿金额  = 代位实际赔款 - 我方应承担赔款 - 第三方已赔付金额\r\n"+
						"     = %s - %s - %s"),			
		sumRealPayText_dwFinall_A1("追偿金额  = 代位实际赔款 - 我方应承担赔款\r\n"+
						"     = %s - %s"),

        /**
		 * 发动机险限额内计算书。
		 */
		sumRealLossTextBelowAmount_X1("对发动机损失的赔款 = (定损金额  - 其他扣除)*(保险金额/投保时保险车辆的新车购置价)\r\n"+"                 = （%s - %s)*(%s / %s) "),
                                         
		sumRealLossTextBelowAmount_X1_CPRC("对发动机损失的赔款 = 定损金额  - 其他扣除\r\n"+
                              "                 = %s - %s "),
        /**
		 * 发动机险超限额计算书。
		 */
		sumRealLossTextOverAmount_X1("对发动机损失的赔款 = 出险时车辆实际价值  *(保险金额/投保时保险车辆的新车购置价)\r\n"+
                                        "                 = %s * (%s / %s)"),
                                        
		sumRealLossTextOverAmount_X1_CPRC("对发动机损失的赔款 = 险别保额 \r\n"+
                                        "                 = %s "),
                                        
        /**
		 * 发动机险最终赔付
		 */
		sumRealPayText_X1("赔款 = @发动机损失@ × (1 - 绝对免赔率) \r\n"+"     = @%s@ × (1 - %s%%) "),


        /**
		 * * 车辆在自付情况下使用的计算书文本（新增设备险）。
		 */
		sumRealLossTextBelowDamageItemRealCost4ZF_X("车损赔款 = (定损金额 - 其他扣除) × 事故责任比例\r\n"+"        = （%s - %s) × %s%%"),
		sumRealLossTextBelowDamageItemRealCost4ZF2020_RS_VS_DS_DC("赔款 = 定损金额  \r\n"+"        = %s "),
		
		sumRealLossTextBelowDamageItemRealCost4ZF2020_X("赔款 = min(险别保额  ,  (定损金额  - 已从第三方获取的赔款)  × 事故责任比例) \r\n"+"        = min(%s , (%s -%s) × %s%%) "),
		sumRealLossTextBelowDamageItemRealCost4ZFS2020_X("车损赔款 =  (定损金额  - 已从第三方获取的赔款)  × 事故责任比例 \r\n"+"        = (%s -%s) × %s%%  "),
		sumRealPayText2020_W1("车损赔款 = min(险别剩余保额  , (定损金额 - 已从第三方获取的赔款  - 其他扣除) × 事故责任比例)\r\n"+"        = min(%s , (%s - %s - %s) × %s%%)"),
		sumRealLossTextOverRealCost4ZF_X_CPRC("车损赔款 = 险别保额  × 事故责任比例\r\n"+"        = %s  × %s%%"),
		
		sumRealPayText_X3("车损赔款 = (定损金额 - 已从第三方获取的赔款) × (1 - 约定绝对免赔率)\r\n"+"        = （%s - %s) × (1 - %s%%)"),
        
		sumRealPayText_X3_CPRC("车损赔款 = (保险金额 - 已从第三方获取的赔款) × (1 - 约定绝对免赔率)\r\n"+"        = （%s - %s) × (1 - %s%%)"),
		
		sumRealPayText_X_CPRC("赔款 = @%s@ × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 免赔额 \r\n"+"     = @%s@ × (1- %s%%)× (1 - %s%%) - %s"),
                                                     
		sumRealPayOverText_X("赔款 = %s × (1 - 事故责任免赔率) × (1 - 可选免赔率) - 已预赔金额\r\n"+"     = %s × (1- %s%%)× (1 - %s%%) - %s"),
                     
		sumRealPayText_X("赔款 = @赔付金额@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @%s@ × (1- %s%%)× (1 - %s%%) "),
                     
	     /**
		 * * 新增设备险 勾选了无法找到第三方
		 */
		sumRealLossText_X_NT("新增设备赔款 = %s \r\n"+
                         "        = %s "),
                         
		sumRealPayTextX_isNT("赔款 = 新增设备赔款 × × 无法找到第三方免赔率 \r\n"+"     = %s × %s%%"),
                
		sumRealPayOverTextX_NT("赔款 = 新增设备赔款  × (1-加扣免赔率) \r\n"+"     =  %s × (1- %s%%)"),
          
        


		sumRealLossText2020_BP("赔款= min( 责任限额   , 定损金额  × 事故责任比例)   \r\n"+"  = min( %s, %s × %s%% )\r\n"+ "        = %s "),
		sumRealLossText2020_D11P("赔款= min( 责任限额   , 定损金额  × 事故责任比例)   \r\n"+"  = min( %s, %s × %s%%)\r\n"+ "        = %s "),
		sumRealLossText2020_D12P("赔款= min( 责任限额   , 定损金额 × 事故责任比例 )   \r\n"+"  = min( %s, %s × %s%%)\r\n"+ "        = %s "),
        /**
		 * 三者险
		 */
		sumRealPayText_B_personName("赔款 = @(%s - 交强已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @(%s - %s - %s) × %s%%@  × (1 - %s%%) × (1 - %s%%)"),
		sumRealPayText2020_B_personName("赔款 = @(%s - 交强已赔付金额 - 其他扣除) × 事故责任比例@ \r\n"+"     = @(%s - %s - %s) × %s%%@ "),
		sumRealPayText2020_FJB_personName("赔款 = @(%s - 交强已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s - %s - %s) × %s%%@  × (1 - %s%%)"),
		sumRealPayText_B("赔款 = @(定损金额 + 施救费  - 交强已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @(%s + %s - %s - %s) × %s%%@  × (1 - %s%%) × (1 - %s%%)"),
		sumRealPayText2020_B("赔款 = @(定损金额 + 施救费  - 交强已赔付金额 - 其他扣除) × 事故责任比例@ \r\n"+"     = @(%s + %s - %s - %s) × %s%%@ "),
		sumRealPayText2020_FJB("赔款 = @(定损金额 + 施救费  - 交强已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s + %s - %s - %s) × %s%%@  × (1 - %s%%)"),
		sumRealOverPayText_B_personName("赔款 = @出险时险别保险金额[%s]@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @%s@ × (1 - %s%%) × (1 - %s%%)"),
		sumRealOverPayText2020_B_personName("赔款 = @出险时险别保险金额[%s]@ \r\n"+"     = @%s@ "),
		sumRealOverPayText2020_FJB_personName("赔款 = @出险时险别保险金额[%s]@ \r\n"+"     = @%s@ "),
		sumRealOverPayText_B("赔款 = @保险金额  × 赔付比例@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @%s × %s%%@ × (1 - %s%%) × (1 - %s%%)"),
		sumRealOverPayText2020_B("赔款 = @保险金额  × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
		sumRealOverPayText2020_FJB("赔款 = @保险金额  × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.sumLoss。 3.CompensateExp.rescueFee。 4.CompensateExp.sumRest。 5.CompensateExp.bZpaid。 6.CompensateExp.otherDeductFee。
		 * 6.CompensateExp.indemnityDutyRate。 7.CompensateExp.deductibleRate。 8.CompensateExp.sumPrePay。
		 */
		sumRealPayText15("赔款 = @(定损金额 + 施救费 - 残值 - 交强已赔付金额 - 其他扣除) × 事故责任比例@ × %s - 已预赔金额 \r\n"+"     = @(%s + %s - %s - %s - %s) × %s@  × %s - %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.damageAmount。 3.CompensateExp.deductibleRate。 4.CompensateExp.sumPrePay。
		 */
		sumRealPayText16("赔款 = @出险时险别保险金额@ × %s - 已预赔金额\r\n"+"     = @%s@ × %s - %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.entryItemCarCost。 3.CompensateExp.deductibleRate。 4.CompensateExp.sumPrePaid。
		 */
		sumRealPayText18("赔款 = @出险时险别保险金额@ × %s - 已预赔金额\r\n"+"     = @%s@ × %s - %s"),
             
	     /**
		 * 盗抢险
		 */
		sumRealPayTextG("赔款 = @(保险金额  - 其他扣除)@ × (1 - 绝对免赔率  - 可选免赔率) \r\n"+"     = @(%s - %s)@ × (1 - %s%% - %s%%) "),
                                 
		sumRealPayTextCostG("赔款 = @%s@ × (1 - 绝对免赔率 - 可选免赔率) \r\n"+"     = @%s@ × (1 - %s%% - %s%%)"),
	             
	             
		sumRealPayTextBPartG("赔款 = @(实际损失金额  + 施救费 - 其他扣除)@ × (1 - 可选免赔率 ) \r\n"+"= @(%s + %s - %s)@ × (1 - %s%%)"),
                         
		sumRealPayTextPartG("赔款 = @(实际损失金额 + 施救费  - 其他扣除)@ \r\n"+
                         "= @(%s + %s - %s)@ "),
        

                         
         /**
 		 * 2020盗抢险
 		 */
 		sumRealPayText2020_G("赔款 = @(保险金额  - 其他扣除)@ \r\n"+"     = @(%s - %s)@ "),

 		sumRealPayTextCost2020_G("赔款 = @%s@ \r\n"+"     = @%s@ "),


 		sumRealPayTextBPart2020_G("赔款 = min(保险金额 ,(实际损失金额  + 施救费 - 其他扣除)) \r\n"+"     = min(%s , %s + %s - %s)"),
        
 		sumRealPayText2020_FJ_G("赔款 = min(保险金额 ,(实际损失金额  + 施救费 - 其他扣除) × (1 - 绝对免赔率  )) \r\n"+"     = min(%s , (%s + %s - %s) × (1 - %s%%)) "),

		sumRealPayTextCost2020_FJ_G("赔款 = @%s@ × (1 - 绝对免赔率) \r\n"+"     = @%s@ × (1 - %s%%)"),


		sumRealPayTextBPart2020_FJ_G("赔款 = @(实际损失金额  + 施救费 - 其他扣除)@ × (1 - 绝对免赔率 ) \r\n"+"= @(%s + %s - %s)@ × (1 - %s%%)"),

         /**
		 * 车上人员
		 */
		sumRealPayText_D11("赔款 = @(%s - 交强险已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @(%s - %s - %s) × %s%%@ × (1 - %s%%) × (1 - %s%%)"),

       
		sumRealPayOverAmountText_D11("赔款 = @单位赔偿金额[%s] × 赔付比例@ × (1 - 事故责任免赔率) × (1 - 可选免赔率)\r\n"+"     = @%s × %s%%@ × (1 - %s%%) × (1 - %s%%)"),
		
		/**
		 * 车上司机
		 */
		sumRealPayText2020_D11("赔款 = @(%s - 交强险已赔付金额 - 其他扣除) × 事故责任比例@ \r\n"+"     = @(%s - %s - %s) × %s%%@ "),


		sumRealPayOverAmountText2020_D11("赔款 = @单位赔偿金额[%s] × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
		
		
		/**
		 * 车上司机附加险
		 */
		sumRealPayText2020_FJ_D11("赔款 = @(%s - 交强险已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s - %s - %s) × %s%%@ × (1 - %s%%)"),


		sumRealPayOverAmountText2020_FJ_D11("赔款 = @单位赔偿金额[%s] × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
		
		/**
		 * 车上乘客
		 */
		sumRealPayText2020_D12("赔款 = @(%s - 交强险已赔付金额 - 其他扣除) × 事故责任比例@ \r\n"+"     = @(%s - %s - %s) × %s%%@ "),


		sumRealPayOverAmountText2020_D12("赔款 = @单位赔偿金额[%s] × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
		
		
		/**
		 * 车上司机附加险
		 */
		sumRealPayText2020_FJ_D12("赔款 = @(%s - 交强险已赔付金额 - 其他扣除) × 事故责任比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s - %s - %s) × %s%%@ × (1 - %s%%)"),


		sumRealPayOverAmountText2020_FJ_D12("赔款 = @单位赔偿金额[%s] × 赔付比例@ \r\n"+"     = @%s × %s%%@ "),
		
		
		
		
	     /**
		 * 车上货物责任险 对应安联： sumRealPayText37 sumRealPayText38
		 */                       
		sumRealPayText_D2("赔款 = @(财产定损金额 + 核定施救费用 - 交强险对车上货物赔款 - 其他扣除) × 事故责任比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s + %s - %s - %s) × %s%%@ × (1 - %s%%)"),

		sumRealPayOverAmountText_D2("赔款 = @保险金额@ × (1 - 绝对免赔率)\r\n"+"     = @%s@ × (1 - %s%%)"),
                         

		
		 /**
		 * 车上货物责任险 对应安联： sumRealPayText37 sumRealPayText38
		 */
		sumRealPayText2020_D2("赔款 = @(财产定损金额 + 核定施救费用 - 交强险对车上货物赔款 - 其他扣除) × 事故责任比例@ \r\n"+"     = @(%s + %s - %s - %s) × %s%%@ "),

		sumRealPayOverAmountText2020_D2("赔款 = @保险金额@ \r\n"+"     = @%s@ "),

         /**
		 * 玻璃单独破碎险
		 */
		sumRealOverPayText_F("赔款 = 出险时车辆实际价值 \r\n"+
         		 	"     = %s "),
                   	 
		sumRealPayText_F("赔款 = 定损金额  + 施救费 - 其他扣除 \r\n"+
                           "     = %s +%s - %s"),
                           
		sumRealOverPayText_F_CPRC("赔款 = 险别保额  \r\n"+
	    		   "     = %s "),

		sumRealPayText_F_CPRC("赔款 = 定损金额  + 施救费 - 其他扣除 \r\n"+
                          "     = %s +%s - %s "),
                                 
          /**
		 * 车身划痕险 对应安联： sumRealPayText24 sumRealPayText25
		 */
		sumRealPayOverAmoutText_L(" = @保险金额  @ × (1 - 绝对免赔率)\r\n"+"   = @%s@ × (1 - %s%%)"),
		sumRealPayOverAmoutText2020_L(" = @保险金额  @\r\n"+"   = @%s@"),
	                      
		sumRealPayText_L(" = @(定损金额 -其他扣除) @ × (1 - 绝对免赔率)\r\n"+"   = @(%s -%s)@ × (1 - %s%%)"),
		sumRealPayText2020_L(" = @(定损金额 -其他扣除) @ \r\n"+"   = @(%s -%s)@ "),
	                      
          /**
		 * 自燃
		 */
		sumRealPayText_Z("赔款 = @(%s + 施救费  - 其他扣除)@ × (1 - 绝对免赔率 )\r\n"+" = @(%s + %s - %s)@ × (1 - %s%%)"),
		
		sumRealOverPayText_Z("赔款 = @(%s + 施救费 )@ × (1 - 绝对免赔率 )\r\n"+" = @(%s + %s)@ × (1 - %s%%)"),
	     /**
		 * 代步险
		 */
		sumRealPayTextT("赔款 = 赔偿天数/数量 × 单位受损金额  - 其他扣除\r\n"+" = %s × %s - %s"),
	
		sumRealOverPayText_T(" = 最高赔偿天数/数量 × 单位受损金额 - 其他扣除 \r\n"+" = %s × %s - %s"),
        /**
		 * 修理期间费用补偿险 RF 和 代步险
		 */
		sumRealPayText_C("赔款 = (赔偿天数/数量 - 绝对免赔天数) × 单位受损金额  - 其他扣除\r\n"+" = (%s - %s) × %s - %s "),

		sumRealOverPayText_C(" = (最高赔偿天数/数量 - 绝对免赔天数) × 单位受损金额 - 其他扣除  \r\n"+" = (%s - %s) × %s - %s "),
                                    
	      


		
		sumRealPayText2020_C("赔款 = (赔偿天数/数量 ) × 单位受损金额  - 其他扣除\r\n"+" = (%s) × %s - %s "),
		
		sumRealOverPayText2020_C(" = (最高赔偿天数/数量 ) × 单位受损金额 - 其他扣除  \r\n"+" = (%s) × %s - %s "),
		

		/** 附加油污污染责任保险 **/
		sumRealPayText_V1("赔款 = @(定损金额-其他扣除)@ × 事故责任比例 × (1 - 绝对免赔率) \r\n"+"= @(%s -%s)@ × %s%% × (1 - %s%%)"),
	                              
		sumRealOverPayText_V1("赔款 = @保险金额@ × (1 - 绝对免赔率) \r\n"+"= @%s@ × %s%% "),

		/** 租车人人车失踪险 **/
		sumRealPayText_Z1("赔款 = @(定损金额 - 其他扣除)@ × (1 - 绝对免赔率)×(1 - 加扣免赔率)\r\n"+"= @(%s -%s)@ × (1 - %s%%) × (1 - %s%%)"),
	             
		sumRealPayOverAmountText_Z1("赔款 = @保险金额@ × (1 - 绝对免赔率)×(1 - 加扣免赔率)\r\n"+"= @%s@ × (1 - %s%%) × (1 - %s%%)"),
	    	
		/** 约定区域通行费用特约条款 **/
		sumRealPayTextZ2("赔款 = @(定损金额 - 其他扣除)@ × 事故责任比例 × (1-绝对免赔率)×(1-加扣免赔率)\r\n"+"= @(%s -%s)@ × %s%% × (1 - %s%%) × (1 - %s%%)"),
	    	                          
		sumRealOverPayTextZ2("赔款 = @保险金额@ × 事故责任比例 × (1 - 绝对免赔率)×(1 - 加扣免赔率)\r\n"+"=  @%s@  × %s%% × (1 - %s%%) × (1 - %s%%)"),
	                            
        /**
		 * 精神损害赔偿
		 */
		sumRealPayText_R("赔款 = @(应由被保险人承担的精神损害赔偿责任 - 交强险有关精神损失的赔款 - 其他扣除)@ × (1 - 绝对免赔率)\r\n"+"     = @(%s - %s - %s)@ × (1 - %s%%) "),
            

		sumRealPayText2020_R("赔款 = @(应由被保险人承担的精神损害赔偿责任 - 交强险有关精神损失的赔款 - 其他扣除)@ \r\n"+"     = @(%s - %s - %s)@ "),

		sumRealOverPayText_R("赔款 = @(单位赔偿金额[%s] - 交强险有关精神损失的赔款 - 其他扣除) × 赔付比例@ × (1 - 绝对免赔率)\r\n"+"     = @(%s - %s - %s) × %s@ × (1 - %s%%)"),
                

		sumRealOverPayText2020_R("赔款 = @(单位赔偿金额) @ \r\n"+"     = @(%s)@ "),

		/** 起重、装卸、挖掘车辆扩展损失扩展条款 **/
		sumRealPayText_K("赔款 = @(定损金额 - 其他扣除) × 事故责任比例@   × (保险金额 / 投保时保险车辆新车购置价) × (1-事故责任免赔率) × (1-加扣免赔率)\r\n"+"= @(%s -%s) × %s%%@ × (%s / %s) × (1 - %s%%) × (1 - %s%%) "),
	             
	             
		sumRealOverPayText_K("赔款 = @出险时车辆实际价值 × 事故责任比例@   × (1-事故责任免赔率) × (1-加扣免赔率)\r\n"+"= @%s × %s%%@ × (1 - %s%%) × (1 - %s%%)"),
	             
		/** 随车行李物品损失保险 **/
		sumRealPayTextNZ("赔款 = @(定损金额 - 其他扣除)@ × 事故责任比例 × (1-事故责任免赔率) × (1-加扣免赔率)\r\n"+"= @(%s -%s)@ × %s%% × (1 - %s%%) × (1 - %s%%)"),
	      
		sumRealOverPayTextNZ("赔款 = 保险金额   × (1-事故责任免赔率) × (1-加扣免赔率)\r\n"+"= @%s@ × (1 - %s%%) × (1 - %s%%)"),

        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.entryItemCarCost。 3.CompensateExp.deductibleRate。 4.CompensateExp.sumPrePaid。
		 */
		sumRealPayText19("赔款 = @出险时保险车辆的实际价值@ × %s - 已预赔金额\r\n"+"     = @%s@ × %s - %s"),

    
  

       
      
        /**
		 * 参数配置如下： 1.sumLoss sumLoss = CompensateExp.sumLoss + CompensateExp.rescueFee - CompensateExp.sumRest - Compensate.otherDeductFee 2.CompensateExp.deductibleRate。 3.CompensateExp.sumPrePay。
		 */
		sumRealPayText26(" = 定损金额 × 免赔率 - 已预赔金额 \r\n"+"     = %s × %s - %s"),

        /**
		 * 参数配置如下： 1.sumLoss sumLoss = CompensateExp.sumLoss + CompensateExp.rescueFee - CompensateExp.sumRest - Compensate.otherDeductFee 2.CompensateExp.deductibleRate。 3.CompensateExp.sumPrePay。
		 */
		sumRealPayText27(" = 出险时险别保险金额 × 免赔率 - 已预赔金额 \r\n"+"     = %s × %s - %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.damageAmount。 2.CompensateExp.deductibleRate。 3.Compensateexp.sumPrePay。
		 */
		sumRealPayText28("赔款 = @出险时险别保险金额@ × 免赔率  - 已预赔金额\r\n"+"     = @%s@ × %s - %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.rescueFee。 3.CompensateExp.deductibleRateText。 4.CompensateExp.damageAmount。
		 */
		sumRealPayText29("赔款 = 施救费 × %s + 出险时险别保险金额 \r\n"+"     = %s × %s + %s"),



        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.rescueFee。 3.CompensateExp.deductibleRateText。 4.CompensateExp.sumRealLoss。
		 */
		sumRealPayText31("赔款 = 施救费 × %s + 对发动机损失的赔款 \r\n"+"     = %s × %s + %s"),

        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.damageAmount。 3.CompensateExp.deductibleRate。
		 */
		sumRealPayText34("赔款 = @保险限额@ × %s \r\n"+"     = @%s@ × %s"),

   
                         
        
 
        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.damageAmount。 3.CompensateExp.damageAmount。 4.CompensateExp.deductibleRate。 5.CompensateExp.sumPrePay。
		 */
		sumRealPayText41("代责任对方商业赔款 = @(出险时险别保险金额 + 出险时险别保险金额)@ × %s  - 已预赔金额\r\n"+"                 = @(%s + %s)@ × %s - %s"),
        
        /**
		 * 参数配置如下： 1.CompensateExp.deductibleRateText。 2.CompensateExp.sumLoss。 3.CompensateExp.dutyDeductibleRate。 4.CompensateExp.selectDeductibleRate。 5.CompensateExp.sumPrePay。
		 */
		sumRealPayText42("赔款 = @实际损失金额@ × %s - 已预赔金额\r\n"+"= @%s@ × (1 - %s%%) × (1 - %s%%) - %s"),
                         
       
                                 
		sumRealPayText_NT("赔款 = (定损金额 - 其他扣除) × 无法找到第三方加扣免赔率\r\n"+"= (%s - %s) × %s%%"),
       
		sumRealOverPayText_NT("赔款 = (保险限额  + 保险限额) × 无法找到第三方加扣免赔率\r\n"+"= (%s + %s) × %s%%"),
     
    
      
     
        /**
		 * 参数配置如下： 1.CompensateExp.damageAmount。
		 */
		simpleText("车损赔款 = 出险时险别保险金额 = %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyLossInfo.lossName。 2.ThirdPartyLossInfo.lossFeeName。 3.ThirdPartyInfo.dutyAmount。 4.ThirdPartyLossInfo.sumLoss。 5.allDamageAmount -
		 * ThirdPartyLossInfo.damageAmount。allDamageAmount表示所有涉案车辆的限额和。
		 */
		payAmountText("\r\n交强险 : %s \r\n本项理算金额 = %s × (本车限额  / 分摊责任限额之和) \r\n= %s × (%s / %s) \r\n= %s"),

		payAmountText_no("\r\n交强险 : %s \r\n %s的交强限额已赔完，故本项理算金额  = 0 \r\n"),
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.lossName。 2.ThirdPartyCompInfo.sumLoss。 3.noDutyCarNum。无责方数量。 4.ThirdPartyCompInfo.sumLoss() / noDutyCarNum。
		 */
		noDutyPayText("\r\n交强险无责代赔 : %s \r\n本项理算金额 = 损失金额 / 无责方车辆个数 \r\n = %s / %s \r\n = %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.lossName。 2.ThirdPartyCompInfo.lossFeeName。 3.ThirdPartyCompInfo.sumLoss。 3.noDutyCarNum。无责方数量。 4.ThirdPartyCompInfo.sumLoss() / noDutyCarNum。
		 */
		mainCarNoDutyPayText("\r\n交强险 : %s 的 %s \r\n本项理算金额 = 损失金额 / 无责方车辆个数 \r\n "+
                                                           "= %s / %s \r\n" +
                                                           "= %s"),

        /**
		 * 参数配置如下： 1.ThirdPartyInfo.dutyAmount。 2.dutyCarNum。有责方数量。 3.ThirdPartyInfo.dutyAmount / dutyCarNum。
		 */
		noDutyPayOverAmountText("\r\n无责代赔金额超过无责方赔付限额 ， 故：\r\n本项理算金额 = 责任限额 / 有责方车辆个数 \r\n "+
                                                                           "= %s / %s \r\n " +
                                                                           "= %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.payCarAmount。 2.ThirdPartyCompInfo.sumLoss / noDutyCarNum。noDutyCarNum表示无责方数量。 3.ThirdPartyCompInfo.carLossSum / noDutyCarNum。noDutyCarNum表示无责方数量。
		 * 4.ThirdPartyCompInfo.sumRealPay。
		 */
		noDutyPayOverAmountAdjust4PropText("\r\n损失项之和超限额,进行超限额调整"+"\r\n本项实赔金额 = 赔付损失车辆交强限额 × 分项赔付金额 / 总赔付金额之和\r\n"+" = %s × %s / %s \r\n"+
                                                       " = %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.lossName。 2.ThirdPartyCompInfo.lossFeeName。 3.dutyCarNum。表示有责方数量。 4.ThirdPartyCompInfo.sumLoss / dutyCarNum。dutyCarNum表示有责方数量。
		 */
		dutyPayNoDuty4PropText("\r\n交强险 : %s 的 %s "+"\r\n本项理算金额= 损失金额 / 有责方车辆个数\r\n"+
                                           "= %s / %s \r\n" +
                                           "= %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.lossName。 2.ThirdPartyCompInfo.lossFeeName。 3.ThirdPartyCompInfo.sumLoss。 4.ThirdPartyLossInfo.noDutyCarPay。 5.dutyCarNum。dutyCarNum表示有责方数量。
		 * 6.(ThirdPartyCompInfo.sumLoss - ThirdPartyLossInfo.noDutyCarPay) / (dutyCarNum - 1)。dutyCarNum表示有责方数量。
		 */
		dutyPayDuty4PropText("\r\n交强险 : %s 的 %s "+"\r\n本项理算金额 = (损失金额 - 无责方赔付金额) / (有责方车辆个数 - 1)"+
                             "\r\n= (%s - %s) / (%s - 1)" +
                             "\r\n= %s"),
        /**
		 * 参数配置如下： 1.ThirdPartyInfo.dutyAmount。 2.ThirdPartyCompInfo.payAmount。 3.ThirdPartyInfo.payAmount。 4.ThirdPartyCompInfo.adjPayAmount。
		 */
		overPayAdjust4PropText("\r\n损失金额超过赔付限额，进行超限额调整 : "+"\r\n= 本车责任限额 × 损失项的限额比例赔偿金额 / 损失项的限额比例赔偿金额之和"+"\r\n= %s × %s / %s"+
                               "\r\n= %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyInfo.leftAmount。 2.ThirdPartyCompInfo.leftPay。 3.ThirdPartyInfo.sumNoenoughPay。 4.ThirdPartyCompInfo.leftPayAmount。
		 */
		leftPayAmountText("\r\n损失存在剩余限额 , 进行剩余限额分摊 : "+"\r\n= 赔付剩余限额 × 损失项的未赔足金额 / 所有未赔付金额"+"\r\n= %s × %s / %s"+
                               "\r\n= %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyCompInfo.leftPay。
		 */
		overLossAdjust4PropText("\r\n剩余限额赔付金额超过未赔足金额 ， 所以 = %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyLossInfo.sumRealPay。
		 */        
		sumRealPayQf4PropText("\r\n实际赔付金额 = (限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额)"+
                              "\r\n= %s"),
        
        /**
		 * 参数配置如下： 1.ThirdPartyLossInfo.sumRealPay。
		 */        
		sumRealPayZfText("\r\n实际赔付金额 = 限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额"+
                              "\r\n=%s"),
        
        /**
		 * 参数配置如下： 1.CompensateExp.sumRealPay。 2.CompensateExp.msumRealPay。 3.CompensateExp.sumRealPay + CompensateExp.msumRealPay。
		 */        
		totalPayText("     总赔付金额 = 赔付金额 + 不计免赔金额"+
                              "\r\n = %s + %s" +
                              "\r\n = %s" +
                              "\r\n"),
                              
        /**
		 * 已承保M3-附加绝对免赔率特约险计算赔款：
		 */
		sumRealPayText20("赔款 = (损失金额  + 施救费  - 已从第三方获取的赔款) * (1 - 约定绝对免赔率)   \r\n"+"     = (%s + %s - %s) * (1 - %s%%)"),
		sumRealPayText21("赔款 = (损失金额  + 保险金额  - 已从第三方获取的赔款) * (1 - 约定绝对免赔率)   \r\n"+"     = (%s + %s - %s) * (1 - %s%%)"),
		sumRealPayText22("赔款 = (保险金额  + 施救费  - 已从第三方获取的赔款) * (1 - 约定绝对免赔率)   \r\n"+"     = (%s + %s - %s) * (1 - %s%%)"),
		sumRealPayText23("赔款 = (保险金额  + 保险金额  - 已从第三方获取的赔款) * (1 - 约定绝对免赔率)   \r\n"+"     = (%s + %s - %s) * (1 - %s%%)"),
		sumRealPayText24("赔款 = (车损赔款  + 施救费赔款  - 无责代赔金额) * 事故责任比例 * (1 - 约定绝对免赔率)   \r\n"+"     = (%s + %s - %s) * %s%% * (1 - %s%%)");
		

		
		
		public final String value;

        CompensateText(String value) {
            this.value = value;
        }
    }
    
    public static final class InsteadFlag{

		// 非代赔
    	public static final String NO_INSTEAD = "0";
		// 无责代赔
    	public static final String NODUTY_INSTEAD = "1";
		// 三者代赔三者
    	public static final String THIRD2THIRD_INSTEAD = "4";
    }
    
    /**
	 * 组织理算公式时 需要的空格格数。
	 */
    public static final class SpaceCount{
    	public static final int one = 1;
    	public static final int shorter = 5;
    	public static final int _short = 8;
    	public static final int normal = 10;
    	public static final int _long = 14;
    	public static final int longer = 21;
    	public static final int longest = 30; 
    }

	// 事故责任代码
    public static final class IndemnityDuty{

		public static final String FULL_DUTY = "0"; // 全责
		public static final String MAIN_DUTY = "1"; // 主责
		public static final String EQUAL_DUTY = "2"; // 同责
		public static final String SECOND_DUTY = "3"; // 次责
		public static final String NO_DUTY = "4"; // 无责

    }
    
    /**
	 * 立案状态标示
	 * 
	 * <pre></pre>
	 */
    public static final class ClaimCancleCode{

		// 立案注销
    	public static final String CLAIMCANCLE = "0";
		// 立案拒赔
    	public static final String CLAIMREJECT = "1";
    }

	/**
	 * 能够赔付标的车的主险险别List
	 */
	public static List<String> KindForSelfCar_List = new ArrayList<String>();
	static{
		KindForSelfCar_List.add("A");
		KindForSelfCar_List.add("A1");
		KindForSelfCar_List.add("Z");
		KindForSelfCar_List.add("L");
		KindForSelfCar_List.add("G");
		KindForSelfCar_List.add("F");
	}

	/**
	 * 医疗费用
	 */
	public static HashMap<String,String> MedicalFee_Map = new HashMap<String,String>();
	static{
		MedicalFee_Map.put("1","医疗费");
		MedicalFee_Map.put("2","续医费");
		MedicalFee_Map.put("3","营养费");
		MedicalFee_Map.put("4","住院伙食补助");
		MedicalFee_Map.put("5","整容费");
		MedicalFee_Map.put("18","住院费");
	}
	/**
	 * 死亡伤残
	 */
	public static HashMap<String,String> DisabilityFee_Map = new HashMap<String,String>();
	static{
		DisabilityFee_Map.put("6","误工费");
		DisabilityFee_Map.put("7","护理费");
		DisabilityFee_Map.put("8","残疾赔偿金");
		DisabilityFee_Map.put("9","残疾辅助器具费");
		DisabilityFee_Map.put("10","被抚养人生活费");
		DisabilityFee_Map.put("11","死亡赔偿金");
		DisabilityFee_Map.put("12","丧葬费");
		DisabilityFee_Map.put("13","交通费");
		DisabilityFee_Map.put("14","住宿费");
		DisabilityFee_Map.put("15","康复费");
		DisabilityFee_Map.put("16","精神损害抚慰金");
		DisabilityFee_Map.put("17","其他");
	}
	
	/**
	 * 医疗费用-上海
	 */
	public static HashMap<String,String> MedicalFee_Map_SH = new HashMap<String,String>();
	static{
		MedicalFee_Map_SH.put("1","医药费");
		MedicalFee_Map_SH.put("2","后续治疗费");
		MedicalFee_Map_SH.put("4","住院伙食补助");
		MedicalFee_Map_SH.put("11","营养费");
		MedicalFee_Map_SH.put("16","诊疗费");
		MedicalFee_Map_SH.put("17","住院费");
		MedicalFee_Map_SH.put("18","整容费");
		MedicalFee_Map_SH.put("20","材料植入费");
	}
	/**
	 * 死亡伤残-上海
	 */
	public static HashMap<String,String> DisabilityFee_Map_SH = new HashMap<String,String>();
	static{
		DisabilityFee_Map_SH.put("3","伤者误工费");
		DisabilityFee_Map_SH.put("5","护理费");
		DisabilityFee_Map_SH.put("6","残疾赔偿金");
		DisabilityFee_Map_SH.put("7","残疾辅助器具费");
		DisabilityFee_Map_SH.put("8","丧葬费");
		DisabilityFee_Map_SH.put("9","死亡补偿费");
		DisabilityFee_Map_SH.put("10","被抚养人生活费");
		DisabilityFee_Map_SH.put("12","康复费");
		DisabilityFee_Map_SH.put("13","精神损害抚慰金");
		DisabilityFee_Map_SH.put("14","交通费");
		DisabilityFee_Map_SH.put("15","住宿费");
		DisabilityFee_Map_SH.put("19","受害人亲属办理丧葬事宜支出的交通费");
		DisabilityFee_Map_SH.put("22","受害人亲属误工费");
		DisabilityFee_Map_SH.put("23","受害人亲属护理费");
		DisabilityFee_Map_SH.put("24","其他");
	}
	
	/**
	 * 计算书冲销状态
	 * @author yk
	 */
	 public static final class WRITEOFFFLAG{

		/** 正常案件 */
		public static final String NORMAL = "0";
		
		/** 全部冲销 */
		public static final String ALLOFF = "1";
		
		/** 部分冲销 */
		public static final String PARTOFF = "2";
	}

	/**
	 * 理算产量与理算类型
	 */
	public static final class CompensateType {

		/** 交强理算标志位 */
		public static final String COMP_CI = "1"; 
		/** 商业理算标志位 */
		public static final String COMP_BI = "2";
		/** 交强险种前缀 */
		public static final String PREFIX_CI = "11";
		/** 商业险种前缀 */
		public static final String PREFIX_BI = "12";
		/** 理算标志 */
		public static final String compen_type = "N";
		/** 预付标志 */
		public static final String prepay_type = "Y";
		/** 垫付标志 */
		public static final String padpay_type = "D";
	}

	/**
	 * 到岗的岗位返回false
	 */
	public static HashMap<String,Boolean> ISTOGRADE_MAP = new HashMap<String,Boolean>();
    static{
		ISTOGRADE_MAP.put(FlowNode.CancelVrf_LV1.getRoleCode(),false);// 立案注销审核分公司1级岗
		ISTOGRADE_MAP.put(FlowNode.CancelVrf_LV2.getRoleCode(),false);// 立案注销审核分公司2级岗
		ISTOGRADE_MAP.put(FlowNode.CancelLVrf_LV1.getRoleCode(),false);// 立案注销审核总公司级岗
		ISTOGRADE_MAP.put(FlowNode.ReCanVrf_LV1.getRoleCode(),false);// 注销恢复分公司审核1级
		ISTOGRADE_MAP.put(FlowNode.ReCanVrf_LV2.getRoleCode(),false);// 注销恢复分公司审核2级
		ISTOGRADE_MAP.put(FlowNode.ReCanVrf_LV3.getRoleCode(),false);// 注销恢复分公司审核3级
		ISTOGRADE_MAP.put(FlowNode.ReCanLVrf_LV11.getRoleCode(),false);// 注销恢复总公司审核1级
		ISTOGRADE_MAP.put(FlowNode.ChkBig_LV1.getRoleCode(),false);// 大案审核1级
		ISTOGRADE_MAP.put(FlowNode.ChkBig_LV2.getRoleCode(),false);// 大案审核2级
		ISTOGRADE_MAP.put(FlowNode.ChkBig_LV3.getRoleCode(),false);// 大案审核3级
		ISTOGRADE_MAP.put(FlowNode.ChkBig_LV4.getRoleCode(),false);// 大案审核4级
		ISTOGRADE_MAP.put(FlowNode.ChkBig_LV5.getRoleCode(),false);// 大案审核5级
		ISTOGRADE_MAP.put(FlowNode.PLBig_LV1.getRoleCode(),false);// 人伤大案审核1级
		ISTOGRADE_MAP.put(FlowNode.PLBig_LV2.getRoleCode(),false);// 人伤大案审核2级
		ISTOGRADE_MAP.put(FlowNode.PLBig_LV3.getRoleCode(),false);// 人伤大案审核3级
		ISTOGRADE_MAP.put(FlowNode.PLBig_LV4.getRoleCode(),false);// 人伤大案审核4级
		ISTOGRADE_MAP.put(FlowNode.PLBig_LV5.getRoleCode(),false);// 人伤大案审核5级
		ISTOGRADE_MAP.put(FlowNode.RecLossCar.getRoleCode(),false);// 车辆损余回收
		ISTOGRADE_MAP.put(FlowNode.RecLossProp.getRoleCode(),false);// 财产损余回收
		ISTOGRADE_MAP.put(FlowNode.Certi.getRoleCode(),false);// 单证收集
		ISTOGRADE_MAP.put(FlowNode.Compe.getRoleCode(),false);// 理算
		ISTOGRADE_MAP.put(FlowNode.ReOpenVrf_LV1.getRoleCode(),false);// 重开分公司1级审核
		ISTOGRADE_MAP.put(FlowNode.ReOpenVrf_LV2.getRoleCode(),false);// 重开总公司1级审核
		ISTOGRADE_MAP.put(FlowNode.PrePay.getRoleCode(),false);// 预付
		ISTOGRADE_MAP.put(FlowNode.PadPay.getRoleCode(),false);// 垫付
    }
    
	// 06 律师费
	// 07 诉讼费
	// 08 代查勘费
	// 13 公估费
	// 14 奖励费
	// 15 检测费
	// 16 咨询顾问费
	// 17 差旅费
	// 18 鉴定费
	// 99 其他

    public static HashMap<String,String> TransChargeMap = new HashMap<String,String>();
	static{
		TransChargeMap.put("06","23");// 律师费
		TransChargeMap.put("07","24");// 诉讼费
		TransChargeMap.put("08","06");// 代查勘费
		TransChargeMap.put("13","13");// 公估费
		TransChargeMap.put("14","25");// 奖励费
		TransChargeMap.put("15","07");// 检测费
		TransChargeMap.put("16","16");// 咨询顾问费
		TransChargeMap.put("17","17");// 差旅费
		TransChargeMap.put("18","26");// 鉴定费
		TransChargeMap.put("99","99");// 其他
		
		
	} 
	
	public static HashMap<String,String> TransPayTypeMap = new HashMap<String,String>();
	static{
		TransPayTypeMap.put("P512","06");// 预付律师费
		TransPayTypeMap.put("P612","06");// 律师费
		TransPayTypeMap.put("P513","07");// 预付诉讼费
		TransPayTypeMap.put("P613","07");// 诉讼费
		TransPayTypeMap.put("P54","08");// 预付代查勘费
		TransPayTypeMap.put("P64","08");// 代查勘费
		TransPayTypeMap.put("P51","13");// 预付公估费
		TransPayTypeMap.put("P67","13");// 公估费
		TransPayTypeMap.put("P6I","14");// 奖励费
		TransPayTypeMap.put("P510","15");// 预付检测费
		TransPayTypeMap.put("P61","15");// 检测费
		TransPayTypeMap.put("P55","16");// 预付咨询顾问费
		TransPayTypeMap.put("P65","16");// 咨询顾问费
		TransPayTypeMap.put("P56","17");// 预付差旅费
		TransPayTypeMap.put("P66","17");// 差旅费
		TransPayTypeMap.put("P511","18");// 预付鉴定费
		TransPayTypeMap.put("P611","18");// 鉴定费
		TransPayTypeMap.put("P69","99");// 其他
	} 
	
	public static HashMap<String,String> TransChargeToPayTypeMap = new HashMap<String,String>();
	static{
		TransChargeToPayTypeMap.put("06","P612");// 律师费
		TransChargeToPayTypeMap.put("07","P613");// 诉讼费
		TransChargeToPayTypeMap.put("08","P64");// 代查勘费
		TransChargeToPayTypeMap.put("13","P67");// 公估费
		TransChargeToPayTypeMap.put("14","P6I");// 奖励费
		TransChargeToPayTypeMap.put("15","P61");// 检测费
		TransChargeToPayTypeMap.put("16","P65");// 咨询顾问费
		TransChargeToPayTypeMap.put("17","P66");// 差旅费
		TransChargeToPayTypeMap.put("18","P611");// 鉴定费
		TransChargeToPayTypeMap.put("99","P69");// 其他
	} 
    
	/**
	 * 进入精友的标识
	 * @author yk
	 */
	public static final class JyFlag {

		/** 未进精友 或未返回数据 */
		public static final String NOIN = "0";
		/** 进入精友 返回值成功 */
		public static final String INBACK = "1";
		/** 自动核价 核损 */
		public static final String AUTO = "2";
	}
	
	public static final class MobileOperationType{

		/** 查勘接收 **/
		public static final String CHECKACCEPT = "查勘接收";
		/** 查勘暂存 **/
		public static final String CHECKSAVE = "查勘暂存";
		/** 查勘提交 **/
		public static final String CHECKSUBMIT = "查勘提交";
		/** 车辆定损接收 **/
		public static final String CARLOSSACCEPT = "车辆定损接收";
		/** 车辆定损暂存 **/
		public static final String CARLOSSSAVE = "车辆定损暂存";
		/** 车辆定损提交 **/
		public static final String CARLOSSSUBMIT = "车辆定损提交";
		/** 车辆核损退回 **/
		public static final String CARVERILOSSBACK = "车辆核损退回";
		/** 财产定损接收 **/
		public static final String PROPLOSSACCEPT = "财产定损接收";
		/** 财产定损暂存 **/
		public static final String PROPLOSSSAVE = "财产定损暂存";
		/** 财产定损提交 **/
		public static final String PROPLOSSSUBMIT = "财产定损提交";
		/** 财产核损退回 **/
		public static final String PROPVERILOSSBACK = "财产核损退回";
		/** 立案注销 **/
		public static final String CLAIMCANCLE = "立案注销";
		/** 定损注销 **/
		public static final String LOSSCANCLE = "定损注销";
		/** 报案注销 **/
		public static final String REGISTCANCLE = "报案注销";
		/** 人伤跟踪接收 **/
		public static final String PLOSSACCEPT = "人伤跟踪接收"; 
		/** 人伤跟踪暂存 **/
		public static final String PLOSSSAVE = "人伤跟踪暂存";
		/** 人伤提交 **/
		public static final String PLOSSSUBMIT = "人伤跟踪提交";
		/** 人伤审核退回 **/
		public static final String PLOSSLOSSBACK = "人伤退回";
	}
	
	/**
	 * 赔款支付状态
	 * @author WLL
	 */
	public static final class PayStatus {

		/** 已支付 */
		public static final String PAID = "1";
		/** 已送收付 */
		public static final String SENDPAY = "2";
		/** 已退票 */
		public static final String PAYBACK = "3";
	}
	
	/**
	 * 定时任务类型
	 * 
	 * <pre></pre>
	 * @author ★LinYi
	 */
    public static final class JobType {

		/** 强制立案 */
        public static final String CLAIMFORCE = "claimForce";
    }
    
    /**
	 * 定时任务状态
	 * 
	 * <pre></pre>
	 * @author ★LinYi
	 */
    public static final class JobStatus {

		/** 首次失败 */
        public static final String FIRST = "0";
		/** 成功 */
        public static final String SUCCEED = "1";
		/** 再次失败 */
        public static final String SECEND = "2";
        
    }

    
    /**
	 * 评分环节
	 * @author ly
	 */
    public static final class ScoreNode {

		/** 报案 */
        public static final String Regis = "C001";
		/** 查勘 */
        public static final String Check = "C002";
		/** 定损 */
        public static final String DLoss = "C003"; 
    }

	
	/**
	 * 山东风险预警系统损失项赔偿类型
	 * @author WLL
	 */
	public static final class EWLossFeeType {

		/** 车辆损失 */
		public static final String CARLOSS = "1";
		/** 财产损失 */
		public static final String PROPLOSS = "2";
		/** 死亡伤残 */
		public static final String DEATHLOSS = "3";
		/** 医疗费用 */
		public static final String MEDILOSS = "4";
	}

    /**
	 * 白名单
	 * @author zhujunde
	 */
	public static class WhiteList{
		public static final String ISMOBILE = "isMobile";
	}

	/** 调度查勘项类型 */
	public static class PriceType{
		//1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格
		public static final String PRICETYPE4S = "1";
		public static final String ORIGINAL = "2";
		public static final String BRAND = "3";
		public static final String APPLY = "4";
		
	}

	/** 是否联共保案件 */
	public static class GBFlag{

		public static final String MAJORCOMMON = "1";// 主共
		public static final String MINORCOMMON = "2";// 从共
		public static final String MAJORRELATION = "3";// 主联
		public static final String MINORRELATION = "4";// 从联
	}

	/**
	 * 系统配置表codekey
	 * @author ★Lundy
	 */
	public static class SysConfigCodeKey {

		public static final String PLATFORM_ERRORCODE_VIN = "platform.errorcode.vin";
	}

	
	// 代查勘标识，0-正常案件，1-司内代查勘，2-公估代查勘，3-公估案件
	public static class subCheckFlag{
		public static final String  normalCase = "0";
		public static final String  internalSurvey = "1";
		public static final String  publiAssessment = "2";
		public static final String  publiAssessmentCase = "4";
	}

	// 审核状态
	public static class  CheckStatus{

		public static final String regist = "4"; // 登记
		public static final String submit = "5"; // 提交上级
		public static final String Pass = "6"; // 审核通过
		public static final String noPass = "7";// 审核不通过
		public static final String returnModify = "8";// 退回修改
	}
	
	/**
	 * 银行账户类型，1-个人账户；2-公司账户
	 * @author wurh
	 */
	public static class Acctype{
		public static final String  personal = "1";
		public static final String  company = "2";
	}
	
	/**
	 * 短信模板的案件类型
	 * @author wurh
	 */
	public static class CaseType{
		public static final String  normal = "1";
		public static final String  selfClaim = "2";
		public static final String  repair = "3";
		public static final String  hnQuickCase = "4";
	}
	
	/**
	 * 计算书类型
	 * @author wurh
	 */
	public static class CompensateFlag{
		public static final String  compensate = "N";
		public static final String  prePay = "Y";
		public static final String  padPay = "D";
	}
	
	/**
	 * 短信模板类型
	 */
	public static class ModelType{

		public static final String report = "1";// 报案人
		public static final String dLoss = "11";// 定损员
		public static final String agent = "2";// 代理人
		public static final String insured = "3";// 被保险人
		public static final String pLoss = "4";// 人员伤亡
		public static final String check = "5";// 查勘员
		public static final String payee = "6";// 收款人
		public static final String repair = "7";// 修理厂
		public static final String steal = "8";// 全车盗抢
		public static final String staff = "9";// 工作人员
		/** 自助查勘 */
		public static final String autocheck = "12";
	}
	
	/**
	 * 短信模板发送节点
	 */
	public static class SystemNode{

		public static final String report = "1";// 报案
		public static final String prePay = "10";// 预付
		public static final String padPay = "11";// 垫付
		public static final String addCheck = "2";// 查勘调度新增
		public static final String reassign = "3";// 调度改派
		public static final String addDLoss = "4";// 定损调度新增
		public static final String vLoss = "5";// 车辆核损
		public static final String check = "6";// 查勘
		public static final String reportCancel = "7";// 报案注销
		public static final String endCase = "8";// 结案
		public static final String addPLoss = "9";// 人伤调度新增
	}


	/**
	 * 公共通用枚举值
	 * @author ★Lundy
	 */
	public static class CommonConst {

		// 假false、无效、否
		public static final String FALSE = "0";
		// 真true、有效、是
		public static final String TRUE = "1";
		// N、否
		public static final String NO = "N";
		// Y、是
		public static final String YES = "Y";
	}
	
	/**
	 * 平台定时任务状态
	 * @author wurh
	 *
	 */
	public static class platformStatus{
		/** 失败 */
		public static final String Failure = "0";
		/** 成功 */
		public static final String Success = "1";
		/** 失效 */
		public static final String Cancel = "2";
		/** 未上传 */
		public static final String None = "3";
	}
	
	/**
	 * 任务执行状态
	 * @author wurh
	 *
	 */
	public static class OperateStatus{
		/** 繁忙 */
		public static final String ON = "on";
		/** 空闲 */
		public static final String OFF = "off";
	}
	
	/**
	 * 全国平台任务等级
	 * @author wurh
	 *
	 */
	public static class PlatformTaskLevel{
		/** 报案 */
		public static final int Regist=1;
		/** 查勘 */
		public static final int Check=2;
		/** 立案 */
		public static final int Claim=3;
		/** 核损 */
		public static final int Loss=4;
		/** 单证 */
		public static final int Certify=5;
		/** 核赔 */
		public static final int VClaim=6;
		/** 结案 */
		public static final int EndCase=7;
		/** 赔款支付 */
		public static final int Payment=8;
		/** 重开 */
		public static final int ReOpen=9;
		/** 注销 */
		public static final int Cancel=10;
	}
	
	/**
	 * 上海平台任务等级
	 * @author wurh
	 *
	 */
	public static class PlatformTaskLevel_SH{
		/** 报案 */
		public static final int Regist=1;
		/** 查勘 */
		public static final int Check=2;
		/** 立案 */
		public static final int Claim=3;
		/** 核损 */
		public static final int Loss=4;
		/** 单证 */
		public static final int Certify=5;
		/** 核赔 */
		public static final int VClaim=6;
		/** 结案 */
		public static final int EndCase=7;
		/** 赔款支付 */
		public static final int Payment=8;
		/** 重开 */
		public static final int ReOpen=9;
		/** 结案追加 */
		public static final int EndCaseAdd=10;
		/** 注销 */
		public static final int Cancel=11;
	}


	public static class CarType{

		public static final String trailer = "011";// 特种车
		public static final String specialVehicle = "016";// 特种车
	}
	
	public static class oldClaimType{

		public static final String oldClaim = "oldClaim";// 旧理赔标识
	}
	
	public static class PayReason{

		/** 代付 追偿 */
		public static final String INSTEAD_PAY_Res = "P6B";
		/** 清付 */
		public static final String CLEAR_PAY_Res = "P6D";
		/** 自付 */
		public static final String COMPENSATE_PAY_Res = "P60";
		
		/**付查勘费 */
		public  static final String CHECKFEE_PAY_Res = "P62";
	}

	/**
	 * 收付退票的错误类型
	 * 
	 * <pre></pre>
	 * @author ★Wurh
	 */
	public static class ErrorType {

		/** 退票 */
		public static final String back = "6";
		/** 支付失败 */
		public static final String payFailure = "3";
	}

	
	public static class ResultAmount{
		
		/** 报案页面查询总数 */
		public static final String registResultAmt = "resultcount";
		/** 保单页面查询总数*/
		public static final String policyResultAmt = "policyResultCount";
	}
	
	
	public static HashMap<String,String> TransInvoiceTypeMap = new HashMap<String,String>();
	static{
		TransInvoiceTypeMap.put("000","待补票");// 待补票
		TransInvoiceTypeMap.put("004","增值税专用发票");// 增值税专用发票
		TransInvoiceTypeMap.put("007","增值税普通发票");// 增值税普通发票
		TransInvoiceTypeMap.put("999","其他有效凭证");// 其他有效凭证
		TransInvoiceTypeMap.put("001","红字增值税专用发票");// 红字增值税专用发票
		TransInvoiceTypeMap.put("002","红字增值税普通发票");// 红字增值税普通发票
		TransInvoiceTypeMap.put("010","代扣代缴完税凭证");// 代扣代缴完税凭证
	}
	
	public static class InvoiceType{
		/** 待补票 */
		public static final String STAYTICKET = "000";
		/** 增值税专用发票*/
		public static final String SPECIALTICKET = "004";
		/** 增值税普通发票*/
		public static final String COMMONTICKET = "007";
		/** 其他有效凭证*/
		public static final String OTHERTICKET = "999";
		/** 红字增值税专用发票*/
		public static final String REDSPECIALTICKET = "001";
		/** 红字增值税普通发票*/
		public static final String REDCOMMONTICKET = "002";
		/** 代扣代缴完税凭证*/
		public static final String WITHHOLDINGTICKET = "010";
	}
	
	public static class VatFeeType{
		/** 赔款 */
		public static final String CPAY = "37";
		public static final String YPAY = "38";
		public static final String DPAY = "39";
	}
	
	public static HashMap<String,String> TransVatFeeTypeMap = new HashMap<String,String>();
	static{
		TransVatFeeTypeMap.put("37","赔款");
		TransVatFeeTypeMap.put("38","预付赔款");
		TransVatFeeTypeMap.put("39","垫付赔款");
	}
	
	public static class CertiType{
		/** 理算 */
		public static final String COMPENSATE = "C";
		/** 预付 */
		public static final String PREPAY = "Y";
	}
	
	//币别
	public static class Currency{
		/** 人民币 */
		public static final String CNY = "CNY";
	}
	
	//系统标识
	public static class SystemCode{
		public static final String NEWCLAIM = "newClaim";
	}
	
	public static HashMap<String,String> TransPayObjectKindMap = new HashMap<String,String>();
	static{
		TransPayObjectKindMap.put("1","投保人");
		TransPayObjectKindMap.put("2","被保险人");
		TransPayObjectKindMap.put("3","责任保险第三者");
		TransPayObjectKindMap.put("4","公估机构");
		TransPayObjectKindMap.put("5","其他收款人");
		TransPayObjectKindMap.put("6","修理厂");
		TransPayObjectKindMap.put("7","医院（法院）");
		TransPayObjectKindMap.put("8","律师所");
	}
	//收款人类型
	public static class PayObjectKind{
		/** 投保人 */
		public static final String POLICYHOLDER = "1";
		/** 被保险人 */
		public static final String INSURED = "2";
		/** 责任保险第三者 */
		public static final String THIRDPARTY = "3";
		/** 公估机构 */
		public static final String ASSESSMENT = "4";
		/**其他收款人 */
		public static final String OTHER = "5";
		/** 修理厂 */
		public static final String GARAGE = "6";
		/** 医院（法院） */
		public static final String HOSPITAL = "7";
		/** 律师所 */
		public static final String LAWYER = "8";
		
	}
	/** 核赔类型 **/
	public static class VClaimName{
		public static final String VC_ADOPT = "核赔通过";
	}
	/** 查勘类型 **/
	public static class CheckType {

		/** 自助查勘 */
		public static final String Self = "1";
		/** 快处快赔查勘 */
		public static final String quick = "2";
		/** 现场查勘 */
		public static final String Scene = "3";
		/** 约定查勘 */
		public static final String Agreed = "4";
	}
	public static class ErrorCode {
		public static final String CIRegistRepeat = "9514";
		public static final String BIRegistRepeat = "9547";
	}
	/**
	 * 计算书案件类型
	 * @author WLL
	 */
	public static class CompCaseType{
		/**1-正常案件**/
		public static final String  NORMAL_CASE = "1";
		/**2-互碰自赔**/
		public static final String  SELF_CASE = "2";
		/**3-代位求偿**/
		public static final String  DW_CASE = "3";
	}
	/**
	 * 单证拒赔原因
	 * @author WLL
	 */
	public static class NotPayCause{
		/**倒签单	1**/
		public static final String  DQD = "1";
		/**故意行为	2**/
		public static final String  GYXW = "2";
		/**虚假材料	3**/
		public static final String  XJCL = "3";
		/**证件无效	4**/
		public static final String  ZJWX = "4";
		/**其它	5**/
		public static final String  QT = "5";
	}
	
	public static class PropType {
		/**	1	财产损失**/
		public static final String PROPLOSS = "1"; 
		/**	2	其他损失（附加险）**/
		public static final String OTHLOSS = "9"; 
	
	}
	
	
	/**
	 * 邮件报文的头部
	 */
	public static final String MailXML_Head ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:impl=\"http://impl.service/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body> <impl:sendMailFrom>";
	/**
	 * 邮件报文的尾部
	 */
	public static final String MailXML_End = "</impl:sendMailFrom> </soapenv:Body> </soapenv:Envelope>";
	/**
	 * 大案邮件列表的头部
	 */
	public static final String MailTable_Head = "已发送邮件：\n	<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:100%;\" >\n   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >      <tr>\n         <td>收件人</td>\n         <td>邮箱</td>\n      </tr>\n   <thead/>\n   <tbody>\n";
	/**
	 * 大案邮件列表的尾部
	 */
	public static final String MailTable_End = "   </tbody>\n</table>";
	
	public static class Mail_CaseType{
		/** 人伤 */
		public static final String PLoss = "1";
		/** 车物 */
		public static final String DLoss= "2";
	} 
	public static class ResponseCode{
		public static final String RESPONSESUCCESSCODE = "200";
		public static final String RESPONSEERRORCODE = "400";
	}
	
	/** 损失方 */
	public static class LossItemParty{

		/** 车外人员  */
		public static final String OUTSIDE_CAR_PERSON = "1";
		/** 本车乘客 */
		public static final String THIS_CAR_PASSAGER = "2";
	
		/** 本车司机 */
		public static final String THIS_CAR_DRIVER = "3";
	}
	/**
	 * 
	 * 归属机构开头
	 * @author ★XHY
	 */
	public static class comCodeBegin{
		public static final String headOfficeBegin1 = "0000";
		
		public static final String headOfficeBegin2 = "0001";
		
		public static final String ShenzhenBegin = "0002";
	}

	/**
	 * 总公司审核等级
	 * @author maofengning
	 */
	public static class TopOfficeVerifyLevel {
		/** 总公司审核九级 */
		public static final int LEVEL9 = 9;
		/** 总公司审核十级 */
		public static final int LEVEL10 = 10;
		/** 总公司审核十一级 */
		public static final int LEVEL11 = 11;
		/** 总公司审核十二级 */
		public static final int LEVEL12 = 12;
	}

	/**
	 * 国标行政编码等级模式
	 * @author maofengning
	 */
	public static class AreaCodeModel {
		/**
		 * 国标省级编码
		 */
		public static final int GB_PROVINCECODE = 1;

		/**
		 * 国标市级编码
		 */
		public static final int GB_CITYCODE = 2;
	}

	/**
	 * 交强险核赔岗位代码及其对应的审核等级
	 * 1~12 分别表示核赔一级~核赔十二级
	 */
	public static final Map<String, Integer> VCLAIM_CI_MAP = new HashMap<String, Integer>() {
		{
			put("5134", 1); // 交强险核赔一级
			put("5135", 2);
			put("5136", 3);
			put("5137", 4);
			put("5138", 5);
			put("5139", 6);
			put("5140", 7);
			put("5141", 8);
			put("5142", 9);
			put("5143", 10);
			put("5144", 11);
			put("5145", 12); // 交强险核赔十二级
		}
	};

	/**
	 * 商业险核赔岗位代码及其对应的审核等级
	 * 1~12 分别表示核赔一级~核赔十二级
	 */
	public static final Map<String, Integer> VCLAIM_BI_MAP = new HashMap<String, Integer>() {
		{
			put("5146", 1); // 商业险核赔一级
			put("5147", 2);
			put("5148", 3);
			put("5149", 4);
			put("5150", 5);
			put("5151", 6);
			put("5152", 7);
			put("5153", 8);
			put("5154", 9);
			put("5155", 10);
			put("5156", 11);
			put("5157", 12); // 商业险核赔十二级
		}
	};

	/**
	 * 车辆核损岗位代码及其对应的审核等级
	 * 1~12 车辆核损一级~车辆核损十二级
	 */
	public static final Map<String, Integer> VLOSS_MAP = new HashMap<String, Integer>() {
		{
			put("5052", 1); // 车辆核损一级
			put("5053", 2);
			put("5054", 3);
			put("5055", 4);
			put("5056", 5);
			put("5057", 6);
			put("5058", 7);
			put("5059", 8);
			put("5060", 9);
			put("5061", 10);
			put("5062", 11);
			put("5063", 12); // 车辆核损十二级
		}
	};
	
	/**
	 * 车辆核价岗位代码及其对应的审核等级
	 * 1~12 车辆核价一级~车辆核价十二级
	 */
	public static final Map<String, Integer> VPCAR_MAP = new HashMap<String, Integer>() {
		{
			put("5039", 1); // 车辆核价一级
			put("5040", 2);
			put("5041", 3);
			put("5042", 4);
			put("5043", 5);
			put("5044", 6);
			put("5045", 7);
			put("5046", 8);
			put("5047", 9);
			put("5048", 10);
			put("5049", 11);
			put("5050", 12); // 车辆核价一级
		}
	};

	public static final Map<String, Map<String, Integer>> TASKNODEMAP = new HashMap<String, Map<String, Integer>>() {
		{
			put("VLCar", VLOSS_MAP);
			put("VClaim_CI", VCLAIM_CI_MAP);
			put("VClaim_BI", VCLAIM_BI_MAP);
		 	put("VPCar", VPCAR_MAP);
		}
	};
	//总赔款key
    public static final String ALLKINDSUMREALPAY="All";
    
    //公估费用类型
    public static final String CHARGECODE_13 = "13";
    
    /**
     * 返回本系统的费用名称
     * @param feeCode
     * @return
     */
    public static String backFeeName(String feeCode){
    	String chargeName="其它";
    	if("06".equals(feeCode)){
    		chargeName="律师费";
    	}else if("07".equals(feeCode)){
    		chargeName="诉讼费";
    	}else if("08".equals(feeCode)){
    		chargeName="代查勘费";
    	}else if("13".equals(feeCode)){
    		chargeName="公估费";
    	}else if("14".equals(feeCode)){
    		chargeName="奖励费";
    	}else if("15".equals(feeCode)){
    		chargeName="检测费";
    	}else if("16".equals(feeCode)){
    		chargeName="咨询顾问费";
    	}else if("17".equals(feeCode)){
    		chargeName="差旅费";
    	}else if("18".equals(feeCode)){
    		chargeName="鉴定费";
    	}else if("37".equals(feeCode)){
    		chargeName="实赔(修理厂)";
    	}else if("38".equals(feeCode)){
    		chargeName="预付赔款(修理厂)";
		}
		return chargeName;
	}
	
	/**
	 * 平安联盟保单号前缀
	 * @author maofengning
	 */
	public static class PINGAN_UNION {
		/** 保单号 */
		public static final String POLICY_PREFIX = "B";

		/** 平安-死亡伤残责任代码 */
		public static final String DUTYCODE_DEATH = "CV37045";
		/** 平安-医疗责任代码 */
		public static final String DUTYCODE_MEDICAL = "CV38046";
		/** 平安-财产损失责任代码 */
		public static final String DUTYCODE_PROPERTY = "CV39047";

	}

	/**
	 * 平安联盟保单号前缀
	 * @author maofengning
	 */
	public static class NEW2020KIND {
		/** 保单号 */
		public static final String kind1230= "1230";
		/** 平安-死亡伤残责任代码 */
		public static final String kind1231= "1231";
		/** 平安-医疗责任代码 */
		public static final String kind1232 = "1232";
		/** 平安-财产损失责任代码 */
		public static final String kind1233 = "1233";

	}
	
	/**
	 * 新险种
	 * @author Administrator
	 *  1230 1231 1232 1233
	 */
	public static class New_RISKCODE {
		public static final String NEW_RISK = "1230,1231,1232,1233";
	}

	/**
	 * 映射反欺诈证件类型，
	 */
	public static HashMap<String,String> identifyMap = new HashMap<String,String>();
	static{
		identifyMap.put("1","01");
		identifyMap.put("2","02");
		identifyMap.put("3","03");
		identifyMap.put("4","04");
		identifyMap.put("5","05");
		//identifyMap.put("","06");
		identifyMap.put("7","07");
		identifyMap.put("8","08");
		identifyMap.put("41","41");
		identifyMap.put("42","42");
		identifyMap.put("43","43");
		identifyMap.put("51","51");
		identifyMap.put("52","52");
		identifyMap.put("53","53");
		identifyMap.put("10","71");
		identifyMap.put("72","72");
		identifyMap.put("73","73");

	}

    /**
     * 返回本vat的费用编码
     * @param feeCode
     * @return
     */
    public static String backvatFeeType(String feeCode){
    	String chargeCode="99";//其它
    	if("06".equals(feeCode)){
    		chargeCode="23";//律师费
    	}else if("07".equals(feeCode)){
    		chargeCode="24";//诉讼费
    	}else if("08".equals(feeCode)){
    		chargeCode="06";//代查勘费
    	}else if("13".equals(feeCode)){
    		chargeCode="13";//公估费
    	}else if("14".equals(feeCode)){
    		chargeCode="25";//奖励费
    	}else if("15".equals(feeCode)){
    		chargeCode="07";//检测费
    	}else if("16".equals(feeCode)){
    		chargeCode="16";//咨询顾问费
    	}else if("17".equals(feeCode)){
    		chargeCode="17";//差旅费
    	}else if("18".equals(feeCode)){
    		chargeCode="26";//鉴定费
    	}else if("37".equals(feeCode)){
    		chargeCode="37";//实赔(修理厂)
    	}else if("38".equals(feeCode)){
    		chargeCode="38";//预付赔款(修理厂)
    	}else if("04".equals(feeCode)){
			chargeCode="04";//查勘费
		}
    	return chargeCode;
    }
    /**
	 * 返回本系统的费用编码
	 * @param feeCode
	 * @return
	 */
    public static String backFeeType(String feeCode){
		String chargeCode="99";//其它
		if("23".equals(feeCode)){
			chargeCode="06";//律师费
		}else if("24".equals(feeCode)){
			chargeCode="07";//诉讼费
		}else if("06".equals(feeCode)){
			chargeCode="08";//代查勘费
		}else if("13".equals(feeCode)){
			chargeCode="13";//公估费
		}else if("25".equals(feeCode)){
			chargeCode="14";//奖励费
		}else if("07".equals(feeCode)){
			chargeCode="15";//检测费
		}else if("16".equals(feeCode)){
			chargeCode="16";//咨询顾问费
		}else if("17".equals(feeCode)){
			chargeCode="17";//差旅费
		}else if("26".equals(feeCode)){
			chargeCode="18";//鉴定费
		}else if("37".equals(feeCode)){
			chargeCode="37";//实赔(修理厂)
		}else if("38".equals(feeCode)){
			chargeCode="38";//预付赔款(修理厂)
		}
		return chargeCode;
	}
    /**
     * 返回业务类型名称
     * @param bussType
     * @return
     */
    public static String backBussName(String bussType){
    	String bussName="";
    	if("0".equals(bussType)){
    		bussName="赔款";
    	}else if("1".equals(bussType)){
    		bussName="费用";
    	}
    	return bussName;
    }
}
