/************************************************************************
 * Description: PICC鏂扮悊璧旂郴缁熼潤鎬佸父閲忕被
 * Author : phillips
 * CreateDate : 2007-7-28
 * UpdateLog : Name Date Reason/Content
 * ------------------------------------------------------------
 ************************************************************************/
package ins.platform.saa.util;

public final class CodeConstants {
	public static final String TOP_USERCODE = "0000000000";
	public static final String TOP_MANAGER = "admin";
	public static final String TOP_COMPANY = "00000000";
	public static final String PLATFORM_TIMES = "PLATFORM_TIMES";//最大上传平台的次数
	public static final String PLATFORM_COUNT = "PLATFORM_COUNT";//自动上传平台执行的最大案件数量
	
	public static final String noCache = "noCache";
	// inputLoad下拉框选项的最大查询数
	public static final int TOP_LIMIT = 30;
	public static final String DEMILITER = "|";
	public static final String VALID = "1";
	public static final String INVALID = "0";
	public static class LicenseType {
		public static final String largePolicyCar = "27";
		public static final String otherCar = "24";
		public static final String guardMotorcycle ="26";
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
	
	public static class ClaimTypeCIPla {
		/**1	正常理赔**/
		public static final String Nomal_case = "1";
		/**2	无责赔案**/
		public static final String Noduty_case = "2";
		/**3	垫付赔案**/
		public static final String PadPay_case = "3";
		/**4	垫付救助基金**/
		public static final String PadPayRelief_case = "4";
		/**9	其它**/
		public static final String Other_case = "9";
	}
	public static class AccLiabilityCIPla {
		/**	全部责任**/
		public static final String FULL_DUTY = "1"; // 全责
		/**	主要责任**/
		public static final String MAIN_DUTY = "2"; // 主责
		/**	同等责任**/
		public static final String EQUAL_DUTY = "3"; // 同责
		/**	次要责任**/
		public static final String SECOND_DUTY = "4"; // 次责
		/**	无责任**/
		public static final String NO_DUTY = "5"; // 无责
		/**	责任未确定**/
		public static final String NOTSURE_DUTY = "9"; 
	}
	public static class ClaimFeeTypeCIPla {
		/**	1	死亡伤残**/
		public static final String DEATHLOSS = "1"; 
		/**	2	医疗费用**/
		public static final String MEDILOSS = "2"; 
		/**	3	财产损失**/
		public static final String PROPLOSS = "3"; 
		/**	5	无责死亡伤残**/
		public static final String DEATHLOSS_NODUTY = "5"; 
		/**	6	无责医疗费用**/
		public static final String MEDILOSS_NODUTY = "6"; 
		/**	7	无责财产损失**/
		public static final String PROPLOSS_NODUTY = "7"; 
	
	}
	
	public static class ClaimFeeTypeBIPla {
		/**	1	车辆损失**/
		public static final String CARLOSS = "1"; 
		/**	2	财产损失**/
		public static final String PROPLOSS = "2"; 
		/**	5	死亡伤残**/
		public static final String DEATHLOSS = "5"; 
		/**	6	医疗费用**/
		public static final String MEDHLOSS = "6"; 
	}
	
	public static class RecoveryOrPayTypeBIPla {
		/**	1	交强险**/
		public static final String CARLOSS = "1"; 
		/**	2	商业三者险**/
		public static final String PROPLOSS = "2"; 
		/**	3	致害人**/
		public static final String DEATHLOSS = "3"; 
		
	}

}
