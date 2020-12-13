package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HISCLAIMINFO")
public class HisclaimInfoVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 交强险保单号
	 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/*
	 * 商业险保单号
	 */
	@XStreamAlias("BUSIPOLICYNO")
	private String busipolicyNo;
	
	/*
	 * 历史出险次数 
	 */
	@XStreamAlias("HISTORICALACCIDENT")
	private String historicalAccident;

	/*
	 * 历史赔款次数
	 */
	@XStreamAlias("HISTORICALCLAIMTIMES")
	private String historicalClaimTimes;
	
	/*
	 * 历史赔款总计
	 */
	@XStreamAlias("HISTORICALCLAIMSUM")
	private String historicalClaimSum;
	
	/*
	 * 数据部分
	 */
	@XStreamAlias("HISCLAIMLIST")
	private List<HisclaimListVo> hisclaimList;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getBusipolicyNo() {
		return busipolicyNo;
	}

	public void setBusipolicyNo(String busipolicyNo) {
		this.busipolicyNo = busipolicyNo;
	}

	public String getHistoricalAccident() {
		return historicalAccident;
	}

	public void setHistoricalAccident(String historicalAccident) {
		this.historicalAccident = historicalAccident;
	}

	public String getHistoricalClaimTimes() {
		return historicalClaimTimes;
	}

	public void setHistoricalClaimTimes(String historicalClaimTimes) {
		this.historicalClaimTimes = historicalClaimTimes;
	}

	public String getHistoricalClaimSum() {
		return historicalClaimSum;
	}

	public void setHistoricalClaimSum(String historicalClaimSum) {
		this.historicalClaimSum = historicalClaimSum;
	}

	public List<HisclaimListVo> getHisclaimList() {
		return hisclaimList;
	}

	public void setHisclaimList(List<HisclaimListVo> hisclaimList) {
		this.hisclaimList = hisclaimList;
	}
	
	
	
}
