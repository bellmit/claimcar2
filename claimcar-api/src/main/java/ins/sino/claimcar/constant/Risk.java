/******************************************************************************
* CREATETIME : 2014年7月2日 上午10:49:01
******************************************************************************/
package ins.sino.claimcar.constant;

import java.lang.reflect.Field;

/**
 * 系统险种常量
 * @author ★LiuPing
 */
public class Risk {


	/*** 家庭自用汽车保险 */
	public final static String DAA = "1201";
	/*** 特种车保险 */
	public final static String DAE = "1202";
	/*** 摩托车、拖拉机保险 */
	public final static String DAC = "1203";
	/*** 机动车 交强险 */
	public final static String DQZ = "1101";
	/*** 机动车提车保险 */
	public final static String DAT = "1204";
	/*** 机动车辆保险（电销） */
	public final static String DAF = "1205";
	
	/***家庭自用汽车综合商业保险 */
	public final static String DBA = "1206";
	/*** 特种车综合商业保险 */
	public final static String DBE = "1207";
	/*** 摩托车、拖拉机综合商业保险 */
	public final static String DBC = "1208";
	/*** 机动车提车保险 */
	public final static String DBT = "1209";
	/*** 摩托车商业险 */
	public final static String DBD = "1232";
	/*** 单程提车险 */
	public final static String DBG = "1233";

	private static Field[] varNames = null;

	static{
		varNames = new Risk().getClass().getDeclaredFields();
	}

	public static String mapToSys(String paramString) {
		if(paramString==null) return null;
		Object localObject = null;

		if(varNames!=null){
			int i = varNames.length;

			for(int j = 0; j<i; j++ ){
				String str1 = varNames[j].getName();
				String str2 = "";
				try{
					str2 = varNames[j].get(str1).toString();
				}
				catch(Exception localException){
					localException.printStackTrace();
					str2 = null;
				}
				if(paramString.equals(str2)){
					localObject = str1;
					break;
				}
			}

			if(localObject==null){
				localObject = paramString;
			}
		}

		return (String)localObject;
	}

	/**
	 * 是否为 家庭自用汽车保险
	 */
	public static boolean isDAA(String riskCode) {
		return equals(riskCode, DAA);
	}
	
	/**
	 * 是否为 摩托车商业险
	 */
	public static boolean isDBD(String riskCode) {
		return equals(riskCode, DBD);
	}
	/**
	 * 是否为 单程提车险
	 */
	public static boolean isDBG(String riskCode) {
		return equals(riskCode, DBG);
	}
	
	/**
	 * 是否为 家庭自用汽车保险（电销）
	 */
	public static boolean isDAF(String riskCode) {
		return equals(riskCode, DAF);
	}

	/**
	 * 是否为 特种车保险
	 */
	public static boolean isDAE(String riskCode) {
		return equals(riskCode, DAE);
	}

	/**
	 * 是否为 摩托车、拖拉机保险
	 */
	public static boolean isDAC(String riskCode) {
		return equals(riskCode, DAC);
	}

	/**
	 * 是否为 机动车 交强险
	 */
	public static boolean isDQZ(String riskCode) {
		return equals(riskCode, DQZ);
	}

	/**
	 * 是否为 机动车提车保险
	 */
	public static boolean isDAT(String riskCode) {
		return equals(riskCode, DAT);
	}
	
	/**
	 * 是否为 家庭自用汽车综合商业保险
	 */
	public static boolean isDBA(String riskCode) {
		return equals(riskCode, DBA);
	}
	
	/**
	 * 是否为 特种车综合商业保险
	 */
	public static boolean isDBE(String riskCode) {
		return equals(riskCode, DBE);
	}
	
	/**
	 * 是否为 摩托车、拖拉机综合商业保险
	 */
	public static boolean isDBC(String riskCode) {
		return equals(riskCode, DBC);
	}
	
	/**
	 * 是否为 机动车提车保险
	 */
	public static boolean isDBT(String riskCode) {
		return equals(riskCode, DBT);
	}

	/** 险种判断私有方法 **/
	private static boolean equals(String valRisk, String sysRisk) {
		if (valRisk == null) return false;
		return sysRisk.equals(valRisk);
	}

}
