package ins.sino.claimcar.ilog.vclaim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("singleCarLossInfo")
public class SingleCarLossInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *	单车总损失（配件、辅料、工时、外修）
	 */
	@XStreamAlias("singleCarSumloss")
	private String singleCarSumloss;

	public String getSingleCarSumloss() {
		return singleCarSumloss;
	}

	public void setSingleCarSumloss(String singleCarSumloss) {
		this.singleCarSumloss = singleCarSumloss;
	}
	
	

}
