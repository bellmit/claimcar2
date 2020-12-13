/******************************************************************************
* CREATETIME : 2015年11月26日 上午11:44:13
******************************************************************************/
package ins.sino.claimcar.check.util;
/**
 * <pre></pre>
 * @author ★Luwei
 */
public final class CheckConst { 
	public static final String VALIDFLAG_Y="1";
	public static final String VALIDFLAG_N="0";
	
	public static final String CIINDEMDUTY_Y="100";
	public static final String CIINDEMDUTY_N="0";
	
	/**调度发起查勘任务中损失项类型*/
	public static final String SCHEDULEITEMTYPE_MAINCAR="1";//主车
	public static final String SCHEDULEITEMTYPE_THIRDCAR="2";//三者车
	public static final String SCHEDULEITEMTYPE_PROP="3";//财产
	public static final String SCHEDULEITEMTYPE_ROBBERY="5";//盗抢
	
	/**工作流状态*/
	public static final String WORKFLOWSTATE_NEW="0";//未处理
	public static final String WORKFLOWSTATE_DOING="2";//正在处理
	public static final String WORKFLOWSTATE_FINISH="1";//已处理
	
	/** 商业交强险标识 */
	public static final String POLICYTYPEFLAG_BUSINESS="1";//商业
	public static final String POLICYTYPEFLAG_CI="2";//交强
	public static final String POLICYTYPEFLAG_BOTH="3";//商业+交强
	
	/** 查勘性质 */
	public static final String CHECKNATURE_SCENE="1"; 
	
	/**损失类别*/
	public static final String LOSSTYPE_NOTALLLOSS="01";
	
	/**承保情况*/
	public static final String INSURED_BOTH="3";//混保
	public static final String INSURED_BI="1";//商业
	public static final String INSURED_CI="2";//交强
	
	/** 获得被保险人/受益人账户信息 和 索赔经办人详细信息 */
	public static final String PRPLPERSONCOMPANYINFO_INSURANT="2";//被保险人
	public static final String PRPLPERSONCOMPANYINFO_CLAIMPEOPLE="3";//索赔经办人
	public static final String PRPLPERSONCOMPANYINFO_BENEFIT="4";//受益人
}
