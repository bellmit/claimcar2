package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class HistoricalClaimInfoResBodyVo  implements Serializable{
	
	/**
	 * 数据部分
	 */
	@XStreamAlias("HISCLAIMINFO")
	private HisclaimInfoVo hisclaimInfo;

	public HisclaimInfoVo getHisclaimInfo() {
		return hisclaimInfo;
	}

	public void setHisclaimInfo(HisclaimInfoVo hisclaimInfo) {
		this.hisclaimInfo = hisclaimInfo;
	}


	
}
