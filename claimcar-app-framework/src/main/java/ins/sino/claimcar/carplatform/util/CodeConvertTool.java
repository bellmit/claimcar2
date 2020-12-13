package ins.sino.claimcar.carplatform.util;
import ins.platform.saa.util.CodeConstants.LicenseType;

import java.math.BigDecimal;

/**
 * 平台代码转换工具
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-6-26
 * @since (2014-6-26 下午07:47:00): <br>
 */
public class CodeConvertTool {

	/**
	 * 车辆种类代码转换
	 * @param carType
	 * @param exhaustScale
	 * @param tonCount
	 * @param seatCount
	 * @return
	 * @author ☆HuangYi(2014-6-26 下午07:49:34): <br>
	 */
	public static String getVehicleCategory(String carType,BigDecimal exhaustScale,BigDecimal tonCount) {
		String vehicleCategory = null;
		if("011".equals(carType)&&tonCount!=null){
			int tonCountInt = tonCount.intValue();
			if(tonCountInt>=0&&tonCountInt<2000){// 二吨以下挂车
				vehicleCategory = "25";
			}else if(tonCountInt>=2000&&tonCountInt<5000){// 二吨至五吨挂车
				vehicleCategory = "26";
			}else if(tonCountInt>=5000&&tonCountInt<10000){// 五吨至十吨挂车
				vehicleCategory = "27";
			}else if(tonCountInt>=10000){ // 十吨以上挂车
				vehicleCategory = "28";
			}
		}else if("016".equals(carType)&&exhaustScale!=null){
			int exhaustScaleInt = exhaustScale.intValue();
			if(exhaustScaleInt==1){
				vehicleCategory = "31";
			}else if(exhaustScaleInt==2){
				vehicleCategory = "41";
			}else if(exhaustScaleInt==3){
				vehicleCategory = "51";
			}
		}
		return vehicleCategory;
	}

	/**
	 * 号牌种类代码转换
	 * @param  licenseType
	 * @return licenseType
	 */
	public static String getVehicleCategory(String licenseType) {
		if(LicenseType.largePolicyCar.equals(licenseType) || LicenseType.otherCar.equals(licenseType)){//号牌种类为大型警车
			licenseType = "25";
		} else if(LicenseType.guardMotorcycle.equals(licenseType)){
			 licenseType = "24";
		}
		return licenseType;
	}

	/**
	 * 车辆使用性质代码（依据条款）
	 * @param useNatureCode
	 * @param carKindCode
	 */
	public static String getUseType(String useKindCode,String carType,String carAttachNature) {
		String retnCode = null;
		if("001,002,003,004,005".indexOf(carType)> -1){// 客车
			if("003".equals(useKindCode)){
				retnCode = "101";
			}else if("004".equals(useKindCode)){
				retnCode = "102";
			}else if("005".equals(useKindCode)){
				retnCode = "103";
			}else if("001".equals(useKindCode)){// 不符合业务规则

			}else if("002".equals(useKindCode)){
				if("001".equals(carAttachNature)){
					retnCode = "210";// 非营业个人
				}else if("002".equals(carAttachNature)){
					retnCode = "220";// 非营业企业
				}else if("003".equals(carAttachNature)){
					retnCode = "230";// 非营业机关
				}
			}else if("008".equals(useKindCode)){
				retnCode = "100";
			}else if("009".equals(useKindCode)){
				retnCode = "200";
			}
		}else if("006,007,008,009,010,011".indexOf(carType)> -1){// 货车、挂车、油罐挂车
			if("002,009".indexOf(useKindCode)> -1){// 非营业
				retnCode = "200";
			}else{
				retnCode = "100";
			}
		}else if("012,013,014,015,016,017,018,019,040,041,042".indexOf(carType)> -1){// 特种车一, 特种车二, 特种车三, 特种车四
			if("002,009".indexOf(useKindCode)> -1){// 非营业
				retnCode = "200";
			}else{
				retnCode = "100";// 营业
			}
		}
		return retnCode==null ? "000" : retnCode;// 000 不区分营业非营业
	}

	/**
	 * 四川车辆使用性质代码（依据条款）
	 * @param useKindCode
	 * @param carType
	 * @param carAttachNature
	 * @param comCode
	 * @param riskCode
	 * @return
	 * @modified: ☆qianxin(2016年4月11日 下午2:57:24): <br>
	 */
	public static String getUseTypeBranch(String useKindCode,String carType,String carAttachNature) {
		if("010".equals(carType)){
			// 分省平台交强险车辆种类93只有对应的唯一使用性质为000-不区分营业非营业的费率
			return "000";
		}
		return getUseType(useKindCode,carType,carAttachNature);
	}

	/**
	 * 车辆使用性质代码（示范条款）
	 * @param useKindCode
	 * @param carType
	 * @param carAttachNature
	 * @return
	 * @modified: ☆qianxin(2015年3月17日 下午6:00:08): <br>
	 */
	public static String getUseTypePremium(String useKindCode,String carType,String carAttachNature) {
		String retnCode = null;
		if("001,002,003,004,005".indexOf(carType)> -1){// 客车
			if("003".equals(useKindCode)){
				retnCode = "101";
			}else if("004".equals(useKindCode)){
				retnCode = "102";
			}else if("005".equals(useKindCode)){
				retnCode = "103";
			}else if("001".equals(useKindCode)){// 不符合业务规则

			}else if("002".equals(useKindCode)){
				if("001".equals(carAttachNature)){
					retnCode = "210";// 非营业个人
				}else if("002".equals(carAttachNature)){
					retnCode = "220";// 非营业企业
				}else if("003".equals(carAttachNature)){
					retnCode = "230";// 非营业机关
				}
			}else if("008".equals(useKindCode)){
				retnCode = "100";
			}else if("009".equals(useKindCode)){
				retnCode = "200";
			}
		}else if("006,007,008,009,010".indexOf(carType)> -1){// 货车、油罐挂车
			if("002,009".indexOf(useKindCode)> -1){// 非营业
				retnCode = "240";
			}else{
				retnCode = "104";// 营业货车
			}
		}else if("011".indexOf(carType)> -1){// 挂车
			if("002,009".indexOf(useKindCode)> -1){// 非营业
				retnCode = "250";// 非营业挂车
			}else{
				retnCode = "106";// 营业挂车
			}
		}else if("020,021".indexOf(carType)> -1){
			retnCode = "310";// 兼用型（拖拉机专用）
		}else if("038,039".indexOf(carType)> -1){
			retnCode = "320";// 运输型（拖拉机专用）
		}else if("012,013,014,015,016,040,041,042".indexOf(carType)> -1){// 特种车一, 特种车二, 特种车三, 特种车四
			if("002,009".indexOf(useKindCode)> -1){// 非营业
				retnCode = "200";
			}else{
				retnCode = "100";// 营业
			}
		}
		return retnCode==null ? "000" : retnCode;// 000 不区分营业非营业
	}

	/**
	 * 证件类型代码转换
	 * @param strInsuredType
	 * @param strIdentifyType
	 * @return
	 * @author ☆HuangYi(2014-6-30 下午07:07:37): <br>
	 */
	// public static String getCertiType(String insuredType,String identifyType) {
	// String strCertiType = "";
	// if("2".equals(insuredType)){// 团体
	// strCertiType = "10";
	// }else{// 个人
	// if("01".equals(identifyType)){
	// strCertiType = "01";
	// }else if("03".equals(identifyType)){
	// strCertiType = "02";
	// }else if("04".equals(identifyType)){
	// strCertiType = "03";
	// }else{
	// strCertiType = "99";
	// }
	// }
	// return strCertiType;
	//
	// }

	/**
	 * 获取险种性质代码
	 * @param coverageCode
	 * @return
	 * @modified: ☆qianxin(2014年7月4日 下午4:18:49): <br>
	 */
	public static String getCoverageClassCode(String coverageCode) {
		String strCoverageClassCode = "";
		if("A,B,D11,D12,G,T1".indexOf(coverageCode)> -1){
			strCoverageClassCode = "1";
		}else{
			strCoverageClassCode = "2";
		}
		return strCoverageClassCode;
	}

}
