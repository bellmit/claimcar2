/******************************************************************************
* CREATETIME : 2016年6月28日 下午2:36:11
******************************************************************************/
package ins.platform.saa.vo;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月28日
 */
public class SaaFactorPowerVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String factorCode;
	private String userCode;
	private String dataType;
	private String dataOper;
	private String dataValue;
	private String systemCode;

	public String getFactorCode() {
		return factorCode;
	}

	public void setFactorCode(String factorCode) {
		this.factorCode = factorCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataOper() {
		return dataOper;
	}

	public void setDataOper(String dataOper) {
		this.dataOper = dataOper;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
}
